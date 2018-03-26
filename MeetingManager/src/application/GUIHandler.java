package application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map.Entry;

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
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("COMPANY files (*.company)", "*.company");
		fileChooser.getExtensionFilters().add(extFilter);

		// Show save file dialog
		File file = fileChooser.showSaveDialog(main);

		if (file != null) {
			try {
				FileWriter fileWriter = new FileWriter(file);
				BufferedWriter out = new BufferedWriter(fileWriter);

				for (Entry<Integer, Employee> entry : Company.getEmployees().entrySet()) {
					out.write(entry.getValue().getEmployeeInformation());
					out.newLine();
					
					for(Meeting m : entry.getValue().getDiary().getMeetings()) {
						out.write(m.getMeetingDetails());
						out.newLine();
					}
				}
				out.close();
				fileWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
	
	public static void loadCompany() {
		FileChooser fileChooser = new FileChooser();

		// Set extension filter
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("COMPANY files (*.company)", "*.company");
		fileChooser.getExtensionFilters().add(extFilter);

		// Show save file dialog
		File file = fileChooser.showOpenDialog(main);

		if (file != null) {
			try {
				FileReader fileReader = new FileReader(file);
				BufferedReader in = new BufferedReader(fileReader);
				
				String line;
				int employeeID = 0;
				
			    while ((line = in.readLine()) != null) {
			    	if(line.isEmpty()) continue;
			    	
			        String[] info = line.split(",");
			        
			        try {
			        	int id = Integer.parseInt(info[0]);
			        	employeeID = id;
			        	String firstName = info[1];
			        	String lastName = info[2];
			        	String jobTitle = info[3];
			        	
			        	Company.addEmployee(id, firstName, lastName, jobTitle);
			        }
			        catch(NumberFormatException e) {
			        	//Tue Mar 20 12:00:00 GMT 2018
			        	SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd kk:mm:ss zzz yyyy");
			        	
			        	System.out.println(info[0]);
			        	Date startTime = format.parse(info[0]);
			        	System.out.println(startTime);
			        	
			        	Date endTime = format.parse(info[1]);
			        	String description = info[2];
			        	
			        	Meeting toAdd = new Meeting(startTime, endTime, description);
			        	Company.selectEmployee(employeeID).addMeeting(toAdd);
			        }
			    }
				
				in.close();
				fileReader.close();
			} catch (IOException | ParseException e) {
				e.printStackTrace();
			}
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
