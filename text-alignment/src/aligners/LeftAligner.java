package aligners;

/**
 * This class implements left alignment within a specified line length.
 */
public class LeftAligner extends Aligner {

    /**
     * Constructs a LeftAligner object with a specific line length.
     * 
     * @param lineLength The maximum number of characters in a line.
     */
    public LeftAligner(int lineLength) {
        super(lineLength);
    }
}
