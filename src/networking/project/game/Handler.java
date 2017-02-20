package networking.project.game;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import networking.project.game.entities.creatures.Player;
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
	private int playerScore;
	private int playerHealth;
	private Player player;
	private int lvlCounter = 1;
	private static final int NUMBER_LEVELS = 4;
	private boolean isTransitioning = false;
	private boolean isVictorious = false;

	private int highScore;
	
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
	
	public void changeWorld(){
		setWorld(new World(this, Assets.fileNames[lvlCounter]));
	}
	
	public void setLvlCounter(int lvl){
		lvlCounter = lvl;
	}
	
	public int getLvlCounter(){
		return lvlCounter;
	}
	
	/**
	 * @param score the new score
	 */
	public void setPlayerScore(int score){
		playerScore = score;
	}
	
	/**
	 * @return player Score
	 */
	public int getPlayerScore(){
		return playerScore;
	}

	/**
	 * @param hp the new health value to set to the playerHealth.
	 */
	public void setPlayerHealth(int hp){
		playerHealth = hp;
	}
	
	/**
	 * @return playerHealth
	 */
	public int getPlayerHealth(){
		return playerHealth;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}
	
	public void setIsTransitioning(boolean b){
		isTransitioning = b;
	}
	
	public int getNumLevels(){
		return this.NUMBER_LEVELS;
	}
	
	public boolean IsTransitioning(){
		return isTransitioning;
	}
	
	public boolean isVictorious(){
		return isVictorious;
	}
	
	public void setVictorious(boolean b){
		this.isVictorious = b;
	}
	
	//Checks the current score against the high score. If higher, update the high score
	public void  checkAndSetHighScore(int score)
	{
		if(score > highScore)
		{
			highScore = score;
		}
	}
	
	//Returns high score
		public int getHighScore()
		{
			return highScore;
		}
		
		//Write high score to a file
		public void writeHighScore()
		{
	        try {
	            // Assume default encoding.
	            FileOutputStream fOut = new FileOutputStream("highscore.txt");
	            DataOutputStream out = new DataOutputStream(fOut);

	            // Note that write() does not automatically
	            // append a newline character.
	            out.writeInt(highScore);

	            // Always close files.
	            out.close();
	        }
	        catch(IOException ex) {
	            System.out.println(
	                "Error writing to file '"
	                + "highscore.txt" + "'");
	            // Or we could just do this:
	            // ex.printStackTrace();
	        }
	    }
		
		//Load high score from a file
		public void loadHighScore()
		{
			 try {
		            // FileReader reads text files in the default encoding.
		            FileInputStream fIn = new FileInputStream("highscore.txt");
		            DataInputStream in = new DataInputStream(fIn);
		            
		            int temp; //Stores high score from file
		            temp = in.readInt();
		            System.out.println(temp);

		            // Always close files.
		            in.close();
		            
		            //Sets high score 
		            highScore = temp;
		            System.out.println(highScore);
		        }
		        catch(FileNotFoundException ex) {
		            System.out.println(
		                "Unable to open file '" + 
		                "highscore.txt" + "'");                
		        }
		        catch(IOException ex) {
		            System.out.println(
		                "Error reading file '" 
		                + "highscore.txt" + "'");                  
		            // Or we could just do this: 
		            // ex.printStackTrace();
		        }
		}
	
}
