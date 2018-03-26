package application;

/**
 * This is a wrapper class for all the different custom exceptions
 * that will be used throughout the program.
 * @author Daniel
 *
 */
public class MeetingManagerExceptions {
	
	/**
	 * This exception is for meetings where the end time is before
	 * the start time.
	 * @author Daniel
	 *
	 */
	static class MeetingTimeBeforeStart extends Exception {
		/**
		 * Default constructor setting the error message.
		 */
		public MeetingTimeBeforeStart() {
			super("The meeting start time must be before the end time.");
		}
	}
	
	/**
	 * This exception is for meeting where the end time and start
	 * time are the same
	 * @author Daniel
	 *
	 */
	static class MeetingTimeSameTime extends Exception {
		/**
		 * Default constructor setting the error message.
		 */
		public MeetingTimeSameTime() {
			super("The meeting cannot start and end at the same time.");
		}
	}
	
	/**
	 * This exception is for meeting that conflict times.
	 * @author Daniel
	 *
	 */
	static class MeetingTimeStartConflict extends Exception {
		/**
		 * Default constructor setting the error message.
		 */
		public MeetingTimeStartConflict() {
			super("The meeting has a conflicting start time.");
		}
	}
	
	/**
	 * This exception is for when a provided employee ID
	 * is invalid
	 * @author Daniel
	 *
	 */
	static class EmployeeDetailsInvalidID extends Exception {
		/**
		 * Default constructor setting the error message.
		 */
		public EmployeeDetailsInvalidID() {
			super("Invalid Employee ID.");
		}
	}
	
	/**
	 * This exception is for when an employee ID
	 * is taken
	 * @author Daniel
	 *
	 */
	static class EmployeeExists extends Exception {
		/**
		 * Default constructor setting the error message.
		 */
		public EmployeeExists() {
			super("Employee ID taken.");
		}
	}
	
	/**
	 * This exception is used when a field is empty.
	 * @author Daniel
	 *
	 */
	static class GenericFieldEmpty extends Exception {
		/**
		 * Default constructor setting the error message.
		 * 
		 * @param field String field name that was empty
		 */
		public GenericFieldEmpty(String field) {
			super("You must provide a " + field + ".");
		}
	}
}