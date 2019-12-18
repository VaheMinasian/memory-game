package model;

import java.util.ArrayList;
import java.util.Random;

import javax.swing.JOptionPane;
import javax.swing.Timer;

import control.ButtonNotAvailableException;
import model.Card.CardState;
import view.GameView;

public class MemoryGame implements Game {

//	private static MemoryGame Single_Instance = null;

	private Card[][] cards;
	private Card selectedCard;
	private static Board boardDimension;
	private static Player player1;
	private static Player player2;
	private static String gameMode;
	private boolean hasWinner = false;
	private Player activePlayer;
	private int numberOfCells;
	private static Timer timer;

	private Card firstCard = null, secondCard = null;
//	private int firstNumber, secondNumber; // Icon file names
	private static int[] buttonsIndex = new int[4];
	private int counter = 0;
	private int wincheck = 0;

	public MemoryGame() {

	}

	/*
	 * public static MemoryGame getInstance() { // TODO Auto-generated method stub
	 * if (Single_Instance == null) { synchronized (MemoryGame.class) {
	 * Single_Instance = new MemoryGame(); } } return Single_Instance; }
	 */

	public void initializeParameters(ArrayList<String> profile) {
		// TODO Auto-generated method stub
		player1 = new HumanPlayer(profile.get(2));
		if (profile.get(0).equals("c")) {
			player2 = new ComputerPlayer(profile.get(3));
		} else if (profile.get(0).equals("h")) {
			player2 = new HumanPlayer(profile.get(3));
		}
		setGameMode(profile.get(0));
		setBoardDimension(new Board(profile.get(1)));

		createCards(Integer.parseInt(profile.get(1)));
	}

	private void createCards(int dimension) {
		cards = new Card[dimension][dimension];
		for (int i = 0; i < dimension; i++) {
			for (int j = 0; j < dimension; j++) {

				cards[i][j] = new Card(i, j);
			}
		}
		numberOfCells = dimension * dimension;
	}

	@Override
	public boolean move(int i, int j) {
		/*
		 * System.out.println("Entering move'1', counter= " + counter + "\n");
		 * System.out.println("firstCard.equals(selectedCard) is: " +
		 * (!firstCard.equals(selectedCard)));
		 */
		if (counter == 0) {// &&(firstCard.getState() == CardState.CLOSED) && (secondCard.getState() ==
							// CardState.CLOSED)){
			// if ((firstCard.getState() == CardState.CLOSED) && (secondCard.getState() ==
			// CardState.CLOSED)){
			firstCard = cards[i][j];
//			firstNumber = GameView.getCurrentIconsNames().get(i * GameView.getCellDimension() + j);
			firstCard.updateCard(CardState.OPEN);
			buttonsIndex[0] = i;
			buttonsIndex[1] = j;

			System.out.println("Buton 1 index is  [" + i + "][" + j + "]");
//			System.out.println("first emoji no. " + firstNumber + " - second emoji no. " + secondNumber);
//			  System.out.println("first card is: " + firstCard.getState() + " - second card is: " + secondCard.getState());

			counter++;

			System.out.println("Leaving move1 counter= " + counter);
			return true;
		}

		// if ((firstCard.getState() == CardState.OPEN) && (firstCard != selectedCard)
		// && (secondCard.getState() == CardState.CLOSED)){
		else if ((counter == 1) && (!firstCard.equals(selectedCard))) {
//			System.out.println("Entering move'2', counter= " + counter + "\n");
			secondCard = cards[i][j];
//			secondNumber = GameView.getCurrentIconsNames().get(i * GameView.getCellDimension() + j);
			secondCard.updateCard(CardState.OPEN);
			buttonsIndex[2] = i;
			buttonsIndex[3] = j;

			System.out.println("Buton 2 index is [" + i + "][" + j + "]");
//			System.out.println("first emoji no. " + firstNumber + " - second emoji no. " + secondNumber);
			System.out.println("first card is \"" + firstCard.getState() + "\" - second card is \""
					+ secondCard.getState() + "\"");

			counter++;
			System.out.println("Leaving move'2', counter= " + counter + "\n");
			return true;
		}
		return false;
	}

	@Override
	public Boolean getStatus(int x, int y) {

		if (x == y) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public int getMessage(String s) {
		int i;
		String[] options = { "play", "Main Menu", "Quit" };
		int returnValue=-10;
		wincheck = (s.equals("s")) ? 1 : 2;
		if (wincheck == 1) {
			System.out.println("inside wincheck==1");

			i = JOptionPane.showOptionDialog(null,
					"Congratulations " + player1.getName() + ", you have won! \nYour socre is " + player1.getScore()
							+ " out of " + player1.getTries() + " moves.",
					"Memory", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

			switch (i) {
			case 0:
				returnValue= 0;
				break;
			case 1:
				returnValue= 1;
				break;
			case 2:
				returnValue= 2;
				break;
			default:
				returnValue= 2;
				break;
			}

		} else if (wincheck == 2) {
			System.out.println("inside wincheck==2");

			switch (player1.getScore() - player2.getScore()) {

			case 0:
				i = JOptionPane.showOptionDialog(null, "The game is drawn!", "Memory", JOptionPane.DEFAULT_OPTION,
						JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
				switch (i) {
				case 0:
					returnValue= 0;
					break;
				case 1:
					returnValue= 1;
					break;
				case 2:
					returnValue= 2;
					break;
				default:
					returnValue= 2;
					break;
				}
				break;
			default:
				int triesOfWinner = (player1.getScore() > numberOfCells / 4) ? player1.getTries() : player2.getTries();
				int winnerScore = Math.max(player1.getScore(), player2.getScore());

				i = JOptionPane.showOptionDialog(null,
						"Congratulations " + getWinnersName(winnerScore) + ", you have won! \nYour socre is "
								+ winnerScore + " out of " + triesOfWinner + " moves.",
						"Memory", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options,
						options[0]);

			}
			switch (i) {
			case 0:
				returnValue= 0;
				break;
			case 1:
				returnValue= 1;
				break;
			case 2:
				returnValue= 2;
				break;
			default:
				returnValue= 2;
				break;
			}
			
		}
		return returnValue;
	}

	// randomly selects the player who will begin the game
	public void randomFirstPlayer() {
		Random random = new Random();
		// Create random integer 1 or 2 refering to eventual 2 players
		int randomInteger = random.nextInt(2) + 1;

		if (randomInteger == 1)
			setActivePlayer(player1);
		else if ((randomInteger == 2) && (!player2.getName().equals("")))
			setActivePlayer(player2);
	}

	// switches the current player
	public void switchActivePlayer() {
		if (activePlayer.equals(player1)) {
			player1.setActive(false);
			player2.setActive(true);
			setActivePlayer(player2);
			System.out.println("active player is: " + getActivePlayer().getName());

		} else if (activePlayer.equals(player2)) {
			player2.setActive(false);
			player1.setActive(true);
			setActivePlayer(player1);
			System.out.println("active player is: " + getActivePlayer().getName());
		}
	}

	public void revoke() {
//		firstNumber = 0;
//		secondNumber = 0;
		getActivePlayer().incrementTries();
		setSelectedCard(null);

		// MemoryModel.getSelectedCard().updateCard(CardState.CLOSED);
		getFirstCard().updateCard(CardState.CLOSED);
		getSecondCard().updateCard(CardState.CLOSED);
		System.out.println("inside mismatch of revoke()\n");
	}

	public void update() {
//		firstNumber = 0;
//		secondNumber = 0;
		setSelectedCard(null);
		getActivePlayer().incrementTries();
		getActivePlayer().incrementScore();

		// MemoryModel.getSelectedCard().updateCard(CardState.CLOSED);
		getFirstCard().updateCard(CardState.NONE);
		getSecondCard().updateCard(CardState.NONE);
		System.out.println("inside MATCH of update()\n");
	}

	public boolean gameOver() {

		for (int i = 0; i < cards.length; i++) {
			for (int j = 0; j < cards[i].length; j++) {
				if (cards[i][j].getState() != CardState.NONE)
					return false;
			}
		}
		return true;
	}

	public void setActivePlayer(Player activePlayer) {
		this.activePlayer = activePlayer;
	}

	public Player getActivePlayer() {
		return activePlayer;
	}

	public static void setPlayer1(Player player1) {
		MemoryGame.player1 = player1;
	}

	public static void setPlayer2(Player player2) {
		MemoryGame.player2 = player2;
	}

	public Player getPlayer1() {
		return player1;
	}

	public Player getPlayer2() {
		return player2;
	}

	public boolean isHasWinner() {
		return hasWinner;
	}

	public void setHasWinner(boolean hasWinner) {
		this.hasWinner = hasWinner;
	}

	public void setFirstCard(Card firstCard) {
		this.firstCard = firstCard;
	}

	public Card getFirstCard() {
		return firstCard;
	}

	public void setSecondCard(Card secondCard) {
		this.secondCard = secondCard;
	}

	public Card getSecondCard() {
		return secondCard;
	}

	/*
	 * public int getFirstNumber() { return firstNumber; }
	 * 
	 * public int getSecondNumber() { return secondNumber; }
	 */
	public Card[][] getCards() {
		return this.cards;
	}

	public void setSelectedCard(Card selectedCard) {
		this.selectedCard = selectedCard;
	}

	public Card getSelectedCard() {
		return selectedCard;
	}

	public int getCounter() {
		return counter;
	}

	public static String getGameMode() {
		return gameMode;
	}

	public static void setGameMode(String gameMode) {
		MemoryGame.gameMode = gameMode;
	}

	public static Board getBoardDimension() {
		return boardDimension;
	}

	public static void setBoardDimension(Board boardDimension) {
		MemoryGame.boardDimension = boardDimension;
	}

	public void buttonIsOpen() throws ButtonNotAvailableException {
		if (getSelectedCard().getState() == CardState.OPEN) {
			System.out.println("Card Already open: \'selected card\'");
			throw new ButtonNotAvailableException("You can't click on an open card!\n");
		}
	}

	public void nullifyButtonsIndex() {
		for (int i = 0; i < buttonsIndex.length; i++)
			buttonsIndex[i] = 0;
	}

	public void resetCounter() {
		counter = 0;
	}

	public String getWinnersName(int i) {
		String winner = player1.getScore() == i ? player1.getName() : player2.getName();
		return winner;
	}
}