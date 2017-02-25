package networking.project.game.entities.statics;

import networking.project.game.Handler;
import networking.project.game.entities.Entity;

/**
 *	A StaticEntity is simply an Entity which does not move.
 * 	
 *	@author Miguel Millan
 *	@version 1.0
 *	@since version 1.0
 */
public abstract class StaticEntity extends Entity {
	
	public StaticEntity(Handler handler, float x, float y, int width, int height){
		super(handler, x, y, width, height, 0);
	}

}
