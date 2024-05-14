package vectordrawing.view;

import vectordrawing.model.Model;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

/**
 * The GUI view of the application. This class is responsible for creating the GUI and handling user events.
 */
public class View {

    private int width;
    private int height;

    private JFrame window;
    private Canvas canvas;

    private JMenuBar menu;
    private JMenu file, edit, draw;
    private JPopupMenu popup;
    private JMenuItem export, save, open;
    private JMenuItem undo, redo, clear;
    private JMenuItem line, rectangle, triangle, ellipse;
    private JMenuItem lineWeight, rotate, resize, translate, changeColour, changeFill;
    private JComboBox<String> colour, fill;
    private JPanel colourPanel;

    /**
     * Creates the GUI view.
     * 
     * @param model Model of the application.
     * @param width Width of the window.
     * @param height Height of the window.
     */
    public View(Model model, int width, int height) {
        this.width = width;
        this.height = height;
        this.canvas = new Canvas(model, width, height);
        this.createWindow();
    }

    /**
     * Creates the window and adds the canvas and menus to it.
     */
    public void createWindow() {
        window = new JFrame("Vector Drawing");
        window.add(canvas, BorderLayout.CENTER);
        window.setSize(this.width, this.height);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.menuBar();
        this.popupMenus();
        this.colourPanel();
        window.setVisible(true);
    }

    /**
     * Returns the canvas.
     * 
     * @return Canvas.
     */
    public Canvas getCanvas() {
        return this.canvas;
    }

    /**
     * Returns the window.
     * 
     * @return Window.
     */
    public JFrame getWindow() {
        return this.window;
    }


    /**
     * Creates the menu bar and adds the menus to it.
     */
    private void menuBar() {
        this.menu = new JMenuBar();
        this.fileMenu();
        this.editMenu();
        this.drawMenu();
        this.window.setJMenuBar(this.menu);
    }

    /**
     * Creates the file menu and adds the menu items to it.
     */
    private void fileMenu() {
        this.file = new JMenu("File");
        this.export = new JMenuItem("Export");
        this.save = new JMenuItem("Save");
        this.open = new JMenuItem("Open");
        this.file.add(export);
        this.file.add(save);
        this.file.add(open);
        this.menu.add(file);
    }

    /**
     * Creates the edit menu and adds the menu items to it.
     */
    private void editMenu() {
        this.edit = new JMenu("Edit");
        this.undo = new JMenuItem("Undo");
        this.redo = new JMenuItem("Redo");
        this.clear = new JMenuItem("Clear");
        this.edit.add(undo);
        this.edit.add(redo);
        this.edit.add(clear);
        this.menu.add(edit);
    }

    /**
     * Creates the draw menu and adds the menu items to it.
     */
    private void drawMenu() {
        this.draw = new JMenu("Draw");
        this.line = new JMenuItem("Line");
        this.rectangle = new JMenuItem("Rectangle");
        this.triangle = new JMenuItem("Triangle");
        this.ellipse = new JMenuItem("Ellipse");
        this.draw.add(line);
        this.draw.add(rectangle);
        this.draw.add(triangle);
        this.draw.add(ellipse);
        this.menu.add(draw);
    }

    /**
     * Creates the colour panel and adds the combo boxes to it.
     */
    private void colourPanel() {
        this.colourPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        String[] colors = {"red", "green", "blue", "yellow", "black", "white",
                           "cyan", "magenta", "orange", "pink", "gray",
                           "dark gray", "light gray"};
        this.colour = new JComboBox<>(colors);
        this.fill = new JComboBox<>(colors);
        this.colour.setSelectedItem(this.canvas.getLineColour());
        this.fill.setSelectedItem(this.canvas.getFill());
        this.colourPanel.add(new JLabel("Colour: "));
        this.colourPanel.add(this.colour);
        this.colourPanel.add(new JLabel("Fill: "));
        this.colourPanel.add(this.fill);
        this.menu.add(this.colourPanel);
    }

    /**
     * Creates the popup menus and adds the menu items to it.
     */
    private void popupMenus() {
        this.popup = new JPopupMenu();
        this.lineWeight = new JMenuItem("Line Weight");
        this.rotate = new JMenuItem("Rotate");
        this.changeColour = new JMenuItem("Change Colour");
        this.changeFill = new JMenuItem("Change Fill");
        this.resize = new JMenuItem("Resize");
        this.translate = new JMenuItem("Translate");
        this.popup.add(lineWeight);
        this.popup.add(rotate);
        this.popup.add(changeColour);
        this.popup.add(changeFill);
        this.popup.add(resize);
        this.popup.add(translate);
    }

    /**
     * Shows the popup menu.
     * 
     * @param e Mouse event.
     */
    public void showPopup(MouseEvent e) {
        this.popup.show(e.getComponent(), e.getX(), e.getY());
    }

    /**
     * Adds an action listener to the export menu item.
     * 
     * @param listener Action listener.
     */
    public void addExportActionListener(ActionListener listener) {
        this.export.addActionListener(listener);
    }

    /**
     * Adds an action listener to the save menu item.
     * 
     * @param listener Action listener.
     */
    public void addSaveActionListener(ActionListener listener) {
        this.save.addActionListener(listener);
    }

    /**
     * Adds an action listener to the open menu item.
     * 
     * @param listener Action listener.
     */
    public void addOpenActionListener(ActionListener listener) {
        this.open.addActionListener(listener);
    }

    /**
     * Adds an action listener to the undo menu item.
     * 
     * @param listener Action listener.
     */
    public void addUndoActionListener(ActionListener listener) {
        this.undo.addActionListener(listener);
    }

    /**
     * Adds an action listener to the redo menu item.
     * 
     * @param listener Action listener.
     */
    public void addRedoActionListener(ActionListener listener) {
        this.redo.addActionListener(listener);
    }

    /**
     * Adds an action listener to the clear menu item.
     * 
     * @param listener Action listener.
     */
    public void addColourActionListener(ActionListener listener) {
        this.colour.addActionListener(listener);
    }

    /**
     * Adds an action listener to the fill menu item.
     * 
     * @param listener Action listener.
     */
    public void addFillActionListener(ActionListener listener) {
        this.fill.addActionListener(listener);
    }

    /**
     * Adds an action listener to the clear menu item.
     * 
     * @param listener Action listener.
     */
    public void addClearActionListener(ActionListener listener) {
        this.clear.addActionListener(listener);
    }

    /**
     * Adds an action listener to the line menu item.
     * 
     * @param listener Action listener.
     */
    public void addLineActionListener(ActionListener listener) {
        this.line.addActionListener(listener);
    }

    /**
     * Adds an action listener to the rectangle menu item.
     * 
     * @param listener Action listener.
     */
    public void addRectangleActionListener(ActionListener listener) {
        this.rectangle.addActionListener(listener);
    }

    /**
     * Adds an action listener to the triangle menu item.
     * 
     * @param listener Action listener.
     */
    public void addTriangleActionListener(ActionListener listener) {
        this.triangle.addActionListener(listener);
    }

    /**
     * Adds an action listener to the ellipse menu item.
     * 
     * @param listener Action listener.
     */
    public void addEllipseActionListener(ActionListener listener) {
        this.ellipse.addActionListener(listener);
    }

    /**
     * Adds an action listener to the line weight menu item.
     * 
     * @param listener Action listener.
     */
    public void addChangeColourActionListener(ActionListener listener) {
        this.changeColour.addActionListener(listener);
    }

    /**
     * Adds an action listener to the line weight menu item.
     * 
     * @param listener Action listener.
     */
    public void addLineWeightActionListener(ActionListener listener) {
        this.lineWeight.addActionListener(listener);
    }

    /**
     * Adds an action listener to the line weight menu item.
     * 
     * @param listener Action listener.
     */
    public void addChangeFillActionListener(ActionListener listener) {
        this.changeFill.addActionListener(listener);
    }

    /**
     * Adds an action listener to the rotate menu item.
     * 
     * @param listener Action listener.
     */
    public void addRotateActionListener(ActionListener listener) {
        this.rotate.addActionListener(listener);
    }

    /**
     * Adds an action listener to the resize menu item.
     * 
     * @param listener Action listener.
     */
    public void addResizeActionListener(ActionListener listener) {
        this.resize.addActionListener(listener);
    }

    /**
     * Adds an action listener to the translate menu item.
     * 
     * @param listener Action listener.
     */
    public void addTranslateActionListener(ActionListener listener) {
        this.translate.addActionListener(listener);
    }
}
