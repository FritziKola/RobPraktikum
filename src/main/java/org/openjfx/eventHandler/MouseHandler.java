package org.openjfx.eventHandler;

import org.openjfx.App;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class MouseHandler implements EventHandler<MouseEvent> {

	private App main;

	public MouseHandler(App instance) {
		this.main = instance;
	}

	@Override
	public void handle(MouseEvent event) {
		if(event.getEventType() == MouseEvent.MOUSE_RELEASED) {
			if(main.getSliderValue() != null) {
				main.getRobot().setSpeed(main.getSliderValue());
				main.setSliderValue(null);
			}
		}
	}
	
	
	
}