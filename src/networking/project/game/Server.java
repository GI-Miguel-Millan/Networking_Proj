package networking.project.game;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;

import networking.project.game.entities.creatures.Player;

public class Server implements Runnable{

	private Thread thread;
	private boolean running = false;
	private boolean gameStarted = false;
	private final int number_of_players;
//	private int GAMEWIDTH = 640;
//	private int GAMEHEIGHT = 480;
	private int GAMEWIDTH = 1080;
	private int GAMEHEIGHT = 720;
	private Game game = new Game("Battle Arena", GAMEWIDTH, GAMEHEIGHT);
	private int ids = 0;
	
	public Server(int numPlayers){
		this.number_of_players = numPlayers;
	}
	public void startServer(){
		System.out.println("Server started");
		this.start();
	}

	private void tick(){
		game.tick();
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
				
				if(delta >= 1 && gameStarted){
						tick();
					ticks++;
					delta--;
				}
				
				
				// Get client data
				server_socket.receive(incoming);
				byte[] data = incoming.getData();
				String s = new String(data, 0, incoming.getLength());
				
				// Do stuff with client data
				evaluateCommand(s, incoming, server_socket);
				// Return data to client
				//DatagramPacket dp = new DatagramPacket(s.getBytes(), s.getBytes().length , incoming.getAddress(), incoming.getPort());
				//server_socket.send(dp);
			}
			
			stop();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * This method will evaluate the command sent from the client to
	 * determine which actions the server should take.
	 * @throws IOException 
	 */
	private void evaluateCommand(String str, DatagramPacket clientDatagram, DatagramSocket serverSocket) throws IOException{
		String[] commands = {"init", "input"};
		
		if(str.contains(commands[0])){
			this.ids +=1;					// add new player, must increment ids
			this.game.getHandler().getPlayers().add(new Player(game.getHandler(),0,0,clientDatagram.getAddress(), clientDatagram.getPort(), this.ids));
			
			//Let the client the id of its own player.
			String t = "identity " + this.ids;
			serverSocket.send(new DatagramPacket(t.getBytes(), t.getBytes().length, clientDatagram.getAddress(), clientDatagram.getPort()));
			
			if (game.getHandler().getPlayers().size() == this.number_of_players){
				String s = "start Battle_Arena " + GAMEWIDTH + " " + GAMEHEIGHT + " " + this.number_of_players;
				for (Player p2: game.getHandler().getPlayers()){
					s = s + " " + p2.getIP().getHostAddress() + " " + p2.getPort() + " " + p2.getID();
				}
				
				for (Player p: game.getHandler().getPlayers()){
					serverSocket.send(new DatagramPacket(s.getBytes(), s.getBytes().length, p.getIP(), p.getPort()));
					
					game.init();		// initialze the game
					gameStarted = true;	// start the game (allow it to tick)
				}
				game.getHandler().setClientPlayer(this.ids);
			}else{
				String s = "waiting for players";
				serverSocket.send(new DatagramPacket(s.getBytes(), s.getBytes().length, clientDatagram.getAddress(), clientDatagram.getPort()));
			}
		}else if (str.contains(commands[1])){
			// inputs format: "input up down left right attack clientID"
			String[] inputs = str.split("\\s");
			game.getHandler().getPlayer(Integer.parseInt(inputs[6])).applyInput(
						Integer.parseInt(inputs[1]), Integer.parseInt(inputs[2]), Integer.parseInt(inputs[3]), Integer.parseInt(inputs[4]), Integer.parseInt(inputs[5]));
			
			String s = "update";
			for (Player p2: game.getHandler().getPlayers()){
				//System.out.println("Server side player " + p2);
				s = s + " " + p2.getIP().getHostAddress() + " " + p2.getPort() + " " + p2.getHealth() + " " + (int)p2.getX() + " " + (int)p2.getY();
			}
			serverSocket.send(new DatagramPacket(s.getBytes(), s.getBytes().length, clientDatagram.getAddress(), clientDatagram.getPort()));
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
