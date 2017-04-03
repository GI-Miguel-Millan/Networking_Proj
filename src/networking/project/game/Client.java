package networking.project.game;

import networking.project.game.entities.creatures.Player;
import networking.project.game.entities.creatures.projectiles.Projectile;
import networking.project.game.gfx.GameCamera;
import networking.project.game.network.packets.ConnectionPacket;
import networking.project.game.network.packets.GameStartPacket;
import networking.project.game.network.packets.Packet;
import networking.project.game.network.packets.PlayerUpdatePacket;
import networking.project.game.utils.NetCodes;
import networking.project.game.utils.Utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.*;

public class Client implements Runnable, NetCodes {
	private Thread thread;
	private InputThread inThread;
	private boolean running = false;
	private Game game = null;
	private boolean sendData = false;
	private int playerID; 
	
	private void tick(){
		game.getGameCamera().centerOnEntity(game.getHandler().getClientPlayer());
		//game.getGameCamera().centerOnCursor();
        game.tick();
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
				host = InetAddress.getByName(cin.readLine());
			} catch (UnknownHostException e) {
				host = InetAddress.getByName("localhost"); 	// default to localhost
				e.printStackTrace();
			}
			
			
			ConnectionPacket connect = new ConnectionPacket();
			connect.type = CONN_REQ;
			connect.ID = -1; 		// This client doesn't have an ID yet;
			connect.compose();
			connect.send(client_socket, host, port);
			
			while(running){
                now = System.nanoTime();
                delta += (now - lastTime) / timePerTick;
                lastTime = now;

                if(delta >= 1 && sendData){
                    tick();
                    delta--;
                }
				// Get data from server
				byte[] buffer = new byte[1500];
				DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
				client_socket.receive(reply);
				
				byte[] data = reply.getData();
				Packet p = Packet.determinePacket(data);
				evaluateData(p, reply, client_socket);
			}
			stop();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void evaluateData(Packet p, DatagramPacket serverDatagram, DatagramSocket clientSocket) throws NumberFormatException, UnknownHostException{
		
		if (p instanceof ConnectionPacket)
        {
			Utils.debug("received connection packet");
            ConnectionPacket cp = (ConnectionPacket)p;
            switch (cp.type)
            {
                case CONN_ACK:
                {
                    playerID = cp.ID;
                    
                    break;
                }
                // TODO: CONN_MSG here would probably be like some sort of chat, do we want this?
                default:
                    break;
            }
        }
		if (p instanceof GameStartPacket){
			Utils.debug("received gameStart packet");
			GameStartPacket gs = (GameStartPacket)p;
			game = new Game("Battle Arena", gs.gameWidth, gs.gameHeight);
			game.init();
			int numP = gs.numPlayers;
			for (int i =0; i < numP; i++){
				// players are identified by their ID's, so no need
				// to specify the address and port on the client side.
				game.getHandler().getPlayers().add(new Player(game.getHandler(), 0, 0, null, -1, i+1));
			}
			
			game.getHandler().setClientPlayer(playerID);
			game.getGameState().displayState();
			game.start();
			sendData = true;
			inThread = new InputThread(game, clientSocket, serverDatagram.getAddress(), serverDatagram.getPort());
			inThread.start();
			
			
		}
		if (p instanceof PlayerUpdatePacket){
			Utils.debug("received playerUpdate packet");
			PlayerUpdatePacket pup = (PlayerUpdatePacket)p;
			Player player = game.getHandler().getPlayer(pup.ID);
			if(player != null){
				player.setInput(pup.input);
				player.setHealth(pup.health);
				player.setX(pup.posX);
				player.setY(pup.posY);
				player.setRotation(pup.rotation);
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
			inThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	

}
