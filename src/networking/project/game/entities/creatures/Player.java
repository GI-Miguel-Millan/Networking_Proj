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
	private Rectangle playerBounds = new Rectangle(16,22,32,12);
	private boolean isHurt = false;
	
	public Player(Handler handler, float x, float y, InetAddress ip, int port, int id) {
		super(handler, x, y, Creature.DEFAULT_CREATURE_WIDTH, Creature.DEFAULT_CREATURE_HEIGHT, id);
		
		//bounds = playerBounds;
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

	
		//lowerBoundCheck();
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
	 *  Gets input from the user and sets the players yMove and
	 *  xMove according to which key is pressed.
	 */
	public void applyInput(int up, int down, int left, int right, int attack){
		//System.out.println("up: " + up +" down: " + down +" left: " + left +" right: "+ right );
		
		

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
		// A player is only allowed to fire a projectile whenever readyFire is true 
		// and they hit the fire key.
		if(attack == 1 && readyFire){
			// attack here

			readyFire = false;
		}
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
		double angle = Math.atan2((posX+width/2) - handler.getMouseManager().getMouseX(), -((posY+height/2) - handler.getMouseManager().getMouseY())) - Math.PI;
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
		
		g.drawLine((int)posX+width/2, (int)posY, (int)(handler.getMouseManager().getMouseX()),(int)(handler.getMouseManager().getMouseY()));
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

	public boolean getIsInvinc(){
		return isInvinc;
	}
	
	public InetAddress getIP(){
		return this.ip;
	}
	
	public int getPort(){
		return this.port;
	}
	
	public int getID(){
		return this.ID;
	}
	
	public String toString(){
		return "Player ID: " + getID() + ", Address: " + getIP() + ", Port: " + getPort() + ", x position: " + getX() + " y position: " + getY();
		
	}
}
