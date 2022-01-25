package org.openjfx.eventHandler;

import org.openjfx.App;
import org.openjfx.Client;
import org.openjfx.Robot;
import org.openjfx.Tracking;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

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
				if(app.getRobot() != null){app.getRobot().disconnect();}
				if(app.getTracking() != null){app.getTracking().disconnect();}
				System.exit(0);
			case "rServerConnect":
				if(app.getRobot() ==null){app.setRobot(new Robot(new Client(5005, app)));}
			case "rbefehlseingabe":
				app.getScene().lookup("#mmButtons").setVisible(false);
				app.getScene().lookup("#rEingabeBox").setVisible(true);
				break;
			case "tServerConnect":
				if(app.getTracking() == null){app.setTracking(new Tracking(new Client(5000, app)));}
			case "tbefehlseingabe":
				app.getScene().lookup("#mmButtons").setVisible(false);
				app.getScene().lookup("#tEingabeBox").setVisible(true);
				break;
			case "backToMenu":
				app.getScene().lookup("#mmButtons").setVisible(true);
				app.getScene().lookup("#rEingabeBox").setVisible(false);
				app.getScene().lookup("#tEingabeBox").setVisible(false);
				app.getScene().lookup("#kaliButtons").setVisible(false);
				break;
			case "endPos":
				app.getRobot().endPos();
				break;
			case "kaliHM":
				 app.createCalibration(3, "");
				 if (app.getCalibration() != null) {
					 app.getScene().lookup("#mmButtons").setVisible(false);
					 app.getScene().lookup("#kaliButtons").setVisible(true);
				 }
				break;
			case "moveRobot":
		    	 app.getCalibration().moveRobotPTP();
		    	 break;
			case "kalibrierung":
				 app.getCalibration().constructLinearEquation();
		    	 break;
			default:
				System.out.println("Button not ready yet");
				break;
			case "testMenue":
				app.createCalibration(3, "test");
				break; 
			
		}
	}

}
