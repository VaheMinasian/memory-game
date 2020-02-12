package view;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

@SuppressWarnings("serial")
public class MainMenuView extends JFrame implements ActionListener {

	public JFrame frame;
	public JPanel panel;
	private JButton resumeButton, playButton, optionsButton, exitButton;


//	  this is the constructor method for the class MainMenuView creates the main menu view object.
	public MainMenuView() {

		//set up main menu frame and main panel
		frame = new JFrame("Memory");
		ImageIcon frameIcon = new ImageIcon(OptionsView.class.getResource("/46.png"));
		frame.setIconImage(frameIcon.getImage());
		panel = new JPanel(new GridBagLayout());
		panel.setPreferredSize(new Dimension(220, 370));
		panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), " M A I N    M E N U ",
				TitledBorder.CENTER, TitledBorder.TOP));
		frame.getContentPane().add(panel);
		
		//create JButtons
		resumeButton = new JButton("resume game");
		resumeButton.setFont((new Font("dialog", Font.BOLD, 13)));
		resumeButton.setPreferredSize(new Dimension(140, 60));
		resumeButton.setEnabled(false);
		resumeButton.setToolTipText("resume last saved game");
		playButton = new JButton("new game");
		playButton.setFont((new Font("dialog", Font.BOLD, 13)));
		playButton.setPreferredSize(new Dimension(140, 60));
		playButton.setEnabled(false);
		playButton.setToolTipText("start a new game with the last saved options");
		optionsButton = new JButton("setup");
		optionsButton.setFont((new Font("dialog", Font.BOLD, 13)));
		optionsButton.setPreferredSize(new Dimension(140, 60));
		optionsButton.setToolTipText("change profile options");
		exitButton = new JButton("Quit");
		exitButton.setFont((new Font("dialog", Font.BOLD, 13)));
		exitButton.setPreferredSize(new Dimension(140, 60));
		exitButton.setToolTipText("save and quit");
		
		//add buttons to main panel using gridbaglayout
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(12, 2, 12, 2);
		gbc.gridx = 0;
		gbc.gridy = 0;
		panel.add(resumeButton, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 1;
		panel.add(playButton, gbc);

		gbc.gridx = 0;
		gbc.gridy = 2;
		panel.add(optionsButton, gbc);

		gbc.gridx = 0;
		gbc.gridy = 3;
		panel.add(exitButton, gbc);

		//finalize frame to display UI
		frame.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setVisible(true);
	}

	// Methods adds action listener to Main menu buttons
	public void addMainMenuViewListener(ActionListener selected) {
		resumeButton.addActionListener(selected);
		playButton.addActionListener(selected);
		optionsButton.addActionListener(selected);
		exitButton.addActionListener(selected);
	}

	public JButton getResumeButton() {
		return resumeButton;
	}
	
	public JButton getOptionsButton() {
		return optionsButton;
	}

	public JButton getPlayButton() {
		return playButton;
	}

	public JButton getExitButton() {
		return exitButton;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
	}
}