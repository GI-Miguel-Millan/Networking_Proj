package networking.project.game.states;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import networking.project.game.Handler;
import networking.project.game.display.Display;
import networking.project.game.gfx.Assets;
import networking.project.game.input.MouseManager;
import networking.project.game.sound.Sound;
import networking.project.game.ui.ClickListener;
import networking.project.game.ui.UIImageButton;
import networking.project.game.ui.UILabel;
import networking.project.game.ui.UIManager;

public class GameOverState extends State
{
	private UIManager uiManager;
	private UILabel lblScore;
	private UILabel lblHighScore;
	
	UILabel lblGameOver = new UILabel(150, 150, 1, 1, "GAME OVER", null);
	UIImageButton btnRestart = new UIImageButton(92, 500, 331, 71, Assets.btn_restart, new ClickListener()
			{
				public void onClick()
				{
					handler.getMouseManager().setUIManager(null);
					handler.getGame().getMenuState().displayState();
				}
			});
	
	public GameOverState(Handler handler)
	{
		super(handler);

		uiManager = new UIManager(handler);
		handler.getMouseManager().setUIManager(uiManager);
	}

	@Override
	public void tick() {
		uiManager.tick();
		
	}

	@Override
	public void render(Graphics g) {
		g.drawImage(Assets.gameOver, 0, 0, handler.getWidth(), handler.getHeight(), null);
		String tmpScore = "Your Score: " + handler.getPlayerScore();
		String gameOver = "GAME OVER";
		String victory = "VICTORY";
		Font stringFont = new Font("Arial", Font.PLAIN, 36);
		Font gameOverFont = new Font("Castellar", Font.PLAIN, 72);
		
		
		g.setFont(stringFont);
		g.setColor(Color.ORANGE);
		g.drawString(tmpScore , 100, 350);
		
		
		g.setFont(gameOverFont);
			
		uiManager.render(g);
	}
	
	/**
	 * Sets the state to the game over state and sets
	 * the ui manager from null to uiManager
	 * @Override
	 */
	public void displayState(){
		State.setState(handler.getGame().getGameOverState());
		Sound.stopAll();
		
		handler.getMouseManager().setUIManager(uiManager);
//		uiManager.removeObject(lblHighScore);
//		uiManager.removeObject(lblScore);
//		lblScore = new UILabel(150, 200, 10, 20, "SCORE: " + handler.getPlayerScore(), null);
//		lblHighScore = new UILabel(150, 250, 10, 20, "HIGH SCORE: " + handler.getHighScore(), null);
//		uiManager.addObject(lblGameOver);
//		uiManager.addObject(lblScore);
//		uiManager.addObject(lblHighScore);
		uiManager.addObject(btnRestart);
	}
}
