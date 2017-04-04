package networking.project.game.sound;
import networking.project.game.Game;
import networking.project.game.gfx.Assets;
import resources.ResourceLoader;

import javax.sound.sampled.*;
import java.net.URL;

public class Sound {

	public static boolean initted = false;

	public static Sound bgm4;
	public static Sound venus;
	public static Sound bgm3;
	public static Sound bgm5;
	public static Sound BossMain;
	public static Sound fight_looped;
	public static Sound Just_Move;
	public static Sound Mars;
	public static Sound Mercury;
	public static Sound failure;
	public static Sound victorious;
	
	public static Sound lazer;
	public static Sound explosion;
	public static Sound victory;
	public static Sound introjingle;
	

	public static void init()
	{
	    if (initted)
	        return;
		bgm4 = new Sound(ResourceLoader.loadSounds(Assets.fileNames[16]), -15);
		venus = new Sound(ResourceLoader.loadSounds(Assets.fileNames[15]), -15);
		bgm3 = new Sound(ResourceLoader.loadSounds(Assets.fileNames[17]), -15);
		bgm5 = new Sound(ResourceLoader.loadSounds(Assets.fileNames[18]), -15);
		BossMain = new Sound(ResourceLoader.loadSounds(Assets.fileNames[19]), -15);
		fight_looped = new Sound(ResourceLoader.loadSounds(Assets.fileNames[20]), -15);
		Just_Move = new Sound(ResourceLoader.loadSounds(Assets.fileNames[22]), -15);
		Mars = new Sound(ResourceLoader.loadSounds(Assets.fileNames[23]), -15);
		Mercury = new Sound(ResourceLoader.loadSounds(Assets.fileNames[24]), -15);
		failure = new Sound(ResourceLoader.loadSounds(Assets.fileNames[26]), -15);
		victorious = new Sound(ResourceLoader.loadSounds(Assets.fileNames[25]), -5);

		lazer = new Sound(ResourceLoader.loadSounds(Assets.fileNames[9]), -30);
		explosion = new Sound(ResourceLoader.loadSounds(Assets.fileNames[7]), -10);
		victory = new Sound(ResourceLoader.loadSounds(Assets.fileNames[10]), -10);
		introjingle = new Sound(ResourceLoader.loadSounds(Assets.fileNames[21]), -10);

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
		if (!initted)
			return;
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
