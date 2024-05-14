package vectordrawing.utils;

import vectordrawing.model.Shape;
import vectordrawing.model.shapes.Ellipse;
import vectordrawing.model.shapes.Line;
import vectordrawing.model.shapes.Rectangle;
import vectordrawing.model.shapes.Triangle;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

/**
 * Manages JSON operations.
 */
public class JsonUtils {

    /**
     * Creates JSON from a shape.
     * 
     * @param shape Shape to convert to JSON.
     * @return JSON shape data.
     */
    public static JsonObject shapeToJson(Shape shape) {
        return shape.toJson();
    }

    /**
     * Creates a shape from JSON.
     * 
     * @param json JSON shape data.
     * @return Shape.
     */
    public static Shape shapeFromJson(JsonObject json) {
        Shape shape = null;
        // Sanitise JSON to ensure that it describes a valid shape.
        json = sanitiseJson(json);
        if (json != null) {
            switch (json.getString("type")) {
                case "rectangle":
                    shape = rectangleFromJson(json);
                    break;
                case "ellipse":
                    shape = ellipseFromJson(json);
                    break;
                case "triangle":
                    shape = triangleFromJson(json);
                    break;
                case "line":
                    shape = lineFromJson(json);
                    break;
                default:
                    break;
            }
        }
        return shape;
    }

    /**
     * Creates a rectangle from JSON.
     * 
     * @param json JSON rectangle data.
     * @return Rectangle.
     */
    private static Rectangle rectangleFromJson(JsonObject json) {
        JsonObject properties = json.getJsonObject("properties");
        String id = "";
        boolean isOwner = false;
        if (json.containsKey("id")) {
            id = json.getString("id");
        }
        if (json.containsKey("isOwner")) {
            isOwner = json.getBoolean("isOwner");
        }
        int x = json.getInt("x");
        int y = json.getInt("y");
        int width = properties.getInt("width");
        int height = properties.getInt("height");
        int rotation = properties.getInt("rotation");
        int lineWeight = properties.getInt("borderWidth");
        String lineColour = properties.getString("borderColor");
        String fill = properties.getString("fillColor");
        return new Rectangle(id, x, y, width, height, rotation, lineWeight, lineColour, fill, isOwner);
    }

    /**
     * Creates an ellipse from JSON.
     * 
     * @param json JSON ellipse data.
     * @return Ellipse.
     */
    private static Ellipse ellipseFromJson(JsonObject json) {
        JsonObject properties = json.getJsonObject("properties");
        String id = "";
        boolean isOwner = false;
        if (json.containsKey("id")) {
            id = json.getString("id");
        }
        if (json.containsKey("isOwner")) {
            isOwner = json.getBoolean("isOwner");
        }
        int x = json.getInt("x");
        int y = json.getInt("y");
        int width = properties.getInt("width");
        int height = properties.getInt("height");
        int rotation = properties.getInt("rotation");
        int lineWeight = properties.getInt("borderWidth");
        String lineColour = properties.getString("borderColor");
        String fill = properties.getString("fillColor");
        return new Ellipse(id, x, y, width, height, rotation, lineWeight, lineColour, fill, isOwner);
    }

    /**
     * Creates an triangle from JSON.
     * 
     * @param json JSON triangle data.
     * @return Triangle.
     */
    private static Triangle triangleFromJson(JsonObject json) {
        JsonObject properties = json.getJsonObject("properties");
        String id = "";
        boolean isOwner = false;
        if (json.containsKey("id")) {
            id = json.getString("id");
        }
        if (json.containsKey("isOwner")) {
            isOwner = json.getBoolean("isOwner");
        }
        int x = json.getInt("x");
        int y = json.getInt("y");
        int x2 = properties.getInt("x2");
        int y2 = properties.getInt("y2");
        int x3 = properties.getInt("x3");
        int y3 = properties.getInt("y3");
        int rotation = properties.getInt("rotation");
        int lineWeight = properties.getInt("borderWidth");
        String lineColour = properties.getString("borderColor");
        String fill = properties.getString("fillColor");
        return new Triangle(id, x, y, x2, y2, x3, y3, rotation, lineWeight, lineColour, fill, isOwner);
    }

    /**
     * Creates a line from JSON.
     * 
     * @param json JSON line data.
     * @return Line.
     */
    private static Line lineFromJson(JsonObject json) {
        JsonObject properties = json.getJsonObject("properties");
        String id = "";
        boolean isOwner = false;
        if (json.containsKey("id")) {
            id = json.getString("id");
        }
        if (json.containsKey("isOwner")) {
            isOwner = json.getBoolean("isOwner");
        }
        int x = json.getInt("x");
        int y = json.getInt("y");
        int x2 = properties.getInt("x2");
        int y2 = properties.getInt("y2");
        int rotation = properties.getInt("rotation");
        int lineWeight = properties.getInt("lineWidth");
        String lineColour = properties.getString("lineColor");
        return new Line(id, x, y, x2, y2, rotation, lineWeight, lineColour, isOwner);
    }

    /**
     * Updates a shape from JSON.
     * 
     * @param shape Shape to update.
     * @param json JSON shape data.
     */
    public static void updateShapeFromJson(Shape shape, JsonObject json) {
        JsonObject properties = json.getJsonObject("properties");
        shape.setPosition(json.getInt("x"), json.getInt("y"));
        shape.setRotation(properties.getInt("rotation"));
        if (shape instanceof Rectangle || shape instanceof Ellipse) {
            shape.resize(properties.getInt("width"), properties.getInt("height"));
            shape.setLineWeight(properties.getInt("borderWidth"));
            shape.setColour(properties.getString("borderColor"));
            shape.setFill(properties.getString("fillColor"));
        } else if (shape instanceof Triangle) {
            shape.resize(properties.getInt("x2"), properties.getInt("y2"), properties.getInt("x3"), properties.getInt("y3"));
            shape.setLineWeight(properties.getInt("borderWidth"));
            shape.setColour(properties.getString("borderColor"));
            shape.setFill(properties.getString("fillColor"));
        } else if (shape instanceof Line) {
            shape.resize(properties.getInt("x2"), properties.getInt("y2"));
            shape.setLineWeight(properties.getInt("lineWidth"));
            shape.setColour(properties.getString("lineColor"));
        }
    }

    /**
     * Sanitises JSON representing a shape to ensure that it is valid.
     * 
     * @param shape Shape to sanitise.
     * @return Sanitised shape, or null if the shape is not supported or invalid.
     */
    private static JsonObject sanitiseJson(JsonObject json) {
        if (json.containsKey("id") && json.containsKey("type")) {
            JsonObjectBuilder builder = Json.createObjectBuilder();

            // Common properties for all shapes.
            builder.add("id", json.getString("id"));
            builder.add("type", json.getString("type"));
            builder.add("x", json.getInt("x", Shape.DEFAULT_X));
            builder.add("y", json.getInt("y", Shape.DEFAULT_Y));
            builder.add("isOwner", json.getBoolean("isOwner", json.getBoolean("isOwner")));

            JsonObject properties = json.getJsonObject("properties");
            JsonObjectBuilder propertiesBuilder = Json.createObjectBuilder();
            switch (json.getString("type")) {
                case "rectangle":
                case "ellipse":
                    propertiesBuilder.add("width", properties.getInt("width", Shape.DEFAULT_DIMENSION));
                    propertiesBuilder.add("height", properties.getInt("height", Shape.DEFAULT_DIMENSION));
                    propertiesBuilder.add("borderColor", properties.getString("borderColor", Shape.DEFAULT_COLOUR));
                    propertiesBuilder.add("borderWidth", properties.getInt("borderWidth", Shape.DEFAULT_LINE_WEIGHT));
                    break;
                case "triangle":
                    propertiesBuilder.add("x2", properties.getInt("x2", Shape.DEFAULT_X + Shape.DEFAULT_DIMENSION));
                    propertiesBuilder.add("y2", properties.getInt("y2", Shape.DEFAULT_Y + Shape.DEFAULT_DIMENSION));
                    propertiesBuilder.add("x3", properties.getInt("x3", Shape.DEFAULT_X));
                    propertiesBuilder.add("y3", properties.getInt("y3", Shape.DEFAULT_Y + Shape.DEFAULT_DIMENSION));
                    propertiesBuilder.add("borderColor", properties.getString("borderColor", Shape.DEFAULT_COLOUR));
                    propertiesBuilder.add("borderWidth", properties.getInt("borderWidth", Shape.DEFAULT_LINE_WEIGHT));
                    break;
                case "line":
                    propertiesBuilder.add("x2", properties.getInt("x2", Shape.DEFAULT_X + Shape.DEFAULT_DIMENSION));
                    propertiesBuilder.add("y2", properties.getInt("y2", Shape.DEFAULT_Y + Shape.DEFAULT_DIMENSION));
                    propertiesBuilder.add("lineColor", properties.getString("lineColor", Shape.DEFAULT_COLOUR));
                    propertiesBuilder.add("lineWidth", properties.getInt("lineWidth", Shape.DEFAULT_LINE_WEIGHT));
                    break;
                default:
                    // Igonore shapes that are not supported.
                    return null;
            }
            // Common properties for all shapes.
            propertiesBuilder.add("rotation", properties.getInt("rotation", Shape.DEFAULT_ROTATION));
            propertiesBuilder.add("fillColor", properties.getString("fillColor", Shape.DEFAULT_FILL));
            builder.add("properties", propertiesBuilder);
            return builder.build();
        }
        return null;
    }
}
