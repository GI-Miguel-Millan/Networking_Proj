package networking.project.game.entities.creatures.enemies.bosses;

import java.awt.Color;
import java.awt.Graphics;

import networking.project.game.Handler;
import networking.project.game.entities.creatures.Creature;
import networking.project.game.entities.creatures.projectiles.FireBall;
import networking.project.game.entities.creatures.projectiles.Projectile;
import networking.project.game.gfx.Assets;
import networking.project.game.utils.Utils;

public class GiantHeadBoss extends Boss{
	private static final int HEAD_WIDTH = 96;
	private static final int HEAD_HEIGHT = 96;
	
	public GiantHeadBoss(Handler handler, float x, float y) {
		super(handler, x, y);
		this.width = HEAD_WIDTH;
		this.height = HEAD_HEIGHT;
		this.bounds.width = this.width;
		this.bounds.height = this.height;
	}

	@Override
	public void render(Graphics g) {
		posX = (int)(x - handler.getGameCamera().getxOffset());
		posY = (int) (y - handler.getGameCamera().getyOffset());
		g.setColor(Color.red);
		//g.drawRect(posX, posY, width, height);
		g.drawImage(Assets.giantHead, posX, posY, width, height, null);
		drawHealthBar(posX, posY, width, height, Boss.DEFAULT_BOSS_HEALTH, health, 15,1, g);
	}
	
	
	@Override
	public void AIMove(){
		if(counter <75){
			xMove = 5;
		}else if(counter < 150){
			xMove = -5;
		}else{
			counter = 0;
		}
		counter += 1;
	}
	
	@Override
	public void attack(){
		int randAttack = Utils.randomNum(0, 25);
		if(randAttack == 0){
			handler.getWorld().getEntityManager().addEntity(new FireBall(handler, this, 1,0));
		}
		
		collisionWithPlayer();
		
	}
	
	
}
