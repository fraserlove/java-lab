package model.objects.shapes;

import model.Point;
import model.objects.Shape;

public class Line extends Shape {

    private double angle;
    private int size;

    public Line(int x0, int y0, int x1, int y1, int rotation, int lineWeight, int[] colour, int[] fill) {

        super("line", rotation, lineWeight, colour, fill);

        /* Object2D attributes */
        position = new Point(Math.min(x0, x1), Math.min(y0, y1));

        vertices = new Point[] {new Point(x0, y0), new Point(x1, y1)};
        angle = Math.atan2(Math.abs(y1 - y0), Math.abs(x1 - x0));
        size = (int) Math.sqrt((y1 - y0) * (y1 - y0) + (x1 - x0) * (x1 - x0));
    }

    public void translate(int x, int y) {
        vertices[0] = new Point(x, y);
        vertices[1] = new Point((int) (vertices[0].x + size * Math.cos(angle)), (int) (vertices[0].y + size * Math.sin(angle)));
        position = vertices[0];
    }

    public void resize(int size, int angle) {
        vertices[1].x = (int) (vertices[0].x + size * Math.cos(Math.toRadians(angle)));
        vertices[1].y = (int) (vertices[0].y + size * Math.sin(Math.toRadians(angle)));
    }
}
