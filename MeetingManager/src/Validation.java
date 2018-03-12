import java.util.Calendar;
import java.util.Date;

/**
 * This class is used to validate various things throughout the program.
 * @author Daniel
 *
 */
public class Validation {
	/**
	 * Determines if the list of given employees is valid.
	 * @param ids String array of employee IDs.
	 * @return True if all employees exist, false otherwise.
	 */
	public static boolean validateEmployees(String[] ids) {
		return false;
		//TODO: Make this work
	}
	
	/**
	 * Determines if a meeting start time and end time is valid.
	 * @param startTime Time of the start of the meeting in the form of a Date object.
	 * @param endTime Time of the end of the meeting in the form of a Date object.
	 * @return True if meeting is vaid, false otherwise.
	 * @throws MeetingManagerExceptions.MeetingTimeBeforeStart The meeting end time is before the start time.
	 * @throws MeetingManagerExceptions.MeetingTimeNotSameDay The meeting is not on the same day.
	 * @throws MeetingManagerExceptions.MeetingTimeSame The meeting starts and ends at the same time.
	 */
	public static boolean validateMeeting(Date startTime, Date endTime) throws MeetingManagerExceptions.MeetingTimeBeforeStart, MeetingManagerExceptions.MeetingTimeNotSameDay, MeetingManagerExceptions.MeetingTimeSameTime {
		//Make sure that the start time provided is before the end time.
		if(endTime.before(startTime)){
			throw new MeetingManagerExceptions.MeetingTimeBeforeStart();
		}
		//Make sure they are on the same day
		else if(!sameDay(startTime, endTime)) {
			throw new MeetingManagerExceptions.MeetingTimeNotSameDay();
		}
		//Make sure they aren't the exact same time.
		else if(startTime.equals(endTime)) {
			throw new MeetingManagerExceptions.MeetingTimeSameTime();
		}
		return true;
	}
	
	/**
	 * Determines if two Date objects are on the same day.
	 * @param date1 Date object to compare.
	 * @param date2 Second Date object to compare.
	 * @return
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
}
