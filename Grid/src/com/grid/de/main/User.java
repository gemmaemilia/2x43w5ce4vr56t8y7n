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
	
	String name;
	
	boolean connected = true;
	
	public User(Socket socket) throws IOException {
		this.socket = socket;
		if(socket != null) {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);
			Setup();
			thread = new Thread(this);
			thread.start();
		}else {
			System.err.println("Failed to get the socket..exiting");
		}
	}
	
	public void run() {
		System.out.println("***User \"" + name +"\" has connected***");
		Main.Send("***User \"" + name +"\" has connected***", null);
		/*
		 * Broadcast to the server on who connected
		 * */
		do {
			String line;
			try {
				while((line = in.readLine()) != null) {
					System.out.println("<"+name+">: "+line);
					Main.Send("<"+name+">: "+line, this);
					if(line.contains("<EXIT>")) {
						connected = false;
						break;
					}
				}
			}catch (IOException e) {
				connected = false;
			}
		}while(connected);
		
		System.out.println("***User \""+name+"\" has disconnected***");
		
		try {
			in.close();
			out.close();
			socket.close();
			thread.join();
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Main.RemoveUser(this);
	}
	
	public String getName() {
		return name;
	}
	
	public void Send(String text) {
		out.println(text);
	}
	
	private void Setup() throws IOException {
		String line;
		while((line = in.readLine()) != null) {
			if(line.startsWith("<NAME>")) {
				System.out.println("Found name..Setting");
				name = line.substring(line.indexOf(" ") + 1);
				break;
			}
		}
		out.println("Done");
	}
}
