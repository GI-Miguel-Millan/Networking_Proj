package networking.project.game.ui;

import java.awt.Graphics;

public class UILabel extends UIObject {

	private ClickListener clicker;
	private String text;
	
	public UILabel(float x, float y, int width, int height, String text, ClickListener clicker) {
		super(x, y, width, height);
		this.clicker = clicker;
		this.text = text;
	}

	@Override
	public void tick() {
		
	}
	
	@Override
	public void render(Graphics g) {
		g.drawString(text, (int)x, (int)y);
	}

	@Override
	public void onClick() {
		clicker.onClick();
	}
	
	public void setText(String txt){
		text = txt;
	}

}
