package main;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

public class CurrentColorPanel extends JPanel {
	
	private Color color;
	
	public CurrentColorPanel() {
		this.color = Color.white;
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		int side = Math.min(getWidth(), getHeight()) - 2;
		int x = getWidth()/2 - side/2;
		int y = getHeight()/2 - side/2;
		g.setColor(getColor());
		g.fillRect(x, y, side, side);
	}

	/**
	 * @return the color
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * @param color the color to set
	 */
	public void setColor(int color) {
		this.color = new Color(color);
	}
	
	

}
