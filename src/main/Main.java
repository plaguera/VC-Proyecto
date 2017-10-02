/**
 * 
 */
package main;

import java.awt.EventQueue;

/**
 * @author Pedro Miguel Lagüera Cabrera
 * Sep 25, 2017
 * Main.java
 */
public class Main {

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

}
