package org.openjfx;


import externalThings.Jama.*;
//import externalThings.Jama.Matrix;
//import externalThings.Jama.SingularValueDecomposition;
import javafx.scene.effect.Light;
import java.util.Random;
import java.util.Arrays;

import java.util.ArrayList;
import java.util.List;

public class Robot {

    private Client client;
    private Matrix rotation = Matrix.identity(4,4);
    private Matrix hMPosition;
    private List<String> history;

    /**
     * Constructor for Robot
     * Will connect to ad850 automatically
     * @param client
     */
    public Robot(Client client){
        this.client = client;
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

    /**
     * Moves robot endefektor to point N
     */
    public void moveToPoint(Matrix N, Matrix X, Matrix Y){
        Matrix YN = Y.times(N);
        SingularValueDecomposition svdYN = new SingularValueDecomposition(YN.getMatrix(0, 2, 0, 2));
        YN.setMatrix(0, 2, 0, 2, svdYN.getU().times(svdYN.getV().transpose()));
        hMPosition  = YN.times(X.inverse());
        SingularValueDecomposition svdm = new SingularValueDecomposition(hMPosition.getMatrix(0, 2, 0, 2));

        Matrix m= svdm.getU().times(svdm.getV().transpose());
        hMPosition.setMatrix(0,2,0,2, m);
        sendAndReceive("EnableAlter");
        sendHomMatrix(hMPosition);
        sendAndReceive("DisableAlter");

    }

    private void sendHomMatrix(Matrix m){
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
    
    public void becherPos() {
    	sendAndReceive("MovePTPJoints 2 -45 125 0 6 0");
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
    public void messageDecoder(String massage){
        if(massage.contains("Rotation")) {
            sendAndReceive("EnableAlter");
            String[] rowWise = massage.split(" ");
            int i = 1;
            for(int j= 0; j <3; j++ ){
                for(int l =0; l<3; l++){
                    rotation.set(j, l, Double.parseDouble(rowWise[i]));
                    i ++;
                }
            }
            rotation.print(10 , 5);
            sendHomMatrix(rotation.times(hMPosition));
            sendAndReceive("DisableAlter");
        }
        else{ sendAndReceive(massage);}

    }
}
