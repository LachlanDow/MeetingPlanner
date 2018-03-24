import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Map;
import java.util.TreeMap;

public class Company {
	/**
	 * A binary tree of all the employees in the company
	 */
	private TreeMap<Integer, Employee> employees = new TreeMap<Integer, Employee>();

	/**
	 * this search method will accept a string of employee id's and find the times
	 * where non of the members of staff are in meetings
	 * 
	 * @param ids
	 *            as the array of the employees ids that the algorithm will run for
	 * @param startTime
	 *            as the the time that the algorithm will start from
	 * @param endTime
	 *            as the time that the algorithm will stop to search for free times
	 */
	public LinkedList<Meeting> search(int[] ids, Date startTime, Date endTime) {
		Employee[] listOfEmployee = new Employee[ids.length];
		LinkedList<Meeting> totalMeetings = new LinkedList<Meeting>();
		for (int i = 0; i < ids.length; i++) {
			listOfEmployee[i] = new Employee();
			listOfEmployee[i] = employees.get(ids[i]);
			for (int j = 0; j < listOfEmployee[i].getDiary().getMeetings().size(); j++) {
				totalMeetings.add(listOfEmployee[j].getDiary().getMeetings().get(j));
			}
		}
		totalMeetings = this.mergeMeetings(totalMeetings);

		return this.getTimesBetween(startTime, endTime, totalMeetings, new LinkedList<Meeting>());

	}

	public LinkedList<Meeting> compareMeetings(Employee[] employees, Date startTime, Date endTime, int index,
			LinkedList<Meeting> totalMeetings) {
		if (index < employees.length) {
			LinkedList<Meeting> employeeMeetings = employees[index].getDiary().getMeetings();
			ListIterator<Meeting> it = employeeMeetings.listIterator();

			while (it.hasNext()) {
				Meeting meetingToAdd = (Meeting) it.next();

				totalMeetings.add(meetingToAdd);

			}
			return compareMeetings(employees, startTime, endTime, ++index, totalMeetings);
		}
		return totalMeetings;
	}

	private LinkedList<Meeting> mergeMeetings(LinkedList<Meeting> totalMeetings) {
		Collections.sort(totalMeetings, new Comparator<Meeting>() {
			public int compare(Meeting meeting1, Meeting meeting2) {
				return meeting1.getStartTime().compareTo(meeting2.getStartTime());
			}
		});

		for (int i = 0; i < totalMeetings.size() - 1; i++) {
			Meeting meeting1 = totalMeetings.get(i);
			Meeting meeting2 = totalMeetings.get(i + 1);
			if (meeting1.getEndTime().compareTo(meeting2.getStartTime()) > 0) {
				totalMeetings.get(i).setEndTime(meeting2.getEndTime());
				totalMeetings.remove(i + 1);
			}
		}

		return totalMeetings;

	}

	/**
	 * A method to add an employee to the binary tree
	 * 
	 * @param id
	 *            as the id of the employee to add
	 * @param name
	 *            as the name of the employee to add
	 * @param jobTitle
	 *            as the title of the position in the workplace of the employee
	 */
	public void addEmployee(int id, String forename, String surname, String jobTitle) {
		employees.put(id, new Employee(id, forename, surname, jobTitle));
	}

	/**
	 * method to delete an employee from the binary tree
	 * 
	 * @param id
	 *            as the id of the employee you want to remove
	 */
	public void deleteEmployee(String id) {

	}

	/**
	 * method to select a certain employee from the binary tree so that methods can
	 * be run on them eg. edit
	 * 
	 * @param id
	 *            as the id of the employee that is to be edited
	 */
	public void selectEmployee(String id) {

	}

	/*
	 * public LinkedList<Meeting> compareMeetings(Employee[] employees,Date
	 * startTime, Date endTime, int index, LinkedList<Meeting> totalMeetings) {
	 * if(index < employees.length ) { LinkedList<Meeting> employeeMeetings=
	 * employees[index].getDiary().getMeetings(); ListIterator<Meeting> it =
	 * employeeMeetings.listIterator();
	 * 
	 * while(it.hasNext()){ Meeting meetingToAdd = (Meeting) it.next();
	 * 
	 * totalMeetings.add(meetingToAdd);
	 * 
	 * } return compareMeetings(employees,startTime, endTime,
	 * ++index,totalMeetings); } return totalMeetings; }
	 */

	public void addTestMeetings() {
		int counter = 0;

		for (Map.Entry<Integer, Employee> me : employees.entrySet()) {
			counter++;
			System.out.print("Key is: " + me.getKey() + " & ");
			System.out.println("Value is: " + employees.get(me.getKey()));
			Date startTime = new Date(2018, 7, 7 + counter, 12, 00, 00);
			Date endTime = new Date(2018, 7, 7 + counter, 12, 30, 00);
			employees.get(me.getKey()).setDiary(new Diary());
			employees.get(me.getKey()).addMeeting(startTime, endTime, "This is a test meeting");

			startTime = new Date(2018, 7, 8 + counter, 12, 00, 00);
			endTime = new Date(2018, 7, 8 + counter, 12, 30, 00);
			employees.get(me.getKey()).addMeeting(startTime, endTime, "This is a test meeting 2");

			employees.get(me.getKey()).getDiary().printDiary();
		}
	}

	public LinkedList<Meeting> getTimesBetween(Date startTime,Date endTime, LinkedList<Meeting> totalMeetings, LinkedList<Meeting> timesBetween) {
		timesBetween.add(new Meeting(startTime,totalMeetings.get(0).getStartTime(),""));
		/*for(int i =0; i < totalMeetings) {
		
		}*/
		return null;
	}
}