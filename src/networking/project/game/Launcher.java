package networking.project.game;

/**
 *	Launches the game.
 *
 *	@author 
 *	@version 1.0
 *	@since version 1.0
 */
public class Launcher {

	public static void main(String[] args){
		Game game = new Game("Shoot'em up Good!", 500, 750);
		game.start();
	}
	
}
