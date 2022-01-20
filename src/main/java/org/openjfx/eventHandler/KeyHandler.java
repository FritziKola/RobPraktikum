package org.openjfx.eventHandler;

import org.openjfx.App;

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
			
			if(event.getCode() == KeyCode.ENTER) {
				if(text.getId().equals("rBefehl"))
					app.getRobot().sendAndReceive(text.getText());
				else
					app.getTracking().sendAndReceive(text.getText());
				text.setText("");
			}
		}
	}

}
