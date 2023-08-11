package gui;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.*;

import model.Object2D;
import model.Model;
import model.objects.Image;
import model.objects.shapes.Ellipse;
import model.objects.shapes.Line;
import model.objects.shapes.Rectangle;
import model.objects.Shape;

/**
 * A view class that displays a GUI on screen. This GUI is controlled by an instance of the Controller class. The main
 * function of this view class is to display all of the objects present within the models objects stack. The GUI is built
 * with the Swing API and the View class is a subclass of the JPanel object. This creates a window that and provides
 * methods that allow objects to be drawn. This class also handles mouse events and converting user inputs into values
 * used to then create a shape within the Model class.
 */
public class View extends JPanel {

    private static final int[] VIEW_DIMENSIONS = new int[] {1000, 700};
    private static final Color DEFAULT_BG_COLOUR = new Color(48, 48, 48);
    private static final Color DEFAULT_FG_COLOUR = Color.white;
    private static final int DEFAULT_LINE_WEIGHT = 4;
    private static final String DEFAULT_SHAPE = "rectangle";

    private Model model;

    // An image of the current GUI which is then saved if the user selects to export to a png. This image is updated
    // along with the window.
    private BufferedImage exportImage = new BufferedImage(VIEW_DIMENSIONS[0], VIEW_DIMENSIONS[1], BufferedImage.TYPE_INT_RGB);

    private Point mPos0; // The first position on the window that a mouse action occurs.
    private Point mPos1; // The second position on the window that a mouse action occurs.
    // Used when dragging left mouse button and displaying preview of shape to draw and o stop right mouse button from
    // drawing shapes when it has been clicked.
    private boolean leftDragging = false;

    private Color currentColour = DEFAULT_FG_COLOUR;
    private int currentLineWeight = DEFAULT_LINE_WEIGHT;
    private String currentShape = DEFAULT_SHAPE;

    // A list storing all of the rendered shapes, used when checking if the mouse is positioned over a certain object.
    private ArrayList<java.awt.Shape> graphicObjects;

    public View(Model model) {
        this.model = model;
        setupMouseEvents();
    }

    public BufferedImage getExportImage() {
        return exportImage;
    }

    public int[] getDimensions() {
        return VIEW_DIMENSIONS;
    }

    public void updateColour(Color colour) {
        currentColour = colour;
    }

    public void updateShape(String shape) {
        currentShape = shape;
    }

    public int[] getRGBColourComponents(Color color) {
        return new int[] {color.getRed(), color.getGreen(), color.getBlue()};
    }

    /**
     * Draws all objects to the GUI window and also the exportImage BufferedImage. This method draws objects based on
     * their attributes, changing the line weight, line colour, fill colour amd rotation. The method is called internally
     * from the inherited repaint() method.
     * @param gfx a graphics object passed in by the repaint() method that draws to the JFrame window.
     */
    public void paint(Graphics gfx) {
        // First graphics instance for drawing to display, second for drawing to the export image
        for (Graphics graphic : new Graphics[] {gfx, exportImage.getGraphics()}) {

            Graphics2D graphics2D = (Graphics2D) graphic;
            graphics2D.setColor(DEFAULT_BG_COLOUR);
            graphics2D.fillRect(0, 0, VIEW_DIMENSIONS[0], VIEW_DIMENSIONS[1]);
            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            graphicObjects = new ArrayList<>();

            graphics2D.setColor(currentColour);
            graphics2D.setStroke(new BasicStroke(currentLineWeight));

            for (Object2D object : model.getObjects()) {

                if (object instanceof Image) {
                    Image image = (Image) object;
                    graphics2D.drawImage(image.image().getScaledInstance(image.getWidth(), image.getHeight(), 0), image.getX(), image.getY(), this);
                    graphicObjects.add(new java.awt.Rectangle(image.getX(), image.getY(), image.getWidth(), image.getHeight()));
                }

                if (object instanceof Shape) {
                    Shape shape = (Shape) object;
                    java.awt.Shape drawShape;

                    if (shape instanceof Ellipse) {
                        Ellipse ellipse = (Ellipse) shape;
                        drawShape = new Ellipse2D.Double(ellipse.getX(), ellipse.getY(), ellipse.getWidth(), ellipse.getHeight());
                    }
                    else if (shape instanceof Rectangle) {
                        Rectangle rectangle = (Rectangle) shape;
                        drawShape = new java.awt.Rectangle(rectangle.getX(), rectangle.getY(), rectangle.getWidth(), rectangle.getHeight());
                    }
                    else {
                        drawShape = new java.awt.Polygon(shape.getXVerts(), shape.getYVerts(), shape.vertSize());
                    }

                    // Performing object rotation around objects centre
                    AffineTransform transform = new AffineTransform();
                    transform.rotate(Math.toRadians(shape.getRotation()), shape.centreX(), shape.centreY());
                    graphicObjects.add(transform.createTransformedShape(drawShape));

                    if (shape.getFill() != null) {
                        graphics2D.setColor(new Color(shape.getFill()[0], shape.getFill()[1], shape.getFill()[2]));
                        graphics2D.fill(graphicObjects.get(graphicObjects.size() - 1));
                    }
                    graphics2D.setColor(new Color(shape.getColour()[0], shape.getColour()[1], shape.getColour()[2]));
                    graphics2D.setStroke(new BasicStroke(shape.getLineWeight()));
                    graphics2D.draw(graphicObjects.get(graphicObjects.size() - 1)); // where a Shape subclass is finally drawn on the screen
                }
            }
        }
    }

    /**
     * Creates a popup menu when the user has right clicked on an object. This popup menu is placed under the cursor and
     * functions to provide easy access to individual operations that can be performed to objects. These operations
     * include changing the line colour, line thickness, fill colour and rotation to shapes, and resize and translation
     * operations to all objects.
     * @param object Object that has right clicked
     * @param e Mouse event that can be used to obtain position where menu should be shown
     */
    private void initPopupMenu(Object2D object, MouseEvent e) {
        JPopupMenu menu = new JPopupMenu();
        if (object instanceof Shape) {

            JMenuItem colour = new JMenuItem("Colour");
            colour.addActionListener(new AbstractAction() {
                public void actionPerformed(ActionEvent e) {

                    int[] colour = getRGBColourComponents(JColorChooser.showDialog(menu, "Line Colour", currentColour));
                    if (colour != null) {
                        ((Shape) object).setColour(colour);
                    }
                    repaint();
                }
            });

            JMenuItem fill = new JMenuItem("Fill");
            fill.addActionListener(new AbstractAction() {
                public void actionPerformed(ActionEvent e) {

                    int[] colour = getRGBColourComponents(JColorChooser.showDialog(menu, "Fill Colour", currentColour));
                    if (colour != null) {
                        ((Shape) object).setFill(colour);
                    }
                    repaint();
                }
            });

            JMenuItem lineWeight = new JMenuItem("Line Weight");
            lineWeight.addActionListener(new AbstractAction() {
                public void actionPerformed(ActionEvent e) {

                    int lineThickness = Integer.parseInt(JOptionPane.showInputDialog("Line thickness: "));
                    ((Shape) object).setLineWeight(lineThickness);
                    repaint();
                }
            });

            JMenuItem rotate = new JMenuItem("Rotate");
            rotate.addActionListener(new AbstractAction() {
                public void actionPerformed(ActionEvent e) {

                    int angle = Integer.parseInt(JOptionPane.showInputDialog("Rotation angle (°): "));
                    ((Shape) object).setRotation(angle);
                    repaint();
                }
            });

            menu.add(colour);
            menu.add(fill);
            menu.add(lineWeight);
            menu.add(rotate);
        }

        // Separating the popup menu by operations that can only be applied to Shapes and operations
        // that can be applied to all Object2D's.
        JMenuItem resize = new JMenuItem("Resize");
        resize.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {

                if (!(object instanceof Line)) {
                    int width = Integer.parseInt(JOptionPane.showInputDialog("New width: "));
                    int height = Integer.parseInt(JOptionPane.showInputDialog("New height: "));
                    object.resize(width, height);
                }
                else {
                    int size = Integer.parseInt(JOptionPane.showInputDialog("New size: "));
                    int angle = Integer.parseInt(JOptionPane.showInputDialog("New direction (°): "));
                    object.resize(size, angle);
                }
                repaint();
            }
        });

        JMenuItem translate = new JMenuItem("Translate");
        translate.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {

                int x = Integer.parseInt(JOptionPane.showInputDialog("Translation along x axis: "));
                int y = Integer.parseInt(JOptionPane.showInputDialog("Translation along y axis: "));
                object.translate(x, y);
                repaint();
            }
        });

        menu.add(resize);
        menu.add(translate);
        menu.show(e.getComponent(), e.getX(), e.getY()); // Displaying the popup menu by the mouse cursor
    }

    /**
     * Outlines functions to perform on different mouse events.
     */
    private void setupMouseEvents() {

        this.addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent e) {

                mPos0 = e.getPoint();
                // If the right mouse button is pressed and the mouse intersects an object drawn on the window,
                // initialise a popup menu at the mouse's position.
                if (e.getModifiersEx() == MouseEvent.BUTTON3_DOWN_MASK) {
                    for (int i = 0; i < graphicObjects.size(); i++) {
                        if (graphicObjects.get(i).intersects(mPos0.x, mPos0.y, 1, 1)) {
                            initPopupMenu(model.getObjects().get(i), e);
                        }
                    }
                }
            }

            public void mouseReleased(MouseEvent e) {

                // If the left mouse button has been released from dragging then remove the latest object added to the
                // object stack (this will be a shape temporarily created to allow the user to visualise their shape
                // in the window before its created) and then create a new shape (which will then automatically be drawn)
                // by the Model class firing a draw event.
                if (leftDragging) {
                    mPos1 = e.getPoint();
                    int[] colour = {currentColour.getRed(), currentColour.getGreen(), currentColour.getBlue()};
                    model.removeObject();
                    model.createShape(currentShape, mPos0.x, mPos0.y, mPos1.x, mPos1.y, 0, currentLineWeight, colour, null);
                    leftDragging = false;
                }
            }
        });

        this.addMouseMotionListener(new MouseMotionAdapter() {

            public void mouseDragged(MouseEvent e) {

                // If the left mouse button is down and the mouse is being dragged, then draw a temporary outline of the
                // shape. This is done by removing the latest shape to be added to the object stack (only if the left
                // mouse button is being dragged) and then creating a new shape with the coordinates being based on the
                // new mouse position. Therefore the shape is always being drawn to the mouse position and so the user
                // knows what it will look like.
                if (e.getModifiersEx() == MouseEvent.BUTTON1_DOWN_MASK) {
                    mPos1 = e.getPoint();
                    int[] colour = {currentColour.getRed(), currentColour.getGreen(), currentColour.getBlue()};
                    if (leftDragging) {
                        model.removeObject();
                    }
                    leftDragging = true;
                    model.createShape(currentShape, mPos0.x, mPos0.y, mPos1.x, mPos1.y, 0, currentLineWeight, colour, null);
                }
            }
        });
    }


}