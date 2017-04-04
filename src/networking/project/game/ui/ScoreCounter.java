package networking.project.game.ui;

import java.awt.*;

public class ScoreCounter
{
	protected int scoreCount = 0;
	
	public ScoreCounter(int scoreCount)
	{
		this.scoreCount = scoreCount;
	}
	
	public int getScore()
	{
		return scoreCount;
	}
	
	public void addToScore(int pointValue)
	{
		scoreCount = scoreCount + pointValue;
	}
	
	public void tick()
	{
		
	}
	
	public void render(Graphics g)
	{
		
	}
}
