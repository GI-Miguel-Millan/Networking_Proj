package networking.project.game.entities.creatures.enemies.bosses;

import java.awt.Color;
import java.awt.Graphics;

import networking.project.game.Handler;
import networking.project.game.entities.creatures.Creature;
import networking.project.game.entities.creatures.projectiles.Projectile;
import networking.project.game.gfx.Assets;
import networking.project.game.utils.Utils;

public class MothershipBoss extends Boss{
	private static final int MOTHERSHIP_WIDTH = 96;
	private static final int MOTHERSHIP_HEIGHT = 96;
	private boolean readySpawn =true;
	private int spawnTimer = 0;
	
	public MothershipBoss(Handler handler, float x, float y) {
		super(handler, x, y);
		this.width = MOTHERSHIP_WIDTH;
		this.height = MOTHERSHIP_HEIGHT;
		this.bounds.width = this.width;
		this.bounds.height = this.height;
	}

	@Override
	public void render(Graphics g) {
		posX = (int)(x - handler.getGameCamera().getxOffset());
		posY = (int) (y - handler.getGameCamera().getyOffset());
		g.setColor(Color.yellow);
		//g.drawRect(posX, posY, width, height);
		g.drawImage(Assets.motherShip, posX, posY, width, height, null);
		drawHealthBar(posX, posY, width, height, Boss.DEFAULT_BOSS_HEALTH, health, 10,1, g);
	}

	@Override
	public void AIMove(){
		if(counter <75){
			xMove = 3;
		}else if(counter < 150){
			xMove = -3;
		}else{
			counter = 0;
		}
		counter += 1;
		
	}
	
	@Override
	public void attack(){
		int randAttack = Utils.randomNum(0, 100);
		if(randAttack == 0){
			handler.getWorld().getEntityManager().addEntity(new Projectile(handler, this, getProjectileOrientation(),0));
		}else if(randAttack == 1){
			if (getProjectileOrientation() == 0 || getProjectileOrientation() == 1){
				handler.getWorld().getEntityManager().addEntity
						(new Projectile(handler, this, getProjectileOrientation(), -(width/2)));
				handler.getWorld().getEntityManager().addEntity
						(new Projectile(handler, this, getProjectileOrientation(),0));
				handler.getWorld().getEntityManager().addEntity
						(new Projectile(handler, this, getProjectileOrientation(), width/2));
			}else{
				handler.getWorld().getEntityManager().addEntity
				(new Projectile(handler, this, getProjectileOrientation(), -(height/2)));
				handler.getWorld().getEntityManager().addEntity
						(new Projectile(handler, this, getProjectileOrientation(),0));
				handler.getWorld().getEntityManager().addEntity
						(new Projectile(handler, this, getProjectileOrientation(), height/2));
			}
			
		}
		
		if(randAttack >= 75 && readySpawn){
			handler.getWorld().getEntityManager().spawnEnemy(handler, (int)(x + width - 10), (int)(y + height + 10), 5);
			handler.getWorld().getEntityManager().spawnEnemy(handler, (int)(x + 10), (int)(y + height + 10), 5);
			handler.getWorld().getEntityManager().spawnEnemy(handler, (int)(x + width + 10), (int)(y + height/2), 5);
			handler.getWorld().getEntityManager().spawnEnemy(handler, (int)(x - 30), (int)(y + height/2), 5);
			readySpawn = false;
		}
		
		if(!readySpawn){
			spawnTimer++;
		}
		
		if(spawnTimer >= 300){
			spawnTimer = 0;
			readySpawn = true;
		}
		
		collisionWithPlayer();
	}
}
