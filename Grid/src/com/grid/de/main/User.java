package com.grid.de.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class User implements Runnable {
	Socket socket;
	BufferedReader in;
	PrintWriter out;
	Thread thread;
	
	public User(Socket socket) throws IOException {
		this.socket = socket;
		if(socket != null) {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);
			thread = new Thread(this);
			thread.start();
		}else {
			System.err.println("Failed to get the socket..exiting");
		}
	}
	
	public void run() {
		do {
			String line;
			try {
				while((line = in.readLine()) != null) {
					System.out.println(line);
				}
			}catch (IOException e) {
				e.printStackTrace();
			}
		}while(socket.isConnected());
		
		try {
			in.close();
			out.close();
			socket.close();
			thread.join();
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
