/**
 * 
 * @author JavaProject Team 9
 *
 */
public class Menu {

	private Company company = new Company();
	public static void main(String[] args) {
		Menu menu = new Menu();
		menu.createTestCompany();

	}
	public void createTestCompany() {
		company.addEmployee(123,"John","doe", "softwareEngineer");
		company.addEmployee(145,"Roger","pat", "softwareEngineer");
		company.addEmployee(167,"Jane","mcmordy", "softwareEngineer");
		company.addEmployee(198,"Mike","Phil", "softwareEngineer");
		
		company.addTestMeetings();
		
	}

}
