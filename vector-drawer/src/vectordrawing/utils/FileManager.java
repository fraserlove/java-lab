package vectordrawing.utils;

import javax.swing.JFrame;
import java.awt.Container;
import java.awt.FileDialog;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;

import javax.imageio.ImageIO;

/**
 * Manages file operations.
 */
public class FileManager {

    /**
     * Opens a file manager window that allows for files to be selected for opening or saving.
     * The file manager can filter allowed extensions which it will whitelist.
     * 
     * @param frame JFrame to open the file manager on.
     * @param useImageExtensions If the file manager should whitelist image files.
     * @param loadMode If the file manager is being used for loading or saving.
     * @return The path to the file.
     */
    public static String manageFile(JFrame frame, boolean useImageExtensions, boolean loadMode) {
        FileDialog fileDialog = new FileDialog(frame, "", FileDialog.LOAD);
        // Only display files with specificed extensions.
        fileDialog.setFilenameFilter(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                if (useImageExtensions) {
                    return name.matches(".*\\.(png|jpg|jpeg)");
                }
                return name.endsWith(".vec");
            }
        });

        if (loadMode) {
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
     * Export the content of a JFrame as an image.
     * 
     * @param frame JFrame to export.
     * @param filename Filename to export to.
     * @param format Format to export as.
     */
    public static void exportAsImage(JFrame frame, String filename, String format) {
        Container content = frame.getContentPane();
        BufferedImage image = new BufferedImage(content.getWidth(), content.getHeight(), BufferedImage.TYPE_INT_RGB);
        content.paint(image.getGraphics());
        try {
            ImageIO.write(image, format, new File(filename));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
