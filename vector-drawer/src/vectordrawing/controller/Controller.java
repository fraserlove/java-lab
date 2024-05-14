package vectordrawing.controller;

import vectordrawing.networking.Client;
import vectordrawing.model.Model;
import vectordrawing.model.Shape;
import vectordrawing.model.shapes.Ellipse;
import vectordrawing.model.shapes.Line;
import vectordrawing.model.shapes.Rectangle;
import vectordrawing.model.shapes.Triangle;
import vectordrawing.utils.FileManager;
import vectordrawing.utils.JsonUtils;
import vectordrawing.view.View;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputListener;

/**
 * The controller for the vector drawing application which listens to user actions performed
 * in the view and updates the model accordingly.
 */
public class Controller implements PropertyChangeListener, MouseInputListener, KeyListener {

    private Client client;
    private Model model;
    private View view;

    // Track mouse and key movements.
    private int initX;
    private int initY;
    private boolean dragging;
    private boolean shiftPressed;

    // Current shape being edited.
    private Shape currentShape;

    /**
     * Creates a new controller for the vector drawing application.
     * 
     * @param model Model for the vector drawing application.
     * @param view View for the vector drawing application.
     */
    public Controller(Client client, Model model, View view) {
        this.client = client;
        this.model = model;
        this.view = view;
        this.dragging = false;
        this.shiftPressed = false;
        this.currentShape = null;
        this.addListeners();
        this.fetchShapes();
    }

    /**
     * Adds listeners to the model, canvas and buttons in the view.
     */
    private void addListeners() {
        // Add listener to the model.
        this.model.addListener(this);

        // Add listeners to the canvas in the view.
        this.view.getCanvas().addMouseListener(this);
        this.view.getCanvas().addMouseMotionListener(this);
        this.view.getCanvas().addKeyListener(this);
        this.view.getCanvas().requestFocus();

        // Add listeners to the buttons in the view.
        this.view.addExportActionListener(e -> this.export());
        this.view.addSaveActionListener(e -> this.save());
        this.view.addOpenActionListener(e -> this.open());
        this.view.addUndoActionListener(e -> this.model.undo());
        this.view.addRedoActionListener(e -> this.model.redo());
        this.view.addClearActionListener(e -> this.model.clear());
        this.view.addLineActionListener(e -> this.view.getCanvas().setShape("line"));
        this.view.addRectangleActionListener(e -> this.view.getCanvas().setShape("rectangle"));
        this.view.addTriangleActionListener(e -> this.view.getCanvas().setShape("triangle"));
        this.view.addEllipseActionListener(e -> this.view.getCanvas().setShape("ellipse"));
        this.view.addChangeColourActionListener(e -> this.changeLineColour());
        this.view.addChangeFillActionListener(e -> this.changeFill());
        this.view.addLineWeightActionListener(e -> this.changeLineWeight());
        this.view.addRotateActionListener(e -> this.rotateShape());
        this.view.addResizeActionListener(e -> this.resize());
        this.view.addTranslateActionListener(e -> this.translate());
        this.view.addColourActionListener(e -> this.view.getCanvas().setLineColour((String) ((JComboBox<String>) e.getSource()).getSelectedItem()));
        this.view.addFillActionListener(e -> this.view.getCanvas().setFill((String) ((JComboBox<String>) e.getSource()).getSelectedItem()));
    }

    /**
     * Fetches and adds the all the shapes from the server.
     */
    private void fetchShapes() {
        try {
            JsonArray shapes = this.client.getShapes();

            for (JsonObject shapeJson : shapes.getValuesAs(JsonObject.class)) {
                try {
                    Shape shape = JsonUtils.shapeFromJson(shapeJson);
                    if (shape != null) {
                        this.addShape(shape, false);
                    }
                } catch (NullPointerException e) {
                    System.out.println(e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Adds a shape to the model and broadcasts a draw event to the view. If the shape is to be
     * sent to the server, it will be assigned an ID and added to the history stack.
     * 
     * @param shape Shape to add.
     * @param sendToServer Whether or not to send the shape to the server.
     */
    private void addShape(Shape shape, boolean sendToServer) {
        try {
            if (sendToServer) {
                shape.setID(this.client.addShape(shape.toJson()));
                this.model.pushToHistory(shape.toJson());
            }
            this.model.addShape(shape);
            draw();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Updates a shape in the model and broadcasts a draw event to the view.
     * 
     * @param shape Shape to update.
     */
    private void updateShape(Shape shape) {
        try {
            client.updateShape(shape.toJson());
            draw();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Sets the current canvas fill colour.
     * 
     * @param colour The fill colour.
     */
    public void setFill(String colour) {
        this.view.getCanvas().setFill(colour);
    }

    /**
     * Opens a .vec file and restores the application to the state it was in when the file was saved.
     */
    private void open() {
        String path = FileManager.manageFile(this.view.getWindow(), false, true);
        if (path != null) {
            this.model.clear();
            try (JsonReader reader = Json.createReader(new FileInputStream(path))) {
                JsonArray jsonArray = reader.readArray();
                for (JsonObject json : jsonArray.getValuesAs(JsonObject.class)) {
                    this.addShape(JsonUtils.shapeFromJson(json), true);
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Saves the current state of the application to a .vec file. This file can then be opened
     * to restore the application to the state it was in when the file was saved.
     */
    private void save() {
        String path = FileManager.manageFile(this.view.getWindow(), false, false);
        if (path != null) {
            try (FileWriter fileWriter = new FileWriter(path.replace(".vec", "") + ".vec")) {
                JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
                for (Shape shape : this.model.getShapes()) {
                    arrayBuilder.add(shape.toJson());
                }
                JsonArray jsonArray = arrayBuilder.build();
                fileWriter.write(jsonArray.toString());
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Exports the current vector drawing as an image. The user can choose between .png and .jpeg.
     * The user can also choose the file name and location. If the user does not specify a file
     * extension, the file will be saved as a .png.
     */
    private void export() {
        String path = FileManager.manageFile(this.view.getWindow(), true, false);
        String extension = path.endsWith(".jpeg") ? "jpeg" : "png";
        path = path.replace(".png", "") + ".png";
        FileManager.exportAsImage(this.view.getWindow(), path, extension);
    }

    /**
     * Changes the line colour of the current shape by a user defined colour.
     */
    private void changeLineColour() {
        String colour = JOptionPane.showInputDialog("Line Colour: ");
        if (colour != null) {
            this.model.pushToHistory(this.currentShape.toJson());
            (this.currentShape).setColour(colour);
            this.updateShape(this.currentShape);
        }
    }

    /**
     * Changes the fill colour of the current shape by a user defined colour.
     */
    private void changeFill() {
        String colour = JOptionPane.showInputDialog("Fill Colour: ");
        if (colour != null) {
            this.model.pushToHistory(this.currentShape.toJson());
            (this.currentShape).setFill(colour);
            this.updateShape(this.currentShape);
        }
    }

    /**
     * Changes the line weight of the current shape by a user defined line-weight.
     */
    private void changeLineWeight() {
        int lineWeight = Integer.parseInt(JOptionPane.showInputDialog("Line Weight: "));
        if (lineWeight >= 0 && lineWeight <= Shape.MAX_LINE_WEIGHT) {
            this.model.pushToHistory(this.currentShape.toJson());
            (this.currentShape).setLineWeight(lineWeight);
            this.updateShape(this.currentShape);
        }
    }

    /**
     * Rotates the current shape by user defined angle.
     */
    private void rotateShape() {
        int rotation = Integer.parseInt(JOptionPane.showInputDialog("Rotation: "));
        if (rotation >= 0) {
            this.model.pushToHistory(this.currentShape.toJson());
            (this.currentShape).setRotation(rotation % Shape.MAX_ROTATION);
            this.updateShape(this.currentShape);
        }
    }

    /**
     * Resizes the current shape by user defined width and height.
     */
    private void resize() {
        if (this.currentShape instanceof Triangle) {
            int x2 = Integer.parseInt(JOptionPane.showInputDialog("x2: "));
            int y2 = Integer.parseInt(JOptionPane.showInputDialog("y2: "));
            int x3 = Integer.parseInt(JOptionPane.showInputDialog("x3: "));
            int y3 = Integer.parseInt(JOptionPane.showInputDialog("y3: "));
            if (Math.abs(x2) <= Shape.MAX_COORDINATE && Math.abs(y2) <= Shape.MAX_COORDINATE && Math.abs(x3) <= Shape.MAX_COORDINATE && Math.abs(y3) <= Shape.MAX_COORDINATE) {
                this.model.pushToHistory(this.currentShape.toJson());
                this.currentShape.resize(x2, y2, x3, y3);
            }
        } else if (this.currentShape instanceof Line) {
            int length = Integer.parseInt(JOptionPane.showInputDialog("Length: "));
            int angle = Integer.parseInt(JOptionPane.showInputDialog("Angle: "));
            if (Math.abs(length) <= Shape.MAX_DIMENSION && angle >= 0) {
                this.model.pushToHistory(this.currentShape.toJson());
                this.currentShape.resize(length, angle % Shape.MAX_ROTATION);
            }
        } else {
            int width = Integer.parseInt(JOptionPane.showInputDialog("Width: "));
            int height = Integer.parseInt(JOptionPane.showInputDialog("Height: "));
            if (Math.abs(width) <= Shape.MAX_DIMENSION && Math.abs(height) <= Shape.MAX_DIMENSION) {
                this.model.pushToHistory(this.currentShape.toJson());
                this.currentShape.resize(width, height);
            }
        }
        this.updateShape(this.currentShape);
    }

    /**
     * Translates the current shape by user defined x and y coordinates.
     */
    private void translate() {
        int x = Integer.parseInt(JOptionPane.showInputDialog("X: "));
        int y = Integer.parseInt(JOptionPane.showInputDialog("Y: "));
        if (Math.abs(x) <= Shape.MAX_COORDINATE && Math.abs(y) <= Shape.MAX_COORDINATE) {
            this.model.pushToHistory(this.currentShape.toJson());
            this.currentShape.setPosition(x, y);
            this.updateShape(this.currentShape);
        }
    }

    /**
     * Create a shape with the specified attributes.
     * 
     * @param sendToServer If the shape should be sent to the server.
     * @param newX The new x coordinate.
     * @param newY The new y coordinate.
     */
    public void createShape(boolean sendToServer, int newX, int newY) {
        int lineWeight = this.view.getCanvas().getLineWeight();
        String colour = this.view.getCanvas().getLineColour();
        String fill = this.view.getCanvas().getFill();
        int width = this.shiftPressed ? Math.max(newX - initX, newY - initY) : newX - initX;
        int height = this.shiftPressed ? Math.max(newX - initX, newY - initY) : newY - initY;
        switch (this.view.getCanvas().getShape()) {
            case "line":
                Line line = new Line("", initX, initY, newX, newY, 0, lineWeight, colour, true);
                this.addShape(line, sendToServer);
                break;
            case "rectangle":
                Rectangle rectangle = new Rectangle("", initX, initY, width, height, 0, lineWeight, colour, fill, true);
                this.addShape(rectangle, sendToServer);
                break;
            case "triangle":
                Triangle triangle = new Triangle("", initX, initY, newX, newY, initX, newY, 0, lineWeight, colour, fill, true);
                this.addShape(triangle, sendToServer);
                break;
            case "ellipse":
                Ellipse ellipse = new Ellipse("", initX, initY, width, height, 0, lineWeight, colour, fill, true);
                this.addShape(ellipse, sendToServer);
                break;
            default:
                break;
        }
    }

    private void draw() {
        SwingUtilities.invokeLater(() -> {
            view.getCanvas().repaint();
        });
    }

    /**
     * Update the view when the model fires a PropertyChangeEvent.
     * 
     * @param e PropertyChangeEvent fired by model class.
     */
    @Override
    public void propertyChange(PropertyChangeEvent e) {
        if (e.getPropertyName().equals("draw")) {
            draw();
        }
        if (e.getPropertyName().equals("remove")) {
            try {
                this.client.deleteShape((String) e.getNewValue());
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
        if (e.getPropertyName().equals("update")) {
            this.updateShape(JsonUtils.shapeFromJson((JsonObject) e.getNewValue()));
        }
        if (e.getPropertyName().equals("add")) {
            this.addShape(JsonUtils.shapeFromJson((JsonObject) e.getNewValue()), true);
        }
    }

    /**
     * If the mouse is pressed, store the initial mouse position. If this press was with the
     * right mouse button over a shape, display a popup menu.
     * 
     * @param e MouseEvent.
     */
    @Override
    public void mousePressed(MouseEvent e) {
        // Store the initial mouse position.
        this.initX = e.getX();
        this.initY = e.getY();
        // Display a popup menu on right click over shape.
        if (e.getModifiersEx() == MouseEvent.BUTTON3_DOWN_MASK) {
            // Check if the mouse is over any shape, most recent shapes first.
            for (int i = this.model.getShapes().size() - 1; i >= 0; i--) {
                Shape shape = this.model.getShapes().get(i);
                java.awt.Shape graphicShape = this.view.getCanvas().getGraphicShapes().get(shape);
                if (graphicShape.contains(e.getX(), e.getY())) {
                    this.currentShape = shape;
                    this.view.showPopup(e);
                    break;
                }
            }
        }
    }

    /**
     * If the mouse is released, create the shape and add it to the model, remove the
     * previously drawn temporary shape.
     * 
     * @param e MouseEvent.
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            if (dragging) {
                this.model.removeShape();
            }
            createShape(true, e.getX(), e.getY());
            dragging = false;
        }
    }

    /**
     * If the mouse is dragged, create a temporary shape and add it to the model and
     * remove the previously drawn temporary shape.
     * 
     * @param e MouseEvent.
     */
    @Override
    public void mouseDragged(MouseEvent e) {
        // Check if the left mouse button is down (regardless of other modifiers).
        if ((e.getModifiersEx() & MouseEvent.BUTTON1_DOWN_MASK) == MouseEvent.BUTTON1_DOWN_MASK) {
            if (this.dragging) {
                this.model.removeShape();
            }
            createShape(false, e.getX(), e.getY());
            dragging = true;
        }
    }

    /**
     * Do nothing.
     * 
     * @param e MouseEvent.
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        return;
    }

    /**
     * Do nothing.
     * 
     * @param e MouseEvent.
     */
    @Override
    public void mouseEntered(MouseEvent e) {
        return;
    }

    /**
     * Do nothing.
     * 
     * @param e MouseEvent.
     */
    @Override
    public void mouseExited(MouseEvent e) {
        return;
    }

    /**
     * Do nothing.
     * 
     * @param e MouseEvent.
     */
    @Override
    public void mouseMoved(MouseEvent e) {
        return;
    }

    /**
     * Do nothing.
     * 
     * @param e KeyEvent.
     */
    @Override
    public void keyTyped(KeyEvent e) {
        return;
    }

    /**
     * Check if the shift key is pressed.
     * 
     * @param e KeyEvent.
     */
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
            this.shiftPressed = true;
        }
    }

    /**
     * Check if the shift key is released.
     * 
     * @param e KeyEvent.
     */
    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
            this.shiftPressed = false;
        }
    }
}
