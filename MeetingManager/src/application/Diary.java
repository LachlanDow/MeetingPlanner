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
	 * A LinkedList that contains all the tasks in the diary
	 */
	private LinkedList<Task> taskList = new LinkedList<Task>();
	
	/**
	 * A Stack that contains all recent add/edit/delete functions
	 * so that they will be able to be reversable.
	 */
	private Stack<Action> recentActions = new Stack<Action>();
	
	/**
	 * Returns a LinkedList<Meeting> of meetings that occur on provided date.
	 * @param date Date to get meetings for
	 * @return LinkedList<Meeting> containing meetings occuring on date
	 */

	public LinkedList<Meeting> getMeetingsOnDay(Date date){
		LinkedList<Meeting> meetings = new LinkedList<Meeting>();

		
		for(int i = 0; i < getMeetings().size(); i++) {
			Meeting next = getMeetings().get(i);
			
			//If they're on the same day, add to list.
			if(Validation.sameDay(next.getStartTime(), date)) {
				meetings.add(next);
			}
		}
		
		return meetings;
	}
	
	/**
	 * Add meeting to the diary
	 * @param meeting meeting to add
	 * @param noStack true to add to hide from recentActions stack, false if not
	 */
	public void addMeeting(Meeting meeting, boolean noStack) {
		meetings.add(meeting);
		
		//When undoing, don't add to the stack
		if(!noStack) {
			recentActions.push(new Action(this, "add", meeting));
		}
	}

	/**
	 * Delete meeting from Employee's diary
	 * @param meeting meeting to delete
	 * @param noStack true to add to hide from recentActions stack, false if not
	 */
	public void deleteMeeting(Meeting meeting, boolean noStack) {
		meetings.remove(meeting);
		
		//When undoing, don't add to the stack
		if(!noStack) {
			recentActions.push(new Action(this, "delete", meeting));
		}
	}
	
	/**
	 * Edit a meeting in the diary.
	 * @param oldMeeting Reference to the old meeting
	 * @param newMeeting Reference to the new (changed) meeting
	 * @param noStack rue to add to hide from recentActions stack, false if not
	 */
	public void editMeeting(Meeting oldMeeting, Meeting newMeeting, boolean noStack) {
		recentActions.push(new Action(this, "edit", oldMeeting, newMeeting));
		
		//Delete old, add new. Hide from stack.
		deleteMeeting(oldMeeting, true);
		addMeeting(newMeeting, true);
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
	 * Print all meetings
	 */
	public void printMeetings() {
		if(meetings.isEmpty()) {
			System.out.println("Nothing to print");
		}
		else {
			ListIterator<Meeting> listIterator = meetings.listIterator();
			System.out.println(listIterator.next());
			while (listIterator.hasNext()) {
				System.out.println(listIterator.next());
			}
		}
	}
	
	/**
	 * Print all tasks.
	 */
	public void printTasks() {
		if(taskList.isEmpty()) {
			System.out.println("Nothing to print");
		}
		else {
			ListIterator<Task> listIterator = taskList.listIterator();
			System.out.println(listIterator.next());
			while (listIterator.hasNext()) {
				System.out.println(listIterator.next());
			}
		}
	}

	/**
	 * Get all meetings
	 * @return all meetings
	 */
	public LinkedList<Meeting> getMeetings() {
		return this.meetings;
	}

	/**
	 * Returns the reference to the Task list
	 * @return LinkedList<Task> list of tasks
	 */
	public LinkedList<Task> getTaskList() {
		return taskList;
	}

	/**
	 * Add Task to task list
	 * @param toAdd Task to add
	 */
	public void addTask(Task toAdd) {
		taskList.add(toAdd);
	}

	/**
	 * Remove Task from task list
	 * @param toDelete
	 */
	public void deleteTask(Task toDelete) {
		taskList.remove(toDelete);
	}
	
	/**
	 * Edit a task
	 * @param oldTask task to edit
	 * @param newTask updated information
	 */
	public void editTask(Task oldTask, Task newTask) {
		deleteTask(oldTask);
		addTask(newTask);
	}
}
