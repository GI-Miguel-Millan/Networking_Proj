package networking.project.game.entities.creatures.enemies;

import java.awt.Color;
import java.awt.Graphics;

import networking.project.game.Handler;
import networking.project.game.entities.creatures.Creature;
import networking.project.game.entities.creatures.projectiles.Projectile;
import networking.project.game.gfx.Animation;
import networking.project.game.gfx.Assets;
import networking.project.game.sound.Sound;
import networking.project.game.utils.Utils;

/**
 *	A StealthFigher is an enemy which is harder to see, and prone to sneak attacks.
 * 	
 *	@author Miguel Millan
 *	@version 1.0
 *	@since version 1.0
 */
public class StealthFighter extends Enemy{

	private boolean isStealthed = true;
	private boolean isAttacking = false;
	private int stealthBreakDistance = 250;
	private int holdDistance = 100;
	
	public StealthFighter(Handler handler, float x, float y) {
		super(handler, x, y);
		
	}

	@Override
	public void tick(){
		if (isOnScreen()){
			playerX = (int)handler.getWorld().getEntityManager().getPlayer().getX();
			playerY = (int)handler.getWorld().getEntityManager().getPlayer().getY();
			//Set x and y movement values
			AIMove();
			
			//Movement of the enemy
			move();
			
			//Attack of the enemy
			if(isAttacking)
				attack();
			
		}
		
	}

	/**
	 * In order for a StealthFighter to move, the player must be between its hold position 
	 * and its stealth break location. If the player is outside the stealth break bounds
	 * then the StealthFighter will become stealthed in which case it should not move, 
	 * and if the player is within the hold position bounds the StealthFighter will
	 * hold its position and shoot the player. This method checks whether or not the player is
	 * between these two bounds.
	 * 
	 * @return true if the StealthFighter can move
	 * @return false if the StealthFighter cannot/should not move
	 */
	private boolean canMove(){
		int holdX1 = (int)x + holdDistance;
		int sBreakX1 = (int)x + stealthBreakDistance;
		int holdY1 = (int)y + holdDistance;
		int sBreakY1 = (int)y + stealthBreakDistance;
		int holdX2 = (int)x - holdDistance;
		int sBreakX2 = (int)x - stealthBreakDistance;
		int holdY2 = (int)y - holdDistance;
		int sBreakY2 = (int)y - stealthBreakDistance;
		
		if((playerX > holdX1) && (playerX < sBreakX1)){
			return true;
		}
		if((playerX < holdX2) && (playerX > sBreakX2)){
			return true;
		}
		if((playerY > holdY1) && (playerY < sBreakY1)){
			return true;
		}
		if((playerY < holdY2) && (playerY > sBreakY2)){
			return true;
		}
		
		return false;
		
	}
	
	public void AIMove(){
		if((x <= playerX + stealthBreakDistance || x > playerX - stealthBreakDistance) && 
				 (y >= playerY + stealthBreakDistance || y < playerY - stealthBreakDistance)){
			isStealthed = true;
			isAttacking = false;
		}else{
			
			isStealthed = false;
			isAttacking = true;
		}
		
		if(isStealthed || !canMove()){
			xMove = 0;
			yMove = 0;
		}else{
			if(handler.getPlayer().getX() - speed > x){
				xMove = speed/2;
			}else if (handler.getPlayer().getX()+speed < x){
				xMove = -speed/2;
			}else {
				xMove =0;
			}
			
			if(handler.getPlayer().getY() - speed > y){
				yMove = speed;
			}else if (handler.getPlayer().getY() + speed < y){
				yMove = -speed;
			}else {
				yMove =0;
			}
			
				
			
			
		}
		counter += Utils.randomNum(0, 3);
		
		collisionWithPlayer();
	}
	
	public void attack(){
		int randAttack = Utils.randomNum(0, 50);
		if(randAttack == 0){
			handler.getWorld().getEntityManager().addEntity(new Projectile(handler, 
					this, getProjectileOrientation(),0));
		}
	}
	
	public void collisionWithPlayer(){
		if(intersectWithPlayer()){
			handler.getPlayer().hurt(10);
			this.hurt(this.health);
		}
	}
	
	@Override
	public void render(Graphics g) {
		posX = (int)(x - handler.getGameCamera().getxOffset());
		posY = (int) (y - handler.getGameCamera().getyOffset());
		g.setColor(Color.DARK_GRAY);
		if(isStealthed){
			//g.drawRect(posX, posY, width, height);
			g.drawImage(Assets.stealthed, posX, posY, width, height, null);		}
		else{
			g.drawImage(Assets.stealth, posX, posY, width, height, null);
			drawHealthBar(posX, posY, width, height, Creature.DEFAULT_HEALTH, health, 10,1, g);
		}
		//g.drawImage(explosion.getCurrentFrame(), posX-(32-width/2), posY-(32-height/2), 64, 64, null);
		
	}

	@Override
	public void die() {
		// A StealthFighter awards 150 score to the player upon death.
		handler.getWorld().getEntityManager().getPlayer().addScore(150);
		Sound.explosion.execute();//New jon edit
		
	}
}
