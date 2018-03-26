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
	private Stack<Action> recentActions = new Stack<Action>();
	
	public LinkedList<Meeting> getMeetingsOnDay(Date date){
		LinkedList<Meeting> meetings = new LinkedList<Meeting>();
		
		for(int i = 0; i < getMeetings().size(); i++) {
			Meeting next = getMeetings().get(i);
			
			if(Validation.sameDay(next.getStartTime(), date)) {
				meetings.add(next);
			}
		}
		
		return meetings;
	}
	
	public void add(Meeting meeting, boolean noStack) {
		meetings.add(meeting);
		
		if(!noStack) {
			recentActions.push(new Action(this, "add", meeting));
		}
	}

	/**
	 * Delete a meeting from the employee's diary.
	 */
	public void delete(Meeting meeting, boolean noStack) {
		meetings.remove(meeting);
		
		if(!noStack) {
			recentActions.push(new Action(this, "delete", meeting));
		}
	}
	
	public void edit(Meeting oldMeeting, Meeting newMeeting, boolean noStack) {
		recentActions.push(new Action(this, "edit", oldMeeting, newMeeting));
		
		delete(oldMeeting, true);
		add(newMeeting, true);
	}
	
	/**
	 * Undo the last add/edit/delete action.
	 */
	public void undo() {
		if(!recentActions.isEmpty()) {
			Action pop = recentActions.pop();
			
			//Reverse the action.
			pop.reverse();
		}
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
