import java.util.Date;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Iterator;


public class Company {
	/**
	 *A binary tree of all the employees in the company
	 */
	private TreeMap<Integer,Employee> employees = new TreeMap<Integer,Employee>();
	
	/**
	 * this search  method will accept a string of employee id's and find the times where non of the members of staff are in meetings
	 * @param ids as the array of the employees ids that the algorithm will run for
	 * @param startTime as the the time that the algorithm will start from
	 * @param endTime as the time that the algorithm will stop to search for free times
	 */
	public void search(int[] ids, Date startTime, Date endTime) {
	 Employee[] listOfEmployee= new Employee[ids.length]; 
	
	for(int i = 0; i < ids.length; i++) {	
		listOfEmployee[i] = new Employee();
		 listOfEmployee[i] = employees.get(ids[i]);
		}
	LinkedList<Meeting> totalMeetings = new LinkedList<Meeting>();
	//totalMeetings = Company.compareMeetings(listOfEmployee,startTime,endTime, 0,totalMeetings);
	}
	
	/**
	 * A method to add an employee to the binary tree
	 * @param id as the id of the employee to add
	 * @param name as the name of the employee to add
	 * @param jobTitle as the title of the position in the workplace of the employee
	 */
	public void addEmployee(int id,String forename,String surname, String jobTitle) {
		employees.put(id, new Employee(id,forename,surname,jobTitle));
	}
	
	/**
	 * method to delete an employee from the binary tree
	 * @param id as the id of the employee you want to remove
	 */
	public void deleteEmployee(String id) {
		
	}
	
	/**
	 * method to select a certain employee from the binary tree so that methods can be run on them
	 * eg. edit
	 * @param id as the id of the employee that is to be edited
	 */
	public void selectEmployee(String id) {
		
	}
	
	/*public static LinkedList<Meeting> compareMeetings(Employee[] employees,Date startTime, Date endTime, int index, LinkedList<Meeting> totalMeetings) {
		if(index <=employees.length ) {
			LinkedList<Meeting> employeeMeetings= employees[index].getMeetings(startTime,endTime);
			Iterator it = employeeMeetings.iterator();
				while(it.hasNext()){
					System.out.println(it.next());
			    	}
			return compareMeetings(employees,startTime, endTime, ++index,totalMeetings);
		}
		return totalMeetings; 
	}*/
	
	public void addTestMeetings() {
		Set set = employees.entrySet();
		 
	    // Get an iterator
	    Iterator it = set.iterator();
	 
	    // Display elements
	    while(it.hasNext()) {
	      Map.Entry me = (Map.Entry)it.next();
	      System.out.print("Key is: "+me.getKey() + " & ");
	      System.out.println("Value is: "+ employees.get(me.getKey()));
	      Date startTime = new Date(2018, 7, 7, 12 , 00, 00);
	      Date endTime = new Date(2018, 7, 7, 12,30, 00);
	      employees.get(me.getKey()).setDiary(new Diary());
	     employees.get(me.getKey()).addMeeting(startTime, endTime, "This is a test meeting");
	     
	     startTime = new Date(2018, 8, 7, 12 , 00, 00);
	     endTime = new Date(2018, 8, 7, 12,30, 00);
	     employees.get(me.getKey()).addMeeting(startTime, endTime, "This is a test meeting 2");
	    
	     
	     employees.get(me.getKey()).getDiary().printDiary();;
	     
	     
	    } 
	  }
	
}
