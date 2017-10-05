package main;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class ImagePanel extends JPanel {

	private static final long serialVersionUID = 2870389239092684937L;
	private BufferedImage image;
	final BufferedImage original;
	private String path, format;
	private boolean showOriginal;
	private Dimension imageDimension;

	public ImagePanel(String file) {
		BufferedImage aux = null;
		try {
			aux = ImageIO.read(new File(file));
			setImage(ImageIO.read(new File(file)));
		} catch (IOException e) { e.printStackTrace(); }
		this.original = ImageTools.deepCopy(aux);
		
		setPath(file);
		setFormat(file.substring(file.indexOf('.')+1,file.length()).toLowerCase());

		setShowOriginal(false);
		setBorder(BorderFactory.createEmptyBorder());
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		imageDimension = ImageTools.getScaledDimension(	new Dimension(image.getWidth(), image.getHeight()),
														new Dimension(getWidth(), getHeight()));
		if (isShowingOriginal())
			g.drawImage(original, 0, 0, imageDimension.width, imageDimension.height, this);
		else
			g.drawImage(image, 0, 0, imageDimension.width, imageDimension.height, this);
	}
	
	public boolean isOnImage(int x, int y) {
		if(x < 0 || x >= imageDimension.width || y < 0 || y >= imageDimension.height)
			return false;
		return true;
	}

	/** @return the image */
	public BufferedImage getImage() {
		if(isShowingOriginal())
			return original;
		return image;
	}

	/* @param image the image to set */
	private void setImage(BufferedImage image) {
		this.image = image;
	}

	/* @param image the original image to set 
	private void setOriginal(BufferedImage original) {
		this.original = original;
	}*/

	/** @return the path */
	public String getPath() {
		return path;
	}

	/**
	 * @param path
	 * the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/** @return the showOriginal */
	public boolean isShowingOriginal() {
		return showOriginal;
	}
	
	public void switchOriginal() {
		setShowOriginal(!isShowingOriginal());
	}

	/**
	 * @param showOriginal
	 * Show original image?
	 */
	public void setShowOriginal(boolean showOriginal) {
		this.showOriginal = showOriginal;
	}

	/** @return the format */
	public String getFormat() {
		return format;
	}

	/** @param format the format to set */
	public void setFormat(String format) {
		this.format = format;
	}
	
	public String getResolution() {
		return getImage().getWidth() + " x " + getImage().getHeight();
	}
	
	/* @return Gray levels range */
	public String getRange() {
		return getImage().getWidth() + " x " + getImage().getHeight();
	}
	
	/*  */
	public double getScaleFactor() {
		return (double) ((double)getImage().getWidth() / (double)imageDimension.getWidth());
	}

}
