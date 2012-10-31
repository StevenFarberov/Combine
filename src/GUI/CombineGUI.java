package GUI;

import gamePlay.Game;
import gameState.Board.Color;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

import Agents.NaiveAgent;

public class CombineGUI extends JFrame {

	private JPanel contentPane;
	private CombineGUI self;

	/**
	 * Launch the application.
	 */
	gameState.Board board;
	Game game;
	
	public CombineGUI(gameState.Board board, Game game, boolean humanPlay) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	    setBounds(0,0,screenSize.width, screenSize.height);
		contentPane = new JPanel();
		contentPane.setBackground(java.awt.Color.LIGHT_GRAY);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		Board GUIBoard = new Board(board, game);
		contentPane.add(GUIBoard);
		self = this;
		this.board = board;	
		this.game = game;
		if (humanPlay)
		{
			GUIBoard.addKeyListener(new ArrowListener());
		}
		setContentPane(contentPane);
	}
	
	
	
	public void AIPlay(final NaiveAgent agent)
	{	
		ActionListener taskPerformer1 = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				try {
					agent.findBestSingleTurn();
					game.drop();
					repaint();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return;
				}

			}
		};

		ActionListener taskPerformer2 = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				try {
					game.combineAll();
					repaint();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return;
				}

			}
		};

		Timer t1 = new Timer(0, taskPerformer1);
		t1.setInitialDelay(500);
		t1.setRepeats(false);
		t1.start();
		
		Timer t2 = new Timer(0, taskPerformer2);
		t2.setInitialDelay(1000);
		t2.setRepeats(false);
		t2.start();
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					gameState.Board board = new gameState.Board();
					final Game game = new Game(board);
					final NaiveAgent agent = new NaiveAgent(game);
					final CombineGUI frame = new CombineGUI(board, game, true);
					//frame.addKeyListener(frame.new ArrowListener());
					frame.setVisible(true);
					ActionListener taskPerformer1 = new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							try {
								agent.findBestSingleTurn();
								frame.repaint();
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								return;
							}

						}
					};
					
					ActionListener taskPerformer2 = new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							try {
								game.drop();
								frame.repaint();
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								return;
							}

						}
					};

					ActionListener taskPerformer3 = new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							try {
								game.combineAll();
								frame.repaint();
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								return;
							}

						}
					};

					Timer t1 = new Timer(1500, taskPerformer1);
					t1.setInitialDelay(0);
					t1.start();
					
					Timer t2 = new Timer(1500, taskPerformer2);
					t2.setInitialDelay(500);
					t2.start();
					
					Timer t3 = new Timer(1500, taskPerformer3);
					t3.setInitialDelay(1000);
					t3.start();
					

				} catch (Exception e) {
					e.printStackTrace();
					return;
				}
			}
		});
	}
	
	private class ArrowListener implements KeyListener{

		@Override
		public void keyPressed(KeyEvent event) {
			// TODO Auto-generated method stub
			if (event.getKeyCode() == KeyEvent.VK_UP) {
				game.pair.rotate();
				self.repaint();	
			}
			
			if (event.getKeyCode() == KeyEvent.VK_RIGHT) {
				game.moveRight();
				self.repaint();
			}
			
			if (event.getKeyCode() == KeyEvent.VK_LEFT) {
				game.moveLeft();
				self.repaint();
			}
			
			if (event.getKeyCode() == KeyEvent.VK_DOWN) {
				try {
					game.drop();
					repaint();
					ActionListener taskPerformer = new ActionListener() {
						  public void actionPerformed(ActionEvent evt) {
							  try {
								game.combineAll();
								repaint();
							} catch (Exception e) {
								// TODO Auto-generated catch block
								System.out.println(e.getMessage());
								System.out.println("Score = " + game.score);
								setVisible(false);
								dispose();
								return;
							}
							  
						  }
						};
					
					Timer t = new Timer(0, taskPerformer);
					t.setInitialDelay(500);
					t.setRepeats(false);
					t.start();
				} catch (Exception e) {
					e.printStackTrace();
					//System.out.println(e.getMessage());
					setVisible(false);
					dispose();
				}
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}
		

	}
}
