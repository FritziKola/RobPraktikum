package org.openjfx;

import java.util.ArrayList;
import java.util.List;

import externalThings.Jama.Matrix;

public class Tracking {

    private Client client;
    private List<String> history;
    public Matrix measurement;
	private App app;
	private MeasurementThread measurementThread;

    /**
     * Constructor for the Tracking connection
     * @param client an which to connect to
     */
    public Tracking(Client client, App app) {
        this.client = client;
        this.app = app;
        this.history = new ArrayList<String>();
        connectToTracking();
    }

    /**
     * sets parameters needed by us
     */
    public void connectToTracking(){
        client.sendAndReceive("CM_GETSYSTEM");
//        client.sendAndReceive("PolarisActive_1");
//        client.sendAndReceive("FORMAT_MATRIXROWWISE");
    }


    /**
     * Starts a thread for taking measurements. 
     */
    public void startMeasurementThread(){
    	measurementThread = new MeasurementThread(app);
        measurementThread.start();
    }
    
    /**
     * Takes measurements
     */
    public void takeMeasurements() {
    	System.out.println("");
    	client.send("CM_NEXTVALUE");
        setMeasurement(app.getCalibration().parser(client.received()));
    }


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
    
    public void setMeasurement(Matrix measurement) {
    	this.measurement = measurement;
    }

    public Matrix getMeasurement() { return measurement; }

    public Client getClient() { return client; }

    public MeasurementThread getMeasurementThread() {
		return measurementThread;
	}

}