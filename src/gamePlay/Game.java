package gamePlay;

import java.awt.Point;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;

import com.rits.cloning.Cloner;

import gameState.Ball;
import gameState.Board;
import gameState.InputPair;
import gameState.Board.Color;
import gameState.InputPair.Orientation;

public class Game {
	
	public Board debuggingBoard;
	public Ball[][] board;
	int boardHeight;
	public int boardWidth;
	public InputPair pair;
	public LinkedList<Color> availableColors;
	public int score;
	public int turnNumber;
	
	public Game(Board b){
		debuggingBoard = b;
		board = b.boardState;
		boardHeight = b.height;
		boardWidth = b.width;
		availableColors = new LinkedList<Color>();
		availableColors.add(Color.GREEN);
		availableColors.add(Color.YELLOW);
		pair = new InputPair(availableColors);
		pair.location = boardWidth/2;
		score = 0;
		turnNumber = 0;
	}
	
	public void drop() throws Exception
	{
		if (pair.orietation == Orientation.HORIZONTAL)
		{
			horizontalDrop(pair);
		}
		else
		{
			verticalDrop(pair);
		}
		pair = new InputPair(availableColors);
		pair.location = boardWidth/2;
		turnNumber++;
	}
	
	public void moveLeft()
	{
		if (pair.location == 0){
			return;
		}
		
		pair.location--;
	}
	
	public void moveRight()
	{
		if (pair.location == boardWidth - 1){
			return;
		}
		
		if (pair.orietation == Orientation.HORIZONTAL && pair.location == boardWidth - 2)
		{
			return;
		}
		
		pair.location++;
	}
	
	public void dropRandomly() throws Exception
	{
		Random rand = new Random();
		int location = rand.nextInt(6);
		pair.location = location;
		drop();
		combineAll();
	}

	public void combineAll() throws Exception
	{
		LinkedList<LinkedList<PlayedBall>> allCombos = new LinkedList<LinkedList<PlayedBall>>();
		LinkedList<Color> allColors = new LinkedList<Color>();
		LinkedList<Point> allLocations = new LinkedList<Point>();
		
		for (int rowIndex = 0; rowIndex < boardHeight; rowIndex++)
		{
			for (int colIndex = 0; colIndex < boardWidth; colIndex++)
			{
				if (board[colIndex][rowIndex] == null)
				{
					continue;
				}
				
				HashSet<Point> ballsSeen = new HashSet<Point>();
				Color currentColor = board[colIndex][rowIndex].color;
				Point currentPos = new Point(colIndex, rowIndex);
				
				LinkedList<PlayedBall> toCombine = new LinkedList<PlayedBall>();
				toCombine.add(new PlayedBall(currentColor, currentPos));
				
				toCombine = combineHelper(currentPos.x, currentPos.y, currentColor, toCombine, ballsSeen);
				if (toCombine.size() > 2)
				{
					allCombos.add(toCombine);
					allColors.add(Util.nextColor(currentColor));
					allLocations.add(getLeftmostLowest(toCombine));
				}
			}
		}
		
		if (allCombos.size() > 0)
		{
			combine(allCombos, allColors, allLocations);
			shiftAllDown();
			combineAll();
		}
		
		
		if (getHighestColumn() > boardHeight-2){
			throw new Exception("Game Over");
		}
	}
	
	public void combine(LinkedList<LinkedList<PlayedBall>> listsOfBalls, LinkedList<Color> colors, LinkedList<Point> locations)
	{
		for (int i = 0; i < colors.size(); i++)
		{
			Color currentColor = listsOfBalls.get(i).get(0).color;
			
			score+= getScore(currentColor, listsOfBalls.get(i).size());
			for (PlayedBall p : listsOfBalls.get(i))
			{
				board[p.location.x][p.location.y] = null;	
			}
			
			Point newLocation = locations.get(i);
			Color newColor = colors.get(i);
			
			if (colors.get(i) == null)
			{
				board[newLocation.x][newLocation.y] = null;
			}
			else
			{
				board[newLocation.x][newLocation.y] = new Ball(newColor);
				if (!availableColors.contains(newColor))
				{
					availableColors.add(currentColor);
				}
			}
		}
		
	}
	
	public int simulateMove()
	{
		Cloner cloner =new Cloner();
			
		Game clone = cloner.deepClone(this);
		
		int currScore = clone.score;
		try {
			clone.drop();
			clone.combineAll();
		} catch (Exception e) {
			return -1;
			// TODO Auto-generated catch block
		}
		
		int turnScore =clone.score - currScore;
		/*int highestCol = getHeighestColumn(clone);
		turnScore -= turnNumber*highestCol;*/
		return turnScore;
	}
	
	
	private int getHighestColumn(Game game)
	{
		int highest = 0;
		for (int colIndex = 0; colIndex < game.boardWidth; colIndex++)
		{
			int height = 0;
			for (int rowIndex = 0; rowIndex < game.boardHeight; rowIndex++)
			{
				if (game.board[colIndex][rowIndex] != null)
				{
					height++;
				}
				else break;
			}
			if (height > highest)
			{
				highest = height;
			}
		}
		return highest;
	}
	
	public int getHighestColumn()
	{
		int highest = 0;
		for (int colIndex = 0; colIndex < boardWidth; colIndex++)
		{
			int height = 0;
			for (int rowIndex = 0; rowIndex < boardHeight; rowIndex++)
			{
				if (board[colIndex][rowIndex] != null)
				{
					height++;
				}
				else break;
			}
			if (height > highest)
			{
				highest = height;
			}
		}
		return highest;
	}
	
	/********************        Helpers          ***********************/
	
	private int getScore(Color color, int numBalls)
	{
		switch(color)
		{
		case GREEN:
			return numBalls*5;
		case YELLOW:
			return numBalls*10;
		case ORANGE:
			return numBalls*15;
		case RED:
			return numBalls*35;
		case PINK:
			return numBalls*295;
		case PURPLE:
			return numBalls*305;
		case BLUE:
			return numBalls*350;
		case CYAN:
			return numBalls*325;
		case BLACK:
			return numBalls*400;
		case WHITE:
			return numBalls*500;
		}
		return 0;
	}
	
	private void shiftAllDown() throws Exception
	{
		for (int rowIndex = 0; rowIndex < boardHeight; rowIndex++)
		{
			for (int colIndex = 0; colIndex < boardWidth; colIndex++)
			{
				Ball temp = board[colIndex][rowIndex];
				board[colIndex][rowIndex] = null;
				board[colIndex][findLowestFreeSpace(colIndex)] = temp;
			}
		}
	}
	
	
	private LinkedList<PlayedBall> combineHelper(int x, int y, Color color, LinkedList<PlayedBall> toCombine, HashSet<Point> ballsSeen)
	{	
		if (board[x][y] == null)
		{
			return toCombine;
		}
		
		ballsSeen.add(new Point(x,y));
		
		if (withinRangeAndNotSeen(x+1,y, ballsSeen) && colorMatches(board[x+1][y], color))
		{
			toCombine.add(new PlayedBall(color, new Point(x+1,y)));
			combineHelper(x+1, y, color, toCombine, ballsSeen);
		}
		
		if (withinRangeAndNotSeen(x-1,y, ballsSeen) && colorMatches(board[x-1][y], color))
		{
			toCombine.add(new PlayedBall(color, new Point(x-1,y)));
			combineHelper(x-1, y, color, toCombine, ballsSeen);
		}
		
		if (withinRangeAndNotSeen(x,y+1, ballsSeen) && colorMatches(board[x][y+1], color))
		{
			toCombine.add(new PlayedBall(color, new Point(x,y+1)));
			combineHelper(x, y+1, color, toCombine, ballsSeen);
		}
		
		if (withinRangeAndNotSeen(x,y-1, ballsSeen) && colorMatches(board[x][y-1], color))
		{
			toCombine.add(new PlayedBall(color, new Point(x,y-1)));
			combineHelper(x, y-1, color, toCombine, ballsSeen);
		}
		
		return toCombine;
	}
	
	
	private boolean withinRangeAndNotSeen(int x, int y, HashSet<Point> ballsSeen)
	{
		if ( withinWidth(x) && withinHeight(y) && !ballsSeen.contains(new Point(x,y))) {
			return true;
		}
		return false;
	}
	
	private boolean colorMatches(Ball ball, Color c)
	{
		if(ball != null && ball.color == c)
		{
			return true;
		}
		return false;
	}
	
	public boolean withinWidth(int x)
	{
		if (x < boardWidth && x >= 0)
			return true;
		return false;
	}
	
	public boolean withinHeight(int y)
	{
		if (y < boardHeight && y >= 0)
			return true;
		return false;
	}
	
	private void horizontalDrop(InputPair pair) throws Exception
	{
		int height1 = findLowestFreeSpace(pair.location);
		int height2 = findLowestFreeSpace(pair.location + 1);

		board[pair.location][height1] = pair.ball1;
		board[pair.location + 1][height2] = pair.ball2;
	}
	

	private void verticalDrop(InputPair pair) throws Exception
	{
		int pos = pair.location;
		int height = findLowestFreeSpace(pos);
		
		board[pos][height + 1] = pair.ball1;
		board[pos][height] = pair.ball2;
	}
	
	public int findLowestFreeSpace(int x) throws Exception 
	{
		int height = 0;
		while (board[x][height] != null)
		{
			height++;
			if (!withinHeight(height))
			{
				throw new Exception("Game Over");
			}
		}
		return height;
	}
	
	private Point getLeftmostLowest(LinkedList<PlayedBall> balls)
	{
		int x = Integer.MAX_VALUE;
		int y = Integer.MAX_VALUE;
		
		for (PlayedBall ball : balls)
		{
			Point p = ball.location;
			if (p.y < y)
			{
				y = p.y;
				x = p.x;
			}
			if (p.y == y)
			{
				if (p.x < x)
				{
					y = p.y;
					x = p.x;
				}
			}
		}
		
		return new Point(x,y);
	}
}
