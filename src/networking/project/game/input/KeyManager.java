package networking.project.game.input;

import networking.project.game.Game;
import networking.project.game.entities.creatures.Player;
import networking.project.game.utils.InputFlags;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;

/**
 *	The KeyManager keeps track of key presses.
 * 	
 *	@author 
 *	@version 1.0
 *	@since version 1.0
 */
public class KeyManager extends KeyAdapter implements InputFlags {

    private byte input;

    // Stores the inputs as a map to the VK_* codes from KeyEvent class
	private HashMap<Integer, Byte> inputMap;

    private Game game; // Current game object

	public KeyManager(Game g){
        game = g;
        inputMap = new HashMap<>();
		inputMap.put(KeyEvent.VK_W, IN_UP);
        inputMap.put(KeyEvent.VK_D, IN_RIGHT);
        inputMap.put(KeyEvent.VK_S, IN_DOWN);
        inputMap.put(KeyEvent.VK_A, IN_LEFT);
        inputMap.put(KeyEvent.VK_SPACE, IN_ATTK);
        inputMap.put(KeyEvent.VK_ESCAPE, IN_ESC);
	}
	
	/**
	 *  Sets the movement keys to true or false based on
	 *  whether or not its key is pressed.
	 */
	public void tick() {
        Player p = game.getHandler().getClientPlayer();
        if (p != null)
        {
            p.setInput(input);
        }
	}

	/**
	 * Sets the key with the given key code to true if 
	 * it has been pressed.
	 * 
	 * @param e
	 * @Override
	 */
	public void keyPressed(KeyEvent e) {
        if (inputMap.containsKey(e.getKeyCode()))
            input |= inputMap.get(e.getKeyCode());
	}

	/**
	 * Sets the key with the given key code to false if 
	 * it has been released.
	 * 
	 * @param e
	 * @Override
	 */
	public void keyReleased(KeyEvent e) {
        if (inputMap.containsKey(e.getKeyCode()))
            input &= ~inputMap.get(e.getKeyCode());
	}
}