package networking.project.game.states;

import java.awt.Graphics;

import networking.project.game.Handler;

/**
 *  A State is any given phase of the game. It determines what will
 *  tick and render during that phase. The State class also acts as
 *  a state manager.
 * 	
 *	@author 
 *	@version 1.0
 *	@since version 1.0
 */
public abstract class State {

	private static State currentState = null;
	
	/**
	 * @param state the current state of the game.
	 */
	public static void setState(State state){
		currentState = state;
	}
	
	/**
	 * @return currentState the current state of the game.
	 */
	public static State getState(){
		return currentState;
	}
	
	//CLASS
	
	protected Handler handler;
	
	public State(Handler handler){
		this.handler = handler;
	}
	
	public abstract void tick();
	
	public abstract void render(Graphics g);
	
	public abstract void displayState();
	
}
