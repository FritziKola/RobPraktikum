package org.openjfx.eventHandler;

import org.openjfx.App;
import org.openjfx.Client;
import org.openjfx.Robot;
import org.openjfx.Tracking;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

public class ButtonHandler implements EventHandler<ActionEvent> {

	private App app;

	public ButtonHandler(App app) {
		this.app = app;
	}
	
	@Override
	public void handle(ActionEvent event) {
		Button button = (Button) event.getSource();
		GridPane gp = (GridPane) app.getScene().lookup("#menuGrid");
		
		switch(button.getId()) {
			case "exit":
				if(app.getRobot() != null){app.getRobot().disconnect();}
				if(app.getTracking() != null){app.getTracking().disconnect();}
				System.exit(0);
			case "serverConnect":
				if(app.getRobot() ==null){app.setRobot(new Robot(new Client(5005, app),app));}
				if(app.getTracking() == null){app.setTracking(new Tracking(new Client(5000, app), app));}
				if(app.getRobot() != null) gp.getChildren().remove(app.getScene().lookup("#serverConnect"));
				break;
			case "endPos":
				app.getRobot().endPos();
				app.getScene().lookup("#rBefehl").requestFocus();
				break;
			case "ansaugen":
				app.getRobot().ansaugen();
				app.getScene().lookup("#rBefehl").requestFocus();
				break;
			case "loslassen":
				app.getRobot().loslassen();
				app.getScene().lookup("#rBefehl").requestFocus();
				break;
			case "bausteinPos":
				app.getRobot().bausteinPos();
				app.getScene().lookup("#rBefehl").requestFocus();
				break;
			case "stackLoop":
				app.getRobot().stackLoop();
				break;
			case "hMBerechnen":
				app.getRobot().mHPositionBerechnen(app.getTracking().getMeasurement(), app.getCalibration().getX(), app.getCalibration().getY()); ;
				break;
			case "moveRobot":
		    	 app.getCalibration().moveRobotPTP();
		    	 break;
			case "kalibrierung":
				 app.createCalibration(12, "");
				 app.getCalibration().solveCalibration();
		    	 break;
			case "mVornehmen":
				app.getTracking().takeMeasurements(app.getCalibration());
				break;
			case "anPunktFahren":
				app.getRobot().moveToPoint(app.getTracking().getMeasurement(), app.getCalibration().getX(), app.getCalibration().getY());
				break;
			default:
				System.out.println("Button not ready yet");
				break;
			case "testMenue":
				break; 
			
		}
	}

	/*

	TODO: 	(	1. Roboter an den punkt fahren button auch beim robot menue
				2.  textfeld in eingabe sollte nich immer nach oben springen bei robot und tracking
				3. messungvronehmen auch in tracking
				4. Kalibriegungs button zu Kalibrieungs menue ändern
				5. haupt kalibrierung auch im hauptmenu
				6. move robot als Roboter zufällig bewegen im roboter )
		Oder:
		Du kannst die punkte machen  oder ganz anders: Alle Button im hauptmenue und rechts roboter textfeld und links tracking textfeld
	 */

}
