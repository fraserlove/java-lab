# Vector Drawing
A vector drawing application built using the Swing API and an MVC (Model-View-Controller) design pattern. The Model class contains the vector shape data and underlying logic of the vector drawing application. The View class displays the vector drawing canvas and accompanying user interface. The controller class sits in the middle of these classes and manages updating the model with mouse and key input and updating the view through property changes fired from the model.

The vector drawing application has all the following functionality:
- Straight lines, rectangles, squares, ellipses, circles and triangles can be drawn. Note that circles and squares are drawn via dragging while holding down the `Shift` key when drawing an ellipse and rectangle respectively to lock the aspect ratio.
- The colours `red`, `green`, `blue`, `yellow`, `black`, `white`, `cyan`, `magenta`, `orange`, `pink`, `gray`, `darkGray`, `lightGray` are all supported as line colours and fill colours.
- Undo/redo functionality for changing shape attributes and shape creation.
- Ability to edit and change different line colours, line widths, fill colours, rotation, position, and size for individual shapes. Any previously drawn shape can be right-clicked to bring up a menu to change the shapes attributes.
- Rotation of shapes through angles 0 to 359 inclusive.
- Shapes with an incorrect format are ignored, and shapes with incorrect property values are given default values. These default values are a width, height of 100, a line thickness of 1, a line colour of black, a fill colour of black, a rotation of 0, a position of (0, 0) and a line thickness of 4.
- Ability to change the size of all objects.
- Exporting the drawing as a `png` or `jpeg` file. Note that if no file type is specified the application defaults to exporting a `png` image.
- Save and open previously drawn vector drawings via the applications through exporting all the drawn shapes in a JSON format and storing in the applications own `vec` file format. This is particularly useful for working offline when the user wants to save a drawing and connection to the internet is limited.
- Ability to clear all shapes from the drawing.
- Shapes being drawn are previewed to the user before they are created.
- Connects with a server to save the clients drawings and pull drawings from other clients, allowing for drawings to be easily shared.

## Usage
This vector drawing application requires the `javax.json` library, the `.jar` of which is located within the included lib directory. Ensure you are in the root directory for this project. The application can be compiled and ran using the following commands.
```bash
javac -cp "lib/*:src" src/VectorDrawing.java
java -cp "lib/*:src" VectorDrawing
```
The application features a menu bar across the top of the display. In the `file` menu there are options to `save` the vector graphic as `png` or `jpeg`, `open` a previous vector drawing stored in a `vec` file, or `save` the current drawing in the `vec` file format.  The `edit` menu provides operations to `undo` and `redo` the latest shape creation or property change and to `clear` the entire drawing space. The `draw` includes options to select the current shape to draw from a `rectangle`, `line`, `ellipse` or `triangle`.

To draw a shape left-click `Draw > {ShapeType}` and press and hold the left mouse button down and drag to draw  the desired shape. Holding down `Shift` at the same time locks the aspect ratio, allowing for circles and squares to be drawn. Right-click on the shape to edit its fill colour, line colour, line thickness, rotation, size and position.

To save a vector drawing click `File > Save` and navigate to the desired save location in the file browser. The `.vec` file extension will be automatically added.

To load a vector drawing click `File > Load` and navigate to the location of the saved vector image with the `.vec` extension. Then select to choose the file, and it will be automatically loaded by the application. Note that large `.vec` file may take some time to be loaded by the application.

To export the vector drawing as a `png` or `jpeg` image, select `File > Export` and navigate to the desired save location. Include the file extension you want to use in the name. If no file extension is provided then the application defaults to exporting a `png`.

## Testing
Note that edge case / invalid input testing for properties was not needed in the model as the view automatically ensures that the entered properties are within the ranges specified in the specification. The tests are runnable using the following commands (ensure you are in the root directory of the project). 

```bash
javac -cp "lib/*:src" src/vectordrawing/tests/ModelTest.java
java -cp "lib/*:src" org.junit.runner.JUnitCore vectordrawing.tests.ModelTest
```

## Implementation Notes
- An MVC design pattern was used for this application because it allows for the separation of the underlying logic of the application with the user interface. This allows for the application to be easily extended and modified in the future. Specifically this also allows for the application to be easily tested as the model can be tested independently of the view and controller.
- The `Shape` class was made abstract as it is not possible to draw a shape without knowing the shape type. This also allows for the `Shape` class to be extended in the future to include more shape types.
- The `DrawingClient` follows the network protocol mentioned in the specification, using JSON to send and receive data from the server.
- The `Model` class provides an undo/redo feature for object creation and property changes. This is done by using to stacks, `history` and `future`. The `history` stack stores the states of previously edited/created shapes and the future stack stores the states of shapes that have been undone. This allows for the vector drawing to be reverted to any previous state if the user wishes to undo the change.
- The `Model` class also stores the shapes in a stack. This ensures that when drawing the shapes are drawn in the correct order, with the most recently drawn shape being drawn on top of the other shapes.
- The `Utils` sub-packages contains a `JSONUtils` class which provides methods to convert a `Shape` object to a `JSONObject` and vice versa. This allows for the `Model` class to easily convert the shapes to JSON objects to be sent to the server and for the `DrawingClient` to convert the JSON objects received from the server to `Shape` objects. It also provides functionality to convert a `Shape` object to a `BufferedImage` object, allowing for the vector drawing to be exported as a `png` or `jpeg` image and mange files with the `.vec` extension. All this functionality from the main `Model` class to improve modularity and readability.