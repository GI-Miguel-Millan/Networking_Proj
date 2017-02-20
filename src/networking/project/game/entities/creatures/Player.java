package networking.project.game.entities.creatures;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

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
	private boolean readyFire;
	private int counter;
	private int score = 1000;
	private int hurtCounter = 0;
	private int powerUpCounter = 0;
	private int speedUp =0;
	private Rectangle playerBounds = new Rectangle(16,22,32,12);
	private boolean isBossDead = false,
					isHurt = false,
					isInvinc = false,
					isSpdUp = false,
					isSplitShot = false,
					fightingBoss = false,
					isBeingMoved =false,
					underSlowEffect = false;
	
	
	public Player(Handler handler, float x, float y) {
		super(handler, x, y, Creature.DEFAULT_CREATURE_WIDTH, Creature.DEFAULT_CREATURE_HEIGHT);
		
		bounds = playerBounds;
		counter = 0;
		readyFire = true;
		health = 50;
		speed = 3;
		handler.setPlayerHealth(health);
		handler.setPlayerScore(score);
		
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
		animDown.tick();
		animUp.tick();
		animRight.tick();
		animLeft.tick();
		hurtDown.tick();
		hurtUp.tick();
		hurtRight.tick();
		hurtLeft.tick();
		
		//System.out.println("px: " + x + ", py: "+ y);
	
		checkConditions();
		lowerBoundCheck();
		updateCounters();
		
		collisionWithGoal((int)x,(int)y);
		collisionWithBlackHole((int)x,(int)y);
		if(!fightingBoss)
			collisionWithBossFightStart((int)x, (int)y);
		
		//handler.getGameCamera().centerOnEntity(this);
		handler.getGameCamera().staticCamera(this);
		
		handler.setPlayerScore(this.score);
		handler.setPlayerHealth(health);
		
		move();
	}
	
	/**
	 * A player goes crashing through space, it should not stop before it 
	 * collides with another entity.
	 */
	public void move(){
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
		//Movement
		if(!isBeingMoved)
			getInput();
		else
			moveToCenter();
		
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
	
	private void collisionWithBossFightStart(int x, int y) {
		int ty = (int)(y + yMove + bounds.y) / Tile.TILEHEIGHT;
		int tx = (int)(x + bounds.x) / Tile.TILEWIDTH;
		if(handler.getWorld().getTile(tx, ty).isBFight()){
			isBeingMoved = true;
		}
		
		//System.out.println(" Ty: " + ty + " tx: " + tx + " this.x: " + this.x + " this.y " + this.y);
	}

	/**
	 * Sets the players y position to the bottom of the game camera 
	 * if the player moves below the screen
	 */
	private void lowerBoundCheck(){
		if(y > (((handler.getGameCamera().getyOffset() + 700)))){
			this.y = handler.getGameCamera().getyOffset() + 700;
		}
	}
	
	/**
	 *  Gets input from the user and sets the players yMove and
	 *  xMove according to which key is pressed.
	 */
	private void getInput(){
		xMove = 0;
		//yMove = -2;
		if(underSlowEffect){
			yMove = -(handler.getGameCamera().getCamSpeed()) + 2;
		}else if(fightingBoss){
			yMove = 0;
		}else if(isBossDead){
			yMove = -2;
		}else{
			yMove = -(handler.getGameCamera().getCamSpeed());
		}
		
		int bottomBounds = handler.getHeight() - 100;
		
//		if(fightingBoss)
//			bottomBounds = handler.getHeight() - 100;
		
		if(handler.getKeyManager().up)
		{
			if (y >= (((handler.getGameCamera().getyOffset() + 1))))
			{
				yMove += -speed - speedUp;
			}
			else
				yMove += 0;
		}
		
		if(handler.getKeyManager().down)
		{
			if (y < (((handler.getGameCamera().getyOffset() + bottomBounds))))
			{	
			yMove += speed + 2 + speedUp;
			}
			else{
				yMove += 0;
			}
				
		}
		
		if(handler.getKeyManager().left)
		{
			xMove = -speed -1 - speedUp;
		}
		
		if(handler.getKeyManager().right)
		{
			xMove = speed + 1 + speedUp;
		}		
		// A player is only allowed to fire a projectile whenever readyFire is true 
		// and they hit the fire key.
		if(handler.getKeyManager().fire && readyFire){
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
	 * Moves the player to the center of the screen.
	 */
	public void moveToCenter(){
		xMove = 0;
		yMove = -2;
		
		//System.out.println("y " + y + ", camY: "+ (camY + handler.getHeight()/2) + ", x " + x + ", camX " + camX);
		if (y + height + 5 >= (((handler.getHeight()/2 + (yMove + speed))))){
			yMove += -speed;
		}else
			yMove += 0;
		
		if(x >= (handler.getWidth()/2 + speed)){
			xMove += -speed;
		}else{
			xMove += 0;
		}
		
		if(x <= (handler.getWidth()/2 - speed)){
			xMove += speed;
		}else{
			xMove += 0;
		}
		
		if(y + height + 1 <= handler.getHeight()/2 + 100 ){
			isBeingMoved = false;
			fightingBoss = true;
		}
	}
	
	/**
	 * Checks if the player is colliding with a Goal Tile.
	 * 
	 * @param x the x position of the Tile
	 * @param y the y position of the Tile
	 * @return true if the Tile is not solid
	 * @return false if the Tile is is solid
	 */
	protected void collisionWithGoal(int x, int y){
		int ty = (int) (y + yMove + bounds.y) / Tile.TILEHEIGHT;
		int tx = (int) (x + bounds.x) / Tile.TILEWIDTH;
		if(handler.getWorld().getTile(tx, ty).isGoal() && isBossDead){
			handler.setPlayerScore(score);
			handler.setLvlCounter(handler.getLvlCounter() + 1);
			if (handler.getLvlCounter() > handler.getNumLevels()){
				this.die();
				handler.setVictorious(true);
			}else{
				handler.setIsTransitioning(true);
			}
				
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
		g.drawImage(getCurrentAnimationFrame(), posX, posY, width, height, null);
		if (isSpdUp)
			g.drawImage(Assets.boosted,posX,posY,width,height,null);
		if (isInvinc)
			g.drawImage(Assets.invincible, posX, posY, width, height, null);
		
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
		handler.getGame().getGameState().setLastTrans(true);
		handler.setIsTransitioning(true);
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
	
}
