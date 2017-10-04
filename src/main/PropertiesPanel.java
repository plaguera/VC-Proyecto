package main;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class PropertiesPanel extends JPanel {
	
	private JLabel categFormat, categResolution, categRange;

	public PropertiesPanel(ImagePanel image) {
		setLayout(new GridLayout(3, 1));
		
		categFormat = new JLabel(image.getFormat());
		categFormat.setBorder(BorderFactory.createTitledBorder("Format"));
		categFormat.setHorizontalAlignment(SwingConstants.CENTER);
		categFormat.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
		add(categFormat);
		
		categResolution = new JLabel(image.getResolution());
		categResolution.setBorder(BorderFactory.createTitledBorder("Resolution"));
		categResolution.setHorizontalAlignment(SwingConstants.CENTER);
		categResolution.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
		add(categResolution);
		
		Map.Entry<Integer, Integer> range = ImageTools.getGrayRange(image.getImage());
		categRange = new JLabel("[" + range.getKey() + " - " + range.getValue() + "]");
		categRange.setBorder(BorderFactory.createTitledBorder("Value Range"));
		categRange.setHorizontalAlignment(SwingConstants.CENTER);
		categRange.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
		add(categRange);
		
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
	}

}
