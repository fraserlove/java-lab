package gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

import model.Model;
import extra.ExtensionAwareFilenameFilter;

/**
 * A controller class whose purpose is to render relevant state information stored in the model and make changes to the
 * model state based on user events. This class uses Swing to display the model state when the model changes. This class
 * implements PropertyChangeListener so that it can act as an observer to the model class and can handle events when the
 * model calls firePropertyChange to update the view class and subsequently the GUI.
 */
public class Controller implements PropertyChangeListener {

    private JFrame window; // Window from which all GUI elements can be placed onto.
    private Model model;
    private View view;

    public Controller(Model model) {

        this.model = model;
        view = new View(model);
        model.addObserver(this);
        initComponents();
    }

    /**
     * Creates a new JFrame object and a window with specified dimensions.
     */
    private void initComponents() {

        window = new JFrame("Vector Graphics Engine");
        window.add(view, BorderLayout.CENTER);
        window.setSize (view.getDimensions()[0], view.getDimensions()[1]);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initMenu();
        window.setVisible(true);

    }

    /**
     * Creates a populated menu bar shown at the top of the window. The menu bar holds different menus, which in turn
     * feature different menu items, that can carry out certain operations with the GUI.
     */
    private void initMenu() {

        JMenuBar menu = new JMenuBar();

        JMenu file = new JMenu("File");
        JMenu edit = new JMenu("Edit");
        JMenu draw = new JMenu("Draw");

        JMenuItem export = new JMenuItem("Export As PNG");
        export.addActionListener(new AbstractAction() {
            /**
             * Exports the vector image to a PNG at a specified location. This is done by writing to the BufferedImage
             * that is drawn onto in the view class.
             * @param e event invoking method
             */
            public void actionPerformed(ActionEvent e) {

                String path = openFile("Export As PNG", new String[] {"png"}, false);
                try {
                    path = path.replace(".png", "") + ".png";
                    ImageIO.write(view.getExportImage(), "png", new File(path));
                }
                catch (IOException ioException) {
                    System.out.println("IO error occurred when exporting png image.");
                }
            }
        });

        JMenuItem image = new JMenuItem("Import Image");
        image.addActionListener(new AbstractAction() {
            /**
             * Imports a bitmap onto the vector image, which then displays on the window. This image can then be
             * manipulated as a Object2D.
             * @param e event invoking method
             */
            public void actionPerformed(ActionEvent e) {

                String path = openFile("Import Image", new String[] {"jpg", "png", "jpeg"}, true);
                int x = Integer.parseInt(JOptionPane.showInputDialog("Image placement x position: "));
                int y = Integer.parseInt(JOptionPane.showInputDialog("Image placement y position: "));
                model.importImage(path, x, y);
            }
        });

        JMenuItem save = new JMenuItem("Save");
        save.addActionListener(new AbstractAction() {
            /**
             * Saves all data in the objects stack to a .vec file where it can then be loaded in another instance of the
             * application.
             * @param e event invoking method
             */
            public void actionPerformed(ActionEvent e) {

                String path = openFile("Save Vector File", new String[] {"vec"}, false);
                if (path != null) {
                    model.save(path);
                }
            }
        });

        JMenuItem load = new JMenuItem("Load");
        load.addActionListener(new AbstractAction() {
            /**
             * Loads data from a .vec file to the objects stack where they can then be displayed on screen. This allows
             * for previous vector images to be loaded again.
             * @param e
             */
            public void actionPerformed(ActionEvent e) {

                String path = openFile("Load Vector File", new String[] {"vec"}, true);
                if (path != null) {
                    model.load(path);
                }
            }
        });

        JMenuItem undo = new JMenuItem("Undo");
        undo.addActionListener(new AbstractAction() { public void actionPerformed(ActionEvent e) { model.undo(); } });

        JMenuItem redo = new JMenuItem("Redo");
        redo.addActionListener(new AbstractAction() { public void actionPerformed(ActionEvent e) { model.redo(); } });

        JMenuItem lineColour = new JMenuItem("Default Line Colour");
        lineColour.addActionListener(new AbstractAction() { public void actionPerformed(ActionEvent e) { view.updateColour(JColorChooser.showDialog(window, "Default Line Colour", Color.white)); } });

        JMenuItem clear = new JMenuItem("Clear");
        clear.addActionListener(new AbstractAction() { public void actionPerformed(ActionEvent e) { model.clear(); } });

        JMenuItem line = new JMenuItem("Line");
        line.addActionListener(new AbstractAction() { public void actionPerformed(ActionEvent e) { view.updateShape("line"); } });

        JMenuItem rectangle = new JMenuItem("Rectangle");
        rectangle.addActionListener(new AbstractAction() { public void actionPerformed(ActionEvent e) { view.updateShape("rectangle"); } });

        JMenuItem triangle = new JMenuItem("Triangle");
        triangle.addActionListener(new AbstractAction() { public void actionPerformed(ActionEvent e) { view.updateShape("triangle"); } });

        JMenuItem ellipse = new JMenuItem("Ellipse");
        ellipse.addActionListener(new AbstractAction() { public void actionPerformed(ActionEvent e) { view.updateShape("ellipse"); } });

        file.add(export);
        file.add(image);
        file.add(save);
        file.add(load);

        edit.add(undo);
        edit.add(redo);
        edit.add(lineColour);
        edit.add(clear);

        draw.add(line);
        draw.add(rectangle);
        draw.add(triangle);
        draw.add(ellipse);

        menu.add(file);
        menu.add(edit);
        menu.add(draw);
        window.setJMenuBar(menu);
    }

    /**
     * Opens a file manager window that allows for files to be selected for loading or saving. The file manager can also
     * accept an array of allowed extensions which it will whitelist in the file manager.
     * @param title title of the file manager window
     * @param allowedExtensions file extensions that can be shown in the file manager
     * @param loadFile specifies if the file manager is being used for loading or saving
     * @return a path to the file
     */
    private String openFile(String title, String[] allowedExtensions, boolean loadFile) {

        FileDialog fileDialog = new FileDialog(window, title);
        FilenameFilter filter = new ExtensionAwareFilenameFilter(allowedExtensions);
        fileDialog.setFilenameFilter(filter);

        if (loadFile) {
            fileDialog.setMode(FileDialog.LOAD);
        }
        else {
            fileDialog.setMode(FileDialog.SAVE);
        }
        fileDialog.setVisible(true);

        if (fileDialog.getDirectory() != null && fileDialog.getFile() != null) {
            return fileDialog.getDirectory() + fileDialog.getFile();
        }
        return null;
    }

    /**
     * Updates the GUI through the View class when the model fires a PropertyChangeEvent.
     * @param event PropertyChangeEvent fired by Model class
     */
    public void propertyChange(final PropertyChangeEvent event) {

        if (event.getPropertyName().equals("draw")) {
            SwingUtilities.invokeLater(() -> {
                view.repaint();
            });
        }
    }

}