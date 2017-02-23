package networking.project.game.entities.creatures.enemies;

import networking.project.game.Handler;
import networking.project.game.entities.creatures.Creature;
import networking.project.game.entities.creatures.Player;
import networking.project.game.entities.creatures.projectiles.Projectile;
import networking.project.game.utils.Utils;

/**
 *	Enemies are creatures controlled by the system and not the user. There are 
 *	different types of Enemies.
 * 	
 *	@author Miguel Millan
 *	@version 1.0
 *	@since version 1.0
 */
public abstract class Enemy extends Creature{
	protected int counter =0;
	protected int readyCount = 0;
	protected boolean ready = true;
	protected int playerY = 0; //must actively set this variable in the tick method whenever it is used.
	protected int playerX = 0; //must actively set this variable in the tick method whenever it is used.
	protected static final int DEFAULT_ENEMY_WIDTH = 30;
	protected static final int DEFAULT_ENEMY_HEIGhT = 50;
	
	public Enemy(Handler handler, float x, float y) {
		super(handler, x, y, DEFAULT_ENEMY_WIDTH, DEFAULT_ENEMY_HEIGhT);
		
	}
	
	@Override
	public void tick(){
		if (isOnScreen()){
			// Will need to rework for multiple screens
//			playerX = (int)handler.getWorld().getEntityManager().getPlayer().getX();
//			playerY = (int)handler.getWorld().getEntityManager().getPlayer().getY();
			//Set x and y movement values
			AIMove();
			
			//Movement of the enemy
			move();
			
			//Attack of the enemy
			attack();
		}
		
	}
	
	// This method should be overwritten in child classes
	// Default is movement from left to right in random time intervals.
	public void AIMove(){
		if(counter <50){
			xMove = 5;
		}else if(counter < 100){
			xMove = -5;
		}else{
			counter = 0;
		}
		counter += Utils.randomNum(0, 3);
	}
	
	// This method should be overwritten in child classes
	// Default is a random projectile spawn.
	public void attack(){
		int randAttack = Utils.randomNum(0, 50);
		if(randAttack == 0){
			handler.getWorld().getEntityManager().addEntity(new Projectile(handler, this, getProjectileOrientation(),0));
		}
		
		collisionWithPlayer();
	}
	
	//Deals damage to the player if they intersect this Enemy, destroys enemy.
	public void collisionWithPlayer(){
		
				if(intersectWithPlayer() && ready){
					hurtPlayer(1);
					ready = false;
				}
				
				if (!ready){
					readyCount++;
				}
				
				if(readyCount >= 5){
					ready = true;
					readyCount =0;
				}
	}
	
	public boolean isOnScreen(){
//		if (y >= (((handler.getGameCamera().getyOffset() - height))) 
//				&& y < (((handler.getGameCamera().getyOffset() + handler.getGame().getHeight())))){
//			return true;
//		}else{
//			return false;
//		}
		
		return true;
	}
	
	/**
	 * @return true if this enemy intersects with the player.
	 */
	public boolean intersectWithPlayer(){
		for(Player p: handler.getPlayers()){
			if(p.getCollisionBounds(0f, 0f).intersects(getCollisionBounds(xMove, yMove))){
			return true;
			}
		}
		return false;
	}
	
	/**
	 * finds and hurts player which collided with this enemy.
	 */
	public void hurtPlayer(int dmg){
		for(Player p: handler.getPlayers()){
			if(p.getCollisionBounds(0f, 0f).intersects(getCollisionBounds(xMove, yMove))){
				p.hurt(dmg);
			}
		}
	}
	/**
	 * 
	 * 
	 * @return
	 */
	public int getProjectileOrientation(){
		playerX = playerX + Creature.DEFAULT_CREATURE_WIDTH /2;
		playerY = playerY + Creature.DEFAULT_CREATURE_HEIGHT /2;
		float relX = width/2;
		float relY = height/2;
		float relPx = playerX - (x + relX);
		float relPy = (y + relY) - playerY; // This is because y gets bigger going down the screen.
		if(relX -relPx == 0)
			relX += 1;
		float playerSlope = ((relY - relPy)/(relX - relPx));
//		System.out.println("relX " + relX + ", relY: " + relY);
//		System.out.println("Slope: " + playerSlope + ", relPx: " + relPx + ", relPy: " + relPy);
		if(playerSlope <= 1 && playerSlope >= -1){
			if(relPx < 0)
				return 3; // Shoot Projectiles left
			else
				return 2; // Shoot Projectiles right
		}else if (playerSlope >= 1 || playerSlope <= -1){
			if(relPy < 0)
				return 1; // Shoot Projectiles down
			else 
				return 0; // Shoot Projectiles up
		}
		
		return 1;
	}
	
	/**
	 * Any Enemy which calls its hurt method will lose health
	 * based on the amount of damage it received. If the health of the Enemy
	 * ever reaches 0, then it will become inactive and its die method is called.
	 * Also it will cause a new PowerUp to be spawned at its location.
	 * 
	 * @param amt the amount of damage an Entity takes
	 */
	public void hurt(int amt){
		health -= amt;
		if(health <= 0){
			//handler.getWorld().getEntityManager().spawnPowerUp(handler, (int)x, (int)y);
			active = false;
			die();
			
		}
	}
	
	/**
	 * @return true since this is an Enemy
	 */
	public boolean isEnemy(){
		return true;
	}
}
