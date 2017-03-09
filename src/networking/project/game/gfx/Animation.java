package networking.project.game.gfx;

import java.awt.image.BufferedImage;

/**
 *	An Animation is an array of Buffered images which loop on
 *	a specified time interval.
 * 	
 *	@author 
 *	@version 1.0
 *	@since version 1.0
 */
public class Animation {
	
	private int speed, index;
	private long lastTime, timer;
	private BufferedImage[] frames;
	
	public Animation(int speed, BufferedImage[] frames){
		this.speed = speed;
		this.frames = frames;
		index = 0;
		timer = 0;
		lastTime = System.currentTimeMillis();
	}
	
	/**
	 *  Increments the index used to change which image is 
	 *  the current frame of the animation.
	 */
	public void tick(){
		timer += System.currentTimeMillis() - lastTime;
		lastTime = System.currentTimeMillis();
		
		if(timer > speed){
			index++;
			timer = 0;
			if(index >= frames.length)
				index = 0;
		}
	}
	
	/**
	 * @return the current frame at index.
	 */
	public BufferedImage getCurrentFrame(){
		return frames[index];
	}
	
	/**
	 * @return true if this Animation is on the last frame, false otherwise.
	 */
	public boolean onLastFrame(){
		return (frames[index].equals(frames[frames.length-1]));
	}
	
}
