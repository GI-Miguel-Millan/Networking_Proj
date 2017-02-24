package networking.project.game.states;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import networking.project.game.Handler;
import networking.project.game.entities.creatures.Player;
import networking.project.game.gfx.Animation;
import networking.project.game.gfx.Assets;
import networking.project.game.sound.Sound;
import networking.project.game.worlds.World;

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
		
		String tmpScore = "SCORE: " + handler.getPlayerScore();
		Font stringFont = new Font("Sans Serif", Font.PLAIN, 18);
		
		g.setColor(Color.getHSBColor(.747222f, .91f, .13f));
		g.fillRect(0, 0, handler.getWidth(), 50);
		
		g.setColor(Color.red);
		g.setFont(stringFont);
		g.drawString(tmpScore , 260, 20);	//Score placeholder
		g.drawString("Health: ", 35, 45);	
		g.fillRect(100, 35, (350/50) * 50, 10); //Healthbar background
		g.setColor(Color.green);
		g.fillRect(100, 35, (350/50) * handler.getPlayerHealth(), 10);
		
		g.setColor(Color.black);
		for(int i = 100 + (350/50); i < 100 + 350; i+=(350/50))
			g.fillRect(i,35, 1, 10);
		
		
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
