package application;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.Map.Entry;

import application.DisplayElements.CustomButton;
import application.DisplayElements.CustomText;
import application.DisplayElements.MenuButton;
import application.DisplayElements.NumberSpinner;
import application.MeetingManagerExceptions.EmployeeDetailsInvalidID;
import application.MeetingManagerExceptions.EmployeeExists;
import application.MeetingManagerExceptions.GenericFieldEmpty;
import application.MeetingManagerExceptions.MeetingTimeBeforeStart;
import application.MeetingManagerExceptions.MeetingTimeSameTime;
import application.MeetingManagerExceptions.MeetingTimeStartConflict;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.util.Duration;

/**
 * This class contains all of the Panes that are used in the program. It also
 * contains all the business logic behind each pane.
 * 
 * @author Daniel
 *
 */
public class GUIPanes {

	/**
	 * The main menu pane.
	 * 
	 * @author Daniel
	 *
	 */
	public static class MainMenu extends BorderPane {

		/**
		 * Classic constructor. Sets up all the styles and Nodes on the Pane.
		 */
		public MainMenu() {
			// Top portion of the pane
			CustomText title = new CustomText("Meeting Manager", 72);

			setMargin(title, new Insets(30, 0, 0, 0));
			setTop(title);
			setAlignment(getTop(), Pos.CENTER);

			VBox options = new VBox(10);
			options.setAlignment(Pos.CENTER);

			// Menu buttons
			MenuButton employeesButton = new MenuButton("Manage Employees");
			employeesButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					GUIHandler.changePane(new ManageEmployees());
				}
			});

			MenuButton diaryButton = new MenuButton("Manage Employee Diaries");
			diaryButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					GUIHandler.changePane(new ManageDiaries());
				}
			});

			MenuButton companyMeetingButton = new MenuButton("Set-up Company Meeting");
			companyMeetingButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					GUIHandler.changePane(new CompanyMeeting());
				}
			});

			MenuButton loadButton = new MenuButton("Load Company");
			loadButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					GUIHandler.loadCompany();
				}
			});

			MenuButton saveButton = new MenuButton("Save Company");
			saveButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					GUIHandler.saveCompany();
				}
			});

			options.getChildren().addAll(employeesButton, diaryButton, companyMeetingButton, loadButton, saveButton);

			setCenter(options);

		}
	}

	/**
	 * ManageEmployees pane that shows all employees and leads to EditEmployee pane.
	 * 
	 * @author Daniel
	 *
	 */
	public static class ManageEmployees extends BorderPane {
		@SuppressWarnings("unchecked")
		/**
		 * Classic constructor. Sets up all the styles and Nodes on the Pane.
		 */
		public ManageEmployees() {
			// Add the
			VBox topPane = new VBox();

			CustomText title = new CustomText("Meeting Manager", 64);
			topPane.getChildren().add(title);

			CustomText subtitle = new CustomText("Click an Employee to edit their details.", 20);
			topPane.getChildren().add(subtitle);

			// Search label and styles
			Label searchLabel = new Label("Search:");
			TextField searchField = new TextField();
			HBox hb = new HBox();
			hb.getChildren().addAll(searchLabel, searchField);
			hb.setSpacing(10);
			hb.setAlignment(Pos.CENTER_RIGHT);

			topPane.getChildren().add(hb);

			setMargin(topPane, new Insets(10));

			setTop(topPane);

			// Table set-up
			TableView<Employee> table = new TableView<Employee>();

			TableColumn<Employee, String> idCol = new TableColumn<Employee, String>("ID");
			idCol.setCellValueFactory(new PropertyValueFactory<Employee, String>("id"));

			TableColumn<Employee, String> firstNameCol = new TableColumn<Employee, String>("First Name");
			firstNameCol.setCellValueFactory(new PropertyValueFactory<Employee, String>("firstName"));

			TableColumn<Employee, String> lastNameCol = new TableColumn<Employee, String>("Last Name");
			lastNameCol.setCellValueFactory(new PropertyValueFactory<Employee, String>("lastName"));

			TableColumn<Employee, String> jobTitleCol = new TableColumn<Employee, String>("Job Title");
			jobTitleCol.setCellValueFactory(new PropertyValueFactory<Employee, String>("jobTitle"));

			table.getColumns().addAll(idCol, firstNameCol, lastNameCol, jobTitleCol);

			// Table column widths (odd value so that it offsets the margin)s
			idCol.prefWidthProperty().bind(table.widthProperty().divide(4.02));
			firstNameCol.prefWidthProperty().bind(table.widthProperty().divide(4.02));
			lastNameCol.prefWidthProperty().bind(table.widthProperty().divide(4.02));
			jobTitleCol.prefWidthProperty().bind(table.widthProperty().divide(4.02));

			setMargin(table, new Insets(10, 5, 5, 5));

			setCenter(table);

			// Convert Employee data structure to ObservableList so that it's compatable
			// with the table.
			ObservableList<Employee> data = FXCollections.observableArrayList();

			for (Entry<Integer, Employee> entry : Company.getEmployees().entrySet()) {
				data.add(entry.getValue());
			}

			table.setItems(data);

			// Credit: https://stackoverflow.com/a/30194680/3102362
			// Row click events.
			table.setRowFactory(tv -> {
				TableRow<Employee> row = new TableRow<>();
				row.setOnMouseClicked(event -> {
					if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {

						Employee clickedRow = row.getItem();

						// Switch to EditEmployee and pass the selected employee.
						GUIHandler.changePane(new EditEmployee(clickedRow));
					}
				});
				return row;
			});

			// Searching the table (Credit: https://stackoverflow.com/a/44317900/3102362)
			FilteredList<Employee> filteredData = new FilteredList<>(data, p -> true);

			// Set the filter Predicate whenever the filter changes.
			searchField.textProperty().addListener((observable, oldValue, newValue) -> {
				filteredData.setPredicate(myObject -> {
					// If filter text is empty, display all employees.
					if (newValue == null || newValue.isEmpty()) {
						return true;
					}

					// Compare first name, last name and id fields with filter.
					String lowerCaseFilter = newValue.toLowerCase();

					if (String.valueOf(myObject.getFirstName()).toLowerCase().contains(lowerCaseFilter)) {
						return true; // Filter matches firstName.
					} else if (String.valueOf(myObject.getLastName()).toLowerCase().contains(lowerCaseFilter)) {
						return true; // Filter matches lastName.
					} else if (String.valueOf(myObject.getId()).toLowerCase().contains(lowerCaseFilter)) {
						return true; // Filter matches ID.
					} else if (String.valueOf(myObject.getJobTitle()).toLowerCase().contains(lowerCaseFilter)) {
						return true; // Filter matches job title.
					}

					return false; // Does not match.
				});
			});

			// Sort the data
			SortedList<Employee> sortedData = new SortedList<>(filteredData);
			sortedData.comparatorProperty().bind(table.comparatorProperty());

			table.setItems(sortedData);

			// Bottom buttons
			BorderPane bottomBox = new BorderPane();

			CustomButton backButton = new CustomButton("Back", 16);
			backButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					GUIHandler.changePane(new MainMenu());
				}
			});
			bottomBox.setLeft(backButton);

			CustomButton addEmployeeButton = new CustomButton("Add Employee", 16);
			addEmployeeButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					GUIHandler.changePane(new AddEmployee());
				}
			});

			bottomBox.setRight(addEmployeeButton);

			setMargin(bottomBox, new Insets(10, 5, 5, 5));
			setBottom(bottomBox);
		}
	}

	/**
	 * EditEmployee pane which allows you to change an Employee's details
	 * 
	 * @author Daniel
	 *
	 */
	public static class EditEmployee extends BorderPane {

		/**
		 * Classic constructor. Sets up all the styles and Nodes on the Pane.
		 */
		public EditEmployee(Employee employee) {
			// Top styles (title, subtitle..)
			VBox topBox = new VBox();
			CustomText title = new CustomText("Meeting Manager", 64);
			topBox.getChildren().add(title);
			CustomText subtitle = new CustomText("Editing employee details.", 20);
			topBox.getChildren().add(subtitle);

			setMargin(topBox, new Insets(10));
			setTop(topBox);

			GridPane grid = new GridPane();

			grid.setHgap(10);
			grid.setVgap(20);
			grid.setPadding(new Insets(25, 25, 25, 25));

			CustomText id = new CustomText("ID:", 30);
			grid.add(id, 0, 0);

			// Form elements
			TextField idTextField = new TextField();
			idTextField.setText(String.valueOf(employee.getId()));
			idTextField.setEditable(false);
			idTextField.setFont(Font.font("Arial", 20));
			grid.add(idTextField, 1, 0);

			CustomText firstNameLabel = new CustomText("firstName:", 30);
			grid.add(firstNameLabel, 0, 1);

			TextField firstNameTextField = new TextField();
			firstNameTextField.setText(String.valueOf(employee.getFirstName()));
			firstNameTextField.setFont(Font.font("Arial", 20));
			grid.add(firstNameTextField, 1, 1);

			CustomText lastNameLabel = new CustomText("lastName:", 30);
			grid.add(lastNameLabel, 0, 2);

			TextField lastNameTextField = new TextField();
			lastNameTextField.setText(String.valueOf(employee.getLastName()));
			lastNameTextField.setFont(Font.font("Arial", 20));
			grid.add(lastNameTextField, 1, 2);

			CustomText jobLabel = new CustomText("Job title:", 30);
			grid.add(jobLabel, 0, 3);

			TextField jobTextField = new TextField();
			jobTextField.setText(String.valueOf(employee.getJobTitle()));
			jobTextField.setFont(Font.font("Arial", 20));
			grid.add(jobTextField, 1, 3);

			CustomButton saveButton = new CustomButton("Save", 20);
			saveButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {

					Company.editEmployee(employee, firstNameTextField.getText(), lastNameTextField.getText(),
							jobTextField.getText());

					// Success message
					CustomText successText = new CustomText("Saved successfully.", 16);
					grid.add(successText, 2, 4);

					// Timer so that the success message shows for 2 seconds. (Credit:
					// https://stackoverflow.com/a/9966213/3102362)
					Timeline timer = new Timeline(new KeyFrame(Duration.seconds(2), new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent event) {
							grid.getChildren().remove(successText);
						}
					}));
					timer.play();
				}
			});

			grid.add(saveButton, 0, 4);

			CustomButton deleteButton = new CustomButton("Delete", 20, "FF0000");
			deleteButton.setOnAction(new EventHandler<ActionEvent>() {
				@SuppressWarnings("unlikely-arg-type")
				@Override
				public void handle(ActionEvent event) {
					Company.getEmployees().remove(employee);
					GUIHandler.changePane(new ManageEmployees());
				}
			});
			grid.add(deleteButton, 1, 4);

			setLeft(grid);

			CustomButton backButton = new CustomButton("Back", 16);
			backButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					GUIHandler.changePane(new ManageEmployees());
				}
			});
			setMargin(backButton, new Insets(10, 5, 5, 5));

			setBottom(backButton);
		}
	}

	/**
	 * ManageDiaries pane that shows all employees and allows you to view an
	 * Employee's diary.
	 * 
	 * @author Daniel
	 *
	 */
	public static class ManageDiaries extends BorderPane {

		@SuppressWarnings("unchecked")
		/**
		 * Classic constructor. Sets up all the styles and Nodes on the Pane.
		 */
		public ManageDiaries() {
			// Title, subtitle, etc.
			VBox topPane = new VBox();

			CustomText title = new CustomText("Meeting Manager", 64);
			topPane.getChildren().add(title);

			CustomText subtitle = new CustomText("Click an Employee to view their diary.", 20);
			topPane.getChildren().add(subtitle);

			Label searchLabel = new Label("Search:");
			TextField searchField = new TextField();
			HBox hb = new HBox();
			hb.getChildren().addAll(searchLabel, searchField);
			hb.setSpacing(10);
			hb.setAlignment(Pos.CENTER_RIGHT);

			topPane.getChildren().add(hb);

			setMargin(topPane, new Insets(10));

			setTop(topPane);

			// Table set-up
			TableView<Employee> table = new TableView<Employee>();

			TableColumn<Employee, String> idCol = new TableColumn<Employee, String>("ID");
			idCol.setCellValueFactory(new PropertyValueFactory<Employee, String>("id"));

			TableColumn<Employee, String> firstNameCol = new TableColumn<Employee, String>("First Name");
			firstNameCol.setCellValueFactory(new PropertyValueFactory<Employee, String>("firstName"));

			TableColumn<Employee, String> lastNameCol = new TableColumn<Employee, String>("First Name");
			lastNameCol.setCellValueFactory(new PropertyValueFactory<Employee, String>("lastName"));

			TableColumn<Employee, String> jobTitleCol = new TableColumn<Employee, String>("Job Title");
			jobTitleCol.setCellValueFactory(new PropertyValueFactory<Employee, String>("jobTitle"));

			table.getColumns().addAll(idCol, firstNameCol, lastNameCol, jobTitleCol);

			// Table column widths (odd value so that it offsets the margin)s
			idCol.prefWidthProperty().bind(table.widthProperty().divide(4.02));
			firstNameCol.prefWidthProperty().bind(table.widthProperty().divide(4.02));
			lastNameCol.prefWidthProperty().bind(table.widthProperty().divide(4.02));
			jobTitleCol.prefWidthProperty().bind(table.widthProperty().divide(4.02));

			setMargin(table, new Insets(10, 5, 5, 5));

			setCenter(table);

			ObservableList<Employee> data = FXCollections.observableArrayList();

			for (Entry<Integer, Employee> entry : Company.getEmployees().entrySet()) {
				data.add(entry.getValue());
			}

			table.setItems(data);

			// Credit: https://stackoverflow.com/a/30194680/3102362
			// Row click events.
			table.setRowFactory(tv -> {
				TableRow<Employee> row = new TableRow<>();
				row.setOnMouseClicked(event -> {
					if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {

						Employee clickedRow = row.getItem();

						// Switch to EditEmployee and pass the selected employee.
						GUIHandler.changePane(new EmployeeDiary(clickedRow));
					}
				});
				return row;
			});

			// Searching the table (Credit: https://stackoverflow.com/a/44317900/3102362)
			FilteredList<Employee> filteredData = new FilteredList<>(data, p -> true);

			// Set the filter Predicate whenever the filter changes.
			searchField.textProperty().addListener((observable, oldValue, newValue) -> {
				filteredData.setPredicate(myObject -> {
					// If filter text is empty, display all employees.
					if (newValue == null || newValue.isEmpty()) {
						return true;
					}

					// Compare first name, last name, id, job title fields with filter.
					String lowerCaseFilter = newValue.toLowerCase();

					if (String.valueOf(myObject.getFirstName()).toLowerCase().contains(lowerCaseFilter)) {
						return true; // Filter matches firstName.
					} else if (String.valueOf(myObject.getLastName()).toLowerCase().contains(lowerCaseFilter)) {
						return true; // Filter matches lastName.
					} else if (String.valueOf(myObject.getId()).toLowerCase().contains(lowerCaseFilter)) {
						return true; // Filter matches ID.
					} else if (String.valueOf(myObject.getJobTitle()).toLowerCase().contains(lowerCaseFilter)) {
						return true; // Filter matches job title.
					}

					return false; // Does not match.
				});
			});

			// Sort the data
			SortedList<Employee> sortedData = new SortedList<>(filteredData);
			sortedData.comparatorProperty().bind(table.comparatorProperty());

			table.setItems(sortedData);

			CustomButton backButton = new CustomButton("Back", 16);
			backButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					GUIHandler.changePane(new MainMenu());
				}
			});
			setMargin(backButton, new Insets(10, 5, 5, 5));

			setBottom(backButton);
		}
	}

	/**
	 * AddEmployee pane that allows you to add an employee.
	 * 
	 * @author Daniel
	 *
	 */
	public static class AddEmployee extends BorderPane {

		/**
		 * Classic constructor. Sets up all the styles and Nodes on the Pane.
		 */
		public AddEmployee() {
			// Title, subtitle, etc.
			VBox topBox = new VBox();
			CustomText title = new CustomText("Meeting Manager", 64);
			topBox.getChildren().add(title);
			CustomText subtitle = new CustomText("Adding employee.", 20);
			topBox.getChildren().add(subtitle);

			setMargin(topBox, new Insets(10));
			setTop(topBox);

			GridPane grid = new GridPane();

			grid.setHgap(10);
			grid.setVgap(20);
			grid.setPadding(new Insets(25, 25, 25, 25));

			CustomText id = new CustomText("ID:", 30);
			grid.add(id, 0, 0);

			TextField idTextField = new TextField();
			idTextField.setFont(Font.font("Arial", 20));
			idTextField.setDisable(true);

			// Auto-generate the ID by default so that they don't have to make one up.
			String nextID = (Company.getEmployees().isEmpty()) ? "1"
					: String.valueOf(Company.getEmployees().lastEntry().getValue().getId() + 1);

			idTextField.setText(nextID);

			// Add to the Pane.
			grid.add(idTextField, 1, 0);

			// Allow them to manually set ID
			CustomText autogen = new CustomText("Auto generate:", 30);
			grid.add(autogen, 2, 0);

			CheckBox autogenCheckbox = new CheckBox();
			autogenCheckbox.setSelected(true);
			grid.add(autogenCheckbox, 3, 0);

			autogenCheckbox.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent event) {
					if (idTextField.isDisable()) {
						// Re-enable the field.
						idTextField.setDisable(false);
					} else {
						// Clear and disable the field.
						idTextField.clear();
						idTextField.setDisable(true);

						// Set the ID field back to the nextID variable.
						idTextField.setText(nextID);
					}
				}
			});

			// Form elements.
			CustomText firstNameLabel = new CustomText("First name:", 30);
			grid.add(firstNameLabel, 0, 1);

			TextField firstNameTextField = new TextField();
			firstNameTextField.setFont(Font.font("Arial", 20));
			grid.add(firstNameTextField, 1, 1);

			CustomText lastNameLabel = new CustomText("Last name:", 30);
			grid.add(lastNameLabel, 0, 2);

			TextField lastNameTextField = new TextField();
			lastNameTextField.setFont(Font.font("Arial", 20));
			grid.add(lastNameTextField, 1, 2);

			CustomText jobLabel = new CustomText("Job title:", 30);
			grid.add(jobLabel, 0, 3);

			TextField jobTextField = new TextField();
			jobTextField.setFont(Font.font("Arial", 20));
			grid.add(jobTextField, 1, 3);

			CustomButton addButton = new CustomButton("Add Employee", 20);
			addButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					try {
						// Validate the employee details. Any errors will cause an exception.
						Employee toAdd = Validation.validateEmployeeDetails(idTextField.getText(),
								firstNameTextField.getText(), lastNameTextField.getText(), jobTextField.getText());

						// Add the Employee
						Company.getEmployees().put(toAdd.getId(), toAdd);

						// Display success message.
						CustomText successText = new CustomText("Employee successfully added", 16);
						grid.add(successText, 1, 4);

						// Remove the message after 2 seconds.
						Timeline timer = new Timeline(
								new KeyFrame(Duration.seconds(2), new EventHandler<ActionEvent>() {
									@Override
									public void handle(ActionEvent event) {
										grid.getChildren().remove(successText);
									}
								}));
						timer.play();

						// Clear the text fields for useability sake
						idTextField.clear();
						firstNameTextField.clear();
						lastNameTextField.clear();
						jobTextField.clear();
					} catch (EmployeeDetailsInvalidID | GenericFieldEmpty | EmployeeExists e) {
						// Display the error to the user.
						CustomText errorText = new CustomText(e.getMessage(), 16);
						grid.add(errorText, 1, 4);

						Timeline timer = new Timeline(
								new KeyFrame(Duration.seconds(2), new EventHandler<ActionEvent>() {
									@Override
									public void handle(ActionEvent event) {
										grid.getChildren().remove(errorText);
									}
								}));
						timer.play();
					}
				}
			});

			grid.add(addButton, 0, 4);

			setLeft(grid);

			CustomButton backButton = new CustomButton("Back", 16);
			backButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					GUIHandler.changePane(new ManageEmployees());
				}
			});
			setMargin(backButton, new Insets(10, 5, 5, 5));

			setBottom(backButton);
		}
	}

	/**
	 * EmployeeDiary pane that shows an Employee's diary.
	 * 
	 * @author Daniel
	 *
	 */
	public static class EmployeeDiary extends BorderPane {

		/**
		 * Employee reference
		 */
		Employee employee;

		/**
		 * List that contains the months.
		 */
		ArrayList<String> monthsList = new ArrayList<String>() {
			{
				add("January");
				add("February");
				add("March");
				add("April");
				add("May");
				add("June");
				add("July");
				add("August");
				add("September");
				add("October");
				add("November");
				add("December");
			}
		};

		/**
		 * Classic constructor. Sets up all the styles and Nodes on the Pane.
		 */
		public EmployeeDiary(Employee employee) {
			this.employee = employee;

			// Title, subtitle, etc.
			VBox topBox = new VBox();
			CustomText title = new CustomText("Meeting Manager", 64);
			topBox.getChildren().add(title);

			CustomText subtitle = new CustomText("Select a day to view meetings.", 20);
			topBox.getChildren().add(subtitle);

			// Add some space between the subtitle and the employee name.
			Region spacer = new Region();
			spacer.setMinHeight(10);
			topBox.getChildren().add(spacer);

			// Add employee name to the Pane.
			CustomText employeeName = new CustomText("Employee: " + employee.getFullName(), 20);
			topBox.getChildren().add(employeeName);

			// Add a margin to the topbox.
			setMargin(topBox, new Insets(10));
			setTop(topBox);

			// To seperate the years/months and the actual calendar.
			BorderPane centerPane = new BorderPane();

			// Container for the month/year dropdowns etc.
			GridPane monthYearControls = new GridPane();
			monthYearControls.setHgap(10);

			// Variables for starting points.
			int currYear = Calendar.getInstance().get(Calendar.YEAR);
			int currMonthNo = Calendar.getInstance().get(Calendar.MONTH);

			// Caldendar grid
			GridPane calendar = new GridPane();

			// Year and months combo boxes
			ComboBox<Integer> yearComboBox = new ComboBox<Integer>();
			ComboBox<String> monthsComboBox = new ComboBox<String>();
			monthsComboBox.getItems().addAll(monthsList);

			// Month, seperator and year labels
			CustomText monthLabel = new CustomText(monthsList.get(currMonthNo), 20);
			monthYearControls.add(monthLabel, 0, 0);

			CustomText seperator = new CustomText("-", 20);
			monthYearControls.add(seperator, 1, 0);

			CustomText yearLabel = new CustomText(String.valueOf(currYear), 20);
			monthYearControls.add(yearLabel, 2, 0);
			
			CustomButton taskViewButton = new CustomButton("Change to task view", 16);
			taskViewButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					GUIHandler.changePane(new TaskListGUI(employee));
				}
			});
			monthYearControls.add(taskViewButton, 28, 0);

			// Event handlers
			monthLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent arg0) {
					monthYearControls.getChildren().remove(monthLabel);
					monthYearControls.add(monthsComboBox, 0, 0);
				}
			});

			monthsComboBox.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent arg0) {
					monthYearControls.getChildren().remove(monthsComboBox);
					monthLabel.setText(String.valueOf(monthsComboBox.getValue()));
					monthYearControls.add(monthLabel, 0, 0);

					String string = monthLabel.getText() + " " + yearComboBox.getValue();
					SimpleDateFormat format = new SimpleDateFormat("MMMM yyyy");

					try {
						Date date = format.parse(string);
						drawCal(calendar, date);
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
			});

			yearLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent arg0) {
					monthYearControls.getChildren().remove(yearLabel);
					monthYearControls.add(yearComboBox, 2, 0);
				}
			});

			yearComboBox.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent arg0) {
					monthYearControls.getChildren().remove(yearComboBox);
					yearLabel.setText(String.valueOf(yearComboBox.getValue()));
					monthYearControls.add(yearLabel, 2, 0);

					String string = monthLabel.getText() + " " + yearComboBox.getValue();
					SimpleDateFormat format = new SimpleDateFormat("MMMM yyyy");

					try {
						Date date = format.parse(string);
						drawCal(calendar, date);
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
			});

			// Set up years.
			yearComboBox.getItems().add(currYear);
			yearComboBox.getSelectionModel().select(0);

			for (int i = 1; i <= 3; i++) {
				yearComboBox.getItems().add(currYear + i);
			}

			centerPane.setTop(monthYearControls);

			// Days of the week.
			ArrayList<String> weeklist = new ArrayList<String>() {
				{
					add("Mon");
					add("Tue");
					add("Wed");
					add("Thu");
					add("Fri");
					add("Sat");
					add("Sun");
				}
			};

			for (int i = 0; i < 7; i++) {
				CustomText tempText = new CustomText(weeklist.get(i), 20);
				calendar.add(tempText, i, 3);
			}

			// Setup the actual calendar.
			drawCal(calendar, new Date());

			centerPane.setCenter(calendar);
			centerPane.setPadding(new Insets(5));
			setCenter(centerPane);

			BorderPane bottomBox = new BorderPane();

			CustomButton backButton = new CustomButton("Back", 16);
			backButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					GUIHandler.changePane(new ManageDiaries());
				}
			});
			bottomBox.setLeft(backButton);

			CustomButton addButton = new CustomButton("Add Meeting", 16);
			addButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					GUIHandler.changePane(new AddMeeting(employee));
				}
			});

			bottomBox.setCenter(addButton);

			CustomButton undoButton = new CustomButton("Undo", 16);
			undoButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					employee.undo();

					String string = monthLabel.getText() + " " + yearComboBox.getValue();
					SimpleDateFormat format = new SimpleDateFormat("MMMM yyyy");

					try {
						Date date = format.parse(string);
						drawCal(calendar, date);
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
			});

			bottomBox.setRight(undoButton);

			setMargin(bottomBox, new Insets(10, 5, 5, 5));
			setBottom(bottomBox);
		}

		/**
		 * Method that re-draws the calendar based on the date given.
		 * 
		 * @param calendar
		 * @param date
		 */
		public void drawCal(GridPane calendar, Date date) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
			int dayOfWeekMonthStart = cal.get(Calendar.DAY_OF_WEEK) - 1;
			int lastDayOfMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

			// Weird way to get around the fact that Calendar starts week on Sunday.
			if (dayOfWeekMonthStart == 0)
				dayOfWeekMonthStart = 7;

			int counter = 1;

			for (int i = 0; i < 5; i++) {
				for (int j = 0; j < 7; j++) {
					VBox calSquare = new VBox();
					Label day = new Label();

					// Black-out days of the week to sync the start and end of month days.
					if (i == 0 && (j + 1) < dayOfWeekMonthStart || counter > lastDayOfMonth) {
						calSquare.setStyle("-fx-background-color: black; -fx-border-color: black;");
					} else {
						day.setText(ordinal(counter));
						calSquare.getChildren().add(day);

						// Add meeting text
						SimpleDateFormat format = new SimpleDateFormat("MMMM d yyyy");

						String toFormat = monthsList.get(cal.get(Calendar.MONTH)) + " " + counter + " "
								+ cal.get(Calendar.YEAR);
						String toFormatOrdinal = monthsList.get(cal.get(Calendar.MONTH)) + " " + ordinal(counter) + " "
								+ cal.get(Calendar.YEAR);

						try {
							// Meetings text on the cal square
							LinkedList<Meeting> meetings = employee.getDiary().getMeetingsOnDay(format.parse(toFormat));

							int size = meetings.size();
							if (size > 0) {
								Label noMeetings = new Label(meetings.size() + " meetings");
								calSquare.getChildren().add(noMeetings);
							}

							counter++;
							calSquare.setStyle("-fx-background-color: white; -fx-border-color: black;");

							calSquare.setOnMouseClicked(new EventHandler<MouseEvent>() {
								@Override
								public void handle(MouseEvent event) {
									// View the day specifics if clicked.
									GUIHandler.changePane(new ViewDay(employee, meetings, toFormatOrdinal));
								}
							});

						} catch (ParseException e) {
							e.printStackTrace();
						}
					}
					calSquare.setMinSize(80, 50);
					calendar.add(calSquare, j, i + 4);
				}
			}
		}

		/**
		 * Gets the ordinal value of given number. Used to add the ordinal to calendar
		 * dates.
		 * 
		 * @param num
		 *            Number to get the ordinal value of.
		 * @return Number and it's ordinal value.
		 */
		String ordinal(int num) {
			String[] suffix = { "th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th" };
			int m = num % 100;
			return String.valueOf(num) + suffix[(m > 3 && m < 21) ? 0 : (m % 10)];
		}
	}

	/**
	 * AddMeeting pane that allows you to add meeting.
	 * 
	 * @author Daniel
	 *
	 */
	public static class AddMeeting extends BorderPane {

		/**
		 * Classic constructor. Sets up all the styles and Nodes on the Pane.
		 */
		public AddMeeting(Employee employee) {
			VBox topBox = new VBox();
			CustomText title = new CustomText("Meeting Manager", 64);
			topBox.getChildren().add(title);
			CustomText subtitle = new CustomText("Adding meeting.", 20);
			topBox.getChildren().add(subtitle);

			// Add some space between the subtitle and the employee name.
			Region spacer = new Region();
			spacer.setMinHeight(10);
			topBox.getChildren().add(spacer);

			// Add employee name to the Pane.
			CustomText employeeName = new CustomText("Employee: " + employee.getFullName(), 20);
			topBox.getChildren().add(employeeName);

			setMargin(topBox, new Insets(10));
			setTop(topBox);

			GridPane grid = new GridPane();

			grid.setHgap(10);
			grid.setVgap(20);
			grid.setPadding(new Insets(15, 25, 25, 25));

			CustomText description = new CustomText("Description:", 30);
			grid.add(description, 0, 0);

			TextField descTextField = new TextField();
			descTextField.setFont(Font.font("Arial", 20));
			grid.add(descTextField, 1, 0);

			CustomText dateLabel = new CustomText("Date:", 30);
			grid.add(dateLabel, 0, 1);

			DatePicker datePicker = new DatePicker();

			grid.add(datePicker, 1, 1);

			CustomText startTimeLabel = new CustomText("Start Time:", 30);
			grid.add(startTimeLabel, 0, 2);

			NumberSpinner startTimePicker = new NumberSpinner(LocalDateTime.now().getHour(),
					LocalDateTime.now().getMinute());
			startTimePicker.setStyle("-fx-font-size: 18px;");
			grid.add(startTimePicker, 1, 2);

			CustomText endTimeLabel = new CustomText("End Time:", 30);
			grid.add(endTimeLabel, 0, 3);

			NumberSpinner endTimePicker = new NumberSpinner(LocalDateTime.now().getHour() + 1,
					LocalDateTime.now().getMinute());
			endTimePicker.setStyle("-fx-font-size: 18px;");
			grid.add(endTimePicker, 1, 3);

			CustomButton saveButton = new CustomButton("Add Meeting", 20);
			saveButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					try {
						// Validate the employee details. Any errors will cause an exception.
						Meeting toAdd = Validation.validateMeeting(datePicker.getValue(), startTimePicker.getText(),
								endTimePicker.getText(), descTextField.getText(), employee.getDiary());

						// Add the Employee
						employee.addMeeting(toAdd);

						// Display success message.
						CustomText successText = new CustomText("Meeting successfully added", 16);
						grid.add(successText, 1, 4);

						// Remove the message after 2 seconds.
						Timeline timer = new Timeline(
								new KeyFrame(Duration.seconds(2), new EventHandler<ActionEvent>() {
									@Override
									public void handle(ActionEvent event) {
										grid.getChildren().remove(successText);
									}
								}));
						timer.play();

						// Clear the text fields for usability sake
						descTextField.clear();
						startTimePicker.clear();
						endTimePicker.clear();
					} catch (MeetingTimeBeforeStart | MeetingTimeSameTime | MeetingTimeStartConflict
							| GenericFieldEmpty e) {
						// Display the error to the user.
						CustomText errorText = new CustomText(e.getMessage(), 16);
						grid.add(errorText, 1, 4);

						Timeline timer = new Timeline(
								new KeyFrame(Duration.seconds(2), new EventHandler<ActionEvent>() {
									@Override
									public void handle(ActionEvent event) {
										grid.getChildren().remove(errorText);
									}
								}));
						timer.play();
					}
				}
			});

			grid.add(saveButton, 0, 4);

			setLeft(grid);

			CustomButton backButton = new CustomButton("Back", 16);
			backButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					GUIHandler.changePane(new EmployeeDiary(employee));
				}
			});
			setMargin(backButton, new Insets(10, 5, 5, 5));

			setBottom(backButton);
		}
	}

	public static class CompanyMeeting extends BorderPane {
		@SuppressWarnings("unchecked")
		public CompanyMeeting() {
			// Add the
			LinkedList<Employee> searchList = new LinkedList<Employee>();
			VBox topPane = new VBox();

			CustomText title = new CustomText("Meeting Manager", 64);
			topPane.getChildren().add(title);

			CustomText subtitle = new CustomText("Click an Employee to edit their details.", 20);
			topPane.getChildren().add(subtitle);

			Label label1 = new Label("Search:");
			TextField searchField = new TextField();
			HBox hb = new HBox();
			hb.getChildren().addAll(label1, searchField);
			hb.setSpacing(10);
			hb.setAlignment(Pos.CENTER_RIGHT);

			topPane.getChildren().add(hb);

			setMargin(topPane, new Insets(10));

			setTop(topPane);

			TableView<Employee> table = new TableView<Employee>();

			TableColumn<Employee, String> idCol = new TableColumn<Employee, String>("ID");
			idCol.setCellValueFactory(new PropertyValueFactory<Employee, String>("id"));

			TableColumn<Employee, String> firstNameCol = new TableColumn<Employee, String>("First Name");
			firstNameCol.setCellValueFactory(new PropertyValueFactory<Employee, String>("firstName"));

			TableColumn<Employee, String> lastNameCol = new TableColumn<Employee, String>("Last Name");
			lastNameCol.setCellValueFactory(new PropertyValueFactory<Employee, String>("lastName"));

			TableColumn<Employee, String> jobTitleCol = new TableColumn<Employee, String>("Job Title");
			jobTitleCol.setCellValueFactory(new PropertyValueFactory<Employee, String>("jobTitle"));

			table.getColumns().addAll(idCol, firstNameCol, lastNameCol, jobTitleCol);

			// Table column widths (odd value so that it offsets the margin)s
			idCol.prefWidthProperty().bind(table.widthProperty().divide(4.02));
			firstNameCol.prefWidthProperty().bind(table.widthProperty().divide(4.02));
			lastNameCol.prefWidthProperty().bind(table.widthProperty().divide(4.02));
			jobTitleCol.prefWidthProperty().bind(table.widthProperty().divide(4.02));

			setMargin(table, new Insets(10, 5, 5, 5));

			setLeft(table);

			TableView<Employee> tableRight = new TableView<Employee>();

			TableColumn<Employee, String> idColRight = new TableColumn<Employee, String>("ID");
			idColRight.setCellValueFactory(new PropertyValueFactory<Employee, String>("id"));

			TableColumn<Employee, String> firstNameColRight = new TableColumn<Employee, String>("First Name");
			firstNameColRight.setCellValueFactory(new PropertyValueFactory<Employee, String>("firstName"));

			TableColumn<Employee, String> lastNameColRight = new TableColumn<Employee, String>("Last Name");
			lastNameColRight.setCellValueFactory(new PropertyValueFactory<Employee, String>("lastName"));

			TableColumn<Employee, String> jobTitleColRight = new TableColumn<Employee, String>("Job Title");
			jobTitleColRight.setCellValueFactory(new PropertyValueFactory<Employee, String>("jobTitle"));

			tableRight.getColumns().addAll(idColRight, firstNameColRight, lastNameColRight, jobTitleColRight);

			// Table column widths (odd value so that it offsets the margin)s
			idCol.prefWidthProperty().bind(table.widthProperty().divide(4.02));
			firstNameCol.prefWidthProperty().bind(table.widthProperty().divide(4.02));
			lastNameCol.prefWidthProperty().bind(table.widthProperty().divide(4.02));
			jobTitleCol.prefWidthProperty().bind(table.widthProperty().divide(4.02));

			setMargin(tableRight, new Insets(10, 5, 5, 5));

			setRight(tableRight);

			ObservableList<Employee> data = FXCollections.observableArrayList();

			for (Entry<Integer, Employee> entry : Company.getEmployees().entrySet()) {
				data.add(entry.getValue());
			}

			table.setItems(data);

			// Credit: https://stackoverflow.com/a/30194680/3102362
			// Row click events.
			ObservableList<Employee> dataRight = FXCollections.observableArrayList();
			table.setRowFactory(tv -> {
				TableRow<Employee> row = new TableRow<>();
				row.setOnMouseClicked(event -> {
					if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {

						Employee clickedRow = row.getItem();
						searchList.add(clickedRow);
						System.out.println(clickedRow);

						dataRight.add(clickedRow);

						tableRight.setItems(dataRight);

					}
				});

				return row;
			});

			// Searching the table (Credit: https://stackoverflow.com/a/44317900/3102362)
			FilteredList<Employee> filteredData = new FilteredList<>(data, p -> true);

			// Set the filter Predicate whenever the filter changes.
			searchField.textProperty().addListener((observable, oldValue, newValue) -> {
				filteredData.setPredicate(myObject -> {
					// If filter text is empty, display all employees.
					if (newValue == null || newValue.isEmpty()) {
						return true;
					}

					// Compare first name, last name and id fields with filter.
					String lowerCaseFilter = newValue.toLowerCase();

					if (String.valueOf(myObject.getFirstName()).toLowerCase().contains(lowerCaseFilter)) {
						return true; // Filter matches firstName.
					} else if (String.valueOf(myObject.getLastName()).toLowerCase().contains(lowerCaseFilter)) {
						return true; // Filter matches lastName.
					} else if (String.valueOf(myObject.getId()).toLowerCase().contains(lowerCaseFilter)) {
						return true; // Filter matches ID.
					} else if (String.valueOf(myObject.getJobTitle()).toLowerCase().contains(lowerCaseFilter)) {
						return true; // Filter matches job title.
					}

					return false; // Does not match.
				});
			});

			// Sort the data
			SortedList<Employee> sortedData = new SortedList<>(filteredData);
			sortedData.comparatorProperty().bind(table.comparatorProperty());

			table.setItems(sortedData);

			BorderPane bottomBox = new BorderPane();

			CustomButton backButton = new CustomButton("Back", 16);
			backButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					GUIHandler.changePane(new MainMenu());
				}
			});
			bottomBox.setLeft(backButton);

			CustomButton searchButton = new CustomButton("Search", 16);
			searchButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					GUIHandler.changePane(new Search(searchList));
				}
			});

			bottomBox.setRight(searchButton);

			setMargin(bottomBox, new Insets(10, 5, 5, 5));
			setBottom(bottomBox);
		}

	}

	public static class Search extends BorderPane {
		@SuppressWarnings("unchecked")
		public Search(LinkedList<Employee> searchList) {
			TableView<Meeting> table = new TableView<Meeting>();
			VBox topPane = new VBox();

			CustomText title = new CustomText("Meeting Manager", 64);
			topPane.getChildren().add(title);

			CustomText subtitle = new CustomText("Employee Search Results, Times all Employees are Free:", 20);
			topPane.getChildren().add(subtitle);

			HBox hb = new HBox();
			hb.setSpacing(10);
			hb.setAlignment(Pos.CENTER_RIGHT);

			topPane.getChildren().add(hb);

			setMargin(topPane, new Insets(10));

			setTop(topPane);

			GridPane grid = new GridPane();

			grid.setHgap(5);
			grid.setVgap(10);
			grid.setPadding(new Insets(15, 25, 25, 25));

			CustomText dateLabel = new CustomText("Date:", 30);
			grid.add(dateLabel, 0, 1);

			DatePicker datePicker = new DatePicker();
			datePicker.setDayCellFactory(picker -> new DateCell() {
				@Override
				public void updateItem(LocalDate date, boolean empty) {
					super.updateItem(date, empty);
					setDisable(empty || date.getDayOfWeek() == DayOfWeek.MONDAY);
				}
			});
			datePicker.setEditable(false);

			grid.add(datePicker, 1, 1);

			CustomText startTimeLabel = new CustomText("Start Time:", 30);
			grid.add(startTimeLabel, 0, 2);

			NumberSpinner startTimePicker = new NumberSpinner(LocalDateTime.now().getHour(),
					LocalDateTime.now().getMinute());
			startTimePicker.setStyle("-fx-font-size: 18px;");
			grid.add(startTimePicker, 1, 2);

			CustomText endTimeLabel = new CustomText("End Time:", 30);
			grid.add(endTimeLabel, 0, 3);

			NumberSpinner endTimePicker = new NumberSpinner(LocalDateTime.now().getHour(),
					LocalDateTime.now().getMinute());
			endTimePicker.setStyle("-fx-font-size: 18px;");
			grid.add(endTimePicker, 1, 3);

			setLeft(grid);
			CustomButton searchButton = new CustomButton("SearchFreeTimes", 20);
			searchButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					try {
						// Validate the employee details. Any errors will cause an exception.
						LocalDate date = datePicker.getValue();
						String startTimeString = startTimePicker.getText();
						String endTimeString = endTimePicker.getText();

						SimpleDateFormat format = new SimpleDateFormat("yyy-MM-dd kk:mm");

						Date startDate = new Date();
						Date endDate = new Date();

						try {
							startDate = format.parse(date + " " + startTimeString);
							endDate = format.parse(date + " " + endTimeString);

						} catch (ParseException e) {
							e.printStackTrace();
						}
						
						long startTime = System.nanoTime();
						LinkedList<Meeting> results = Company.search(searchList, startDate, endDate);
						long endTime = System.nanoTime();

						Label duration = new Label("" + ((endTime - startTime) / 1000000.0) + " milliseconds");

						ObservableList<Meeting> data = FXCollections.observableArrayList();
						data.addAll(results);
						table.setItems(data);
						
						grid.add(duration, 1, 5);
						Timeline timer = new Timeline(
								new KeyFrame(Duration.seconds(5), new EventHandler<ActionEvent>() {
									@Override
									public void handle(ActionEvent event) {
										grid.getChildren().remove(duration);
									}
								}));
						timer.play();

					} catch (Exception e) {
						CustomText successText = new CustomText("Neither Employees have Meetings between these Times",
								16);
						grid.add(successText, 1, 5);
						
						Timeline timer = new Timeline(
								new KeyFrame(Duration.seconds(2), new EventHandler<ActionEvent>() {
									@Override
									public void handle(ActionEvent event) {
										grid.getChildren().remove(successText);
									}
								}));
						timer.play();
					}
				}
			});
			grid.add(searchButton, 1, 4);

			table.setEditable(false);

			TableColumn<Meeting, String> startTimeCol = new TableColumn<Meeting, String>("Start of Free Time");
			startTimeCol.setCellValueFactory(new PropertyValueFactory<Meeting, String>("startTime"));

			TableColumn<Meeting, String> endTimeCol = new TableColumn<Meeting, String>("End of Free Time");
			endTimeCol.setCellValueFactory(new PropertyValueFactory<Meeting, String>("endTime"));

			startTimeCol.prefWidthProperty().bind(table.widthProperty().divide(2.02));
			endTimeCol.prefWidthProperty().bind(table.widthProperty().divide(2.02));

			table.getColumns().addAll(startTimeCol, endTimeCol);
			setMargin(table, new Insets(10, 5, 5, 5));

			setCenter(table);

			BorderPane bottomBox = new BorderPane();

			CustomButton backButton = new CustomButton("Back", 16);
			backButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					GUIHandler.changePane(new MainMenu());
				}
			});
			bottomBox.setLeft(backButton);

			CustomButton addMultipul = new CustomButton("Search for free times", 16);
			addMultipul.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					GUIHandler.changePane(new AddMultiMeeting(searchList));
				}
			});

			bottomBox.setRight(addMultipul);

			setMargin(bottomBox, new Insets(10, 5, 5, 5));
			setBottom(bottomBox);

		}
	}

	/**
	 * ViewDay pane that allows you to view a day's meetings.
	 * 
	 * @author Daniel
	 *
	 */
	public static class ViewDay extends BorderPane {
		@SuppressWarnings("unchecked")
		public ViewDay(Employee employee, LinkedList<Meeting> meetings, String date) {
			VBox topBox = new VBox();
			CustomText title = new CustomText("Meeting Manager", 64);
			topBox.getChildren().add(title);

			CustomText subtitle = new CustomText("Click a meeting to view or edit it", 20);
			topBox.getChildren().add(subtitle);

			// Add some space between the subtitle and the employee name.
			Region spacer = new Region();
			spacer.setMinHeight(10);
			topBox.getChildren().add(spacer);

			// Add employee name to the Pane.
			CustomText employeeName = new CustomText("Employee: " + employee.getFullName(), 20);
			topBox.getChildren().add(employeeName);

			// Add a margin to the topbox.
			setMargin(topBox, new Insets(10));
			setTop(topBox);

			TableView<Meeting> table = new TableView<Meeting>();

			TableColumn<Meeting, String> monthThing = new TableColumn<>();

			HBox colBox = new HBox();

			CustomText backArrow = new CustomText("<", 26);

			Region space = new Region();
			space.setMinWidth(200);

			CustomText dateColHeader = new CustomText(date, 26);

			colBox.getChildren().add(backArrow);
			colBox.getChildren().add(space);
			colBox.getChildren().add(dateColHeader);

			// Back to the diary if "<" is clicked.
			colBox.setOnMouseClicked(e -> GUIHandler.changePane(new EmployeeDiary(employee)));
			monthThing.setGraphic(colBox);

			// Table set-up
			TableColumn<Meeting, String> descCol = new TableColumn<Meeting, String>("Description");
			descCol.setCellValueFactory(new PropertyValueFactory<Meeting, String>("description"));

			TableColumn<Meeting, String> startTimeCol = new TableColumn<Meeting, String>("Start Time");
			startTimeCol.setCellValueFactory(new PropertyValueFactory<Meeting, String>("rawStart"));

			TableColumn<Meeting, String> endTimeCol = new TableColumn<Meeting, String>("End Time");
			endTimeCol.setCellValueFactory(new PropertyValueFactory<Meeting, String>("rawEnd"));

			monthThing.getColumns().addAll(descCol, startTimeCol, endTimeCol);

			table.getColumns().add(monthThing);

			// Table column widths (odd value so that it offsets the margin)
			descCol.prefWidthProperty().bind(table.widthProperty().divide(2.02));
			startTimeCol.prefWidthProperty().bind(table.widthProperty().divide(4.02));
			endTimeCol.prefWidthProperty().bind(table.widthProperty().divide(4.02));

			setMargin(table, new Insets(10, 5, 5, 5));

			setCenter(table);

			ObservableList<Meeting> data = FXCollections.observableArrayList(meetings);

			table.setItems(data);

			// Credit: https://stackoverflow.com/a/30194680/3102362
			// Row click events.
			table.setRowFactory(tv -> {
				TableRow<Meeting> row = new TableRow<>();
				row.setOnMouseClicked(event -> {
					if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {

						Meeting clickedRow = row.getItem();

						// Switch to EditMeeting and pass the selected meeting and employee.
						GUIHandler.changePane(new EditMeeting(employee, clickedRow, meetings, date));
					}
				});
				return row;
			});
		}
	}

	/**
	 * EditMeeting pane that allows you to edit meeting.
	 * 
	 * @author Daniel
	 *
	 */
	public static class EditMeeting extends BorderPane {
		public EditMeeting(Employee employee, Meeting meeting, LinkedList<Meeting> meetings, String date) {
			VBox topBox = new VBox();
			CustomText title = new CustomText("Meeting Manager", 64);
			topBox.getChildren().add(title);
			CustomText subtitle = new CustomText("Editing meeting.", 20);
			topBox.getChildren().add(subtitle);

			// Add some space between the subtitle and the employee name.
			Region spacer = new Region();
			spacer.setMinHeight(10);
			topBox.getChildren().add(spacer);

			setMargin(topBox, new Insets(10));
			setTop(topBox);

			GridPane grid = new GridPane();

			grid.setHgap(10);
			grid.setVgap(20);
			grid.setPadding(new Insets(15, 25, 25, 25));

			CustomText description = new CustomText("Description:", 30);
			grid.add(description, 0, 0);

			TextField descTextField = new TextField();
			descTextField.setFont(Font.font("Arial", 20));

			descTextField.setText(meeting.getDescription());
			grid.add(descTextField, 1, 0);

			CustomText dateLabel = new CustomText("Date:", 30);

			grid.add(dateLabel, 0, 1);

			// Get values from date
			Calendar cal = Calendar.getInstance();
			cal.setTime(meeting.getStartTime());

			// Set date picker to current meeting date.
			DatePicker datePicker = new DatePicker(LocalDate.of(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1,
					cal.get((Calendar.DAY_OF_MONTH))));

			grid.add(datePicker, 1, 1);

			CustomText startTimeLabel = new CustomText("Start Time:", 30);
			grid.add(startTimeLabel, 0, 2);

			NumberSpinner startTimePicker = new NumberSpinner(Integer.parseInt(meeting.getRawStart().substring(0, 2)),
					Integer.parseInt(meeting.getRawStart().substring(3, 5)));

			startTimePicker.setStyle("-fx-font-size: 18px;");
			grid.add(startTimePicker, 1, 2);

			CustomText endTimeLabel = new CustomText("End Time:", 30);
			grid.add(endTimeLabel, 0, 3);

			NumberSpinner endTimePicker = new NumberSpinner(Integer.parseInt(meeting.getRawEnd().substring(0, 2)),
					Integer.parseInt(meeting.getRawEnd().substring(3, 5)));
			endTimePicker.setStyle("-fx-font-size: 18px;");
			grid.add(endTimePicker, 1, 3);

			CustomButton saveButton = new CustomButton("Save Meeting", 20);
			saveButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					try {
						Meeting temp = meeting;

						// Avoid conflicting with itself
						employee.deleteMeeting(meeting);

						// Validate the employee details. Any errors will cause an exception.
						Meeting toAdd = Validation.validateMeeting(datePicker.getValue(), startTimePicker.getText(),
								endTimePicker.getText(), descTextField.getText(), employee.getDiary());

						// Add the Meeting
						employee.editMeeting(temp, toAdd);

						// Display success message.
						CustomText successText = new CustomText("Meeting successfully edited.", 16);
						grid.add(successText, 1, 5);

						// Remove the message after 2 seconds.
						Timeline timer = new Timeline(
								new KeyFrame(Duration.seconds(2), new EventHandler<ActionEvent>() {
									@Override
									public void handle(ActionEvent event) {
										grid.getChildren().remove(successText);
									}
								}));
						timer.play();

					} catch (MeetingTimeBeforeStart | MeetingTimeSameTime | MeetingTimeStartConflict
							| GenericFieldEmpty e) {
						// Display the error to the user.
						CustomText errorText = new CustomText(e.getMessage(), 16);
						grid.add(errorText, 1, 5);

						Timeline timer = new Timeline(
								new KeyFrame(Duration.seconds(2), new EventHandler<ActionEvent>() {
									@Override
									public void handle(ActionEvent event) {
										grid.getChildren().remove(errorText);
									}
								}));
						timer.play();

					}
				}
			});

			grid.add(saveButton, 0, 4);

			CustomButton deleteButton = new CustomButton("Delete Meeting", 20);
			deleteButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					employee.deleteMeeting(meeting);
					meetings.remove(meeting);
					GUIHandler.changePane(new ViewDay(employee, meetings, date));
				}
			});
			grid.add(deleteButton, 1, 4);

			setLeft(grid);

			CustomButton backButton = new CustomButton("Back", 16);
			backButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {

					GUIHandler.changePane(new EmployeeDiary(employee));

				}
			});
			setMargin(backButton, new Insets(10, 5, 5, 5));

			setBottom(backButton);
		}
	}
	
	public static class TaskListGUI extends BorderPane {
	@SuppressWarnings("unchecked")
	public TaskListGUI(Employee employee) {
		// Add the
		VBox topPane = new VBox();

		CustomText title = new CustomText("Meeting Manager", 64);
		topPane.getChildren().add(title);

		CustomText subtitle = new CustomText("Check off tasks you have completed.", 20);
		topPane.getChildren().add(subtitle);

		setMargin(topPane, new Insets(10));

		setTop(topPane);
		
		TableView<Task> table = new TableView<Task>();

		TableColumn<Task, String> taskCol = new TableColumn<Task, String>("Task");
		taskCol.setCellValueFactory(new PropertyValueFactory<Task, String>("task"));

		TableColumn<Task, String> descriptionCol = new TableColumn<Task, String>("Description");
		descriptionCol.setCellValueFactory(new PropertyValueFactory<Task, String>("description"));

		TableColumn<Task, String> priorityCol = new TableColumn<Task, String>("Priority");
		priorityCol.setCellValueFactory(new PropertyValueFactory<Task, String>("priority"));
		
		table.getColumns().addAll(taskCol, descriptionCol, priorityCol);

		// Table column widths (odd value so that it offsets the margin)s
		taskCol.prefWidthProperty().bind(table.widthProperty().divide(5.50));
		descriptionCol.prefWidthProperty().bind(table.widthProperty().divide(2.00));
		priorityCol.prefWidthProperty().bind(table.widthProperty().divide(3.20));
		
		setMargin(table, new Insets(10, 5, 5, 5));
		
		// Credit: https://stackoverflow.com/a/30194680/3102362
		// Row click events.
		table.setRowFactory(tv -> {
			TableRow<Task> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {

					Task clickedRow = row.getItem();

					// Switch to EditEmployee and pass the selected employee.
					GUIHandler.changePane(new EditTask(employee, clickedRow));
				}
			});
			return row;
		});

		setCenter(table);
		
		// TEST DATA
		ObservableList<Task> data = FXCollections.observableArrayList(employee.getTaskList());
		table.setItems(data);
		
		BorderPane bottomBox = new BorderPane();
		
		CustomButton backButton = new CustomButton("Back", 16);
		backButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				GUIHandler.changePane(new EmployeeDiary(employee));
			}
		});
		bottomBox.setLeft(backButton);
		
		CustomButton addTaskButton = new CustomButton("Add Task", 16);
		addTaskButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				GUIHandler.changePane(new AddTask(employee));
			}
		});
		bottomBox.setRight(addTaskButton);
		
		setMargin(bottomBox, new Insets(10, 5, 5, 5));
		setBottom(bottomBox);
		
		}
	
	}
	
	public static class AddTask extends BorderPane {
		public AddTask(Employee employee) {
			
			VBox topBox = new VBox();
			CustomText title = new CustomText("Meeting Manager", 64);
			topBox.getChildren().add(title);
			CustomText subtitle = new CustomText("Adding task.", 20);
			topBox.getChildren().add(subtitle);

			// Add some space between the subtitle and the employee name.
			Region spacer = new Region();
			spacer.setMinHeight(10);
			topBox.getChildren().add(spacer);

			// Add employee name to the Pane.
			CustomText employeeName = new CustomText("Employee: " + employee.getFullName(), 20);
			topBox.getChildren().add(employeeName);

			setMargin(topBox, new Insets(10));
			setTop(topBox);

			GridPane grid = new GridPane();

			grid.setHgap(10);
			grid.setVgap(20);
			grid.setPadding(new Insets(15, 25, 25, 25));

			CustomText description = new CustomText("Description:", 30);
			grid.add(description, 0, 1);

			TextField descTextField = new TextField();
			descTextField.setFont(Font.font("Arial", 20));
			grid.add(descTextField, 1, 1);
			
			setCenter(grid);
			
			CustomText priority = new CustomText("Priority:", 30);
			grid.add(priority, 0, 4);

			TextField priorityTextField = new TextField();
			priorityTextField.setFont(Font.font("Arial", 20));
			grid.add(priorityTextField, 1, 4);
			
			CustomButton saveButton = new CustomButton("Add Task", 20);
			saveButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
			
					Task toAdd;
					try {
						toAdd = Validation.validateTask(descTextField.getText(), priorityTextField.getText());
						
						employee.addTask(toAdd);
						
						// Display success message.
						CustomText successText = new CustomText("Task successfully added", 16);
						grid.add(successText, 1, 6);

						// Remove the message after 2 seconds.
						Timeline timer = new Timeline(
								new KeyFrame(Duration.seconds(2), new EventHandler<ActionEvent>() {
									@Override
									public void handle(ActionEvent event) {
										grid.getChildren().remove(successText);
									}
								}));
						timer.play();

						// Clear the text fields for usability sake
						descTextField.clear();
						priorityTextField.clear();
					} catch (GenericFieldEmpty e) {
						// Display the error to the user.
						CustomText errorText = new CustomText(e.getMessage(), 16);
						grid.add(errorText, 1, 6);

						Timeline timer = new Timeline(
								new KeyFrame(Duration.seconds(2), new EventHandler<ActionEvent>() {
									@Override
									public void handle(ActionEvent event) {
										grid.getChildren().remove(errorText);
									}
								}));
						timer.play();
					}
			}
			
		});
			
		grid.add(saveButton, 0, 6);
		
		

		CustomButton backButton = new CustomButton("Back", 16);
		backButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				GUIHandler.changePane(new TaskListGUI(employee));
			}
		});
		setMargin(backButton, new Insets(10, 5, 5, 5));

		setBottom(backButton);
		
		}
		
	}
	
	public static class EditTask extends BorderPane {
		public EditTask(Employee employee, Task clickedRow) {
			VBox topBox = new VBox();
			CustomText title = new CustomText("Meeting Manager", 64);
			topBox.getChildren().add(title);
			CustomText subtitle = new CustomText("Editing task.", 20);
			topBox.getChildren().add(subtitle);

			// Add some space between the subtitle and the employee name.
			Region spacer = new Region();
			spacer.setMinHeight(10);
			topBox.getChildren().add(spacer);

			// Add employee name to the Pane.
			CustomText employeeName = new CustomText("Employee: " + employee.getFullName(), 20);
			topBox.getChildren().add(employeeName);

			setMargin(topBox, new Insets(10));
			setTop(topBox);

			GridPane grid = new GridPane();

			grid.setHgap(10);
			grid.setVgap(20);
			grid.setPadding(new Insets(15, 25, 25, 25));

			CustomText description = new CustomText("Description:", 30);
			grid.add(description, 0, 0);

			TextField descTextField = new TextField();
			descTextField.setFont(Font.font("Arial", 20));
			descTextField.setText(clickedRow.getDescription());
			grid.add(descTextField, 1, 0);
			
			setCenter(grid);
			
			CustomText priority = new CustomText("Priority:", 30);
			grid.add(priority, 0, 4);

			TextField priorityTextField = new TextField();
			priorityTextField.setFont(Font.font("Arial", 20));
			priorityTextField.setText(clickedRow.getPriority());
			grid.add(priorityTextField, 1, 4);
			
			CustomButton saveButton = new CustomButton("Save Meeting", 20);
			saveButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					
					Task toAdd = new Task(descTextField.getText(), priorityTextField.getText());
				
					// Add the Meeting
					employee.editTask(clickedRow, toAdd);

					// Display success message.
					CustomText successText = new CustomText("Task successfully edited.", 16);
					grid.add(successText, 1, 5);

					// Remove the message after 2 seconds.
					Timeline timer = new Timeline(
							new KeyFrame(Duration.seconds(2), new EventHandler<ActionEvent>() {
								@Override
								public void handle(ActionEvent event) {
									grid.getChildren().remove(successText);
								}
							}));
					timer.play();
				}
			});

			grid.add(saveButton, 0, 6);
			
			CustomButton deleteButton = new CustomButton("Delete Task", 20);
			deleteButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					employee.deleteTask(clickedRow);
					GUIHandler.changePane(new TaskListGUI(employee));
				}
			});
			grid.add(deleteButton, 1, 6);
			
			
			CustomButton backButton = new CustomButton("Back", 16);
			backButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					GUIHandler.changePane(new TaskListGUI(employee));
				}
			});
			setMargin(backButton, new Insets(10, 5, 5, 5));

			setBottom(backButton);
			
			
			
		}
	}
	
	public static class AddMultiMeeting extends BorderPane {
        public AddMultiMeeting(LinkedList<Employee> employeeMulti) {
            VBox topBox = new VBox();
            CustomText title = new CustomText("Meeting Manager", 64);
            topBox.getChildren().add(title);
            CustomText subtitle = new CustomText("Adding meeting to Multiul Employees.", 20);
            topBox.getChildren().add(subtitle);
 
            // Add some space between the subtitle and the employee name.
            Region spacer = new Region();
            spacer.setMinHeight(10);
            topBox.getChildren().add(spacer);
 
 
            setMargin(topBox, new Insets(10));
            setTop(topBox);
 
            GridPane grid = new GridPane();
 
            grid.setHgap(10);
            grid.setVgap(20);
            grid.setPadding(new Insets(15, 25, 25, 25));
 
            CustomText description = new CustomText("Description:", 30);
            grid.add(description, 0, 0);
 
            TextField descTextField = new TextField();
            descTextField.setFont(Font.font("Arial", 20));
            grid.add(descTextField, 1, 0);
 
            CustomText dateLabel = new CustomText("Date:", 30);
            grid.add(dateLabel, 0, 1);
 
            DatePicker datePicker = new DatePicker();
            datePicker.setDayCellFactory(picker -> new DateCell() {
                @Override
                public void updateItem(LocalDate date, boolean empty) {
                    super.updateItem(date, empty);
                    setDisable(empty || date.getDayOfWeek() == DayOfWeek.MONDAY);
                }
            });
            datePicker.setEditable(false);
           
            grid.add(datePicker, 1, 1);
           
            CustomText startTimeLabel = new CustomText("Start Time:", 30);
            grid.add(startTimeLabel, 0, 2);
 
            NumberSpinner startTimePicker = new NumberSpinner(LocalDateTime.now().getHour(), LocalDateTime.now().getMinute());
            startTimePicker.setStyle("-fx-font-size: 18px;");
            grid.add(startTimePicker, 1, 2);
           
            CustomText endTimeLabel = new CustomText("End Time:", 30);
            grid.add(endTimeLabel, 0, 3);
 
            NumberSpinner endTimePicker = new NumberSpinner(LocalDateTime.now().getHour(), LocalDateTime.now().getMinute());
            endTimePicker.setStyle("-fx-font-size: 18px;");
            grid.add(endTimePicker, 1, 3);
 
            CustomButton saveButton = new CustomButton("Add Meeting", 20);
           
            saveButton.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                public void handle(ActionEvent event) {
                        for(int i = 0; i < employeeMulti.size();i++) {
                        try {      
                        // Validate the employee details. Any errors will cause an exception.
                            Meeting toAdd = Validation.validateMeeting(datePicker.getValue(), startTimePicker.getText(), endTimePicker.getText(), descTextField.getText(), employeeMulti.get(i).getDiary());
 
                        // Add the Employee
                        employeeMulti.get(i).getDiary().getMeetings().add(toAdd);
 
                        // Display success message.
                        CustomText successText = new CustomText("Meeting successfully added", 16);
                        grid.add(successText, 1, 4);
                        System.out.println(successText);
 
                        // Remove the message after 2 seconds.
                        Timeline timer = new Timeline(
                                new KeyFrame(Duration.seconds(2), new EventHandler<ActionEvent>() {
                                    @Override
                                    public void handle(ActionEvent event) {
                                        grid.getChildren().remove(successText);
                                    }
                                }));
                        timer.play();
 
                        // Clear the text fields for usability sake
                    } catch (MeetingTimeBeforeStart | MeetingTimeSameTime | MeetingTimeStartConflict | GenericFieldEmpty e) {
                        // Display the error to the user.
                        CustomText errorText = new CustomText(e.getMessage(), 16);
                        grid.add(errorText, 1, 4);
                        System.out.println(errorText);
                        Timeline timer = new Timeline(
                                new KeyFrame(Duration.seconds(2), new EventHandler<ActionEvent>() {
                                    @Override
                                    public void handle(ActionEvent event) {
                                        grid.getChildren().remove(errorText);
                                    }
                                }));
                        timer.play();
                       
                        }
                    }
                }
        });
 
            grid.add(saveButton, 0, 4);
 
            setLeft(grid);
 
            CustomButton backButton = new CustomButton("Back", 16);
            backButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    GUIHandler.changePane(new Search(employeeMulti) );
                }
            });
            setMargin(backButton, new Insets(10, 5, 5, 5));
 
            setBottom(backButton);
        }
	}
}