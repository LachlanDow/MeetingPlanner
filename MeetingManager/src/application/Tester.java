package application;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.Map.Entry;

import application.MeetingManagerExceptions.EmployeeDetailsInvalidID;
import application.MeetingManagerExceptions.EmployeeExists;
import application.MeetingManagerExceptions.GenericFieldEmpty;
import application.MeetingManagerExceptions.MeetingTimeBeforeStart;
import application.MeetingManagerExceptions.MeetingTimeSameTime;
import application.MeetingManagerExceptions.MeetingTimeStartConflict;

/**
 * Tester class that is used to test various elements throughout 
 * the program.
 * @author Daniel
 *
 */
public class Tester {
	/**
	 * test the validation of the employee
	 */
	public void testValidationEmployee() {
		//Test Employee with invalid ID
		System.out.println("Testing adding employee with invalid ID");
		
		try {
			Validation.validateEmployeeDetails("forty-five", "John", "Smith", "Worker");
			System.out.println("Employee added!");
		} catch (EmployeeDetailsInvalidID | GenericFieldEmpty | EmployeeExists e) {
			System.out.println(e.getMessage());
		}
		System.out.println();
		
		//Test Employee with empty field
		System.out.println("Testing adding employee with empty field");
		
		try {
			Validation.validateEmployeeDetails("10", "John", "", "Worker");
			System.out.println("Employee added!");
		} catch (EmployeeDetailsInvalidID | GenericFieldEmpty | EmployeeExists e) {
			System.out.println(e.getMessage());
		}
		System.out.println();
		
		//Test Employee with duplicate ID
		System.out.println("Adding employee with ID 5");
		Company.addEmployee(5, "Paul", "Stan", "Worker");
		System.out.println("Success");
		
		System.out.println("Adding another employee with ID 5");
		try {
			Validation.validateEmployeeDetails("5", "John", "Doe", "Worker");
			System.out.println("Employee added!");
		} catch (EmployeeDetailsInvalidID | GenericFieldEmpty | EmployeeExists e) {
			System.out.println(e.getMessage());
		}
		System.out.println();
		
		//Add valid employee
		System.out.println("Adding a valid employee");
		try {
			Validation.validateEmployeeDetails("56", "John", "Doe", "Worker");
			System.out.println("Employee added!");
		} catch (EmployeeDetailsInvalidID | GenericFieldEmpty | EmployeeExists e) {
			System.out.println(e.getMessage());
		}
		System.out.println();
		
	}
	/**
	 * Method that tests the meeting time validation function.
	 */
	public void testValidationMeeting() {
		//Test two dates where the end is before the start.
		System.out.println("Testing the start: 13/3/18 13:00:00 and end: 13/03/18 12:00:00");
		
		try {
			Validation.validateMeeting(LocalDate.of(2018, 3, 13), "14:00", "13:00", "Test description", new Diary());
			System.out.println("Valid meeting");
		} catch (MeetingManagerExceptions.MeetingTimeBeforeStart | MeetingManagerExceptions.MeetingTimeSameTime | MeetingTimeStartConflict | GenericFieldEmpty e) {
			System.out.println(e.getMessage());
		}
		System.out.println();
		
		//Test two times that are the same
		System.out.println("Testing the start: 15/3/18 13:00:00 and end: 15/03/18 13:00:00");
		
		try {
			Validation.validateMeeting(LocalDate.of(2018, 3, 15), "13:00", "13:00", "Test description", new Diary());
			System.out.println("Valid meeting");
		} catch (MeetingManagerExceptions.MeetingTimeBeforeStart | MeetingManagerExceptions.MeetingTimeSameTime | MeetingTimeStartConflict | GenericFieldEmpty e) {
			System.out.println(e.getMessage());
		}
		System.out.println();
		
		//Test empty field
		System.out.println("Testing the start: 13/3/18 13:00:00 and end: ");
		
		try {
			Validation.validateMeeting(LocalDate.of(2018, 3, 13), "12:00", "", "Test description", new Diary());
			System.out.println("Valid meeting");
		} catch (MeetingManagerExceptions.MeetingTimeBeforeStart | MeetingManagerExceptions.MeetingTimeSameTime | MeetingTimeStartConflict | GenericFieldEmpty e) {
			System.out.println(e.getMessage());
		}
		System.out.println();
		
		//Test conflicting meetings
		Diary testDiary = new Diary();
		
		System.out.println("Adding meeting to Diary: 13/3/18 13:00 - 15:00");
		Meeting meeting = null;
		try {
			meeting = Validation.validateMeeting(LocalDate.of(2018, 3, 13), "13:00", "15:00", "Test description", testDiary);
		} catch (MeetingTimeBeforeStart | MeetingTimeSameTime | MeetingTimeStartConflict | GenericFieldEmpty e) {
			System.out.println(e.getMessage());
		}
		testDiary.addMeeting(meeting, true);
		
		//Adding conflicting meeting
		System.out.println("Adding conflicting meeting to Diary: 13/3/18 12:00 - 14:00");
		try {
			Validation.validateMeeting(LocalDate.of(2018, 3, 13), "12:00", "14:00", "Test description", testDiary);
			System.out.println("Valid meeting");
		} catch (MeetingManagerExceptions.MeetingTimeBeforeStart | MeetingManagerExceptions.MeetingTimeSameTime | MeetingTimeStartConflict | GenericFieldEmpty e) {
			System.out.println(e.getMessage());
		}
		System.out.println();
		
		//Test two dates that should be valid
		System.out.println("Testing the start: 20/3/18 13:00:00 and end: 20/03/18 14:00:00");
				
		try {
			Validation.validateMeeting(LocalDate.of(2018, 3, 20), "13:00", "14:00", "Test description", new Diary());
			System.out.println("Valid meeting");
		} catch (MeetingManagerExceptions.MeetingTimeBeforeStart | MeetingManagerExceptions.MeetingTimeSameTime | MeetingTimeStartConflict | GenericFieldEmpty e) {
			System.out.println(e.getMessage());
		}
		System.out.println();
	}
	/**
	 * test the edit employee methods
	 */
	public void testEditEmployee() {
		System.out.println("Adding employee");
		Employee e = Company.addEmployee(20, "Daniel", "Test", "Worker");
		
		System.out.println("Printing employees");
		Company.printEmployees();
		
		System.out.println("Editing employee name to John");
		Company.editEmployee(e, "John");
		
		System.out.println("Printing employees");
		Company.printEmployees();
	}
	
	public void testDeleteEmployee() {
		System.out.println("Adding employee");
		Employee e = Company.addEmployee(20, "Daniel", "Test", "Worker");
		
		System.out.println("Printing employees");
		Company.printEmployees();
		
		System.out.println("Deleting employee");
		Company.deleteEmployee(e.getId());
		
		System.out.println("Printing employees");
		Company.printEmployees();
	}
	/**
	 * test the edit meeting methods 
	 */
	public void testEditMeeting() {
		Diary testDiary = new Diary();
		
		System.out.println("Adding meeting");
		Meeting toAdd = null;
		try {
			toAdd = Validation.validateMeeting(LocalDate.of(2018, 3, 20), "13:00", "14:00", "Test description", testDiary);
		} catch (MeetingTimeBeforeStart | MeetingTimeSameTime | MeetingTimeStartConflict | GenericFieldEmpty e) {
			e.printStackTrace();
		}
		
		testDiary.addMeeting(toAdd, true);
		
		System.out.println("Printing meetings");
		testDiary.printMeetings();
		
		Meeting toChange = null;
		try {
			toChange = Validation.validateMeeting(LocalDate.of(2018, 3, 28), "13:00", "14:00", "Test description", testDiary);
		} catch (MeetingTimeBeforeStart | MeetingTimeSameTime | MeetingTimeStartConflict | GenericFieldEmpty e) {
			e.printStackTrace();
		}
		System.out.println("Editing meeting time to: 28/03/2018");
		testDiary.editMeeting(toAdd, toChange, true);
		
		System.out.println("Printing meetings");
		testDiary.printMeetings();
	}
	/**
	 * test the delete methods
	 */
	
	public void testDeleteMeeting() {
		Diary testDiary = new Diary();
		
		System.out.println("Adding meeting");
		Meeting toAdd = null;
		try {
			toAdd = Validation.validateMeeting(LocalDate.of(2018, 3, 20), "13:00", "14:00", "Test description", testDiary);
		} catch (MeetingTimeBeforeStart | MeetingTimeSameTime | MeetingTimeStartConflict | GenericFieldEmpty e) {
			e.printStackTrace();
		}
		
		testDiary.addMeeting(toAdd, true);
		
		System.out.println("Printing meetings");
		testDiary.printMeetings();
		
		System.out.println("Deleting meeting");
		testDiary.deleteMeeting(toAdd, true);
		
		System.out.println("Printing meetings");
		testDiary.printMeetings();
	}
	/**
	 * test the add task method
	 */
	public void testAddTask() {
		Diary testDiary = new Diary();
		
		System.out.println("Adding Task with blank priority");
		
		Task toAdd = null;
		try {
			toAdd = Validation.validateTask("Foobar", "");
			testDiary.addTask(toAdd);
		} catch (GenericFieldEmpty e) {
			System.out.println(e.getMessage());
		}

		
		System.out.println("Adding valid Task");
		try {
			toAdd = Validation.validateTask("Printing", "High");
		} catch (GenericFieldEmpty e) {
			System.out.println(e.getMessage());
		}
		testDiary.addTask(toAdd);
		
		System.out.println("Printing tasks");
		testDiary.printTasks();
	}
	
	/**
	 * test the edit task method
	 */
	public void testEditTask() {
		Diary testDiary = new Diary();

		System.out.println("Adding valid Task");
		
		Task toAdd = null;
		try {
			toAdd = Validation.validateTask("Printing", "High");
		} catch (GenericFieldEmpty e) {
			System.out.println(e.getMessage());
		}
		testDiary.addTask(toAdd);
		
		System.out.println("Printing tasks:");
		testDiary.printTasks();
		
		System.out.println("Editing task description to running");
		Task toChange = new Task("Running", toAdd.getPriority());
		testDiary.editTask(toAdd, toChange);
		
		System.out.println("Printing tasks:");
		testDiary.printTasks();
	}
	/**
	 * test the delete task method
	 */
	public void testDeleteTask() {
		Diary testDiary = new Diary();

		System.out.println("Adding valid Task");
		
		Task toAdd = null;
		try {
			toAdd = Validation.validateTask("Printing", "High");
		} catch (GenericFieldEmpty e) {
			System.out.println(e.getMessage());
		}
		testDiary.addTask(toAdd);
		
		System.out.println("Printing tasks:");
		testDiary.printTasks();
		
		System.out.println("Deleting task");
		testDiary.deleteTask(toAdd);
		
		System.out.println("Printing tasks");
		testDiary.printTasks();
	}
	
	/**
	 * method to test the search functions of the employees
	 */
	public void testSearch() {
		
		System.out.println("Adding employee");
		 Company.addEmployee(20, "Daniel", "Test1", "Worker");
		 Company.addEmployee(21, "James", "Test2", "Worker");
		 Company.addEmployee(22, "Eric", "Test3", "Worker");
		 
		 Meeting meeting = null;
			try {
				meeting = Validation.validateMeeting(LocalDate.of(2018, 3, 13), "13:00", "15:00", "Test description", Company.getEmployees().get(20).getDiary());
			} catch (MeetingTimeBeforeStart | MeetingTimeSameTime | MeetingTimeStartConflict | GenericFieldEmpty e) {
				System.out.println(e.getMessage());
			}
		 Company.getEmployees().get(20).getDiary().addMeeting(meeting,true);
		 
		 try {
				meeting = Validation.validateMeeting(LocalDate.of(2018, 3, 13), "15:00", "16:00", "Test description", Company.getEmployees().get(20).getDiary());
			} catch (MeetingTimeBeforeStart | MeetingTimeSameTime | MeetingTimeStartConflict | GenericFieldEmpty e) {
				System.out.println(e.getMessage());
			}
		 Company.getEmployees().get(21).getDiary().addMeeting(meeting,true);
		
		 try {
				meeting = Validation.validateMeeting(LocalDate.of(2018, 3, 13), "15:00", "16:00", "Test description", Company.getEmployees().get(20).getDiary());
			} catch (MeetingTimeBeforeStart | MeetingTimeSameTime | MeetingTimeStartConflict | GenericFieldEmpty e) {
				System.out.println(e.getMessage());
			}
		 
		 Company.getEmployees().get(22).getDiary().addMeeting(meeting,true);
		 SimpleDateFormat format = new SimpleDateFormat("yyy-MM-dd kk:mm");
		 	
		 Date startDate = new Date();
		Date endDate = new Date();
		
		

			try {
				startDate = format.parse("2018-3-13"  + " " + "09:00");
				endDate = format.parse("2018-3-13" + " " + "20:00");

			} catch (ParseException e) {
				e.printStackTrace();
			}
			LinkedList<Employee> employees = new LinkedList<Employee>();
			for (Entry<Integer, Employee> entry : Company.getEmployees().entrySet()) {
				employees.add(entry.getValue());
			}

			
			System.out.println(Company.search(employees, startDate, endDate));
			
			
	}
}