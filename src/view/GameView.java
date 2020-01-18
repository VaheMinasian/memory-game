package view;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;

import model.Card;

@SuppressWarnings("serial")
public class GameView extends JFrame implements ActionListener {

	private JPanel mainPanel, namesPanel, turnPanel;
	private MyPanel boardPanel;
	private JLabel player1Label, player2Label, score1Label, score2Label;
	private JLabel p1LabelGreen, p2LabelGreen, matchesLabel;
	private int boardDimension;
	private int boardSize;
	private ImageIcon backgroundIcon;
	private JButton[][] emojiButtons;
	JButton jbtn;
	Font boldFont, planeFont;
	private ArrayList<Integer> iconIndexes;
	private ArrayList<Integer> currentIcons;
	private JButton titleIconButton;
	private final ImageIcon titleIcon = new ImageIcon(GameView.class.getResource("/46.png"));
	private final ImageIcon patternIcon = new ImageIcon(GameView.class.getResource("/pattern.png"));
	private final ImageIcon greenOn = new ImageIcon(GameView.class.getResource("/green-on.png"));
	private final ImageIcon greenOff = new ImageIcon(GameView.class.getResource("/green-off.png"));
	private final ImageIcon unselectedImage = new ImageIcon(GameView.class.getResource("/image.png"));
	private final ImageIcon selectedImage = new ImageIcon(GameView.class.getResource("/46-grey.png"));
	private ImageIcon tempImage = null;
	private Icon image, image1, image2;
	private Image img;
	boolean buttonState;

	ImageIcon getScaledImage(ImageIcon image, int height, int width) {

		img = image.getImage();
		img = img.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH);
		tempImage = new ImageIcon(img);
		return tempImage;
	}

	public GameView() {
	}

	public void displayGameWindow(ArrayList<String> profile) {

//		public void setBoard(int noOfCells, String player1, String player2, String player3){

		boardDimension = Integer.parseInt(profile.get(1));
		boardSize = boardDimension * 75;

		boldFont = new Font("Courier", Font.BOLD, boardSize / 20);
		planeFont = new Font("Courier", Font.PLAIN, boardSize / 20);

		mainPanel = new JPanel();
		boardPanel = new MyPanel();
		namesPanel = new JPanel();

		// titleIcon = new ImageIcon(OptionsView.class.getResource("/46.png"));//
		// smiling emoji
		namesPanel.setAlignmentX(CENTER_ALIGNMENT);
		namesPanel.setSize(new Dimension(boardSize, boardSize / 3));
		namesPanel.setLayout(new GridLayout(2, 2));
		namesPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
				" P L A Y E R S     &     S C O R E S ", TitledBorder.CENTER, TitledBorder.TOP));

		turnPanel = new JPanel();
		turnPanel.setAlignmentX(CENTER_ALIGNMENT);
		turnPanel.setLayout(new GridBagLayout());
//		turnPanel.setPreferredSize(new Dimension(boardSize, boardSize / 8));
		matchesLabel = new JLabel(getScaledImage(unselectedImage, boardDimension * 8, boardDimension * 8));
		matchesLabel.setName("unselectedLabel");
		image1 = getScaledImage(greenOn, boardDimension * 8, boardDimension * 8);
		image2 = getScaledImage(greenOff, boardDimension * 8, boardDimension * 8);
		p1LabelGreen = new JLabel();
		p2LabelGreen = new JLabel();

		jbtn = new JButton();
		jbtn.setPreferredSize(new Dimension(60, 60));
		jbtn.add(matchesLabel);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;

		if (!profile.get(0).equals("s")) {
			gbc.gridx = 0;
			gbc.gridy = 0;
//		gbc.weightx=0.1;
			turnPanel.add(p1LabelGreen, gbc);

			gbc.gridx = 1;
			gbc.gridy = 0;
			turnPanel.add(Box.createHorizontalStrut(30), gbc);

			gbc.gridx = 2;
//		gbc.gridwidth = 3;   
			gbc.gridy = 0;
			turnPanel.add(jbtn, gbc);

			gbc.gridx = 3;
			gbc.gridy = 0;
			turnPanel.add(Box.createHorizontalStrut(30), gbc);

			gbc.gridx = 4;
			gbc.gridy = 0;
//		gbc.weightx=0.1;
			turnPanel.add(p2LabelGreen, gbc);
		}

		player1Label = makeLabel("N");
		player1Label.setText(profile.get(2));
		player1Label.setVerticalAlignment(SwingConstants.BOTTOM);
		score1Label = makeLabel("S");
		player1Label.setFont(planeFont);
		score1Label.setFont(planeFont);
		setScore1Label(0);
		score1Label.setVerticalAlignment(SwingConstants.NORTH);
		namesPanel.add(player1Label);

		player2Label = makeLabel("N");
		player2Label.setFont(planeFont);
		player2Label.setVerticalAlignment(SwingConstants.BOTTOM);
		score2Label = makeLabel("S");
		score2Label.setFont(planeFont);
		score2Label.setVerticalAlignment(SwingConstants.NORTH);
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
		currentIcons = new ArrayList<>(boardDimension * boardDimension);
		boardPanel.setBoardSize(boardSize);

		iconIndexes = new ArrayList<>();
		for (int i = 1; i < 73; i++) {
			iconIndexes.add(i);
		}
		Collections.shuffle(iconIndexes);

		iconIndexes = new ArrayList<Integer>(iconIndexes.subList(0, 72 - (72 - (boardDimension * boardDimension / 2))));

		for (int n = 0; n < iconIndexes.size(); n++) {
			currentIcons.add(iconIndexes.get(n));
			currentIcons.add(iconIndexes.get(n));
		}
		Collections.shuffle(currentIcons);

		emojiButtons = new JButton[boardDimension][boardDimension];
		for (int i = 0; i < boardDimension; i++) {
			for (int j = 0; j < boardDimension; j++) {
				emojiButtons[i][j] = new JButton(patternIcon);
				boardPanel.add(emojiButtons[i][j]);
			}
		}
		// Collections.shuffle(Arrays.asList(emojiButtons));

		UIManager.put("ToggleButton.select", Color.BLACK);
		SwingUtilities.updateComponentTreeUI(this);
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		mainPanel.add(boardPanel);
		mainPanel.add(turnPanel);
		mainPanel.add(namesPanel);
		this.add(mainPanel);
		this.pack();
		this.setPreferredSize(new Dimension(boardSize, boardSize + 100));
		this.setIconImage(titleIcon.getImage());
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setResizable(false);
		this.setVisible(true);

	}

	private JLabel makeLabel(String sort) {

		JLabel temp = new JLabel();

		temp.setHorizontalAlignment(JLabel.CENTER);
		temp.setOpaque(true);
		temp.setBackground(Color.black);
//		temp.setFont((new Font("dialog", Font.BOLD, fontSize)));
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
		Image newImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
		defaultImage = new ImageIcon(newImg);

		return defaultImage;
	}

	public void addGameViewListener(ActionListener BottonListener) {
		for (int i = 0; i < boardDimension; i++) {
			for (int j = 0; j < boardDimension; j++) {
				emojiButtons[i][j].addActionListener(BottonListener);
			}
		}
	}

	public void addBackgroundButtonListener(ActionListener bListener) {
		jbtn.addActionListener(bListener);
	}

	public JButton getJbtn() {
		return jbtn;
	}

	public void setJbtn(JButton jbtn) {
		this.jbtn = jbtn;
	}

	public void updateCardBoard(int i, int j) {
		Icon image;
		int iconName;
		iconName = getCurrentIcons().get(i * boardDimension + j);
		image = new ImageIcon(GameView.class.getResource("/" + iconName + ".png"));
		getEmojiButton(i, j).setIcon(image);
		System.out.println("CardBoard updated got emojiButton:(" + i + "," + j + ")");
	}

	public JButton getEmojiButton(int i, int j) {
		return emojiButtons[i][j];
	}

	public int getBoardDimension() {
		return boardDimension;
	}

	public ArrayList<Integer> getCurrentIcons() {
		return currentIcons;
	}

	public JButton[][] getEmojiButtons() {
		return emojiButtons;
	}

	public int getBoardSize() {
		return boardSize;
	}

	public void setActivePlayerFont(String string) {
		if (string == "player1") {
			player1Label.setFont(boldFont);
			score1Label.setFont(boldFont);
			player2Label.setFont(planeFont);
			score2Label.setFont(planeFont);
			p1LabelGreen.setIcon(image1);
			p2LabelGreen.setIcon(image2);

			
		} else if (string == "player2") {
			player2Label.setFont(boldFont);
			score2Label.setFont(boldFont);
			player1Label.setFont(planeFont);
			score1Label.setFont(planeFont);
			p1LabelGreen.setIcon(image2);
			p2LabelGreen.setIcon(image1);
		}
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

	public void restoreDefaultIcon(Card card) {
		image = new ImageIcon(GameView.class.getResource("/pattern.png"));
		emojiButtons[card.getCardIndex()[0]][card.getCardIndex()[1]].setIcon(image);

	}

	public void removeCards(Card card1, Card card2) {

		if (buttonState == false) {
			emojiButtons[card1.getCardIndex()[0]][card1.getCardIndex()[1]].setEnabled(false);
			emojiButtons[card2.getCardIndex()[0]][card2.getCardIndex()[1]].setEnabled(false);
			emojiButtons[card1.getCardIndex()[0]][card1.getCardIndex()[1]].setVisible(false);
			emojiButtons[card2.getCardIndex()[0]][card2.getCardIndex()[1]].setVisible(false);

		} else if (buttonState == true) {
			emojiButtons[card1.getCardIndex()[0]][card1.getCardIndex()[1]].setEnabled(false);
			emojiButtons[card2.getCardIndex()[0]][card2.getCardIndex()[1]].setEnabled(false);
		}

		System.out.println("set visible false");
		boardPanel.revalidate();
	}

	public void switchBackground() {
		if (buttonState == false) {
			matchesLabel.setIcon(selectedImage);
			for (int i = 0; i < emojiButtons.length; i++) {
				for (int j = 0; j < emojiButtons.length; j++) {
					if (emojiButtons[i][j].isVisible() == false) {
						emojiButtons[i][j].setVisible(true);
					}
				}
			}
		}
		if (buttonState == true) {
			matchesLabel.setIcon(unselectedImage);
			for (int i = 0; i < emojiButtons.length; i++) {
				for (int j = 0; j < emojiButtons.length; j++) {
					if ((emojiButtons[i][j].isVisible() == true) && (emojiButtons[i][j].isEnabled() == false)) {
						emojiButtons[i][j].setVisible(false);
					}
				}
			}
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
			p1LabelGreen.setIcon(image1);
			p2LabelGreen.setIcon(image2);
		} else if (activePlayerLabel.equals("player2")) {
			p2LabelGreen.setIcon(image1);
			p1LabelGreen.setIcon(image2);
		}
	}

}