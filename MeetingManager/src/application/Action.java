package application;

/**
 * Container for "actions" that are stored on the undo stack.
 * @author Daniel
 *
 */
public class Action {
	Diary diary;
	String action;
	Meeting meeting;
	Meeting newMeeting;
	/**
	 * 
	 * @param diary as the diary that has been use
	 * @param action as the action that has been taken
	 * @param meeting as the meeting that was acted on
	 */
	public Action(Diary diary, String action, Meeting meeting) {
		this.diary = diary;
		this.action = action;
		this.meeting = meeting;
	}
	/**
	 * constructor method
	 * @param diary as the diary that has been use
	 * @param action as the action that has been taken
	 * @param oldMeeting as the old meeting that was used
	 * @param newMeeting as the new meeting to be used
	 */
	
	public Action(Diary diary, String action, Meeting oldMeeting, Meeting newMeeting) {
		this.diary = diary;
		this.action = action;
		this.meeting = oldMeeting;
		this.newMeeting = newMeeting;
		
	}
	/**
	 * method to reverse the action taken by the user to undo
	 */
	public void reverse() {
		switch(action) {
			case "add":
				//Remove the meeting
				diary.deleteMeeting(meeting, true);
				break;
			case "delete":
				//Add the meeting
				diary.addMeeting(meeting, true);
				break;
			case "edit":
				diary.deleteMeeting(newMeeting, true);
				diary.addMeeting(meeting, true);
				break;
		}
	}
}
