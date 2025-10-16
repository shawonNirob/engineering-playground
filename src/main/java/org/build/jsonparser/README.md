## A Java Implementation of a JSON Parser

### Description
This project is a lightweight, from-scratch JSON parser built in Java. It validates raw JSON text from a file or standard input to confirm it adheres to the official JSON syntax, without using any external libraries.

### Core Features
- **String Parsing:** Correctly handles strings with escape sequences like `\n`, `\t`, `\\`, and `\"`.
- **Number Parsing:** Supports integers, decimals, and scientific notation (e.g., `-12.34e+2`).
- **Array Parsing:** Validates the structure of JSON arrays (e.g., `[ "apple", "banana" ]`).
- **Object Parsing:** Validates JSON objects with key-value pairs (e.g., `{ "key": "value" }`).
- **Literal Parsing:** Recognizes and validates the literals `true`, `false`, and `null`.

### Functionality
The tool operates as a command-line validator. It reads a file, parses its content, and reports whether the JSON is valid.

**The process is typically split into two main parts:**
1. Lexical Analysis (Lexing/Tokenizing): Input: {"key": "value"}; Output Tokens: LEFT_BRACE, STRING("key"), COLON, STRING("value"), RIGHT_BRACE
2. Syntactic Analysis (Parsing): The parser's job is to validate this structure.

### Input Handling
- Accepts a file path as a command-line argument.
- Throws detailed runtime errors for invalid syntax (e.g., unclosed strings, unexpected characters).

### Purpose
This project demonstrates how JSON parsing works internally—from lexical analysis (tokenizing) to syntactic analysis (parsing)—giving developers a deeper understanding of how compilers and interpreters process structured text.


### Build
```bash
javac org/build/jsonparser/JsonParser.java
````

### Run
```bash
java -cp . org/build/jsonparser/JsonParser [file]
```

### Example

### Validating a correct JSON file

Assuming `valid.json` contains:

```json
{
  "name": "Shawon",
  "age": 24,
  "isDeveloper": true,
  "skills": ["Java", "Spring Boot", "SQL"],
  "details": {
    "country": "Bangladesh",
    "experience": 1.2
  },
  "projects": null
}

```

Run:

```bash
java -cp . org.build.jsonparser.JsonParser org/build/jsonparser/valid.json
```

Output:

```
Valid JSON
```

### Validating an incorrect JSON file

Assuming `invalid.json` contains:

```json
{
  name: Shawon,
  age: 24,
  skills: ["Java", "Spring Boot", "SQL",],
  "isDeveloper": true,
  "details": {
    "country": "Bangladesh",
    "experience": 1.2,
  }
}

```

Run:

```bash
java -cp . org.build.jsonparser.JsonParser org/build/jsonparser/invalid.json
```

Output:

```
Invalid JSON: Invalid literal: expected null
```

### Supported JSON Features

| Feature      | Description                                  |
| ------------ | -------------------------------------------- |
| **Strings**  | Supports standard escape sequences           |
| **Numbers**  | Integers, decimals, and exponential notation |
| **Arrays**   | `[value1, value2, ...]`                      |
| **Objects**  | `{"key": value, ...}`                        |
| **Literals** | `true`, `false`, `null`                      |

```
