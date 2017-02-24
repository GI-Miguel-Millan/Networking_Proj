package networking.project.game.entities.creatures;

import java.awt.Graphics;
import java.awt.Rectangle;
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
	private final int ID;
	
	private boolean readyFire;
	private int counter;
	private int score = 1000;
	private int hurtCounter = 0;
	private int powerUpCounter = 0;
	private int speedUp =0;
	private Rectangle playerBounds = new Rectangle(16,22,32,12);
	private boolean isBossDead = false,
					isHurt = false,
					isSpdUp = false,
					isSplitShot = false,
					fightingBoss = false,
					isBeingMoved =false,
					underSlowEffect = false;
	
	
	public Player(Handler handler, float x, float y, InetAddress ip, int port, int id) {
		super(handler, x, y, Creature.DEFAULT_CREATURE_WIDTH, Creature.DEFAULT_CREATURE_HEIGHT);
		
		bounds = playerBounds;
		counter = 0;
		readyFire = true;
		health = 50;
		speed = 3;
		handler.setPlayerHealth(health);
		handler.setPlayerScore(score);
		
		this.ip = ip;
		this.port = port;
		this.ID = id;
		
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
		//Animations
//		animDown.tick();
//		animUp.tick();
//		animRight.tick();
//		animLeft.tick();
//		hurtDown.tick();
//		hurtUp.tick();
//		hurtRight.tick();
//		hurtLeft.tick();
		
		//System.out.println("px: " + x + ", py: "+ y);
	
		checkConditions();
		//lowerBoundCheck();
		updateCounters();
		
		collisionWithBlackHole((int)x,(int)y);
		//handler.getGameCamera().staticCamera(this);
		
		handler.setPlayerScore(this.score);
		handler.setPlayerHealth(health);
		
		move();
		// only clear movement values after we've moved.
		yMove = 0;
		xMove = 0;
	}
	
	/**
	 * A player goes crashing through space, it should not stop before it 
	 * collides with another entity.
	 */
	public void move(){
		System.out.println("movement( x , y): " + xMove + " " + yMove);
			moveX();
			moveY();
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
		
		if(isHurt == true)
			hurtCounter++;
		
		if (hurtCounter == 50){
			isHurt = false;
			hurtCounter = 0;
		}
		
		if(isPoweredUp()){
			powerUpCounter++;
		}
		
		if(powerUpCounter >= 300){
			powerDown();
		}
		
		if(isSpdUp){
			speedUp = 2;
		}else{
			speedUp =0;
		}
	}
	
	/**
	 * Any condition or attribute that must be checked each tick.
	 */
	private void checkConditions(){
		
		//check for slow conditions
		if(collisionWithSlowVortex((int)x, (int)y)){
			underSlowEffect = true;
		}else{
			underSlowEffect = false;
		}
		
		//Check if boss fight is over.
		if(isBossDead){
			fightingBoss = false;
			Sound.stopAll();
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
			yMove = -speed - speedUp;
		}
		
		if(down == 1)
		{
			yMove = speed  + speedUp;
				
		}
		
		if(left == 1)
		{
			xMove = -speed -1 - speedUp;
		}
		
		if(right == 1)
		{
			xMove = speed + 1 + speedUp;
		}		
		// A player is only allowed to fire a projectile whenever readyFire is true 
		// and they hit the fire key.
		if(attack == 1 && readyFire){
			// Spawns a projectile above the player moving upwards
			if(!isSplitShot){
				handler.getWorld().getEntityManager().addEntity(new Projectile(handler, this, 0, -3));
			}else
			{
				handler.getWorld().getEntityManager().addEntity(new Projectile(handler, this, 0, -13));
				handler.getWorld().getEntityManager().addEntity(new Projectile(handler, this, 0, -3));
				handler.getWorld().getEntityManager().addEntity(new Projectile(handler, this, 0, 7));
			}
				
			// Every time a player fires a projectile they lose 10 score (accuracy is important)
			// and their guns go on cooldown (they are not ready to fire).
			score -=10;
			readyFire = false;
		}
	}

	/**
	 * Checks if the player is colliding with a Black Hole Tile.
	 * 
	 * @param x the x position of the Tile
	 * @param y the y position of the Tile
	 * @return true if the Tile is not solid
	 * @return false if the Tile is is solid
	 */
	protected void collisionWithBlackHole(int x, int y){
		int ty = (int) (y + yMove + bounds.y) / Tile.TILEHEIGHT;
		int tx = (int) (x + bounds.x) / Tile.TILEWIDTH;
		if(handler.getWorld().getTile(tx, ty).isBlackHole() && !isHurt){
			this.hurt(5);
		}
	}
	
	/**
	 * Checks if the player is colliding with a Slow Vortex Tile.
	 * 
	 * @param x the x position of the Tile
	 * @param y the y position of the Tile
	 * @return true if the Tile is not solid
	 * @return false if the Tile is is solid
	 */
	protected boolean collisionWithSlowVortex(int x, int y){
		int ty = (int) (y + yMove + bounds.y) / Tile.TILEHEIGHT;
		int tx = (int) (x + bounds.x) / Tile.TILEWIDTH;
		if(handler.getWorld().getTile(tx, ty).isSlowVortex()){
			return true;
		}else
		{
			return false;
		}
	}
	
	@Override
	public void render(Graphics g) {
		posX = (int)(x - handler.getGameCamera().getxOffset());
		posY = (int) (y - handler.getGameCamera().getyOffset());
		//g.drawImage(getCurrentAnimationFrame(), posX, posY, width, height, null);
		if (isSpdUp)
			g.drawImage(Assets.boosted,posX,posY,width,height,null);
		if (isInvinc)
			g.drawImage(Assets.invincible, posX, posY, width, height, null);
		
		g.drawRect(posX, posY, width, height);
//		g.fillRect((int) (x + bounds.x - handler.getGameCamera().getxOffset()),
//				(int) (y + bounds.y - handler.getGameCamera().getyOffset()),
//				bounds.width, bounds.height);
	}
	
	/**
	 *  @return the current animation frame based on which direction the player moves.
	 */
	private BufferedImage getCurrentAnimationFrame(){
		if(isHurt == false){
			if(xMove < 0){
				return animLeft.getCurrentFrame();
			}else if(xMove > 0){
				return animRight.getCurrentFrame();
			}else if(yMove < 0){
				return animUp.getCurrentFrame();
			}else{
				return animDown.getCurrentFrame();
			}
		}else{
			if(xMove < 0){
				return hurtLeft.getCurrentFrame();
			}else if(xMove > 0){
				return hurtRight.getCurrentFrame();
			}else if(yMove < 0){
				return hurtUp.getCurrentFrame();
			}else{
				return hurtDown.getCurrentFrame();
			}
		}
	}

	@Override
	public void die() {
		handler.checkAndSetHighScore(score);
		handler.writeHighScore();
//		handler.getGame().getGameOverState().displayState();
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
	
	public void setIsBossDead(boolean bool){
		isBossDead = bool;
	}
	
	public boolean isBossDead(){
		return isBossDead;
	}

	public void setIsSpdUp(boolean b){
		isSpdUp = b;
	}
	
	public boolean getIsSplitShot(){
		return isSplitShot;
	}
	
	public boolean getFightingBoss(){
		return fightingBoss;
	}
	
	public void setIsSplitShot(boolean b){
		isSplitShot = b;
	}
	
	public boolean getIsSpdUp(){
		return isSpdUp;
	}
	
	public void setIsInvic(boolean b) {
		isInvinc = b;
	}

	public boolean isPoweredUp(){
		if(isInvinc || isSpdUp || isSplitShot)
			return true;
		else 
			return false;
	}
	
	public void powerDown(){
		isInvinc = false;
		isSpdUp = false;
		isSplitShot = false;
		powerUpCounter = 0;
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
