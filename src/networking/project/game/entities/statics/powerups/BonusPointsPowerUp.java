package networking.project.game.entities.statics.powerups;

import java.awt.Color;
import java.awt.Graphics;

import networking.project.game.Handler;
import networking.project.game.gfx.Assets;

public class BonusPointsPowerUp extends PowerUp{

	public BonusPointsPowerUp(Handler handler, float x, float y) {
		super(handler, x, y);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void render(Graphics g) {
		posX = (int)(x - handler.getGameCamera().getxOffset());
		posY = (int) (y - handler.getGameCamera().getyOffset());
		//g.setColor(Color.blue);
		//g.drawRect(posX, posY, width, height);
		g.drawImage(Assets.star, posX, posY, width, height, null);
		
	}
	
	public void die(){
		active = false;
		handler.getPlayer().addScore(250);
	}
	
}
