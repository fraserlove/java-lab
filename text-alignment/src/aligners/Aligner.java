package aligners;

/**
 * Defines the default behaviour of an Aligner class.
 */
public abstract class Aligner {

    protected int lineLength;

    /**
     * Constructs an Aligner object with a specific line length.
     * 
     * @param lineLength The maximum number of characters in a line.
     */
    public Aligner(int lineLength) {
        this.lineLength = lineLength;
    }

    /**
     * Formats the provided text using the align method. This can be overriden
     * according to each alignment implementation.
     *
     * @param paragraph The input text to be aligned.
     * @return The aligned input text.
     */
    public String format(String paragraph) {
        String[] words = paragraph.split(" ");

        // Store the aligned text and the current line being aligned.
        StringBuilder aligned = new StringBuilder();
        StringBuilder line = new StringBuilder();

        for (String word : words) {

            // If line length with new word is exceeded, add current line to aligned and start a new line.
            if (line.length() + word.length() > lineLength && !line.isEmpty()) {
                // Align the current line and add to the aligned text.
                aligned.append(align(line.toString().trim())).append("\n");
                line.setLength(0); // Reset the current line.
            }
            line.append(word).append(" "); // Add the word and appending space to the current line.
        }

        // Add the remaining line to the aligned text.
        aligned.append(align(line.toString().trim())).append("\n");

        return aligned.toString();
    }

    /**
     * Aligns a line, padding it with spaces. Default is no padding, so left alignment.
     *
     * @param line The input line to be aligned.
     * @return The aligned line.
     */
    String align(String line) {
        return line;
    }
}
