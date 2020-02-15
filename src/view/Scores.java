package view;

import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

public class Scores{
		JFrame frame;
		JPanel mainPanel, buttonPanel, textAreaPanel;
	public Scores() {	
		
		frame= new JFrame();  
		
		mainPanel = new JPanel();
		textAreaPanel = new JPanel();
		buttonPanel = new JPanel();
		
		mainPanel.setPreferredSize(new Dimension(350,330));
		textAreaPanel.setPreferredSize(new Dimension(335, 280));
		textAreaPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
				" HIGH SCORES ", TitledBorder.CENTER, TitledBorder.TOP));
				
		JTextArea textArea=new JTextArea(); 
		textArea.setPreferredSize(new Dimension(318,250));
		textArea.setEditable(false);
		textAreaPanel.add(textArea);

		JButton okButton = new JButton("close");
		okButton.setFont((new Font("dialog", Font.BOLD, 13)));
		buttonPanel.add(okButton);

		mainPanel.add(textAreaPanel);
		mainPanel.add(buttonPanel);
		
		frame.setUndecorated(true);
		frame.add(mainPanel);
		frame.pack();
		frame.setVisible(true);  
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);		
	}
}
