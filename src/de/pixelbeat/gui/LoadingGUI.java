package de.pixelbeat.gui;

import java.awt.Color;
import java.awt.HeadlessException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

public class LoadingGUI extends JFrame {

	public static JFrame frame = new JFrame(); 
	public static JProgressBar bar = new JProgressBar();
	
	public LoadingGUI(){
		ImageIcon icon = new ImageIcon("/icon.png");
		
		bar.setValue(0);
		bar.setBounds(300, 500, 600, 50);
		bar.setStringPainted(true);
		bar.setForeground(Color.RED);
		bar.setBackground(Color.BLACK);
		
		JLabel label = new JLabel("");
		label.setIcon(icon);
		label.setEnabled(true);
		label.setBounds(450, 150, 300, 300);
		
		this.setTitle("Pixel-Beat Monitor");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setSize(1200, 800);
		this.setVisible(true);
		this.setLayout(null);
		this.setIconImage(icon.getImage());
		this.getContentPane().setBackground(new Color(44, 47, 51));
		this.add(bar);
		this.add(label);
		frame = this;
	}
}
