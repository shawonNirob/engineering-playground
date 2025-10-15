package org.build.jsonparser;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

enum TokenType {
    LEFT_BRACE, RIGHT_BRACE, LEFT_BRACKET, RIGHT_BRACKET,
    COLON, COMMA,
    STRING, NUMBER, TRUE, FALSE, NULL,
    EOF
}

class Token {
    TokenType type;
    String value;

    Token(TokenType type, String value) {
        this.type = type;
        this.value = value;
    }

    public String toString() {
        return type + (value != null ? "(" + value + ")" : "");
    }
}

class Lexer {
    private final String text;
    private int pos = 0;

    Lexer(String text) {
        this.text = text;
    }

    private char peek() {
        return pos < text.length() ? text.charAt(pos) : '\0';
    }

    private char next() {
        return pos < text.length() ? text.charAt(pos++) : '\0';
    }

    private void skipWhitespace() {
        while (Character.isWhitespace(peek())) next();
    }

    private String readString() {
        StringBuilder sb = new StringBuilder();
        next();
        while (true) {
            char c = next();
            if (c == '\0') throw new RuntimeException("Unterminated string");
            if (c == '"') break;
            if (c == '\\') {
                char escaped = next();
                switch (escaped) {
                    case '"': sb.append('"'); break;
                    case '\\': sb.append('\\'); break;
                    case '/': sb.append('/'); break;
                    case 'b': sb.append('\b'); break;
                    case 'f': sb.append('\f'); break;
                    case 'n': sb.append('\n'); break;
                    case 'r': sb.append('\r'); break;
                    case 't': sb.append('\t'); break;
                    default: throw new RuntimeException("Invalid escape sequence: \\" + escaped);
                }
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    private String readNumber() {
        StringBuilder sb = new StringBuilder();
        if (peek() == '-') sb.append(next());
        while (Character.isDigit(peek())) sb.append(next());
        if (peek() == '.') {
            sb.append(next());
            while (Character.isDigit(peek())) sb.append(next());
        }
        if (peek() == 'e' || peek() == 'E') {
            sb.append(next());
            if (peek() == '+' || peek() == '-') sb.append(next());
            while (Character.isDigit(peek())) sb.append(next());
        }
        return sb.toString();
    }

    private String readLiteral(String expected) {
        for (char c : expected.toCharArray()) {
            if (next() != c) throw new RuntimeException("Invalid literal: expected " + expected);
        }
        return expected;
    }

    List<Token> tokenize() {
        List<Token> tokens = new ArrayList<>();
        while (true) {
            skipWhitespace();
            char c = peek();
            if (c == '\0') {
                tokens.add(new Token(TokenType.EOF, null));
                break;
            }
            switch (c) {
                case '{': next(); tokens.add(new Token(TokenType.LEFT_BRACE, null)); break;
                case '}': next(); tokens.add(new Token(TokenType.RIGHT_BRACE, null)); break;
                case '[': next(); tokens.add(new Token(TokenType.LEFT_BRACKET, null)); break;
                case ']': next(); tokens.add(new Token(TokenType.RIGHT_BRACKET, null)); break;
                case ':': next(); tokens.add(new Token(TokenType.COLON, null)); break;
                case ',': next(); tokens.add(new Token(TokenType.COMMA, null)); break;
                case '"': tokens.add(new Token(TokenType.STRING, readString())); break;
                case 't': tokens.add(new Token(TokenType.TRUE, readLiteral("true"))); break;
                case 'f': tokens.add(new Token(TokenType.FALSE, readLiteral("false"))); break;
                case 'n': tokens.add(new Token(TokenType.NULL, readLiteral("null"))); break;
                default:
                    if (c == '-' || Character.isDigit(c)) {
                        tokens.add(new Token(TokenType.NUMBER, readNumber()));
                    } else {
                        throw new RuntimeException("Unexpected character: " + c);
                    }
            }
        }
        return tokens;
    }
}

public class JsonParser {
    private static List<Token> tokens;
    private static int current = 0;

    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("Error: No file specified.");
            System.exit(1);
        }

        String filePath = args[0];
        try {
            String text = new String(Files.readAllBytes(Paths.get(filePath)));
            Lexer lexer = new Lexer(text);
            tokens = lexer.tokenize();
            parseValue();
            expect(TokenType.EOF);
            System.out.println("Valid JSON");
            System.exit(0);
        } catch (IOException e) {
            System.err.println("Error: Could not read file '" + filePath + "'");
            System.exit(1);
        } catch (RuntimeException e) {
            System.err.println("Invalid JSON: " + e.getMessage());
            System.exit(1);
        }
    }

    private static Token peek() {
        return tokens.get(current);
    }

    private static Token next() {
        return tokens.get(current++);
    }

    private static void expect(TokenType type) {
        if (peek().type != type)
            throw new RuntimeException("Expected " + type + " but found " + peek().type);
        next();
    }

    private static void parseValue() {
        Token token = peek();
        switch (token.type) {
            case LEFT_BRACE -> parseObject();
            case LEFT_BRACKET -> parseArray();
            case STRING, NUMBER, TRUE, FALSE, NULL -> next();
            default -> throw new RuntimeException("Unexpected token: " + token.type);
        }
    }

    private static void parseObject() {
        expect(TokenType.LEFT_BRACE);
        if (peek().type == TokenType.RIGHT_BRACE) {
            next();
            return;
        }
        while (true) {
            if (peek().type != TokenType.STRING)
                throw new RuntimeException("Expected string key in object.");
            next();
            expect(TokenType.COLON);
            parseValue();
            if (peek().type == TokenType.RIGHT_BRACE) {
                next();
                break;
            }
            expect(TokenType.COMMA);
        }
    }

    private static void parseArray() {
        expect(TokenType.LEFT_BRACKET);
        if (peek().type == TokenType.RIGHT_BRACKET) {
            next();
            return;
        }
        while (true) {
            parseValue();
            if (peek().type == TokenType.RIGHT_BRACKET) {
                next();
                break;
            }
            expect(TokenType.COMMA);
        }
    }
}

