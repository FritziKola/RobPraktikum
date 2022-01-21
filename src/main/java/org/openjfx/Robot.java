package org.openjfx;

public class Robot {
	
    private Client client;

    /**
     * Constructor for Robot
     * Will connect to ad850 automatically
     * @param client
     */
    public Robot(Client client){
        this.client = client;
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
    	client.sendAndReceive("MovePTPJoints 0 -150 150 0 0 0");
    }
}
