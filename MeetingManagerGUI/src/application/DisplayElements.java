package application;

import javafx.scene.control.Button;
import javafx.scene.text.Text;

public class DisplayElements {
	public static class MenuButton extends Button {
		
		public MenuButton(String string) {
			super(string);
			getStyleClass().add("menuButton");
		}
	}
	
	public static class CustomButton extends Button {
		
		public CustomButton(String string, int fontSize) {
			super(string);
			setStyle("-fx-font: " + fontSize + " arial;" + 
					" -fx-base: #FFFFFF;");
		}
		
		public CustomButton(String string, int fontSize, String color) {
			super(string);
			setStyle("-fx-font: " + fontSize + " arial;" + 
					" -fx-base: #" + color + ";");
		}
	}
	
	public static class CustomText extends Text {
		
		public CustomText(String string, int fontSize) {
			super(string);
			setStyle("-fx-font: " + fontSize + " arial;");
		}
	}
}
