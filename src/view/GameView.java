package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;

import model.Card;

public class GameView extends JFrame implements ActionListener {

	private JPanel mainPanel, namesPanel;
	private MyPanel boardPanel;
	private JLabel player1Label, player2Label, score1Label, score2Label;
	private static int cellDimension;
	private int boardDimension, fontSize;
	private ImageIcon titleIcon, backgroundIcon;
	private JButton[][] emojiButtons;

	private final ImageIcon patternIcon = new ImageIcon(OptionsView.class.getResource("/pattern.png"));
	private ArrayList<Integer> iconsNames;
	private static ArrayList<Integer> currentIconsNames;
	private int randomNumber;
	private Icon image;

	public GameView() {
	}

	public void initialize(ArrayList<String> profile) {

//		public void setBoard(int noOfCells, String player1, String player2, String player3){

		cellDimension = Integer.parseInt(profile.get(1));
		boardDimension = cellDimension * 75;
		fontSize = cellDimension * 4;

		mainPanel = new JPanel();
		boardPanel = new MyPanel();
		namesPanel = new JPanel();

		titleIcon = new ImageIcon(OptionsView.class.getResource("/46.png"));// smiling emoji
		namesPanel.setAlignmentX(CENTER_ALIGNMENT);
		namesPanel.setPreferredSize(new Dimension(boardDimension, boardDimension / 3));
		namesPanel.setLayout(new GridLayout(2, 2));
		namesPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
				" P L A Y E R S     &     S C O R E S ", TitledBorder.CENTER, TitledBorder.TOP));

		player1Label = makeLabel("N");
		player1Label.setText(profile.get(2));
		player1Label.setVerticalAlignment(SwingConstants.BOTTOM);
		score1Label = makeLabel("S");
		setScore1Label(0, 0);
		score1Label.setVerticalAlignment(SwingConstants.NORTH);
		namesPanel.add(player1Label);

		player2Label = makeLabel("N");
		player2Label.setVerticalAlignment(SwingConstants.BOTTOM);
		score2Label = makeLabel("S");
		score2Label.setVerticalAlignment(SwingConstants.NORTH);
		namesPanel.add(player2Label);
		namesPanel.add(score1Label);
		namesPanel.add(score2Label);

		if (profile.get(0).equals("c")) {
			player2Label.setText(profile.get(3));
		} else if (profile.get(0).equals("h")) {
			player2Label.setText(profile.get(3));
		}
		setScore2Label(0, 0);

		backgroundIcon = getRandomBackGIcon();
		backgroundIcon = resizeImage(backgroundIcon, getBoardDimension(), getBoardDimension());
		MyPanel.setImage(backgroundIcon);
		getBoardPanel().setLayout(new GridLayout(cellDimension, cellDimension));
		getBoardPanel().setVisible(true);
		getBoardPanel().setPreferredSize(new Dimension(boardDimension, boardDimension));
		getBoardPanel().setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
				" M E M O R Y      G A M E ", TitledBorder.CENTER, TitledBorder.TOP));

		currentIconsNames = new ArrayList<>(cellDimension * cellDimension);

		iconsNames = new ArrayList<>();
		for (int i = 1; i < 73; i++) {
			iconsNames.add(i);
		}
		Collections.shuffle(iconsNames);

		iconsNames = new ArrayList<Integer>(iconsNames.subList(0, 72 - (72 - (cellDimension * cellDimension / 2))));

		for (int n = 0; n < iconsNames.size(); n++) {
			currentIconsNames.add(iconsNames.get(n));
			currentIconsNames.add(iconsNames.get(n));
		}
		Collections.shuffle(currentIconsNames);

		emojiButtons = new JButton[cellDimension][cellDimension];
		for (int i = 0; i < cellDimension; i++) {
			for (int j = 0; j < cellDimension; j++) {
				emojiButtons[i][j] = new JButton(patternIcon);
				getBoardPanel().add(emojiButtons[i][j]);
			}
		}
		// Collections.shuffle(Arrays.asList(emojiButtons));

		UIManager.put("ToggleButton.select", Color.BLACK);
		SwingUtilities.updateComponentTreeUI(this);

		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		mainPanel.add(getBoardPanel());
		mainPanel.add(namesPanel);

		this.add(mainPanel);
		this.pack();
		this.setPreferredSize(new Dimension(boardDimension, boardDimension + 100));
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
		temp.setFont((new Font("dialog", Font.BOLD, fontSize)));
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
		for (int i = 0; i < cellDimension; i++) {
			for (int j = 0; j < cellDimension; j++) {
				emojiButtons[i][j].addActionListener(BottonListener);
			}
		}
	}

	public void updateCardBoard(int i, int j) {
		Icon image;
		int iconName;
		iconName = getCurrentIconsNames().get(i * getCellDimension() + j);
		image = new ImageIcon(GameView.class.getResource("/" + iconName + ".png"));
		getEmojiButton(i, j).setIcon(image);
		System.out.println("CardBoard updated got emojiButton:("+i+","+j+")");
	}

	/*
	 * public void removeButton(int x, int y){ emojiButtons[x][y].setVisible(false);
	 * System.out.println("set visible false"); boardPanel.revalidate(); }
	 */

	public JButton getEmojiButton(int i, int j) {
		return emojiButtons[i][j];
	}

	public static int getCellDimension() {
		return cellDimension;
	}

	public static ArrayList<Integer> getCurrentIconsNames() {
		return currentIconsNames;
	}

	public JButton[][] getEmojiButtons() {
		return emojiButtons;
	}

	public int getBoardDimension() {
		return boardDimension;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
	}

	public void setScore1Label(int newScore, int tries) {
		this.score1Label.setText(newScore + " out of " + tries);
	}

	public void setScore2Label(int newScore, int tries) {
		this.score2Label.setText(newScore + " out of " + tries);
	}

	public void closeButton(Card card) {
		image = new ImageIcon(GameView.class.getResource("/pattern.png"));
		emojiButtons[card.getCardIndex()[0]][card.getCardIndex()[1]].setIcon(image);
		// emojiButtons[x][y].setSelected(false);
	}

	public void removeButtons(Card card1, Card card2) {
		emojiButtons[card1.getCardIndex()[0]][card1.getCardIndex()[1]].setVisible(false);
		emojiButtons[card2.getCardIndex()[0]][card2.getCardIndex()[1]].setVisible(false);
		System.out.println("set visible false");
		getBoardPanel().revalidate();
	}

	public MyPanel getBoardPanel() {
		return boardPanel;
	}
}