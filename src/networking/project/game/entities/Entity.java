package networking.project.game.entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Iterator;
import java.util.Set;
import java.util.Map.Entry;

import networking.project.game.Handler;
import networking.project.game.entities.creatures.projectiles.Projectile;

/**
 *  An Entity is anything in the game which is not a Tile. 
 *	It defines the dimensions, boundaries, and position
 *	on the screen of an Entity.
 * 	
 *	@author Miguel Millan
 *	@version 1.0
 *	@since version 1.0
 */
public abstract class Entity {

	public static final int DEFAULT_HEALTH = 1;
	
	protected int health;
	protected Handler handler;
	protected float x, y;
	protected int posX, posY;
	protected int width, height;
	protected Rectangle bounds;
	protected boolean active = true;
	//protected int collidedWith;
	protected boolean isInvinc = false;
	
	protected final int ID;
	
	public Entity(Handler handler, float x, float y, int width, int height, int id){
		this.handler = handler;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.ID = id;
		health = DEFAULT_HEALTH;
		
		bounds = new Rectangle(0, 0, width, height);
	}
	
	public abstract void tick();
	
	public abstract void render(Graphics g);
	
	public abstract void die();
	
	/**
	 * By default, any entity which calls its hurt method will lose health
	 * based on the amount of damage it received. If the health of the entity
	 * ever reaches 0, then it will become inactive and its die method is called.
	 * 
	 * @param amt the amount of damage an Entity takes
	 */
	public void hurt(int amt){
		health -= amt;
		if(health <= 0){
			active = false;
			die();
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
			if(e.getID() == this.ID || e.isProjectile())
				continue;
			
			// Compare the collision bounds of the other entity, with the collision bounds of this entity.
			if(e.getCollisionBounds(0f, 0f).intersects(getCollisionBounds(xOffset, yOffset))){
				//collidedWith = handler.getWorld().getEntityManager().getIndex(e);
				return true;
			}
		}
		
		
		return false;
	}
	
	/**
	 * Should be overwritten to return true if an Entity is an Enemy.
	 * @return false by default.
	 */
	public boolean isEnemy(){
		return false;
	}
	
	/**
	 * Should be overwritten to return true if an Entity is a Projectile.
	 * @return false by default.
	 */
	public boolean isProjectile(){
		return false;
	}
	
	/**
	 * Returns a rectangle specifying the collision bounds for an Entity.
	 * 
	 * @param xOffset
	 * @param yOffset
	 * @return Rectangle with collision bounds.
	 */
	public Rectangle getCollisionBounds(float xOffset, float yOffset){
		return new Rectangle((int) (x + bounds.x + xOffset), (int) (y + bounds.y + yOffset), bounds.width, bounds.height);
	}

	/**
	 * Any Entity that is not active is removed from the entity manager 
	 * upon the next tick of the clock.
	 * 
	 * @return true if an Entity is active
	 * @return false if an Entity is inactive
	 */
	public boolean isActive(){
		return active;
	}
	
	/**
	 * Set whether or not an entity is active.
	 * @param b
	 */
	public void setActive(boolean b){
		active = b;
	}
	
	/**
	 * @return x the x position of the entity
	 */
	public float getX() {
		return x;
	}

	/**
	 * Set the x position of the Entity
	 * 
	 * @param x
	 */
	public void setX(float x) {
		this.x = x;
	}
	
	/**
	 * @return y the y position of the Entity
	 */
	public float getY() {
		return y;
	}

	/**
	 * Set the y position of the Entity
	 * 
	 * @param y
	 */
	public void setY(float y) {
		this.y = y;
	}
	
	/**
	 * @return width the Width of the Entity
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Set the width of the Entity
	 * 
	 * @param width
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * @return height the height of the Entity
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Set the Height of the Entity
	 * 
	 * @param height
	 */
	public void setHeight(int height) {
		this.height = height;
	}
	
	/**
	 * @return health the current health of the Creature
	 */
	public int getHealth() {
		return health;
	}

	/**
	 * @param health the new health of the Creature
	 */
	public void setHealth(int health) {
		this.health = health;
	}
	
	/**
	 * @return true if this entity is invincible, false otherwise
	 */
	public boolean getIsInvinc(){
		return isInvinc;
	}
	
	/**
	 * @return integer ID of this entity
	 */
	public int getID(){
		return this.ID;
	}
}
