package application;

import java.util.Date;
import java.util.LinkedList;
import java.util.Scanner;

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
	private LinkedList<Task> taskList = new LinkedList<Task>();

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
	
	public int getId() {
		return id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String fn) {
		firstName = fn;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String ln) {
		lastName = ln;
	}

	public String getJobTitle() {
		return jobTitle;
	}

	public void setJobTitle(String job) {
		jobTitle = job;
	}

	public String getFullName() {
		return firstName + " " + lastName;
	}

	public String getEmployeeInformation() {
		return id + "," + firstName + "," + lastName + "," + jobTitle;
	}
	
	public Diary getDiary() {
		return diary;
	}

	public void setDiary(Diary diary) {
		this.diary = diary;
	}
	
	public LinkedList<Task> getTaskList()
	{
		return taskList;
	}
	
	public void setTaskList(LinkedList<Task> taskList)
	{
		this.taskList = taskList;
	}
	
	public LinkedList<Meeting> getMeetings(Date startTime, Date endTime) {
    	LinkedList<Meeting> withinMeetings = new LinkedList<Meeting>();
    	LinkedList<Meeting> allMeetings =  diary.getMeetings();
    	for (int i = 0; i < allMeetings.size();i++) {
    		
    		if(allMeetings.get(i).getStartTime().after(startTime) && allMeetings.get(i).getEndTime().after(endTime)) {
    			withinMeetings.add(allMeetings.get(i));
    		}
    	}
    	return withinMeetings;
    	
    }
	
    public void addMeeting(Meeting meeting) {
    	diary.add(meeting, false);
    }
    
    /**
     * Method to add a task to the list
     * 
     * @param description
     * @param priority
     */
    public void addTask(String description, String priority)
    {
    	Task toAdd = new Task(description, priority);
    	taskList.add(toAdd);
    }
    
    /**
	 * Method to delete a task in the list
	 */
	public void delete(Task toDelete)
	{
		taskList.remove(toDelete);
	}
    
    public void deleteMeeting(Meeting meeting) {
    	diary.delete(meeting, false);
    }
    
	public void editMeeting(Meeting oldMeeting, Meeting newMeeting) {
		diary.edit(oldMeeting, newMeeting, false);
	}
    
    public void undo() {
    	diary.undo();
    }
    
    public String toString() {
		return ("ID:" + this.id + ". ForeName:" + this.firstName + ". Surname: " + this.lastName + " Job title: " + this.jobTitle );
	}
}
