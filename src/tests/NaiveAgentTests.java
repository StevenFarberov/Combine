package tests;

import static org.junit.Assert.*;
import gamePlay.Game;
import gameState.Ball;
import gameState.Board;
import gameState.Board.Color;
import gameState.InputPair;
import gameState.InputPair.Orientation;

import org.junit.Test;

import Agents.NaiveAgent;

public class NaiveAgentTests {
	
	@Test
	public void testSwap() 
	{
		Board board = new Board();
		Game game = new Game(board);
		game.pair = new InputPair(Color.GREEN, Color.YELLOW, 0, Orientation.HORIZONTAL);
		NaiveAgent agent = new NaiveAgent(game);
		
		assertEquals(Color.GREEN, game.pair.ball1.color);
		assertEquals(Color.YELLOW, game.pair.ball2.color);
		
		agent.ballSwap(game.pair);
		
		assertEquals(Color.YELLOW, game.pair.ball1.color);
		assertEquals(Color.GREEN, game.pair.ball2.color);
	}

	@Test
	public void testBestMoveWithEmptyBoard() throws Exception {
		Board board = new Board();
		Game game = new Game(board);
		NaiveAgent agent = new NaiveAgent(game);
		game.pair = new InputPair(Color.GREEN, Color.YELLOW, 0, Orientation.HORIZONTAL);
		agent.findBestSingleTurn();
		
		assertEquals(0, game.pair.location);
		assertEquals(Orientation.HORIZONTAL, game.pair.orietation);
	}
	
	@Test
	public void testBestMoveWithOnePairHoriz() throws Exception {
		Board board = new Board();
		Game game = new Game(board);
		
		board.boardState[0][0] = new Ball(Color.GREEN);
		board.boardState[1][0] = new Ball(Color.GREEN);
		
		NaiveAgent agent = new NaiveAgent(game);
		game.pair = new InputPair(Color.GREEN, Color.GREEN, 3, Orientation.HORIZONTAL);
		agent.findBestSingleTurn();
		game.drop();
		game.combineAll();
		
		assertEquals(Color.YELLOW, board.boardState[0][0].color);
	}
	
	@Test
	public void testBestMoveWithOnePairVert() throws Exception {
		Board board = new Board();
		Game game = new Game(board);
		
		board.boardState[0][0] = new Ball(Color.GREEN);
		board.boardState[0][1] = new Ball(Color.GREEN);
		
		NaiveAgent agent = new NaiveAgent(game);
		game.pair = new InputPair(Color.GREEN, Color.YELLOW, 0, Orientation.HORIZONTAL);
		agent.findBestSingleTurn();
		
		System.out.println("ball1 = " + game.pair.ball1.color + " , ball2 = " + game.pair.ball2.color +
				" with Orient = " + game.pair.orietation);
		
		game.drop();
		game.combineAll();

		assertEquals(Color.YELLOW, board.boardState[0][0].color);
		assertEquals(Color.YELLOW, board.boardState[0][1].color);
	}
	
	@Test
	public void testBestMoveWithOnePairNoCombos() throws Exception {
		Board board = new Board();
		Game game = new Game(board);
		
		board.boardState[0][0] = new Ball(Color.GREEN);
		board.boardState[0][1] = new Ball(Color.YELLOW);
		
		NaiveAgent agent = new NaiveAgent(game);
		game.pair = new InputPair(Color.GREEN, Color.YELLOW, 0, Orientation.HORIZONTAL);
		agent.findBestSingleTurn();
		
		game.drop();
		game.combineAll();
		
		board.print();
		
		assertEquals(Color.GREEN, board.boardState[1][0].color);
		assertEquals(Color.YELLOW, board.boardState[1][1].color);
	}
	
	@Test
	public void testTwoTouchingHueristic() throws Exception {
		Board board = new Board();
		Game game = new Game(board);
		
		board.boardState[0][0] = new Ball(Color.PINK);
		board.boardState[1][0] = new Ball(Color.RED);
		board.boardState[2][0] = new Ball(Color.PINK);
		board.boardState[3][0] = new Ball(Color.GREEN);
		board.boardState[4][0] = new Ball(Color.PINK);
		board.boardState[5][0] = new Ball(Color.RED);
		board.boardState[6][0] = new Ball(Color.RED);
		
		NaiveAgent agent = new NaiveAgent(game);
		game.pair = new InputPair(Color.GREEN, Color.YELLOW, 3, Orientation.HORIZONTAL);
		assertEquals(true, agent.twoTouchingHoriz(3));
		agent.findBestSingleTurn();
		
		game.drop();
		game.combineAll();
		
		
		assertEquals(Color.GREEN, board.boardState[3][1].color);
	}
	
	@Test
	public void testTwoTouchingHueristicVert() throws Exception {
		Board board = new Board();
		Game game = new Game(board);
		
		board.boardState[6][0] = new Ball(Color.GREEN);
		board.boardState[5][0] = new Ball(Color.RED);
		board.boardState[5][1] = new Ball(Color.PINK);
		board.boardState[5][2] = new Ball(Color.RED);
		board.boardState[5][3] = new Ball(Color.PINK);
		board.boardState[5][4] = new Ball(Color.RED);
		board.boardState[5][5] = new Ball(Color.PINK);
		board.boardState[5][6] = new Ball(Color.RED);
		
		NaiveAgent agent = new NaiveAgent(game);
		game.pair = new InputPair(Color.YELLOW, Color.GREEN, 6, Orientation.VERTICAL);
		assertEquals(true, agent.twoTouchingVert(6));
		agent.findBestSingleTurn();
		
		game.drop();
		game.combineAll();
		
		
		assertEquals(Color.GREEN, board.boardState[6][1].color);
	}

}
