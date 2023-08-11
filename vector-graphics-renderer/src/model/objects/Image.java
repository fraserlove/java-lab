package model.objects;

import model.Object2D;
import model.Point;

import java.awt.image.BufferedImage;

public class Image extends Object2D {

    private final BufferedImage image;
    private final String path;

    public Image(BufferedImage image, String path, int x0, int y0, int ... dimensions) {

        /* Object2D attributes */
        position = new Point(x0, y0);
        width = image.getWidth();
        height = image.getHeight();

        if (dimensions.length == 2) {
            width = dimensions[0];
            height = dimensions[1];
        }

        this.image = image;
        this.path = path;
    }

    public void resize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void translate(int x, int y) { this.position = new Point(x, y); }

    public BufferedImage image() { return image; }
    public String path() { return path; }
}
