package vectordrawing.tests;

import vectordrawing.model.shapes.Line;
import vectordrawing.model.shapes.Ellipse;
import vectordrawing.model.shapes.Rectangle;
import vectordrawing.model.shapes.Triangle;
import vectordrawing.model.Model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

public class ModelTest {

    private Model model;

    public ModelTest() throws IOException {
        model = new Model();
    }

    @Before
    public void setUp() {
        return;
    }

    @After
    public void tearDown() {
        model.clear();
    }

    @Test
    public void createRectangle() {
        assertTrue(model.getShapes().isEmpty());
        model.addShape(new Rectangle("rect1", 10, 20, 100, 200, 0, 1, "red", "blue", true));
        assertFalse(model.getShapes().isEmpty());
        assertTrue(model.getShapes().peek() instanceof Rectangle);
    }

    @Test
    public void createEllipse() {
        assertTrue(model.getShapes().isEmpty());
        model.addShape(new Ellipse("ellipse1", 10, 20, 100, 200, 0, 1, "red", "blue", true));
        assertFalse(model.getShapes().isEmpty());
        assertTrue(model.getShapes().peek() instanceof Ellipse);
    }

    @Test
    public void createLine() {
        assertTrue(model.getShapes().isEmpty());
        model.addShape(new Line("line1", 10, 20, 100, 200, 0, 1, "red", true));
        assertFalse(model.getShapes().isEmpty());
        assertTrue(model.getShapes().peek() instanceof Line);
    }

    @Test
    public void createTriangle() {
        assertTrue(model.getShapes().isEmpty());
        model.addShape(new Triangle("triangle1", 10, 20, 100, 200, 50, 79, 0, 1, "red", "blue", true));
        assertFalse(model.getShapes().isEmpty());
        assertTrue(model.getShapes().peek() instanceof Triangle);
    }

    @Test
    public void createAllShapes() {
        assertTrue(model.getShapes().isEmpty());
        model.addShape(new Rectangle("rect1", 10, 20, 100, 200, 0, 1, "red", "blue", true));
        assertTrue(model.getShapes().peek() instanceof Rectangle);
        assertEquals(1, model.getShapes().size());
        model.addShape(new Ellipse("ellipse1", 10, 20, 100, 200, 0, 1, "red", "blue", true));
        assertTrue(model.getShapes().peek() instanceof Ellipse);
        assertEquals(2, model.getShapes().size());
        model.addShape(new Line("line1", 10, 20, 100, 200, 0, 1, "red", true));
        assertTrue(model.getShapes().peek() instanceof Line);
        assertEquals(3, model.getShapes().size());
        model.addShape(new Triangle("triangle1", 10, 20, 100, 200, 50, 79, 0, 1, "red", "blue", true));
        assertTrue(model.getShapes().peek() instanceof Triangle);
        assertEquals(4, model.getShapes().size());
    }

    @Test
    public void checkID() {
        createRectangle();
        assertEquals("rect1", model.getShape("rect1").getID());
    }

    @Test
    public void checkPosition() {
        createRectangle();
        assertArrayEquals(new int[] {10, 20}, model.getShape("rect1").getPosition());
    }

    @Test
    public void checkDimensions() {
        createRectangle();
        assertArrayEquals(new int[] {100, 200}, model.getShape("rect1").getDimensions());
    }

    @Test
    public void checkRotation() {
        createRectangle();
        assertEquals(0, model.getShape("rect1").getRotation());
    }

    @Test
    public void checkLineWeight() {
        createRectangle();
        assertEquals(1, model.getShape("rect1").getLineWeight());
    }

    @Test
    public void checkLineColour() {
        createRectangle();
        assertEquals("red", model.getShape("rect1").getLineColour());
    }

    @Test
    public void checkFill() {
        createRectangle();
        assertEquals("blue", model.getShape("rect1").getFill());
    }

    @Test
    public void checkRectangleJson() {
        createRectangle();
        JsonObjectBuilder propertiesBuilder = Json.createObjectBuilder()
        .add("width", 100)
        .add("height", 200)
        .add("rotation", 0)
        .add("borderWidth", 1)
        .add("borderColor", "red")
        .add("fillColor", "blue");
        JsonObject json = Json.createObjectBuilder()
            .add("type", "rectangle")
            .add("isOwner", true)
            .add("id", "rect1")
            .add("x", 10)
            .add("y", 20)
            .add("properties", propertiesBuilder)
            .build();
        assertEquals(json, model.getShape("rect1").toJson());
    }

    @Test
    public void checkEllipseJson() {
        createEllipse();
        JsonObjectBuilder propertiesBuilder = Json.createObjectBuilder()
        .add("width", 100)
        .add("height", 200)
        .add("rotation", 0)
        .add("borderWidth", 1)
        .add("borderColor", "red")
        .add("fillColor", "blue");
        JsonObject json = Json.createObjectBuilder()
            .add("type", "ellipse")
            .add("isOwner", true)
            .add("id", "ellipse1")
            .add("x", 10)
            .add("y", 20)
            .add("properties", propertiesBuilder)
            .build();
        assertEquals(json, model.getShape("ellipse1").toJson());
    }

    @Test
    public void checkLineJson() {
        createLine();
        JsonObjectBuilder propertiesBuilder = Json.createObjectBuilder()
        .add("x2", 100)
        .add("y2", 200)
        .add("rotation", 0)
        .add("lineWidth", 1)
        .add("lineColor", "red");
        JsonObject json = Json.createObjectBuilder()
            .add("type", "line")
            .add("isOwner", true)
            .add("id", "line1")
            .add("x", 10)
            .add("y", 20)
            .add("properties", propertiesBuilder)
            .build();
        assertEquals(json, model.getShape("line1").toJson());
    }

    @Test
    public void checkTriangleJson() {
        createTriangle();
        JsonObjectBuilder propertiesBuilder = Json.createObjectBuilder()
        .add("x2", 100)
        .add("y2", 200)
        .add("x3", 50)
        .add("y3", 79)
        .add("rotation", 0)
        .add("borderWidth", 1)
        .add("borderColor", "red")
        .add("fillColor", "blue");
        JsonObject json = Json.createObjectBuilder()
            .add("type", "triangle")
            .add("isOwner", true)
            .add("id", "triangle1")
            .add("x", 10)
            .add("y", 20)
            .add("properties", propertiesBuilder)
            .build();
        assertEquals(json, model.getShape("triangle1").toJson());
    }

    @Test
    public void checkSetID() {
        createRectangle();
        model.getShape("rect1").setID("rect2");
        assertEquals("rect2", model.getShape("rect2").getID());
    }

    @Test
    public void checkSetPosition() {
        createRectangle();
        model.getShape("rect1").setPosition(500, 500);
        assertArrayEquals(new int[] {500, 500}, model.getShape("rect1").getPosition());
    }

    @Test
    public void checkSetRotation() {
        createRectangle();
        model.getShape("rect1").setRotation(90);
        assertEquals(90, model.getShape("rect1").getRotation());
    }

    @Test
    public void checkSetLineWeight() {
        createRectangle();
        model.getShape("rect1").setLineWeight(5);
        assertEquals(5, model.getShape("rect1").getLineWeight());
    }

    @Test
    public void checkSetLineColour() {
        createRectangle();
        model.getShape("rect1").setColour("green");
        assertEquals("green", model.getShape("rect1").getLineColour());
    }

    @Test
    public void checkSetFill() {
        createRectangle();
        model.getShape("rect1").setFill("yellow");
        assertEquals("yellow", model.getShape("rect1").getFill());
    }

    @Test
    public void checkSetResize() {
        createRectangle();
        model.getShape("rect1").resize(200, 300);
        assertArrayEquals(new int[] {200, 300}, model.getShape("rect1").getDimensions());
    }

    @Test
    public void checkCentre() {
        createRectangle();
        assertArrayEquals(new int[] {60, 120}, model.getShape("rect1").centre());
    }

    @Test
    public void getShapes() {
        createAllShapes();
        assertEquals(4, model.getShapes().size());
        assertEquals("rect1", model.getShape("rect1").getID());
        assertEquals("ellipse1", model.getShape("ellipse1").getID());
        assertEquals("line1", model.getShape("line1").getID());
        assertEquals("triangle1", model.getShape("triangle1").getID());
    }
    
    @Test
    public void undoSingleShapeCreation() {
        assertTrue(model.getShapes().isEmpty());
        Rectangle rect = new Rectangle("rect1", 10, 20, 100, 200, 0, 1, "red", "blue", true);
        model.addShape(rect);
        model.pushToHistory(rect.toJson());
        assertFalse(model.getShapes().isEmpty());
        assertEquals(1, model.getShapes().size());
        model.undo();
        assertEquals(0, model.getShapes().size());
    }

    @Test
    public void undoMultipleShapeCreation() {
        Rectangle rect = new Rectangle("rect1", 10, 20, 100, 200, 0, 1, "red", "blue", true);
        model.addShape(rect);
        model.pushToHistory(rect.toJson());
        Triangle triangle = new Triangle("triangle1", 10, 20, 100, 200, 50, 79, 0, 1, "red", "blue", true);
        model.addShape(triangle);
        model.pushToHistory(triangle.toJson());
        Ellipse ellipse = new Ellipse("ellipse1", 10, 20, 100, 200, 0, 1, "red", "blue", true);
        model.addShape(ellipse);
        model.pushToHistory(ellipse.toJson());
        Line line = new Line("line1", 10, 20, 100, 200, 0, 1, "red", true);
        model.addShape(line);
        model.pushToHistory(line.toJson());
    }

    @Test
    public void clear() {
        createAllShapes();
        assertEquals(4, model.getShapes().size());
        model.clear();
        assertTrue(model.getShapes().isEmpty());
    }
}
