package networking.project.game;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;

import networking.project.game.entities.creatures.Player;

public class Server implements Runnable{

	private Thread thread;
	private boolean running = false;
	private final int number_of_players;
	private ArrayList<Player> players = new ArrayList<Player>();
	
	public Server(int numPlayers){
		this.number_of_players = numPlayers;
	}
	public void startServer(){
		System.out.println("Server started");
		this.start();
	}

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
		DatagramSocket server_socket = null;
		
		try {
			server_socket = new DatagramSocket(7777);
			
			byte[] buffer = new byte[65536];
			DatagramPacket incoming = new DatagramPacket(buffer, buffer.length);
			
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
				
				// Get client data
				server_socket.receive(incoming);
				
				
				byte[] data = incoming.getData();
				String s = new String(data, 0, incoming.getLength());
				
				// Do stuff with client data
				s = "echo from the server: " + s;
				// Return data to client
				DatagramPacket dp = new DatagramPacket(s.getBytes(), s.getBytes().length , incoming.getAddress(), incoming.getPort());
				server_socket.send(dp);
			}
			
			stop();
			
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
