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
	 * This exception is for meetings where the end time and start
	 * time are not on the same day.
	 * @author Daniel
	 *
	 */
	static class MeetingTimeNotSameDay extends Exception {
		/**
		 * Default constructor setting the error message.
		 */
		public MeetingTimeNotSameDay() {
			super("The meeting must take place on the same day.");
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
	
	static class MeetingTimeStartConflict extends Exception {
		/**
		 * Default constructor setting the error message.
		 */
		public MeetingTimeStartConflict() {
			super("The meeting has a conflicting start time.");
		}
	}
	
	static class EmployeeDetailsInvalidID extends Exception {
		/**
		 * Default constructor setting the error message.
		 */
		public EmployeeDetailsInvalidID() {
			super("Invalid Employee ID.");
		}
	}
	
	static class EmployeeExists extends Exception {
		/**
		 * Default constructor setting the error message.
		 */
		public EmployeeExists() {
			super("Employee ID taken.");
		}
	}
	
	static class GenericFieldEmpty extends Exception {
		/**
		 * Default constructor setting the error message.
		 */
		public GenericFieldEmpty(String field) {
			super("You must provide a " + field + ".");
		}
	}
}