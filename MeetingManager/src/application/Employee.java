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
		return "Employee " + id + " Job " + jobTitle + " " + getFullName();
	}
	
	public Diary getDiary() {
		return diary;
	}

	public void setDiary(Diary diary) {
		this.diary = diary;
	}
	
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
	
    public void addMeeting(Date startTime, Date endTime, String description) {
    	diary.addMeeting(startTime, endTime, description);
    }
    
    public void displayDiary() {
    	
    }
    
    public String toString() {
		return ("ID:" + this.id + ". ForeName:" + this.firstName + ". Surname: " + this.lastName + " Job title: " + this.jobTitle );
	}
}
