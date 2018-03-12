import java.util.Date;

/**
 * Tester class that is used to test various elements throughout 
 * the program.
 * @author Daniel
 *
 */
public class Tester {
	
	/**
	 * Method that tests the meeting time validation function.
	 */
	public void testValidationMeeting() {
		//Test two dates where the end is before the start.
		Date start = new Date(1520946000000l); // 13/3/18 13:00:00
		Date end = new Date(1520942400000l); // 13/03/18 12:00:00
		System.out.println("Testing the start: 13/3/18 13:00:00 and end: 13/03/18 12:00:00");
		
		try {
			if(!Validation.validateMeeting(start,end)) {
				System.out.println("Invalid meeting time!");
			}
			else {
				System.out.println("Valid meeting time!");
			}
		} catch (MeetingManagerExceptions.MeetingTimeBeforeStart | MeetingManagerExceptions.MeetingTimeNotSameDay | MeetingManagerExceptions.MeetingTimeSameTime e) {
			System.out.println(e.getMessage());
		}
		
		System.out.println();
		
		//Test two dates that are on different days.
		start = new Date(1520942400000l); // 13/03/18 12:00:00
		end = new Date(1521032400000l); // 14/03/18 13:00:00
		System.out.println("Testing the start: 13/03/18 12:00:00 and end: 14/03/18 13:00:00");
		
		try {
			if(!Validation.validateMeeting(start,end)) {
				System.out.println("Invalid meeting time!");
			}
			else {
				System.out.println("Valid meeting time!");
			}
		} catch (MeetingManagerExceptions.MeetingTimeBeforeStart | MeetingManagerExceptions.MeetingTimeNotSameDay | MeetingManagerExceptions.MeetingTimeSameTime e) {
			System.out.println(e.getMessage());
		}
		
		System.out.println();
		
		//Test two dates that are the same time
		start = new Date(1521208800000l); // 16/03/18 14:00:00
		end = new Date(1521208800000l); // 16/03/18 14:00:00
		System.out.println("Testing the start: 16/03/18 14:00:00 and end: 16/03/18 14:00:00");
		
		try {
			if(!Validation.validateMeeting(start,end)) {
				System.out.println("Invalid meeting time!");
			}
			else {
				System.out.println("Valid meeting time!");
			}
		} catch (MeetingManagerExceptions.MeetingTimeBeforeStart | MeetingManagerExceptions.MeetingTimeNotSameDay | MeetingManagerExceptions.MeetingTimeSameTime e) {
			System.out.println(e.getMessage());
		}
		
		System.out.println();
		
		//Test two dates that should be valid
		start = new Date(1521208800000l); // 16/03/18 14:00:00
		end = new Date(1521216000000l); // 16/03/18 16:00:00
		System.out.println("Testing the start: 16/03/18 14:00:00 and end: 16/03/18 16:00:00");
		
		try {
			if(!Validation.validateMeeting(start,end)) {
				System.out.println("Invalid meeting time!");
			}
			else {
				System.out.println("Valid meeting time!");
			}
		} catch (MeetingManagerExceptions.MeetingTimeBeforeStart | MeetingManagerExceptions.MeetingTimeNotSameDay | MeetingManagerExceptions.MeetingTimeSameTime e) {
			System.out.println(e.getMessage());
		}
	}
}
