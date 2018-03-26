package application;

public class Task {
	
	private String description;
	private String priority;
	
	public Task(String description, String priority)
	{
		this.description = description;
		this.priority = priority;
	}
	

	/**
	 * Method to get the description of a task
	 * 
	 * @return description
	 */
	public String getDescription()
	{
		return description;
	}
	
	/**
	 * Method to set the description of a task
	 * 
	 * @param description as the description to be set
	 */
	public void setDescription(String description)
	{
		this.description = description;
	}
	
	/**
	 * Method to get the priority of a task
	 * 
	 * @return priority
	 */
	public String getPriority()
	{
		return priority;
	}
	
	/**
	 * Method to set the priority of a task
	 * 
	 * @param priority as the level of pripority the task has
	 */
	public void setPriority(String priority)
	{
		this.priority = priority;
	}
	
	/**
	 * Returns the task details of the task
	 * @return the details of the task 
	 */
	public String getTaskDetails()
	{
		return this.description + "," + this.priority;
	}
	

	/**
	 * Print the task as string
	 */
	public String toString() {
		return ("Description: " + this.description + " Priority: " + this.priority);
	}
}
