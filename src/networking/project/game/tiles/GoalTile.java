package networking.project.game.tiles;

import java.awt.Graphics;

import networking.project.game.gfx.Assets;

/**
 *	 A player must reach the GoalTile to end the level.
 * 	
 *	@author 
 *	@version 1.0
 *	@since version 1.0
 */
public class GoalTile extends Tile {

	private boolean solid = true;
	public GoalTile(int id) {
		super(Assets.stone, id);
		// TODO Auto-generated constructor stub
	}
	
	public void render(Graphics g, int x, int y){
		g.drawImage(texture, x, y, TILEWIDTH, TILEHEIGHT, null);
	}
	
	public void showGoal(){
		texture = Assets.spaceBound;
		solid = false;
	}
	
	public void hideGoal(){
		texture = Assets.stone;
		solid = true;
	}
	
	public boolean isSolid(){
		return solid;
	}

}
