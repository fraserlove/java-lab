# Vector Graphics Rendering
A vector drawing application built using the Swing GUI API and a MVC (Model-View-Controller) framework. The MVC design pattern separates functionality between three main classes. The Model class contains the data to be displayed in some representation by the GUI. The View class displays the GUI and provides methods for receiving user input from the GUI. The Controller class manages
Model and 3View class. It updates the Model class according to input received from the view and then updates the view with this information. A list of the features of this vector graphics renderer are detailed below:
- Straight lines, rectangles, squares, ellipses, circles and triangles can be drawn
- Diﬀerent line and ﬁll colours are supported.
- Undo and redo function for drawing.
- Load and save operations allowing for a state to be saved then loaded into another instance of the application, with all objects and properties remaining constant across instances.
- Images can be imported and placed on the vector graphic.
- Drawn objects can be selected, and properties such as location, rotation, colour and size can
be altered.
- Objects being drawn are visible to the user so that they can preview the shape before
it is created.
- Vector graphics can be exported to a PNG ﬁle.

## Usage
The application can be compiled and ran using the following commands inside the `/src` directory.

```bash
javac VectorGraphicsRenderer.java
```
Then ran with the following command:
```bash
java VectorGraphicsRenderer
```
The application has a menu bar across the top of the screen. In the `file`
menu are operations to save the vector graphic, load another vector graphic, 
import an image to place on the vector graphic, and an option to export the 
vector graphic to a PNG. In the `edit` menu there are operations to undo
and redo the latest object creation, change the default line colour when
drawing shapes, and an option to clear the display of all vector objects.
The `draw` menu allows you to select what type of shape you would like to
draw. This can be a rectangle, line, triangle or ellipse.

To draw a shape click `Draw > {ShapeType}` and press and hold the left mouse button down and drag to draw 
the desired shape. Right click on the shape to edit its fill colour, line colour, 
line thickness, rotation, size and position.

To import an image click `File > Import Image` and then choose the image
you would like to add. Note that only `.png`, `.jpeg` and `.jpg` images are supported.
Right click on the image to resize it or change its position.

To save a vector drawing click `File > Save` and navigate to the desired save
location in the file browser. The `.vec` file extension will be automatically added.

To load a vector drawing click `File > Load` and navigate to the location of the
saved vector image with the `.vec` extension. Then select to choose the file,
and it will be automatically loaded by the application.

To export the vector drawing as a PNG image, select `File > Export As PNG` and 
navigate to the desired save location. 


