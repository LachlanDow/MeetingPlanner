import java.util.Date;

public class Meeting {

  private Date startTime;
	private Date endTime;
	private String description;
	private long duration;
	
	
	/**
	 * Method to get the start time of the meeting
	 * 
	 * @return startTime
	 */
	public Date getStartTime()
	{
		return startTime;
	}
	
	/**
	 * Method to set the start time of the meeting
	 * 
	 * @param startTime
	 */
	public void setStartTime(Date startTime)
	{
		this.startTime = startTime;
	}
	
	/**
	 * Method to get the end time of the meeting
	 * 
	 * @return
	 */
	public Date getEndTime()
	{
		return endTime;
	}
	
	/**
	 * Method to set the end time of the meeting
	 * 
	 * @param endTime
	 */
	public void setEndTime(Date endTime)
	{
		this.endTime = endTime;
	}
	
	/**
	 * Method to get the description of the meeting
	 * 
	 * @return
	 */
	public String getDescription()
	{
		return description;
	}
	
	/**
	 * Method to set the description of the meeting
	 * 
	 * @param description
	 */
	public void setDescription(String description)
	{
		this.description = description;
	}
	
	/**
	 * Method to get the duration of a meeting
	 * 
	 * @param startTime
	 * @param endTime
	 * @return duration
	 */
	public long getDuration(Date startTime, Date endTime)
	{
		duration = (endTime.getTime() - startTime.getTime());
		return duration;
	}
  
}
