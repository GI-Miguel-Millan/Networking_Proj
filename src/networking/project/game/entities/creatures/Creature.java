 package networking.project.game.entities.creatures;

import java.awt.Color;
import java.awt.Graphics;

import networking.project.game.Handler;
import networking.project.game.entities.Entity;
import networking.project.game.tiles.Tile;

/**
 *	Creatures are entities which move and have health.
 * 	
 *	@author Miguel Millan
 *	@version 1.0
 *	@since version 1.0
 */
public abstract class Creature extends Entity {

	public static final float DEFAULT_SPEED = 2.0f;
	public static final int DEFAULT_CREATURE_WIDTH = 64,
							DEFAULT_CREATURE_HEIGHT = 64;

	protected float speed;
	protected float xMove, yMove;
	protected boolean drawHealthBars = true;

	public Creature(Handler handler, float x, float y, int width, int height, int id) {
		super(handler, x, y, width, height, id);
		speed = DEFAULT_SPEED;
		xMove = 0;
		yMove = 0;
	}
	
	/**
	 *	Calls the Creatures Move methods if there are no Entity collisions.
	 */
	public void move(){
		if(!checkEntityCollisions(xMove, 0f))
			moveX();
		if(!checkEntityCollisions(0f, yMove))
			moveY();
	}
	
	/**
	 * Moves the Creature in the x direction.
	 */
	public void moveX(){
		if(xMove > 0){//Moving right
			int tx = (int) (x + xMove + bounds.x + bounds.width) / Tile.TILEWIDTH;
			
			if(!collisionWithTile(tx, (int) (y + bounds.y) / Tile.TILEHEIGHT) &&
					!collisionWithTile(tx, (int) (y + bounds.y + bounds.height) / Tile.TILEHEIGHT)){
				x += xMove;
			}else{
				x = tx * Tile.TILEWIDTH - bounds.x - bounds.width - 1;
			}
			
		}else if(xMove < 0){//Moving left
			int tx = (int) (x + xMove + bounds.x) / Tile.TILEWIDTH;
			
			if(!collisionWithTile(tx, (int) (y + bounds.y) / Tile.TILEHEIGHT) &&
					!collisionWithTile(tx, (int) (y + bounds.y + bounds.height) / Tile.TILEHEIGHT)){
				x += xMove;
			}else{
				x = tx * Tile.TILEWIDTH + Tile.TILEWIDTH - bounds.x;
			}
			
		}
	}
	
	/**
	 * Moves the Creature in the y direction.
	 */
	public void moveY(){
		if(yMove < 0){//Up
			int ty = (int) (y + yMove + bounds.y) / Tile.TILEHEIGHT;
			
			if(!collisionWithTile((int) (x + bounds.x) / Tile.TILEWIDTH, ty) &&
					!collisionWithTile((int) (x + bounds.x + bounds.width) / Tile.TILEWIDTH, ty)){
				y += yMove;
			}else{
				y = ty * Tile.TILEHEIGHT + Tile.TILEHEIGHT - bounds.y;
			}
			
		}else if(yMove > 0){//Down
			int ty = (int) (y + yMove + bounds.y + bounds.height) / Tile.TILEHEIGHT;
			
			if(!collisionWithTile((int) (x + bounds.x) / Tile.TILEWIDTH, ty) &&
					!collisionWithTile((int) (x + bounds.x + bounds.width) / Tile.TILEWIDTH, ty)){
				y += yMove;
			}else{
				y = ty * Tile.TILEHEIGHT - bounds.y - bounds.height - 1;
			}
			
		}
	}
	
	public void drawHealthBar(int x, int y, int width, int height , int maxHealth, int health, int offset, int scale, Graphics g){
		if(drawHealthBars){
			g.setColor(Color.red);
			g.fillRect(x - offset, y - offset, (((width + (2*offset))/maxHealth) * maxHealth)/scale, 5);
			g.setColor(Color.green);
			g.fillRect(x - offset, y - offset, (((width + (2*offset))/maxHealth) * health)/scale, 5);
			
//			g.setColor(Color.black);
//			for(int i = x - offset + ((width + (2*offset))/maxHealth)/scale; i < x - offset + (width + (2*offset)); i+=((width + (2*offset))/maxHealth)/scale)
//				g.fillRect(i,y - offset, 1, 10);
		}
		
	}
	
	/**
	 * Checks if the creature is colliding with a solid Tile.
	 * 
	 * @param x the x position of the Tile
	 * @param y the y position of the Tile
	 * @return true if the Tile is not solid
	 * @return false if the Tile is is solid
	 */
	protected boolean collisionWithTile(int x, int y){
		return handler.getWorld().getTile(x, y).isSolid();
	}
	
	public boolean isLegalSpawn(){
		int ty = (int) (y + yMove + bounds.y) / Tile.TILEHEIGHT;
		int tx = (int) (x + xMove + bounds.x + bounds.width) / Tile.TILEWIDTH;
		
		if(!collisionWithTile((int) (x + bounds.x) / Tile.TILEWIDTH, ty) &&
			!collisionWithTile((int) (x + bounds.x + bounds.width) / Tile.TILEWIDTH, ty) &&
			!collisionWithTile(tx, (int) (y + bounds.y) / Tile.TILEHEIGHT) &&
			!collisionWithTile(tx, (int) (y + bounds.y + bounds.height) / Tile.TILEHEIGHT)){
			return true;
		}else
			return false;
			
	}
	
	//GETTERS SETTERS

	/**
	 * @return xMove 
	 */
	public float getxMove() {
		return xMove;
	}

	/**
	 * @param xMove how far the creature will move in the x-direction
	 */
	public void setxMove(float xMove) {
		this.xMove = xMove;
	}

	/**
	 * @return yMove
	 */
	public float getyMove() {
		return yMove;
	}

	/**
	 * @param yMove how far the Creature will move in the y-direction
	 */
	public void setyMove(float yMove) {
		this.yMove = yMove;
	}

	/**
	 * @return speed the current speed of the Creature
	 */
	public float getSpeed() {
		return speed;
	}

	/**
	 * @param speed the new speed of the Creature
	 */
	public void setSpeed(float speed) {
		this.speed = speed;
	}
	
}
