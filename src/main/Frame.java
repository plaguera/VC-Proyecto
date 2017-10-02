package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FileDialog;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;

import java.awt.GridLayout;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.awt.Label;

public class Frame extends JFrame {

	private static final int WINDOW_WIDTH = 1000;
	private static final int WINDOW_HEIGHT = 680;

	private JPanel contentPane;
	private JTabbedPane tabbedPane;
	private List<BufferedImage> images;

	private int imageIndex;
	JMenuItem mntmOpen, mntmCloseTab;
	private Label resolutionLabel, xCoordLabel, yCoordLabel;
	private JPanel coordLabelsPanel;
	
	private JButton btnGrayScale;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Frame frame = new Frame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Frame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, WINDOW_WIDTH, WINDOW_HEIGHT);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		mntmOpen = new JMenuItem("Open");
		mntmCloseTab = new JMenuItem("Close Tab");

		mnFile.add(mntmOpen);
		mnFile.add(mntmCloseTab);

		JMenu mnEdit = new JMenu("Edit");
		menuBar.add(mnEdit);

		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JPanel infoPanel = new JPanel();
		contentPane.add(infoPanel, BorderLayout.SOUTH);
		infoPanel.setLayout(new GridLayout(0, 4, 0, 0));

		coordLabelsPanel = new JPanel();
		infoPanel.add(coordLabelsPanel);
		coordLabelsPanel.setLayout(new GridLayout(1, 2, 0, 0));

		xCoordLabel = new Label();
		coordLabelsPanel.add(xCoordLabel);

		yCoordLabel = new Label();
		coordLabelsPanel.add(yCoordLabel);

		resolutionLabel = new Label();
		infoPanel.add(resolutionLabel);

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setAlignmentX(SwingConstants.CENTER);
		contentPane.add(tabbedPane, BorderLayout.CENTER);

		JPanel buttonBar = new JPanel(new GridLayout(1, 5));
		btnGrayScale = new JButton("GrayScale");
		buttonBar.add(btnGrayScale);
		buttonBar.add(new JButton("2"));
		buttonBar.add(new JButton("3"));
		buttonBar.add(new JButton("4"));
		buttonBar.add(new JButton("5"));
		
		contentPane.add(buttonBar, BorderLayout.NORTH);
		
		images = new ArrayList<BufferedImage>();
		imageIndex = -1;
		setUpListeners();

	}

	private void setUpListeners() {
		FileDialog fc = new FileDialog(this, "Choose a file", FileDialog.LOAD);
		mntmOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fc.setVisible(true);
				String fn = fc.getFile();
				if (fn == null)
					System.out.println("You cancelled the choice");
				else {
					System.out.println("You chose '" + fn + "'");
					Image newImage = new Image(fn);
					images.add(newImage.getImage());
					imageIndex++;
					newImage.setBorder(BorderFactory.createEmptyBorder());
					newImage.addMouseMotionListener(new MouseMotionAdapter() {
						@Override
						public void mouseMoved(MouseEvent e) {
							xCoordLabel.setText("X: " + e.getX());
							yCoordLabel.setText("Y: " + e.getY());
							repaint();
						}
					});
					tabbedPane.addTab(fn, newImage);
					tabbedPane.setSelectedIndex(imageIndex);
					tabbedPane.repaint();
				}
			}
		});
		mntmCloseTab.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (imageIndex > -1)
					tabbedPane.remove(imageIndex--);
			}
		});
		tabbedPane.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if(imageIndex < 0)
					return;
				int width = images.get(imageIndex).getWidth();
				int height = images.get(imageIndex).getHeight();
				String text = width + " x " + height + "pixels";
				resolutionLabel.setText(text);
				System.out.println("Tab: " + imageIndex);
			}
		});
		btnGrayScale.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				BufferedImage aux = images.get(imageIndex);
				for(int i = 0; i < aux.getWidth(); i++)
					for(int j = 0; j < aux.getHeight(); j++) {
						Color color = new Color(aux.getRGB(i, j));
						int red = (int) (color.getRed() * 0.299);
						int green =(int) (color.getGreen() * 0.587);
						int blue = (int) (color.getBlue() *0.114);
						Color newColor = new Color(	red+green+blue,
					               					red+green+blue,
					               					red+green+blue);
						//System.out.println(r + " - " + g + " - " + b);
						images.get(imageIndex).setRGB(i, j, newColor.getRGB());
					}
				tabbedPane.repaint();
			}
		});
	}
	
	public static Dimension getScaledDimension(Dimension imgSize, Dimension boundary) {

	    int original_width = imgSize.width;
	    int original_height = imgSize.height;
	    int bound_width = boundary.width;
	    int bound_height = boundary.height;
	    int new_width = original_width;
	    int new_height = original_height;

	    // first check if we need to scale width
	    if (original_width > bound_width) {
	        //scale width to fit
	        new_width = bound_width;
	        //scale height to maintain aspect ratio
	        new_height = (new_width * original_height) / original_width;
	    }

	    // then check if we need to scale even with the new height
	    if (new_height > bound_height) {
	        //scale height to fit instead
	        new_height = bound_height;
	        //scale width to maintain aspect ratio
	        new_width = (new_height * original_width) / original_height;
	    }

	    return new Dimension(new_width, new_height);
	}

}
