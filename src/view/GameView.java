package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

@SuppressWarnings("serial")
public class GameView extends JFrame implements ActionListener {

	private JPanel mainPanel, turnPanel, namesPanel, timePanel;
	private MyPanel boardPanel;
	private JLabel player1Label, player2Label, score1Label, score2Label, p1TurnLabel, p2TurnLabel, iconBackgroundTogglerLabel, timerLabel;
	private int boardSize, boardDimension, lowIndex = 1, highIndex = 73, indexRange = 73;
	private ArrayList<JButton> emojiButtons;
	JButton matchesButton;
	Font boldFont, planeFont;
	private ArrayList<Integer> allIconIndexes, currentIconIndexes;
	private ImageIcon backgroundIcon;
	private Image scaledImage;
	private final ImageIcon titleIcon = new ImageIcon(GameView.class.getResource("/46.png")),
			patternIcon = new ImageIcon(GameView.class.getResource("/pattern.png")),
			greenOn = new ImageIcon(GameView.class.getResource("/green-on.png")),
			greenOff = new ImageIcon(GameView.class.getResource("/green-off.png")),
			unselectedImageIcon = new ImageIcon(GameView.class.getResource("/image.png")),
			selectedImageIcon = new ImageIcon(GameView.class.getResource("/46-grey.png"));
	private Icon scaledImageIcon, greenOnIcon, greenOffIcon, unselectedIcon, selectedIcon;
	boolean buttonState;
	Border emptyBorder;
	private Date elapsed;
	SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

	public GameView() {}

	public void displayGameWindow(ArrayList<String> profile) {
		switch (profile.get(5)) {
		case "animals":
			lowIndex = 100;
			highIndex = 150;
			indexRange = 50;
			break;
		case "musical":
			lowIndex = 200;
			highIndex = 250;
			indexRange = 50;
			break;
		case "food":
			lowIndex = 300;
			highIndex = 382;
			indexRange = 82;
			break;
		case "emotics":
		default:
			lowIndex = 1;
			highIndex = 73;
			indexRange = 72;
			break;
		}
		
		boardDimension = Integer.parseInt(profile.get(1));
		boardSize = boardDimension * 75;
		timePanel = new JPanel();
		timerLabel = new JLabel("00:00:00");
		timerLabel.setAlignmentY(RIGHT_ALIGNMENT);
		timerLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
//		timerLabel.setForeground(Color.ORANGE);
		timerLabel.setBounds(96, 221, 200, 220);
		timerLabel.setToolTipText("elapsed time");

		timePanel.add(timerLabel);

		boldFont = new Font("Courier", Font.BOLD, boardSize / 20);
		planeFont = new Font("Courier", Font.PLAIN, boardSize / 20);

		mainPanel = new JPanel();
		boardPanel = new MyPanel();
		namesPanel = new JPanel();

		namesPanel.setAlignmentX(CENTER_ALIGNMENT);
		namesPanel.setPreferredSize(new Dimension(boardSize, boardSize * 2 / 7));
		namesPanel.setLayout(new GridLayout(2, 2));
		namesPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
				" P L A Y E R S     &     S C O R E S ", TitledBorder.CENTER, TitledBorder.TOP));

		turnPanel = new JPanel();
		turnPanel.setAlignmentX(CENTER_ALIGNMENT);
		turnPanel.setLayout(new GridBagLayout());
		turnPanel.setPreferredSize(new Dimension(boardSize, boardSize * 2 / 11));
		turnPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "",
				TitledBorder.CENTER, TitledBorder.TOP));

		greenOnIcon = getScaledImage(greenOn, boardDimension * 8, boardDimension * 8);
		greenOffIcon = getScaledImage(greenOff, boardDimension * 8, boardDimension * 8);
		unselectedIcon = getScaledImage(unselectedImageIcon, boardDimension * 8, boardDimension * 8);
		selectedIcon = getScaledImage(selectedImageIcon, boardDimension * 8, boardDimension * 8);
		
		iconBackgroundTogglerLabel = new JLabel();
		iconBackgroundTogglerLabel.setSize(new Dimension((int) Math.pow(boardDimension, 2), (int) Math.pow(boardDimension, 2)));
		iconBackgroundTogglerLabel.setIcon(unselectedIcon);
		p1TurnLabel = new JLabel();
		p2TurnLabel = new JLabel();

		emptyBorder = BorderFactory.createEmptyBorder();
		matchesButton = new JButton();
		matchesButton.setBorder(emptyBorder);
		matchesButton.setBackground(new java.awt.Color(238, 238, 238));
		matchesButton.setOpaque(true);
		matchesButton.setMargin(new Insets(0, 3, 0, 3));
		matchesButton.setPreferredSize(new Dimension((int) Math.pow(boardDimension, 3), (int) Math.pow(boardDimension, 3)));
		matchesButton.add(iconBackgroundTogglerLabel);
		matchesButton.setToolTipText("showing image in background");

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.CENTER;

		if (!profile.get(0).equals("s")) {
			gbc.gridx = 0;
			gbc.gridy = 0;
			gbc.weightx = GridBagConstraints.CENTER;
			turnPanel.add(p1TurnLabel, gbc);

			gbc.gridx = 1;
			gbc.gridy = 0;
			turnPanel.add(matchesButton, gbc);

			gbc.gridx = 2;
			gbc.gridy = 0;
			gbc.weightx = GridBagConstraints.CENTER;
			turnPanel.add(p2TurnLabel, gbc);

		} else {
			gbc.gridx = 0;
			gbc.gridy = 0;
			turnPanel.add(matchesButton, gbc);
		}

		player1Label = makeLabel("N");
		player1Label.setText(profile.get(2));
		player1Label.setVerticalAlignment(SwingConstants.BOTTOM);
		score1Label = makeLabel("S");
		player1Label.setFont(boldFont);
		score1Label.setFont(planeFont);
		setScore1Label(0);
		score1Label.setVerticalAlignment(SwingConstants.NORTH);
		score1Label.setToolTipText("player 1 score");
		namesPanel.add(player1Label);

		player2Label = makeLabel("N");
		player2Label.setFont(boldFont);
		player2Label.setVerticalAlignment(SwingConstants.BOTTOM);
		score2Label = makeLabel("S");
		score2Label.setFont(planeFont);
		score2Label.setVerticalAlignment(SwingConstants.NORTH);
		score2Label.setToolTipText("player 2 score");

		if (!profile.get(0).equals("s")) {
			namesPanel.add(player2Label);
		}
		namesPanel.add(score1Label);
		if (!profile.get(0).equals("s")) {
			namesPanel.add(score2Label);
		}

		if (profile.get(0).equals("c") || profile.get(0).equals("h")) {
			player2Label.setText(profile.get(3));
		}
		setScore2Label(0);

		backgroundIcon = getRandomBackGIcon();
		backgroundIcon = resizeImage(backgroundIcon, getBoardSize(), getBoardSize());
		boardPanel.setImage(backgroundIcon);
		boardPanel.setLayout(new GridLayout(boardDimension, boardDimension));
		boardPanel.setVisible(true);
		boardPanel.setPreferredSize(new Dimension(boardSize, boardSize));
		boardPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
				" M E M O R Y      G A M E ", TitledBorder.CENTER, TitledBorder.TOP));
		currentIconIndexes = new ArrayList<>(boardDimension * boardDimension);
		boardPanel.setBoardSize(boardSize);
	
		allIconIndexes = new ArrayList<>();
		for (int i = lowIndex; i < highIndex; i++) {
			allIconIndexes.add(i);
		}
		System.out.println("lowIndex is: " + lowIndex);
		System.out.println("highIndex is: " + highIndex);
		Collections.shuffle(allIconIndexes);
		allIconIndexes = new ArrayList<Integer>(allIconIndexes.subList(0,
				indexRange - (indexRange-(boardDimension * boardDimension / 2))));

		for (int n = 0; n < allIconIndexes.size(); n++) {
			currentIconIndexes.add(allIconIndexes.get(n));
			currentIconIndexes.add(allIconIndexes.get(n));
		}
		Collections.shuffle(currentIconIndexes);

		emojiButtons = new ArrayList<>();
		for (int i = 0; i < boardDimension*boardDimension; i++) {
				emojiButtons.add(new JButton(patternIcon));
				boardPanel.add(emojiButtons.get(i));
		}

//		UIManager.put("ToggleButton.select", Color.BLACK);
//		SwingUtilities.updateComponentTreeUI(this);
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		mainPanel.add(boardPanel);
		mainPanel.add(turnPanel);
		mainPanel.add(namesPanel);
		mainPanel.add(timePanel);
		this.add(mainPanel);
		this.pack();

//		this.add(timerLabel);
		this.setPreferredSize(new Dimension(boardSize, boardSize + 100));
		this.setIconImage(titleIcon.getImage());
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.setResizable(false);
		this.setVisible(true);
	}

	ImageIcon getScaledImage(ImageIcon image, int height, int width) {

		scaledImage = image.getImage();
		scaledImage = scaledImage.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH);
		image.setImage(scaledImage);
		return image;
	}

	public void updateClock(long startTime) {
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
		elapsed = new Date(System.currentTimeMillis() - startTime);
		timerLabel.setText(sdf.format(elapsed));
	}

	public Date getElapsed() {
		return elapsed;
	}

	public JLabel getTimerLabel() {
		return timerLabel;
	}

	public void setTimerLabel(JLabel timerLabel) {
		this.timerLabel = timerLabel;
	}

	private JLabel makeLabel(String sort) {
		JLabel temp = new JLabel();

		temp.setHorizontalAlignment(JLabel.CENTER);
		temp.setOpaque(true);
		temp.setBackground(Color.black);
		temp.setFont((boldFont));
		if (sort == "S")
			temp.setForeground(Color.ORANGE);
		else if (sort == "N")
			temp.setForeground(Color.GRAY);
		else
			System.out.println("shouldn't apear");
		return temp;
	}

	private ImageIcon getRandomBackGIcon() {
		int big = 75, small = 73, random;
		Random rand = new Random();
		ImageIcon icon;
		random = (rand.nextInt(big + 1 - small) + small);
		icon = new ImageIcon(GameView.class.getResource("/" + random + ".jpg"));
		return icon;
	}

	public static ImageIcon resizeImage(ImageIcon defaultImage, int width, int height) {

		Image img = defaultImage.getImage();
		img = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
		defaultImage.setImage(img);
		return defaultImage;
	}

	public void addGameViewListener(ActionListener BottonListener) {
		for (int i = 0; i < boardDimension*boardDimension; i++) {
				emojiButtons.get(i).addActionListener(BottonListener);
		}
	}

	public void addBackgroundButtonListener(ActionListener bListener) {
		matchesButton.addActionListener(bListener);
	}

	public JButton getMatchesButton() {
		return matchesButton;
	}

	public void setMatchesButton(JButton matchesButton) {
		this.matchesButton = matchesButton;
	}

	public void removeCard(int index) {
		if (buttonState == false) {
			emojiButtons.get(index).setEnabled(false);
			emojiButtons.get(index).setVisible(false);
			System.out.println("buttonState is false, setEnabled(false); is set");
		} else if (buttonState == true) {
			emojiButtons.get(index).setEnabled(false);
			System.out.println("buttonState is true, setEnabled(false); is set");
		}

		System.out.println("set visible false");
		boardPanel.revalidate();
	}

	public void updateCardBoard(int index) {
		Icon image;
		int iconName;
		iconName = getCurrentIcons().get(index);
		image = new ImageIcon(GameView.class.getResource("/" + iconName + ".png"));
		emojiButtons.get(index).setIcon(image);
		System.out.println("CardBoard updated got emojiButton of index:(" + index + ")");
	}

	public JButton getEmojiButton(int index) {
		return emojiButtons.get(index);
	}

	public int getBoardDimension() {
		return boardDimension;
	}

	public ArrayList<Integer> getCurrentIcons() {
		return currentIconIndexes;
	}

	public ArrayList<JButton> getEmojiButtons() {
		return emojiButtons;
	}

	public int getBoardSize() {
		return boardSize;
	}

	public void setActivePlayerLight(String string) {
		if (string == "player1") {
			p1TurnLabel.setIcon(greenOnIcon);
			p1TurnLabel.setToolTipText("play!");
			p2TurnLabel.setIcon(greenOffIcon);
			p2TurnLabel.setToolTipText("wait!");

		} else if (string == "player2") {
			p1TurnLabel.setIcon(greenOffIcon);
			p1TurnLabel.setToolTipText("wait!");
			p2TurnLabel.setIcon(greenOnIcon);
			p2TurnLabel.setToolTipText("play!");
		}
	}

	public boolean getButtonState() {
		return buttonState;
	}

	public void setButtonState(boolean buttonState) {
		this.buttonState = buttonState;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
	}

	public void setScore1Label(int newScore) {
		this.score1Label.setText("" + newScore);
	}

	public void setScore2Label(int newScore) {
		this.score2Label.setText("" + newScore);
	}

	public void restoreDefaultIcon(int index) {
		scaledImageIcon = new ImageIcon(GameView.class.getResource("/pattern.png"));
		emojiButtons.get(index).setIcon(scaledImageIcon);

	}

	public void switchBackground() {
		if (buttonState == false) {
			iconBackgroundTogglerLabel.setIcon(selectedIcon);
			for (int i = 0; i < emojiButtons.size(); i++) {
					if (emojiButtons.get(i).isVisible() == false) {
						emojiButtons.get(i).setVisible(true);	
				}
			}
			matchesButton.setToolTipText("showing guessed cards in background");
		}
		if (buttonState == true) {
			iconBackgroundTogglerLabel.setIcon(unselectedIcon);
			for (int i = 0; i < emojiButtons.size(); i++) {
					if ((emojiButtons.get(i).isVisible() == true) && (emojiButtons.get(i).isEnabled() == false)) {
						emojiButtons.get(i).setVisible(false);
				}
			}
			matchesButton.setToolTipText("showing image in background");
		}
		switchButtonState();
	}

	void switchButtonState() {
		if (buttonState == false) {
			buttonState = true;
			System.out.println(buttonState);
		} else if (buttonState == true) {
			buttonState = false;
			System.out.println(buttonState);
		}
	}

	public MyPanel getBoardPanel() {
		return boardPanel;
	}

	public void switchActivePlayerLight(String activePlayerLabel) {

		// TODO Auto-generated method stub
		if (activePlayerLabel.equals("player1")) {
			p1TurnLabel.setIcon(greenOnIcon);
			p1TurnLabel.setToolTipText("play!");
			p2TurnLabel.setIcon(greenOffIcon);
			p2TurnLabel.setToolTipText("wait!");

		} else if (activePlayerLabel.equals("player2")) {
			p2TurnLabel.setIcon(greenOnIcon);
			p2TurnLabel.setToolTipText("play!");
			p1TurnLabel.setIcon(greenOffIcon);
			p1TurnLabel.setToolTipText("wait!");
		}
	}

	public void removeIcons(int firstNumber, int secondNumber) {
		currentIconIndexes.set(currentIconIndexes.indexOf(firstNumber), 0);
		currentIconIndexes.set(currentIconIndexes.indexOf(secondNumber), 0);
	}

}