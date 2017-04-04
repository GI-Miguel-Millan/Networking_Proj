package networking.project.game.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 *	Utils contains static functions which can 
 *	be useful for other classes.
 * 	
 *	@author 
 *	@version 1.0
 *	@since version 1.0
 */
public class Utils {

	private static boolean debugging = false;
	
	/**
	 * Loads a file into the game as a string. This is primarily used for loading
	 * pre-made worlds into the game.
	 * 
	 * @param path the location of the target file to be loaded
	 * @return a string containing the information of the file.
	 */
	public static String loadFileAsString(String path){
		StringBuilder builder = new StringBuilder();
		
		try{
			BufferedReader br = new BufferedReader(new FileReader(path));
			String line;
			while((line = br.readLine()) != null)
				builder.append(line + "\n");
			
			br.close();
		}catch(IOException e){
			e.printStackTrace();
		}
		
		return builder.toString();
	}
	
	/**
	 * Parses a string of numbers for integers.
	 * 
	 * @param number the string of numbers
	 * @return the parsed integer if it exists
	 * @return 0 otherwise
	 */
	public static int parseInt(String number){
		try{
			return Integer.parseInt(number);
		}catch(NumberFormatException e){
			e.printStackTrace();
			return 0;
		}
	}
	
	/**
	 * Random number in an interval generator
	 * @param min
	 * @param max
	 * @return random number in the interval of min to max
	 */
	public static int randomNum(int min, int max){
		return (int)(Math.random() * (max - min + 1) + min) ;
	}
	
	public static void debug(String message){
		if(debugging)
			System.out.println(message);
	}

}
