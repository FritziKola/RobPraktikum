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
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
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
	
    @Override
    public void start(Stage stage) {
        scene = new Scene(new StackPane(),717, 600);
        stage.setResizable(false);
        stage.setScene(scene);
        
        bh = new ButtonHandler(this);
        
        
        initMainMenu();
        
        initRobotInput();
        
        initTrackingInput();
        
        stage.show();
        
        // Testdaten Marker (Splicer):
         Splicer.returnMatrix("1639716756.780680 y 0.05546889 0.04634489 0.99738426 -234.14794922 -0.07443932 0.99633410 -0.04215620 -186.64950562 -0.99568167 -0.07190625 0.05871543 -1985.50878906 0.147736");
    }
   
    private void initRobotInput() {
    	StackPane root = (StackPane) scene.getRoot();
    	Label eingabe = new Label("Eingabe:");
		TextField befehl = new TextField();
		befehl.setId("rBefehl");
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
	}
    
    private void initTrackingInput() {
     	StackPane root = (StackPane) scene.getRoot();
    	Label eingabe = new Label("Eingabe:");
		TextField befehl = new TextField();
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
		
		Text ausgabetext = new Text();
		ausgabetext.setId("tAusgabe");
		ScrollPane ausgabe = new ScrollPane(ausgabetext);
		ausgabe.setId("ausgabePan");
		
		grid.add(ausgabe, 0 ,0);
		grid.setId("tEingabeBox");
		grid.add(hb, 0, 1);
		//grid.setSpacing(10);
		hb.getChildren().add(eingabe);
		hb.getChildren().add(befehl);
		hb.getChildren().add(backToMmButton);
		
		befehl.setPrefSize(500, 50);
		grid.setVisible(false);
		root.getChildren().add(grid);
	}
    

	private void initMainMenu() {
    	StackPane root = (StackPane) scene.getRoot();
        root.setBackground(new Background(new BackgroundFill(Color.ROSYBROWN, null, null)));
        root.setPrefWidth(scene.getWidth());
        root.setPrefHeight(scene.getHeight());
        FlowPane mainMenuButtons = new FlowPane(Orientation.VERTICAL, 20, 40);
        mainMenuButtons.setId("mmButtons");
        mainMenuButtons.setBackground(new Background(new BackgroundFill(Color.ROSYBROWN, null, null)));
        mainMenuButtons.setMaxHeight(400);
        mainMenuButtons.setMaxWidth(200);
        root.getChildren().add(mainMenuButtons);
        root.setAlignment(Pos.CENTER);
        
        Button rServerButton = new Button("RobServer verbinden");
        rServerButton.setId("rServerConnect");
        mainMenuButtons.getChildren().add(rServerButton);
        Button tServerButton = new Button("TrackingServer verbinden");
        tServerButton.setId("tServerConnect");
        mainMenuButtons.getChildren().add(tServerButton);
        Button rbefehlseingabe = new Button("RobServer Befehlseingabe"); 
        rbefehlseingabe.setId("rbefehlseingabe");
        mainMenuButtons.getChildren().add(rbefehlseingabe);
        Button tbefehlseingabe = new Button("TrackingServer Befehlseingabe"); 
        tbefehlseingabe.setId("tbefehlseingabe");
        mainMenuButtons.getChildren().add(tbefehlseingabe);
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
	
	public Tracking getTracking() {
		return tracking;
	}
	
	public void setScene(Scene scene) {
		this.scene = scene;
	}
	
	public Scene getScene() {
		return scene;
	}


}