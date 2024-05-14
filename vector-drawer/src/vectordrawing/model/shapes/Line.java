package vectordrawing.model.shapes;

import vectordrawing.model.Shape;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

/**
 * Represents a line.
 */
public class Line extends Shape {

    private int x2;
    private int y2;

    /**
     * Creates a new line.
     * 
     * @param id ID of the line.
     * @param x x-coordinate of the line.
     * @param y y-coordinate of the line.
     * @param x2 x2-coordinate of the line.
     * @param y2 y2-coordinate of the line.
     * @param rotation Rotation of the line.
     * @param lineWeight Line weight of the line.
     * @param lineColour Line colour of the line.
     * @param isOwner Whether the line is owned by the client.
     */
    public Line(String id, int x, int y, int x2, int y2, int rotation, int lineWeight, String lineColour, boolean isOwner) {
        super(id, x, y, rotation, lineWeight, lineColour, null, isOwner);
        this.x2 = x2;
        this.y2 = y2;
    }

    /**
     * Get the dimensions of the line.
     * 
     * @return Dimensions of the line.
     */
    @Override
    public int[] getDimensions() {
        return new int[] {this.x, this.y, this.x2, this.y2};
    }

    /**
     * Resize the line to the specified dimensions.
     * 
     * @param params Dimensions to resize the line to.
     */
    @Override
    public void resize(int... params) {
        int size = params[0];
        int angle = params[1];
        this.x2 = (int) (this.x + size * Math.cos(Math.toRadians(angle)));
        this.y2 = (int) (this.y + size * Math.sin(Math.toRadians(angle)));
    }

    /**
     * Get the centre of the line.
     * 
     * @return Centre of the line.
     */
    @Override
    public int[] centre() {
        return new int[] {(this.x + this.x2) / 2, (this.y + this.y2) / 2};
    }

    /**
     * Get the x-coordinates of the line.
     * 
     * @return x-coordinates of the line.
     */
    @Override
    public int[] getXs() {
        return new int[] {this.x, this.x2};
    }

    /**
     * Get the y-coordinates of the line.
     * 
     * @return y-coordinates of the line.
     */
    @Override
    public int[] getYs() {
        return new int[] {this.y, this.y2};
    }

     /**
     * Converts the line to JSON.
     *
     * @return Line representation in JSON.
     */
    @Override
    public JsonObject toJson() {
        JsonObjectBuilder propertiesBuilder = Json.createObjectBuilder()
        .add("x2", this.x2)
        .add("y2", this.y2)
        .add("rotation", this.rotation)
        .add("lineWidth", this.lineWeight)
        .add("lineColor", this.lineColour);

        return Json.createObjectBuilder()
            .add("type", "line")
            .add("isOwner", this.isOwner)
            .add("id", this.id)
            .add("x", this.x)
            .add("y", this.y)
            .add("properties", propertiesBuilder)
            .build();
    }

}
