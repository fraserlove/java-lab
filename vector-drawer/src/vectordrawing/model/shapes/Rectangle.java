package vectordrawing.model.shapes;

import vectordrawing.model.Shape;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

/**
 * Represents a rectangle.
 */
public class Rectangle extends Shape {

    private int width;
    private int height;

    /**
     * Creates a new rectangle.
     * 
     * @param id ID of the rectangle.
     * @param x x-coordinate of the rectangle.
     * @param y y-coordinate of the rectangle.
     * @param width Width of the rectangle.
     * @param height Height of the rectangle.
     * @param rotation Rotation of the rectangle.
     * @param lineWeight Line weight of the rectangle.
     * @param lineColour Line colour of the rectangle.
     * @param fill Fill colour of the rectangle.
     * @param isOwner Whether the rectangle is owned by the client.
     */
    public Rectangle(String id, int x, int y, int width, int height, int rotation, int lineWeight, String lineColour, String fill, boolean isOwner) {
        super(id, x, y, rotation, lineWeight, lineColour, fill, isOwner);
        this.width = width;
        this.height = height;
    }

    /**
     * Get the dimensions of the rectangle.
     * 
     * @return Dimensions of the rectangle.
     */
    @Override
    public int[] getDimensions() {
        return new int[] {this.width, this.height};
    }

    /**
     * Resize the rectangle to the specified dimensions.
     * 
     * @param params Dimensions to resize the rectangle to.
     */
    @Override
    public void resize(int... params) {
        this.width = params[0];
        this.height = params[1];
    }

    /**
     * Get the centre of the rectangle.
     * 
     * @return Centre of the rectangle.
     */
    @Override
    public int[] centre() {
        return new int[] {this.x + this.width / 2, this.y + this.height / 2};
    }

    /**
     * Get the x-coordinates of the rectangle.
     * 
     * @return x-coordinates of the rectangle.
     */
    @Override
    public int[] getXs() {
        return new int[] {this.x, this.x + this.width, this.x + this.width, this.x};
    }

    /**
     * Get the y-coordinates of the rectangle.
     * 
     * @return y-coordinates of the rectangle.
     */
    @Override
    public int[] getYs() {
        return new int[] {this.y, this.y, this.y + this.height, this.y + this.height};
    }

    /**
     * Converts the line to JSON.
     *
     * @return Line representation in JSON.
     */
    @Override
    public JsonObject toJson() {
        JsonObjectBuilder propertiesBuilder = Json.createObjectBuilder()
        .add("width", this.width)
        .add("height", this.height)
        .add("rotation", this.rotation)
        .add("borderWidth", this.lineWeight)
        .add("borderColor", this.lineColour)
        .add("fillColor", this.fill);

        return Json.createObjectBuilder()
            .add("type", "rectangle")
            .add("isOwner", this.isOwner)
            .add("id", this.id)
            .add("x", this.x)
            .add("y", this.y)
            .add("properties", propertiesBuilder)
            .build();
    }
}
