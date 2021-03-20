package de.pixelbeat.gui;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.border.Border;

public class MainGUI extends JFrame implements ActionListener{
	
	
	Action enterAction;
	
	JTextField commandField;
	JButton commandSubmitButton;
	
	static JLabel Uptime;
	public MainGUI() {
		LoadingGUI.frame.dispose();
		ImageIcon icon = new ImageIcon("icon.png");
		Border border = BorderFactory.createLineBorder(new Color(0, 0, 0));
		enterAction = new EnterAction();
		
		//*****************************************************************************
			
			JPanel consolePanel = new JPanel();
			consolePanel.setBounds(599, 0, 600, 762);
			consolePanel.setBackground(new Color(78,93,148));
			consolePanel.setBorder(border);
		
		//*****************************************************************************
			commandSubmitButton = new JButton("Submit");
			commandSubmitButton.addActionListener(this);
			commandSubmitButton.setPreferredSize(new Dimension(80,40));
			commandSubmitButton.setBackground(new Color(171, 189, 212));
			commandSubmitButton.getInputMap().put(KeyStroke.getKeyStroke("ENTER"), "enterAction");
			commandSubmitButton.getActionMap().put("enterAction", enterAction);
			
			commandField = new JTextField();
			commandField.setPreferredSize(new Dimension(480,37));
			commandField.setFont(new Font("Comic Sans",Font.PLAIN,22));
			commandField.setHorizontalAlignment(JTextField.LEFT);
			commandField.setBackground(new Color(171, 189, 212));
			commandField.getInputMap().put(KeyStroke.getKeyStroke("ENTER"), "enterAction");
			commandField.getActionMap().put("enterAction", enterAction);
			
			JPanel commandLinePanel = new JPanel();
			commandLinePanel.setBounds(599, 710, 600, 40);
			commandLinePanel.setBorder(border);
			commandLinePanel.add(commandSubmitButton);
			commandLinePanel.add(commandField);
			
		//*****************************************************************************
			Uptime = new JLabel();
			
			JPanel botinfoPanel = new JPanel();
			botinfoPanel.setBounds(0, 0, 600, 400);
			botinfoPanel.setBackground(new Color(112, 145, 255));
			botinfoPanel.setBorder(border);
			botinfoPanel.add(Uptime);
		/*
		JLabel label = new JLabel();
		label.setIcon(icon);
		*/		
		this.setTitle("Pixel-Beat Monitor");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setSize(1200, 800);
		this.setVisible(true);
		this.setLayout(null);
		this.setIconImage(icon.getImage());
		this.getContentPane().setBackground(new Color(44, 47, 51));
		this.add(botinfoPanel);
		this.add(consolePanel);
		this.add(commandLinePanel);
		//this.pack();
	}

	
	@SuppressWarnings("serial")
	public class EnterAction extends AbstractAction{
		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println("Welcome "+commandField.getText());
			Uptime.setText(commandField.getText());
			commandField.setText("");
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == commandSubmitButton){
			System.out.println("Welcome "+commandField.getText());
			Uptime.setText(commandField.getText());
			commandField.setText("");
			LoadingGUI.frame.dispose();
		}
	}
	public static void updateUptime(String uptime) {
		Uptime.setText("Uptime: "+uptime);
	}
}
