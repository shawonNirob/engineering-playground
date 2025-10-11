package org.build.wctool;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;

public class CCWC {
    public static void main(String[] args) {
        boolean c_opt = false;
        boolean l_opt = false;
        boolean w_opt = false;
        boolean m_opt = false;
        boolean u_opt = false; // count only letters/digits
        String filePath = null;


        for(String arg : args) {
            if(arg.startsWith("-")){
                for(char flag: arg.substring(1).toCharArray()) {
                    switch(flag) {
                        case 'c':
                            c_opt = true;
                            break;
                        case 'l':
                            l_opt = true;
                            break;
                        case 'w':
                            w_opt = true;
                            break;
                        case 'm':
                            m_opt = true;
                            break;
                        case 'u':
                            u_opt = true;
                            break;
                        default:
                            System.err.println("ccwc: invalid option -- '" + flag + "'");
                            System.err.println("Usage: java CCWC [-clwm] [file]");
                            System.exit(1);
                    }
                }
            } else if (filePath == null) {
                filePath = arg;
            }else{
                System.err.println("ccwc: invalid option -- '" + arg + "'");
                System.err.println("Usage: java CCWC [-clwm] [file]");
                System.exit(1);
            }
        }

        boolean isDefault = !c_opt && !l_opt && !w_opt && !m_opt;

        try{
            byte[] bytes;
            if(filePath != null) {
                File file = new File(filePath);
                if(!file.exists()){
                    System.err.println("ccwc: file '" + filePath + "' does not exist");
                    System.exit(1);
                }
                bytes = Files.readAllBytes(file.toPath());
            }else {
                bytes = System.in.readAllBytes();
            }


            String content = new String(bytes, Charset.defaultCharset());

            long byteCount = bytes.length;
            long lineCount = content.lines().count();


            long wordCount = 0;

            if(!content.isBlank()){
                String cleaned = content.replaceAll("[^\\p{L}\\p{Nd}]+", " "); //to Replaces everything that is not a letter or digit (punctuation, symbols, extra whitespace) with a single space.
                String[] words = cleaned.trim().split("\\s+");
                wordCount = words.length;
            }

            long charCount = content.length();

            long uCount = 0;
            if(u_opt){
                uCount = content.replaceAll("[^\\p{L}\\p{Nd}]", "").length();
            }

            StringBuilder result = new StringBuilder();

            if (l_opt || isDefault) {
                result.append(String.format("%8d", lineCount));
            }
            if (w_opt || isDefault) {
                result.append(String.format("%8d", wordCount));
            }
            if (c_opt || isDefault) {
                result.append(String.format("%8d", byteCount));
            }
            if (m_opt) {
                result.append(String.format("%8d", charCount));
            }
            if(u_opt){
                result.append(String.format("%8d", uCount));
            }


            if (filePath != null) {
                result.append(" ").append(filePath);
            }


            System.out.println(result.toString().stripLeading());


        } catch (IOException e){
            System.err.println("ccwc: error reading file '" + filePath + "'" + e.getMessage());
            System.exit(1);
        }
    }
}
