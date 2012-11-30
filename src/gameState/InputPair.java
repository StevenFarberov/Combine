package gameState;

import java.util.LinkedList;
import java.util.Random;

import gameState.Board.Color;


public class InputPair {
	
	Random rand = new Random();
	public Orientation orietation = Orientation.HORIZONTAL;
	public Ball ball1;
	public Ball ball2;
	public int location;
	
	public enum Orientation{
		HORIZONTAL, VERTICAL
	}
	
	public InputPair(LinkedList<Color> list)
	{
		int b1 = rand.nextInt(list.size());
		int b2 = rand.nextInt(list.size());
		ball1 = new Ball(list.get(b1));
		ball2 = new Ball(list.get(b2));
		location = 0;
	}
	
	public void rotate()
	{
		if(orietation == Orientation.HORIZONTAL)
		{
			orietation = Orientation.VERTICAL;
		}
		else 
		{
			if (location == 6)
			{
				location--;
			}
			orietation = Orientation.HORIZONTAL;
			Ball temp = ball1;
			ball1 = ball2;
			ball2 = temp;
		}
	}
	
	// FOR TESTING ONLY
	public InputPair(Color c1, Color c2, int loc, Orientation or)
	{
		ball1 = new Ball(c1);
		ball2 = new Ball(c2);
		location = loc;
		orietation = or;
	}
}
