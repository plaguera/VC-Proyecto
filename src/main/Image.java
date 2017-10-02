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

	private BufferedImage image;
	private String path;

	public Image(String file) {
		setLayout(new GridLayout(1, 2));
		setPath(file);
		try {
			setImage(ImageIO.read(new File(file)));
		} catch (IOException ex) {
			// handle exception...
		}
	}
	
	@Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Dimension dim = Frame.getScaledDimension(new Dimension(image.getWidth(), image.getHeight()), new Dimension(getWidth(), getHeight()));
        int width = dim.width;
        int height = dim.height;
        g.drawImage(image, 0, 0, width, height, this); // see javadoc for more info on the parameters            
    }

	/** @return the image */
	public BufferedImage getImage() {
		return image;
	}

	/* @param image the image to set */
	public void setImage(BufferedImage image) {
		this.image = image;
	}

	/** @return the path */
	public String getPath() {
		return path;
	}

	/** @param path
	 *            the path to set */
	public void setPath(String path) {
		this.path = path;
	}

}
