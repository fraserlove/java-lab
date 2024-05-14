package vectordrawing.model.shapes;

import vectordrawing.model.Shape;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

/**
 * Represents an ellipse.
 */
public class Ellipse extends Shape {

    private int width;
    private int height;

    /**
     * Creates a new ellipse.
     * 
     * @param id ID of the ellipse.
     * @param x x-coordinate of the ellipse.
     * @param y y-coordinate of the ellipse.
     * @param width Width of the ellipse.
     * @param height Height of the ellipse.
     * @param rotation Rotation of the ellipse.
     * @param lineWeight Line weight of the ellipse.
     * @param lineColour Line colour of the ellipse.
     * @param fill Fill colour of the ellipse.
     * @param isOwner Whether the ellipse is owned by the client.
     */
    public Ellipse(String id, int x, int y, int width, int height, int rotation, int lineWeight, String lineColour, String fill, boolean isOwner) {
        super(id, x, y, rotation, lineWeight, lineColour, fill, isOwner);
        this.width = width;
        this.height = height;
    }

    /**
     * Get the dimensions of the ellipse.
     * 
     * @return Dimensions of the ellipse.
     */
    @Override
    public int[] getDimensions() {
        return new int[] {this.width, this.height};
    }

    /**
     * Resize the ellipse to the specified dimensions.
     * 
     * @param params Dimensions to resize the ellipse to.
     */
    @Override
    public void resize(int... params) {
        this.width = params[0];
        this.height = params[1];
    }

    /**
     * Get the centre of the ellipse.
     * 
     * @return Centre of the ellipse.
     */
    @Override
    public int[] centre() {
        return new int[] {this.x + this.width / 2, this.y + this.height / 2};
    }

    /**
     * Get the x-coordinates of the ellipse.
     * 
     * @return x-coordinates of the ellipse.
     */
    @Override
    public int[] getXs() {
        return new int[] {this.x, this.x + this.width, this.x + this.width, this.x};
    }

    /**
     * Get the y-coordinates of the ellipse.
     * 
     * @return y-coordinates of the ellipse.
     */
    @Override
    public int[] getYs() {
        return new int[] {this.y, this.y, this.y + this.height, this.y + this.height};
    }

    /**
     * Converts the ellipse to JSON.
     *
     * @return Ellipse representation in JSON.
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
            .add("type", "ellipse")
            .add("isOwner", this.isOwner)
            .add("id", this.id)
            .add("x", this.x)
            .add("y", this.y)
            .add("properties", propertiesBuilder)
            .build();
    }
}
