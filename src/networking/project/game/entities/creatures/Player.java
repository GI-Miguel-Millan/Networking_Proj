package networking.project.game.entities.creatures;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.net.*;

import networking.project.game.Handler;
import networking.project.game.entities.creatures.projectiles.*;
import networking.project.game.gfx.Animation;
import networking.project.game.gfx.Assets;
import networking.project.game.gfx.GameCamera;
import networking.project.game.sound.Sound;
import networking.project.game.states.State;
import networking.project.game.tiles.Tile;
import networking.project.game.worlds.World;

/**
 *	Player is a Creature controlled by the user. This class takes input from the user
 *	for movement, and is able to shoot projectiles.
 * 	
 *	@author 
 *	@version 1.0
 *	@since version 1.0
 */
public class Player extends Creature {
	
	//Animations
	private Animation animDown, animUp, animLeft, animRight,
						hurtDown, hurtUp, hurtLeft, hurtRight;
	
	//Networking info
	private InetAddress ip;
	private final int port;
	
	private boolean readyFire;
	private int counter;
	private int score = 1000;
	//private Rectangle playerBounds = new Rectangle(16,22,32,12); // Can be used to adjust player bounds
	private boolean isHurt = false;
	private int mouseX =0 , mouseY =0, camX = 0, camY =0;
	
	public Player(Handler handler, float x, float y, InetAddress ip, int port, int id) {
		super(handler, x, y, Creature.DEFAULT_CREATURE_WIDTH, Creature.DEFAULT_CREATURE_HEIGHT, id);
		
		//bounds = playerBounds; // uncomment to use specified bounds rather than default ones
		counter = 0;
		readyFire = true;
		health = 50;
		speed = 5;
		
		this.ip = ip;
		this.port = port;
		
		//Animatons
		animDown = new Animation(500, Assets.player_down);
		animUp = new Animation(500, Assets.player_up);
		animLeft = new Animation(500, Assets.player_left);
		animRight = new Animation(500, Assets.player_right);
		hurtDown = new Animation(50, Assets.hurt_down);
		hurtUp = new Animation(50, Assets.hurt_up);
		hurtLeft = new Animation(50, Assets.hurt_left);
		hurtRight = new Animation(50, Assets.hurt_right);
	}

	@Override
	public void tick() {
		
		updateCounters();
		
		move();
		// only clear movement values after we've moved.
		yMove = 0;
		xMove = 0;
	}

	/**
	 * Updates counters
	 */
	private void updateCounters(){
		if(!readyFire)
			counter++;
		
		if(counter == 20){
			readyFire = true;
			counter = 0;
		}
	}
	

	
	
	/**
	 *  Gets input from the client and sets the players yMove and
	 *  xMove according to which key is pressed, as well as the camera offsets for 
	 *  that player (which are used for proper rendering)
	 */
	public void applyInput(int up, int down, int left, int right, int xOffset, int yOffset){

		if(up == 1)
		{
			yMove = -speed;
		}
		
		if(down == 1)
		{
			yMove = speed;
				
		}
		
		if(left == 1)
		{
			xMove = -speed;
		}
		
		if(right == 1)
		{
			xMove = speed;
		}		
		
		this.camX = xOffset;
		this.camY = yOffset;
	}

	@Override
	public void render(Graphics g) {
		//Animations
		animDown.tick();
		animUp.tick();
		animRight.tick();
		animLeft.tick();
		hurtDown.tick();
		hurtUp.tick();
		hurtRight.tick();
		hurtLeft.tick();
		posX = (int)(x - handler.getGameCamera().getxOffset());
		posY = (int) (y - handler.getGameCamera().getyOffset());
		
		Graphics2D gr = (Graphics2D)g;
		AffineTransform transform = gr.getTransform();
//		System.out.println("mouseX: " + mouseX + ", mouseY: " + mouseY);
//		System.out.println("mmX: " + handler.getMouseManager().getMouseX() + ", mmY: " + handler.getMouseManager().getMouseY());
		//double angle = Math.atan2((posX+width/2) - handler.getMouseManager().getMouseX() , -((posY+height/2) - handler.getMouseManager().getMouseY())) - Math.PI;
		double angle = Math.atan2((posX+width/2) - mouseX , -((posY+height/2) - mouseY)) - Math.PI;
		//double angle = Math.atan2(-((posY+height/2) - mouseY), (posX+width/2) - mouseX) - Math.PI/2;
		//double angle = Math.PI/2 - Math.atan(-handler.getMouseManager().getMouseY()/(handler.getMouseManager().getMouseX()+1));
		gr.rotate(angle, posX+width/2, posY+width/2);
		if(this.ID == 1)
			gr.drawImage(Assets.eagle, posX, posY, width, height, null);
		if(this.ID == 2)
			gr.drawImage(Assets.assault, posX, posY, width, height, null);
		if(this.ID == 3)
			gr.drawImage(Assets.stealth, posX, posY, width, height, null);
		if(this.ID == 4)
			gr.drawImage(Assets.interceptor, posX, posY, width, height, null);
		gr.setTransform(transform);
		
		//g.drawLine((int)posX+width/2, (int)posY, (int)(handler.getMouseManager().getMouseX()),(int)(handler.getMouseManager().getMouseY()));
		//g.drawRect(posX, posY, width, height);
//		g.fillRect((int) (x + bounds.x - handler.getGameCamera().getxOffset()),
//				(int) (y + bounds.y - handler.getGameCamera().getyOffset()),
//				bounds.width, bounds.height);
	}
	
	/**
	 *  @return the current animation frame based on which direction the player moves.
	 */
	private BufferedImage getCurrentAnimationFrame(){
		if(isHurt == false){
			if(handler.getKeyManager().left){
				return animLeft.getCurrentFrame();
			}else if(handler.getKeyManager().right){
				return animRight.getCurrentFrame();
			}else if(handler.getKeyManager().up){
				return animUp.getCurrentFrame();
			}else{
				return animDown.getCurrentFrame();
			}
		}else{
			if(handler.getKeyManager().left){
				return hurtLeft.getCurrentFrame();
			}else if(handler.getKeyManager().right){
				return hurtRight.getCurrentFrame();
			}else if(handler.getKeyManager().up){
				return hurtUp.getCurrentFrame();
			}else{
				return hurtDown.getCurrentFrame();
			}
		}
	}

	@Override
	public void die() {
		// implement action upon player death
		handler.getK_ID().add(this.ID);
		active =false;
	}
	
	/**
	 * The hurt method of the Player must be overridden so that 
	 * every time the player takes damage, the handler can update 
	 * the player health.
	 * @Override
	 */
	public void hurt(int amt){
		if(!isInvinc){
			health -= amt;
			isHurt = true;
		}
		
		
		if(health <= 0){
			active = false;
			die();
		}
	}
	
	/**
	 * Add integer to the players score.
	 * @param score
	 */
	public void addScore(int score){
		this.score += score;
	}
	
	/**
	 * 
	 * @return player score
	 */
	public int getScore(){
		return score;
	}
	
	/**
	 * @return IP address of this Player
	 */
	public InetAddress getIP(){
		return this.ip;
	}
	
	/**
	 * @return port of this Player
	 */
	public int getPort(){
		return this.port;
	}
	
	/**
	 * @return unique ID of this Player
	 */
	public int getID(){
		return this.ID;
	}
	
	/**
	 * returns a string containing this player ID, associated IP and Port, and x and y positions.
	 */
	public String toString(){
		return "Player ID: " + getID() + ", Address: " + getIP() + ", Port: " + getPort() + ", x position: " + getX() + " y position: " + getY();
		
	}

	/**
	 * Sets (x, y) position of the mouse associated with this Player.
	 * @param x
	 * @param y
	 */
	public void setMouseCoord(int x, int y) {
		mouseX = x;
		mouseY = y;
	}

	/**
	 * @return the x coordinate of the mouse associated with this Player
	 */
	public int getMouseX() {
		return mouseX;
	}

	/**
	 * @return the y coordinate of the mouse associated with this Player
	 */
	public int getMouseY() {
		
		return mouseY;
	}
	
	/**
	 * @return true if this player is ready to attack, false otherwise.
	 */
	public boolean isReady(){
		return readyFire;
	}
	
	/**
	 * Sets whether or not a player is ready to attack.
	 * @param b 
	 */
	public void setReady(boolean b){
		readyFire = b;
	}
	
	/**
	 * @return the xOffset of the GameCamera associated with this player.
	 */
	public float getCamX(){
		return (float) camX;
	}
	
	/**
	 * @return the yOffset of the GameCamera associated with this player.
	 */
	public float getCamY(){
		return (float) camY;
	}

	/**
	 * Set the xOffset of the GameCamera associated with this Player.
	 * @param cx
	 */
	public void setCamX(int cx) {
		this.camX = cx;
		
	}

	/**
	 * Set the yOffset of the GameCamera associated with this Player.
	 * @param cy
	 */
	public void setCamY(int cy) {
		this.camY = cy;
		
	}
}


