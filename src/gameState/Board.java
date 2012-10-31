package gameState;

import java.awt.Point;
import java.util.Arrays;
import java.util.LinkedList;

public class Board {

	public static int height;
	public static int width;
	public Ball[][] boardState;
	
	public Board()
	{
		height = 9;
		width = 7;
		boardState = new Ball[7][9];
	}
	
	public Board(int height, int width)
	{
		this.height = height;
		this.width = width;
		boardState = new Ball[width][height];
	}
	
	public enum Color{
		GREEN, YELLOW, ORANGE, RED, PINK, PURPLE, BLUE, CYAN, BLACK, WHITE, NONE
	}
	
	
	public void print() {
		for (int rowIndex = height - 1; rowIndex >= 0; rowIndex--)
		{
			for (int colIndex = 0; colIndex < width; colIndex++)
			{
				Ball ball = boardState[colIndex][rowIndex];
				if (ball != null)
				{
					System.out.print(ball.color);
					for (int i = ball.color.toString().length(); i < 8; i++)
					{
						System.out.print(" ");
					}
					System.out.print("|");
				}
				else
				{
					System.out.print("null");
					for (int i = 4; i < 8; i++)
					{
						System.out.print(" ");
					}
					System.out.print("|");
				}
			}
			System.out.println();
		}
		System.out.println();
	}
	
}
