package model;

public abstract class Object2D {

    protected Point position;
    protected int width;
    protected int height;

    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }
    public int getX() { return position.x; }
    public int getY() { return position.y; }

    public abstract void translate(int x, int y);
    public abstract void resize(int width, int height);

}
