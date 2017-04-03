package networking.project.game.entities.creatures.projectiles;

import java.awt.Color;
import java.awt.Graphics;

import networking.project.game.Handler;
import networking.project.game.entities.Entity;
import networking.project.game.entities.creatures.Player;
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

	public FireBall(Handler handler, Player e, int id) {
		super(handler, e, id);
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
				if(e.getClass().equals(Player.class) && !e.getIsInvinc()){
					e.hurt(2);
				}
					
				// If the creator of this projectile is the player, then it should hurt
				// all other entities.
				if(creator.getClass().equals(Player.class)){
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
		posX = (x - handler.getGameCamera().getXOffset());
		posY = (y - handler.getGameCamera().getYOffset());
		g.setColor(Color.red);
		//g.drawRect(posX, posY, width, height);
		g.drawImage(Assets.fireBall, (int)posX, (int)posY, width, height, null);
		//g.drawImage(Assets.projectile, posX, posY, width, height, null);
		
	}
}
