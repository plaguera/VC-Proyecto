package main;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class Image extends JPanel {

	private BufferedImage image, original;
	private String path;
	private boolean showOriginal;

	public Image(String file) {
		setPath(file);
		try {
			setImage(ImageIO.read(new File(file)));
			setOriginal(ImageIO.read(new File(file)));
			setShowOriginal(false);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Dimension dim = Frame.getScaledDimension(new Dimension(image.getWidth(), image.getHeight()),
				new Dimension(getWidth(), getHeight()));
		if (isShowOriginal())
			g.drawImage(original, 0, 0, dim.width, dim.height, this); // see javadoc for more info on the parameters
		else
			g.drawImage(image, 0, 0, dim.width, dim.height, this); // see javadoc for more info on the parameters
	}

	/** @return the image */
	public BufferedImage getImage() {
		return image;
	}

	/** @return the image */
	public BufferedImage getOriginal() {
		return original;
	}

	/* @param image the image to set */
	private void setImage(BufferedImage image) {
		this.image = image;
	}

	/* @param image the original image to set */
	public void setOriginal(BufferedImage original) {
		this.original = original;
	}

	/** @return the path */
	public String getPath() {
		return path;
	}

	/**
	 * @param path
	 *            the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * @return the showOriginal
	 */
	public boolean isShowOriginal() {
		return showOriginal;
	}

	/**
	 * @param showOriginal
	 *            the showOriginal to set
	 */
	public void setShowOriginal(boolean showOriginal) {
		this.showOriginal = showOriginal;
	}

}
