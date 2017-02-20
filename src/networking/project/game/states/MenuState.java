package networking.project.game.states;

import java.awt.Graphics;

import networking.project.game.Handler;
import networking.project.game.entities.creatures.Player;
import networking.project.game.gfx.Assets;
import networking.project.game.sound.Sound;
import networking.project.game.ui.ClickListener;
import networking.project.game.ui.UIImageButton;
import networking.project.game.ui.UIManager;
import networking.project.game.worlds.World;

/**
 *	The MenuState is the pre-game state, giving the option to start the game.
 * 	
 *	@author 
 *	@version 1.0
 *	@since version 1.0
 */
public class MenuState extends State {

	private UIManager uiManager;

	public MenuState(Handler handler) {
		super(handler);
		uiManager = new UIManager(handler);
		handler.getMouseManager().setUIManager(uiManager);

		// adds a button that switches the current state to the gamestate when pressed.
		uiManager.addObject(new UIImageButton(136, 623, 229, 64, Assets.btn_start, new ClickListener() {
			@Override
			public void onClick() {
				handler.getMouseManager().setUIManager(null);
				handler.getGame().getGameState().displayState();
			}
		}));
	}

	@Override
	public void tick() {
		uiManager.tick();
	}

	@Override
	public void render(Graphics g) {
		g.drawImage(Assets.menu, 0, 0, handler.getWidth(), handler.getHeight(), null);
		uiManager.render(g);
		
	}
	
	/**
	 * Sets the state to the menu state and sets
	 * the ui manager from null to uiManager
	 * @Override
	 */
	public void displayState() {
		Sound.stopAll();
		Sound.resetAll();
		Sound.Just_Move.play();
		handler.setVictorious(false);
		State.setState(handler.getGame().getMenuState());
		handler.getMouseManager().setUIManager(uiManager);
	}

}
