package networking.project.game.entities.statics.powerups;

import java.awt.Color;
import java.awt.Graphics;

import networking.project.game.Handler;
import networking.project.game.gfx.Assets;

public class HpUpPowerUp extends PowerUp{

	public HpUpPowerUp(Handler handler, float x, float y) {
		super(handler, x, y);
		// TODO Auto-generated constructor stub
	}

	
	@Override
	public void render(Graphics g) {
		posX = (int)(x - handler.getGameCamera().getxOffset());
		posY = (int) (y - handler.getGameCamera().getyOffset());
		//g.setColor(Color.red);
		//g.drawRect(posX, posY, width, height);
		g.drawImage(Assets.heart, posX, posY, width, height, null);
		
	}
	
	public void die(){
		active = false;
//		if(handler.getPlayer().getHealth() + 4 < 50)
//			handler.getPlayer().setHealth(handler.getPlayerHealth() + 4);
//		else 
//			handler.getPlayer().setHealth(50);
	}
}
