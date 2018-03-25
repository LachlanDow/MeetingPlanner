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
import application.MeetingManagerExceptions.GenericFieldEmpty;
import application.MeetingManagerExceptions.EmployeeDetailsInvalidID;
import application.MeetingManagerExceptions.EmployeeExists;
import application.MeetingManagerExceptions.MeetingTimeBeforeStart;
import application.MeetingManagerExceptions.MeetingTimeNotSameDay;
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
			//
			CustomText title = new CustomText("Meeting Manager", 72);

			setMargin(title, new Insets(30, 0, 0, 0));
			setTop(title);
			setAlignment(getTop(), Pos.CENTER);

			VBox options = new VBox(10);
			options.setAlignment(Pos.CENTER);

			//
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

			options.getChildren().addAll(employeesButton, diaryButton, companyMeetingButton);

			setCenter(options);

		}
	}

	public static class ManageEmployees extends BorderPane {
		public ManageEmployees() {
			// Add the
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

			setCenter(table);

			// TEST DATA
			// TODO: LOL
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

	public static class EditEmployee extends BorderPane {
		public EditEmployee(Employee employee) {
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
					if (!employee.getFirstName().equals(firstNameTextField.getText())) {
						employee.setFirstName(firstNameTextField.getText());
					}

					if (!employee.getLastName().equals(lastNameTextField.getText())) {
						employee.setLastName(lastNameTextField.getText());
					}

					if (!employee.getJobTitle().equals(jobTextField.getText())) {
						employee.setJobTitle(jobTextField.getText());
					}

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
				@Override
				public void handle(ActionEvent event) {
					// TODO: Delete
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

	public static class ManageDiaries extends BorderPane {
		public ManageDiaries() {
			// Add the
			VBox topPane = new VBox();

			CustomText title = new CustomText("Meeting Manager", 64);
			topPane.getChildren().add(title);

			CustomText subtitle = new CustomText("Click an Employee to view their diary.", 20);
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

			// TEST DATA
			// TODO: Use real data
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

	public static class AddEmployee extends BorderPane {
		public AddEmployee() {
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
			String nextID = String.valueOf(Company.getEmployees().lastEntry().getValue().getId() + 1);
			idTextField.setText(nextID);

			// Add to the Pane.
			grid.add(idTextField, 1, 0);

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

	public static class EmployeeDiary extends BorderPane {

		Employee employee;

		public EmployeeDiary(Employee employee) {
			this.employee = employee;

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

			// Set up the months in a seperate arrayList (allows me to use currentMonthNo to
			// get the month name)
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

			// Caldendar grid
			GridPane calendar = new GridPane();

			// Year and months combo boxes
			ComboBox<Integer> yearComboBox = new ComboBox<Integer>();
			ComboBox<String> monthsComboBox = new ComboBox<String>();

			// Month, seperator and year labels
			CustomText monthLabel = new CustomText(monthsList.get(currMonthNo), 20);
			monthYearControls.add(monthLabel, 0, 0);

			CustomText seperator = new CustomText("-", 20);
			monthYearControls.add(seperator, 1, 0);

			CustomText yearLabel = new CustomText(String.valueOf(currYear), 20);
			monthYearControls.add(yearLabel, 2, 0);

			monthLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent arg0) {
					SimpleDateFormat format = new SimpleDateFormat("MMMM yyyy");

					monthsComboBox.getItems().clear();

					for (String month : monthsList) {
						try {
							Date date = format.parse(month + " " + yearLabel.getText());
							if (date.after(new Date()) || month.equals(monthLabel.getText())) {
								monthsComboBox.getItems().add(month);
							}
						} catch (ParseException e) {
							e.printStackTrace();
						}
					}

					monthsComboBox.getSelectionModel().select(0);

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

			// Set up months

			// Set up years.
			yearComboBox.getItems().add(currYear);
			yearComboBox.getSelectionModel().select(0);

			// TODO: Discuss future meeting max time (for now 3 years)
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

			bottomBox.setRight(addButton);

			setMargin(bottomBox, new Insets(10, 5, 5, 5));
			setBottom(bottomBox);
		}

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
					HBox calSquare = new HBox();
					Label day = new Label();

					if (i == 0 && (j + 1) < dayOfWeekMonthStart || counter > lastDayOfMonth) {
						calSquare.setStyle("-fx-background-color: black; -fx-border-color: black;");
					} else {
						day.setText(ordinal(counter));
						calSquare.getChildren().add(day);

						// Add meeting text
						// employee.getDiary().getMeetingsOnDay();

						counter++;
						calSquare.setStyle("-fx-background-color: white; -fx-border-color: black;");
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

	public static class AddMeeting extends BorderPane {
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
					try {				
						// Validate the employee details. Any errors will cause an exception.
						Meeting toAdd = Validation.validateMeeting(datePicker.getValue(), startTimePicker.getText(), endTimePicker.getText(), descTextField.getText(), employee.getDiary());

						// Add the Employee
						employee.getDiary().getMeetings().add(toAdd);

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
					} catch (MeetingTimeBeforeStart | MeetingTimeNotSameDay | MeetingTimeSameTime | MeetingTimeStartConflict | GenericFieldEmpty e) {
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
	public static class CompanyMeeting extends BorderPane{
		public CompanyMeeting() {
			LinkedList<Employee> employeeSearch = new LinkedList<Employee>();
			VBox topPane = new VBox();

			CustomText title = new CustomText("Meeting Manager", 64);
			topPane.getChildren().add(title);

			CustomText subtitle = new CustomText("Click an Employee to choose to search through their details.", 20);
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

			table.getColumns().addAll(idCol,firstNameCol,lastNameCol,jobTitleCol);

			// Table column widths (odd value so that it offsets the margin)s
			idCol.prefWidthProperty().bind(table.widthProperty().divide(4.02));
			firstNameCol.prefWidthProperty().bind(table.widthProperty().divide(4.02));
			lastNameCol.prefWidthProperty().bind(table.widthProperty().divide(4.02));
			jobTitleCol.prefWidthProperty().bind(table.widthProperty().divide(4.02));

			setMargin(table, new Insets(10, 5, 5, 5));

			setCenter(table);
			// Add some space between the subtitle and the employee name.
			Region spacer = new Region();
			spacer.setMinHeight(10);
			topPane.getChildren().add(spacer);

			// Add employee name to the Pane.
			CustomText employeeName = new CustomText("Employee: " + employee.getFullName(), 20);
			topPane.getChildren().add(employeeName);

			BorderPane bottomBox = new BorderPane();

			CustomButton backButton = new CustomButton("Back", 16);
			backButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					GUIHandler.changePane(new MainMenu());
				}
			});
			bottomBox.setLeft(backButton);

			CustomButton searchButton = new CustomButton("Search for free times", 16);
			searchButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
			//		GUIHandler.changePane(new search());
				}
			});

			bottomBox.setRight(searchButton);

			setMargin(bottomBox, new Insets(10, 5, 5, 5));
			setBottom(bottomBox);
				
		}
	}
	public static class Search extends BorderPane{
		public Search(LinkedList<Employee> employee) {
			
			LinkedList<Meeting> returnedSearch = new LinkedList<Meeting>();
			VBox topPane = new VBox();

			CustomText title = new CustomText("Meeting Manager", 64);
			topPane.getChildren().add(title);

			CustomText subtitle = new CustomText("Employee Search Results", 20);
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

			TableColumn<Meeting, String> timeStart = new TableColumn<Meeting, String>("Time Start");
			timeStart.setCellValueFactory(new PropertyValueFactory<Meeting, String>("timeStart"));

			TableColumn<Meeting, String> timeFinish = new TableColumn<Meeting, String>("Time Finish");
			timeFinish.setCellValueFactory(new PropertyValueFactory<Meeting, String>("timeFinish"));

			
			table.getColumns().addAll(timeStart, timeFinish);


			// Table column widths (odd value so that it offsets the margin)s
			timeStart.prefWidthProperty().bind(table.widthProperty().divide(4.02));
			timeFinish.prefWidthProperty().bind(table.widthProperty().divide(4.02));

			setMargin(table, new Insets(10, 5, 5, 5));

			setCenter(table);

			// TEST DATA
			// TODO: LOL
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
						employeeSearch.add(clickedRow);
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

			CustomButton searchButton = new CustomButton("Search for free times", 16);
			searchButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					GUIHandler.changePane(new search());
				}
			});

			bottomBox.setRight(searchButton);

			setMargin(bottomBox, new Insets(10, 5, 5, 5));
			setBottom(bottomBox);
				
		}
	}
}