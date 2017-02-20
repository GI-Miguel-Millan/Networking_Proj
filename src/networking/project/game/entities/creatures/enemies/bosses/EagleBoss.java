package networking.project.game.entities.creatures.enemies.bosses;

import java.awt.Color;
import java.awt.Graphics;

import networking.project.game.Handler;
import networking.project.game.entities.creatures.Creature;
import networking.project.game.entities.creatures.projectiles.DarkLaser;
import networking.project.game.entities.creatures.projectiles.FireBall;
import networking.project.game.entities.creatures.projectiles.Projectile;
import networking.project.game.gfx.Assets;
import networking.project.game.tiles.Tile;
import networking.project.game.utils.Utils;

public class EagleBoss extends Boss{
	private static final int EAGLE_WIDTH = 96;
	private static final int EAGLE_HEIGHT = 64;
	private int holdDistance = 100;
	private boolean canMove =false;
	private boolean waitHalfSec = false;
	private int reverseCounter =0;
	private int shootInterval = 0;
	private int waitTimer =0;
	private int dir = 0;
	
	public EagleBoss(Handler handler, float x, float y) {
		super(handler, x, y);
		this.width = EAGLE_WIDTH;
		this.height = EAGLE_HEIGHT;
		this.bounds.width = this.width;
		this.bounds.height = this.height;
		this.speed = 3;
	}

	@Override
	public void render(Graphics g) {
		posX = (int)(x - handler.getGameCamera().getxOffset());
		posY = (int) (y - handler.getGameCamera().getyOffset());
		g.setColor(Color.blue);
		//g.drawRect(posX, posY, width, height);
		g.drawImage(Assets.eagle, posX, posY, width, height, null);
		drawHealthBar(posX, posY, width, height, Boss.DEFAULT_BOSS_HEALTH, health, 10,1, g);
//		g.drawLine((int)posX+width/2, (int)posY+height/2, (int)(handler.getPlayer().getX() + handler.getPlayer().getWidth()/2 - handler.getGameCamera().getxOffset())
//				, (int)(handler.getPlayer().getY() + handler.getPlayer().getHeight()/2 - handler.getGameCamera().getyOffset()));
	}
	
	@Override
	public void tick(){
		if (isOnScreen()){
			canMove = true;
		}
		if(canMove){
			playerX = (int)handler.getWorld().getEntityManager().getPlayer().getX();
			playerY = (int)handler.getWorld().getEntityManager().getPlayer().getY();
			
			//Attack of the enemy
			if(!waitHalfSec)
				attack();
			else
				waitTimer++;
			
			//Set x and y movement values
			AIMove();
			
			//Movement of the enemy
			move();
			
			
			
			if(waitTimer == 15){
				waitHalfSec = false;
				waitTimer =0;
			}
		}
		
		
	}
	
	@Override
	public void AIMove(){
		if(dir == 0)
			moveDownRight();
		else if(dir == 1)
			moveDownLeft();
		else if(dir == 2)
			moveUpLeft();
		else if(dir == 3)
			moveUpRight();
		
		if(reverseCounter < 130)
			dir = 0;
		else if(reverseCounter < 180)
			dir = 1;
		else if(reverseCounter < 310)
			dir = 2;
		else if(reverseCounter < 360)
			dir = 3;
		else 
			reverseCounter =0;
		
//		int centeredPlayerX = (int)(playerX + handler.getPlayer().getWidth()/2);
//		int centeredPlayerY = (int)(playerY + handler.getPlayer().getHeight()/2);
//		int centeredX = (int)(x + width/2);
//		int centeredY = (int)(y + height/2);
//		if(reverseCounter <= 400){
//				//down right
//			if(centeredPlayerX <= centeredX && centeredPlayerY >= centeredY + holdDistance){
//				moveDownRight();
//				
//				//down left
//			}else if( centeredPlayerX <= centeredX - holdDistance && centeredPlayerY <= centeredY){
//				moveDownLeft();
//				
//				//up left
//			}else if( centeredPlayerX >= centeredX && centeredPlayerY <= centeredY - holdDistance){
//				moveUpLeft();
//				
//				//up right
//			}else if( centeredPlayerX >= centeredX + holdDistance && centeredPlayerY >= centeredY){
//				moveUpRight();
//			}
//		}else if(reverseCounter <= 800){
//			if(centeredPlayerX <= centeredX && centeredPlayerY <= centeredY + holdDistance){
//				moveUpRight();
//				
//			}else if( centeredPlayerX <= centeredX - holdDistance && centeredPlayerY >= centeredY){
//				moveUpLeft();
//				
//			}else if( centeredPlayerX >= centeredX && centeredPlayerY >= centeredY - holdDistance){
//				moveDownLeft();
//				
//			}else if( centeredPlayerX >= centeredX + holdDistance && centeredPlayerY <= centeredY){
//				moveDownRight();
//			}
//		}else{
//			reverseCounter =0;
//		}
		
		reverseCounter++;
	}
	
	public void moveDownRight(){
		xMove = speed;
		if(y < (((handler.getHeight()))))
			yMove = speed;
		else
			yMove =0;
	}
	
	public void moveDownLeft(){
		xMove = -speed;
		yMove = speed;
		if(y < (((handler.getHeight()))))
			yMove = speed;
		else
			yMove = 0;
	}
	
	public void moveUpLeft(){
		xMove = -speed;
		yMove = -speed;
	}
	public void moveUpRight(){
		xMove = speed;
		yMove = -speed;
	}
	
	@Override
	public void attack(){
		shootInterval++;
		if(shootInterval % 15 == 0){
			handler.getWorld().getEntityManager().addEntity(new DarkLaser(handler, this, getProjectileOrientation(),0));
		}
		
		if(shootInterval % 60 == 0){
			handler.getWorld().getEntityManager().addEntity(new DarkLaser(handler, this, getProjectileOrientation(), width/2));
			handler.getWorld().getEntityManager().addEntity(new DarkLaser(handler,this, getProjectileOrientation(), -width/2));
			waitHalfSec = true;
		}
		
		collisionWithPlayer();
	}
	
}
