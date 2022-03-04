package org.openjfx.eventHandler;

import org.openjfx.App;
import org.openjfx.Gui;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class MouseHandler implements EventHandler<MouseEvent> {

	private App main;
	private Gui gui;

	public MouseHandler(App instance, Gui gui) {
		this.main = instance;
		this.gui = gui;
	}

	@Override
	public void handle(MouseEvent event) {
		if(event.getEventType() == MouseEvent.MOUSE_RELEASED) {
			if(gui.getSliderValue() != null) {
				main.getRobot().setSpeed(gui.getSliderValue());
				gui.setSliderValue(null);
			}
		}
	}
	
	
	
}