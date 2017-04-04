package networking.project.game.states;

import networking.project.game.Handler;
import networking.project.game.gfx.Assets;
import networking.project.game.sound.Sound;
import networking.project.game.worlds.World;

import java.awt.*;

/**
 *	All gameplay is done in the chosen world, during the GameState.
 * 	
 *	@author 
 *	@version 1.0
 *	@since version 1.0
 */
public class GameState extends State {
	
	private World world;
	
	public GameState(Handler handler){
		super(handler);
		displayState();
	}
	
	@Override
	public void tick() {
		
		if (world != handler.getWorld())
			world = handler.getWorld();
		
			world.tick();

	}

	@Override
	public void render(Graphics g) {
		world.render(g);
	}


	/**
	 * Sets the state to the game state 
	 * and starts the game over at level 1
	 */
	public void displayState(){
		Sound.stopAll();
		State.setState(handler.getGame().getGameState());
		world = new World(handler, Assets.fileNames[1]); // fileNames[1] = world1.txt
		handler.setWorld(world);
	}
	
}
