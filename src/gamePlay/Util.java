package gamePlay;

import java.util.LinkedList;

import Agents.NaiveAgent.ScoredState;

import gameState.Board.Color;

public class Util {

	public static Color nextColor(Color c)
	{
		switch(c)
		{
		case GREEN:
			return Color.YELLOW;
		case YELLOW:
			return Color.ORANGE;
		case ORANGE:
			return Color.RED;
		case RED:
			return Color.PINK;
		case PINK:
			return Color.PURPLE;
		case PURPLE:
			return Color.BLUE;
		case BLUE:
			return Color.CYAN;
		case CYAN:
			return Color.BLACK;
		case BLACK:
			return Color.WHITE;
		case WHITE:
			return null;
			
		}
		return null;
	}
	
	public static void printListColors(LinkedList<PlayedBall> balls)
	{
		for (PlayedBall ball : balls)
		{
			System.out.println(ball.color + " ball at " + ball.location);
		}
		System.out.println();
	}
	
	public static void printStates(LinkedList<ScoredState> states)
	{
		for (ScoredState state : states)
		{
			System.out.println("Pair at location " + state.location + " with orientation " + state.orientation +
					" and score = " + state.score);
		}
	}
}
