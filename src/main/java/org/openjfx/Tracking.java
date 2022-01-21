package org.openjfx;

public class Tracking {

    private Client client;

    /**
     * Constructor for the Tracking connection
     * @param client an which to connect to
     */
    public Tracking(Client client) {
		this.client = client;     
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



}