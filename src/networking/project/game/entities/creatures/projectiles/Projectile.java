package networking.project.game.entities.creatures.projectiles;

import java.awt.Color;
import java.awt.Graphics;

import networking.project.game.Handler;
import networking.project.game.entities.Entity;
import networking.project.game.entities.creatures.Creature;
import networking.project.game.entities.creatures.Player;
import networking.project.game.gfx.Assets;
import networking.project.game.sound.Sound;

/**
 *	A Projectile is a moving Entity which deals damage to creatures.
 * 
 *	@author Miguel Millan
 *	@version 1.0
 *	@since version 1.0
 */
public class Projectile extends Creature{

	public static final int DEFAULT_PROJECTILE_WIDTH = 5,
							DEFAULT_PROJECTILE_HEIGHT = 20;
	protected int orientation; // 0 = up, 1 = down, 2 = right, 3 = left
	protected int counter = 0; 
	protected Entity creator; 
	
	public Projectile(Handler handler, Entity e, int orient, int offset) {
		super(handler, e.getX(), e.getY(), DEFAULT_PROJECTILE_WIDTH, DEFAULT_PROJECTILE_HEIGHT);
		orientation = orient;
		speed = handler.getGameCamera().getCamSpeed() + 5.0f;
		health = 1;
		creator = e;
		Sound.lazer.execute();//New jon edit
		//makes lazer sound while shooting
		if(orientation == 0){
			x += e.getWidth()/2 - width/2 + offset;
			y += -30;
			yMove = speed;
		}else if(orientation == 1){
			x += e.getWidth()/2 - width/2 + offset;
			y += e.getHeight() + 15;
			yMove = -speed;
		}else if(orientation == 2){
			x += e.getWidth() + 10;
			y += e.getHeight()/2 + offset;
			xMove = speed;
			width = DEFAULT_PROJECTILE_HEIGHT;
			height =DEFAULT_PROJECTILE_WIDTH;
		}else{
			x += -10;
			y += e.getHeight()/2 + offset;
			xMove = -speed;
			width = DEFAULT_PROJECTILE_HEIGHT;
			height =DEFAULT_PROJECTILE_WIDTH;
		}
		
		
		
	}

	@Override
	public void tick() {
		//Ensures that a projectile is eventually killed
		if(counter == 80)
			this.hurt(1);
		
		if(orientation == 0)
			yMove = speed;
		else if(orientation == 1)
			yMove = -speed;
		else if(orientation == 2)
			xMove = speed;
		else if(orientation == 3)
			xMove = -speed;
			
		
		if(!checkEntityCollisions(xMove, yMove)){
			y -= yMove;
			x += xMove;
		}else{
			checkAttack();
		}
		
		counter++;
		
	}
	
	/**
	 *  A projectile must be able to check if it is going to collide 
	 *  with another entity, and act accordingly.
	 */
	public void checkAttack(){
		for(Entity e: handler.getWorld().getEntityManager().getEntities()){
			//No Need to check for collision if e is this projectile or its creator.
			if(e.equals(this) || e.equals(creator))
				continue;
			if(e.getCollisionBounds(0, 0).intersects(getCollisionBounds(xMove,yMove))){
			
				// If this projectile collides with the player hurt it.
				if(e.getClass().equals(Player.class) && !e.getIsInvinc()){
					e.hurt(1);
				}
					
				// If the creator of this projectile is the player, then it should hurt
				// all other entities (except FireBalls).
				if(creator.getClass().equals(Player.class) && !e.getClass().equals(FireBall.class))
					e.hurt(1);
				
				// Regardless of whether or not the projectile deals damage,
				// if it has collided with an entity it must kill itself.
				this.hurt(1);
			}
		}
	}

	@Override
	public void render(Graphics g) {
		posX = (int)(x - handler.getGameCamera().getxOffset());
		posY = (int) (y - handler.getGameCamera().getyOffset());
		/*g.setColor(Color.blue);
		g.drawRect(posX, posY, width, height);*/
		g.drawImage(Assets.projectile, posX, posY, width, height, null);
		
	}

	@Override
	public void die() {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * Should be overwritten to return true if an Entity is a Projectile.
	 * @return false by default.
	 */
	public boolean isProjectile(){
		return true;
	}

}
