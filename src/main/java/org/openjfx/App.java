package org.openjfx;

import org.openjfx.eventHandler.ButtonHandler;
import org.openjfx.eventHandler.KeyHandler;

import javafx.application.Application;
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
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * JavaFX App, Gui for Robot Gui
 */
public class App extends Application {

	private Robot robot;
	private Tracking tracking;
	private Scene scene;
	private ButtonHandler bh;
	public Client client;
	private Calibration calibration;
	
    @Override
    public void start(Stage stage) {
        scene = new Scene(new StackPane(), 900, 600);
        scene.getStylesheets().add(getClass().getResource("css/style.css").toExternalForm());
        stage.setResizable(false);
        stage.setScene(scene);
        
        bh = new ButtonHandler(this);
        
        
        initMainMenu();
        
        initRobotInput();
        
        initHMKalibrierung();
        
        initTrackingInput();
        
        stage.show();
    }

	/**
	 * Initializes robot menu scene
	 */
    private void initRobotInput() {
    	StackPane root = (StackPane) scene.getRoot();
    	Label eingabe = new Label("Eingabe:");
		TextField befehl = new TextField();
		befehl.setId("rBefehl");
		befehl.addEventHandler(KeyEvent.KEY_PRESSED, new KeyHandler(this));
		
		Button backToMmButton = new Button("Back to Menu");
		backToMmButton.setId("backToMenu");
		Button endPos = new Button("EndPosition");
		endPos.setId("endPos");
		HBox hb = new HBox();
		GridPane grid = new GridPane();
		ColumnConstraints left = new ColumnConstraints();
		left.setPercentWidth(75);
		grid.getColumnConstraints().add(left);
		ColumnConstraints right = new ColumnConstraints();
		right.setPercentWidth(25);
		grid.getColumnConstraints().add(right);
		
	
		RowConstraints biggy = new RowConstraints();
		biggy.setPercentHeight(89);
		grid.getRowConstraints().add(biggy);
		RowConstraints smally = new RowConstraints();
		smally.setPercentHeight(11);
		grid.getRowConstraints().add(smally);
		
		FlowPane buttons = new FlowPane(Orientation.VERTICAL, 10, 20);
		buttons.getChildren().add(backToMmButton);
		buttons.getChildren().add(endPos);
		TextArea ausgabetext = new TextArea();
		ausgabetext.setId("rAusgabe");
		ausgabetext.setEditable(false);
		ausgabetext.setPrefWidth(scene.getWidth() * .75 - 2);
		ausgabetext.setPrefHeight(scene.getHeight() * .89 - 2);
		ScrollPane ausgabe = new ScrollPane(ausgabetext);
		ausgabe.setId("ausgabePan");
		buttons.setAlignment(Pos.CENTER);
		//TODO Funktion einfügen für z.B. Geschwindigkeit vom Roboter einbauen
		Slider slider = new Slider(1, 20, 1);
		slider.setShowTickMarks(true);
		buttons.getChildren().add(slider);
		//grid.add(buttons, 1, 0, 1, 2);
		grid.add(ausgabe, 0 ,0);
		grid.setId("rEingabeBox");
		grid.add(hb, 0, 1);
		//grid.setSpacing(10);
		hb.getChildren().add(eingabe);
		hb.getChildren().add(befehl);	
		grid.add(buttons, 1,0);
		//hb.getChildren().add(backToMmButton);
		//hb.getChildren().add(endPos);
		//grid.add(backToMmButton, 1, 1);
		//grid.add(endPos, 1, 1);
		befehl.setPrefSize(500, 50);
		grid.setVisible(false);
		root.getChildren().add(grid);
		for(Node node : buttons.getChildren()) {
        	if(node instanceof Button) {
        		Button b = (Button) node;
        		b.setPrefSize(150, 70);
        		b.addEventHandler(ActionEvent.ACTION, bh);
        	}
        }
	}

	/**
	 * Initializes tracking menu scene
	 */
    private void initTrackingInput() {
     	StackPane root = (StackPane) scene.getRoot();
    	Label eingabe = new Label("Eingabe:");
		TextField befehl = new TextField();
		FlowPane buttons = new FlowPane(Orientation.VERTICAL, 20, 40);
		befehl.setId("tBefehl");
		befehl.addEventHandler(KeyEvent.KEY_PRESSED, new KeyHandler(this));
		Button backToMmButton = new Button("Back to Menu");
		backToMmButton.setId("backToMenu");
		backToMmButton.addEventFilter(ActionEvent.ACTION, bh);
		HBox hb = new HBox();
		GridPane grid = new GridPane();
		RowConstraints biggy = new RowConstraints();
		biggy.setPercentHeight(89);
		grid.getRowConstraints().add(biggy);
		RowConstraints smally = new RowConstraints();
		smally.setPercentHeight(11);
		grid.getRowConstraints().add(smally);
		ColumnConstraints left = new ColumnConstraints();
		left.setPercentWidth(75);
		grid.getColumnConstraints().add(left);
		ColumnConstraints right = new ColumnConstraints();
		right.setPercentWidth(25);
		grid.getColumnConstraints().add(right);
		TextArea ausgabetext = new TextArea();
		ausgabetext.setId("tAusgabe");
		ausgabetext.setEditable(false);
		ausgabetext.setPrefWidth(scene.getWidth() * .75 - 2);
		ausgabetext.setPrefHeight(scene.getHeight() * .89 - 2);
		ScrollPane ausgabe = new ScrollPane(ausgabetext);
		ausgabe.setId("ausgabePan");
		buttons.getChildren().add(backToMmButton);
		buttons.setAlignment(Pos.CENTER);
		
		
		//grid.add(buttons, 1, 0, 1, 2);
		grid.add(ausgabe, 0 ,0);
		grid.setId("tEingabeBox");
		grid.add(hb, 0, 1);
		
		grid.add(buttons, 1,0);
		//grid.setSpacing(10);
		hb.getChildren().add(eingabe);
		hb.getChildren().add(befehl);
		
		befehl.setPrefSize(500, 50);
		grid.setVisible(false);
		root.getChildren().add(grid);
		for(Node node : buttons.getChildren()) {
        	if(node instanceof Button) {
        		Button b = (Button) node;
        		b.setPrefSize(150, 70);
        		b.addEventHandler(ActionEvent.ACTION, bh);
        	}
		}
		
	}
/*  private void initTestInput() {
        	StackPane root = (StackPane) scene.getRoot();
        	Label eingabe = new Label("Eingabe:");
    		TextField befehl = new TextField();
    		befehl.setId("testBefehl");
    		befehl.addEventHandler(KeyEvent.KEY_PRESSED, new KeyHandler(this));
    		
    		Button backToMmButton = new Button("Back to Menu");
    		backToMmButton.setId("backToMenu");
    		backToMmButton.addEventFilter(ActionEvent.ACTION, bh);
    		Button endPos = new Button("EndPosition");
    		endPos.setId("endPos");
    		endPos.addEventFilter(ActionEvent.ACTION, bh);
    		HBox hb = new HBox();
    		GridPane grid = new GridPane();
    		RowConstraints biggy = new RowConstraints();
    		biggy.setPercentHeight(89);
    		grid.getRowConstraints().add(biggy);
    		
    		RowConstraints smally = new RowConstraints();
    		smally.setPercentHeight(11);
    		grid.getRowConstraints().add(smally);
    		
    		Text ausgabetext = new Text();
    		ausgabetext.setId("rAusgabe");
    		ScrollPane ausgabe = new ScrollPane(ausgabetext);
    		ausgabe.setId("ausgabePan");
    		
    		grid.add(ausgabe, 0 ,0);
    		grid.setId("rEingabeBox");
    		grid.add(hb, 0, 1);
    		//grid.setSpacing(10);
    		hb.getChildren().add(eingabe);
    		hb.getChildren().add(befehl);
    		hb.getChildren().add(backToMmButton);
    		hb.getChildren().add(endPos);
    		
    		befehl.setPrefSize(500, 50);
    		grid.setVisible(false);
    		root.getChildren().add(grid);
    	
    } */

	/**
	 * Initializes calibration  menu scene
	 */
    private void initHMKalibrierung() {
    	StackPane root = (StackPane) scene.getRoot();
    	FlowPane kaliButtons = new FlowPane(Orientation.VERTICAL, 20, 40);
    	kaliButtons.setId("kaliButtons");
    	Button moveRobot = new Button("Move Robot");
    	moveRobot.setId("moveRobot");
    	kaliButtons.getChildren().add(moveRobot);
    	Button kalibrierung =new Button("Kalibrierung");
    	kalibrierung.setId("kalibrierung");
    	kaliButtons.getChildren().add(kalibrierung);
    	Button leererB1 = new Button("Leerer Buttons 1");
    	leererB1.setId("leer1");
    	kaliButtons.getChildren().add(leererB1);
    	Button leererB2 =new Button("Leerer Button 2");
    	leererB2.setId("leer2");
    	kaliButtons.getChildren().add(leererB2);
    	Button backToMmButton = new Button("Back to Menu");
		backToMmButton.setId("backToMenu");
		kaliButtons.getChildren().add(backToMmButton);
        kaliButtons.setMaxHeight(400);
        kaliButtons.setMaxWidth(200);
        root.setPrefWidth(scene.getWidth());
        root.setPrefHeight(scene.getHeight());
    	root.getChildren().add(kaliButtons);
        root.setAlignment(Pos.CENTER);
        kaliButtons.setVisible(false);
        for(Node node : kaliButtons.getChildren()) {
        	if(node instanceof Button) {
        		Button b = (Button) node;
        		b.setPrefSize(200, 70);
        		b.addEventHandler(ActionEvent.ACTION, bh);
        	}
        }
    }

	/**
	 * Initializes main menu scene
	 */
	private void initMainMenu() {
    	StackPane root = (StackPane) scene.getRoot();
    	Color bg = Color.web("0xDCD3CE");
        root.setBackground(new Background(new BackgroundFill(bg, null, null)));
        root.setPrefWidth(scene.getWidth());
        root.setPrefHeight(scene.getHeight());
        FlowPane mainMenuButtons = new FlowPane(Orientation.VERTICAL, 20, 40);
        mainMenuButtons.setId("mmButtons");
        mainMenuButtons.setBackground(new Background(new BackgroundFill(bg, null, null)));
        mainMenuButtons.setMaxHeight(400);
        mainMenuButtons.setMaxWidth(200);
        root.getChildren().add(mainMenuButtons);
        root.setAlignment(Pos.CENTER);
        
        Button serverButton = new Button("Server verbinden");
        serverButton.setId("serverConnect");
        mainMenuButtons.getChildren().add(serverButton);
        Button rbefehlseingabe = new Button("RobServer Befehlseingabe"); 
        rbefehlseingabe.setId("rbefehlseingabe");
        mainMenuButtons.getChildren().add(rbefehlseingabe);
        Button tbefehlseingabe = new Button("TrackingServer Befehlseingabe"); 
        tbefehlseingabe.setId("tbefehlseingabe");
        mainMenuButtons.getChildren().add(tbefehlseingabe);
        Button kaliHM = new Button("Kalibrierung");
        kaliHM.setId("kaliHM");
        mainMenuButtons.getChildren().add(kaliHM);
        Button testMenue = new Button("TestMenu");
        testMenue.setId("testMenu");
        mainMenuButtons.getChildren().add(testMenue);
        Button exitButton = new Button("Exit");
        exitButton.setId("exit");
        mainMenuButtons.getChildren().add(exitButton);
        
        for(Node node : mainMenuButtons.getChildren()) {
        	if(node instanceof Button) {
        		Button b = (Button) node;
        		b.setPrefSize(200, 70);
        		b.addEventHandler(ActionEvent.ACTION, bh);
        	}
        }
	}


	/**
	 * TODO: schöner machen!
	 * @param measurements
	 * @param test
	 */
	public void createCalibration(int measurements, String test){
		if(robot != null && tracking != null){ calibration = new Calibration(robot,tracking, measurements); }
		else if(test.equals("test")){ calibration = new Calibration(); }
		else { System.out.println("No Robot or Tracking server, try again don't proceed"); }

	}

	public static void main(String[] args) {
        launch(args);
    }
    
    public void setRobot(Robot robot) {
		this.robot = robot;
	}
    
    public Robot getRobot() {
		return robot;
	}

	public void setTracking(Tracking tracking) {
		this.tracking = tracking;
	}
	
	public Tracking getTracking() { return tracking; }

	
	public Scene getScene() {
		return scene;
	}

	public Calibration getCalibration() {
		return calibration;
	}


}