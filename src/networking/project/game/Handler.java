package networking.project.game;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;

import networking.project.game.entities.Entity;
import networking.project.game.entities.creatures.Player;
import networking.project.game.entities.creatures.projectiles.Projectile;
import networking.project.game.gfx.Assets;
import networking.project.game.gfx.GameCamera;
import networking.project.game.input.KeyManager;
import networking.project.game.input.MouseManager;
import networking.project.game.worlds.World;

/**
 *	Handler stores references to objects which need to be
 *  referenced throughout the game. 
 * 	
 *	@author 
 *	@version 1.0
 *	@since version 1.0
 */
public class Handler {
	
	private Game game;
	private World world;
	private ArrayList<Player> players = new ArrayList<Player>();
	private ArrayList<Integer> kill_ids = new ArrayList<Integer>();
	private Player clientPlayer;

	
	public Handler(Game game){
		this.game = game;
	}
	
	/**
	 * @return the gameCamera object stored in game.
	 */
	public GameCamera getGameCamera(){
		return game.getGameCamera();
	}
	
	/**
	 * @return the KeyManager stored in game.
	 */
	public KeyManager getKeyManager(){
		return game.getKeyManager();
	}
	
	/**
	 * @return the mouseManager stored in game.
	 */
	public MouseManager getMouseManager(){
		return game.getMouseManager();
	}
	
	/**
	 * @return the width of the game.
	 */
	public int getWidth(){
		return game.getWidth();
	}
	
	/**
	 * @return the height of the game.
	 */
	public int getHeight(){
		return game.getHeight();
	}

	/**
	 * @return the game 
	 */
	public Game getGame() {
		return game;
	}

	/**
	 * @param game 
	 */
	public void setGame(Game game) {
		this.game = game;
	}
	
	/**
	 * @return the current world
	 */
	public World getWorld() {
		return world;
	}

	/**
	 * @param world the new world 
	 */
	public void setWorld(World world) {
		this.world = world;
	}


	/**
	 * @return player Score
	 */
	public int getPlayerScore(){
		return clientPlayer.getScore();
	}

	/**
	 * @return playerHealth
	 */
	public int getPlayerHealth(){
		return clientPlayer.getHealth();
	}

	/**
	 * @return players arraylist
	 */
	public ArrayList<Player> getPlayers(){
		return this.players;
	}
		
	/**
	 * identifies the player belonging to this client
	 */
	public void setClientPlayer(int id){
		clientPlayer = this.getPlayer(id);
	}
	
	/**
	 * @return the player associated with this handler's client
	 */
	public Player getClientPlayer(){
		return this.clientPlayer;
	}
	
	/**
	 * Gets the player from the players arrayList using an id.
	 * @param id
	 * @return
	 */
	public Player getPlayer(int id){
		for(Player p : players)
			if(p.getID() == id)
				return p;
		return null;
	}
	
	/**
	 * Gets the player from the players ArrayList using the ip and port 
	 * number associated with that player.
	 * 
	 * @param ip
	 * @param port
	 * @return
	 */
	public Player getPlayer(InetAddress ip, int port){
		for(Player p : players)
			if(p.getIP().equals(ip) && p.getPort() == port)
				return p;
		return null;
	}

	/**
	 * returns an entity matching the id provided.
	 * @param id
	 * @return
	 */
	public Entity getEntity(int id) {
		return world.getEntityManager().getEntities().get(id);
	}
	
	/**
	 * @return ArrayList of entities ID's indicating entities to deactivate
	 */
	public ArrayList<Integer> getK_ID(){
		return this.kill_ids;
	}

	/**
	 * Removes an entity from the world based on its id
	 * 
	 * @param id - the id of the entity to remove
	 */
	public void killEntity(int id) {
		world.getEntityManager().removeEntity(id);
	}
}
