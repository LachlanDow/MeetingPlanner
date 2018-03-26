package application;

import java.util.Calendar;
import java.util.Date;

public class Meeting {

	private Date startTime;
	private Date endTime;
	private String description;
	
	private long duration;
	private String rawStart;
	private String rawEnd;

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
	
	public String getMeetingDetails() {
		return "" + startTime + "," + endTime + "," + description;
	}

	/**
	 * 
	 * @param hour
	 * @param mnts
	 * @return
	 * @credit https://stackoverflow.com/a/49133733/3102362
	 */
	public static String hourMinuteZero(int hour,int mnts){
	    String hourZero = (hour >=10)? Integer.toString(hour):String.format("0%s",Integer.toString(hour));
	    String minuteZero = (mnts >=10)? Integer.toString(mnts):String.format("0%s",Integer.toString(mnts));
	    return hourZero+":"+minuteZero;
	}
	
	/**
	 * Method to get the start time of the meeting
	 * 
	 * @return startTime
	 */
	public Date getStartTime() {
		return startTime;
	}

	public String getRawStart() {
		return this.rawStart;
	}
	
	public String getRawEnd() {
		return this.rawEnd;
	}

	/**
	 * Method to set the start time of the meeting
	 * 
	 * @param startTime
	 */
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	/**
	 * Method to get the end time of the meeting
	 * 
	 * @return
	 */
	public Date getEndTime() {
		return endTime;
	}

	/**
	 * Method to set the end time of the meeting
	 * 
	 * @param endTime
	 */
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	/**
	 * Method to get the description of the meeting
	 * 
	 * @return
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Method to set the description of the meeting
	 * 
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Method to get the duration of a meeting
	 * 
	 * @param startTime
	 * @param endTime
	 * @return duration
	 */
	public long getDuration(Date startTime, Date endTime) {
		duration = (endTime.getTime() - startTime.getTime());
		return duration;
	}

	public void printMeeting() {
		System.out.println("Start time:" + this.startTime + ". End time:" + this.endTime + ". Has description of: "
				+ this.description);
	}

	public String toString() {
		return ("Start time:" + this.startTime + ". End time:" + this.endTime + ". Has description of: "
				+ this.description);
	}
}
