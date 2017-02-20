package networking.project.game.entities.creatures.projectiles;

import java.awt.Color;
import java.awt.Graphics;

import networking.project.game.Handler;
import networking.project.game.entities.Entity;
import networking.project.game.gfx.Assets;

/**
 *	A FireBall is a slow moving projectile which deals more damage and does not kill itself
 *	on projectiles.
 * 
 *	@author Miguel Millan
 *	@version 1.0
 *	@since version 1.0
 */
public class FireBall extends Projectile {

	public FireBall(Handler handler, Entity e, int orient, int offset) {
		super(handler, e, orient, offset);
		speed = 3.0f;
		width = 20;
		height = 20;
		bounds.width = this.width;
		bounds.height = this.height;
	}

	
	/**
	 *  A FireBall must be able to check if it is going to collide 
	 *  with another entity, and act accordingly. It will destroy other projectiles
	 *  and deals 2 damage.
	 */
	public void checkAttack(){
		for(Entity e: handler.getWorld().getEntityManager().getEntities()){
			//No Need to check for collision if e is this projectile or its creator.
			if(e.equals(this) || e.equals(creator) || e.getClass().equals(Projectile.class))
				continue;
			if(e.getCollisionBounds(0, 0).intersects(getCollisionBounds(0,yMove))){
			
				// If this projectile collides with the player hurt it.
				if(e.equals(handler.getWorld().getEntityManager().getPlayer()) && !handler.getPlayer().getIsInvinc()){
					e.hurt(2);
				}
					
				// If the creator of this projectile is the player, then it should hurt
				// all other entities.
				if(creator.equals(handler.getWorld().getEntityManager().getPlayer())){
					e.hurt(2);
				}
				
				if(!e.getClass().equals(Projectile.class))
					this.hurt(1);
			}
		}
	}
	
	@Override
	public void tick() {
		//Ensures that a projectile is eventually killed
		if(counter == 200)
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
	
	@Override
	public void render(Graphics g) {
		posX = (int)(x - handler.getGameCamera().getxOffset());
		posY = (int) (y - handler.getGameCamera().getyOffset());
		g.setColor(Color.red);
		//g.drawRect(posX, posY, width, height);
		g.drawImage(Assets.fireBall, posX, posY, width, height, null);
		//g.drawImage(Assets.projectile, posX, posY, width, height, null);
		
	}
}
