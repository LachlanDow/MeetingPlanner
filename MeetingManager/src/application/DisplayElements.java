package application;

import javax.swing.JSpinner;

import javafx.beans.binding.NumberBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.text.Text;

/**
 * Wrapper class for custom JAVAFX elements to avoid repetitive styling code.
 * @author Daniel
 *
 */
public class DisplayElements {
	
	/**
	 * Button wrapper for menu buttons
	 * @author Daniel
	 *
	 */
	public static class MenuButton extends Button {

		/**
		 * Constructor that sets styles.
		 * @param string button text
		 */
		public MenuButton(String string) {
			super(string);
			getStyleClass().add("menuButton");
		}
	}

	/**
	 * Wrapper for Button.
	 * @author Daniel
	 *
	 */
	public static class CustomButton extends Button {

		/**
		 * Sets button styles
		 * @param string button text
		 * @param fontSize font size
		 */
		public CustomButton(String string, int fontSize) {
			super(string);
			setStyle("-fx-font: " + fontSize + " arial;" + " -fx-base: #FFFFFF;");
		}

		/**
		 * Sets button styles
		 * @param string button text
		 * @param fontSize font size
		 * @param color background color
		 */
		public CustomButton(String string, int fontSize, String color) {
			super(string);
			setStyle("-fx-font: " + fontSize + " arial;" + " -fx-base: #" + color + ";");
		}
	}

	/**
	 * Text wrapper class
	 * @author Daniel
	 *
	 */
	public static class CustomText extends Text {
		
		/**
		 * Set custom text styles
		 * @param string text
		 * @param fontSize font size
		 */
		public CustomText(String string, int fontSize) {
			super(string);
			setStyle("-fx-font: " + fontSize + " arial;");
		}
	}

	/**
	 * Textfield implementation that has one input, split into two.
	 *
	 * @author Thomas Bolz
	 * @modifiedBy Daniel Sewerynski
	 * @credit https://dzone.com/articles/javafx-numbertextfield-and
	 */
	public static class NumberTextField extends TextField {
		private ObjectProperty<String> combined = new SimpleObjectProperty<>();

		public NumberTextField(int hour, int mins) {
			super();
			initHandlers();
			setCombined(hour, mins);
		}

		/**
		 * Tries to parse the user input
		 */
		private void parseAndFormatInput() {
			String input = getText();
			try {
				if (input == null || input.length() == 0) {
					return;
				}

				String[] parts = input.split(":", 2);

				int hours = Integer.parseInt(parts[0]);
				int mins = (parts.length < 2) ? 0 : Integer.parseInt(parts[1]);

				if (hours > 23 || hours < 0 || mins < 0 || mins > 60 || parts.length > 2) {
					combined.set("00:00");
				} else {
					setCombined(hours, mins);
				}
			} catch (NumberFormatException e) {
				combined.set("00:00");
			}
		}

		private void initHandlers() {

			// try to parse when focus is lost or RETURN is hit
			setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent arg0) {
					parseAndFormatInput();
				}
			});

			focusedProperty().addListener(new ChangeListener<Boolean>() {
				@Override
				public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
					if (!newValue.booleanValue()) {
						parseAndFormatInput();
					}
				}
			});

			// Set text in field if the text is changed within the program.
			stringProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					setText(combined.get());
				}
			});
		}

		public void setCombined(int hours, int mins) {
			// Pad the numbers
			String hoursStr = "" + hours;
			String minsStr = "" + mins;

			if (hoursStr.length() < 2) {
				hoursStr = "0" + hoursStr;
			}

			if (minsStr.length() < 2) {
				minsStr = "0" + minsStr;
			}

			combined.set(hoursStr + ":" + minsStr);
		}

		public ObjectProperty<String> stringProperty() {
			return combined;
		}
	}

	/**
	 * JavaFX Control that behaves like a {@link JSpinner} known in Swing. The
	 * number in the textfield can be incremented or decremented by a configurable
	 * stepWidth using the arrow buttons in the control or the up and down arrow
	 * keys.
	 *
	 * @author Thomas Bolz
	 * @modifiedBy Daniel Sewerynski
	 * @credit https://dzone.com/articles/javafx-numbertextfield-and
	 */
	public static class NumberSpinner extends HBox {

		private NumberTextField numberField;
		private final double ARROW_SIZE = 4;
		private final Button incrementButton;
		private final Button decrementButton;
		private final NumberBinding buttonHeight;
		private final NumberBinding spacing;

		public NumberSpinner(int hour, int mins) {
			super();

			// TextField
			numberField = new NumberTextField(hour, mins);

			// Enable arrow keys for dec/inc
			numberField.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {

				@Override
				public void handle(KeyEvent keyEvent) {
					if (keyEvent.getCode() == KeyCode.DOWN) {
						decrement();
						keyEvent.consume();
					}
					if (keyEvent.getCode() == KeyCode.UP) {
						increment();
						keyEvent.consume();
					}
				}
			});

			// Painting the up and down arrows
			Path arrowUp = new Path();
			arrowUp.getElements().addAll(new MoveTo(-ARROW_SIZE, 0), new LineTo(ARROW_SIZE, 0),
					new LineTo(0, -ARROW_SIZE), new LineTo(-ARROW_SIZE, 0));
			// mouse clicks should be forwarded to the underlying button
			arrowUp.setMouseTransparent(true);

			Path arrowDown = new Path();
			arrowDown.getElements().addAll(new MoveTo(-ARROW_SIZE, 0), new LineTo(ARROW_SIZE, 0),
					new LineTo(0, ARROW_SIZE), new LineTo(-ARROW_SIZE, 0));
			arrowDown.setMouseTransparent(true);

			// the spinner buttons scale with the textfield size
			// the following approach leads to the desired result, but it is
			// not fully understood why and obviously it is not quite elegant
			buttonHeight = numberField.heightProperty().subtract(3).divide(2);
			// give unused space in the buttons VBox to the incrementBUtton
			spacing = numberField.heightProperty().subtract(2).subtract(buttonHeight.multiply(2));

			// inc/dec buttons
			VBox buttons = new VBox();
			incrementButton = new Button();
			incrementButton.prefWidthProperty().bind(numberField.heightProperty());
			incrementButton.minWidthProperty().bind(numberField.heightProperty());
			incrementButton.maxHeightProperty().bind(buttonHeight.add(spacing));
			incrementButton.prefHeightProperty().bind(buttonHeight.add(spacing));
			incrementButton.minHeightProperty().bind(buttonHeight.add(spacing));
			incrementButton.setFocusTraversable(false);
			incrementButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent ae) {
					increment();
					ae.consume();
				}
			});

			// Paint arrow path on button using a StackPane
			StackPane incPane = new StackPane();
			incPane.getChildren().addAll(incrementButton, arrowUp);
			incPane.setAlignment(Pos.CENTER);

			decrementButton = new Button();
			decrementButton.prefWidthProperty().bind(numberField.heightProperty());
			decrementButton.minWidthProperty().bind(numberField.heightProperty());
			decrementButton.maxHeightProperty().bind(buttonHeight);
			decrementButton.prefHeightProperty().bind(buttonHeight);
			decrementButton.minHeightProperty().bind(buttonHeight);

			decrementButton.setFocusTraversable(false);
			decrementButton.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent ae) {
					decrement();
					ae.consume();
				}
			});

			StackPane decPane = new StackPane();
			decPane.getChildren().addAll(decrementButton, arrowDown);
			decPane.setAlignment(Pos.CENTER);

			buttons.getChildren().addAll(incPane, decPane);
			this.getChildren().addAll(numberField, buttons);
		}

		/**
		 * increment value
		 */
		private void increment() {
			ObjectProperty<String> value = numberField.stringProperty();
			String[] parts = value.get().split(":");

			int incHours = Integer.parseInt(parts[0]) + 1;
			int incMins = Integer.parseInt(parts[1]) + 1; //Round to nearest 10 and add 15 mins
			
			

			if (incMins >= 60) {
				if (incHours < 24) {
					numberField.setCombined(incHours, 0);
				} else {
					numberField.setCombined(0, 0);
				}
			} else {
				numberField.setCombined(Integer.parseInt(parts[0]), incMins);
			}
		}

		/**
		 * decrement value
		 */
		private void decrement() {
			ObjectProperty<String> value = numberField.stringProperty();
			String[] parts = value.get().split(":");

			int incHours = Integer.parseInt(parts[0]) - 1;
			int incMins = Integer.parseInt(parts[1]) - 1; //Round to nearest 10 and minus 15 mins

			if (incMins < 0) {
				if (incHours < 0) {
					numberField.setCombined(23, 59);
				} else {
					numberField.setCombined(incHours, 59);
				}
			} else {
				numberField.setCombined(Integer.parseInt(parts[0]), incMins);
			}
		}
		
		public String getText() {
			return numberField.stringProperty().get();
		}

		public void clear() {
			numberField.clear();
		}
	}
}