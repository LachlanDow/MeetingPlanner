import java.util.Date;
import java.util.TreeMap;

public class Company {
	/**
	 *A binary tree of all the employees in the company
	 */
	private TreeMap<String,Employee> employees = new TreeMap<String,Employee>();
	
	/**
	 * this search  method will accept a string of employee id's and find the times where non of the members of staff are in meetings
	 * @param ids as the array of the employees ids that the algorithm will run for
	 * @param startTime as the the time that the algorithm will start from
	 * @param endTime as the time that the algorithm will stop to search for free times
	 */
	public void search(String[] ids, Date startTime, Date endTime) {
		
	}
	
	/**
	 * A method to add an employee to the binary tree
	 * @param id as the id of the employee to add
	 * @param name as the name of the employee to add
	 * @param jobTitle as the title of the position in the workplace of the employee
	 */
	public void addEmployee(String id,String name, String jobTitle) {
		//employees.put("1234", new Employee(id,name,jobTitle));
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
}
