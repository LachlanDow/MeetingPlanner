import java.util.LinkedList;
import java.util.Scanner;


public class TaskList {
	

	private LinkedList<Task> tasks;
	
	/**
	 * Default constructor. Initialise fields to default values.
	 */
	public TaskList()
	{
		tasks = new LinkedList<Task>();
	}
	
	/**
	 * Method to add a task to the list
	 * 
	 * @param description
	 * @param priority
	 */
	public void add(/**String description, String priority*/)
	{
		
		Scanner s1 = new Scanner(System.in);
		System.out.print("Give a description of your task.");
		String description = s1.nextLine();
		
		Scanner s2 = new Scanner(System.in);
		System.out.print("What priority is this task?");
		String priority = s1.nextLine();
		
		Task task = new Task(description, priority);
		tasks.add(task);
	}
	
	/**
	 * Method to edit a task in the list
	 */
	public void edit()
	{
		
	}
	
	/**
	 * Method to delete a task in the list
	 */
	public void delete()
	{
		Scanner s1 = new Scanner(System.in);
		System.out.print("Which task would you like to remove?");
		int index = s1.nextInt();
		
		tasks.remove(index);
	}
	
	/**
	 * Method to view the tasks in the list
	 */
	public void viewTasks()
	{
		Task marker = tasks.getFirst();
    	
    	do
    	{
    	System.out.println("Description: " + marker.getDescription() + "   Priority: " + marker.getPriority());
    	marker = marker.getNext();
    	}
    	while (marker != null);
	}
	
	
}
