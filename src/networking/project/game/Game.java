package networking.project.game;

import networking.project.game.display.Display;
import networking.project.game.entities.creatures.Player;
import networking.project.game.entities.events.PlayerDisconnectEvent;
import networking.project.game.gfx.Assets;
import networking.project.game.gfx.GameCamera;
import networking.project.game.input.KeyManager;
import networking.project.game.input.MouseManager;
import networking.project.game.states.GameState;
import networking.project.game.states.State;
import networking.project.game.tiles.Tile;
import networking.project.game.utils.Utils;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;

/**
 *	The Game class runs the game: 
 *  It initializes the display and renders the game. 
 *  It also contains the main game loop.
 * 	
 *	@author 
 *	@version 1.0
 *	@since version 1.0
 */
public class Game extends WindowAdapter implements Runnable {

	private Display display;
	private int width, height;
	public String title;
	public static boolean MUTED = false;
	
	private boolean running = false, isServer = false;
	private Thread thread;

    //States
	private State gameState;
	private State menuState;
	private State GameOverState;
	
	//Input
	private KeyManager keyManager;
	private MouseManager mouseManager;
	
	//Camera
	private GameCamera gameCamera;
	
	//Handler
	private Handler handler;
	
	public Game(String title, int width, int height){
		this.width = width;
		this.height = height;
		this.title = title;
		handler = new Handler(this);
		keyManager = new KeyManager(this);
		mouseManager = new MouseManager();
	}

    @Override
    public void windowClosing(WindowEvent e)
    {
        if (!isServer)
        {
            Player p = handler.getClientPlayer();
            if (p != null)
                p.fireEvent(new PlayerDisconnectEvent(p));
        }
    }

    /**
	 *  Initializes everything.
	 */
	public void init(boolean server){
	    isServer = server;
		if (!server) {
			display = new Display(title, width, height, this);
			display.getFrame().addKeyListener(keyManager);
			display.getFrame().addMouseListener(mouseManager);
			display.getFrame().addMouseMotionListener(mouseManager);
			display.getCanvas().addMouseListener(mouseManager);
			display.getCanvas().addMouseMotionListener(mouseManager);
		}
		Assets.init();
		//Sound.background.play();//New jon edit
		
		gameState = new GameState(handler);
		gameCamera = new GameCamera(handler, 0, handler.getWorld().getHeight() * Tile.TILEHEIGHT);
	}
	
	/**
	 *  Updates the game every tick.
	 */
	public void tick(){
		keyManager.tick();
		
		if(State.getState() != null)
			State.getState().tick();
		this.getGameCamera().centerOnEntity(getHandler().getClientPlayer());
		
	}
	
	/**
	 *  Renders all the graphics in the game to the screen.
	 */
	public void render(){
        BufferStrategy bs = display.getCanvas().getBufferStrategy();
		if(bs == null){
			display.getCanvas().createBufferStrategy(3);
			return;
		}
		try {
            Graphics g = bs.getDrawGraphics();
            //Clear Screen
            g.clearRect(0, 0, width, height);
            //Draw Here!

            if(State.getState() != null)
                State.getState().render(g);

            //End Drawing!
            bs.show();
            g.dispose();
        }
		catch (Exception e)
        {
            e.printStackTrace();
        }
	}
	
	/**
	 *  The main game loop which ticks and renders every loop 
	 *  as long as the game is running.
	 */
	public void run(){
		
		//init();
		
		
		int fps = 60;  // How many times every second we want to run tick() and render()
		double timePerTick = 1000000000 / fps;
		double delta = 0;
		long now;
		long lastTime = System.nanoTime();
		long timer = 0;
		int ticks = 0;
		
		while(running){
			now = System.nanoTime();
			delta += (now - lastTime) / timePerTick;
			timer += now - lastTime;
			lastTime = now;
			
			if(delta >= 1){
					tick();
					render();
				
				ticks++;
				delta--;
			}
			
			
			
			// Temporary FPS counter
			if(timer >= 1000000000){
                Utils.debug("Ticks and Frames: " + ticks);
				ticks = 0;
				timer = 0;
			}
		}
		
		stop();
		
	}
	
	/**
	 * @return keyManager
	 */
	public KeyManager getKeyManager(){
		return keyManager;
	}
	
	/**
	 * @return mouseManager
	 */
	public MouseManager getMouseManager(){
		return mouseManager;
	}
	
	/**
	 * @return gameCamera
	 */
	public GameCamera getGameCamera(){
		return gameCamera;
	}
	
	/**
	 * @return width of the game
	 */
	public int getWidth(){
		return width;
	}
	
	/**
	 * @return height of the game
	 */
	public int getHeight(){
		return height;
	}
	
	/**
	 *  Starts the thread which runs the game,
	 *  if it is not already running
	 */
	public synchronized void start(){
		if(running)
			return;
		running = true;
		thread = new Thread(this);
		thread.start();
	}
	
	/**
	 *  Joins the game thread, stopping the game,
	 *  if it is already running.
	 */
	public synchronized void stop(){
		if(!running)
			return;
		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public Display getDisplay(){
		return display;
	}
	
	public GameState getGameState(){
		return (GameState) gameState;
	}
	
	public Handler getHandler(){
		return this.handler;
	}
}