package com.grap.de.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Main {

	private Socket socket;
	private BufferedReader in;
	private PrintWriter out;
	
	Main() throws InterruptedException, IOException{
		socket = new Socket("localhost", 9090); // Attempts to connect to the server
		while(!socket.isConnected()) { // Runs till it'ss not connected
			try {
				socket = new Socket("localhost", 9090); // Attempts to connect to the server
			} catch (Exception e) {
				System.err.println("Failed to connect..trying again");
				Thread.sleep(2000); // Sleeps the thread to prevent 100% CPU usage
			}
		}
		in = new BufferedReader(new InputStreamReader(socket.getInputStream())); // Gets servers input
		out = new PrintWriter(socket.getOutputStream(), true); // Sets ability to send back to the server
		
		out.println("Hello World"); // Sends a message to the server
		
		// Cleaning up
		in.close();
		out.close();
		socket.close();
		//////////////
	}
	
	public static void main(String[] args) throws InterruptedException, IOException {
		// TODO Auto-generated method stub
		new Main();
	}

}
