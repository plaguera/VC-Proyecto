package main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FileDialog;
import java.awt.FlowLayout;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
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

	private static final long serialVersionUID = 1415573919342366965L;
	private static final int WINDOW_WIDTH = 1000;
	private static final int WINDOW_HEIGHT = 680;

	private JPanel contentPane;
	private JTabbedPane tabbedPane;
	//private List<BufferedImage> images;
	private List<ImagePanel> imagePanels;

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
		setTitle("Image Editor");
		
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

		//images = new ArrayList<BufferedImage>();
		imagePanels = new ArrayList<ImagePanel>();
		imageIndex = -1;
		setUpListeners();

	}

	private void setUpListeners() {
		/**
		 * Open File dialog listener
		 */
		Frame frame = this;
		mntmOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FileDialog fcLoad = new FileDialog(frame, "Open Image...", FileDialog.LOAD);
				fcLoad.setVisible(true);
				if (fcLoad.getFile() == null) {
					System.out.println("Cancelled Open !!");
					return;
				}
				else {
					String directory = fcLoad.getDirectory();
					String fileName = fcLoad.getFile();
					System.out.println("Opening '" + directory + fileName + "'");
					
					ImagePanel newImagePanel = new ImagePanel(directory + fileName);
					
					newImagePanel.addMouseMotionListener(new MouseMotionAdapter() {
						@Override
						public void mouseMoved(MouseEvent e) {
							if(!newImagePanel.isOnImage(e.getX(), e.getY()))
								return;
							int x = (int)(e.getX() * newImagePanel.getScaleFactor());
							int y = (int)(e.getY() * newImagePanel.getScaleFactor());
							xCoordLabel.setText("X: " + x);
							yCoordLabel.setText("Y: " + y);
							currentColorPanel.setColor(newImagePanel.getImage().getRGB(x, y));
							xCoordLabel.repaint();
							yCoordLabel.repaint();
							currentColorPanel.repaint();
						}
					});
					
					imagePanels.add(newImagePanel);
					imageIndex++;
					tabbedPane.addTab(fcLoad.getFile(), newImagePanel);
					tabbedPane.setSelectedIndex(imageIndex);
					tabbedPane.repaint();
				}
			}
		});
		
		/**
		 * Save File dialog listener
		 */
		mntmSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FileDialog fcSave = new FileDialog(frame, "Save Image...", FileDialog.SAVE);
				fcSave.setVisible(true);
				if (fcSave.getFile() == null) {
					System.out.println("Cancelled Save !!");
					return;
				}
				else {
					String directory = fcSave.getDirectory();
					String fileName = fcSave.getFile();
					try {
						ImageIO.write(	imagePanels.get(imageIndex).getImage(),
										imagePanels.get(imageIndex).getFormat(),
										new File(directory + fileName));
						System.out.println("Saved as '" + fileName + "' in " + directory);
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
					imagePanels.remove(imageIndex);
					tabbedPane.remove(imageIndex--);
				}
			}
		});
		
		/** Current Tab Change Listener */
		tabbedPane.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (imageIndex < 0)
					return;
				resolutionLabel.setText(imagePanels.get(imageIndex).getResolution() + " pixels");
				imageIndex = tabbedPane.getSelectedIndex();
				//System.out.println("Tab: " + imageIndex);
			}
		});
		
		/** Grayscale Button Listener */
		btnGrayScale.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (imageIndex < 0)
					return;
				ImageTools.rgbToGrayscale(imagePanels.get(imageIndex).getImage());
				tabbedPane.repaint();
			}
		});

		/** Show Original Image Button Listener */
		btnShowOriginal.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (imageIndex < 0)
					return;
				imagePanels.get(imageIndex).switchOriginal();
				imagePanels.get(imageIndex).repaint();
			}
		});

		/** Show Properties Button Listener */
		btnProperties.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

				if (imageIndex < 0)
					return;
				JFrame frame = new JFrame("Properties");
				frame.add(new PropertiesPanel(imagePanels.get(imageIndex)));
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
				frame.add(new JScrollPane(new Histogram(imagePanels.get(imageIndex).getImage())));
				frame.pack();
				frame.setLocationRelativeTo(null);
				frame.setVisible(true);
			}
		});

	}

}
