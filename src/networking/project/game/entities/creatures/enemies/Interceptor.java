package networking.project.game.entities.creatures.enemies;

import java.awt.Color;
import java.awt.Graphics;

import networking.project.game.Handler;
import networking.project.game.entities.creatures.Creature;
import networking.project.game.gfx.Assets;
import networking.project.game.sound.Sound;

/**
 *	An Interceptor is an Enemy which tracks down the player
 *	and suicides on them, causing damage.
 * 	
 *	@author Miguel Millan
 *	@version 1.0
 *	@since version 1.0
 */
public class Interceptor extends Enemy{
	
	public Interceptor(Handler handler, float x, float y) {
		
		super(handler, x, y);
		
		
	}
	
	@Override
	public void tick(){
		if (isOnScreen()){
			//Set x and y movement values
			AIMove();
			
			//Movement of the enemy
			move();
			
			//Attack of the enemy
			attack();
		}
		
	}
	
//	public void AIMove(){
//		if(handler.getPlayer().getX() - speed > x){
//			xMove = speed/2;
//		}else if (handler.getPlayer().getX()+speed < x){
//			xMove = -speed/2;
//		}else {
//			xMove =0;
//		}
//		
//		if(handler.getPlayer().getY() - speed > y){
//			yMove = speed;
//		}else if (handler.getPlayer().getY() + speed < y){
//			yMove = -speed;
//		}else {
//			yMove =0;
//		}
//		
//	}
	
	public void attack(){
		if(intersectWithPlayer()){
			hurtPlayer(5);
			this.hurt(this.health);
		}
	}
	
	
	@Override
	public void render(Graphics g) {
		posX = (int)(x - handler.getGameCamera().getxOffset());
		posY = (int) (y - handler.getGameCamera().getyOffset());
		/*g.setColor(Color.blue);
		g.drawRect(posX, posY, width, height);*/
		g.drawImage(Assets.interceptor, posX, posY, width, height, null);
		drawHealthBar(posX, posY, width, height, Creature.DEFAULT_HEALTH, health, 10,1, g);
	}

	@Override
	public void die() {
		Sound.explosion.execute();//New jon edit
	}

}
