package org.openjfx;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import javafx.scene.control.TextArea;


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
     * Sends a message to the robot or tracking server and prints out the answer of the server
     * @param message The message you want to sent to the server
     */
    public void sendAndReceive(String message) {
        send(message);
        System.out.println("Received: " + received());
//        if (message.equals("CM_NEXTVALUE") || message.equals("GetPositionHomRowWise")) {
//            Splicer.returnMatrix(answer);
//        }
    }

    /**
     * Sends a message to the robot or tracking server
     * @param message The message you want to sent to the server
     */
    public void send(String message) {
        System.out.println("Sending: "+ message);
        writer.println(message);
        TextArea sent = port == 5005 ? (TextArea) app.getScene().lookup("#rAusgabe") : (TextArea) app.getScene().lookup("#tAusgabe");
        sent.setText(sent.getText() + "\n" + message);
    }
    



    /**
     * Wating from an answer from the server
     * @return The answer from the server
     */
    public String received(){
        String answer = null;
        try {
            answer = reader.readLine();
            TextArea receive = port == 5005 ? (TextArea) app.getScene().lookup("#rAusgabe") : (TextArea) app.getScene().lookup("#tAusgabe");
            receive.setText(receive.getText() + "\n" + answer + "\n");
            receive.setScrollTop(Double.MAX_VALUE);
        } catch(Exception e){
            System.out.println("received methode didn't work.");
        }
        return answer;
    }



}
