package application;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map.Entry;

import javax.swing.JOptionPane;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * This class controls the GUI overall. It boots up the GUI and loads the main
 * menu panel. It also controls the switching of GUI panes.
 * 
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
			GUIHandler.main = main;

			// Set up the style of the window
			main.setTitle("Meeting Manager");

			changePane(new GUIPanes.MainMenu());

			main.setResizable(false);
			main.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void saveCompany() {
		FileChooser fileChooser = new FileChooser();

		// Set extension filter
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("COMPANY files (*.company)",
				"*.company");
		fileChooser.getExtensionFilters().add(extFilter);

		// Show save file dialog
		File file = fileChooser.showSaveDialog(main);

		if (file != null) {
			FileWriter fileWriter;
			try {
				fileWriter = new FileWriter(file);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			for (Entry<Integer, Employee> entry : Company.getEmployees().entrySet()) {

			}

			// fileWriter.write();
			//fileWriter.close();
		}

	}

	/**
	 * Used to change the pane in the GUI.
	 * 
	 * @param p
	 *            Parent pane to change to.
	 */
	public static void changePane(Parent p) {
		main.setScene(new CustomScene(p));

		// Counteract the auto-focusing of the first node.
		main.getScene().getRoot().requestFocus();
	}

	/**
	 * Alternative entry point to the program that boots up the GUI and skips the
	 * text-based version.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
