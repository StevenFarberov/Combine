package tests;

import static org.junit.Assert.*;
import gamePlay.Game;
import gameState.Ball;
import gameState.Board;
import gameState.Board.Color;

import org.junit.Test;

import Agents.NaiveAgent;

public class SimulationTests {

	@Test
	public void testSimpleSim() throws Exception {
		Board board = new Board();
		Game game = new Game(board);
		
		board.boardState[0][0] = new Ball(Color.GREEN);
		board.boardState[1][0] = new Ball(Color.GREEN);

		board.print();
		
		game.simulateMove();
		
		board.print();
		
		assertEquals(Color.GREEN, board.boardState[0][0].color);
	}

}
