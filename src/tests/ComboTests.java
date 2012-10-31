package tests;

import static org.junit.Assert.*;
import gamePlay.Game;
import gameState.Ball;
import gameState.Board;
import gameState.Board.Color;

import java.util.LinkedList;

import org.junit.Test;

public class ComboTests {

	@Test
	public void testCombineAll() throws Exception {
		Board board = new Board();
		Game gl = new Game(board);
		LinkedList colors = new LinkedList<Color>();
		colors.add(Color.GREEN);
		colors.add(Color.YELLOW);
		
		board.print();
		
		board.boardState[0][0] = new Ball(Color.GREEN);
		board.boardState[1][0] = new Ball(Color.GREEN);
		board.boardState[2][0] = new Ball(Color.GREEN);
		
		gl.combineAll();
		
		assertTrue(board.boardState[0][0].color == Color.YELLOW);
		assertTrue(board.boardState[1][0] == null);
	}
	
	@Test
	public void testCombineAllOnEdges() throws Exception {
		Board board = new Board();
		Game gl = new Game(board);
		LinkedList colors = new LinkedList<Color>();
		colors.add(Color.GREEN);
		colors.add(Color.YELLOW);
		
		board.boardState[4][0] = new Ball(Color.GREEN);
		board.boardState[5][0] = new Ball(Color.GREEN);
		board.boardState[6][0] = new Ball(Color.GREEN);
		
		gl.combineAll();
		
		assertTrue(board.boardState[4][0].color == Color.YELLOW);
		assertTrue(board.boardState[5][0] == null);
	}
	
	@Test
	public void testCombineAllDouble() throws Exception {
		Board board = new Board();
		Game gl = new Game(board);
		LinkedList colors = new LinkedList<Color>();
		colors.add(Color.GREEN);
		colors.add(Color.YELLOW);
		
		board.boardState[0][0] = new Ball(Color.GREEN);
		board.boardState[1][0] = new Ball(Color.GREEN);
		board.boardState[2][0] = new Ball(Color.GREEN);
		
		board.boardState[0][1] = new Ball(Color.YELLOW);
		board.boardState[1][1] = new Ball(Color.YELLOW);
		board.boardState[2][1] = new Ball(Color.YELLOW);
		
		gl.combineAll();
		
		assertEquals(Color.YELLOW, board.boardState[0][0].color);
		assertTrue(board.boardState[1][0] == null);
		
		assertEquals(Color.ORANGE,board.boardState[0][1].color);
		assertTrue(board.boardState[1][1] == null);

	}
	
	@Test
	public void testCombineAllRecursive() throws Exception {
		Board board = new Board();
		Game gl = new Game(board);
		LinkedList colors = new LinkedList<Color>();
		colors.add(Color.GREEN);
		colors.add(Color.YELLOW);
		
		board.boardState[0][0] = new Ball(Color.GREEN);
		board.boardState[1][0] = new Ball(Color.GREEN);
		board.boardState[2][0] = new Ball(Color.GREEN);
		
		board.boardState[0][1] = new Ball(Color.YELLOW);
		board.boardState[0][2] = new Ball(Color.YELLOW);
		
		gl.combineAll();
		
		assertEquals(Color.ORANGE, board.boardState[0][0].color);
		assertTrue(board.boardState[1][0] == null);
		assertTrue(board.boardState[0][1] == null);

	}
	
	@Test
	public void testShiftDown() throws Exception {
		Board board = new Board();
		Game gl = new Game(board);
		LinkedList colors = new LinkedList<Color>();
		colors.add(Color.GREEN);
		colors.add(Color.YELLOW);
		
		board.boardState[0][0] = new Ball(Color.GREEN);
		board.boardState[0][1] = new Ball(Color.GREEN);
		board.boardState[0][2] = new Ball(Color.GREEN);
		
		board.boardState[0][3] = new Ball(Color.YELLOW);
		board.boardState[0][4] = new Ball(Color.YELLOW);
		
		gl.combineAll();
		
		assertEquals(Color.ORANGE, board.boardState[0][0].color);
		assertTrue(board.boardState[1][0] == null);
		assertTrue(board.boardState[0][1] == null);

	}
	
	@Test
	public void testDiagnolCombos() throws Exception {
		Board board = new Board();
		Game gl = new Game(board);
		LinkedList colors = new LinkedList<Color>();
		colors.add(Color.GREEN);
		colors.add(Color.YELLOW);
		
		board.boardState[0][0] = new Ball(Color.GREEN);
		board.boardState[1][0] = new Ball(Color.YELLOW);
		board.boardState[2][0] = new Ball(Color.GREEN);
		board.boardState[3][0] = new Ball(Color.GREEN);
		
		board.boardState[0][1] = new Ball(Color.GREEN);
		board.boardState[1][1] = new Ball(Color.GREEN);
		board.boardState[2][1] = new Ball(Color.YELLOW);
		
		gl.combineAll();
		
		assertEquals(Color.YELLOW, board.boardState[0][0].color);
		assertEquals(Color.YELLOW, board.boardState[1][0].color);
		assertEquals(Color.GREEN, board.boardState[2][0].color);

	}
	
	@Test
	public void testHighestColumn() throws Exception {
		Board board = new Board();
		Game gl = new Game(board);
		
		board.print();
		
		board.boardState[0][0] = new Ball(Color.GREEN);
		board.boardState[0][1] = new Ball(Color.YELLOW);
		board.boardState[0][2] = new Ball(Color.GREEN);
		
		board.boardState[1][0] = new Ball(Color.GREEN);
		board.boardState[1][1] = new Ball(Color.GREEN);
		board.boardState[1][2] = new Ball(Color.YELLOW);
		board.boardState[1][3] = new Ball(Color.YELLOW);
		
		
		assertEquals(4, gl.getHighestColumn());

	}
	
	@Test
	public void testRotate() throws Exception {
		Board board = new Board();
		Game game = new Game(board);
		
		game.pair.location = 0;
		game.pair.ball1 = new Ball(Color.GREEN);
		game.pair.ball2 = new Ball(Color.YELLOW);
		
		game.pair.rotate();
		game.drop();
		
		assertEquals(board.boardState[0][0].color, Color.YELLOW);
		assertEquals(board.boardState[0][1].color, Color.GREEN);
	}

}
