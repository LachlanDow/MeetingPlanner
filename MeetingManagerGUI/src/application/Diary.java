package application;

import java.util.Date;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Stack;
 	 
/**
 * This class handles all things that relate to an Employee's Diary.
 * It stores all the meetings and also contains the Stack that keeps
 * track of the recently performed actions.
 * @author Daniel
 *
 */
public class Diary {
	/**
	 * A LinkedList that contains all the meetings in the diary.
	 */
	private LinkedList<Meeting> meetings = new LinkedList<Meeting>();
	
	/**
	 * A Stack that contains all recent add/edit/delete functions
	 * so that they will be able to be reversable.
	 */
	private Stack<String> recentActions;
	
	/**
	 * Add a meeting to the employee's diary.
	 */
	public void add(Date startTime, Date endTime, String description) {
		
		meetings.add(new Meeting(startTime, endTime, description));
	}
	
	/**
	 * Delete a meeting from the employee's diary.
	 * @return Meeting that was deleted.
	 */
	public Meeting delete() {
		return null;
	}
	
	/**
	 * Edit a meeting within the employee's diary.
	 * @param meeting Meeting that has to be changed.
	 * @param start New start time of the meeting.
	 * @param end New end time of the meeting.
	 * @param desc New description for the meeting.
	 */
	public void edit(Date meeting, Date start, Date end, String desc) {
		
	}
	
	/**
	 * Undo the last add/edit/delete action.
	 */
	public void undo() {
		
	}
	
	/**
	 * Print the contents of the diary.
	 */
	public void printDiary() {
		System.out.println("ListIterator Approach: ==========");
		ListIterator<Meeting> listIterator = meetings.listIterator();
		System.out.println(listIterator.next());
		while (listIterator.hasNext()) {
			System.out.println(listIterator.next());
		}
	}
	/**
	 * Save the diary contents to a file.
	 */
	public void save() {
		
	}
	
	/**
	 * Load diary contents into the program.
	 */
	public void load() {
		
}
	public LinkedList<Meeting> getMeetings() {
		return this.meetings;
	}
 }	 