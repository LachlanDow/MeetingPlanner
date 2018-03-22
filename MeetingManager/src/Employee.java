import java.util.Date;
import java.util.LinkedList;

public class Employee implements Comparable<Employee>{
    private int id;
    private String forename;
    private String surname;
    private String jobTitle;
    private Diary diary = new Diary();
   
    public Employee(int id, String forename, String surname, String jobTitle) {
        this.id = id;
        this.forename = forename;
        this.surname = surname;
        this.jobTitle = jobTitle;
    }
    
    public Employee() {
        this.id = 0;
        this.forename = "";
        this.surname = "";
        this.jobTitle = "";
    }
   
    @Override
    public int compareTo(Employee o) {
        return Integer.compare(this.id, o.id);
    }
   
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getForename() {
        return forename;
    }
    public void setForename(String forename) {
        this.forename = forename;
    }
    public String getSurname() {
        return surname;
    }
    public void setSurname(String surname) {
        this.surname = surname;
    }
    public String getFullName() {
        return forename + " " + surname;
    }
    public String getJobTitle() {
        return jobTitle;
    }
    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }
 
    public Diary getDiary() {
        return diary;
    }
 
    public void setDiary(Diary diary) {
        this.diary = diary;
    }
   // public LinkedList getMeetings(Date startTime, Date endTime) {
    	
    	
   // }
    public void addMeeting(Date startTime, Date endTime, String description)
    {
    	diary.add(startTime, endTime, description);
    }
    public void displayDiary() {
    	
    }
    
}