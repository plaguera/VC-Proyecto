package main;

import java.awt.GridLayout;
import javax.swing.JPanel;

public class ImagePanel extends JPanel{

    private Image original;

    public ImagePanel(String file) {
    	setLayout(new GridLayout(1, 2));
		original = new Image(file);
		add(original);
    }

}
