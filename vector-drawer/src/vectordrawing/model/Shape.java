package vectordrawing.model;

import javax.json.JsonObject;

/**
 * Represents an abstract shape.
 */
public abstract class Shape {

    /** Maximum possible line weight. */
    public static final int MAX_LINE_WEIGHT = 100;
    /** Maximum possible coordinate. */
    public static final int MAX_COORDINATE = 10000;
    /** Maximum possible dimension. */
    public static final int MAX_DIMENSION = 10000;
    /** Maximum possible rotation. */
    public static final int MAX_ROTATION = 360;

    /** Fallback dimension. */
    public static final int DEFAULT_DIMENSION = 100;
    /** Fallback x-coordinate. */
    public static final int DEFAULT_X = 0;
    /** Fallback y-coordinate. */
    public static final int DEFAULT_Y = 0;
    /** Fallback rotation. */
    public static final int DEFAULT_ROTATION = 0;
    /** Fallback line weight. */
    public static final int DEFAULT_LINE_WEIGHT = 4;
    /** Fallback line colour. */
    public static final String DEFAULT_COLOUR = "black";
    /** Fallback fill colour. */
    public static final String DEFAULT_FILL = "black";
    /** Fallback shape type. */
    public static final String DEFAULT_SHAPE = "rectangle";

    // Shape properties.
    protected String id;
    protected int x;
    protected int y;
    protected int rotation;
    protected String lineColour;
    protected String fill;
    protected int lineWeight;
    protected boolean isOwner;

    /**
     * Creates a new shape.
     * 
     * @param id ID of the shape.
     * @param x x-coordinate of the shape.
     * @param y y-coordinate of the shape.
     * @param rotation Rotation of the shape.
     * @param lineWeight Line weight of the shape.
     * @param lineColour Line colour of the shape.
     * @param fill Fill colour of the shape.
     * @param isOwner Whether the shape is owned by the client.
     */
    public Shape(String id, int x, int y, int rotation, int lineWeight, String lineColour, String fill, boolean isOwner) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.rotation = rotation;
        this.lineWeight = lineWeight;
        this.lineColour = lineColour;
        this.fill = fill;
        this.isOwner = isOwner;
    }

    /**
     * Get the ID of the shape.
     * 
     * @return ID of the shape.
     */
    public String getID() {
        return this.id;
    }

    /**
     * Get whether the shape is owned by the client.
     * 
     * @return Whether the shape is owned by the client.
     */
    public boolean isOwner() {
        return this.isOwner;
    }

    /*
     * Get the position of the shape.
     * 
     * @return Position of the shape.
     */
    public int[] getPosition() {
        return new int[] {this.x, this.y};
    }

    /**
     * Get the rotation of the shape.
     * 
     * @return Rotation of the shape.
     */
    public int getRotation() {
        return this.rotation;
    }

    /**
     * Get the line weight of the shape.
     * 
     * @return Line weight of the shape.
     */
    public int getLineWeight() {
        return this.lineWeight;
    }

    /**
     * Get the line colour of the shape.
     * 
     * @return Line colour of the shape.
     */
    public String getLineColour() {
        return this.lineColour;
    }

    /**
     * Get the fill colour of the shape.
     * 
     * @return Fill colour of the shape.
     */
    public String getFill() {
        return fill;
    }

    /**
     * Set the ID of the shape.
     * 
     * @param id ID of the shape.
     */
    public void setID(String id) {
        this.id = id;
    }

    /**
     * Set the position of the shape.
     * 
     * @param x x-coordinate of the shape.
     * @param y y-coordinate of the shape.
     */
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Set the rotation of the shape.
     * 
     * @param rotation Rotation of the shape.
     */
    public void setRotation(int rotation) {
        this.rotation = rotation;
    }

    /**
     * Set the line weight of the shape.
     * 
     * @param lineWeight Line weight of the shape.
     */
    public void setLineWeight(int lineWeight) {
        this.lineWeight = lineWeight;
    }

    /**
     * Set the line colour of the shape.
     * 
     * @param lineColour Line colour of the shape.
     */
    public void setColour(String lineColour) {
        this.lineColour = lineColour;
    }

    /**
     * Set the fill colour of the shape.
     * 
     * @param fill Fill colour of the shape.
     */
    public void setFill(String fill) {
        this.fill = fill;
    }

    /**
     * Get the dimensions of the shape.
     * 
     * @return Dimensions of the shape.
     */
    public abstract int[] getDimensions();

    /**
     * Resize the shape to the specified dimensions.
     * 
     * @param params Dimensions to resize the shape to.
     */
    public abstract void resize(int... params);

    /**
     * Get the centre of the shape.
     * 
     * @return Centre of the shape.
     */
    public abstract int[] centre();

    /**
     * Get the x-coordinates of the shape.
     * 
     * @return x-coordinates of the shape.
     */
    public abstract int[] getXs();

    /**
     * Get the y-coordinates of the shape.
     * 
     * @return y-coordinates of the shape.
     */
    public abstract int[] getYs();

    /**
     * Convert the shape to JSON.
     * 
     * @return Shape representation in JSON.
     */
    public abstract JsonObject toJson();
}
