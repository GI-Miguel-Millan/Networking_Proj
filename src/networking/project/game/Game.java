package networking.project.game;

import java.awt.Graphics;
import java.awt.image.BufferStrategy;

import networking.project.game.display.Display;
import networking.project.game.gfx.Assets;
import networking.project.game.gfx.GameCamera;
import networking.project.game.input.KeyManager;
import networking.project.game.input.MouseManager;
import networking.project.game.sound.Sound;
import networking.project.game.states.*;
import networking.project.game.tiles.Tile;

/**
 *	The Game class runs the game: 
 *  It initializes the display and renders the game. 
 *  It also contains the main game loop.
 * 	
 *	@author 
 *	@version 1.0
 *	@since version 1.0
 */
public class Game implements Runnable {

	private Display display;
	private int width, height;
	public String title;
	public static boolean MUTED = false;
	
	private boolean running = false;
	private Thread thread;
	
	private BufferStrategy bs;
	private Graphics g;
	
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
		keyManager = new KeyManager();
		mouseManager = new MouseManager();
	}
	
	/**
	 *  Initializes everything.
	 */
	public void init(){
		display = new Display(title, width, height);
		display.getFrame().addKeyListener(keyManager);
		display.getFrame().addMouseListener(mouseManager);
		display.getFrame().addMouseMotionListener(mouseManager);
		display.getCanvas().addMouseListener(mouseManager);
		display.getCanvas().addMouseMotionListener(mouseManager);
		Assets.init();
		//Sound.background.play();//New jon edit
		
		gameState = new GameState(handler);
		GameOverState = new GameOverState(handler);
		menuState = new MenuState(handler);
		
		
		gameCamera = new GameCamera(handler, 0, handler.getWorld().getHeight() * Tile.TILEHEIGHT);
		
		
		
	}
	
	/**
	 *  Updates the game every tick.
	 */
	public void tick(){
		//keyManager.tick();
		
		if(State.getState() != null)
			State.getState().tick();
		//this.getGameCamera().centerOnEntity(getHandler().getClientPlayer());
		
	}
	
	/**
	 *  Renders all the graphics in the game to the screen.
	 */
	public void render(){
		bs = display.getCanvas().getBufferStrategy();
		if(bs == null){
			display.getCanvas().createBufferStrategy(3);
			return;
		}
		g = bs.getDrawGraphics();
		//Clear Screen
		g.clearRect(0, 0, width, height);
		//Draw Here!
		
		if(State.getState() != null)
			State.getState().render(g);
		
		//End Drawing!
		bs.show();
		g.dispose();
	}
	
	/**
	 *  The main game loop which ticks and renders every loop 
	 *  as long as the game is running.
	 */
	public void run(){
		
		init();
		
		
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
				System.out.println("Ticks and Frames: " + ticks);
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
	
	public MenuState getMenuState(){
		return (MenuState) menuState;
	}
	
	public GameOverState getGameOverState(){
		return (GameOverState) GameOverState;
	}
	
	public GameState getGameState(){
		return (GameState) gameState;
	}
	
	public Handler getHandler(){
		return this.handler;
	}
	
	
	public String getPlayerInput(){
		int up = 0, down = 0, left = 0, right = 0, attack = 0;
		if(keyManager.up){
			up = 1;
		}
		if(keyManager.down){
			down = 1;
		}
		if(keyManager.left){
			left = 1;
		}
		if(keyManager.right){
			right = 1;
		}
		if(keyManager.fire){
			attack = 1;
		}
			
		
		return "input " + up + " " + down + " " + left + " " + right + " " + attack + " " + handler.getClientPlayer().getID();
	}
}











