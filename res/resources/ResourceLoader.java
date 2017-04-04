package resources;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;


public class ResourceLoader {
	static ResourceLoader rl = new ResourceLoader();
	
	/**
	 * Loads the specified image from the textures folder relative to the ResourceLoader.
	 * 
	 * @param fileName the name of the image to be loaded
	 * @return a buffered image of the specified image
	 * @return null if the image could not be found at the path.
	 */
	public static BufferedImage loadImage(String fileName){
		try {
			//returns the buffered image located at the specified path
			return ImageIO.read(rl.getClass().getResource("textures/" + fileName));
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		return null;
	}
	
//	public static void loadFont(String fileName){
//		
//		
//		try {
//		     GraphicsEnvironment ge = 
//		         GraphicsEnvironment.getLocalGraphicsEnvironment();
//		     ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File(rl.getClass().getResource("files/" + fileName).toString())));
//		     
//		     System.out.println(rl.getClass().getResource("files/" + fileName).toString());
//		} catch (Exception e) {
//		     //Handle exception
//		}
//	}
	
	/**
	 * Loads the specified world file in as a string. This file needs to be a text file.
	 * 
	 * @param fileName
	 * @return String of 
	 */
	public static String loadWorldFile(String fileName){
		StringBuilder builder = new StringBuilder();
		
		try{
			BufferedReader br = new BufferedReader(new 
					InputStreamReader(rl.getClass().getResource("worlds/" + fileName).openStream()));
			String line;
			while((line = br.readLine()) != null)
			{
				builder.append(line);
				builder.append("\n");
			}

			
			br.close();
		}catch(IOException e){
			e.printStackTrace();
		}
		
		return builder.toString();
	}
	
	/**
	 * Loads a file from the files folder into the game as a string. This is primarily used for loading
	 * pre-made worlds into the game.
	 * 
	 * @param fileName the location of the target file to be loaded
	 * @return a string containing the information of the file.
	 */
	public static String loadFileAsString(String fileName){
		StringBuilder builder = new StringBuilder();
		
		try (InputStreamReader isr = new InputStreamReader(rl.getClass().getResource("files/" + fileName).openStream());
			BufferedReader br = new BufferedReader(isr))
		{
			String line;
			while((line = br.readLine()) != null)
			{
				builder.append(line);
				builder.append("\n");
			}

		} catch(IOException e){
			e.printStackTrace();
		}
		
		return builder.toString();
	}
	
	public static URL loadSounds(String fileName){
		
		return rl.getClass().getResource("/resources/sounds/" + fileName);
	}
}
