package aligners;

/**
 * This class implements right alignment within a specified line length.
 */
public class RightAligner extends Aligner {

    /**
     * Constructs a RightAligner object with a specific line length.
     * 
     * @param lineLength The maximum number of characters in a line.
     */
    public RightAligner(int lineLength) {
        super(lineLength);
    }

    /**
     * Right-aligns a line, left-padding it with spaces.
     *
     * @param line The input line to be right-aligned.
     * @return The right-aligned line.
     */
    protected String align(String line) {
        // Amount of padding to be applied. Is set to be non-negative
        // (incase the line is already longer than the specified line length).
        int padding = Math.max(0, lineLength - line.length());
        // Add spaces on the left to right-align the line.
        return " ".repeat(padding) + line;
    }
}
