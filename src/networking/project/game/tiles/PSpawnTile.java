package networking.project.game.tiles;

import networking.project.game.gfx.Assets;

/**
 *	The PSpawnTile determines where in the World a player is spawned. 
 * 	
 *	@author 
 *	@version 1.0
 *	@since version 1.0
 */
public class PSpawnTile extends Tile{

	public PSpawnTile(int id) {
		super(Assets.spaceBound, id);
	}
}