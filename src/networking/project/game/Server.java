package networking.project.game;

import networking.project.game.entities.creatures.Player;
import networking.project.game.network.packets.*;
import networking.project.game.utils.NetCodes;
import networking.project.game.utils.Utils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;

public class Server implements Runnable, NetCodes {

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
		//game.tick();
		//game.render();
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
		game.init(true);		// initialze the game before creating players to avoid null pointers
		
		try {
			server_socket = new DatagramSocket(7777);
			
			
			byte[] buffer = new byte[1500];
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
                // Determine what it is and do stuff with it
				Packet p = Packet.determinePacket(data);
				
//				if(gameStarted)
//					Utils.debug("received a packet");
				
				evaluateCommand(p, incoming, server_socket);
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
	 */
	private void evaluateCommand(Packet p, DatagramPacket clientDatagram, DatagramSocket serverSocket)
    {
		if(!(p instanceof ConnectionPacket) && !(p instanceof PlayerUpdatePacket) 
				&& !(p instanceof ProjectileUpdatePacket))
		{
			Utils.debug("Something wrong with packet");
		}
		
		
        if (p instanceof ConnectionPacket)
        {
            ConnectionPacket cp = (ConnectionPacket)p;
            switch (cp.type)
            {
                case CONN_REQ:
                {
                    // A new client connecting
                    this.ids++; // add new player, must increment ids
                    this.game.getHandler().getPlayers().add(new Player(game.getHandler(),0,0,clientDatagram.getAddress(), clientDatagram.getPort(), this.ids));

                    // Identify this player to them
                    ConnectionPacket ack = new ConnectionPacket();
                    ack.type = CONN_ACK;
                    ack.ID = ids;
                    ack.compose();
                    ack.send(serverSocket, clientDatagram);


                    // If this client was the last one we needed, start the game
                    if (game.getHandler().getPlayers().size() == number_of_players)
                    {
                        GameStartPacket gs = new GameStartPacket();
                        // TODO: ep.setMode("Battle_Arena") ?
                        gs.gameWidth = (short)GAMEWIDTH;
                        gs.gameHeight = (short)GAMEHEIGHT;
                        gs.numPlayers = (byte)number_of_players;
                        gs.compose();

                        // Send this packet to everyone
                        for (Player pl : game.getHandler().getPlayers())
                        {
                            gs.send(serverSocket, pl.getIP(), pl.getPort());
                        }

                        game.getGameState().displayState();
                        gameStarted = true;	// start the game (allow it to tick)
                        game.getHandler().setClientPlayer(1);
                    }
                    else
                    {
                        // Waiting on more players to join
                        ConnectionPacket mess = new ConnectionPacket();
                        mess.type = CONN_MSG;
                        mess.message = "Waiting for more players!";
                        mess.compose();
                        mess.send(serverSocket, clientDatagram);
                    }
                    break;
                }
                case CONN_DISC:
                {
                    // TODO: Implement disconnecting (them pressing escape)
                    break;
                }
                // TODO: CONN_MSG here would probably be like some sort of chat, do we want this?
                default:
                    break;
            }
        }
        else if (p instanceof PlayerUpdatePacket)
        {
        	Utils.debug("received an update packet from the player");
            PlayerUpdatePacket pup = (PlayerUpdatePacket)p;
            // This is a player sending an update to us.
            // First, update this player
            Player player = game.getHandler().getPlayer(pup.ID);
            if (player != null)
            {
                player.setInput(pup.input);
                player.setHealth(pup.health);
                player.setX(pup.posX);
                player.setY(pup.posY);
                player.setRotation(pup.rotation);

                // Update this player with everybody else's data
                ArrayList<Player> players = game.getHandler().getPlayers();
                for (Player other : players)
                {
                    // Don't care about this same player
                    if (other.getID() == pup.ID)
                        continue;

                    PlayerUpdatePacket pupOther = new PlayerUpdatePacket();
                    pupOther.ID = other.getID();
                    pupOther.input = other.getInput();
                    pupOther.health = other.getHealth();
                    pupOther.posX = other.getX();
                    pupOther.posY = other.getY();
                    pupOther.rotation = other.getRotation();
                    pupOther.compose();
                    pupOther.send(serverSocket, clientDatagram);
                }

                // TODO: Update the projectiles for this player

                // TODO: Send the killed list to the player
            }
        }else if (p instanceof ProjectileUpdatePacket){
        	ProjectileUpdatePacket projUP = (ProjectileUpdatePacket)p;
        	
        	if (projUP.ID == -1)		// An ID of -1 indicates a player wants to spawn a projectile
        	{
        		if(ids <255)			// increment ids to indicate a new projectile spawn
        			ids++;
        		else					
        			ids = number_of_players +1;
        		
        		ProjectileUpdatePacket otherPUP = new ProjectileUpdatePacket();
                otherPUP.ID = ids;
                otherPUP.parentID = projUP.parentID;
                otherPUP.rotation = projUP.rotation;
                otherPUP.xPos = projUP.xPos;
                otherPUP.yPos = projUP.yPos;
                otherPUP.mX = projUP.mX;
                otherPUP.mY = projUP.mY;
                otherPUP.compose();
                		
        		// Tell each player to spawn a projectile
                for (Player pl : game.getHandler().getPlayers())
                {
                    otherPUP.send(serverSocket, pl.getIP(), pl.getPort());
                }
        	}else						// otherwise (something)
        	{
        		
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
