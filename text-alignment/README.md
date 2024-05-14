# Practical 1 - Text Alignment

A program for aligning text with a specific line length. Alignment options include left-align, right-align, centre-align and justify.

## Usage
The following assumes you are in the root directory of this project. To compile the Java code enter the following. Note that you must enter the `src/` folder before running `javac` so that the `aligners` package compiles correctly.
```bash
cd src/
javac *.java
```
The `TextAlignment` executable requires three command line arguments and should be run via the command prompt as outlined below.
```bash
java TextAlignment <filename> <alignmentType> <lineLength>
```
The `filename` should be the absolute or relative path to the text file containing the text you want to align. The `alignmentType` is the method of alignment you want to use, the options being `left`, `right`, `centre` or `justify`. Finally, the `lineLength` parameter should be a positive integer specifying the maximum number of characters to be in each line of the aligned text. Note that if any of the arguments are incorrect the code will display the following.

```bash
usage: java TextAlignment <filename> <alignmentType> <lineLength>
```

## Testing
Extra tests have been included in this project. They are available under the `tests/private` directory. These 12 tests are used to verify that the program works correctly on an empty text file `empty.txt` and on a file with decreasing paragraph lengths called `triangle.txt`, over all text alignment types. The file `triangle.txt` is a particularly useful for testing as it verifies that `justify` works for a wide range of line lengths. Also, since each paragraph is just represented by a single line it is a great edge case to verify that the formatting (in particular line breaks) work correctly. I recommend giving these files and their expected output a browse; this gives a great perspective into what these tests check for. You can run all the tests by entering the following command:

```bash
stacscheck /cs/home/<username>/CS5001-p1/tests/
```
If all the tests run successfully the following should be displayed:

```bash
50 out of 50 tests passed
```

## Extra Features and Implementation Notes
- Using the `Scanner` object I have implemented by own version of the `FileUtil` class to handle reading in paragraphs from a file. This class reads the contents of a file line by line, splits the text into paragraphs, and returns an `ArrayList` of strings, where each `String` is a separate paragraph. Note that paragraphs are assumed to be deliminated by line breaks. I have initialised the `Scanner` within the `try` block, so if we handle an exception at any point in reading the file, the `Scanner` object closes the file.
- The abstract class `Aligner` was implemented instead of using an interface as the subclasses `LeftAligner`, `RightAligner` and `CentreAligner` had identical implementations of the `format` method, hence this shared functionality was moved into the abstract class. Defining the functionality in this abstract class removed repeated code and has resulted in much smaller implementations for these classes. Note that the `JustifyAligner` class overrides this base `format` method as the justify alignment case is quite a bit different from the others. The `Aligner` class was declared abstract as we do not want to explicitly be able to create an `Aligner` object as it does not make sense to make such and object without a specific type of alignment in mind.
- Encapsulation was used for the `align` method in the `Aligner` class. The `Aligner` class and all corresponding subclasses were placed in a separate package called `aligners`. This was done as the `align` method was given the `protected` modifier and so should only be accessible via the subclasses of `Aligner`. Both the `LeftAligner` and `JustifyAligner` subclasses keep the default `align` functionality, however both the `RightAligner` and `CentreAligner` classes implement their own version for right and centre alignment respectively.
- The class `FileUtil` uses an `ArrayList<String>` object to store the paragraphs. A list is used here as we do not know the number of paragraphs at compile time. Hence, resizing an array would be less efficient. This `ArrayList` is then cast into a `String` array when it is returned. This is to improve efficiency when accessing the array in the `TextAlignment` class as we do not alter the array within this class.
- Within the `Aligner` class and corresponding subclasses the built-in `StringBuilder` object is used instead of doing direct manipulation on `String` objects. Since `String`'s are immutable, the concatenation needed to join words together in the `format` and `align` methods would generate numerous new `String` objects. These are quite complex operations and leave a lot of garbage for Java to collect. The `StringBuilder` class alleviates this problem as they are mutable objects which provide efficient methods (`append()`, `insert()`, `delete()` and `substring()`) for string manipulation. Note that there is also a `StringBuffer` object which is similar to `StringBuilder` except that it is threadsafe. Since we are not dealing with multithreading in this practical it makes more sense to use `StringBuilder` to get the maximum performance (a roughly 15% improvement over `StringBuffer`).
- A bug I came across in testing using `triangle.txt` was that the text files I created had an extra line break character at the end of every line. This was causing an extra line to be printed and so the output was displaying incorrectly. To fix this in `FileUtils.java` I added a regex replace to remove any extra line breaks when loading the paragraphs from a file. The extra condition in the `if` statement for the `Aligner.format()` method `line.length() > 0` is needed as if a paragraph is a single long word then an extra newline character would be printed. This was not noticed in the included tests, and was instead noticed with tests using the `triangle.txt` file.