package model.objects.shapes;

import model.Point;
import model.objects.Shape;

public class Rectangle extends Shape {

    public Rectangle(int x0, int y0, int x1, int y1, int rotation, int lineWeight, int[] colour, int[] fill) {

        super("rectangle", rotation, lineWeight, colour, fill);

        /* Object2D attributes */
        position = new Point(Math.min(x0, x1), Math.min(y0, y1));
        width = Math.abs(x1 - x0);
        height = Math.abs(y1 - y0);

        /* Shape attributes */
        vertices = new Point[] {new Point(x0, y0), new Point(x1, y1)};
    }

    public void translate(int x, int y) {
        vertices[0] = new Point(x, y);
        vertices[1] = new Point(x + width, y + height);
        position = vertices[0];
    }

    public void resize(int width, int height) {
        this.width = width;
        this.height = height;
    }
}