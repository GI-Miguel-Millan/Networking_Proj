package networking.project.game;

import java.io.*;
import java.util.*;
import java.net.*;

public class Client implements Runnable {
	private Thread thread;
	private boolean running = false;
	
	private void tick(){
			
	}
	
	@Override
	public void run() {
		int fps = 60;  // How many times every second we want to run tick() and render()
		double timePerTick = 1000000000 / fps;
		double delta = 0;
		long now;
		long lastTime = System.nanoTime();
		long timer = 0;
		int ticks = 0;
		
		DatagramSocket client_socket = null;
		int port = 7777;
		BufferedReader cin = new BufferedReader(new InputStreamReader(System.in));
		
		try {
			client_socket = new DatagramSocket();
			
			System.out.println("Enter the ip address of the server.");
			
			InetAddress host = InetAddress.getByName((String)cin.readLine());
			
			while(running){
				now = System.nanoTime();
				delta += (now - lastTime) / timePerTick;
				timer += now - lastTime;
				lastTime = now;
				
				if(delta >= 1){
						tick();
					ticks++;
					delta--;
				}
				
				// Send data to the server
				System.out.println("enter test text:");
				String s = (String)cin.readLine();
				byte[] b = s.getBytes();
				
				DatagramPacket dp = new DatagramPacket(b, b.length, host, port);
				client_socket.send(dp);
				
				// Get data from server
				byte[] buffer = new byte[65536];
				DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
				client_socket.receive(reply);
				
				byte[] data = reply.getData();
				s = new String(data, 0, reply.getLength());
				
				System.out.println(s);
			}
			stop();
			
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownHostException e) {
			System.out.println("There was an error with the IP Address");
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 *  Starts the thread which runs the game,
	 *  if it is not already running
	 */
	public synchronized void start(){
		if(running)
			return;
		running = true;
		thread = new Thread(this);
		thread.start();
	}
	
	/**
	 *  Joins the game thread, stopping the game,
	 *  if it is already running.
	 */
	public synchronized void stop(){
		if(!running)
			return;
		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
