package org.openjfx;

import java.util.ArrayList;
import java.util.List;

public class Tracking {

    private Client client;
    private List<String> history;

    /**
     * Constructor for the Tracking connection
     * @param client an which to connect to
     */
    public Tracking(Client client) {
		this.client = client;
		this.history = new ArrayList<String>();
		connectToTracking();
    }

    /**
     * sets parameters needed by us
     */
    public void connectToTracking(){
    	client.sendAndReceive("CM_GETSYSTEM");
    	client.sendAndReceive("PolarisActive_1");
        client.sendAndReceive("FORMAT_MATRIXROWWISE");
	}


    /**
     * TODO: Klasse takeMeasurement die eine Messung vor nimmt
     */


    /**
     * Send a message to the Tracking server and receives an answer
     * @param message The message send to the server
     */
    public void sendAndReceive(String message){
    	makeHistory(message);
        client.sendAndReceive(message);
    }

    /**
     * Send a message to the Tracking server
     * @param message The message send to the server
     */
    public void send(String message){
        client.send(message);
    }

    public void disconnect() {client.sendAndReceive("CM_QUITCONNECTION");}


    /**
     * Receives a message form the Tracking server
     * @return The message from the Tracking server
     */
    public String received(){
        return client.received();
    }
    
    private void makeHistory(String input) {
    	this.history.add(input);
    }
    
    public List<String> getHistory() {
		return history;
	}



}