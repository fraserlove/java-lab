package model;

import model.objects.*;
import model.objects.shapes.Ellipse;
import model.objects.shapes.Line;
import model.objects.shapes.Rectangle;
import model.objects.shapes.Triangle;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A model class whose purpose is to store and manipulate all of the application data. The model can call on the GUI
 * controller class to update the GUI after it has performed some operations to the data. This data comes in the form of
 * a stack of Object2D objects. These objects represent an abstract object that can be rendered to the display. There are
 * sub classes of Object2D, an Image and an abstract Shape, with the shape either being a Rectangle, Line, Ellipse or
 * Triangle. This class allows for these objects to be created according to some input from the GUI and can be altered
 * or saved and reloaded from a file.
 */
public class Model {

    // Holds all of the objects that are to be displayed by the GUI. A stack was used so that the most recent objects can
    // be popped off and pushed on with the redo and undo commands.
    private Stack<Object2D> objects = new Stack<>();
    // Holds previously undone objects that can be popped and placed back on the objects stack to be redone.
    private Stack<Object2D> undoneObjects = new Stack<>();
    // Fires change events at observers, notifying them to perform some task.
    private PropertyChangeSupport notifier;

    public Model() {
        notifier = new PropertyChangeSupport(this);
    }

    public Stack<Object2D> getObjects() {
        return objects;
    }

    public void removeObject() {
        objects.pop();
    }

    public void addObserver(PropertyChangeListener listener) {
        notifier.addPropertyChangeListener("draw", listener);
    }

    /**
     * Fires a change event to all observers telling them to draw all objects to the screen after an update within this
     * class.
     */
    private void drawCall() {
        notifier.firePropertyChange("draw", null, null);
    }

    /**
     * Creates a new Shape object. This object can be of subtype Rectangle, Triangle, Line or Ellipse. The method takes
     * in a shape parameter that specifies what subtype of object to initialise.
     * @param shape type of shape to initialise
     * @param x0 x coordinate of the shapes first point
     * @param y0 y coordinate of the shapes first point
     * @param x1 x coordinate of the shapes second point
     * @param y1 y coordinate of the shapes second point
     * @param rotation rotation of the shape in the 2D screen space
     * @param lineWeight line thickness that the shape should be drawn with
     * @param colour line colour of the shape
     * @param fill fill colour of the shape
     */
    public void createShape(String shape, int x0, int y0, int x1, int y1, int rotation, int lineWeight, int[] colour, int[] fill) {
        if (shape.equals("rectangle")) {
            objects.push(new Rectangle(x0, y0, x1, y1, rotation, lineWeight, colour, fill));
        }
        else if (shape.equals("triangle")) {
            objects.push(new Triangle(x0, y0, x1, y1, rotation, lineWeight, colour, fill));
        }
        else if (shape.equals("line")) {
            objects.push(new Line(x0, y0, x1, y1, rotation, lineWeight, colour, fill));
        }
        else if (shape.equals("ellipse")) {
            objects.push(new Ellipse(x0, y0, x1, y1, rotation, lineWeight, colour, fill));
        }
        drawCall();
    }

    /**
     * Imports an image at a specific path by initialising a new Image object and pushes it onto the object stack. This
     * image holds a BufferedImage that can then be displayed on the GUI.
     * @param path path to the image
     * @param x x coordinate of the images position
     * @param y y coordinate of the images position
     * @param dimensions variable argument specifying dimensions that image should be scaled to
     */
    public void importImage(String path, int x, int y, int ... dimensions) {
        try {
            if (dimensions.length == 2) {
                objects.push(new Image(ImageIO.read(new File(path)), path, x, y, dimensions));
            }
            else {
                objects.push(new Image(ImageIO.read(new File(path)), path, x, y));
            }
        }
        catch (IOException ioException) {
            System.out.println("Error occurred when importing image");
        }
        drawCall();
    }

    /**
     * Undoes the addition of the latest object to the display. This is done by popping the latest object added to the
     * stack and then pushing it onto a stack holding all undone objects. A draw call is then created, notifying the GUI
     * to update the display.
     */
    public void undo() {
        if (objects.size() > 0) {
            undoneObjects.push(objects.pop());
            drawCall();
        }
    }

    /**
     * Redoes the addition of the latest object to be added to the display. This is done by pushing the topmost object
     * popped from the undone stack back onto the objects stack. A draw call is then created, notifying the GUI to update
     * the display.
     */
    public void redo() {
        if (undoneObjects.size() > 0) {
            objects.push(undoneObjects.pop());
            drawCall();
        }
    }

    /**
     * Clears all objects on the screen by repeatedly undoing the latest object creation, removing them off the objects
     * stack.
     */
    public void clear() {
        while (objects.size() > 0) {
            undo();
        }
    }

    /**
     * Saves the state of all of the objects in the display to a file. This allows for the state to be loaded in another
     * instance of the application, with all objects and properties remaining constant. The application has its own .vec
     * file extension to which it saves files with.
     * @param path path to the file that the vector data should be written to
     */
    public void save(String path) {

        try {
            // Creating a file writer to create and write to a file at the specified path. First making sure we obtain
            // just the file name (in case the .vec extension has been added already) and then adding the .vec extension.
            // This ensures we dont get any files named "test.vec.vec".
            FileWriter fileWriter = new FileWriter(path.replace(".vec", "") + ".vec");
            for (Object2D object: objects) {
                // Shapes and Images are different sub types of Object2D with different attributes, so are saved
                // differently.
                if (object instanceof Shape) {
                    Shape shape = (Shape) object;

                    fileWriter.append(shape.getType() + " [ ");
                    for (int x : shape.getXVerts()) {
                        fileWriter.append(x + " ");
                    }

                    fileWriter.append("] [ ");
                    for (int y : shape.getYVerts()) {
                        fileWriter.append(y + " ");
                    }

                    fileWriter.append("] [ " + shape.getRotation() + " ] [ " + shape.getLineWeight() + " ] ");
                    fileWriter.append("[ " + shape.getColour()[0] + " " + shape.getColour()[1] + " " + shape.getColour()[2] + " ] ");

                    if (shape.getFill() != null) {
                        fileWriter.append("[ " + shape.getFill()[0] + " " + shape.getFill()[1] + " " + shape.getFill()[2] + " ]");
                    }
                    else {
                        fileWriter.append("[ -1 ]"); // Represents a shape with no fill
                    }

                    fileWriter.append("\n");
                }

                if (object instanceof Image) {
                    Image image = (Image) object;
                    fileWriter.append("image " + image.path() + " [ " + image.getX() + " " + image.getY() + " ] [ " + image.getWidth() + " " + image.getHeight() + " ]\n");
                }
            }
            fileWriter.flush();
            fileWriter.close();
        }
        catch (IOException ioException) {
            System.out.println("File writer produced error when saving file.");
        }
    }

    /**
     * Loads a .vec vector file that contains object data allowing for the reconstruction of a specific state of the
     * application, with all objects and their properties remaining the same.
     * @param path path to the vector file that should be loaded
     */
    public void load(String path) {

        String objectType;

        // Variables for importing shape
        int[] xVerts;
        int[] yVerts;
        int[] colour;
        int[] fill;
        int rotation;
        int lineWeight;

        // Variables for importing image
        String imagePath;
        int x;
        int y;
        int width;
        int height;

        clear(); // Before loading in a file, the previous objects are removed from the display

        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
            String line;
            // Each object is stored on a separate line
            while ((line = bufferedReader.readLine()) != null) {

                // Matches against object properties stored between square brackets in the format below
                // "shapeType [ x0 x1 ] [ y0 y1 ] [ rotation ] [ lineWeight ] [ r g b ] [ r g b ]"
                // "image path [ x y ] [ w h ]"
                Matcher matcher = Pattern.compile("\\[\\s(.*?)\\s\\]").matcher(line);
                objectType = line.split(" ")[0];
                List<String> objectData = new ArrayList<>();
                // Shapes and images are represented differently and so much have separate import procedures
                if (Arrays.asList("rectangle", "line", "triangle", "ellipse").contains(objectType)) {

                    while (matcher.find()) {
                        objectData.add(matcher.group(1));
                    }

                    xVerts = Arrays.stream(objectData.get(0).split(" ")).mapToInt(Integer::parseInt).toArray();
                    yVerts = Arrays.stream(objectData.get(1).split(" ")).mapToInt(Integer::parseInt).toArray();
                    rotation = Integer.parseInt(objectData.get(2).split(" ")[0]);
                    lineWeight = Integer.parseInt(objectData.get(3).split(" ")[0]);
                    colour = Arrays.stream(objectData.get(4).split(" ")).mapToInt(Integer::parseInt).toArray();
                    fill = Arrays.stream(objectData.get(5).split(" ")).mapToInt(Integer::parseInt).toArray();
                    if (fill[0] == -1) {
                        fill = null; // A fill of -1 was used to represent an object with no fill.
                    }
                    createShape(objectType, xVerts[0], yVerts[0], xVerts[1], yVerts[1], rotation, lineWeight, colour, fill);
                }

                else if (objectType.equals("image")) {

                    while (matcher.find()) {
                        objectData.add(matcher.group(1));
                    }

                    imagePath = line.split(" ")[1];
                    x = Integer.parseInt(objectData.get(0).split(" ")[0]);
                    y = Integer.parseInt(objectData.get(0).split(" ")[1]);
                    width = Integer.parseInt(objectData.get(1).split(" ")[0]);
                    height = Integer.parseInt(objectData.get(1).split(" ")[1]);
                    importImage(imagePath, x, y, width, height);
                }
            }
        } catch (IOException ioException) {
            System.out.println("File writer produced error when loading file.");
        }
    }

}