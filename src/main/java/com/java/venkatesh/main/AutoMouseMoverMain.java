package com.java.venkatesh.main;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.Robot;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;

public class AutoMouseMoverMain {

	private JFrame frame;
	private TrayIcon trayIcon;
	private SystemTray tray;
	private Thread t1;
	private static boolean xInProgess = true; // Stop condition for X
	private static boolean yInProgess = true; // Stop condition for Y

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AutoMouseMoverMain window = new AutoMouseMoverMain();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public AutoMouseMoverMain() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		tray = SystemTray.getSystemTray();
		URL url = AutoMouseMoverMain.class.getClassLoader().getResource("media/Duke256.png");
		System.out.println("URL  :" + url);
		Image image = Toolkit.getDefaultToolkit().getImage(url);
		PopupMenu popup = new PopupMenu();
		MenuItem defaultItem = new MenuItem("Exit");
		ActionListener exitListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				xInProgess = false;
				yInProgess = false;
				System.out.println("Exiting....");
				t1.stop();
				System.exit(0);

			}
		};
		defaultItem.addActionListener(exitListener);
		popup.add(defaultItem);
		defaultItem = new MenuItem("Open");
		defaultItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				xInProgess = false;
				yInProgess = false;
				frame.setVisible(true);
				frame.setExtendedState(JFrame.NORMAL);
				tray.remove(trayIcon);
				t1.stop();
			}
		});

		popup.add(defaultItem);
		trayIcon = new TrayIcon(image, "AutoMouseMover", popup);
		trayIcon.setImageAutoSize(true);
		trayIcon.addMouseListener(new java.awt.event.MouseAdapter() {

			@Override
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				if (evt.getClickCount() == 1) {
					xInProgess = false;
					yInProgess = false;
					frame.setVisible(true);
					frame.setExtendedState(JFrame.NORMAL);
					tray.remove(trayIcon);
					t1.stop();
				}
			}

		});
		frame = new JFrame("AutoMouseMover");
		frame.setBounds(100, 100, 450, 300);
		JButton startSaveButton = new JButton("Start and Hide to System Tray");
		JButton stopExitButton = new JButton("Stop and Exit");
		frame.setLayout(new FlowLayout());
		frame.add(startSaveButton);
		frame.add(stopExitButton);
		startSaveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					tray.add(trayIcon);
					frame.setVisible(false);

					xInProgess = true;
					yInProgess = true;
					System.out.println("added to SystemTray");
					Robot r = new Robot();
					/*
					 * PointerInfo a = MouseInfo.getPointerInfo(); Point p = a.getLocation();
					 */
					t1 = new Thread(new Runnable() {
						@Override
						public void run() {
							try {
								gradualMouseMove(r);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					});
					t1.start();
				} catch (AWTException ex) {
					System.out.println("unable to add to tray");
				}
				JOptionPane.showMessageDialog(frame, "Application Minimized in system tray");
			}
		});
		stopExitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				xInProgess = false;
				yInProgess = false;
				System.out.println("Exiting....");
				t1.stop();
				System.exit(0);

			}
		});
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

	}

	private static void gradualMouseMove(Robot robot) throws InterruptedException {
		Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int) size.getWidth();
		int height = (int) size.getHeight();
		do {
			System.out.println("Mouse Moved ...");
			Random random = new Random();
			int x = random.nextInt() % width;
			int y = random.nextInt() % height;
			robot.mouseMove(x, y);
			Thread.sleep(5000); // pause to slow down movement
		} while (xInProgess || yInProgess); // loop until complete on X & Y
	}

}
