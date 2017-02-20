package networking.project.game.tiles;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 *	A Tile is the basic building block of each World. 
 * 	
 *	@author 
 *	@version 1.0
 *	@since version 1.0
 */
public class Tile {
	
	//STATIC STUFF HERE
	
	public static Tile[] tiles = new Tile[256];
	public static Tile grassTile = new Space2File(0);
	public static Tile dirtTile = new SpaceTile(1);
	public static Tile rockTile = new SpaceBoundsTile(2);
	public static Tile playerSpawnTile = new PSpawnTile(3);
	public static Tile enemySpawnTile = new ESpawnTile(4);
	public static Tile goalTile = new GoalTile(5);
	public static Tile bossSpawnTile = new BossSpawnTile(6);
	public static Tile blackHoleTile = new BlackHoleTile(7);
	public static Tile slowVortexTile = new SlowVortexTile(8);
	public static Tile bossFightStartTile = new BossFightStartTile(9);
	
	
	//CLASS
	
	public static final int TILEWIDTH = 64, TILEHEIGHT = 64;
	
	protected BufferedImage texture;
	protected final int id;
	
	public Tile(BufferedImage texture, int id){
		this.texture = texture;
		this.id = id;
		
		tiles[id] = this;
	}
	
	public void tick(){
		
	}
	
	/**
	 * Renders the Tile at the given position (x,y)
	 */
	public void render(Graphics g, int x, int y){
		g.drawImage(texture, x, y, TILEWIDTH, TILEHEIGHT, null);
	}
	
	/**
	 * Override this method and return true for a solid tile.
	 * @return false means it is not solid
	 */
	public boolean isSolid(){
		return false;
	}
	
	/**
	 * Override this method and return true if the tile is 
	 * a bossFightStartTile.
	 * @return false means it is not a bossFightStartTile.
	 */
	public boolean isBFight(){
		return false;
	}
	
	/**
	 * Checks if this tile is a player spawn Tile
	 * @return true if it IS a player spawn Tile
	 * @return false if it IS NOT a player spawn Tile
	 */
	public boolean isPSpawn(){
		if(this.getId() == playerSpawnTile.getId())
			return true;
		else
			return false;
	}
	
	/**
	 * Checks if this Tile is an Enemy spawn Tile
	 * @return true if it IS an Enemy spawn Tile
	 * @return false if it IS Not an Enemy Spawn Tile
	 */
	public boolean isESpawn(){
		if(this.getId() == enemySpawnTile.getId())
			return true;
		else
			return false;
	}
	
	/**
	 * Checks if this Tile is a Goal Tile
	 * @return true if it Is
	 * @return false if not
	 */
	public boolean isGoal(){
		
		if(this.getId() == goalTile.getId())
			return true;
		else 
			return false;
	}
	
	/**
	 * Checks if this Tile is a blackHoleTile
	 * @return true if it Is
	 * @return false if not
	 */
	public boolean isBlackHole(){
		
		if(this.getId() == blackHoleTile.getId())
			return true;
		else 
			return false;
	}
	
	/**
	 * Checks if this Tile is a SlowVortexTile
	 * @return true if it Is
	 * @return false if not
	 */
	public boolean isSlowVortex(){
		
		if(this.getId() == slowVortexTile.getId())
			return true;
		else 
			return false;
	}
	
	/**
	 * @return id the unique id of a given Tile.
	 */
	public int getId(){
		return id;
	}

	/**
	 * Checks if this Tile is a boss spawn Tile
	 * @return true if it Is
	 * @return false if not
	 */
	public boolean isBossSpawn() {
		if(this.getId() == bossSpawnTile.getId())
			return true;
		else
			return false;
	}
	
	public void showGoal(){
	}
	
	public void hideGoal(){
	}
	
}
