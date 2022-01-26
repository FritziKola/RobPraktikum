package org.openjfx;

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
     * TODO: Klasse getToPoint, die ,mit Hilfe einer übergeben Matrix an einem Punkt fährt
     */

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
