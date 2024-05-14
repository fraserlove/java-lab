package aligners;

/**
 * This class implements centre alignment within a specified line length.
 */
public class CentreAligner extends Aligner {

    /**
     * Constructs a CentreAligner object with a specific line length.
     * 
     * @param lineLength The maximum number of characters in a line.
     */
    public CentreAligner(int lineLength) {
        super(lineLength);
    }

    /**
     * Centre-aligns a line, with equal left-padding and right-padding it with spaces.
     * If the padding is an odd number the left side is given the extra padding.
     *
     * @param line The input line to be right-aligned.
     * @return The right-aligned line.
     */
    @Override
    protected String align(String line) {
        // Amount of padding to be applied left and right of the text. Is set to be
        // non-negative (incase the line is already longer than the specified line length).
        int padding = lineLength - line.length();
        int left = Math.max(0, (int) Math.ceil((double) padding / 2));
        int right = Math.max(0, (int) Math.floor((double) padding / 2));
        // Add spaces on the left and right to centre-align the line.
        return " ".repeat(left) + line + " ".repeat(right);
    }
}
