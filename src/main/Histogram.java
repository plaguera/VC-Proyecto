package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

public class Histogram extends JPanel {

	private static final long serialVersionUID = 729670405199776986L;
	protected static final int MIN_BAR_WIDTH = 4;
	private int[][] rgbValues;
	private JPopupMenu popup;
	private JCheckBoxMenuItem red, green, blue;
	private boolean showRed, showGreen, showBlue;

	public Histogram(BufferedImage image) {
		this.rgbValues = Histogram.getRGBValues(image);
		/*
		 * for (int i = 0; i < 3; i++) { for (int j = 0; j < 256; j++) {
		 * System.out.print(rgbValues[i][j] + " "); } System.out.println(); }
		 */
		int width = (256 * MIN_BAR_WIDTH) + 11;
		Dimension minSize = new Dimension(width, 128);
		Dimension prefSize = new Dimension(width, 256);
		setMinimumSize(minSize);
		setPreferredSize(prefSize);
		if(isGrayscale(rgbValues))
			return;
		showRed = true;
		showGreen = true;
		showBlue = true;
		
		popup = new JPopupMenu("Edit");
		red = new JCheckBoxMenuItem("Red", showRed);
		green = new JCheckBoxMenuItem("Green", showGreen);
		blue = new JCheckBoxMenuItem("Blue", showBlue);
		
		popup.add(red);
		popup.add(green);
		popup.add(blue);
		
		setListeners();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		drawBorder(g);
		if (isGrayscale(rgbValues))
			drawColorHistogram(rgbValues[0], g, 3);
		else {
			if(showRed)
				drawColorHistogram(rgbValues[0], g, 0);
			if(showGreen)
				drawColorHistogram(rgbValues[1], g, 1);
			if(showBlue)
				drawColorHistogram(rgbValues[2], g, 2);
		}
	}
	
	private void drawBorder(Graphics g) {
		int xOffset = 5;
		int yOffset = 5;
		int width = getWidth() - 1 - (xOffset * 2);
		int height = getHeight() - 1 - (yOffset * 2);
		Graphics2D g2d = (Graphics2D) g.create();
		g2d.setColor(Color.DARK_GRAY);
		g2d.drawRect(xOffset, yOffset, width, height);
	}

	private void drawColorHistogram(int[] color, Graphics g, int rgb) {
		if (color != null) {
			int xOffset = 5;
			int yOffset = 5;
			int width = getWidth() - 1 - (xOffset * 2);
			int height = getHeight() - 1 - (yOffset * 2);
			Graphics2D g2d = (Graphics2D) g.create();
			int barWidth = Math.max(MIN_BAR_WIDTH, (int) Math.floor((float) width / (float) color.length));
			int maxValue = 0;
			for (Integer value : color) {
				maxValue = Math.max(maxValue, value);
			}
			int xPos = xOffset;
			for (int i = 0; i < color.length; i++) {
				int value = color[i];
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
				case 3:
					g2d.setColor(Color.DARK_GRAY);
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

	public static int[][] getRGBValues(BufferedImage image) {
		int width = image.getWidth();
		int height = image.getHeight();
		int[][] data = new int[3][256];

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 256; j++) {
				data[i][j] = 0;
			}
		}

		for (int c = 0; c < width; c++) {
			for (int r = 0; r < height; r++) {
				Color color = new Color(image.getRGB(c, r));
				data[0][color.getRed()]++;
				data[1][color.getGreen()]++;
				data[2][color.getBlue()]++;
			}
		}
		return data;
	}

	public static boolean isGrayscale(int[][] data) {
		int r = 0, g = 0, b = 0;
		while (r < 256 && g < 256 && b < 256) {
			if (data[0][r] != data[1][g] || data[1][g] != data[2][b] || data[0][r] != data[2][b])
				return false;
			r++;
			g++;
			b++;
		}
		return true;
	}
	
	private void setListeners() {
		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (SwingUtilities.isRightMouseButton(e)) {
					popup.show(e.getComponent(), e.getX(), e.getY());
				}
			}

		});
		red.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				showRed = red.getState();
				repaint();
			}
		});
		green.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				showGreen = green.getState();
				repaint();
			}
		});
		blue.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				showBlue = blue.getState();
				repaint();
			}
		});
	}
}
