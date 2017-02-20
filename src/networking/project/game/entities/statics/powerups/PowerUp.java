package networking.project.game.entities.statics.powerups;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import networking.project.game.Handler;
import networking.project.game.entities.Entity;
import networking.project.game.entities.statics.StaticEntity;
import networking.project.game.utils.Utils;

public class PowerUp extends StaticEntity{

	public static final int DEFAULT_POWERUP_WIDTH = 20,
							DEFAULT_POWERUP_HEIGHT = 20,
							DEFAULT_POWERUP_HP = 1;
	public static ArrayList<PowerUp> powerUps = new ArrayList<PowerUp>();
	private int counter = 0;
	
	
	public PowerUp(Handler handler, float x, float y) {
		super(handler, x, y, DEFAULT_POWERUP_WIDTH, DEFAULT_POWERUP_HEIGHT);
		this.health = DEFAULT_HEALTH;
		
	}

	@Override
	public void tick() {
		if(handler.getPlayer().getCollisionBounds(0, 0).intersects(getCollisionBounds(0,0))){
			this.die();
		}
		
		counter++;
		if(counter >= 300){
			active = false;
			counter = 0;
		}
	}

	@Override
	public void render(Graphics g) {
		posX = (int)(x - handler.getGameCamera().getxOffset());
		posY = (int) (y - handler.getGameCamera().getyOffset());
		g.setColor(Color.yellow);
		g.drawRect(posX, posY, width, height);
		
	}
	
	public static void spawnPowerUp(Handler handler, int x, int y){
		int rndInt = Utils.randomNum(0, 100);
		
		if(rndInt < 25){
			powerUps.add(new BonusPointsPowerUp(handler, x, y));
		}else if(rndInt >= 25 && rndInt < 45){
			powerUps.add(new SplitShotPowerUp(handler, x, y));
		}else if(rndInt >= 45 && rndInt < 50){
			powerUps.add(new InvincibilityPowerUp(handler, x ,y));
		}else if(rndInt >= 50 && rndInt < 60){
			powerUps.add(new SpdUpPowerUp(handler, x, y));
		}else if(rndInt >= 60 && rndInt < 90){
			powerUps.add(new HpUpPowerUp(handler, x, y));
		}
	}

	/**
	 * This method needs to be overridden in order to determine the effect 
	 * the powerup has on the player.
	 */
	public void die() {
		active = false;
	}

	
	
}
