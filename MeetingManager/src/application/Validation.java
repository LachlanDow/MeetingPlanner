package application;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

/**
 * This class is used to validate various things throughout the program.
 * @author Daniel
 *
 */
public class Validation {
	
	/**
	 * Validate a meeting that is to be added to a Diary
	 * @param date Date that the meeting occurs
	 * @param startTime Time that the meeting starts
	 * @param endTime Time that the meeting ends
	 * @param desc Description of meeting
	 * @param diary Diary to add to
	 * @return reference to new meeting
	 * @throws MeetingManagerExceptions.MeetingTimeBeforeStart Thrown if end time comes before start time.
	 * @throws MeetingManagerExceptions.MeetingTimeNotSameDay Thrown if meeting start and end not on the same day
	 * @throws MeetingManagerExceptions.MeetingTimeSameTime Thrown if meeting starts and ends at the same time
	 * @throws MeetingManagerExceptions.MeetingTimeStartConflict Thrown if meeting time conflicts with another meeting.
	 * @throws MeetingManagerExceptions.GenericFieldEmpty Thrown if a field is empty.
	 */
	public static Meeting validateMeeting(LocalDate date, String startTime, String endTime, String desc, Diary diary) throws MeetingManagerExceptions.MeetingTimeBeforeStart, MeetingManagerExceptions.MeetingTimeNotSameDay, MeetingManagerExceptions.MeetingTimeSameTime, MeetingManagerExceptions.MeetingTimeStartConflict, MeetingManagerExceptions.GenericFieldEmpty {
		//Check that all fields were entered.
		if(desc.isEmpty()) {
			throw new MeetingManagerExceptions.GenericFieldEmpty("description");
		}
		else if(date == null) {
			throw new MeetingManagerExceptions.GenericFieldEmpty("date");
		}
		else if(startTime.isEmpty()) {
			throw new MeetingManagerExceptions.GenericFieldEmpty("start time");
		}
		else if(endTime.isEmpty()) {
			throw new MeetingManagerExceptions.GenericFieldEmpty("end time");
		}
		
		//Convert two times to Date objects
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd kk:mm");
		
		Date startDate = new Date();
		Date endDate = new Date();
	
		try {
			startDate = format.parse(date + " " + startTime);
			endDate = format.parse(date + " " + endTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
				
		//Make sure that the start time provided is before the end time.
		if(endDate.before(startDate)){
			throw new MeetingManagerExceptions.MeetingTimeBeforeStart();
		}
		//Make sure they are on the same day
		else if(!sameDay(startDate, endDate)) {
			throw new MeetingManagerExceptions.MeetingTimeNotSameDay();
		}
		//Make sure they aren't the exact same time.
		else if(startDate.equals(endDate)) {
			throw new MeetingManagerExceptions.MeetingTimeSameTime();
		}
		
		Meeting toValidate = new Meeting(startDate, endDate, desc);
		
		//Make sure the meeting slot is available and doesn't intersect with another meeting
		for(int i = 0; i < diary.getMeetings().size(); i++) {
			Meeting next = diary.getMeetings().get(i);
			
			if(toValidate.getStartTime().before(next.getEndTime()) && next.getStartTime().before(toValidate.getEndTime())){
				throw new MeetingManagerExceptions.MeetingTimeStartConflict();
			}
		}
		
		return toValidate;
	}
	
	/**
	 * Determines if two Date objects are on the same day.
	 * @param date1 Date object to compare.
	 * @param date2 Second Date object to compare.
	 * @return true if the dates are on the same day, false otherwise.
	 */
	public static boolean sameDay(Date date1, Date date2) {
		//Credit: https://stackoverflow.com/a/2517824/3102362
		//(Small modification made)
		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		cal1.setTime(date1);
		cal2.setTime(date2);
		return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) && cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
	}
	
	/**
	 * This method is used to validate a number that is given to the program.
	 * @param s String to be parsed.
	 * @return Number (if valid)
	 * @throws NumberFormatException Throws if the number is invalid.
	 */
	public static int validateNumber(String s) throws NumberFormatException {
		int number = 0;
		
		//Try turn the string into an int
		try {
			number = Integer.parseInt(s);
		}
		catch(Exception e){
			throw new NumberFormatException();
		}
		
		//Return the number if it's valid.
		return number;
	}
	
	/**
	 * This method is used to validate employee details gathered from the program.
	 * @param id ID to be valiated.
	 * @param forename Forename to be validated.
	 * @param surname Surname to be validated.
	 * @param jobTitle Job title to be validated.
	 * @return Employee Reference to Employee object if valid.
	 * @throws MeetingManagerExceptions.EmployeeDetailsInvalidID Thrown if the ID is a non-integer
	 * @throws MeetingManagerExceptions.GenericFieldEmpty Thrown if details are left blank.
	 * @throws MeetingManagerExceptions.EmployeeExists Thrown if the employee ID is taken.
	 */
	public static Employee validateEmployeeDetails(String id, String forename, String surname, String jobTitle) throws MeetingManagerExceptions.EmployeeDetailsInvalidID, MeetingManagerExceptions.GenericFieldEmpty, MeetingManagerExceptions.EmployeeExists{
		int valId;
		
		//Make sure the ID is valid.
		try {
			valId = Validation.validateNumber(id);
		}
		catch (NumberFormatException e) {
			throw new MeetingManagerExceptions.EmployeeDetailsInvalidID();
		}
		
		//Check that all fields were entered.
		if(forename.isEmpty()) {
			throw new MeetingManagerExceptions.GenericFieldEmpty("forename");
		}
		else if(surname.isEmpty()) {
			throw new MeetingManagerExceptions.GenericFieldEmpty("surname");
		}
		else if(jobTitle.isEmpty()) {
			throw new MeetingManagerExceptions.GenericFieldEmpty("jobtitle");
		}
		
		//Make sure the ID is not in the current list.
		if(Company.getEmployees().containsKey(valId)) {
			throw new MeetingManagerExceptions.EmployeeExists();
		}
		
		return new Employee(valId, forename, surname, jobTitle);
	}
}