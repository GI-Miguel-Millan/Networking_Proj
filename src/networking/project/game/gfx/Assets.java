package networking.project.game.gfx;

import networking.project.game.sound.Sound;
import resources.ResourceLoader;

import java.awt.image.BufferedImage;

/**
 *	Assets loads in and stores all the assets for the game.
 * 	
 *	@author 
 *	@version 1.0
 *	@since version 1.0
 */
public class Assets {
	
	//Dimensions of tiles in the SpriteSheet
	private static final int width = 64, height = 64;
	
	// Images that will be loaded into the game.
	public static BufferedImage space, space2, stone, tree, spaceBound, stealth, 
								projectile, interceptor, assault, stealthed, giantHead,
								motherShip,darkTumorRang, dt_left, dt_right, fireBall, eagle, darkLaser,
								boost, shield, heart, star, splitshot, menu, menu2, blackHole, slowVortex, paused,
								boosted, invincible, gameOver;

	public static BufferedImage[] player_down, player_up, player_left, player_right;
	public static BufferedImage[] hurt_down, hurt_up, hurt_left, hurt_right;
	public static BufferedImage[] explosion;
	public static BufferedImage[] btn_start;
	public static BufferedImage[] btn_restart;
	public static BufferedImage[] transIn, transOut;
//	public static Font gameFont;
	//Array of names stored in the Paths.txt of the files we want to load
	public static String[] fileNames; 	// [0] = "sheets.png" 
								      	// [1] = "world1.txt"
									  	// [2] = "world2.txt"
									  	// [3] = "world3.txt"
										// [4] = "world4.txt"
										// [5] = "world5.txt"
										// [6] = "background.au"
	  									// [7] = "explosion.au"
									  	// [8] = "hawkscreech.au"
										// [9] = "lazer-sound.au"
										// [10] = "victory.au"
										// [11] = "world0.txt"
										// [12] = "menu_graphic.png"
										// [13] = "PAUSED.png"
										// [14] = "Transition.png"
										// [15] = "Venus.wav"
										// [16] = "bgm_action_4.wav"
										// [17] = "bgm_action_3.wav"
										// [18] = "bgm_action_5.wav"
										// [19] = "BossMain.wav"
										// [20] = "fight_looped.wav"
										// [21] = "introjingle.wav"
										// [22] = "Just_Move.wav"
										// [23] = "Mars.wav"
										// [24] = "Mercury.wav"
										// [25] = "victorious.wav"
										// [26] = "failure.wav"
										// [27] = "MenuState.png"
										// [28] = "MenuState2.png"
										// [29] = "engage.png"
										// [30] = "engage2.png"
										// [31] = "GameOver.png"
										// [32] = "GameOver2.png"
										// [33] = "GameOverScreen.png"
	/**
	 *  Loads all assets into the game. This should only be called once.
	 */
	public static void init(){
		initFilenames();
		
//		for(String i: fileNames)
//			System.out.println(i);
		//System.out.println("/resources/sounds/" + ResourceLoader.loadSounds(fileNames[6]));
		
		SpriteSheet sheet = new SpriteSheet(ResourceLoader.loadImage(fileNames[0]));
		SpriteSheet sheet2 = new SpriteSheet(ResourceLoader.loadImage(fileNames[14]));
		menu = ResourceLoader.loadImage(fileNames[27]);
		menu2 = ResourceLoader.loadImage(fileNames[28]);
		paused = ResourceLoader.loadImage(fileNames[13]);
		gameOver = ResourceLoader.loadImage(fileNames[33]);
		
//		ResourceLoader.loadFont(fileNames[34]);
//		gameFont = new Font("ARCADECLASSIC.TTF", Font.PLAIN, 72);
		
		btn_start = new BufferedImage[2];
		btn_start[1] = ResourceLoader.loadImage(fileNames[29]);
		btn_start[0] = ResourceLoader.loadImage(fileNames[30]);
		btn_restart = new BufferedImage[2];
		btn_restart[1] = ResourceLoader.loadImage(fileNames[31]);
		btn_restart[0] = ResourceLoader.loadImage(fileNames[32]);
		
		player_down = new BufferedImage[2];
		player_up = new BufferedImage[2];
		player_left = new BufferedImage[2];
		player_right = new BufferedImage[2];
		hurt_down = new BufferedImage[2];
		hurt_up = new BufferedImage[2];
		hurt_left = new BufferedImage[2];
		hurt_right = new BufferedImage[2];
		
		player_down[0] = sheet.crop(width * 4, 0, width, height);
		player_down[1] = sheet.crop(width * 5, 0, width, height);
		player_up[0] = sheet.crop(width * 6, 0, width, height);
		player_up[1] = sheet.crop(width * 7, 0, width, height);
		player_right[0] = sheet.crop(width * 4, height, width, height);
		player_right[1] = sheet.crop(width * 5, height, width, height);
		player_left[0] = sheet.crop(width * 6, height, width, height);
		player_left[1] = sheet.crop(width * 7, height, width, height);
		
		hurt_down[0] = sheet.crop(width * 4, 0, width, height);
		hurt_down[1] = sheet.crop(0, height * 6, width, height);
		hurt_up[0] = sheet.crop(width * 6, 0, width, height);
		hurt_up[1] = sheet.crop(0, height * 6, width, height);
		hurt_right[0] = sheet.crop(width * 4, height, width, height);
		hurt_right[1] = sheet.crop(0, height * 6, width, height);
		hurt_left[0] = sheet.crop(width * 6, height, width, height);
		hurt_left[1] = sheet.crop(0, height * 6, width, height);
		
		explosion = new BufferedImage[8];
		
		explosion[0] = sheet.crop(width * 4, height * 2, width, height);
		explosion[1] = sheet.crop(width * 5, height * 2, width, height);
		explosion[2] = sheet.crop(width * 6, height * 2, width, height);
		explosion[3] = sheet.crop(width * 7, height * 2, width, height);
		explosion[4] = sheet.crop(width * 4, height * 3, width, height);
		explosion[5] = sheet.crop(width * 5, height * 3, width, height);
		explosion[6] = sheet.crop(width * 6, height * 3, width, height);
		explosion[7] = sheet.crop(width * 7, height * 3, width, height);
		
		transIn = new BufferedImage[10];
		
		transIn[9] = sheet2.crop(0, 0, width, height);
		transIn[8] = sheet2.crop(width, 0, width, height);
		transIn[7] = sheet2.crop(width * 2, 0, width, height);
		transIn[6] = sheet2.crop(width * 3, 0, width, height);
		transIn[5] = sheet2.crop(width * 4, 0, width, height);
		transIn[4] = sheet2.crop(width * 5, 0, width, height);
		transIn[3] = sheet2.crop(width * 6, 0, width, height);
		transIn[2] = sheet2.crop(width * 7, 0, width, height);
		transIn[1] = sheet2.crop(width * 8, 0, width, height);
		transIn[0] = sheet2.crop(width * 9, 0, width, height);
		
		transOut = new BufferedImage[10];
		
		transOut[0] = sheet2.crop(0, 0, width, height);
		transOut[1] = sheet2.crop(width, 0, width, height);
		transOut[2] = sheet2.crop(width * 2, 0, width, height);
		transOut[3] = sheet2.crop(width * 3, 0, width, height);
		transOut[4] = sheet2.crop(width * 4, 0, width, height);
		transOut[5] = sheet2.crop(width * 5, 0, width, height);
		transOut[6] = sheet2.crop(width * 6, 0, width, height);
		transOut[7] = sheet2.crop(width * 7, 0, width, height);
		transOut[8] = sheet2.crop(width * 8, 0, width, height);
		transOut[9] = sheet2.crop(width * 9, 0, width, height);
		
		space = sheet.crop(width, 0, width, height);
		space2 = sheet.crop(width * 2, 0, width, height);
		stone = sheet.crop(width * 3, 0, width, height);
		tree = sheet.crop(0, 0, width, height * 2);
		spaceBound = sheet.crop(0, height * 2, width, height);
		stealth = sheet.crop(width, height, width, height);
		projectile = sheet.crop(width, height * 2, width, height);
		interceptor = sheet.crop(width * 2, height, width, height * 2);
		assault = sheet.crop(width * 3, height, width, height);
		stealthed = sheet.crop(width * 3, height * 2, width, height);
		giantHead = sheet.crop(0, height*3, width, height);
		motherShip = sheet.crop(0, height * 4, width * 2, height * 2);
		darkTumorRang = sheet.crop(width * 2, height * 3, width, height);
		dt_left = sheet.crop(0, 0, width, height);
		dt_right = sheet.crop(0, height, width, height);
		fireBall = sheet.crop(width * 3, height * 3, width, height);
		darkLaser = sheet.crop(width, height * 3, width, height);
		eagle = sheet.crop(width * 4, height * 4, width* 2, height* 2);
		boost = sheet.crop(0, height * 7, width, height);
		shield = sheet.crop(width, height * 7, width, height);
		heart = sheet.crop(width * 2, height * 7, width, height);
		star = sheet.crop(width * 3, height * 7, width, height);
		splitshot = sheet.crop(width * 4, height * 7, width, height);
		blackHole = sheet.crop(width * 2, height * 4, width, height);
		slowVortex = sheet.crop(width * 3, height * 4, width, height);
		boosted = sheet.crop(width * 5, height * 7, width, height);
		invincible = sheet.crop(width * 6, height *7, width, height);
		//test comment
		// Init all the sounds
		Sound.init();
	}

	public static void initFilenames()
	{
		String regex = "\\s+";
		String pathsFile = ResourceLoader.loadFileAsString("Paths.txt");
		//Splits up each file name into the fileNames array
		fileNames = pathsFile.split(regex);
	}
}
