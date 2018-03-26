package application;

import javafx.scene.Parent;
import javafx.scene.Scene;

public class CustomScene extends Scene {

	public CustomScene(Parent root) {
		super(root, 1000, 600);
		root.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
	}
	
}
