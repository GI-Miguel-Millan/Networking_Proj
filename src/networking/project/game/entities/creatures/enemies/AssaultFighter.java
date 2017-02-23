package networking.project.game.entities.creatures.enemies;

import java.awt.Color;
import java.awt.Graphics;

import networking.project.game.Handler;
import networking.project.game.entities.creatures.Creature;
import networking.project.game.gfx.Assets;
import networking.project.game.sound.Sound;
import networking.project.game.utils.Utils;

/**
 *	An Assault Fighter is a standard space fighter which shoots projectiles at the player.
 * 	
 *	@author Miguel Millan
 *	@version 1.0
 *	@since version 1.0
 */
public class AssaultFighter extends Enemy{

	public AssaultFighter(Handler handler, float x, float y) {
		super(handler, x, y);
		// TODO Auto-generated constructor stub
	}

	
	
	@Override
	public void render(Graphics g) {
		posX = (int)(x - handler.getGameCamera().getxOffset());
		posY = (int) (y - handler.getGameCamera().getyOffset());
		/*g.setColor(Color.blue);
		g.drawRect(posX, posY, width, height);*/
		g.drawImage(Assets.assault, posX, posY, width, height, null);
		drawHealthBar(posX, posY, width, height, Creature.DEFAULT_HEALTH, health, 10,1, g);
		
	}
	
	@Override
	public void AIMove(){
		if(counter <100){
			xMove = 4;
		}else if(counter < 200){
			xMove = -4;
		}else{
			counter = 0;
		}
		counter += Utils.randomNum(1, 2);
	}
	
	public void collisionWithPlayer(){
		
		if(intersectWithPlayer()){
			hurtPlayer(10);
			this.hurt(this.health);
		}
}

	@Override
	public void die() {
		
		Sound.explosion.execute();//New jon edit
		
	}
}
