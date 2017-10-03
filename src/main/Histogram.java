package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;

public class Histogram extends JPanel {

	protected static final int MIN_BAR_WIDTH = 4;
	private List<List<Integer>> rgbValues;

	public Histogram(BufferedImage image) {
		this.rgbValues = Histogram.getRGBValues(image);
		int width = (rgbValues.get(0).size() * MIN_BAR_WIDTH) + 11;
		Dimension minSize = new Dimension(width, 128);
		Dimension prefSize = new Dimension(width, 256);
		setMinimumSize(minSize);
		setPreferredSize(prefSize);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		drawColorHistogram(rgbValues.get(0), g, 0);
		drawColorHistogram(rgbValues.get(1), g, 1);
		drawColorHistogram(rgbValues.get(2), g, 2);
	}

	private void drawColorHistogram(List<Integer> color, Graphics g, int rgb) {
		if (color != null) {
			int xOffset = 5;
			int yOffset = 5;
			int width = getWidth() - 1 - (xOffset * 2);
			int height = getHeight() - 1 - (yOffset * 2);
			Graphics2D g2d = (Graphics2D) g.create();
			g2d.setColor(Color.DARK_GRAY);
			g2d.drawRect(xOffset, yOffset, width, height);
			int barWidth = Math.max(MIN_BAR_WIDTH, (int) Math.floor((float) width / (float) color.size()));
			System.out.println("width = " + width + "; size = " + color.size() + "; barWidth = " + barWidth);
			int maxValue = 0;
			for (Integer value : color) {
				maxValue = Math.max(maxValue, value);
			}
			int xPos = xOffset;
			for (int i = 0; i < color.size(); i++) {
				int value = color.get(i);
				int barHeight = Math.round(((float) value / (float) maxValue) * height);
				g2d.setColor(new Color(i, i, i));
				int yPos = height + yOffset - barHeight;
				// Rectangle bar = new Rectangle(xPos, yPos, barWidth, barHeight);
				Rectangle2D bar = new Rectangle2D.Float(xPos, yPos, barWidth, barHeight);
				g2d.fill(bar);
				switch (rgb) {
				case 0:
					g2d.setColor(Color.RED);
					break;
				case 1:
					g2d.setColor(Color.GREEN);
					break;
				case 2:
					g2d.setColor(Color.BLUE);
					break;
				default:
					break;
				}

				g2d.draw(bar);
				xPos += barWidth;
			}
			g2d.dispose();
		}
	}

	public static List<List<Integer>> getRGBValues(BufferedImage image) {
		int width = image.getWidth();
		int height = image.getHeight();
		int[][] data = new int[width][height];

		List<List<Integer>> values = new ArrayList<List<Integer>>();
		for (int i = 0; i < 3; i++) {
			values.add(new ArrayList<Integer>());
			for (int j = 0; j < 256; j++) {
				values.get(i).add(0);
			}
		}

		for (int c = 0; c < width; c++) {
			for (int r = 0; r < height; r++) {
				Color color = new Color(image.getRGB(c, r));
				values.get(0).set(color.getRed(), values.get(0).get(color.getRed()) + 1);
				values.get(1).set(color.getGreen(), values.get(1).get(color.getGreen()) + 1);
				values.get(2).set(color.getBlue(), values.get(2).get(color.getBlue()) + 1);
				System.out.println(color.toString());
			}
		}
		return values;
	}
}
