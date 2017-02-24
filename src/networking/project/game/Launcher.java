package networking.project.game;

import java.util.*;

public class Launcher {

	public static void main(String[] args){
		System.out.println("Host: Y or N ?");
		Scanner input = new Scanner(System.in);
		if(input.next().trim().toUpperCase().equals("Y")){
			System.out.println("How many players?");
			try{
				new Server(input.nextInt()).startServer();
			}catch(Exception e){
				System.out.println("not a valid input, default to 1 player");
				e.printStackTrace();
				new Server(1).startServer();
			}
		}
			new Client().start();
		
		
		
		
		
		
		
		
	}
	
}
