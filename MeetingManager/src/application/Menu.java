package application;

import java.io.File;
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

		company.addEmployee(123,"John","doe", "softwareEngineer");
		company.addEmployee(145,"Roger","pat", "softwareEngineer");
		company.addEmployee(167,"Jane","mcmordy", "softwareEngineer");
		company.addEmployee(198,"Mike","Phil", "softwareEngineer");
		
		company.addTestMeetings();
		
		/*
		Date startDate = new Date(2018, 7, 6, 11 , 00, 00);
		Date endDate = new Date(2018, 7, 12, 16 , 00, 00);

		Company.addEmployee(123,"John","doe", "softwareEngineer");
		Company.addEmployee(145,"Roger","pat", "softwareEngineer");
		Company.addEmployee(167,"Jane","mcmordy", "softwareEngineer");
		Company.addEmployee(198,"Mike","Phil", "softwareEngineer");
		
		Date startDate = new Date(2018, 7, 7, 11 , 00, 00);
		Date endDate = new Date(2018, 7, 7, 16 , 00, 00);

		int[] ids = {123,145};
		System.out.println(company.search(ids, startDate, endDate));
		
		*/
		
	}

}
