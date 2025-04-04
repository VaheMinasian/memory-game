package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JOptionPane;

import control.ButtonNotAvailableException;
import model.Card.CardState;
import view.GameView;

public class MemoryGame implements Game {

//	private static MemoryGame Single_Instance = null;

	private int boardDimension;
	private static Player player1;
	private static Player player2;
	private Player activePlayer;
	private Card selectedCard;
	private Card firstCard = null, secondCard = null;
	private ArrayList<Card> cards = new ArrayList<>();
	private List<Integer> missedButtons = new ArrayList<>(); // checked
	private int savedCardNumber, tempIndexValue;
	private int cardIndex = 0;

	boolean valid;
	int cardsClicked = 0;
	private int counter = 0;
	private int wincheck = 0;
	Random randomNo;
	int low, high;
	int savedIcon;
	int excludeLimit = 36;
	int result;
	boolean firstRun = true;

	public void initializeParameters(ArrayList<String> profile) {
		player1 = new HumanPlayer(profile.get(2));
		if (profile.get(0).equals("c")) {
			player2 = new ComputerPlayer(profile.get(3));
		} else if (profile.get(0).equals("h")) {
			player2 = new HumanPlayer(profile.get(3));
		}
//		setGameMode(profile.get(0));
		boardDimension = Integer.parseInt(profile.get(1));
		this.activePlayer = player1;
		createCards(boardDimension * boardDimension);
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

	private void createCards(int size) {

		for (int i = 0; i < size; i++) {
			cards.add(new Card());
		}
	}

	public List<Integer> getMissedButtons() {
		return missedButtons;
	}

	// determining size of memory according to selected difficulty level.
	public void addToMemory(int firstNumber, int secondNumber, GameView view, String difficulty) {// checked

		if (firstRun) {
			firstRun = false;
			switch (difficulty) {
			case "4":
				excludeLimit = 8;
				break;
			case "3":
				excludeLimit = 16;
				break;
			case "2":
				excludeLimit = 24;
				break;
			default:
				excludeLimit = 40;
				break;
			}

		}

		int z1, z2, q1, q2;

		if (missedButtons.size() == excludeLimit) {
			for (int n = 0; n < 4; n++) {
				missedButtons.remove(0);
			}
		}

		z1 = cards.indexOf(firstCard);
		q1 = view.getCurrentIcons().get(z1);

		missedButtons.add(z1);
		missedButtons.add(q1);

		z2 = cards.indexOf(secondCard);
		q2 = view.getCurrentIcons().get(z2);

		missedButtons.add(z2);
		missedButtons.add(q2);

		if (firstNumber == secondNumber) {
			for (int i = 0; i < missedButtons.size(); i += 2) {
				if (z1 == missedButtons.get(i)) {
					missedButtons.set(i, -1);
					missedButtons.set(i + 1, -1);
				}
				if (z2 == missedButtons.get(i)) {
					missedButtons.set(i, -1);
					missedButtons.set(i + 1, -1);
				}
			}
		}
	}

	// calculate percentage of remaining closed cards related to number of total
	// board cells
	float getRemainingCardsNo() {
		float k = 0.f;
		for (int i = 0; i < cards.size(); i++) {
			if (cards.get(i).getState() == CardState.CLOSED)
				k = k + 1.0f;
		}
		float count = 100.0f * (k / (float) (boardDimension * boardDimension));
		return count;
	}

	// set lower limit of range according to difficulty below which randomly
	// generated number will be regarded as 'valid'.
	boolean getRandomProbability(int difficulty, float i, int index) {
		boolean value = true;
		high = 100;
		switch (difficulty) {
		case 4:
			low = 60 + (int) ((high - 60) * ((high - (int) i)) * (100 - index / 100)) / 100;
			break;
		case 3:
			low = 70 + (int) ((high - 70) * ((high - (int) i)) * (100 - index / 100)) / 100;
			break;
		case 2:
			low = 80 + (int) ((high - 80) * ((high - (int) i)) * (100 - index / 100)) / 100;
			break;
		default:
			low = 90 + (int) ((high - 90) * ((high - (int) i)) * (100 - index / 100)) / 100;
			break;
		}

		randomNo = new Random();
		result = randomNo.nextInt(high - 1) + 1;

		if (result >= low) {
			value = false;
		}
		return value;
	}

	public Card getRandomCardIndex(String difficulty, GameView view) {
		valid = false;
		randomNo = new Random();

		if (cardsClicked == 0) {

			// 4th level ... check if there is 2 matching buttons in missedButtons
			if (Integer.parseInt(difficulty) <= 2) {

				for (int i = 0; i < missedButtons.size() - 4; i += 2) {
					if ((missedButtons.get(missedButtons.size() - 1) != -1)
							&& (missedButtons.get(i + 1) == missedButtons.get(missedButtons.size() - 1))) {
						if (getRandomProbability(Integer.parseInt(difficulty), getRemainingCardsNo(), 0)) {
							savedCardNumber = missedButtons.get(missedButtons.size() - 2);
							cardsClicked++;
							return cards.get(savedCardNumber);
						}

					} else if ((missedButtons.get(missedButtons.size() - 3) != -1)
							&& (missedButtons.get(i + 1) == missedButtons.get(missedButtons.size() - 3))) {
						if (getRandomProbability(Integer.parseInt(difficulty), getRemainingCardsNo(), 0)) {
							savedCardNumber = missedButtons.get(missedButtons.size() - 4);
							cardsClicked++;
							return cards.get(savedCardNumber);
						}
					}
				}
			}

			// level 1 intelligence
			do {
				cardIndex = randomNo.nextInt(boardDimension * boardDimension);

			} while (cards.get(cardIndex).getState() != CardState.CLOSED);
			// end level 1 intelligence
			savedCardNumber = cardIndex;
			cardsClicked++;

		} else if (cardsClicked == 1) {

			savedIcon = view.getCurrentIcons().get(savedCardNumber);

			// 3rd level ... Check if first opened card has it's pair in memory
			// (missedButtons arraylist)
			for (int i = 0; i < missedButtons.size(); i += 2) {
				if ((savedIcon == missedButtons.get(i + 1)) && (savedCardNumber != missedButtons.get(i))) {
					if (getRandomProbability(Integer.parseInt(difficulty), getRemainingCardsNo(),
							missedButtons.size() - 2 - i)) {
						int tempNumber = missedButtons.get(i);
						cardsClicked--;
						return cards.get(tempNumber);
					}
				}
			}

			do {
				valid = true;

				// 1st level ... assign random card
				cardIndex = randomNo.nextInt(boardDimension * boardDimension);
				tempIndexValue = cardIndex;

				// 2nd level intelligence ... prevent previously missed pair in missedButtons
				// from being selected
				for (int i = (missedButtons.size() / 2) + 4; i < missedButtons.size() - 2; i += 2) {
					if (savedCardNumber == missedButtons.get(i)) {
						if ((i % 2 == 2) || (i % 4 == 0) && (tempIndexValue == missedButtons.get(i + 2))
								|| ((i + 2) % 4 == 0) && (tempIndexValue == missedButtons.get(i - 2))) {
							valid = false;
						}
					}
				} // end check

			} while ((cards.get(tempIndexValue).getState() != CardState.CLOSED) || (!valid));
			savedCardNumber = tempIndexValue;
			cardsClicked--;
		}

		return cards.get(savedCardNumber);
	}

	public void buttonIsOpen() throws ButtonNotAvailableException {
		if (selectedCard.getState() == CardState.OPEN) {
			throw new ButtonNotAvailableException("You can't click on an open card!\n");
		}
	}

	@Override
	public boolean move(int index) {
		if (counter == 0) {
			firstCard = cards.get(index);
			firstCard.updateCard(CardState.OPEN);
			counter++;
			return true;
		}

		else if ((counter == 1) && (!firstCard.equals(selectedCard))) {
			secondCard = cards.get(index);
			secondCard.updateCard(CardState.OPEN);
			counter--;
			return true;
		}
		return false;
	}

	@Override
	public Boolean getStatus(int firstNumber, int secondNumber) {

		if (firstNumber == secondNumber) {
			return true;
		} else {
			return false;
		}
	}

	public int getInterruptionMessage(String s, GameView view) {

		String[] options = { "Resume", "Main Menu", "Save & Quit" };
		int dialogReturnValue = -1;
		dialogReturnValue = JOptionPane.showOptionDialog(null, "            GAME PAUSED", "Memory",
				JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
		return dialogReturnValue;
	}

	@Override
	public int getMessage(String s) {

		int dialogBoxReturnValue = -1;

		String[] options = { "play", "Main Menu", "Quit" };
		wincheck = (s.equals("s")) ? 1 : 2;

		if (wincheck == 1) {
			dialogBoxReturnValue = JOptionPane.showOptionDialog(null,
					"Congratulations " + player1.getName() + ", you have won! \nYour socre is " + player1.getScore()
							+ " out of " + player1.getTries() + " tries.",
					"Memory", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

		} else if (wincheck == 2) {
			switch (player1.getScore() - player2.getScore()) {
			case 0:
				dialogBoxReturnValue = JOptionPane.showOptionDialog(null, "The game is drawn!", "Memory",
						JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
				break;
			default:
				int triesOfWinner = (player1.getScore() > boardDimension * boardDimension / 4) ? player1.getTries()
						: player2.getTries();
				int winnerScore = Math.max(player1.getScore(), player2.getScore());

				if (getWinnersName(winnerScore).equals("Computer")) {
					dialogBoxReturnValue = JOptionPane.showOptionDialog(null,
							"                   I won!\n  would you like to play again?", "Memory",
							JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
				} else {
					dialogBoxReturnValue = JOptionPane.showOptionDialog(null,
							"Congratulations " + getWinnersName(winnerScore) + ", you have won! \nYour socre is "
									+ winnerScore + " out of " + triesOfWinner + " tries.",
							"Memory", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options,
							options[0]);
				}
				break;
			}
		}
		return dialogBoxReturnValue;
	}

	// switches the current player
	public String switchActivePlayer() {
		if (activePlayer.equals(player2)) {
			player2.setActive(false);
			player1.setActive(true);
			setActivePlayer(player1);
			return "player1";
		} else if (activePlayer.equals(player1)) {
			player1.setActive(false);
			player2.setActive(true);
			setActivePlayer(player2);
			return "player2";
		}
		return "player2";
	}

	public void revoke() {
//		firstNumber = 0;
//		secondNumber = 0;
		getActivePlayer().incrementTries();
		setSelectedCard(null);

		// MemoryModel.getSelectedCard().updateCard(CardState.CLOSED);
		getFirstCard().updateCard(CardState.CLOSED);
		getSecondCard().updateCard(CardState.CLOSED);
	}

	public void update() {
//		firstNumber = 0;
//		secondNumber = 0;
//		setSelectedCard(null);
		getActivePlayer().incrementTries();
		getActivePlayer().incrementScore();

		// MemoryModel.getSelectedCard().updateCard(CardState.CLOSED);
		getFirstCard().updateCard(CardState.NONE);
		getSecondCard().updateCard(CardState.NONE);
	}

	public boolean gameOver() {

		for (int i = 0; i < cards.size(); i++) {
			if (cards.get(i).getState() != CardState.NONE) {
				return false;
			}
		}
		return true;
	}

	public String getWinnersName(int i) {
		String winner = player1.getScore() == i ? player1.getName() : player2.getName();
		return winner;
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

	public ArrayList<Card> getCards() {
		return this.cards;
	}

	public void setSelectedCard(Card selectedCard) {
		this.selectedCard = selectedCard;
	}

	public Card getSelectedCard() {
		return selectedCard;
	}

	public int getSavedCardNumber() {
		return savedCardNumber;
	}

	public void setSavedCardNumber(int savedCardNumber) {
		this.savedCardNumber = savedCardNumber;
	}

	public int getCardIndexX() {
		return cardIndex;
	}

	public void setCardIndexX(int cardIndexX) {
		this.cardIndex = cardIndexX;
	}

	public int getTempIndexValue() {
		return tempIndexValue;
	}

	public void setTempIndexValue(int tempIndexValue) {
		this.tempIndexValue = tempIndexValue;
	}

	public int getSavedIcon() {
		return savedIcon;
	}

	public void setSavedIcon(int savedIcon) {
		this.savedIcon = savedIcon;
	}
}