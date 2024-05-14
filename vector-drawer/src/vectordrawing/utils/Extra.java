package vectordrawing.utils;

import java.awt.Color;

/**
 * A class containing extra utilities.
 */
public class Extra {

    /**
     * Converts a string to a colour.
     * 
     * @param colour String representation of a colour.
     * @return Colour.
     */
    public static Color stringToColour(String colour) {
        switch (colour.toLowerCase()) {
            case "red":
                return Color.RED;
            case "green":
                return Color.GREEN;
            case "blue":
                return Color.BLUE;
            case "yellow":
                return Color.YELLOW;
            case "black":
                return Color.BLACK;
            case "white":
                return Color.WHITE;
            case "cyan":
                return Color.CYAN;
            case "magenta":
                return Color.MAGENTA;
            case "orange":
                return Color.ORANGE;
            case "pink":
                return Color.PINK;
            case "gray":
                return Color.GRAY;
            case "dark gray":
                return Color.DARK_GRAY;
            case "light gray":
                return Color.LIGHT_GRAY;
            default:
                return Color.BLACK;
        }
    }
}
