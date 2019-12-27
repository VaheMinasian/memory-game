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

	public void removeMemoryButtons(Integer emojiNumber) {

		for (int i = 0; i < missedButtons.size(); i++) {
			if (missedButtons.get(i) == emojiNumber)
				missedButtons.set(i, -1);
//			missedButtonsOrdered.set(i, -1);
			System.out.println("removed button: " + emojiNumber);
		}
	}

	public void setMissedButtons(Integer firstNumber, Integer secondNumber) {// checked
		int i1, j1, i2, j2, x, y;
		int excludeLimit = 24;
		i1 = firstCard.getCardIndex()[0];
		j1 = firstCard.getCardIndex()[1];
		i2 = secondCard.getCardIndex()[0];
		j2 = secondCard.getCardIndex()[1];

		if (missedButtons.size() == excludeLimit) {
			missedButtons.remove(0);
			missedButtons.remove(0);
			missedButtons.remove(0);
			missedButtons.remove(0);
		}
		x=i1*boardDimension+j1;
		y=i2*boardDimension+j2;

		missedButtons.add(x);
		missedButtons.add(y);
		missedButtons.add(firstNumber);
		missedButtons.add(secondNumber);

		System.out.println("\n Current Player is: " + getActivePlayer().getName());
		for (int i = 0; i < missedButtons.size(); i+=4) {
			System.out.println("MissedButton 1 is: " + missedButtons.get(i));
			System.out.println("MissedButton 1 is: " + missedButtons.get(i+1));
			System.out.println("MissedButton firstNumber is: " + missedButtons.get(i+2));
			System.out.println("MissedButton secondNumber is: " + missedButtons.get(i+3));	
		}
	}

	public Card getRandomCardIndex(String difficulty, GameView view) {
		randomNo = new Random();
		int low = 1;
		int high = 100;
		int result = randomNo.nextInt(high - low) + low;
		int savedIcon;

		switch (difficulty) {
		case "n":
			do {
				cardIndexX = randomNo.nextInt(boardDimension);
				cardIndexY = randomNo.nextInt(boardDimension);
			} while (getCards()[cardIndexX][cardIndexY].getState() != CardState.CLOSED);
			System.out.println("returning true from gameModel.setRandomIndex cardIndexX & cardIndexY = (" + cardIndexX
					+ "," + cardIndexY + ")");
			break;
		case "e":
			if (temp == 1) {
				System.out.println("entering (temp == 1) easy : temp value is: " + temp);
				do {
					valid = true;
					cardIndexX = randomNo.nextInt(boardDimension);
					cardIndexY = randomNo.nextInt(boardDimension);
					tempIndexValue = cardIndexX * boardDimension + cardIndexY;
					System.out.println("\"tempIndexValue X, Y and value are: " + cardIndexX + " " + cardIndexY + " "
							+ tempIndexValue);
					subMissedButtons.clear();

					for (int i = 0; i < missedButtons.size(); i += 2) {
						for (int j = 0; j < 2; j++) {
							if (tempIndexValue == missedButtons.get(j)) {
								if (j % 2 == 0) {
									subMissedButtons.add(missedButtons.get(j + 1));
									System.out.println(
											"subMissedButtons.add(missedButtons.get(i+1));" + missedButtons.get(j + 1));
								} else if (j % 2 == 1) {
//								subMissedButtons.add(missedButtons.get(i - 1));
//								System.out.println("subMissedButtons.add(missedButtons.get(i-1));" + missedButtons.get(i - 1));
								}
							}
						}
					}
					for (int i = 0; i < subMissedButtons.size(); i++) {
						System.out.println("submissedButton nr." + i + " is " + subMissedButtons.get(i));
					}
					subMissedButtons.forEach((n) -> {
						System.out.println("inside foreach no match yet");
						if (savedCardNumber == n) {
							System.out.println(
									"savedCardNumber == n --> " + "savedCardNumber == " + savedCardNumber + " n==" + n);
							valid = false;
						}
					});

					System.out.println(getCards()[cardIndexX][cardIndexY].getState() + " " + valid);
				} while ((getCards()[cardIndexX][cardIndexY].getState() != CardState.CLOSED) || (!valid));
				temp--;
			} else if (temp == 0) {

				System.out.println("entering (temp == 0) : temp value is: " + temp);
				do {
					cardIndexX = randomNo.nextInt(boardDimension);
					cardIndexY = randomNo.nextInt(boardDimension);
				} while (getCards()[cardIndexX][cardIndexY].getState() != CardState.CLOSED);
				savedCardNumber = cardIndexX * boardDimension + cardIndexY;

				System.out.println("saved number index X, Y and value are: " + cardIndexX + " " + cardIndexY + " "
						+ savedCardNumber);
				System.out.println("returning true from gameModel.setRandomIndex cardIndexX & cardIndexY = ("
						+ cardIndexX + "," + cardIndexY + ")");
				temp++;
			}
			break;
		case "m":
			if (temp == 1) {
				System.out.println("entering (temp == 1) medium : temp value is: " + temp);
				do {
					valid = true;
					cardIndexX = randomNo.nextInt(boardDimension);
					cardIndexY = randomNo.nextInt(boardDimension);
					tempIndexValue = cardIndexX * boardDimension + cardIndexY;
					System.out.println("\"tempIndexValue X, Y and value are: " + cardIndexX + " " + cardIndexY + " "
							+ tempIndexValue);
					subMissedButtons.clear();

					for (int i = 0; i < missedButtons.size(); i++) {
						if (tempIndexValue == missedButtons.get(i)) {
							if (i % 2 == 0) {
								subMissedButtons.add(missedButtons.get(i + 1));
								System.out.println(
										"subMissedButtons.add(missedButtons.get(i+1));" + missedButtons.get(i + 1));
							} else if (i % 2 == 1) {
//								subMissedButtons.add(missedButtons.get(i - 1));
//								System.out.println("subMissedButtons.add(missedButtons.get(i-1));" + missedButtons.get(i - 1));
							}
						}
					}
					for (int i = 0; i < subMissedButtons.size(); i++) {
						System.out.println("submissedButton nr." + i + " is " + subMissedButtons.get(i));
					}
					subMissedButtons.forEach((n) -> {
						System.out.println("inside foreach no match yet");
						if (savedCardNumber == n) {
							System.out.println(
									"savedCardNumber == n --> " + "savedCardNumber == " + savedCardNumber + " n==" + n);
							valid = false;
						}
					});

					System.out.println(getCards()[cardIndexX][cardIndexY].getState() + " " + valid);
				} while ((getCards()[cardIndexX][cardIndexY].getState() != CardState.CLOSED) || (!valid));
				temp--;
			} else if (temp == 0) {

				System.out.println("entering (temp == 0) : temp value is: " + temp);
				do {
					cardIndexX = randomNo.nextInt(boardDimension);
					cardIndexY = randomNo.nextInt(boardDimension);
				} while (getCards()[cardIndexX][cardIndexY].getState() != CardState.CLOSED);
				savedCardNumber = cardIndexX * boardDimension + cardIndexY;

				System.out.println("saved number index X, Y and value are: " + cardIndexX + " " + cardIndexY + " "
						+ savedCardNumber);
				System.out.println("returning true from gameModel.setRandomIndex cardIndexX & cardIndexY = ("
						+ cardIndexX + "," + cardIndexY + ")");
				temp++;
			}
			break;
		case "h":
			if (temp == 1) {
				System.out.println("entering (temp == 1) hard : temp value is: " + temp);
				savedIcon = view.getCurrentIcons().get(savedCardNumber);
				System.out.println(savedIcon + " is the savedIcon.");
//				for (int i = 0; i < missedButtons.size(); i++) {
//					/*
//					 * if((missedButtons.get(i)==savedCardNumber)&&(missedButtons.get(i+2)==tempIcon
//					 * )){ System.out.println("missedButtons.get(i)==savedNumber is: "
//					 * +missedButtons.get(i) +"=="+savedCardNumber);
//					 * System.out.println("&& missedButtons.get(i+2)==tempIcon is: "
//					 * +missedButtons.get(i+2) +"=="+tempIcon);
//					 * if(getCards()[cardIndexX][cardIndexY].getState() != CardState.CLOSED) temp--;
//					 * return(getCards()[savedIndexX][savedIndexY]); }else
//					 */ if (i % 2 == 0) {
//						subMissedButtons.add(missedButtons.get(i + 1));
//						System.out.println("subMissedButtons.add(missedButtons.get(i+1));" + missedButtons.get(i + 1));
//					} else if (i % 2 == 1) {
////						subMissedButtons.add(missedButtons.get(i - 1));
////						System.out.println("subMissedButtons.add(missedButtons.get(i-1));" + missedButtons.get(i - 1));
//					}
//				}
				do {
					valid = true;
					cardIndexX = randomNo.nextInt(boardDimension);
					cardIndexY = randomNo.nextInt(boardDimension);
					tempIndexValue = cardIndexX * boardDimension + cardIndexY;

					System.out.println("\"tempIndexValue and it\'s X, Y values are: " + tempIndexValue + ", " + cardIndexX + ", " + cardIndexY);
					System.out.println("\"savedCardNumber and it\'s X, Y values are: " + savedCardNumber + ", " + savedIndexX + ", " + savedIndexY);
					subMissedButtons.clear();

						for (int i = 0; i < missedButtons.size(); i+=4) {
							for(int j=0; j<2; j++) {
								if (tempIndexValue == missedButtons.get(j)) {
									if (j % 2 == 0) {
										subMissedButtons.add(missedButtons.get(i + 1));
										System.out.println(
												"subMissedButtons.add(missedButtons.get(i+1));--> " + missedButtons.get(i + 1));
									} else if (j % 2 == 1) {
										subMissedButtons.add(missedButtons.get(i - 1));
										System.out.println("subMissedButtons.add(missedButtons.get(i-1));--> " + missedButtons.get(i - 1));
									}
								}								
							}
						}

					for (int i = 0; i < subMissedButtons.size(); i++) {
						System.out.println("submissedButton nr." + i + " is " + subMissedButtons.get(i));
					}

					subMissedButtons.forEach((n) -> {
						System.out.println("inside foreach no match yet");
						if (tempIndexValue == n) {
							System.out.println(
									"savedCardNumber == n --> " + "savedCardNumber == " + savedCardNumber + " n==" + n);
							valid = false;
						}
					});

					System.out.println(getCards()[cardIndexX][cardIndexY].getState() + " " + valid);
				} while ((getCards()[cardIndexX][cardIndexY].getState() != CardState.CLOSED) || (!valid));
				temp--;
				System.out.println("exiting temp==1 with temp value of " + temp);

			} else if (temp == 0) {

				System.out.println("entering (temp == 0) : temp value is: " + temp);
				do {
					cardIndexX = randomNo.nextInt(boardDimension);
					cardIndexY = randomNo.nextInt(boardDimension);
				} while (getCards()[cardIndexX][cardIndexY].getState() != CardState.CLOSED);
				savedCardNumber = cardIndexX * boardDimension + cardIndexY;
				savedIndexX = cardIndexX;
				savedIndexY = cardIndexY;
				
				System.out.println("returning true from gameModel.setRandomIndex. savedCardNumber & cardIndexX & cardIndexY = ("
						+ savedCardNumber + " - " + cardIndexX + "," + cardIndexY + ")");
				temp++;
				System.out.println("exiting temp==0 with temp value of " + temp);
			}
			break;
		default:
			break;
		}
		return (getCards()[cardIndexX][cardIndexY]);
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