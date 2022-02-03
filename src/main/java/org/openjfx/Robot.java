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
        Matrix mh  = YN.times(X.inverse());
        SingularValueDecomposition svdm = new SingularValueDecomposition(mh.getMatrix(0, 2, 0, 2));

        Matrix m= svdm.getU().times(svdm.getV().transpose());
        m.print(10, 5);


        sendAndReceive("EnableAlter");
        sendAndReceive("MoveRTHomRowWise " + m.get(0,0) + " " + m.get(0 ,1) + " " + m.get(0,2) + " " + mh.get(0,3)
                + m.get(1,0) + " " + m.get(1 ,1) + " " + m.get(1,2) + " " + mh.get(1,3)
                + m.get(2,0) + " " + m.get(2 ,1) + " " + m.get(2,2) + " " + mh.get(2,3));
        sendAndReceive("DisableAlter");

    }

    public void setSpeed(String speed){
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
     */
    public void endPos() {
        sendAndReceive("MovePTPJoints 0 -150 150 0 0 0");
    }

    private void makeHistory(String input) {
        this.history.add(input);
    }

    public List<String> getHistory() {
        return history;
    }
}
