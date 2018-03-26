package application;

import java.util.Scanner;

/**
 * Menu class that allows you to launch the GUI as well as use test methods.
 * @author JavaProject Team 9
 *
 */
public class Menu {

	private Tester tester = new Tester();
	
	/**
	 * Entry point to the program.
	 * @param args Default args param for main
	 */
	public static void main(String[] args) {
		Menu menu = new Menu();
		
		menu.userChoice();
	}
	
	/**
	 * Display the menu to the user.
	 */
	public void displayMenu() {
		System.out.println("Meeting Manager Launcher");
		System.out.println("------------------------");
		System.out.println("1. Launch GUI");
		System.out.println("e. Exit");
		System.out.println("-------TEST CASES-------");
		System.out.println("2. Test add Employee validation.");
		System.out.println("3. Test edit Employee.");
		System.out.println("4. Test delete Employee.");
		System.out.println("5. Test meeting validation.");
		System.out.println("6. Test meeting edit.");
		System.out.println("7. Test meeting delete.");
		
	}
	
	/**
	 * Processing the user's choice and running the correct methods.
	 */
	public void userChoice() {
		String choice = "";
		Scanner input = new Scanner(System.in);
		
		//Menu loop
		do {
			displayMenu();
			
			choice = input.nextLine();
			switch(choice) {
				case "1": {
					javafx.application.Application.launch(GUIHandler.class);
				}
				break;
				case "2": {
					tester.testValidationEmployee();
				}
				break;
				case "3": {
					tester.testEditEmployee();
				}
				break;
				case "4": {
					tester.testDeleteEmployee();
				}
				break;
				case "5": {
					tester.testValidationMeeting();
				}
				break;
				case "6": {
					tester.testEditMeeting();
				}
				break;
				case "7": {
					tester.testDeleteMeeting();
				}
				break;
				case "e":{
					System.out.println("Thanks for playing the lottery!");
				}
				break;
				default:{
					System.out.println("Invalid choice selected.");
				}
			}
		}
		while(!choice.equals("e"));
		
		input.close();
	}

}
