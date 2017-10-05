package main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FileDialog;
import java.awt.FlowLayout;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;

import java.awt.GridLayout;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Frame extends JFrame {

	private static final int WINDOW_WIDTH = 1000;
	private static final int WINDOW_HEIGHT = 680;

	private JPanel contentPane;
	private JTabbedPane tabbedPane;
	private List<BufferedImage> images;
	private List<ImagePanel> imagePanels;
	private JPopupMenu popup;

	private int imageIndex;
	private JMenuItem mntmOpen, mntmSave, mntmCloseTab;
	private JLabel resolutionLabel, xCoordLabel, yCoordLabel;
	private JPanel coordLabelsPanel;

	private JButton btnGrayScale, btnShowOriginal, btnProperties, btnHistogram;
	private CurrentColorPanel currentColorPanel;

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
		mntmOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
		mntmSave = new JMenuItem("Save...");
		mntmSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		mntmCloseTab = new JMenuItem("Close Tab");
		mntmCloseTab.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, ActionEvent.CTRL_MASK));

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
		coordLabelsPanel.setLayout(new GridLayout(1, 3, 0, 0));

		xCoordLabel = new JLabel();
		xCoordLabel.setHorizontalAlignment(SwingConstants.CENTER);
		coordLabelsPanel.add(xCoordLabel);

		yCoordLabel = new JLabel();
		yCoordLabel.setHorizontalAlignment(SwingConstants.CENTER);
		coordLabelsPanel.add(yCoordLabel);
		
		currentColorPanel = new CurrentColorPanel();
		coordLabelsPanel.add(currentColorPanel);

		resolutionLabel = new JLabel();
		resolutionLabel.setHorizontalAlignment(SwingConstants.CENTER);
		infoPanel.add(resolutionLabel);

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setAlignmentX(SwingConstants.CENTER);
		contentPane.add(tabbedPane, BorderLayout.CENTER);

		JPanel buttonBar = new JPanel(new FlowLayout());
		btnGrayScale = new JButton();
		btnGrayScale.setPreferredSize(new Dimension(48, 48));
		btnGrayScale.setHorizontalTextPosition(SwingConstants.CENTER);
		btnGrayScale.setToolTipText("Grayscale");
		btnGrayScale.setFocusable(false);

		btnShowOriginal = new JButton();
		btnShowOriginal.setPreferredSize(new Dimension(48, 48));
		btnShowOriginal.setHorizontalTextPosition(SwingConstants.CENTER);
		btnShowOriginal.setToolTipText("Show Original Image");
		btnShowOriginal.setFocusable(false);

		btnProperties = new JButton();
		btnProperties.setPreferredSize(new Dimension(48, 48));
		btnProperties.setHorizontalTextPosition(SwingConstants.CENTER);
		btnProperties.setToolTipText("Properties");
		btnProperties.setFocusable(false);

		btnHistogram = new JButton();
		btnHistogram.setPreferredSize(new Dimension(48, 48));
		btnHistogram.setHorizontalTextPosition(SwingConstants.CENTER);
		btnHistogram.setToolTipText("Histogram");
		btnHistogram.setFocusable(false);

		try {
			btnGrayScale.setIcon(new ImageIcon("grayscale.png"));
			btnShowOriginal.setIcon(new ImageIcon("original.png"));
			btnProperties.setIcon(new ImageIcon("properties.png"));
			btnHistogram.setIcon(new ImageIcon("histogram.png"));
		} catch (Exception ex) {
			System.out.println(ex);
		}
		System.out.println("Working Directory = " + System.getProperty("user.dir"));
		buttonBar.add(btnProperties);
		buttonBar.add(btnShowOriginal);
		buttonBar.add(btnHistogram);
		buttonBar.add(btnGrayScale);
		buttonBar.add(new JButton("5"));
		buttonBar.add(new JButton("6"));
		buttonBar.add(new JButton("7"));
		buttonBar.add(new JButton("8"));
		buttonBar.add(new JButton("9"));
		buttonBar.add(new JButton("10"));

		contentPane.add(buttonBar, BorderLayout.NORTH);

		images = new ArrayList<BufferedImage>();
		imagePanels = new ArrayList<ImagePanel>();
		imageIndex = -1;
		setUpListeners();

	}

	private void setUpListeners() {
		/**
		 * Open File dialog listener
		 */
		FileDialog fcLoad = new FileDialog(this, "Open Image...", FileDialog.LOAD);
		mntmOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fcLoad.setVisible(true);
				String fn = fcLoad.getDirectory() + fcLoad.getFile();
				if (fn == null)
					System.out.println("Cancelled Open");
				else {
					System.out.println("You chose '" + fn + "'");
					ImagePanel newImage = new ImagePanel(fn);
					images.add(newImage.getImage());
					imageIndex++;
					newImage.setBorder(BorderFactory.createEmptyBorder());
					newImage.addMouseMotionListener(new MouseMotionAdapter() {
						@Override
						public void mouseMoved(MouseEvent e) {
							if(!newImage.isOnImage(e.getX(), e.getY()))
								return;
							int x = (int)(e.getX() * newImage.getScaleFactor());
							int y = (int)(e.getY() * newImage.getScaleFactor());
							xCoordLabel.setText("X: " + x);
							yCoordLabel.setText("Y: " + y);
							currentColorPanel.setColor(newImage.getImage().getRGB(x, y));
							xCoordLabel.repaint();
							yCoordLabel.repaint();
							currentColorPanel.repaint();
						}
					});
					imagePanels.add(newImage);
					tabbedPane.addTab(fcLoad.getFile(), newImage);
					tabbedPane.setSelectedIndex(imageIndex);
					tabbedPane.repaint();
				}
			}
		});
		
		/**
		 * Save File dialog listener
		 */
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
		
		/**
		 * Close Tab listener
		 */
		mntmCloseTab.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (imageIndex > -1) {
					images.remove(imageIndex);
					imagePanels.remove(imageIndex);
					tabbedPane.remove(imageIndex--);
				}
			}
		});
		
		/**
		 * Current Tab Change Listener
		 */
		tabbedPane.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (imageIndex < 0)
					return;
				int width = images.get(imageIndex).getWidth();
				int height = images.get(imageIndex).getHeight();
				String text = width + " x " + height + "pixels";
				resolutionLabel.setText(text);
				imageIndex = tabbedPane.getSelectedIndex();
				System.out.println("Tab: " + imageIndex + " - " + imageIndex);
			}
		});
		
		/**
		 * Grayscale Button Listener
		 */
		btnGrayScale.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (imageIndex < 0)
					return;
				ImageTools.rgbToGrayscale(images.get(imageIndex));
				tabbedPane.repaint();
			}
		});

		/**
		 * Show Original Image Button Listener
		 */
		btnShowOriginal.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (imageIndex < 0)
					return;
				if (imagePanels.get(imageIndex).isShowOriginal())
					imagePanels.get(imageIndex).setShowOriginal(false);
				else
					imagePanels.get(imageIndex).setShowOriginal(true);
				imagePanels.get(imageIndex).repaint();
			}
		});

		/**
		 * Show Properties Button Listener
		 */
		btnProperties.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

				if (imageIndex < 0)
					return;
				JFrame frame = new JFrame("Properties");
				// frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				if (imagePanels.get(imageIndex).isShowOriginal())
					frame.add(new PropertiesPanel(imagePanels.get(imageIndex)));
				else
					frame.add(new PropertiesPanel(imagePanels.get(imageIndex)));
				// frame.pack();
				frame.setSize(150, 300);
				frame.setLocationRelativeTo(null);
				frame.setVisible(true);
			}
		});

		/**
		 * Open Histogram Button Listener
		 */
		btnHistogram.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (imageIndex < 0)
					return;
				JFrame frame = new JFrame("Histogram");
				frame.setLayout(new BorderLayout());
				
				if (imagePanels.get(imageIndex).isShowOriginal())
					frame.add(new JScrollPane(new Histogram(imagePanels.get(imageIndex).getOriginal())));
				else
					frame.add(new JScrollPane(new Histogram(images.get(imageIndex))));
				
				frame.pack();
				frame.setLocationRelativeTo(null);
				frame.setVisible(true);
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
			// scale width to fit
			new_width = bound_width;
			// scale height to maintain aspect ratio
			new_height = (new_width * original_height) / original_width;
		}

		// then check if we need to scale even with the new height
		if (new_height > bound_height) {
			// scale height to fit instead
			new_height = bound_height;
			// scale width to maintain aspect ratio
			new_width = (new_height * original_width) / original_height;
		}

		return new Dimension(new_width, new_height);
	}

}
