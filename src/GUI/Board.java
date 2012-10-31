package GUI;

import gamePlay.Game;
import gameState.InputPair.Orientation;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.lang.reflect.Field;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Board extends JPanel {

	/**
	 * Create the panel.
	 */
	gameState.Board board;
	Game game;
	JLabel scoreLabel = new JLabel("Score: ");
	JTextField score = new JTextField(0);
	Point root;
	int dimension = 555;
	
	public Board(gameState.Board gs, Game game)
	{
		board = gs;
		root = new Point(350, 120);	
		this.game = game;
	}
	
	public void paint(Graphics g) { 
		Graphics2D g2 = (Graphics2D) g;
		g2.setFont(new Font("TimesRoman", Font.PLAIN, 30));
		g2.drawString("Score: ", 20, 125);
		g2.drawString(String.valueOf(game.score), 200, 125);
		GradientPaint gp = new GradientPaint(root.x, root.y, Color.decode("#539DC2") , 
				dimension + 150, root.y, Color.decode("#5EDA9E"));
		g2.setPaint(gp);
		g2.fillRoundRect(root.x, root.y, dimension + 1, dimension, 50, 50);
		g2.setColor(Color.BLACK);
		g2.drawRoundRect(root.x, root.y, dimension + 1, dimension, 50, 50);
		int diameter = 78;
		
		int inputLocation = game.pair.location;
		if (game.pair.orietation == Orientation.VERTICAL)
		{
			g2.setColor(getColor(game.pair.ball2.color.name()));
			g2.fillOval(root.x + 2 + inputLocation*(diameter+1), root.y - diameter, diameter, diameter);
			g2.setColor(getColor(game.pair.ball1.color.name()));
			g2.fillOval(root.x + 2 + inputLocation*(diameter+1), root.y - 2*diameter, diameter, diameter);
			
			g2.setColor(Color.BLACK);
			g2.drawOval(root.x + 2 + inputLocation*(diameter+1), root.y - diameter, diameter, diameter);
			g2.drawOval(root.x + 2 + inputLocation*(diameter+1), root.y - 2*diameter, diameter, diameter);
		}
		else
		{	
			g2.setColor(getColor(game.pair.ball2.color.name()));
			g2.fillOval(root.x + 2 + (inputLocation+1)*(diameter+1), root.y - diameter, diameter, diameter);
			g2.setColor(getColor(game.pair.ball1.color.name()));
			g2.fillOval(root.x + 2 + inputLocation*(diameter+1), root.y - diameter, diameter, diameter);
			
			g2.setColor(Color.BLACK);
			g2.drawOval(root.x + 2 + (inputLocation+1)*(diameter+1), root.y - diameter, diameter, diameter);
			g2.drawOval(root.x + 2 + inputLocation*(diameter+1), root.y - diameter, diameter, diameter);
		}
		
		for (int rowIndex = 0; rowIndex < 7; rowIndex++)
		{
			for (int colIndex = 0; colIndex < 7; colIndex++)
			{
				if (board.boardState[colIndex][rowIndex] != null)
				{	
					g2.setColor(getColor(board.boardState[colIndex][rowIndex].color.name()));
					g2.fillOval(root.x + 2 + (colIndex)*(diameter+1), root.y + (6-rowIndex)*(diameter+1), diameter, diameter);
					
					g2.setColor(Color.BLACK);
					g2.drawOval(root.x + 2 + (colIndex)*(diameter+1), root.y + (6-rowIndex)*(diameter+1), diameter, diameter);
				}
			}
		}
	}
	
	private java.awt.Color getColor(String colorName) {
	    try {
	    	if (colorName.equals("ORANGE"))
	    	{
	    		return Color.decode("#ff8c00");
	    	}
	    	
	    	if (colorName.equals("PINK"))
	    	{
	    		return Color.decode("#ff69b4");
	    	}
	    	
	    	if (colorName.equals("PURPLE"))
	    	{
	    		return Color.decode("#800080");
	    	}
	    	
	    	if (colorName.equals("WHITE"))
	    	{
	    		return Color.decode("#CBCAB6");
	    	}
	    	
	        Field field = Class.forName("java.awt.Color").getField(colorName);
	        return (java.awt.Color)field.get(null);
	        
	    } catch (Exception e) {
	        return null;
	    }
	}

}
