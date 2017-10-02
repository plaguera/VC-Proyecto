package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FileDialog;
import java.awt.FlowLayout;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;

import java.awt.GridLayout;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.awt.Label;

public class Frame extends JFrame {

	private static final int WINDOW_WIDTH = 1000;
	private static final int WINDOW_HEIGHT = 680;

	private JPanel contentPane;
	private JTabbedPane tabbedPane;
	private List<BufferedImage> images;
	private List<Image> imagePanels;

	private int imageIndex;
	JMenuItem mntmOpen, mntmSave, mntmCloseTab;
	private Label resolutionLabel, xCoordLabel, yCoordLabel;
	private JPanel coordLabelsPanel;
	
	private JButton btnGrayScale, btnShowOriginal, btnProperties;
	private JDialog dialogProperties;

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
		mntmOpen = new JMenuItem("Open...");
		mntmOpen.setAccelerator(KeyStroke.getKeyStroke(
		        KeyEvent.VK_O, ActionEvent.CTRL_MASK));
		mntmSave = new JMenuItem("Save...");
		mntmSave.setAccelerator(KeyStroke.getKeyStroke(
		        KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		mntmCloseTab = new JMenuItem("Close Tab");
		mntmCloseTab.setAccelerator(KeyStroke.getKeyStroke(
		        KeyEvent.VK_W, ActionEvent.CTRL_MASK));

		mnFile.add(mntmOpen);
		mnFile.add(mntmSave);
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

		JPanel buttonBar = new JPanel(new FlowLayout());
		btnGrayScale = new JButton();
		btnGrayScale.setPreferredSize(new Dimension(48, 48));
		btnGrayScale.setHorizontalTextPosition(SwingConstants.CENTER);
		btnShowOriginal = new JButton();
		btnShowOriginal.setPreferredSize(new Dimension(48, 48));
		btnShowOriginal.setHorizontalTextPosition(SwingConstants.CENTER);
		btnProperties = new JButton();
		btnProperties.setPreferredSize(new Dimension(48, 48));
		btnProperties.setHorizontalTextPosition(SwingConstants.CENTER);
		
		try {
			btnGrayScale.setIcon(new ImageIcon("grayscale.png"));
			btnShowOriginal.setIcon(new ImageIcon("original.png"));
			btnProperties.setIcon(new ImageIcon("properties.png"));
		  } catch (Exception ex) {
		    System.out.println(ex);
		  }
		System.out.println("Working Directory = " +
	              System.getProperty("user.dir"));
		buttonBar.add(btnGrayScale);
		buttonBar.add(btnShowOriginal);
		buttonBar.add(btnProperties);
		buttonBar.add(new JButton("4"));
		buttonBar.add(new JButton("5"));
		buttonBar.add(new JButton("6"));
		buttonBar.add(new JButton("7"));
		buttonBar.add(new JButton("8"));
		buttonBar.add(new JButton("9"));
		buttonBar.add(new JButton("10"));
		
		contentPane.add(buttonBar, BorderLayout.NORTH);
		
		images = new ArrayList<BufferedImage>();
		imagePanels = new ArrayList<Image>();
		imageIndex = -1;
		setUpListeners();

	}

	private void setUpListeners() {
		FileDialog fcLoad = new FileDialog(this, "Open Image...", FileDialog.LOAD);
		mntmOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fcLoad.setVisible(true);
				String fn = fcLoad.getDirectory() + fcLoad.getFile();
				if (fn == null)
					System.out.println("Cancelled Open");
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
					imagePanels.add(newImage);
					tabbedPane.addTab(fcLoad.getFile(), newImage);
					tabbedPane.setSelectedIndex(imageIndex);
					tabbedPane.repaint();
				}
			}
		});
		FileDialog fcSave = new FileDialog(this, "Save Image...", FileDialog.SAVE);
		mntmSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fcSave.setVisible(true);
				String fn = fcSave.getDirectory() + fcSave.getFile();
				System.out.println(fcSave.getDirectory());
				if (fn == null)
					System.out.println("Cancelled Save");
				else {
					try {
					    // retrieve image
					    BufferedImage image = images.get(imageIndex);
					    File outputfile = new File(fcSave.getFile());
					    ImageIO.write(image, "jpg", outputfile);
					    System.out.println("Saved as '" + fn + "'");
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		mntmCloseTab.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (imageIndex > -1) {
					images.remove(imageIndex);
					imagePanels.remove(imageIndex);
					tabbedPane.remove(imageIndex--);
				}
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
				if(imageIndex < 0)
					return;
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
		
		btnShowOriginal.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(imageIndex < 0)
					return;
				if(imagePanels.get(imageIndex).isShowOriginal())
					imagePanels.get(imageIndex).setShowOriginal(false);
				else
					imagePanels.get(imageIndex).setShowOriginal(true);
				imagePanels.get(imageIndex).repaint();
			}
		});
		
		dialogProperties = new JDialog(this, true);
		btnProperties.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				Object[][] data = {
					    {"Format: ", "0"},
					    {"Histogram: ", "Smith"},
					    {"Resolution: ", "Smith"},
					    {"Contrast Ratio: ", "Smith"},
					    {"Brightness and Contrast: ", "Smith"},
					    {"Entropy: ", "Smith"},
					    {"Position and Gray Levels: ", "Smith"}
					};
				String[] columnNames = {"Property", "Value"};
				JTable table = new JTable(data, columnNames);
				dialogProperties.add(table);
				dialogProperties.pack();
				dialogProperties.setVisible(true); // blocks until dialog is closed
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
