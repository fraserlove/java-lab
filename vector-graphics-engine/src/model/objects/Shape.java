package model.objects;

import model.Object2D;
import model.Point;

public abstract class Shape extends Object2D {

    protected Point[] vertices;
    final String type;
    int rotation;

    int[] colour;
    int[] fill;
    int lineWeight;

    public Shape(String type, int rotation, int lineWeight, int[] colour, int[] fill) {

        this.type = type;
        this.rotation = rotation;
        this.lineWeight = lineWeight;
        this.colour = colour;
        this.fill = fill;
    }

    public String getType() { return type; }
    public int getRotation() { return rotation; }
    public int getLineWeight () { return lineWeight; }
    public int[] getColour() { return colour; }
    public int[] getFill() { return fill; }

    public void setRotation(int rotation) { this.rotation = rotation; }
    public void setLineWeight(int pt) { this.lineWeight = pt; }
    public void setColour(int[] colour) { this.colour = colour; }
    public void setFill(int[] fill) { this.fill = fill; }

    public int vertSize() { return vertices.length; }
    public int centreX() { return ((vertices[1].x + vertices[0].x) / 2); }
    public int centreY() { return ((vertices[1].y + vertices[0].y) / 2); }

    public int[] getXVerts() {
        int[] xVerts = new int[vertices.length];
        for (int i = 0; i < vertices.length; i++)
            xVerts[i] = vertices[i].x;
        return xVerts;
    }

    public int[] getYVerts() {
        int[] yVerts = new int[vertices.length];
        for (int i = 0; i < vertices.length; i++)
            yVerts[i] = vertices[i].y;
        return yVerts;
    }

    public abstract void translate(int x, int y);
    public abstract void resize(int width, int height);
}
