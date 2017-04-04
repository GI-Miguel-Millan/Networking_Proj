package networking.project.game.gfx;

import networking.project.game.Handler;
import networking.project.game.entities.Entity;
import networking.project.game.tiles.Tile;

/**
 *	The GameCamera specifies which part of the game is displayed.
 * 	
 *	@author 
 *	@version 1.0
 *	@since version 1.0
 */
public class GameCamera {
	
	private Handler handler;
	private float xOffset, yOffset;
	private boolean cameraStop = false;
	public static final int DEFAULT_CAMSPEED = 0;
	private int camSpeed = DEFAULT_CAMSPEED;
	
	public GameCamera(Handler handler, float xOffset, float yOffset){
		this.handler = handler;
		this.xOffset = xOffset;
		this.yOffset = yOffset;
	}
	
	/**
	 *  Ensures that no blank spaces are shown by making
	 *  sure the xOffset and yOffset is never outside of the dimensions of the world.
	 */
	public void checkBlankSpace(){
		
		//System.out.println("xoff: " + xOffset + ", yoff: " + yOffset);
		
		if(xOffset < 0){
			xOffset = 0;
		}else if(xOffset > handler.getWorld().getWidth() * Tile.TILEWIDTH - handler.getWidth()){
			xOffset = handler.getWorld().getWidth() * Tile.TILEWIDTH - handler.getWidth();
		}
		
		if(yOffset < 0){
			yOffset = 0;
		}else if(yOffset > handler.getWorld().getHeight() * Tile.TILEHEIGHT - handler.getHeight()){
			yOffset = handler.getWorld().getHeight() * Tile.TILEHEIGHT - handler.getHeight();
		}
		
		//System.out.println("After transformation: xoff: " + xOffset + ", yoff: " + yOffset);
	}
	
	/**
	 * Centers the GameCamera on the specified entity.
	 * 
	 * @param e
	 */
	public void centerOnEntity(Entity e){
		if(e != null){
			xOffset = e.getX() - handler.getWidth() / 2 + e.getWidth() / 2;
			//xOffset = Tile.TILEWIDTH/2 + 5;
			yOffset = e.getY() - handler.getHeight() / 2 + e.getHeight() / 2;
			checkBlankSpace();
		}
		
	}
	
	public void centerOnCursor(){
		xOffset = handler.getMouseManager().getMouseX() - handler.getWidth() / 2 + handler.getMouseManager().getMouseX() / 2;
		yOffset = handler.getMouseManager().getMouseY() - handler.getHeight() / 2 + handler.getMouseManager().getMouseY() / 2;
		checkBlankSpace();
	}
	
	/**
	 * Moves the GameCamera at a constant speed.
	 * 
	 * @param e
	 */
	public void staticCamera(Entity e){
		//xOffset = e.getX() - handler.getWidth() / 2 + e.getWidth() / 2;
		if(cameraStop)
			camSpeed=0;
		xOffset = Tile.TILEWIDTH/2 + 5;
		yOffset -= 1;
		checkBlankSpace();
	}
	
	/**
	 * Moves the GameCamera.
	 * 
	 * @param xAmt amount to move in the x direction
	 * @param yAmt amount to move in the y direction
	 */
	public void move(float xAmt, float yAmt){
		xOffset += xAmt;
		yOffset += yAmt;
		checkBlankSpace();
	}

	/**
	 * @return xOffset
	 */
	public float getXOffset() {
		return xOffset;
	}

	/**
	 * @param xOffset how far to offset the GameCamera in the x direction.
	 */
	public void setXOffset(float xOffset) {
		this.xOffset = xOffset;
	}

	/**
	 * @return yOffset
	 */
	public float getYOffset() {
		return yOffset;
	}

	/**
	 * @param yOffset how far to offset the GameCamera in the y direction.
	 */
	public void setyOffset(float yOffset) {
		this.yOffset = yOffset;
	}

	public int getCamSpeed(){
		return camSpeed;
	}
}
