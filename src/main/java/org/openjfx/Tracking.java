package org.openjfx;

public class Tracking {

    private Client client;
	
    public Tracking(Client client) {
		this.client = client;     
		connectToTracking();
		client.send("PolarisActive_1");
    }
    
    public void connectToTracking(){
    	client.send("CM_GETSYSTEM");
	}
    
    public void send(String message){
        client.send(message);
    }
}