package vectordrawing.model.shapes;

import vectordrawing.model.Shape;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

/**
 * Represents a triangle.
 */
public class Triangle extends Shape {

    private int x2;
    private int y2;
    private int x3;
    private int y3;

    /**
     * Creates a new triangle.
     * 
     * @param id ID of the triangle.
     * @param x x-coordinate of the triangle.
     * @param y y-coordinate of the triangle.
     * @param x2 x2-coordinate of the triangle.
     * @param y2 y2-coordinate of the triangle.
     * @param x3 x3-coordinate of the triangle.
     * @param y3 y3-coordinate of the triangle.
     * @param rotation Rotation of the triangle.
     * @param lineWeight Line weight of the triangle.
     * @param lineColour Line colour of the triangle.
     * @param fill Fill colour of the triangle.
     * @param isOwner Whether the triangle is owned by the client.
     */
    public Triangle(String id, int x, int y, int x2, int y2, int x3, int y3, int rotation, int lineWeight, String lineColour, String fill, boolean isOwner) {
        super(id, x, y, rotation, lineWeight, lineColour, fill, isOwner);
        this.x2 = x2;
        this.y2 = y2;
        this.x3 = x3;
        this.y3 = y3;
    }

    /**
     * Get the dimensions of the triangle.
     * 
     * @return Dimensions of the triangle.
     */
    @Override
    public int[] getDimensions() {
        return new int[] {this.x, this.y, this.x2, this.y2, this.x3, this.y3};
    }

    /**
     * Resize the triangle to the specified dimensions.
     * 
     * @param params Dimensions to resize the triangle to.
     */
    public void resize(int... params) {
        this.x2 = params[0];
        this.y2 = params[1];
        this.x3 = params[2];
        this.y3 = params[3];
    }

    /**
     * Get the centre of the triangle.
     * 
     * @return Centre of the triangle.
     */
    @Override
    public int[] centre() {
        return new int[] {(this.x + this.x2 + this.x3) / this.getXs().length, (this.y + this.y2 + this.y3) / this.getYs().length};
    }

    /**
     * Get the x-coordinates of the triangle.
     * 
     * @return x-coordinates of the triangle.
     */
    @Override
    public int[] getXs() {
        return new int[] {this.x, this.x2, this.x3};
    }

    /**
     * Get the y-coordinates of the triangle.
     * 
     * @return y-coordinates of the triangle.
     */
    @Override
    public int[] getYs() {
        return new int[] {this.y, this.y2, this.y3};
    }

    /**
     * Converts the triangle to JSON.
     *
     * @return Triangle representation in JSON.
     */
    @Override
    public JsonObject toJson() {
        JsonObjectBuilder propertiesBuilder = Json.createObjectBuilder()
        .add("x2", this.x2)
        .add("y2", this.y2)
        .add("x3", this.x3)
        .add("y3", this.y3)
        .add("rotation", this.rotation)
        .add("borderWidth", this.lineWeight)
        .add("borderColor", this.lineColour)
        .add("fillColor", this.fill);

        return Json.createObjectBuilder()
            .add("type", "triangle")
            .add("isOwner", this.isOwner)
            .add("id", this.id)
            .add("x", this.x)
            .add("y", this.y)
            .add("properties", propertiesBuilder)
            .build();
    }
}
