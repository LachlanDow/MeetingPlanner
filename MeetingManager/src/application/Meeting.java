package application;

import java.util.Calendar;
import java.util.Date;

/**
 * Meeting class to store information about meetings.
 * @author Daniel
 *
 */
public class Meeting {

	/**
	 * Start time of the meeting
	 */
	private Date startTime;
	
	/**
	 * End time of the meeting
	 */
	private Date endTime;
	
	/**
	 * Description of the meeting
	 */
	private String description;
	
	/**
	 * Duration of the meeting
	 */
	private long duration;
	
	/**
	 * Start time only of the meeting
	 */
	private String rawStart;
	
	/**
	 * End time only of the meeting
	 */
	private String rawEnd;

	/**
	 * Constructor to set up a meeting
	 * @param startTime Date instance start time
	 * @param endTime Date instance end time
	 * @param description Description of meeting
	 */
	public Meeting(Date startTime, Date endTime, String description) {
		this.startTime = startTime;
		this.endTime = endTime;
		this.description = description;
		this.duration = this.getDuration(startTime, endTime);
		
		Calendar cal = Calendar.getInstance();
		
		cal.setTime(startTime);
		this.rawStart = hourMinuteZero(cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE));
		
		cal.setTime(endTime);
		this.rawEnd = hourMinuteZero(cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE));
		 
	}
	
	/**
	 * Get the meeting details as a string
	 * @return String containing meeting details.
	 */
	public String getMeetingDetails() {
		return "" + startTime + "," + endTime + "," + description;
	}

	/**
	 * Pad hour/minute values
	 * @param hour hour value
	 * @param mnts mins value
	 * @return padded hours/mins
	 * https://stackoverflow.com/a/49133733/3102362
	 */
	public static String hourMinuteZero(int hour,int mnts){
	    String hourZero = (hour >=10)? Integer.toString(hour):String.format("0%s",Integer.toString(hour));
	    String minuteZero = (mnts >=10)? Integer.toString(mnts):String.format("0%s",Integer.toString(mnts));
	    return hourZero+":"+minuteZero;
	}
	
	/**
	 * Method to get the start time of the meeting
	 * 
	 * @return startTime Date instance start time
	 */
	public Date getStartTime() {
		return startTime;
	}

	/**
	 * Method to get the raw start of the meeting
	 * @return String raw start time
	 */
	public String getRawStart() {
		return this.rawStart;
	}
	
	/**
	 * Method to get the raw end of the meeting
	 * @return String raw end time
	 */
	public String getRawEnd() {
		return this.rawEnd;
	}

	/**
	 * Method to set the start time of the meeting
	 * 
	 * @param startTime Date instance start of meeting.
	 */
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	/**
	 * Method to get the end time of the meeting
	 * 
	 * @return Date instance end time of meeting
	 */
	public Date getEndTime() {
		return endTime;
	}

	/**
	 * Method to set the end time of the meeting
	 * 
	 * @param endTime Date instance end of meeting.
	 */
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	/**
	 * Method to get the description of the meeting
	 * 
	 * @return String description of meeting
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Method to set the description of the meeting
	 * 
	 * @param description New description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Method to get the duration of a meeting
	 * 
	 * @param startTime as the start time of the meeting
	 * @param endTime as the end time of the meeting
	 * @return duration as the duration of the meeting
	 */
	public long getDuration(Date startTime, Date endTime) {
		duration = (endTime.getTime() - startTime.getTime());
		return duration;
	}

	/**
	 * Print the meeting
	 */
	public void printMeeting() {
		System.out.println("Start time:" + this.startTime + ". End time:" + this.endTime + ". Has description of: "
				+ this.description);
	}

	/**
	 * Print the meeting as string
	 */
	public String toString() {
		return ("Start time:" + this.startTime + ". End time:" + this.endTime + ". Has description of: "
				+ this.description);
	}
}
