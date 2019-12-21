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

@SuppressWarnings("serial")
public class GameView extends JFrame implements ActionListener {

	private JPanel mainPanel, namesPanel;
	private MyPanel boardPanel;
	private JLabel player1Label, player2Label, score1Label, score2Label;
	private int boardDimension;
	private int boardSize;
	private ImageIcon titleIcon, backgroundIcon;
	private JButton[][] emojiButtons;
	Font boldFont = new Font("Courier", Font.BOLD,18);
	Font  planeFont = new Font("Courier", Font.PLAIN,17);
	private ArrayList<Integer> iconIndexes;
	private ArrayList<Integer> currentIconIndexes;
	private final ImageIcon patternIcon = new ImageIcon(GameView.class.getResource("/pattern.png"));
	private Icon image;

	public GameView() {
	}

	public void displayGameWindow(ArrayList<String> profile) {

//		public void setBoard(int noOfCells, String player1, String player2, String player3){

		boardDimension = Integer.parseInt(profile.get(1));
		boardSize = boardDimension * 75;

		mainPanel = new JPanel();
		boardPanel = new MyPanel();
		namesPanel = new JPanel();

		titleIcon = new ImageIcon(OptionsView.class.getResource("/46.png"));// smiling emoji
		namesPanel.setAlignmentX(CENTER_ALIGNMENT);
		namesPanel.setPreferredSize(new Dimension(boardSize, boardSize / 3));
		namesPanel.setLayout(new GridLayout(2, 2));
		namesPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
				" P L A Y E R S     &     S C O R E S ", TitledBorder.CENTER, TitledBorder.TOP));

		player1Label = makeLabel("N");
		player1Label.setText(profile.get(2));
		player1Label.setVerticalAlignment(SwingConstants.BOTTOM);
		score1Label = makeLabel("S");
		player1Label.setFont(planeFont);
		score1Label.setFont(planeFont);
		setScore1Label(0, 0);
		score1Label.setVerticalAlignment(SwingConstants.NORTH);
		namesPanel.add(player1Label);

	
			player2Label = makeLabel("N");
			player2Label.setFont(planeFont);
			player2Label.setVerticalAlignment(SwingConstants.BOTTOM);
			score2Label = makeLabel("S");
			score2Label.setFont(planeFont);
			score2Label.setVerticalAlignment(SwingConstants.NORTH);
			if (!profile.get(0).equals("s")){
				namesPanel.add(player2Label);				
			} 
			namesPanel.add(score1Label);
			if (!profile.get(0).equals("s")){
			namesPanel.add(score2Label);
			}
			if (!profile.get(0).equals("s")){
				
		} 

		if (profile.get(0).equals("c") || profile.get(0).equals("h")) {
			player2Label.setText(profile.get(3));
		} 
		setScore2Label(0, 0);

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

		iconIndexes = new ArrayList<>();
		for (int i = 1; i < 73; i++) {
			iconIndexes.add(i);
		}
		Collections.shuffle(iconIndexes);

		iconIndexes = new ArrayList<Integer>(iconIndexes.subList(0, 72 - (72 - (boardDimension * boardDimension / 2))));

		for (int n = 0; n < iconIndexes.size(); n++) {
			currentIconIndexes.add(iconIndexes.get(n));
			currentIconIndexes.add(iconIndexes.get(n));
		}
		Collections.shuffle(currentIconIndexes);

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

	
	public void updateCardBoard(int i, int j) {
		Icon image;
		int iconName;
		iconName = getCurrentIconsNames().get(i * getCellDimension() + j);
		image = new ImageIcon(GameView.class.getResource("/" + iconName + ".png"));
		getEmojiButton(i, j).setIcon(image);
		System.out.println("CardBoard updated got emojiButton:("+i+","+j+")");
	}

	
	public JButton getEmojiButton(int i, int j) {
		return emojiButtons[i][j];
	}

	public int getCellDimension() {
		return boardDimension;
	}

	public ArrayList<Integer> getCurrentIconsNames() {
		return currentIconIndexes;
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
		} else if (string=="player2") {
			player2Label.setFont(boldFont);
			score2Label.setFont(boldFont);
			player1Label.setFont(planeFont);
			score1Label.setFont(planeFont);
		}	
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
		int counterTest = 0;
		image = new ImageIcon(GameView.class.getResource("/pattern.png"));
		emojiButtons[card.getCardIndex()[0]][card.getCardIndex()[1]].setIcon(image);
		counterTest++;
		if(counterTest==1) {
			System.out.println("first card closes " + "[" + card.getCardIndex()[0] + "]" + "[" + card.getCardIndex()[1] + "]");			
		} else if (counterTest ==2) {
			System.out.println("second card closes " + "[" + card.getCardIndex()[2] + "]" + "[" + card.getCardIndex()[3] + "]");
			counterTest=0;
		}
	}

	public void removeButtons(Card card1, Card card2) {
		emojiButtons[card1.getCardIndex()[0]][card1.getCardIndex()[1]].setVisible(false);
		emojiButtons[card2.getCardIndex()[0]][card2.getCardIndex()[1]].setVisible(false);
		System.out.println("set visible false");
		boardPanel.revalidate();
	}

	
	  public MyPanel getBoardPanel() { return boardPanel; }
	 
}