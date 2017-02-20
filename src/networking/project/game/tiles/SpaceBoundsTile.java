package networking.project.game.tiles;

import networking.project.game.gfx.Assets;

public class SpaceBoundsTile extends Tile {

	public SpaceBoundsTile(int id) {
		super(Assets.stone, id);
	}
	
	@Override
	public boolean isSolid(){
		return true;
	}

}
