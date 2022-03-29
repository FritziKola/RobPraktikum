package org.openjfx;


import java.util.ArrayList;
import java.util.List;

import externalThings.Jama.Matrix;
import externalThings.Jama.SingularValueDecomposition;

public class Robot {

    private Client client;
    private Matrix r = new Matrix( new double[][]{{0, 0, -1, 0},
            {0, 1, 0 ,0 }, {1, 0 , 0 , 0 }, {0, 0, 0, 1 }});
    private Matrix rotation = new Matrix( new double[][] {{0, 0, -1, 0},
            {0, 1, 0 ,0 }, {1, 0 , 0 , 0 }, {0, 0, 0, 1 }} );
    private Matrix hMPosition;
    private final Matrix zwischenPos = new Matrix(new double[][]{{-0.637382, -0.770544, -0.002456, 434.744630}, {0.770528, -0.637340, -0.009174, -363.894951}, {0.005504, -0.007740, 0.999955, 190.619356},{0,0,0,1}});
    private final Matrix bausteinPos[] = {new Matrix( new double[][] {{0, 0, -1, -66},
            {0, 1, 0 ,75 }, {1, 0 , 0 , 180}, {0, 0, 0, 1 }}), // erste baustein position
            new Matrix( new double[][] {{0, 0, -1, -66},
            {0, 1, 0 ,75 }, {1, 0 , 0 , 290}, {0, 0, 0, 1 }}), // zweite baustein position
            new Matrix( new double[][] {{0, 0, -1, -67},
                    {0, 1, 0 ,125 }, {1, 0 , 0 , 180}, {0, 0, 0, 1 }}), // dritte baustein position
            new Matrix( new double[][] {{0, 0, -1, -67},
                    {0, 1, 0 ,125 }, {1, 0 , 0 , 290}, {0, 0, 0, 1 }}), // vierte baustein position
            new Matrix( new double[][] {{0, 0, -1, -67},
                    {0, 1, 0 ,125 }, {1, 0 , 0 , 290}, {0, 0, 0, 1 }}),}; // fünfte baustein position
    private final Matrix ueberBausteinen = new Matrix( new double[][] {{0, 0, -1, -120},
            {0, 1, 0 ,165 }, {1, 0 , 0 , 180}, {0, 0, 0, 1 }});
    private Matrix ueberAblagePos = new Matrix(new double[][]{{0.194659, -0.979396, 0.053764, -4.301147},
            {0.975962, 0.187917, -0.110390, -460.177640}, {0.098012, 0.073960, 0.992433, 190.833656}, {0,0,0,1}});
    private Matrix ablagePos = new Matrix( new double[][]{{0.194659, -0.979396, 0.053764, -4.301147},
            {0.975962, 0.187917, -0.110390, -460.177640}, {0.098012, 0.073960, 0.992433, 80.833656}, {0,0,0,1}});
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
        sendHomMatrix(aktuellePosition);
    }

    public void sendHomMatrix(Matrix m){
        sendAndReceive("EnableAlter");
        sendAndReceive("MoveRTHomRowWiseStatus " + m.get(0,0) + " " + m.get(0 ,1) + " " + m.get(0,2) + " " + m.get(0,3)
                + m.get(1,0) + " " + m.get(1 ,1) + " " + m.get(1,2) + " " + m.get(1,3)
                + m.get(2,0) + " " + m.get(2 ,1) + " " + m.get(2,2) + " " + m.get(2,3) + " noToggleArm noToggleHand");
        sendAndReceive("DisableAlter");
    }

    public void sendHomMatrixNoToggel(Matrix m){
        sendAndReceive("EnableAlter");
        sendAndReceive("MoveRTHomRowWiseStatus " + m.get(0,0) + " " + m.get(0 ,1) + " " + m.get(0,2) + " " + m.get(0,3)
                + m.get(1,0) + " " + m.get(1 ,1) + " " + m.get(1,2) + " " + m.get(1,3)
                + m.get(2,0) + " " + m.get(2 ,1) + " " + m.get(2,2) + " " + m.get(2,3) + " noToggleArm" );
        sendAndReceive("DisableAlter");
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
    public void bausteinPos(Matrix N, Matrix X, Matrix Y) {
        mHPositionBerechnen(N, X, Y);
        bausteinPos[1].print(10, 5);
        aktuellePosition=hMPosition.times(bausteinPos[1]);
        sendHomMatrix(aktuellePosition);
    }
    public void bausteinPos(Matrix N, Matrix X, Matrix Y, int i) {
        mHPositionBerechnen(N, X, Y);
        bausteinPos[i].print(10, 5);
        aktuellePosition=hMPosition.times(bausteinPos[i]);
        sendHomMatrix(aktuellePosition);
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
            String[] rowWise = message.split(" ");
            int i = 1;
            for(int j= 0; j <3; j++ ){
                for(int l =0; l<3; l++){
                    r.set(j, l, Double.parseDouble(rowWise[i]));
                    i ++;
                }
            }
            // vielleicht nicht nötig
            r.print(10, 5);
            aktuellePosition = aktuellePosition.times(r);
            moveToAktuellePosition();
        }
        else if(message.contains("xPlus")){ // nach oben
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
        System.out.println("Stackloop begint");
    	//app.getTracking().startMeasurementThread();
    	// TODO loop for moving step by step
        mHPositionBerechnen(app.getTracking().getMeasurement(), app.getCalibration().getX(), app.getCalibration().getY());
        sendHomMatrix(hMPosition.times(ueberBausteinen));
        bausteinPos(app.getTracking().getMeasurement(), app.getCalibration().getX(), app.getCalibration().getY());
        ansaugen();
        sendHomMatrix(aktuellePosition.times(new Matrix( new double[][] {{1, 0, 0, 0},
                {0, 1, 0 ,0 }, {0, 0 , 1 , 50}, {0, 0, 0, 1 }})));
        sendHomMatrix(hMPosition.times(ueberBausteinen));
        sendHomMatrix(ueberAblagePos);
        sendHomMatrix(ablagePos);
        loslassen();
        sendHomMatrix(ueberAblagePos);
        sendHomMatrix(hMPosition.times(ueberBausteinen));
        //app.getTracking().getMeasurementThread().stopMeasuring();
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

