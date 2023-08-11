import gui.Controller;
import model.Model;

/**
 * Main class that initiates the Vector Graphics Engine GUI Application.
 * It first creates a Model object and then a controller method. Initiating the components of the MVC GUI design pattern.
 */
public class VectorGraphicsEngine {

    public static void main(String[] args){
        Model model = new Model();
        Controller controller = new Controller(model);
    }
}