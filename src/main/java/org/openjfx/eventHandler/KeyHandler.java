package org.openjfx.eventHandler;

import org.openjfx.App;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class KeyHandler implements EventHandler<KeyEvent> {

	private App app;

	public KeyHandler(App app) {
		this.app = app;
	}

	@Override
	public void handle(KeyEvent event) {
		if(event.getSource() instanceof TextField) {
			TextField text = (TextField) event.getSource();
			boolean isRobot = text.getId().equals("rBefehl");
			boolean isTracking = text.getId().equals("tBefehl");
			
			if(event.getCode() == KeyCode.ENTER) {
				if(isRobot) {
					app.getRobot().messageDecoder(text.getText());
					text.setText("");
				}
				else if(isTracking) {
					app.getTracking().sendAndReceive(text.getText());
					text.setText("");
				}
				else if(text.getId().startsWith("rotation")) {
					if(text.getId().equals("rotationXPlusWert")) {
						app.getRobot().getRotation().set(0, 3,  app.getRobot().getRotation().get(0,3) + Double.parseDouble(text.getText()));
					}
					else if(text.getId().equals("rotationYPlusWert")) {
						app.getRobot().getRotation().set(1, 3,  app.getRobot().getRotation().get(1,3) + Double.parseDouble(text.getText()));
					}
					else if(text.getId().equals("rotationZPlusWert")) {
						app.getRobot().getRotation().set(2, 3,  app.getRobot().getRotation().get(2,3) + Double.parseDouble(text.getText()));
					}
					app.getRobot().sendAndReceive("EnableAlter"); // vielleicht nicht nötig
		            /*String[] rowWise = massage.split(" ");
		            int i = 1;
		            for(int j= 0; j <3; j++ ){
		                for(int l =0; l<3; l++){
		                    rotation.set(j, l, Double.parseDouble(rowWise[i]));
		                    i ++;
		                }
		            }*/

		            app.getRobot().getRotation().print(10 , 5);
		            app.getRobot().gethMPosition().print(10, 5);
		            app.getRobot().sendHomMatrix(app.getRobot().gethMPosition().times(app.getRobot().getRotation()));
		            app.getRobot().sendAndReceive("DisableAlter");
				}
			}
			else if(event.getCode() == KeyCode.UP) {
				if(isRobot && !app.getRobot().getHistory().isEmpty()) {
					text.setText(app.getRobot().getHistory().get(app.getRobot().getHistory().size() - 1));
					Platform.runLater(new Runnable() {

						@Override
						public void run() {
							text.positionCaret(text.getText().length());
						}
						
					});
				}
				else if(isTracking && !app.getTracking().getHistory().isEmpty()) {
					text.setText(app.getTracking().getHistory().get(app.getTracking().getHistory().size() - 1));
					Platform.runLater(new Runnable() {

						@Override
						public void run() {
							text.positionCaret(text.getText().length());
						}
						
					});
				}
			}
		}
	}

}
