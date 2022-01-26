package org.openjfx.eventHandler;

import org.openjfx.App;

import javafx.application.Platform;
import javafx.event.EventHandler;
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
					app.getRobot().sendAndReceive(text.getText());
					text.setText("");
				}
				else if(isTracking) {
					app.getTracking().sendAndReceive(text.getText());
					text.setText("");
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
