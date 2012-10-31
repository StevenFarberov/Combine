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
		game.pair = new InputPair(Color.GREEN, Color.YELLOW, 0, Orientation.HORIZONTAL);
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
		game.drop();
		game.combineAll();
		
		assertEquals(Color.YELLOW, board.boardState[0][0].color);
		assertEquals(Color.YELLOW, board.boardState[1][0].color);
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
		
		assertEquals(Color.GREEN, board.boardState[0][1].color);
		assertEquals(Color.YELLOW, board.boardState[1][1].color);
	}

}
