package application;

import java.util.Date;

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
	
	@SuppressWarnings("deprecation")
	public void createTestCompany() {
		Company.addEmployee(123,"John","doe", "softwareEngineer");
		Company.addEmployee(145,"Roger","pat", "softwareEngineer");
		Company.addEmployee(167,"Jane","mcmordy", "softwareEngineer");
		Company.addEmployee(198,"Mike","Phil", "softwareEngineer");
		
		Date startDate = new Date(2018, 7, 7, 11 , 00, 00);
		Date endDate = new Date(2018, 7, 7, 16 , 00, 00);
		int[] ids = {123,145};
		System.out.println(company.search(ids, startDate, endDate));
		
	}

}
