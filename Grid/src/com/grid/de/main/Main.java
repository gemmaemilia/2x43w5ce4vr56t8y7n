package com.grid.de.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.util.ArrayList;

public class Main implements Runnable {

	ServerSocket serverSocket;

	static ArrayList<User> users = new ArrayList<User>(); // All connected users

	Thread thread;
	Thread window;

	BufferedReader in;

	private boolean serverStatus = true; // True for on, false for off

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new Main();
	}

	private Main() {
		try {
			serverSocket = new ServerSocket(9090); // Calls ServerSocket constructor and binds it to a port

			thread = new Thread(this); // Creates a new thread pointing to this class with a run() function
			thread.start(); // Starts the thread

			in = new BufferedReader(new InputStreamReader(System.in)); // Reads console input

			while (serverStatus) { // Command line thread (this)
				System.out.print("> ");
				String line = in.readLine(); // Reads typed text till \n
				if (line.contains("exit()")) // Searches for exit() command
				{
					serverStatus = false; // Turns off the server
				}
				if (line.startsWith("SendAll(") && line.endsWith(")")) // Broadcasts the message to all users
				{
					Send("<Server>: " + line.substring(line.indexOf("(") + 1, line.indexOf(")")), null);
				} else if (line.startsWith("SendAll(") && !line.endsWith(")")) // Gives an error for bad syntax for the
																				// command
				{
					System.err.println("Bad syntax. Usage: SendAll(text>");
				}
				if (line.contains("ShowUsers")) {
					if (users.isEmpty()) {
						System.out.println("No users connected!");
					} else {
						for (int i = 0; i < users.size(); i++) {
							System.out.println("User #" + i + ": <" + users.get(i).getName() + ">");
						}
					}
				}
				Thread.sleep(15);
			}

			thread.join(); // Waits for the thread to finish then joins it to main

			in.close(); // Closes Buffered Reader to release memory
			serverSocket.close(); // Closes server to release memory
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void RemoveUser(User user) {
		users.remove(user);
	}

	public static void Send(String text, User user) {
		for (int i = 0; i < users.size(); i++) {
			if (user != null && !users.get(i).equals(user)) {
				users.get(i).Send(text);
			} else {
				users.get(i).Send(text);
			}
		}
	}

	public void run() {
		while (serverStatus) { // User accept thread (called with thread.start())
			try {
				serverSocket.setSoTimeout(2000); // Sets timeout for accepting users
				users.add(new User(serverSocket.accept())); // Tries to accept any incoming connections
			} catch (IOException e) {
				/*
				 * Prints an error for SoTimeout(), ignore for now since the while loop runs
				 * till the server closes.
				 */
			}
		}
	}
}
