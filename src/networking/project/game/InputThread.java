package networking.project.game;

import networking.project.game.entities.creatures.Player;
import networking.project.game.network.packets.PlayerUpdatePacket;
import networking.project.game.network.packets.ProjectileUpdatePacket;
import networking.project.game.utils.Utils;

import java.net.DatagramSocket;
import java.net.InetAddress;

public class InputThread extends Thread {
	private Game game;
	private DatagramSocket client_socket;
	private InetAddress host;
	private int port;

	private int projUP_counter = 0;
	private int waitTime = 15; 		// wait time before sending packet, in nanoseconds
	private boolean readyToSendProjUP = true;
	
	
	public InputThread(Game g, DatagramSocket cs, InetAddress h, int p){
		this.game = g;
		this.client_socket = cs;
		this.host = h;
		this.port = p;
	}
	
	@Override
	public void run(){
		PlayerUpdatePacket pup = new PlayerUpdatePacket();
		
		Player player = game.getHandler().getClientPlayer();
		
		int fps = 150;  // How many times every second we want to send
		double timePerTick = 1000000000 / fps;
		double delta = 0;
		long now;
		long lastTime = System.nanoTime();
		long timer = 0;
		int ticks = 0;
		while(true){
			now = System.nanoTime();
			delta += (now - lastTime) / timePerTick;
			lastTime = now;
			timer += now - lastTime;
			
			if(delta >= 1){
				
				if(projUP_counter >= waitTime){
					readyToSendProjUP = true;
					projUP_counter = 0;
				}
				
				Utils.debug("sending player data");
				pup.ID = player.getID();
				pup.input = player.getInput();
				pup.health = player.getHealth();
				pup.posX = player.getX();
				pup.posY = player.getY();
				pup.rotation = player.getRotation();
				pup.compose();
				pup.send(client_socket, host, port);
				
				if(player.wantToFire && readyToSendProjUP){
					readyToSendProjUP = false; 			// start counting down before sending another proj update packet
					ProjectileUpdatePacket projUP = new ProjectileUpdatePacket();
					projUP.ID = -1; // let the server assign proper ID to projectile
					projUP.parentID = player.getID();
					projUP.rotation = player.getRotation();
					projUP.xPos = player.getX();
					projUP.yPos = player.getY();
					projUP.mX = player.getCamX() + player.getMouseX();
					projUP.mY = player.getCamY() + player.getMouseY();
					projUP.compose();
					projUP.send(client_socket,  host, port);
					
				}
				
				if(!readyToSendProjUP){
					projUP_counter++;
				}
				
				ticks++;
				delta--;
			}
			
			
		}
	}
}
