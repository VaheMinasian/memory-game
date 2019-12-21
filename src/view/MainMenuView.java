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

	private JButton playButton;
	private JButton optionsButton;
	private JButton exitButton;

//	  this is the constructor method for the class MainMenuView creates the main menu view object.
	public MainMenuView() {

		frame = new JFrame("Memory");
		ImageIcon frameIcon = new ImageIcon(OptionsView.class.getResource("/46.png"));
		frame.setIconImage(frameIcon.getImage());
		panel = new JPanel(new GridBagLayout());
		panel.setPreferredSize(new Dimension(220, 300));
		panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), " M A I N    M E N U ",
				TitledBorder.CENTER, TitledBorder.TOP));
		frame.getContentPane().add(panel);

		playButton = new JButton("play!");
		playButton.setFont((new Font("dialog", Font.BOLD, 13)));
		playButton.setPreferredSize(new Dimension(140, 60));
//		playButton.setActionCommand("start");
		playButton.setEnabled(false);

		optionsButton = new JButton("setup");
		optionsButton.setFont((new Font("dialog", Font.BOLD, 13)));
		optionsButton.setPreferredSize(new Dimension(140, 60));
//		optionsButton.setActionCommand("options");

		exitButton = new JButton("Quit");
		exitButton.setFont((new Font("dialog", Font.BOLD, 13)));
		exitButton.setPreferredSize(new Dimension(140, 60));
//		exitButton.setActionCommand("exit");

		GridBagConstraints c = new GridBagConstraints();

		c.insets = new Insets(12, 2, 12, 2);
		c.gridx = 0;
		c.gridy = 0;
		panel.add(playButton, c);

		c.gridx = 0;
		c.gridy = 1;
		panel.add(optionsButton, c);

		c.gridx = 0;
		c.gridy = 2;
		panel.add(exitButton, c);

		frame.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setVisible(true);
	}

	// Methods adds action listener to Main menu buttons
	public void addMainMenuViewListener(ActionListener selected) {
		optionsButton.addActionListener(selected);
		playButton.addActionListener(selected);
		exitButton.addActionListener(selected);
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