package org.openjfx;

public class Robot {
	
    private Client client;

    public Robot(Client client){
        this.client = client;
        client.received();
        connectToRobot();
    }

    /**
     * rob6server.exe ad850 -d -p
     */
    public void connectToRobot(){
        client.send("Hello Robot");
        client.send("SetVerbosity 5");
    }

    public void setSpeed(String speed){
        client.send("SetAdeptSpeed " + speed);
    }

    public void send(String message){
        client.send(message);
    }

    public void disconnect(){
        client.send("Quit");
    }

}
