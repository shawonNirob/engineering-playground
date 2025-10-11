## A Java implementation of the Unix `wc` command.

### Description

`ccwc` (Coding Challenges Word Count) is a command-line tool built in Java that replicates the core functionality of the Unix `wc` utility. It reads text from a file or standard input and provides statistical information about the content.

### Core Features

* **Byte Count:** Calculates the total number of bytes in the input.
* **Line Count:** Counts the total number of lines (newline characters).
* **Word Count:** Counts words separated by whitespace.
* **Character Count:** Counts all characters, including punctuation and spaces.
* **Unique Character Count (-u):** Counts only letters and digits, ignoring punctuation, symbols, and spaces.

### Functionality

The tool accepts command-line options to determine which statistic to display:

* `ccwc -c test.txt` → byte count
* `ccwc -l test.txt` → line count
* `ccwc -w test.txt` → word count
* `ccwc -m test.txt` → character count (all characters)
* `ccwc -u test.txt` → character count (letters and digits only)
* `ccwc test.txt` → displays line count, word count, and byte count (default behavior)

### Input Handling

* **File Input:** Reads directly from a specified file.
* **Standard Input (stdin):** Reads from piped data when no file is provided, e.g.:

  ```bash
  cat test.txt | ccwc -l
  ```

### Purpose

This project demonstrates efficient text processing, command-line argument parsing, and adherence to Unix-like design principles in Java.


### Build
```bash
javac org/build/wctool/CCWC.java
````

### Run

```bash
java -cp . org.build.wctool.CCWC [option] [file]
```

### Usage

| Option | Description |
|:------:|--------------|
| `-c` | Count bytes |
| `-l` | Count lines |
| `-w` | Count words |
| `-m` | Count all characters (including punctuation and spaces) |
| `-u` | Count only letters and digits (ignores punctuation, symbols, spaces) |
| *(none)* | Show lines, words, and bytes (default) |

### Examples

```bash
java -cp . org.build.wctool.CCWC -c org/build/wctool/test.txt
java -cp . org.build.wctool.CCWC -l org/build/wctool/test.txt
java -cp . org.build.wctool.CCWC -w org/build/wctool/test.txt
java -cp . org.build.wctool.CCWC -m org/build/wctool/test.txt
java -cp . org.build.wctool.CCWC -u org/build/wctool/test.txt
java -cp . org.build.wctool.CCWC org/build/wctool/test.txt
java -cp . org.build.wctool.CCWC -clmw org/build/wctool/test.txt
cat org/build/wctool/test.txt | java -cp . org.build.wctool.CCWC -l (pipe cat command)
java -cp . org.build.wctool.CCWC -l file1.txt file2.txt  ❌ (shows “extra operand” error)
```


