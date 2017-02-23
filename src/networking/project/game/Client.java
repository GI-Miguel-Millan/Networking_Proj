package networking.project.game;

import java.io.*;
import java.util.*;

import networking.project.game.entities.creatures.Player;

import java.net.*;

public class Client implements Runnable {
	private Thread thread;
	private boolean running = false;
	private Game game = null;
	private boolean sendData = false;
	
	private void tick(){
		System.out.println("hi");
	}
	
	@Override
	public void run() {
		int fps = 60;  // How many times every second we want to run tick() and render()
		double timePerTick = 1000000000 / fps;
		double delta = 0;
		long now;
		long lastTime = System.nanoTime();
		
		DatagramSocket client_socket = null;
		int port = 7777;
		BufferedReader cin = new BufferedReader(new InputStreamReader(System.in));
		
		try {
			client_socket = new DatagramSocket();
			
			System.out.println("Enter the ip address of the server.");
			
			InetAddress host = InetAddress.getByName((String)cin.readLine());
			
			String cmd = "init";
			byte[] b = cmd.getBytes();
			
			DatagramPacket dp = new DatagramPacket(b, b.length, host, port);
			client_socket.send(dp);
			
			while(running){
				now = System.nanoTime();
				delta += (now - lastTime) / timePerTick;
				lastTime = now;
				
				if(delta >= 1){
						tick();
					delta--;
				}
				// Get data from server
				byte[] buffer = new byte[1460];
				DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
				client_socket.receive(reply);
				
				byte[] data = reply.getData();
				String ans = new String(data, 0, reply.getLength());
				
				cmd = evaluateData(ans);
				if(sendData){
					
				}
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
	
	private String evaluateData(String messageFromServer) throws NumberFormatException, UnknownHostException{
		String[] commands = {"wait", "start"};
		
		System.out.println(messageFromServer);
		if (messageFromServer.contains(commands[0])){
			sendData = false;
			return null;
		}else if(messageFromServer.contains(commands[1])){
			// gameVars format: "start title width height number_of_players P1_address P1_port P2_address P2_port P3_address P3_port P4_address P4_port"
			String[] gameVars = messageFromServer.split("\\s");
			game = new Game(gameVars[1], Integer.parseInt(gameVars[2]), Integer.parseInt(gameVars[3]));
			
			int numP = Integer.parseInt(gameVars[4]);
			System.out.println(numP);
			int j = 5;
			for (int i =0; i < numP; i++){
				System.out.println(j);
				int port = Integer.parseInt(gameVars[j+1]);
				game.getHandler().getPlayers().add(new Player(game.getHandler(), 0, 0, InetAddress.getByName(gameVars[j]), port));
				System.out.println(j);
				j+=2;
			}
			
			game.start();
			sendData = true;
			
			return game.getPlayerInput();
		}
		
		return null;
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
