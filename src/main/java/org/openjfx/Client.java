package org.openjfx;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import javafx.scene.paint.Color;
import javafx.scene.text.Text;


public class Client {
	
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;
    private App app;
	private int port;

    /**
     * This constructor for a simple client only in a local network
     * to connect to the robot or tracking server
     * @param port The port to connect with
     */
    public Client(int port, App app) {
    	this.port = port;
    	this.app = app;
        try {
            socket = new Socket("localhost",port);
            InputStream input = socket.getInputStream();
            reader = new BufferedReader(new InputStreamReader(input));
            OutputStream output = socket.getOutputStream();
            writer = new PrintWriter(output, true);
        } catch (IOException e) {
            System.out.println("Client wasn't created.");
        }

    }

    /**
     * Sends a message to the robot or tracking server
     * @param message The message you want to sent to the server
     */
    public void send(String message) {

        System.out.println("Sending: "+ message);
        writer.println(message);
        Text sended = port == 5005 ? (Text) app.getScene().lookup("#rAusgabe") : (Text) app.getScene().lookup("#tAusgabe");
        sended.setText(sended.getText() + "\n" + message);
        sended.setFill(Color.MEDIUMPURPLE);
        received();
    }

    /**
     * Wating from an answer from the server
     * @return The answer from the server
     */
    public void received(){
        String answer = null;
        try {
            answer = reader.readLine();
            Text receive = port == 5005 ? (Text) app.getScene().lookup("#rAusgabe") : (Text) app.getScene().lookup("#tAusgabe");
            receive.setText(receive.getText() + "\n" + answer);
            receive.setFill(Color.DARKSLATEGREY);
        } catch(Exception e){
            System.out.println("received methode didn't work.");
        }
        System.out.println("Received: " + answer);
    }



}
