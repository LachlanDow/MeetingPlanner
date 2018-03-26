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
	
	public Action(Diary diary, String action, Meeting meeting) {
		this.diary = diary;
		this.action = action;
		this.meeting = meeting;
	}
	
	public Action(Diary diary, String action, Meeting oldMeeting, Meeting newMeeting) {
		this.diary = diary;
		this.action = action;
		this.meeting = oldMeeting;
		this.newMeeting = newMeeting;
		
	}
	
	public void reverse() {
		switch(action) {
			case "add":
				//Remove the meeting
				diary.delete(meeting, true);
				break;
			case "delete":
				//Add the meeting
				diary.add(meeting, true);
				break;
			case "edit":
				diary.delete(newMeeting, true);
				diary.add(meeting, true);
				break;
		}
	}
}
