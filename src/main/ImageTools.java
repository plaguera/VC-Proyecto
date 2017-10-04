package main;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.util.AbstractMap;
import java.util.Map;
import java.util.Map.Entry;

public class ImageTools {

	public static void rgbToGrayscale(BufferedImage image) {
		for (int i = 0; i < image.getWidth(); i++)
			for (int j = 0; j < image.getHeight(); j++) {
				Color color = new Color(image.getRGB(i, j));
				int red = (int) (color.getRed() * 0.299);
				int green = (int) (color.getGreen() * 0.587);
				int blue = (int) (color.getBlue() * 0.114);
				Color newColor = new Color(red + green + blue, red + green + blue, red + green + blue);
				image.setRGB(i, j, newColor.getRGB());
			}
	}

	public static BufferedImage rgbToGrayscaleCopy(BufferedImage original) {
		BufferedImage image = deepCopy(original);
		for (int i = 0; i < image.getWidth(); i++)
			for (int j = 0; j < image.getHeight(); j++) {
				Color color = new Color(image.getRGB(i, j));
				int red = (int) (color.getRed() * 0.299);
				int green = (int) (color.getGreen() * 0.587);
				int blue = (int) (color.getBlue() * 0.114);
				Color newColor = new Color(red + green + blue, red + green + blue, red + green + blue);
				image.setRGB(i, j, newColor.getRGB());
			}
		return image;
	}

	public static Map.Entry<Integer, Integer> getGrayRange(BufferedImage bi) {
		int max = Integer.MIN_VALUE;
		int min = Integer.MAX_VALUE;
		BufferedImage grayscale = ImageTools.rgbToGrayscaleCopy(bi);

		for (int i = 0; i < grayscale.getWidth(); i++)
			for (int j = 0; j < grayscale.getHeight(); j++) {
				Color color = new Color(grayscale.getRGB(i, j));
				if (color.getRed() > max)
					max = color.getRed();
				if (color.getRed() < min)
					min = color.getRed();
			}
		return new AbstractMap.SimpleEntry<Integer, Integer>(min, max);
	}

	public static BufferedImage deepCopy(BufferedImage bi) {
		ColorModel cm = bi.getColorModel();
		boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		WritableRaster raster = bi.copyData(bi.getRaster().createCompatibleWritableRaster());
		return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
	}

}
