package org.openjfx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * JavaFX App, Gui for Robot Gui
 */
public class App extends Application {

	private Robot robot;
	private Tracking tracking;
	private Scene scene;
	public Client client;
	private Calibration calibration;
	//private Long sliderValue = null;
	private Gui gui;
	
    @Override
    public void start(Stage stage) {
       
		scene = new Scene(new StackPane(), 1200, 900);
		scene.getStylesheets().add(getClass().getResource("css/style.css").toExternalForm());
		stage.setResizable(false);
		stage.setScene(scene); 
		gui = new Gui(this); 
		stage.show();
		// Testmatrix
		/*double[][] t = {{1, 2, 3, 1},
				{4, 5, 6, 2},
				{7, 8, 9, 3},
				{0, 0 ,0 , 1}};
		Matrix test = new Matrix(t);
		invertHM(test);*/
        // Testdaten Marker (Splicer):
        // Splicer.returnMatrix("1639716756.780680 y 0.05546889 0.04634489 0.99738426 -234.14794922 -0.07443932 0.99633410 -0.04215620 -186.64950562 -0.99568167 -0.07190625 0.05871543 -1985.50878906 0.147736");
    }
   


	/**
	 * TODO: schöner machen!
	 * @param measurements
	 * @param test
	 */
	public void createCalibration(int measurements, String test){
		if(test.equals("test")){
			calibration = new Calibration();
		}
		else if(robot != null && tracking != null){
			calibration = new Calibration(robot,tracking, measurements);
		}
		else {
			System.out.println("No Robot or Tracking server, try again don't proceed");
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
	
	public void setCalibration(Calibration calibration) {
		this.calibration = calibration;
	}
	
	public Calibration getCalibration() {
		return calibration;
	}
	
	public void setClient(Client client) {
		this.client = client;
	}
	
	public Client getClient() {
		return client;
	}
	/*public Long getSliderValue() {
		return sliderValue;
	}
    
    public void setSliderValue(Long sliderValue) {
		this.sliderValue = sliderValue;
	}

	 */
}