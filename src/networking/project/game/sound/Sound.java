package networking.project.game.sound;
import networking.project.game.Game;
import networking.project.game.gfx.Assets;
import resources.ResourceLoader;

import javax.sound.sampled.*;
import java.net.URL;

public class Sound {

	public static boolean initted = false;
	
	public static Sound lazer;
	

	public static void init()
	{
	    if (initted)
	        return;

		lazer = new Sound(ResourceLoader.loadSounds(Assets.fileNames[9]), -30);

		initted = true;
	}

	private Clip clip;
	private AudioInputStream audioInputStream;
	private float gain;
	private URL name;
	
	public Sound(URL filename, float audioGain)
	{
		name = filename;
		try{
			audioInputStream = AudioSystem.getAudioInputStream(filename);
			clip = AudioSystem.getClip();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		this.gain = audioGain;
		
	}
	
	public void play()
	{
		if(!Game.MUTED){
			try{
				new Thread(() -> {
                    try {
                        if(!clip.isOpen()){
                            clip.open(audioInputStream);
                            clip.loop(Clip.LOOP_CONTINUOUSLY);
                            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                                gainControl.setValue(gain);
                            clip.start();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).start();
			}catch(Exception ex){
				ex.printStackTrace();
				
			}
		}
	}
	
	public void stop(){
		clip.stop();
		clip.close();
	}
	
	public void reset(){
		try {
			audioInputStream = AudioSystem.getAudioInputStream(name);
			clip = AudioSystem.getClip();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void resetAll(){
		if (!initted)
			return;
		lazer.reset();
	}
	
	public static void stopAll(){
		if (!initted)
			return;
		lazer.stop();
	}
	
	public synchronized void execute()
	{
		if(clip.isOpen())
			clip.close();
		
		if(!Game.MUTED){
			try{
			new Thread(){
				public void run(){
					try {
						audioInputStream = AudioSystem.getAudioInputStream(name);
						Clip clip = AudioSystem.getClip();
						clip.open(audioInputStream);
						clip.setFramePosition(0);
						FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
							gainControl.setValue(gain); 
							
						clip.addLineListener(new CloseClipWhenDone());
						clip.start();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				}.start();
			}catch(Exception ex){
				ex.printStackTrace();
				
			}
		}
	}
	
	private static class CloseClipWhenDone implements LineListener
	{
	    @Override public void update(LineEvent event)
	    {
	        if (event.getType().equals(LineEvent.Type.STOP))
	        {
	            Line soundClip = event.getLine();
	            soundClip.close();
	        }
	    }
	}
}
