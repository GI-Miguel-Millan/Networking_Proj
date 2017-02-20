package networking.project.game.gfx;

import java.awt.image.BufferedImage;

/**
 *	Stores a buffered image of the sprite sheet from which 
 *	we can crop out the individual textures.
 * 	
 *	@author 
 *	@version 1.0
 *	@since version 1.0
 */
public class SpriteSheet {

	private BufferedImage sheet;
	
	public SpriteSheet(BufferedImage sheet){
		this.sheet = sheet;
	}
	
	/**
	 * Crops the sprite sheet to get the desired image. 
	 * 
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @return a sub-image of sheet
	 */
	public BufferedImage crop(int x, int y, int width, int height){
		return sheet.getSubimage(x, y, width, height);
	}
	
}
