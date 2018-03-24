package application;
	
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.stage.Stage;

/**
 * This class controls the GUI overall. It boots up the GUI and loads the main menu panel.
 * It also controls the switching of GUI panes.
 * @author Daniel
 *
 */
public class GUIHandler extends Application {
	private static Stage main;
	
	/**
	 * Basic constructor for the GUI that gets it up and running.
	 */
	public GUIHandler() {
		
	}
	
	@Override
	public void start(Stage main) {
		try {
			//TODO: Delete this and read from file.
			Employee a = new Employee(14, "Dan", "Smith", "Manager");
			Employee b = new Employee(23, "John", "Doe", "Worker");
			Employee c = new Employee(45, "Paul", "Lang", "Worker");
			Employee d = new Employee(35, "Jacob", "Person", "Worker");
			
			Company.getEmployees().put(14, a);
			Company.getEmployees().put(23, b);
			Company.getEmployees().put(45, c);
			Company.getEmployees().put(35, d);
			
			GUIHandler.main = main;
			
			//Set up the style of the window
			main.setTitle("Meeting Manager");
			
			changePane(new GUIPanes.MainMenu());
			
			main.setResizable(false);
			main.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Used to change the pane in the GUI.
	 * @param p Parent pane to change to.
	 */
	public static void changePane(Parent p) {
		main.setScene(new CustomScene(p));
		
		//Counteract the auto-focusing of the first node.
		main.getScene().getRoot().requestFocus();
	}
	
	/**
	 * Alternative entry point to the program that boots up the GUI and skips the text-based version.
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
