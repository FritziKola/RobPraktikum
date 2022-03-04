package org.openjfx;

import org.openjfx.eventHandler.ButtonHandler;
import org.openjfx.eventHandler.KeyHandler;
import org.openjfx.eventHandler.MouseHandler;

import javafx.event.ActionEvent;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class Gui {
	
	private Scene scene;
	private ButtonHandler bh;
	private App app;
	private long sliderValue;

	public Gui(App app) {
		this.app = app;
		scene = app.getScene();    
		bh = new ButtonHandler(app);
		    
		    
		initMenu();
		    
		//initTestInput();
		    

	}

	
	// Aufbau der gesamten Szene in Form eines Grids
	private void initMenu() {
		StackPane root = (StackPane) scene.getRoot(); 
		GridPane grid = new GridPane();
		grid.setId("menuGrid");
		ColumnConstraints robot = new ColumnConstraints();
		robot.setPercentWidth(42.5);
		grid.getColumnConstraints().add(robot);
		
		ColumnConstraints tracking = new ColumnConstraints();
		tracking.setPercentWidth(42.5);
		grid.getColumnConstraints().add(tracking);
		
		ColumnConstraints generalButtons = new ColumnConstraints(); 
		generalButtons.setPercentWidth(15);
		grid.getColumnConstraints().add(generalButtons);
		
		RowConstraints title = new RowConstraints();
		title.setPercentHeight(10);
		grid.getRowConstraints().add(title);
		
		
		RowConstraints buttonFeld = new RowConstraints();
		buttonFeld.setPercentHeight(35);
		grid.getRowConstraints().add(buttonFeld);
		
		RowConstraints schieber = new RowConstraints();
		schieber.setPercentHeight(15);
		grid.getRowConstraints().add(schieber);
		
		RowConstraints ausgabe = new RowConstraints();
		ausgabe.setPercentHeight(30);
		grid.getRowConstraints().add(ausgabe);
		
		RowConstraints eingabe = new RowConstraints();
		eingabe.setPercentHeight(10);
		grid.getRowConstraints().add(eingabe);
		
		root.getChildren().add(grid);
		Color bg = Color.web("0xDCD3CE");
        root.setBackground(new Background(new BackgroundFill(bg, null, null)));
        grid.setMaxWidth(scene.getWidth());
        grid.setMaxHeight(scene.getHeight());
        grid.setMinWidth(scene.getWidth());
        grid.setMinHeight(scene.getHeight());
		initRobotMenu();
		initTrackingMenu();
		initGeneralButtons();
	}
	
	private void initRobotMenu() {
		GridPane grid = (GridPane) scene.lookup("#menuGrid");
		FlowPane rLabel = new FlowPane();
		Label robotLabel = new Label("RoboterInput");
		rLabel.getChildren().add(robotLabel);
		rLabel.setAlignment(Pos.CENTER);
		grid.add(rLabel, 0, 0);
		//Button Bereich
		FlowPane robotButtons = new FlowPane(Orientation.HORIZONTAL, 10, 20);
		Button endPos = new Button("EndPosition");
		endPos.setId("endPos");
		robotButtons.getChildren().add(endPos);
		Button greifKlotz = new Button("Greife Bauklotz");
		greifKlotz.setId("greifKlotz");
		robotButtons.getChildren().add(greifKlotz);
		Button ansaugen = new Button("Ansaugen");
		ansaugen.setId("ansaugen");
		robotButtons.getChildren().add(ansaugen);
		Button loslassen = new Button("loslassen");
		loslassen.setId("loslassen");
		robotButtons.getChildren().add(loslassen);
		Button becherPos = new Button("an BecherPosition fahren");
		becherPos.setId("becherPos");
		robotButtons.getChildren().add(becherPos);
		robotButtons.setAlignment(Pos.CENTER);
		grid.add(robotButtons, 0, 1);
		for(Node node : robotButtons.getChildren()) {
        	if(node instanceof Button) {
        		Button b = (Button) node;
        		b.setPrefSize(150, 70);
        		b.addEventHandler(ActionEvent.ACTION, bh);
        	}
        }
		//Slider
		Slider robotSlider = new Slider(1, 100, 1);
		robotSlider.setShowTickMarks(true);
		robotSlider.valueProperty().addListener((observable, oldVal, newVal) -> {
			sliderValue = Math.round((double) newVal);
		});
		robotSlider.addEventHandler(MouseEvent.MOUSE_RELEASED, new MouseHandler(app, this));
		robotSlider.setMaxWidth(scene.getWidth() * .20);
		robotSlider.setMinWidth(scene.getWidth() * .20);
		grid.add(robotSlider, 0, 2);
		//AusgabeFeld
		TextArea robotAusgabetext = new TextArea();
		robotAusgabetext.setId("rAusgabe");
		robotAusgabetext.setEditable(false);
		ScrollPane robotAusgabe = new ScrollPane(robotAusgabetext); 
		robotAusgabetext.setPrefHeight(scene.getWidth() * .45);
		robotAusgabe.setId("robotAusgabePane");
		grid.add(robotAusgabe, 0, 3);
		//Eingabe 
		TextField robotBefehl = new TextField();
		robotBefehl.setId("rBefehl");
		robotBefehl.addEventHandler(KeyEvent.KEY_PRESSED, new KeyHandler(app));
		grid.add(robotBefehl, 0, 4);
	}
	
	private void initTrackingMenu() {
		GridPane grid = (GridPane) scene.lookup("#menuGrid");
		FlowPane tLabel = new FlowPane();
		Label trackingLabel = new Label("TrackingInput");
		trackingLabel.setAlignment(Pos.CENTER);
		tLabel.getChildren().add(trackingLabel);
		tLabel.setAlignment(Pos.CENTER);
		grid.add(tLabel, 1, 0);
		//Button Bereich
		FlowPane trackingButtons = new FlowPane(Orientation.HORIZONTAL, 10, 20);
		Button moveRobot = new Button("Move Robot");
		moveRobot.setId("moveRobot");
		trackingButtons.getChildren().add(moveRobot);
		Button kalibrierung = new Button("Kalibrierung");
		kalibrierung.setId("kalibrierung");
		trackingButtons.getChildren().add(kalibrierung);
		Button mVornehmen = new Button("Messung vornehmen");
		mVornehmen.setId("mVornehmen");
		trackingButtons.getChildren().add(mVornehmen);
		Button anPunktFahren = new Button("Roboter an Punkt fahren");
		anPunktFahren.setId("anPunktfahren");
		trackingButtons.getChildren().add(anPunktFahren);
		trackingButtons.setAlignment(Pos.CENTER);
		grid.add(trackingButtons, 1, 1);
		for(Node node : trackingButtons.getChildren()) {
        	if(node instanceof Button) {
        		Button b = (Button) node;
        		b.setPrefSize(150, 70);
        		b.addEventHandler(ActionEvent.ACTION, bh);
        	}
        }
		//Slider
		/*Slider robotSlider = new Slider(1, 100, 1);
		robotSlider.setShowTickMarks(true);
		robotSlider.valueProperty().addListener((observable, oldVal, newVal) -> {
			sliderValue = Math.round((double) newVal);
		});
		robotSlider.addEventHandler(MouseEvent.MOUSE_RELEASED, new MouseHandler(app));
		grid.add(robotSlider, 1, 2);
		*/
		//AusgabeFeld
		TextArea trackingAusgabetext = new TextArea();
		trackingAusgabetext.setId("tAusgabe");
		trackingAusgabetext.setEditable(false);
		ScrollPane trackingAusgabe = new ScrollPane(trackingAusgabetext); 
		trackingAusgabetext.setPrefHeight(scene.getWidth() * .45 );
		trackingAusgabe.setId("trackingAusgabePane");
		grid.add(trackingAusgabe, 1, 3);
		//Eingabe 
		TextField trackingBefehl = new TextField();
		trackingBefehl.setId("tBefehl");
		trackingBefehl.addEventHandler(KeyEvent.KEY_PRESSED, new KeyHandler(app));
		grid.add(trackingBefehl, 1, 4);
	}
	
	private void initGeneralButtons() {
		GridPane grid = (GridPane) scene.lookup("#menuGrid");
		FlowPane gb = new FlowPane(Orientation.VERTICAL, 10, 20);
		Button serverButton = new Button("Server verbinden");
        serverButton.setId("serverConnect");
        gb.getChildren().add(serverButton);
	    Button exit = new Button("Exit");
	    exit.setId("exit");
	    gb.getChildren().add(exit);
        for(Node node : gb.getChildren()) {
    		if(node instanceof Button) {
        		Button b = (Button) node;
        		b.setPrefSize(150, 70);
        		b.addEventHandler(ActionEvent.ACTION, bh);
    		}
        }
        grid.add(gb, 2, 1, 1, 5);
	}
	
	public Long getSliderValue() {
		return sliderValue;
	}
    
    public void setSliderValue(Long sliderValue) {
		this.sliderValue = sliderValue;
	}
}

