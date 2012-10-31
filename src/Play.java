import Agents.NaiveAgent;
import gamePlay.Game;
import gameState.Board;


public class Play {
	
	private static int play()
	{
		Board board = new Board();
		Game gl = new Game(board);
		while(true)
		{
			try {
				gl.dropRandomly();
			} catch (Exception e) {
				return gl.score;
			}
		}
	}
	
	private static void playN(int n)
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
	
	public static void main(String [] args) throws Exception{
		int n = 1;
		int total = 0;
		for (int i = 0; i < n; i++)
		{
			Board board = new Board();
			Game game = new Game(board);
			NaiveAgent a = new NaiveAgent(game);
			int score = a.play();
			System.out.println("score " + i + " = " + score);
			total += score;
		}
		System.out.println("average score = " + total/n);
	}

}
