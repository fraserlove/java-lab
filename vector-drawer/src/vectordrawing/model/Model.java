package vectordrawing.model;

import vectordrawing.utils.JsonUtils;

import javax.json.JsonObject;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Stack;

/**
 * The model for the vector drawing application which is responsible for storing all of the shapes
 * that are displayed on the canvas. It also handles the creation, deletion and modification of
 * these shapes as well as the saving and opening of files and broadcasting draw events to the view,
 * notifying it of any changes that have occurred.
 */
public class Model {

    // Stores the shapes that are currently displayed on the canvas.
    private Stack<Shape> shapes = new Stack<>();
    // Stores the history of shapes that have been created.
    private Stack<JsonObject> history = new Stack<>();
    // Stores the history of shapes that have been undone.
    private Stack<JsonObject> future = new Stack<>();

    // Broadcasts events to observers.
    private PropertyChangeSupport notifier;

    /**
     * Creates a new model and initialises the client.
     * 
     * @param client Client to use for server communication.
     */
    public Model() {
        notifier = new PropertyChangeSupport(this);
    }

    /**
     * Registers a listener to be notified when a draw event occurs.
     * 
     * @param listener Listener to register.
     */
    public void addListener(PropertyChangeListener listener) {
        notifier.addPropertyChangeListener("draw", listener);
        notifier.addPropertyChangeListener("add", listener);
        notifier.addPropertyChangeListener("update", listener);
        notifier.addPropertyChangeListener("remove", listener);
    }

    /** 
     * Broadcast a draw action to all listeners.
     */
    private void draw() {
        notifier.firePropertyChange("draw", null, null);
    }

    /**
     * Broadcast a shape addition to all listeners.
     * 
     * @param shape Shape that was added.
     */
    private void add(JsonObject shape) {
        notifier.firePropertyChange("add", null, shape);
    }

    /**
     * Broadcast a shape update to all listeners.
     * 
     * @param shape Shape that was updated.
     */
    private void update(JsonObject shape) {
        notifier.firePropertyChange("update", null, shape);
    }

    /**
     * Broadcast a shape removal to all listeners.
     * 
     * @param shape Shape that was removed.
     */
    private void remove(String id) {
        notifier.firePropertyChange("remove", null, id);
    }

    /**
     * Pushes a shape to the history stack.
     * 
     * @param shape Shape to push to the history stack.
     */
    public void pushToHistory(JsonObject shape) {
        this.history.push(shape);
    }

    /**
     * Returns a stack of all the shapes.
     * 
     * @return Stack of all the shapes.
     */
    public Stack<Shape> getShapes() {
        return this.shapes;
    }

    /**
     * Gets a shape by its ID.
     * 
     * @param id ID of the shape to get.
     * @return Shape with the given ID, or null if no shape with that ID exists.
     */
    public Shape getShape(String id) {
        for (Shape shape : this.shapes) {
            if (shape.getID().equals(id)) {
                return shape;
            }
        }
        return null;
    }

    /**
     * Adds a shape to the model.
     * 
     * @param shape Shape to add.
     */
    public void addShape(Shape shape) {
        this.shapes.push(shape);
    }

    /**
     * Removes the most recent shape from the model.
     */
    public void removeShape() {
        this.shapes.pop();
    }

    /**
     * Checks the history to see if a shape has been previously added or modified.
     * 
     * @param id ID of the shape to check.
     * @return Number of times the shape has been added.
     */
    private boolean shapeInHistory(String id) {
        for (JsonObject shape : this.history) {
            if (shape.getString("id").equals(id)) {
                return true;
            }
        }
        return false;
    }

    public int getHistorySize() {
        return this.history.size();
    }

    public int getFutureSize() {
        return this.future.size();
    }

    /**
     * Undoes the latest action by removing the latest change from the record stack and pushing it onto
     * the undone history stack, then removing the new shape from the shapes map and putting back the old one.
     */
    public void undo() {
        if (!history.isEmpty()) {
            JsonObject undoJson = history.pop();
            String id = undoJson.getString("id");
            Shape undoShape = this.getShape(id);

            // Shape creation action, delete the shape.
            if (!shapeInHistory(id)) {
                // Save the current state to future, remove the shape from the model and delete it from the server.
                this.future.push(undoShape.toJson());
                this.shapes.pop();
                this.remove(id);
            } else { // Shape property change action, undo the change.
                // Save the current state to future, undo the change and update the shape on the server.
                this.future.push(undoShape.toJson());
                JsonUtils.updateShapeFromJson(undoShape, undoJson);
                this.update(undoShape.toJson());
            }
            this.draw();
        }
    }

    /**
     * Redoes the latest action creation by removing the latest shape from the undone shapes
     * map and pushing it back onto the shapes map.
     */
    public void redo() {
        if (!future.isEmpty()) {
            JsonObject redoJson = future.pop();
            String id = redoJson.getString("id");
            Shape redoShape = this.getShape(id);

            // Shape creation action, recreate the shape.
            if (!shapeInHistory(id)) {
                // Add the shape to the model and server.
                this.add(redoJson);
            } else { // Shape property change action, redo the change.
                // Save the current state to history, redo the change and update the shape on the server.
                this.history.push(redoShape.toJson());
                JsonUtils.updateShapeFromJson(redoShape, redoJson);
                this.update(redoShape.toJson());
            }
            this.draw();
        }
    }

    /**
     * Clears all shapes from the vector drawing, informing the server of the deletion of each shape.
     */
    public void clear() {
        for (Shape shape : this.shapes) {
            if (shape.isOwner()) {
                this.remove(shape.getID());
            }
        }
        this.shapes.clear();
        this.history.clear();
        this.future.clear();
        draw();
    }
}
