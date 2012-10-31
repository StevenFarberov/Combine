package gamePlay;

import gameState.Ball;
import gameState.Board.Color;

import java.awt.Point;

public class PlayedBall extends Ball{
	
	public Point location;
	
	public PlayedBall(Color c, Point p)
	{
		super(c);
		location = p;
	}
	

}
