package com.grap.de.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Main implements Runnable {

	private Socket socket;
	private BufferedReader in;
	private BufferedReader stdIn;
	private PrintWriter out;

	private Scanner key;
	private String name;
	private String ip_port;
	private Thread thread;

	private boolean run = true;

	private int port;
	private String ip;

	Main() throws InterruptedException, IOException {

		key = new Scanner(System.in);

		System.out.print("Enter an ip to connect to and port (ip/port): ");
		ip_port = key.nextLine();

		System.out.print("Enter your name: ");
		name = key.nextLine();
		if (ip_port.contains("/")) {
			ip = ip_port.substring(0, ip_port.indexOf("/"));
			port = Integer.parseInt(ip_port.substring(ip_port.indexOf("/") + 1));
		} else if (ip_port.contains("test")) {
			ip = "localhost";
			port = 9090;
		} else if (ip_port.contains("home")) {
			ip = "73.22.175.131";
			port = 9090;
		}
		System.out.println("Connecting to " + ip + ":" + port);

		socket = new Socket(ip, port); // Attempts to connect to the server
		while (!socket.isConnected()) { // Runs till it's not connected
			try {
				socket = new Socket(ip, port); // Attempts to connect to the server
			} catch (Exception e) {
				System.err.println("Failed to connect..trying again");
				Thread.sleep(2000); // Sleeps the thread to prevent 100% CPU usage
			}
		}
		in = new BufferedReader(new InputStreamReader(socket.getInputStream())); // Gets servers input
		stdIn = new BufferedReader(new InputStreamReader(System.in));
		out = new PrintWriter(socket.getOutputStream(), true); // Sets ability to send back to the server
		System.out.println("Connected! Sending necessary data..");

		out.println("<NAME>: " + name);

		while (!in.readLine().contains("Done")) {
			// Halts the progress of the application
			// *There is definitely a better way, TODO -> Fix this method
		}

		thread = new Thread(this);
		thread.start();

		String line;

		while (run) {
			System.out.println("Running MainThread..");
			

			if (!(line = stdIn.readLine()).isEmpty() && run) {
				out.println(line);
			}
			System.out.println("Running MainThread..");
		}
		// Cleaning up
		thread.join();
		in.close();
		stdIn.close();
		out.close();
		socket.close();
		//////////////
	}

	public void run() {
		do {
			String line;

			System.out.println("Running OutputThread..");
			try {
				if ((line = in.readLine()) != null) {
					System.out.println(line);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				run = false;
				break;
			}
		} while (run);
	}

	public static void main(String[] args) throws InterruptedException, IOException {
		// TODO Auto-generated method stub
		new Main();
	}

}
