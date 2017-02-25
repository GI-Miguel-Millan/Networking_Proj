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
	private int playerID; 
	
	private void tick(){
		game.getGameCamera().centerOnEntity(game.getHandler().getClientPlayer());
		//game.getGameCamera().centerOnCursor();
		game.render();
		game.getKeyManager().tick();
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
			
			InetAddress host;
			
			try{
				host = InetAddress.getByName((String)cin.readLine());
			} catch (UnknownHostException e) {
				host = InetAddress.getByName("localhost"); 	// default to localhost
				e.printStackTrace();
			}
			
			
			String cmd = "init";
			byte[] b = cmd.getBytes();
			
			DatagramPacket dp = new DatagramPacket(b, b.length, host, port);
			client_socket.send(dp);
			
			while(running){
				now = System.nanoTime();
				delta += (now - lastTime) / timePerTick;
				lastTime = now;
				
				if(delta >= 1 && sendData){
						tick();
					delta--;
				}
				// Get data from server
				byte[] buffer = new byte[1460];
				DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
				client_socket.receive(reply);
				
				byte[] data = reply.getData();
				String ans = new String(data, 0, reply.getLength());
				
				evaluateData(ans);
				if(sendData){
					//System.out.println("Message to server: " + game.getPlayerInput());
					b = game.getPlayerInput().getBytes();
					dp = new DatagramPacket(b, b.length, host, port);
					client_socket.send(dp);
				}
			}
			stop();
			
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void evaluateData(String messageFromServer) throws NumberFormatException, UnknownHostException{
		String[] commands = {"wait", "start", "identity", "update"};
		
		//System.out.println("Message From Server: " + messageFromServer);
		
		if (messageFromServer.contains(commands[0])){			// client waits for other clients to connect to server
			sendData = false;
		}else if(messageFromServer.contains(commands[1])){		// Client starts the game
			// gameVars format: "start title width height number_of_players P1_address P1_port P1_ID --- up to P4"
			String[] gameVars = messageFromServer.split("\\s");
			game = new Game(gameVars[1], Integer.parseInt(gameVars[2]), Integer.parseInt(gameVars[3]));
			game.init(); // must initialize assets early
			
			int numP = Integer.parseInt(gameVars[4]);
			int j = 5;
			for (int i =0; i < numP; i++){
				int port = Integer.parseInt(gameVars[j+1]);
				int id = Integer.parseInt(gameVars[j+2]);
				game.getHandler().getPlayers().add(new Player(game.getHandler(), 0, 0, InetAddress.getByName(gameVars[j]), port, id));
				j+=3;
			}
			game.getHandler().setClientPlayer(playerID);
			
			game.getGameState().displayState();
			//game.start();
			sendData = true;
			
		}else if(messageFromServer.contains(commands[2])){		// identify the clients IP and port as seen by the server.
			// info format: "identity playerID"
			String[] info = messageFromServer.split("\\s");
			playerID = Integer.parseInt(info[1]);
		}else if (messageFromServer.contains(commands[3])){		// update position and health of players
			// info format: "update P1_address P1_Port P1_health P1_X P1_Y .... "
			String[] info = messageFromServer.split("\\s");
			int num_entities = (info.length - 1)/5;
			
			//System.out.println(game.getHandler().getClientPlayer().toString());
			int j = 1;
			for (int i =0; i < num_entities; i++){
				InetAddress ip = InetAddress.getByName(info[j]);
				int port = Integer.parseInt(info[j+1]);
				int hp = Integer.parseInt(info[j+2]);
				int xPos = Integer.parseInt(info[j+3]);
				int yPos = Integer.parseInt(info[j+4]);
				
				Player current = game.getHandler().getPlayer(ip, port);
				
				current.setHealth(hp);
				current.setX(xPos);
				current.setY(yPos);
				
				j+=5;
			}
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
