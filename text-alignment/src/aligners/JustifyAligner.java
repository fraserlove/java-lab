package aligners;

/**
 * This class implements justify alignment within a specified line length.
 */
public class JustifyAligner extends Aligner {

    /**
     * Constructs a JustifyAligner object with a specific line length.
     * 
     * @param lineLength The maximum number of characters in a line.
     */
    public JustifyAligner(int lineLength) {
        super(lineLength);
    }

    /**
     * Justifies text to fit within a specified line length.
     * Words are split and hyphenated if they exceed the line length.
     * 
     * @param paragraph The input text to be aligned.
     * @return The justified text.
     */
    @Override
    public String format(String paragraph) {
        String[] words = paragraph.split(" ");

        // Store the aligned text and the current line being aligned.
        StringBuilder aligned = new StringBuilder();
        StringBuilder line = new StringBuilder();

        for (String word : words) {

            // If word is too large for current line, split word across multiple lines.
            while (line.length() + word.length() > lineLength) {
                // Part of the word which fits on the current line.
                String part = word.substring(0,  lineLength - line.length() - 1);
                // Add the part of word to the current line, split with a hyphen.
                line.append(part);
                if (!part.isEmpty()) {
                    line.append("-");
                }
                // Add current line to aligned and start a new line.
                aligned.append(align(line.toString().trim())).append("\n");
                line.setLength(0); // Reset the current line.
                // Update word to contain only the leftover part that was not added to the current line.
                word = word.substring(part.length());
            }

            // Otherwise, word can fit on current line.
            line.append(word).append(" "); // Add the word and appending space to the current line.

            // Start a new line if we have less than one character left on the current line.
            if (line.length() > lineLength - 1) {
                aligned.append(align(line.toString().trim())).append("\n");
                line.setLength(0); // Reset the current line.
            }
        }

        // Add the remaining line to the aligned text, if it is non-empty.
        if (!line.isEmpty()) {
            aligned.append(line.toString().trim()).append("\n");
        }

        return aligned.toString();
    }
}
