package networking.project.game.entities.creatures.projectiles;

import java.awt.Color;
import java.awt.Graphics;

import networking.project.game.Handler;
import networking.project.game.entities.Entity;
import networking.project.game.entities.creatures.Player;
import networking.project.game.gfx.Assets;

/**
 *	A DarkLaser is a faster projectile with a different texture.
 * 
 *	@author Miguel Millan
 *	@version 1.0
 *	@since version 1.0
 */
public class DarkLaser extends Projectile {

	public DarkLaser(Handler handler, Player e, int orient, int offset, int id) {
		super(handler, e, orient, offset, id);
		speed = 8.0f + handler.getGameCamera().getCamSpeed();
	}
	
	@Override
	public void render(Graphics g) {
		posX = (int)(x - handler.getGameCamera().getxOffset());
		posY = (int) (y - handler.getGameCamera().getyOffset());
		g.setColor(Color.red);
		//g.drawRect(posX, posY, width, height);
		g.drawImage(Assets.darkLaser, posX, posY, width, height, null);
		//g.drawImage(Assets.projectile, posX, posY, width, height, null);
		
	}

}
