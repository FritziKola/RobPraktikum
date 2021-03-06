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
    private final Matrix bausteinPos[] = {new Matrix( new double[][] {{0, 0, -1, -64},
            {0, 1, 0 ,66 }, {1, 0 , 0 , 186}, {0, 0, 0, 1 }}), // erste baustein position
            new Matrix( new double[][] {{0, 0, -1, -64},
            {0, 1, 0 ,66 }, {1, 0 , 0 , 296}, {0, 0, 0, 1 }}), // zweite baustein position
            new Matrix( new double[][] {{0, 0, -1, -62},
                    {0, 1, 0 ,113 }, {1, 0 , 0 , 181}, {0, 0, 0, 1 }}), // dritte baustein position
            new Matrix( new double[][] {{0, 0, -1, -62},
                    {0, 1, 0 ,115 }, {1, 0 , 0 , 289}, {0, 0, 0, 1 }}), // vierte baustein position
            new Matrix( new double[][] {{0, 0, -1, -62},
                    {0, 1, 0 ,161 }, {1, 0 , 0 , 291}, {0, 0, 0, 1 }}),}; // fünfte baustein position
    private final Matrix ueberBausteinen = new Matrix( new double[][] {{0, 0, -1, -120},
            {0, 1, 0 ,165 }, {1, 0 , 0 , 180}, {0, 0, 0, 1 }});
    private Matrix ueberAblagePos[] = {new Matrix(new double[][]{{0.194659, -0.979396, 0.053764, -4.301147},
            {0.975962, 0.187917, -0.110390, -460.177640}, {0.098012, 0.073960, 0.992433, 190.833656}, {0,0,0,1}}),// erste ÜberAblage Pos;
            new Matrix(new double [][] {{0.192111, -0.980889, 0.030821, 50.465723}, {0.980637, 0.190655, -0.044751, -465.428743}, {0.038020, 0.038822, 0.998523, 190.346491}, {0,0,0,1}}),// zweite überAblage Pos
            new Matrix(new double [][] {{0.980889, 0.192111, 0.030821, 30.465720}, {-0.190655, 0.980636, -0.044751, -485.428746}, {-0.038821, 0.038020, 0.998523, 190.346520}, {0,0,0,1}}),// dritte überAblage Pos
            new Matrix(new double [][] {{0.980889, 0.192111, 0.030821, 25.465720}, {-0.190655, 0.980636, -0.044751, -438.428746}, {-0.038821, 0.038020, 0.998523, 190.346520}, {0,0,0,1}}),// vierte überAblage Pos
    		//new Matrix(new double [][] {{ 0.192111, -0.980889, 0.030821, 25.465719},
              //      {0.980636, 0.190655, -0.044751, -438.428750},{0.038020, 0.038821, 0.998523,190.346551}, {0,0,0,1}})// fünfte überAblage Pos TODO: nochmal nach schauen
    };
    private Matrix ablagePos[] = {new Matrix( new double[][]{{0.194659, -0.979396, 0.053764, -4.301147},
            {0.975962, 0.187917, -0.110390, -460.177640}, {0.098012, 0.073960, 0.992433, 80.833656}, {0,0,0,1}}), // erste AblagePos
    		new Matrix(new double [][] {{0.192111, -0.980889, 0.030821, 50.465723}, {0.980637, 0.190655, -0.044751, -465.428743}, {0.038020, 0.038822, 0.998523, 80.346491}, {0,0,0,1}}),// zweite Ablage Pos
    		new Matrix(new double [][] {{0.980889, 0.192111, 0.030821, 30.465720}, {-0.190655, 0.980636, -0.044751, -485.428746}, {-0.038821, 0.038020, 0.998523, 105.346520}, {0,0,0,1}}),// dritte Ablage Pos
    		new Matrix(new double [][] {{0.980889, 0.192111, 0.030821, 25.465720}, {-0.190655, 0.980636, -0.044751, -438.428746}, {-0.038821, 0.038020, 0.998523, 105.346520}, {0,0,0,1}}),// vierte Ablage Pos
    		//new Matrix(new double [][] {{ 0.192111, -0.980889, 0.030821, 25.465719},
              //      {0.980636, 0.190655, -0.044751, -438.428750},{0.038020, 0.038821, 0.998523, 190.346551}, {0,0,0,1}}),// fünfte Ablage Pos TODO: nochmal nachschauen
    };
    private Matrix aktuellePosition;
    private List<String> history;
    private App app;
    private boolean toggleEchtzeit = false;


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

    public void sendHomMatrixHandToggle(Matrix m){
        sendAndReceive("EnableAlter");
        sendAndReceive("MoveRTHomRowWiseStatus " + m.get(0,0) + " " + m.get(0 ,1) + " " + m.get(0,2) + " " + m.get(0,3)
                + m.get(1,0) + " " + m.get(1 ,1) + " " + m.get(1,2) + " " + m.get(1,3)
                + m.get(2,0) + " " + m.get(2 ,1) + " " + m.get(2,2) + " " + m.get(2,3) + " noToggleArm");
        sendAndReceive("DisableAlter");
    }

    public void sendHomMatrixWithoutAlterOnOff(Matrix m){
        sendAndReceive("MoveRTHomRowWiseStatus " + m.get(0,0) + " " + m.get(0 ,1) + " " + m.get(0,2) + " " + m.get(0,3)
                + m.get(1,0) + " " + m.get(1 ,1) + " " + m.get(1,2) + " " + m.get(1,3)
                + m.get(2,0) + " " + m.get(2 ,1) + " " + m.get(2,2) + " " + m.get(2,3) + " noToggleArm noToggleHand");
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


    public Matrix bausteinPos(Matrix N, Matrix X, Matrix Y, int i) {
        mHPositionBerechnen(N, X, Y);
        bausteinPos[i].print(10, 5);
        return hMPosition.times(bausteinPos[i]);
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
        Matrix steinPos;
        int n = 4;
        for(int i= 0; i < n; i++) {
            System.out.println(i);
        	Matrix ueberAblage = ueberAblagePos[i];
        	Matrix ablage = ablagePos[i];
        	app.getTracking().takeMeasurements(); //macht immer Messung am Anfang des Loops
	        mHPositionBerechnen(app.getTracking().getMeasurement(), app.getCalibration().getX(), app.getCalibration().getY());
	        sendHomMatrix(hMPosition.times(ueberBausteinen));
	        steinPos= bausteinPos(app.getTracking().getMeasurement(), app.getCalibration().getX(), app.getCalibration().getY(), i);
            sendHomMatrix(steinPos.times(new Matrix( new double[][] {{1, 0, 0, 0},
                    {0, 1, 0 ,0 }, {0, 0 , 1 , 50}, {0, 0, 0, 1 }})));
            sendHomMatrix(steinPos);
	        ansaugen();
	        sendHomMatrix(steinPos.times(new Matrix( new double[][] {{1, 0, 0, 0},
	                {0, 1, 0 ,0 }, {0, 0 , 1 , 50}, {0, 0, 0, 1 }})));
	        sendHomMatrix(hMPosition.times(ueberBausteinen));
	        sendHomMatrix(ueberAblage);
	        sendHomMatrixHandToggle(ablage);
	        loslassen();
	        sendHomMatrixHandToggle(ueberAblage);
	        sendHomMatrix(hMPosition.times(ueberBausteinen));
            System.out.println(i);
        }
        System.out.println("stack loop ende");
    }

    /**
     * Robot should move over the table while the table is moved beneath him
     */
    public void echtzeit(){
        int sec = 0;
        sendAndReceive("EnableAlter");
        send("GetPositionHomRowWise");
        Matrix momentPos = app.getCalibration().parser(received());
        toggleEchtzeit = true;
        Matrix zielPosition;
        mHPositionBerechnen(app.getTracking().getMeasurement(), app.getCalibration().getX(), app.getCalibration().getY());
        zielPosition = hMPosition.times(ueberBausteinen);
        Matrix normVector = new Matrix(3, 1);
        double wurzel;
        wurzel = Math.sqrt(Math.pow(zielPosition.get(0, 3), 2) + Math.pow(zielPosition.get(1, 3),2) + Math.pow(zielPosition.get(2, 3),2));
        normVector.set(0, 0, (1/wurzel)*zielPosition.get(0,3));
        normVector.set(1, 0, (1/wurzel)*zielPosition.get(1,3));
        normVector.set(2, 0, (1/wurzel)*zielPosition.get(2,3));
        while(toggleEchtzeit){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            sec++;
            app.getTracking().startMeasurementThread();
            if(app.getTracking().measurementChanged){
                mHPositionBerechnen(app.getTracking().getMeasurement(), app.getCalibration().getX(), app.getCalibration().getY());
                zielPosition = hMPosition.times(ueberBausteinen);
                wurzel = Math.sqrt(Math.pow(zielPosition.get(0, 3), 2) + Math.pow(zielPosition.get(1, 3),2) + Math.pow(zielPosition.get(2, 3),2));
                normVector.set(0, 0, (1/wurzel)*zielPosition.get(0,3));
                normVector.set(1, 0, (1/wurzel)*zielPosition.get(1,3));
                normVector.set(2, 0, (1/wurzel)*zielPosition.get(2,3));
                System.out.println("Ziel Pos:");
                zielPosition.print(10,5);
            }
            normVector.print(10,5);
            if(Math.abs(momentPos.get(0, 3) - zielPosition.get(0,3)) > 2 || Math.abs(momentPos.get(1, 3) - zielPosition.get(1,3)) > 2 || Math.abs(momentPos.get(2, 3) - zielPosition.get(2,3)) > 2 ){
                momentPos.set(0, 3, momentPos.get(0,3) + normVector.get(0,0) * 2);
                momentPos.set(1, 3, momentPos.get(1,3) + normVector.get(1,0) * 2);
                momentPos.set(2, 3, momentPos.get(2,3) + normVector.get(2,0) * 2);
                System.out.println("Movment Pos:");
                momentPos.print(10,5);
                sendHomMatrixWithoutAlterOnOff(momentPos);
            }
            // else {toggleEchtzeit = false;}
            if(sec == 100){
                toggleEchtzeit = false;}
        }
        System.out.println("echtzeitEnde");
        sendAndReceive("DisableAlter");
        app.getTracking().getMeasurementThread().stopMeasuring();
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

