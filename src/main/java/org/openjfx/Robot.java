package org.openjfx;


import java.util.ArrayList;
import java.util.List;

import externalThings.Jama.Matrix;
import externalThings.Jama.SingularValueDecomposition;

public class Robot {

    private Client client;
    //private double[][] r = {{0, 0, -1, 0},
    //        {0, 1, 0 ,0 }, {1, 0 , 0 , 0 }, {0, 0, 0, 1 }};
    private Matrix rotation = new Matrix( new double[][] {{0, 0, -1, 0},
            {0, 1, 0 ,0 }, {1, 0 , 0 , 0 }, {0, 0, 0, 1 }} );
    private Matrix hMPosition;
    private Matrix bausteinPos = new Matrix(new double[][] {{0, 0, -1, -70},
            {0, 1, 0 ,65 }, {1, 0 , 0 , 180}, {0, 0, 0, 1 }});
    private Matrix aktuellePosition;
    private List<String> history;
    private App app;
    private List<String> koordinatenAblage; //kann man  Roboterkoordinaten nehmen
    private List<Matrix> koordinatenAufnahme; //über Marker, muss Offset zum Marker noch berechnen

    /**
     * Constructor for Robot
     * Will connect to ad850 automatically
     * @param client
     */
    public Robot(Client client, App app){
        this.client = client;
        this.app = app;
        this.history = new ArrayList<String>();
        System.out.println("Received: " + client.received());
        connectToRobot();
    }

    /**
     * Connect to rob6server.exe ad850 -d -p
     * and set verbosity to 5
     */
    public void connectToRobot(){
        client.sendAndReceive("Hello Robot");
        client.sendAndReceive("SetAdeptSpeed 5");
    }

    public void mHPositionBerechnen(Matrix N, Matrix X, Matrix Y){
        Matrix YN = Y.times(N);
        SingularValueDecomposition svdYN = new SingularValueDecomposition(YN.getMatrix(0, 2, 0, 2));
        YN.setMatrix(0, 2, 0, 2, svdYN.getU().times(svdYN.getV().transpose()));
        hMPosition  = YN.times(X.inverse());
        SingularValueDecomposition svdm = new SingularValueDecomposition(hMPosition.getMatrix(0, 2, 0, 2));

        Matrix m= svdm.getU().times(svdm.getV().transpose());
        hMPosition.setMatrix(0,2,0,2, m);
    }
    /**
     * Moves robot endefektor to point N
     */
    public void moveToPoint(Matrix N, Matrix X, Matrix Y){
        mHPositionBerechnen(N, X, Y);
        aktuellePosition =hMPosition;
        moveToAktuellePosition();
    }

    private void moveToAktuellePosition(){
        sendAndReceive("EnableAlter");
        sendHomMatrix(aktuellePosition);
        sendAndReceive("DisableAlter");
    }

    public void sendHomMatrix(Matrix m){
        sendAndReceive("MoveRTHomRowWise " + m.get(0,0) + " " + m.get(0 ,1) + " " + m.get(0,2) + " " + m.get(0,3)
                + m.get(1,0) + " " + m.get(1 ,1) + " " + m.get(1,2) + " " + m.get(1,3)
                + m.get(2,0) + " " + m.get(2 ,1) + " " + m.get(2,2) + " " + m.get(2,3));
    }

    public void setSpeed(Long speed){
        client.sendAndReceive("SetAdeptSpeed " + speed);
    }

    /**	
     * Send a message to the Robot and receives an answer
     * @param message The message send to the server
     */
    public void sendAndReceive(String message){
        makeHistory(message);
        client.sendAndReceive(message);	
    }

    public void disconnect(){
        client.sendAndReceive("Quit");
    }

    /**
     * Send a message to the robot
     * @param message The message send to the server
     */
    public void send(String message){
        client.send(message);
    }

    /**
     * Receives a message form the robot
     * @return The message from the Robot
     */
    public String received(){
        String answer = client.received();
        System.out.println(answer);
        return answer;
    }

    /**
     * Brings the robot in end position
     * TODO: funktionsfähig im alter und nicht
     */
    public void endPos() {
        sendAndReceive("MovePTPJoints 0 -150 150 0 0 0");
    }
    
    public void ansaugen() {
    	sendAndReceive("DirectAdeptCmd signal 5, -6");
    }
    
    public void loslassen() {
    	sendAndReceive("DirectAdeptCmd signal -5, 6");
    }

    /**
     * Fährt an die Baustein Position des ersten Bausteins
     */
    public void bausteinPos() {
        sendHomMatrix(hMPosition.times(bausteinPos));
    }

    private void makeHistory(String input) {
        this.history.add(input);
    }

    public List<String> getHistory() {
        return history;
    }

    /**
     * als kruze funktion zum testen der rotations teile
     * TODO: schöner machen oder raus nehmen
     */
    public void messageDecoder(String message){
        if(message.contains("Rotation")) {
             // vielleicht nicht nötig
            rotation.print(10 , 5);
            aktuellePosition = aktuellePosition.times(rotation);
            moveToAktuellePosition();
        } else if(message.contains("xPlus")){ // nach oben
            String[] rowWise = message.split(" ");
            rotation.set(0, 3,  rotation.get(0,3) + Double.parseDouble(rowWise[1]));
        }
        else if(message.contains("yPlus")){ // zur Seite
            String[] rowWise = message.split(" ");
            rotation.set(1, 3,  rotation.get(1,3) + Double.parseDouble(rowWise[1]));
        }
        else if(message.contains("zPlus")){ //nach hinten
            String[] rowWise = message.split(" ");
            rotation.set(2, 3,  rotation.get(2,3) + Double.parseDouble(rowWise[1]));
        }
        else if(message.contains("bewegen")){
            moveToAktuellePosition();
        }
        else{ sendAndReceive(message);}

    }
    
    public void stackLoop(){
    	koordinatenAblage = new ArrayList<String>();
    	koordinatenAufnahme = new ArrayList<Matrix>();
    	int n = 3;
    	for(int i = 0; i < n; i++) {
    		if(koordinatenAufnahme.size() <= i)
    			break;
    		Matrix target = koordinatenAufnahme.get(i);
    		
    		moveToPoint(target /* + Offset*/ ,app.getCalibration().getX(), app.getCalibration().getY());
    		ansaugen();
    		//potentiell hochfahren
    		send("MovePTPJoints " + koordinatenAblage.get(i));
    		loslassen();
    		
    	}
    	
    }
    
    public void setRotation(Matrix rotation) {
    	this.rotation = rotation;
    }
    
    public Matrix getRotation() {
    	return rotation;
    }
    
    public void sethmPosition(Matrix hMPosition) {
    	this.hMPosition = hMPosition;
    }
    public Matrix gethMPosition() {
    	return hMPosition;
    }
}	

