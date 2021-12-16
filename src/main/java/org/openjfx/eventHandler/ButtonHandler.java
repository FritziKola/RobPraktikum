package org.openjfx.eventHandler;

import org.openjfx.App;
import org.openjfx.Client;
import org.openjfx.Robot;
import org.openjfx.Tracking;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

public class ButtonHandler implements EventHandler<ActionEvent> {

	private App app;

	public ButtonHandler(App app) {
		this.app = app;
	}
	
	@Override
	public void handle(ActionEvent event) {
		Button button = (Button) event.getSource();
		
		switch(button.getId()) {
			case "exit":
				System.exit(0);
			case "rServerConnect":
				app.setRobot(new Robot(new Client(5005, app)));
				break;
			case "tServerConnect":
				app.setTracking(new Tracking(new Client(5000, app)));
				break;
			case "befehlseingabe":
				app.getScene().lookup("#mmButtons").setVisible(false);
				app.getScene().lookup("#eingabeBox").setVisible(true);
				break;
			case "backToMenu":
				app.getScene().lookup("#mmButtons").setVisible(true);
				app.getScene().lookup("#eingabeBox").setVisible(false);
		}
	}

}
