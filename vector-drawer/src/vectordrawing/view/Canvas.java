package vectordrawing.view;

import vectordrawing.model.Model;
import vectordrawing.model.Shape;
import vectordrawing.model.shapes.Ellipse;
import vectordrawing.utils.Extra;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.util.HashMap;

import javax.swing.JPanel;

/**
 * A canvas to draw shapes to.
 */
public class Canvas extends JPanel {

    // Link to the root GUI view.
    private Model model;

    private int width;
    private int height;

    // Shape attributes.
    private String lineColour;
    private int lineWeight;
    private String fill;
    private String shape;

    // Stores the shapes and their corresponding graphic shapes.
    private HashMap<Shape, java.awt.Shape> graphicShapes;

    /**
     * Creates a canvas to draw shapes to.
     * 
     * @param model Model of the application.
     * @param width Width of the canvas.
     * @param height Height of the canvas.
     */
    public Canvas(Model model, int width, int height) {
        this.model = model;
        this.width = width;
        this.height = height;
        this.lineColour = Shape.DEFAULT_COLOUR;
        this.lineWeight = Shape.DEFAULT_LINE_WEIGHT;
        this.fill = Shape.DEFAULT_COLOUR;
        this.shape = Shape.DEFAULT_SHAPE;
        this.graphicShapes = new HashMap<Shape, java.awt.Shape>();
    }

    /**
     * Sets the line colour of the shape.
     * 
     * @param colour Line colour of the shape.
     */
    public void setLineColour(String colour) {
        this.lineColour = colour;
    }

    /**
     * Sets the fill colour of the shape.
     * 
     * @param fill Fill colour of the shape.
     */
    public void setFill(String fill) {
        this.fill = fill;
    }

    /**
     * Sets the shape type.
     * 
     * @param shape Shape type.
     */
    public void setShape(String shape) {
        this.shape = shape;
    }

    /**
     * Sets the line weight of the shape.
     * 
     * @param lineWeight Line weight of the shape.
     */
    public void setLineWeight(int lineWeight) {
        this.lineWeight = lineWeight;
    }

    /**
     * Gets the line colour of the shape.
     * 
     * @return Line colour of the shape.
     */
    public String getLineColour() {
        return lineColour;
    }

    /**
     * Gets the fill colour of the shape.
     * 
     * @return Fill colour of the shape.
     */
    public String getShape() {
        return shape;
    }

    /**
     * Gets the line weight of the shape.
     * 
     * @return Line weight of the shape.
     */
    public int getLineWeight() {
        return lineWeight;
    }

    /**
     * Gets the fill colour of the shape.
     * 
     * @return Fill colour of the shape.
     */
    public String getFill() {
        return fill;
    }

    /**
     * Gets the graphic shapes.
     * 
     * @return Graphic shapes.
     */
    public HashMap<Shape, java.awt.Shape> getGraphicShapes() {
        return graphicShapes;
    }

    /**
     * Draws a shape on the canvas based on the current attributes.
     * 
     * @param graphics Graphics object to draw to.
     * @param shape Shape to draw.
     */
    private void drawShape(Graphics2D graphics, Shape shape) {
        java.awt.Shape drawShape;
        if (shape instanceof Ellipse) {
            Ellipse ellipse = (Ellipse) shape;
            drawShape = new Ellipse2D.Double(ellipse.getPosition()[0], ellipse.getPosition()[1], ellipse.getDimensions()[0], ellipse.getDimensions()[1]);
        } else {
            drawShape = new java.awt.Polygon(shape.getXs(), shape.getYs(), shape.getXs().length);
        }
        // Performing rotation around the shapes centre.
        AffineTransform transform = new AffineTransform();
        transform.rotate(Math.toRadians(shape.getRotation()), shape.centre()[0], shape.centre()[1]);
        // Drawing the shape.
        graphics.setColor(Extra.stringToColour(shape.getLineColour()));
        graphics.setStroke(new BasicStroke(shape.getLineWeight()));
        graphics.draw(transform.createTransformedShape(drawShape));
        // Filling the shape if it has a fill colour.
        if (shape.getFill() != null) {
            graphics.setColor(Extra.stringToColour(shape.getFill()));
            graphics.fill(transform.createTransformedShape(drawShape));
        }
        this.graphicShapes.put(shape, transform.createTransformedShape(drawShape));
    }

    /**
     * Paints the canvas.
     * 
     * @param graphics Graphics object to draw to.
     */
    @Override
    public void paint(Graphics graphics) {
        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setColor(Color.WHITE);
        graphics2D.fillRect(0, 0, width, height);
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        this.graphicShapes.clear();
        // Drawing all the shapes.
        for (int i = 0; i < this.model.getShapes().size(); i++) {
            drawShape(graphics2D, this.model.getShapes().get(i));
        }
    }
}
