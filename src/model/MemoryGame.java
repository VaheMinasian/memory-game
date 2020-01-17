package model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;

import javax.swing.JOptionPane;

import control.ButtonNotAvailableException;
import model.Card.CardState;
import view.GameView;

public class MemoryGame implements Game {

//	private static MemoryGame Single_Instance = null;

	private Card[][] cards;
	private Card selectedCard;
	private int boardDimension;
	private static Player player1;
	private static Player player2;
	private Player activePlayer;
	private Card firstCard = null, secondCard = null;
	private List<Integer> missedButtons = new ArrayList<>(); // checked
	private List<Integer> subMissedButtons = new ArrayList<>();
	LinkedHashMap<Integer, Integer> missedIndexes = new LinkedHashMap<>();

	private int savedCardNumber, tempIndexValue;
	private int cardIndexX = 0, cardIndexY = 0, savedIndexX, savedIndexY;
	boolean valid = true;

	int temp = 0;
	private int counter = 0;
	private int wincheck = 0;
	Random randomNo;
	int guessHigherPoint = 0;
	int difficultyDepth = 0;
	int depthChangeScale = 0;
	int low, high;
	int savedIcon1, savedIcon2;
	int excludeLimit = 36;
	int result;
	int intDifficulty;

//	private static String gameMode;
//	private static int[] buttonsIndex = new int[4];

	public MemoryGame() {

	}

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
		createCards(boardDimension);
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

	private void createCards(int dimension) {
		cards = new Card[dimension][dimension];
		for (int i = 0; i < dimension; i++) {
			for (int j = 0; j < dimension; j++) {

				cards[i][j] = new Card(i, j);
			}
		}
	}

	public List<Integer> getMissedButtons() {
		return missedButtons;
	}

//	public void setMemorySize(String difficulty) { // Checked
//		switch (difficulty) {
//		case "e":
//		case "m":
//			/*
//			 * for (int i = 0; i < boardDimension * boardDimension * 2; i++) {
//			 * missedButtons.add(0); }
//			 */
//			break;
//		case "h":
//			for (int i = 0; i < boardDimension * boardDimension * 2; i++) {
//				missedButtons.add(0);
//			}
//			break;
//		default:
//			break;
//		}
//		System.out.println(missedButtons.size());
//
//		// list.add(0)); list.set(9, 51); this.missedButtons = i*i;
//	}

	public List<Integer> getMissedButtonsOrdered() {
		return subMissedButtons;
	}

//	public void removeMemoryButtons(Integer emojiNumber) {
//
//		for (int i = 0; i < missedButtons.size(); i++) {
//			if (missedButtons.get(i) == emojiNumber)
//				missedButtons.set(i, -1);
////			missedButtonsOrdered.set(i, -1);
//			System.out.println("removed button: " + emojiNumber);
//		}
//	}
//
//	// remove last pair from memory if matched
//	public void removeFromMemory() {
//		missedButtons.remove(missedButtons.size() - 1);
//		missedButtons.remove(missedButtons.size() - 1);
//		missedButtons.remove(missedButtons.size() - 1);
//	}

	// determining size of memory according to selected difficulty level.
	public void addToMemory(int firstNumber, int secondNumber, GameView view, String difficulty) {// checked

		switch (difficulty) {
		case "4":
			excludeLimit = 16;
			break;
		case "3":
			excludeLimit = 32;
			break;
		case "2":
			excludeLimit = 48;
			break;
		default:
			break;
		}

		int x1, x2, y1, y2, z1, z2, q1, q2;

		if (missedButtons.size() == excludeLimit) {
			for (int n = 0; n < 8; n++) {
				missedButtons.remove(0);
			}
			System.out.println("limit excluded removed 8 items from missedButtons.");
		}
		// end determining

		x1 = firstCard.getCardIndex()[0];
		y1 = firstCard.getCardIndex()[1];
		z1 = x1 * boardDimension + y1;
		q1 = view.getCurrentIcons().get(z1);

		missedButtons.add(z1);
		missedButtons.add(q1);
		missedButtons.add(x1);
		missedButtons.add(y1);

		x2 = secondCard.getCardIndex()[0];
		y2 = secondCard.getCardIndex()[1];
		z2 = x2 * boardDimension + y2;
		q2 = view.getCurrentIcons().get(z2);

		missedButtons.add(z2);
		missedButtons.add(q2);
		missedButtons.add(x2);
		missedButtons.add(y2);

		if (firstNumber == secondNumber) {
			for (int i = 0; i < missedButtons.size(); i += 4) {
				if (z1 == missedButtons.get(i)) {
					missedButtons.set(i, -1);
					missedButtons.set(i + 1, -1);
					missedButtons.set(i + 2, -1);
					missedButtons.set(i + 3, -1);

				}
				if (z2 == missedButtons.get(i)) {

					missedButtons.set(i, -1);
					missedButtons.set(i + 1, -1);
					missedButtons.set(i + 2, -1);
					missedButtons.set(i + 3, -1);
				}
			}
		}

		for (int n = 0; n < missedButtons.size(); n += 8) {
			System.out.println("MissedButtons " + n + " " + missedButtons.get(n));
			System.out.println("MissedButtons " + (n + 1) + " " + missedButtons.get(n + 1));
			System.out.println("MissedButtons " + (n + 2) + " " + missedButtons.get(n + 2));
			System.out.println("MissedButtons " + (n + 3) + " " + missedButtons.get(n + 3));
			System.out.println("MissedButtons " + (n + 4) + " " + missedButtons.get(n + 4));
			System.out.println("MissedButtons " + (n + 5) + " " + missedButtons.get(n + 5));
			System.out.println("MissedButtons " + (n + 6) + " " + missedButtons.get(n + 6));
			System.out.println("MissedButtons " + (n + 7) + " " + missedButtons.get(n + 7));
		}
	}

	// calculate percentage of remaining closed cards related to number of total
	// board cells
	float getRemainingCardsNo() {
		float k = 0.f;
		for (int i = 0; i < getCards().length; i++) {
			for (int j = 0; j < getCards().length; j++) {
				if (getCards()[i][j].getState() == CardState.CLOSED)
					k = k + 1.0f;
			}
		}
		float count = 100.0f * (k / (float) (boardDimension * boardDimension));
		System.out.println("k = " + k);
		System.out.println("count is: " + count);
		return count;
	}

	// set lower limit of range according to difficulty below which randomly
	// generated number will be regarded as 'valid'.
	boolean getRandomProbability(int difficulty, float i) {
		boolean value = true;
		high = 100;
		System.out.println("i value is " + (int) i);
		switch (difficulty) {
		case 4:
			low = 60 + (int) ((100 - 60) * ((100 - (int) i))) / 100;
			break;
		case 3:
			low = 70 + (int) ((100 - 70) * ((100 - (int) i))) / 100;
			break;
		case 2:
			low = 80 + (int) ((100 - 80) * ((100 - (int) i))) / 100;
			break;
		default:
			low = 90 + (int) ((100 - 90) * ((100 - (int) i))) / 100;
			break;
		}

		randomNo = new Random();
		result = randomNo.nextInt(100 - 1) + 1;

		if (result >= low) {
			value = false;
			System.out.println("V A L U E   I S    M I S S E D !!!!!");
			System.out.println("random result is: " + result);
			System.out.println("low value is: " + low);
		} else {
			System.out.println("random result is: " + result);
			System.out.println("low value is: " + low);
		}
		return value;
	}

	public Card getRandomCardIndex(String difficulty, GameView view) {
		intDifficulty= Integer.parseInt(difficulty);
		
		randomNo = new Random();
		guessHigherPoint = 95;
		difficultyDepth = 10;
		depthChangeScale = 8;

		switch (difficulty) {
		case "4":
			do {
				cardIndexX = randomNo.nextInt(boardDimension);
				cardIndexY = randomNo.nextInt(boardDimension);
			} while (getCards()[cardIndexX][cardIndexY].getState() != CardState.CLOSED);
			System.out.println("returning true from gameModel.setRandomIndex cardIndexX & cardIndexY = (" + cardIndexX
					+ "," + cardIndexY + ")");
			break;
		case "3":

			if (temp == 1) {
				System.out.println("entering (temp == 1) easy : temp value is: " + temp);
				savedIcon1 = view.getCurrentIcons().get(savedCardNumber);
				System.out.println(savedIcon1 + " is the savedIcon.");

				do {
					valid = true;

					// 1st level ... assign random card
					cardIndexX = randomNo.nextInt(boardDimension);
					cardIndexY = randomNo.nextInt(boardDimension);
					tempIndexValue = cardIndexX * boardDimension + cardIndexY;

					System.out.println("\"tempIndexValue and it\'s X, Y values are: " + tempIndexValue + ", "
							+ cardIndexX + ", " + cardIndexY);
					System.out.println("\"savedCardNumber and it\'s X, Y values are: " + savedCardNumber + ", "
							+ savedIndexX + ", " + savedIndexY);
//					subMissedButtons.clear();

					// 2nd level ... check that a previous pair from missedButtons is not selected
					for (int i = 0; i < missedButtons.size() - 4; i += 4) {
						if (savedCardNumber == missedButtons.get(i)) {
							if ((i % 2 == 4 || i % 4 == 0) && (tempIndexValue == missedButtons.get(i + 4))
									|| (i % 4 == 2) && (tempIndexValue == missedButtons.get(i - 4))) {
								valid = false;
							}
						}
					} // end check

					// 3rd level ... Check if first opened card has it's pair in memory
					// (missedButtons arraylist)
					for (int i = 0; i < missedButtons.size(); i += 4) {
						System.out.println("I is equal to: " + i);
						System.out.println(missedButtons.get(i));
						if ((savedIcon1 == missedButtons.get(i + 1)) && (savedCardNumber != missedButtons.get(i))) {
							System.out.println("saved Icon is equal to " + missedButtons.get(i));
							if (getRandomProbability(3, getRemainingCardsNo())) {
								cardIndexX = missedButtons.get(i + 2);
								cardIndexY = missedButtons.get(i + 3);
								valid = true;
								break;
							}
						}
					}

					System.out.println(getCards()[cardIndexX][cardIndexY].getState() + " " + valid);
				} while ((getCards()[cardIndexX][cardIndexY].getState() != CardState.CLOSED) || (!valid));
				temp--;
				System.out.println("exiting temp==1 with temp value of " + temp);

			} else if (temp == 0) {

				System.out.println("entering (temp == 0) : temp value is: " + temp);
				
				// 4th level ... check if there is 2 matching buttons in missedButtons
				for (int i = 0; i < missedButtons.size() - 8; i += 4) {
					if ( (missedButtons.get(missedButtons.size() - 3) !=  -1) && (missedButtons.get(i + 1) == missedButtons.get(missedButtons.size() - 3)) ) 
					{
							cardIndexX = missedButtons.get(missedButtons.size() - 2);
							cardIndexY = missedButtons.get(missedButtons.size() - 1);
							savedCardNumber = cardIndexX * boardDimension + cardIndexY;
							savedIndexX = cardIndexX;
							savedIndexY = cardIndexY;
							temp++;
							System.out.println("cardIndexX is: " +cardIndexX + "cardIndexY is: " + cardIndexY);
							return getCards()[cardIndexX][cardIndexY];
					} else if ( (missedButtons.get(missedButtons.size() - 7) !=  -1) && (missedButtons.get(i + 1) == missedButtons.get(missedButtons.size() - 7)) ) 
						{
								cardIndexX = missedButtons.get(missedButtons.size() - 6);
								cardIndexY = missedButtons.get(missedButtons.size() - 5);
								savedCardNumber = cardIndexX * boardDimension + cardIndexY;
								savedIndexX = cardIndexX;
								savedIndexY = cardIndexY;
								temp++;
								System.out.println("cardIndexX is: " +cardIndexX + "cardIndexY is: " + cardIndexY);
								return getCards()[cardIndexX][cardIndexY];
						}
				}
				
				do {
					cardIndexX = randomNo.nextInt(boardDimension);
					cardIndexY = randomNo.nextInt(boardDimension);
				} while (getCards()[cardIndexX][cardIndexY].getState() != CardState.CLOSED);
				savedCardNumber = cardIndexX * boardDimension + cardIndexY;
				savedIndexX = cardIndexX;
				savedIndexY = cardIndexY;

				System.out.println(
						"returning true from gameModel.setRandomIndex. savedCardNumber & cardIndexX & cardIndexY = ("
								+ savedCardNumber + " - " + cardIndexX + "," + cardIndexY + ")");
				temp++;
				System.out.println("exiting temp==0 with temp value of " + temp);
			}
			break;
		case "2":

			if (temp == 1) {
				System.out.println("entering (temp == 1) hard : temp value is: " + temp);
				savedIcon1 = view.getCurrentIcons().get(savedCardNumber);
				System.out.println(savedIcon1 + " is the savedIcon.");

				do {
					valid = true;

					// 1st level ... assign random card
					cardIndexX = randomNo.nextInt(boardDimension);
					cardIndexY = randomNo.nextInt(boardDimension);
					tempIndexValue = cardIndexX * boardDimension + cardIndexY;

					System.out.println("\"tempIndexValue and it\'s X, Y values are: " + tempIndexValue + ", "
							+ cardIndexX + ", " + cardIndexY);
					System.out.println("\"savedCardNumber and it\'s X, Y values are: " + savedCardNumber + ", "
							+ savedIndexX + ", " + savedIndexY);
//					subMissedButtons.clear();

					// 2nd level ... check that a previous pair from missedButtons is not selected
					for (int i = 0; i < missedButtons.size() - 4; i += 4) {
						if (savedCardNumber == missedButtons.get(i)) {
							if ((i % 2 == 4 || i % 4 == 0) && (tempIndexValue == missedButtons.get(i + 4))
									|| (i % 4 == 2) && (tempIndexValue == missedButtons.get(i - 4))) {
								valid = false;
							}
						}
					} // end check

					// 3rd level ... Check if first opened card has it's pair in memory
					// (missedButtons arraylist)
					for (int i = 0; i < missedButtons.size(); i += 4) {
						System.out.println("I is equal to: " + i);
						System.out.println(missedButtons.get(i));
						if ((savedIcon1 == missedButtons.get(i + 1)) && (savedCardNumber != missedButtons.get(i))) {
							System.out.println("saved Icon is equal to " + missedButtons.get(i));
							if (getRandomProbability(2, getRemainingCardsNo())) {
								cardIndexX = missedButtons.get(i + 2);
								cardIndexY = missedButtons.get(i + 3);
								valid = true;
								break;
							}
						}
					}

					System.out.println(getCards()[cardIndexX][cardIndexY].getState() + " " + valid);
				} while ((getCards()[cardIndexX][cardIndexY].getState() != CardState.CLOSED) || (!valid));
				temp--;
				System.out.println("exiting temp==1 with temp value of " + temp);

			} else if (temp == 0) {

				System.out.println("entering (temp == 0) : temp value is: " + temp);
				
				// 4th level ... check if there is 2 matching buttons in missedButtons
				for (int i = 0; i < missedButtons.size() - 8; i += 4) {
					if ( (missedButtons.get(missedButtons.size() - 3) !=  -1) && (missedButtons.get(i + 1) == missedButtons.get(missedButtons.size() - 3)) ) 
					{
							cardIndexX = missedButtons.get(missedButtons.size() - 2);
							cardIndexY = missedButtons.get(missedButtons.size() - 1);
							savedCardNumber = cardIndexX * boardDimension + cardIndexY;
							savedIndexX = cardIndexX;
							savedIndexY = cardIndexY;
							temp++;
							System.out.println("cardIndexX is: " +cardIndexX + "cardIndexY is: " + cardIndexY);
							return getCards()[cardIndexX][cardIndexY];
					} else if ( (missedButtons.get(missedButtons.size() - 7) !=  -1) && (missedButtons.get(i + 1) == missedButtons.get(missedButtons.size() - 7)) ) 
						{
								cardIndexX = missedButtons.get(missedButtons.size() - 6);
								cardIndexY = missedButtons.get(missedButtons.size() - 5);
								savedCardNumber = cardIndexX * boardDimension + cardIndexY;
								savedIndexX = cardIndexX;
								savedIndexY = cardIndexY;
								temp++;
								System.out.println("cardIndexX is: " +cardIndexX + "cardIndexY is: " + cardIndexY);
								return getCards()[cardIndexX][cardIndexY];
						}
				}
				
				do {
					cardIndexX = randomNo.nextInt(boardDimension);
					cardIndexY = randomNo.nextInt(boardDimension);
				} while (getCards()[cardIndexX][cardIndexY].getState() != CardState.CLOSED);
				savedCardNumber = cardIndexX * boardDimension + cardIndexY;
				savedIndexX = cardIndexX;
				savedIndexY = cardIndexY;

				System.out.println(
						"returning true from gameModel.setRandomIndex. savedCardNumber & cardIndexX & cardIndexY = ("
								+ savedCardNumber + " - " + cardIndexX + "," + cardIndexY + ")");
				temp++;
				System.out.println("exiting temp==0 with temp value of " + temp);
			}
			break;
		case "1":

			if (temp == 1) {
				System.out.println("entering (temp == 1) hard : temp value is: " + temp);
				savedIcon1 = view.getCurrentIcons().get(savedCardNumber);
				System.out.println(savedIcon1 + " is the savedIcon.");

				do {
					valid = true;

					// 1st level ... assign random card
					cardIndexX = randomNo.nextInt(boardDimension);
					cardIndexY = randomNo.nextInt(boardDimension);
					tempIndexValue = cardIndexX * boardDimension + cardIndexY;

					System.out.println("\"tempIndexValue and it\'s X, Y values are: " + tempIndexValue + ", "
							+ cardIndexX + ", " + cardIndexY);
					System.out.println("\"savedCardNumber and it\'s X, Y values are: " + savedCardNumber + ", "
							+ savedIndexX + ", " + savedIndexY);
//					subMissedButtons.clear();

					// 2nd level ... check that a previous pair from missedButtons is not selected
					for (int i = 0; i < missedButtons.size() - 4; i += 4) {
						if (savedCardNumber == missedButtons.get(i)) {
							if ((i % 2 == 4 || i % 4 == 0) && (tempIndexValue == missedButtons.get(i + 4))
									|| (i % 4 == 2) && (tempIndexValue == missedButtons.get(i - 4))) {
								valid = false;
							}
						}
					} // end check

					// 3rd level ... Check if first opened card has it's pair in memory
					// (missedButtons arraylist)
					for (int i = 0; i < missedButtons.size(); i += 4) {
						System.out.println("I is equal to: " + i);
						System.out.println(missedButtons.get(i));
						if ((savedIcon1 == missedButtons.get(i + 1)) && (savedCardNumber != missedButtons.get(i))) {
							System.out.println("saved Icon is equal to " + missedButtons.get(i));
							if (getRandomProbability(1, getRemainingCardsNo())) {
								cardIndexX = missedButtons.get(i + 2);
								cardIndexY = missedButtons.get(i + 3);
								valid = true;
								break;
							}
						}
					}

					System.out.println(getCards()[cardIndexX][cardIndexY].getState() + " " + valid);
				} while ((getCards()[cardIndexX][cardIndexY].getState() != CardState.CLOSED) || (!valid));
				temp--;
				System.out.println("exiting temp==1 with temp value of " + temp);

			} else if (temp == 0) {

				System.out.println("entering (temp == 0) : temp value is: " + temp);

				// 4th level ... check if there is 2 matching buttons in missedButtons
				for (int i = 0; i < missedButtons.size() - 8; i += 4) {
					if ( (missedButtons.get(missedButtons.size() - 3) !=  -1) && (missedButtons.get(i + 1) == missedButtons.get(missedButtons.size() - 3)) ) 
					{
							cardIndexX = missedButtons.get(missedButtons.size() - 2);
							cardIndexY = missedButtons.get(missedButtons.size() - 1);
							savedCardNumber = cardIndexX * boardDimension + cardIndexY;
							savedIndexX = cardIndexX;
							savedIndexY = cardIndexY;
							temp++;
							System.out.println("cardIndexX is: " +cardIndexX + "cardIndexY is: " + cardIndexY);
							return getCards()[cardIndexX][cardIndexY];
					} else if ( (missedButtons.get(missedButtons.size() - 7) !=  -1) && (missedButtons.get(i + 1) == missedButtons.get(missedButtons.size() - 7)) ) 
						{
								cardIndexX = missedButtons.get(missedButtons.size() - 6);
								cardIndexY = missedButtons.get(missedButtons.size() - 5);
								savedCardNumber = cardIndexX * boardDimension + cardIndexY;
								savedIndexX = cardIndexX;
								savedIndexY = cardIndexY;
								temp++;
								System.out.println("cardIndexX is: " +cardIndexX + "cardIndexY is: " + cardIndexY);
								return getCards()[cardIndexX][cardIndexY];
						}
				}

				do {
					cardIndexX = randomNo.nextInt(boardDimension);
					cardIndexY = randomNo.nextInt(boardDimension);
				} while (getCards()[cardIndexX][cardIndexY].getState() != CardState.CLOSED);
				savedCardNumber = cardIndexX * boardDimension + cardIndexY;
				savedIndexX = cardIndexX;
				savedIndexY = cardIndexY;

				System.out.println(
						"returning true from gameModel.setRandomIndex. savedCardNumber & cardIndexX & cardIndexY = ("
								+ savedCardNumber + " - " + cardIndexX + "," + cardIndexY + ")");
				temp++;
				System.out.println("exiting temp==0 with temp value of " + temp);
			}
			break;
		default:
			break;
		}
		return getCards()[cardIndexX][cardIndexY];
	}

	public void buttonIsOpen() throws ButtonNotAvailableException {
		if (selectedCard.getState() == CardState.OPEN) {
			System.out.println("Card Already open: \'selected card\'");
			throw new ButtonNotAvailableException("You can't click on an open card!\n");
		}
	}

	@Override
	public boolean move(int i, int j) {
		if (counter == 0) {
			firstCard = cards[i][j];
			firstCard.updateCard(CardState.OPEN);
			counter++;
			return true;
		}

		else if ((counter == 1) && (!firstCard.equals(selectedCard))) {
			secondCard = cards[i][j];
			secondCard.updateCard(CardState.OPEN);
			counter--;
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
		int dialogBoxReturnValue = -1;

		String[] options = { "play", "Main Menu", "Quit" };
		wincheck = (s.equals("s")) ? 1 : 2;

		if (wincheck == 1) {
			dialogBoxReturnValue = JOptionPane.showOptionDialog(null,
					"Congratulations " + player1.getName() + ", you have won! \nYour socre is " + player1.getScore()
							+ " out of " + player1.getTries() + " moves.",
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

				dialogBoxReturnValue = JOptionPane.showOptionDialog(null,
						"Congratulations " + getWinnersName(winnerScore) + ", you have won! \nYour socre is "
								+ winnerScore + " out of " + triesOfWinner + " moves.",
						"Memory", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options,
						options[0]);
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
			System.out.println("\nactive player is: " + getActivePlayer().getName());
			return "player1";
		} else if (activePlayer.equals(player1)) {
			player1.setActive(false);
			player2.setActive(true);
			setActivePlayer(player2);
			System.out.println("\nactive player is: " + getActivePlayer().getName());
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
		System.out.println("inside mismatch of revoke()");
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
		System.out.println("inside MATCH of update()\n");
	}

	public boolean gameOver() {

		for (int i = 0; i < cards.length; i++) {
			for (int j = 0; j < cards[i].length; j++) {
				if (cards[i][j].getState() != CardState.NONE) {
					System.out.println("will return false from gameOver");
					return false;
				}
			}
		}
		System.out.println("will return true from gameOver");
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

	public Card[][] getCards() {
		return this.cards;
	}

	public void setSelectedCard(Card selectedCard) {
		this.selectedCard = selectedCard;
	}

	public Card getSelectedCard() {
		return selectedCard;
	}
}

//if (missedButtons.get(cardIndexX * boardDimension + cardIndexY) > 0) {
//	tempValue = missedButtons.get(cardIndexX * boardDimension + cardIndexY);
////	if(cardIndexX * boardDimension+ cardIndexY % 2 == 1) {
//	for (int i = 0; i < missedButtonsOrdered.size(); i++) {
//		if (missedButtonsOrdered.get(i) == tempValue) {
//			indexArray.add(missedButtonsOrdered.indexOf(tempValue));
//			for (int j = 0; j < indexArray.size(); j++) {
//				if (indexArray.get(j) % 2 == 1) {
//					if (missedButtonsOrdered.get(
//							indexArray.get(j) - 1) == savedIndexX * boardDimension + savedIndexY) {
//						valid = false;
//					}
//				} else if (indexArray.get(j) % 2 == 0) {
//					if (missedButtonsOrdered.get(
//							indexArray.get(j) + 1) == savedIndexX * boardDimension + savedIndexY) {
//						valid = false;
//					}
//				}
//			}
//		}
//	}
//}