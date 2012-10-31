package GUI;

import java.awt.Graphics;
import java.awt.geom.Ellipse2D;

import gameState.Board.Color;
import java.awt.color.*;
import java.lang.reflect.Field;

import javax.swing.JPanel;

public class Ball extends JPanel {

	/**
	 * Create the panel.
	 */
	
	Color color;
	int x;
	int y;
	public static int radius = 70;
	
	public Ball(Color color, int x , int y) {
		System.out.println("test");
		this.color = color;
		this.x = x;
		this.y = y;
		repaint();
	}
	
	protected void paintComponent(Graphics g) { 
		System.out.println("test2");
		super.paintComponent(g);
		java.awt.Color c = getColor(color.name());
		g.setColor(c);
		g.fillOval(x, y, radius, radius);
	}
	
	private java.awt.Color getColor(String colorName) {
	    try {
	        // Find the field and value of colorName
	        Field field = Class.forName("java.awt.Color").getField(colorName);
	        return (java.awt.Color)field.get(null);
	    } catch (Exception e) {
	        return null;
	    }
	}

}
