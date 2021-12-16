package org.openjfx;

import java.util.Scanner;

public class Tracking {

    private Client client;
	
    public Tracking(Client client) {
		Scanner scanner = new Scanner(System.in);
		this.client = client;     
		connectToTracking();
		client.send("PolarisActive_1");
		System.out.println("Geben Sie Ihren Befehl ein");
		while(true) {
			String userInput = scanner.nextLine();
		    client.send(userInput);
		}
    }
    
    public void connectToTracking(){
    	client.send("CM_GETSYSTEM");
	}
}