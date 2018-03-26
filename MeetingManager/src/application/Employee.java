package application;

import java.util.Date;
import java.util.LinkedList;

/**
 * Project: Employee Manager This is the employee class, this class will produce
 * the employees information such as the employee's ID and their name and their
 * department which can be added into the menu manager
 * @author Mikaela & Daniel
 *
 */
public class Employee {
	
	private int id;
	private String firstName;
	private String lastName;
	private String jobTitle;
	private Diary diary = new Diary();

	public Employee() {
        this.id = 0;
        this.firstName = "";
        this.lastName = "";
        this.jobTitle = "";
    }
	
	public Employee(int id, String firstName, String lastName, String jobTitle) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.jobTitle = jobTitle;
	}
	
	/**
	 * Method to get the ID of the employee
	 * 
	 * @return id
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Method to get the first name of the employee
	 * 
	 * @return firstName
	 */
	public String getFirstName() {
		return firstName;
	}
	
	/**
	 * Method to set the first name of the employee
	 * 
	 * @param fn
	 */
	public void setFirstName(String fn) {
		firstName = fn;
	}
	
	/**
	 * Method to get the last name of the employee
	 * 
	 * @return lastName
	 */
	public String getLastName() {
		return lastName;
	}
	
	/**
	 * Method to set the last name of the employee
	 * 
	 * @param ln
	 */
	public void setLastName(String ln) {
		lastName = ln;
	}
	
	/**
	 * Method to get the job title of the employee
	 * 
	 * @return jobTitle
	 */
	public String getJobTitle() {
		return jobTitle;
	}
	
	/**
	 * Method to set the job title of the employee
	 * 
	 * @param job
	 */
	public void setJobTitle(String job) {
		jobTitle = job;
	}

	/**
	 * Method to get the full name of the employee
	 * 
	 * @return firstName, lastName
	 */
	public String getFullName() {
		return firstName + " " + lastName;
	}

	/**
	 * Method to get all employee information
	 * 
	 * @return id, firstName, lastName, jobTitle
	 */
	public String getEmployeeInformation() {
		return id + "," + firstName + "," + lastName + "," + jobTitle;
	}
	
	/**
	 * Method to get the diary
	 * 
	 * @return diary
	 */
	public Diary getDiary() {
		return diary;
	}

	/**
	 * Method to set the diary
	 * 
	 * @param diary
	 */
	public void setDiary(Diary diary) {
		this.diary = diary;
	}
	
	/**
	 * Method to get the task list
	 * 
	 * @return taskList
	 */
	public LinkedList<Task> getTaskList()
	{
		return diary.getTaskList();
	}
	
	/**
	 * Method to get the list of meetings
	 * 
	 * @param startTime
	 * @param endTime
	 * @return withinMeetings
	 */
	public LinkedList<Meeting> getMeetings(Date startTime, Date endTime) {
    	LinkedList<Meeting> withinMeetings = new LinkedList<Meeting>();
    	LinkedList<Meeting> allMeetings =  diary.getMeetings();
    	for (int i = 0; i < allMeetings.size();i++) {
    		
    		if(allMeetings.get(i).getStartTime().after(startTime) && allMeetings.get(i).getStartTime().before(endTime)) {
    			withinMeetings.add(allMeetings.get(i));
    		}
    	}
    	return withinMeetings;
    	
    }
	public void add(Meeting meeting) {
		diary.add(meeting);
	}
	
	/**
	 * Method to add a meeting to the list
	 * 
	 * @param meeting
	 */
    public void addMeeting(Meeting meeting) {
    	diary.addMeeting(meeting, false);

    }
    
    /**
     * Method to add a task to the list
     * 
     * @param toAdd
     */
    public void addTask(Task toAdd)
    {
    	diary.addTask(toAdd);
    }
    
    /**
	 * Method to delete a task in the list
	 * 
	 * @param toDelete
	 */
	public void deleteTask(Task toDelete)
	{
		diary.deleteTask(toDelete);
	}
    
	/**
	 * Method to delete a meeting from the list
	 * 
	 * @param meeting
	 */
    public void deleteMeeting(Meeting meeting) {
    	diary.deleteMeeting(meeting, false);
    }
    
    /**
     * Method to edit a meeting in the list
     * 
     * @param oldMeeting
     * @param newMeeting
     */
	public void editMeeting(Meeting oldMeeting, Meeting newMeeting) {
		diary.editMeeting(oldMeeting, newMeeting, false);
	}
    
	/**
	 * Method to edit a task in the list
	 * 
	 * @param oldTask
	 * @param newTask
	 */
	public void editTask(Task oldTask, Task newTask)
	{
		diary.deleteTask(oldTask);
		diary.addTask(newTask);
	}
	
	/**
	 * Method to undo an action
	 */
    public void undo() {
    	diary.undo();
    }
    
    /**
     * Method to return all information
     * 
     * @return id, firstName, lastName, jobTitle
     */
    public String toString() {
		return ("ID:" + this.id + ". ForeName:" + this.firstName + ". Surname: " + this.lastName + " Job title: " + this.jobTitle );
	}
}
