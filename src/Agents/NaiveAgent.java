package Agents;

import java.util.Collections;
import java.util.LinkedList;

import gamePlay.Game;
import gamePlay.Util;
import gameState.Board.Color;
import gameState.InputPair;
import gameState.InputPair.Orientation;

public class NaiveAgent {
	
	public Game game;
	
	public NaiveAgent(Game game)
	{
		this.game = game;
	}
	
	public void playN(int n)
	{
		int score = 0;
		for (int i = 0; i < n; i++)
		{
			int temp = play();
			System.out.println("score " + i + " = " + temp);
			score += temp;
		}
		System.out.println("average score = " + score/n);
	}
	
	public int play()
	{
		while(true)
		{
			try {
				findBestSingleTurn();
				game.drop();
				game.combineAll();
				game.debuggingBoard.print();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				return game.score;
			}
			
		}
	}
	
	public void findBestSingleTurn() throws Exception
	{
		int maxScore = 0;
		int bestLocation = 0;
		Orientation bestOrient = Orientation.HORIZONTAL;
		
		LinkedList<ScoredState> states = new LinkedList<ScoredState>();
		for (int i = 0; i < game.boardWidth; i++ )
		{
			game.pair.location = i;
			if (i < game.boardWidth - 1)
			{
				game.pair.orietation = Orientation.HORIZONTAL;
				states.add(new ScoredState(game.simulateMove(), Orientation.HORIZONTAL, i));
			}
			
			game.pair.orietation = Orientation.VERTICAL;
			states.add(new ScoredState(game.simulateMove(), Orientation.VERTICAL, i));
		}
		Collections.sort(states);
		bestLocation = states.getLast().location;
		bestOrient = states.getLast().orientation;
		maxScore = states.getLast().score;
		
		if (maxScore <= 0) //No combos
		{
			LinkedList<ScoredState> weightedStates = new LinkedList<ScoredState>();
			for (int i = 0; i < game.boardWidth; i++ )
			{
				game.pair.location = i;
				if (i < game.boardWidth - 1)
				{
					game.pair.orietation = Orientation.HORIZONTAL;
					weightedStates.add(new ScoredState(calcWeightedScoreHoriz(i), Orientation.HORIZONTAL, i));
				}
				
				game.pair.orietation = Orientation.VERTICAL;
				weightedStates.add(new ScoredState(calcWeightedScoreVert(i), Orientation.VERTICAL, i));
			}
			Collections.sort(weightedStates);
			bestLocation = weightedStates.getLast().location;
			bestOrient = weightedStates.getLast().orientation;
		}
		
		
		game.pair.location = bestLocation;
		game.pair.orietation = bestOrient;
	}
	
	private int calcWeightedScoreVert(int index)
	{
		try {
			int score = 10;
			int currHighest = game.getHighestColumn();
			
			if((game.findLowestFreeSpace(index) + 2) > currHighest)
			{
				score -= 1;
			}
			
			if (twoTouchingVert(index))
			{
				score += 2;
			}
			return score;
		} catch (Exception e) {
			//e.printStackTrace();
			return 0;
		}
	}
	
	private int calcWeightedScoreHoriz(int index)
	{
		try {
			int score = 10;		
			int currHighest = game.getHighestColumn();
			int highestAfterDrop = Math.max(game.findLowestFreeSpace(index) + 1, game.findLowestFreeSpace(index + 1) + 1);

			if(highestAfterDrop > currHighest)
			{
				score -= 1;
			}
			
			if (twoTouchingHoriz(index))
			{
				score += 2;
			}
			return score;
		} catch (Exception e) {
			//e.printStackTrace();
			return 0;
		}
	}
	
	private boolean createsBlockerVert()
	{
		Color c1 = game.pair.ball1.color;
		Color c2 = game.pair.ball2.color;
		
		return true;
	}
	
	
	private boolean twoTouchingVert(int index)
	{
		Color c1 = game.pair.ball1.color;
		Color c2 = game.pair.ball2.color;
		
		int height;
		try {
			height = game.findLowestFreeSpace(index);
			
			if (game.withinWidth(index+1))
			{
				if (game.board[index+1][height].color == c2 )
				{
					return true;
				}
				if (game.withinHeight(height+1) && game.board[index+1][height+1].color == c1)
				{
					return true;
				}
			}
			
			if (game.withinWidth(index - 1))
			{
				if (game.board[index-1][height].color == c2)
				{
					return true;
				}
				if (game.withinHeight(height + 1) && game.board[index-1][height+1].color == c1)
				{
					return true;
				}
			}
			
			if (game.withinHeight(height - 1) && game.board[index][height-1].color == c2)
			{
				return true;
			}

			return false;
		} catch (Exception e) {
			// Thrown from findLowestFreeSpce when a drop at index will end the game
			return false;
		}
		
	}
	
	
	private boolean twoTouchingHoriz(int index)
	{
		Color c1 = game.pair.ball1.color;
		Color c2 = game.pair.ball2.color;
		
		try {
			int height1 = game.findLowestFreeSpace(index);
			int height2 = game.findLowestFreeSpace(index + 1);
			
			if (game.withinWidth(index+2) && game.board[index+2][height2].color == c2)
			{
				return true;
			}
			
			if (game.withinHeight(height2 - 1) && game.board[index+1][height2-1].color == c2)
			{
				return true;
			}
			
			if (game.withinWidth(index-1) && game.board[index-1][height1].color == c1)
			{
				return true;
			}
			
			if (game.withinHeight(height1 - 1) && game.board[index][height1-1].color == c1)
			{
				return true;
			}
			
			
			if (height1 != height2)
			{
				if (game.board[index+1][height1].color == c1 || game.board[index][height2].color == c2)
				{
					return true;
				}
			}
			
			return false;
		} catch (Exception e) {
			// Thrown from findLowestFreeSpce when a drop at index will end the game
			return false;
		}	
	}
	
	public class ScoredState implements Comparable<ScoredState>
	{
		public int score;
		public Orientation orientation;
		public int location;
		
		public ScoredState(int sc, Orientation o, int loc)
		{
			score = sc;
			orientation = o;
			location = loc;
		}

		@Override
		public int compareTo(ScoredState other) {
			if (score > other.score)
			{
				return 1;
			}
			if (score < other.score)
			{
				return -1;
			}
			if (location < other.location)
			{
				return 1;
			}
			if (location > other.location)
			{
				return -1;
			}
			if (orientation == Orientation.HORIZONTAL && other.orientation == Orientation.VERTICAL)
			{
				return 1;
			}
			if (orientation == Orientation.VERTICAL && other.orientation == Orientation.HORIZONTAL)
			{
				return -1;
			}
			return 0;
		}
	}

}
