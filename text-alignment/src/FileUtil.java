import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * A file utility class to handle reading paragraphs from a file.
 */
public class FileUtil {

    /**
     * Reads the contents of a file and returns a list of paragraphs.
     * Paragraphs are assumed to be deliminated by single line breaks.
     *
     * @param filename The absolute or relative path of the file to be read.
     * @return A list of paragraphs from the file, represented as strings.
     * @throws FileNotFoundException If the file is not found.
     */
    public static String[] readFile(String filename) {
        // Contains the paragraphs from the file.
        ArrayList<String> paragraphs = new ArrayList<String>();

        // Load the file one line at a time, and separate into paragraphs.
        try (Scanner textScanner = new Scanner(new File(filename))) {
            textScanner.useDelimiter("\n"); // Assumes paragraphs are separated by line breaks.
            while (textScanner.hasNext()) {
                String strippedParagraph = textScanner.next().replace("\n", ""); // Remove line-breaks.
                paragraphs.add(strippedParagraph);
            }
        // Display an error message if the file does not exist and exit the program.
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + filename + " (No such file or directory)");
            System.exit(1);
        }

        // Convert ArrayList<String> to String[] for performance.
        return paragraphs.toArray(new String[0]);
    }
}
