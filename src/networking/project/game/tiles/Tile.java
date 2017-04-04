package networking.project.game.tiles;

import java.awt.*;
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
	public static boolean initted = false;
	public static Tile[] tiles = new Tile[256];
	public static Tile grassTile;
	public static Tile dirtTile;
	public static Tile rockTile;
	public static Tile playerSpawnTile;
	public static Tile enemySpawnTile;
	public static Tile goalTile;
	public static Tile bossSpawnTile;
	public static Tile blackHoleTile;
	public static Tile slowVortexTile;
	public static Tile bossFightStartTile;

	public static void init()
    {
        if (initted)
            return;
        grassTile = new Space2File(0);
        dirtTile = new SpaceTile(1);
        rockTile = new SpaceBoundsTile(2);
        playerSpawnTile = new PSpawnTile(3);
        enemySpawnTile = new ESpawnTile(4);
        goalTile = new GoalTile(5);
        bossSpawnTile = new BossSpawnTile(6);
        blackHoleTile = new BlackHoleTile(7);
        slowVortexTile = new SlowVortexTile(8);
        bossFightStartTile = new BossFightStartTile(9);
        initted = true;
    }
	
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
		return this.getId() == playerSpawnTile.getId();
	}
	
	/**
	 * Checks if this Tile is an Enemy spawn Tile
	 * @return true if it IS an Enemy spawn Tile
	 * @return false if it IS Not an Enemy Spawn Tile
	 */
	public boolean isESpawn(){
		return this.getId() == enemySpawnTile.getId();
	}
	
	/**
	 * Checks if this Tile is a Goal Tile
	 * @return true if it Is
	 * @return false if not
	 */
	public boolean isGoal(){

		return this.getId() == goalTile.getId();
	}
	
	/**
	 * Checks if this Tile is a blackHoleTile
	 * @return true if it Is
	 * @return false if not
	 */
	public boolean isBlackHole(){

		return this.getId() == blackHoleTile.getId();
	}
	
	/**
	 * Checks if this Tile is a SlowVortexTile
	 * @return true if it Is
	 * @return false if not
	 */
	public boolean isSlowVortex(){

		return this.getId() == slowVortexTile.getId();
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
		return this.getId() == bossSpawnTile.getId();
	}
	
	public void showGoal(){
	}
	
	public void hideGoal(){
	}
	
}
