package networking.project.game.entities.creatures.projectiles;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Iterator;
import java.util.Set;
import java.util.Map.Entry;

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
							DEFAULT_PROJECTILE_HEIGHT = 5;
	protected int counter = 0; 
	protected Player creator; 
	protected int mouseX, mouseY;
	
	public Projectile(Handler handler, Player e, int mX, int mY, int id) {
		super(handler, e.getX() + e.getWidth()/2, e.getY() + e.getHeight()/2, DEFAULT_PROJECTILE_WIDTH, DEFAULT_PROJECTILE_HEIGHT, id);
		mouseX=mX;
		mouseY=mY;
		speed = handler.getGameCamera().getCamSpeed() + 5.0f;
		health = 1;
		creator = e;
		Sound.lazer.execute();//makes lazer sound while shooting
		
		handler.getGameCamera().checkBlankSpace();
		
		
		// Adjust projectile xMove and yMove so that it will travel in the direction of the clients mouse position
		posX = (int)(x - creator.getCamX());
		posY = (int) (y - creator.getCamY());
		float r = (float) Math.sqrt(Math.pow(mouseX - posX, 2) + Math.pow(posY - mouseY, 2));
		float speedX = speed * ( (mouseX - posX)/ r);
		float speedY = speed * ( (posY - mouseY)/ r);
		
		xMove = speedX;
		yMove = speedY;
		//System.out.println("speedX: "+ speedX + ", speedY: " + speedY);
		//System.out.println("poX: " + posX + ", posY: " + posY + ", mX: " + mouseX + " " + ", mY: " +  mouseY);
	
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
		

		Set<Entry<Integer, Entity>> set = handler.getWorld().getEntityManager().getEntities().entrySet();
		
		Iterator<Entry<Integer, Entity>> iter = set.iterator();
		
		// Loop through each entity that exist in a world.
		while(iter.hasNext()){
			Entity e = iter.next().getValue();
			
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
		
		Set<Entry<Integer, Entity>> set = handler.getWorld().getEntityManager().getEntities().entrySet();
		
		Iterator<Entry<Integer, Entity>> iter = set.iterator();
		
		// Loop through each entity that exist in a world.
		while(iter.hasNext()){
			Entity e = iter.next().getValue();
			
			// Skip this entity, no need to check for self collision, also ignore Projectiles since
			// they have their own method to check for collision with other entities.
			if(e.getID() == creator.getID() || e.isProjectile())
				continue;
			
			// Compare the collision bounds of the other entity, with the collision bounds of this entity.
			if(e.getCollisionBounds(0f, 0f).intersects(getCollisionBounds(xOffset, yOffset))){
				//collidedWith = handler.getWorld().getEntityManager().getIndex(e);
				return true;
			}
		}
		
		return false;
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
		handler.getK_ID().add(this.ID);
		
	}
	
	/**
	 * Should be overwritten to return true if an Entity is a Projectile.
	 * @return false by default.
	 */
	public boolean isProjectile(){
		return true;
	}
	
	/**
	 * @return creator - the reference to the Entity which spawned this Projectile.
	 */
	public Entity getCreator(){
		return creator;
	}
	
	/**
	 * @return the x position of the mouse when this Projectile was spawned.
	 */
	public int getMouseX(){
		return mouseX;
	}
	
	/**
	 * @return the y position of the mouse when this Projectile was spawned.
	 */
	public int getMouseY(){
		return mouseY;
	}

}
