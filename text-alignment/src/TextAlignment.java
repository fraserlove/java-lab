import aligners.Aligner;
import aligners.LeftAligner;
import aligners.RightAligner;
import aligners.CentreAligner;
import aligners.JustifyAligner;

/**
 * Provides functionality to align text from a file with different alignment strategies.
 * The strategies implemented are 'left', 'right', 'centre' and 'justify'.
 */
public class TextAlignment {

    /**
     * Verifies command-line arguments.
     * 
     * @param args Command-line arguments.
     * @return A string array containing filename, alignment type, and line length.
     */
    private static String[] verifyArguments(String[] args) {
        if (args.length != 3) {
            System.out.println("usage: java TextAlignment <filename> <alignmentType> <lineLength>");
            System.exit(1);
        }

        if (!args[1].matches("left|right|centre|justify")) {
            System.out.println("usage: java TextAlignment <filename> <alignmentType> <lineLength>");
            System.exit(1);
        }

        try {
            if (Integer.parseInt(args[2]) <= 0) {
                throw new IllegalArgumentException();
            }
        } catch (IllegalArgumentException e) {
            System.out.println("usage: java TextAlignment <filename> <alignmentType> <lineLength>");
            System.exit(1);
        }
        return args;
    }

    /**
     * Fetches the relevant IAligner implementation based on the chosen alignment strategy.
     * 
     * @param alignmentType The chosen alignment type.
     * @param lineLength The chosen line length.
     * @return An instance of an aligner or null if alignmentType is invalid.
     */
    private static Aligner getAligner(String alignmentType, int lineLength) {
        switch (alignmentType) {
            case "left": return new LeftAligner(lineLength);
            case "right": return new RightAligner(lineLength);
            case "centre": return new CentreAligner(lineLength);
            case "justify": return new JustifyAligner(lineLength);
            default: return null;
        }
    }
    /**
     * The main function from which the text alignment program is launched.
     * @param args The command line arguments: filename, alignment type, and line length.
     */
    public static void main(String[] args) {

        // Verifying and parsing arguments.
        String[] arguments = verifyArguments(args);

        String filename = arguments[0];
        String alignmentType = arguments[1];
        int lineLength = Integer.parseInt(arguments[2]);

        Aligner aligner = getAligner(alignmentType, lineLength);
        String[] paragraphs = FileUtil.readFile(filename);

        // Aligning and displaying each paragraph, one at a time.
        for (String paragraph : paragraphs) {
            String aligned = aligner.format(paragraph);
            System.out.print(aligned);
        }
    }
}
