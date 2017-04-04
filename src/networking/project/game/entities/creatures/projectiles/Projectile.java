package networking.project.game.entities.creatures.projectiles;

import networking.project.game.Handler;
import networking.project.game.entities.Entity;
import networking.project.game.entities.creatures.Creature;
import networking.project.game.entities.creatures.Player;
import networking.project.game.gfx.Assets;
import networking.project.game.sound.Sound;

import java.awt.*;
import java.awt.geom.AffineTransform;

/**
 *	A Projectile is a moving Entity which deals damage to creatures.
 * 
 *	@author Miguel Millan
 *	@version 1.0
 *	@since version 1.0
 */
public class Projectile extends Creature{

	public static final int DEFAULT_PROJECTILE_WIDTH = 15,
							DEFAULT_PROJECTILE_HEIGHT = 25;
	protected int counter = 0; 
	protected Player creator; 
	protected float mouseX, mouseY;
	protected double rotation;
	
	public Projectile(Handler handler, Player e, float mX, float mY, int id) {
		super(handler, e.getX() + e.getWidth()/2.0f, e.getY() + e.getHeight()/2.0f, DEFAULT_PROJECTILE_WIDTH, DEFAULT_PROJECTILE_HEIGHT, id);
		mouseX=mX;
		mouseY=mY;
		speed = handler.getGameCamera().getCamSpeed() + 5.0f;
		health = 1;
		creator = e;
		Sound.lazer.execute();//makes lazer sound while shooting
		
		handler.getGameCamera().checkBlankSpace();
		x -= DEFAULT_PROJECTILE_WIDTH / 2.0f;
		// Adjust projectile xMove and yMove so that it will travel in the direction of the clients mouse position
		posX = x;
		posY = y;
        rotation = e.getRotation();
		float r = (float) Math.sqrt(Math.pow(mouseX - posX, 2.0) + Math.pow(posY - mouseY, 2.0));
		float speedX = speed * ( (mouseX - posX)/ r);
		float speedY = speed * ( (posY - mouseY)/ r);
		
		xMove = speedX;
		yMove = speedY;
		//System.out.println("speedX: "+ speedX + ", speedY: " + speedY);
		System.out.println("poX: " + posX + ", posY: " + posY + ", mX: " + mouseX + " " + ", mY: " +  mouseY + ", camX:" + creator.getCamX() + ", camY: " + creator.getCamX());
	
	}

	@Override
	public void tick() {
		//Ensures that a projectile is eventually killed
		if(counter == 100)
			this.hurt(1);
			
		
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
	
	/**
	 * Checks for collision between entities by checking for an intersection
	 * between boundaries of each entity.
	 * 
	 * @param xOffset - how far away from the entity in the x direction to check for intersections
	 * @param yOffset - how far away from the entity in the y direction to check for intersections
	 * @return true there is a collision
	 * @return false if there is no collision
	 */
	public boolean checkEntityCollisions(float xOffset, float yOffset){
		// Loop through each entity that exist in a world.
		for(Entity e : handler.getWorld().getEntityManager().getEntities()){
			
			// Skip this entity, no need to check for self collision, also ignore Projectiles since
			// they have their own method to check for collision with other entities.
			if(e.getID() == creator.getID() || e.isProjectile())
				continue;
			
			// Compare the collision bounds of the other entity, with the collision bounds of this entity.
			if(e.getCollisionBounds(0f, 0f).intersects(getCollisionBounds(xOffset, yOffset))){
				collidedWith = handler.getWorld().getEntityManager().getIndex(e);
				return true;
			}
				
		}
		return false;
	}

	@Override
	public void render(Graphics g) {
		posX = (x - handler.getGameCamera().getXOffset());
		posY = (y - handler.getGameCamera().getYOffset());
		/*g.setColor(Color.blue);
		g.drawRect(posX, posY, width, height);*/
        Graphics2D g2 = (Graphics2D)g;
        AffineTransform transform = g2.getTransform();
        g2.rotate(rotation, posX + width / 2.0, posY + width / 2.0);
		g2.drawImage(Assets.projectile, (int)posX, (int)posY, width, height, null);
        g2.setTransform(transform);
	}

	@Override
	public void die() {
		handler.getK_ID().add(this.ID);
		
	}
	
	/**
	 * Should be overwritten to return true if an Entity is a Projectile.
	 * @return false by default.
	 */
	public boolean isProjectile(){
		return true;
	}
	
	public Entity getCreator(){
		return creator;
	}
	
	/*public int getMouseX(){
		return mouseX;
	}
	
	public int getMouseY(){
		return mouseY;
	}*/

}
