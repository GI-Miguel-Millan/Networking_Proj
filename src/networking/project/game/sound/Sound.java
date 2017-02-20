package networking.project.game.sound;
import java.applet.Applet;
import java.applet.AudioClip;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import networking.project.game.Game;
import networking.project.game.gfx.Assets;
import resources.ResourceLoader;

public class Sound {
	
	public static final Sound bgm4 = new Sound(ResourceLoader.loadSounds(Assets.fileNames[16]), -15);
	public static final Sound venus = new Sound(ResourceLoader.loadSounds(Assets.fileNames[15]), -15);
	public static final Sound bgm3 = new Sound(ResourceLoader.loadSounds(Assets.fileNames[17]), -15);
	public static final Sound bgm5 = new Sound(ResourceLoader.loadSounds(Assets.fileNames[18]), -15);
	public static final Sound BossMain = new Sound(ResourceLoader.loadSounds(Assets.fileNames[19]), -15);
	public static final Sound fight_looped = new Sound(ResourceLoader.loadSounds(Assets.fileNames[20]), -15);
	public static final Sound Just_Move = new Sound(ResourceLoader.loadSounds(Assets.fileNames[22]), -15);
	public static final Sound Mars = new Sound(ResourceLoader.loadSounds(Assets.fileNames[23]), -15);
	public static final Sound Mercury = new Sound(ResourceLoader.loadSounds(Assets.fileNames[24]), -15);
	public static final Sound failure = new Sound(ResourceLoader.loadSounds(Assets.fileNames[26]), -15);
	public static final Sound victorious = new Sound(ResourceLoader.loadSounds(Assets.fileNames[25]), -5);
	
	public static final Sound lazer = new Sound(ResourceLoader.loadSounds(Assets.fileNames[9]), -30);
	public static final Sound explosion = new Sound(ResourceLoader.loadSounds(Assets.fileNames[7]), -10);
	public static final Sound victory = new Sound(ResourceLoader.loadSounds(Assets.fileNames[10]), -10);
	public static final Sound introjingle = new Sound(ResourceLoader.loadSounds(Assets.fileNames[21]), -10);
	
		
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
				new Thread(){
					public void run(){
						try {
							if(!clip.isOpen()){
								clip.open(audioInputStream);
								clip.loop(Clip.LOOP_CONTINUOUSLY);
								FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
									gainControl.setValue(gain); 
								clip.start();
							}
							
							
						} catch (LineUnavailableException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}.start();			
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
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedAudioFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void resetAll(){
		bgm4.reset();
		venus.reset();
		bgm3.reset();
		bgm5.reset();
		BossMain.reset();
		fight_looped.reset();
		Just_Move.reset();
		Mars.reset();
		Mercury.reset();
		victorious.reset();
		failure.reset();
	}
	
	public static void stopAll(){
		bgm4.stop();
		venus.stop();
		bgm3.stop();
		bgm5.stop();
		BossMain.stop();
		fight_looped.stop();
		Just_Move.stop();
		Mars.stop();
		Mercury.stop();
		victorious.stop();
		failure.stop();
		
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
					} catch (LineUnavailableException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (UnsupportedAudioFileException e) {
						// TODO Auto-generated catch block
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
