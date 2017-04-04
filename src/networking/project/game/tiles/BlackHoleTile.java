package networking.project.game.tiles;

import networking.project.game.gfx.Assets;

/**
 *	 A BlackHoleTile sets the player at the beginning of the level.
 * 	
 *	@author 
 *	@version 1.0
 *	@since version 1.0
 */
public class BlackHoleTile extends Tile{

	public BlackHoleTile(int id) {
		super(Assets.blackHole, id);
	}
}