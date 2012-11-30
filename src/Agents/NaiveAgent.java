package Agents;

import java.util.Collections;
import java.util.LinkedList;

import gamePlay.Game;
import gamePlay.Util;
import gameState.Ball;
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
		Color ball1Color = null;
		Color ball2Color = null;
		Orientation bestOrient = Orientation.HORIZONTAL;
		LinkedList<ScoredState> states = new LinkedList<ScoredState>();
		
		for (int i = 0; i < game.boardWidth; i++ )
		{
			game.pair.location = i;
			
			// Horizontal scores
			if (i < game.boardWidth - 1)
			{
				game.pair.orietation = Orientation.HORIZONTAL;
				states.add(new ScoredState(game.simulateMove(), Orientation.HORIZONTAL, i, 
						game.pair.ball1.color, game.pair.ball2.color));
				
				ballSwap(game.pair);
				
				states.add(new ScoredState(game.simulateMove(), Orientation.HORIZONTAL, i, 
						game.pair.ball1.color, game.pair.ball2.color));
				
			}
			
			// Vertical Scores
			game.pair.orietation = Orientation.VERTICAL;
			states.add(new ScoredState(game.simulateMove(), Orientation.VERTICAL, i, 
					game.pair.ball1.color, game.pair.ball2.color));
			
			ballSwap(game.pair);
			
			states.add(new ScoredState(game.simulateMove(), Orientation.VERTICAL, i, 
					game.pair.ball1.color, game.pair.ball2.color));
		}
		Collections.sort(states);
		
		ScoredState bestMove = states.getLast();
		
		ball1Color = bestMove.ball1Color;
		ball2Color = bestMove.ball2Color;
		bestLocation = bestMove.location;
		bestOrient = bestMove.orientation;
		maxScore = bestMove.score;
		
		if (maxScore <= 0) //No combos
		{
			LinkedList<ScoredState> weightedStates = new LinkedList<ScoredState>();
			
			for (int i = 0; i < game.boardWidth; i++ )
			{
				game.pair.location = i;
				if (i < game.boardWidth - 1)
				{
					System.out.println("for i = " + i + ": " + calcWeightedScoreHoriz(i));
					game.pair.orietation = Orientation.HORIZONTAL;
					weightedStates.add(new ScoredState(calcWeightedScoreHoriz(i), Orientation.HORIZONTAL, i, 
							game.pair.ball1.color, game.pair.ball2.color));
					
					ballSwap(game.pair);
					
					weightedStates.add(new ScoredState(calcWeightedScoreHoriz(i), Orientation.HORIZONTAL, i, 
							game.pair.ball1.color, game.pair.ball2.color));
				}
				
				game.pair.orietation = Orientation.VERTICAL;
				weightedStates.add(new ScoredState(calcWeightedScoreVert(i), Orientation.VERTICAL, i, 
						game.pair.ball1.color, game.pair.ball2.color));
				
				ballSwap(game.pair);
				
				weightedStates.add(new ScoredState(calcWeightedScoreVert(i), Orientation.VERTICAL, i, 
						game.pair.ball1.color, game.pair.ball2.color));
			}
			Collections.sort(weightedStates);
			ScoredState bestMove2 = weightedStates.getLast();
			
			ball1Color = bestMove2.ball1Color;
			ball2Color = bestMove2.ball2Color;
			bestLocation = bestMove2.location;
			bestOrient = bestMove2.orientation;
			maxScore = bestMove2.score;
		}
		
		game.pair = new InputPair(ball1Color, ball2Color, bestLocation, bestOrient);
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
				score += 4;
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
				System.out.println("Found 2 touching horizontally");
				score += 4;
			}
			return score;
		} catch (Exception e) {
			//e.printStackTrace();
			return 0;
		}
	}
	
	public void ballSwap(InputPair pair)
	{
		Ball temp = pair.ball1;
		pair.ball1 = pair.ball2;
		pair.ball2 = temp;
	}
	
	private boolean createsBlockerVert()
	{
		Color c1 = game.pair.ball1.color;
		Color c2 = game.pair.ball2.color;
		
		return true;
	}
	
	
	public boolean twoTouchingVert(int index)
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
			e.printStackTrace();
			return false;
		}
		
	}
	
	
	public boolean twoTouchingHoriz(int index)
	{
		Color c1 = game.pair.ball1.color;
		Color c2 = game.pair.ball2.color;
		
		try {
			int height1 = game.findLowestFreeSpace(index);
			int height2 = game.findLowestFreeSpace(index + 1);

			if (touchingHorizRight(index, height2, c2))
			{
				return true;
			}
			
			if (touchingHorizRightBottom(index, height2, c2))
			{
				return true;
			}
			
			if (touchingHorizLeft(index, height1, c1))
			{
				return true;
			}

			if (touchingHorizLeftBottom(index, height1, c1))
			{
				return true;
			}
			
			
			if (height1 > height2 && game.board[index][height2].color == c2)
			{
				return true;
			}
			if (height1 < height2 && game.board[index+1][height1].color == c1)
			{
						return true;
			}
			
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}	
	}
	
	// Horizonal touching helpers
	private boolean touchingHorizRight(int index, int height, Color c)
	{
		if(game.withinWidth(index+2) && game.board[index+2][height] != null && game.board[index+2][height].color == c)
		{
			return true;
		}
		return false;
	}
	
	private boolean touchingHorizRightBottom(int index, int height, Color c)
	{
		if(game.withinHeight(height - 1) && game.board[index+1][height-1] != null && game.board[index+1][height-1].color == c)
		{
			return true;
		}
		return false;
	}
	
	private boolean touchingHorizLeft(int index, int height, Color c)
	{
		if(game.withinWidth(index-1) && game.board[index-1][height] != null && game.board[index-1][height].color == c)
		{
			return true;
		}
		return false;
	}
	
	private boolean touchingHorizLeftBottom(int index, int height, Color c)
	{
		if(game.withinHeight(height - 1) && game.board[index][height-1] != null && game.board[index][height-1].color == c)
		{
			return true;
		}
		return false;
	}
	
	public class ScoredState implements Comparable<ScoredState>
	{
		public int score;
		public Orientation orientation;
		public int location;
		public Color ball1Color;
		public Color ball2Color;
		
		public ScoredState(int sc, Orientation o, int loc, Color ball1, Color ball2)
		{
			score = sc;
			orientation = o;
			location = loc;
			ball1Color = ball1;
			ball2Color = ball2;
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
