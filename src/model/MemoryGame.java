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

	private ArrayList<Card> cards = new ArrayList<>();
	private Card selectedCard;
	private int boardDimension;
	private static Player player1;
	private static Player player2;
	private Player activePlayer;
	private Card firstCard = null, secondCard = null;
	private List<Integer> missedButtons = new ArrayList<>(); // checked
	private int savedCardNumber, tempIndexValue;
	private int cardIndexX = 0, cardIndexY = 0, savedIndexX, savedIndexY;
	
	
	boolean valid;

	int temp = 0;
	private int counter = 0;
	private int wincheck = 0;
	Random randomNo;
	int low, high;
	int savedIcon;
	int excludeLimit = 36;
	int result;

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
		createCards(boardDimension*boardDimension);
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
		
		for (int i = 0; i < dimension; i++) {
				cards.add(new Card());
		}
	}

	public List<Integer> getMissedButtons() {
		return missedButtons;
	}

	
	
	// determining size of memory according to selected difficulty level.
	public void addToMemory(int firstNumber, int secondNumber, GameView view, String difficulty) {// checked
		boolean firstRun=true;
		if(firstRun) {
			System.out.println("firstrun is: " + firstRun);
			firstRun=false;
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
				excludeLimit = 80;
				break;
			}
			
		}

		int z1, z2, q1, q2;

		if (missedButtons.size() == excludeLimit) {
			for (int n = 0; n < 8; n++) {
				missedButtons.remove(0);
			}
			System.out.println("limit excluded removed 8 items from missedButtons.");
		}

		z1 = cards.indexOf(firstCard);
		q1 = view.getCurrentIcons().get(z1);

		missedButtons.add(z1);
		missedButtons.add(q1);
//		missedButtons.add(x1);
//		missedButtons.add(y1);

		z2 = cards.indexOf(secondCard);
		q2 = view.getCurrentIcons().get(z2);

		missedButtons.add(z2);
		missedButtons.add(q2);
//		missedButtons.add(x2);
//		missedButtons.add(y2);

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
		for (int i = 0; i < cards.size(); i++) {
				if (cards.get(i).getState() == CardState.CLOSED)
					k = k + 1.0f;
		}
		float count = 100.0f * (k / (float) (boardDimension * boardDimension));
		System.out.println("k = " + k);
		System.out.println("count is: " + count);
		return count;
	}

	// set lower limit of range according to difficulty below which randomly
	// generated number will be regarded as 'valid'.
	boolean getRandomProbability(int difficulty, float i, int index) {
		boolean value = true;
		high = 100;
		System.out.println("i value is " + (int) i);
		switch (difficulty) {
		case 4:
			low = 60 + (int) ((high - 60) * ((high - (int) i))*(100-index/100)) / 100;
			System.out.println("low value for case 4 is reduced by: " + (100-index) +"%" );
			break;
		case 3:
			low = 70 + (int) ((high - 70) * ((high - (int) i))*(100-index/100)) / 100;
			System.out.println("low value for case 3 is reduced by: " + (100-index) +"%" );

			break;
		case 2:
			low = 80 + (int) ((high - 80) * ((high - (int) i))*(100-index/100)) / 100;
			System.out.println("low value for case 2 is reduced by: " + (100-index) +"%" );

			break;
		default:
			low = 90 + (int) ((high - 90) * ((high - (int) i))*(100-index/100)) / 100;
			System.out.println("low value for case 1 is reduced by: " + (100-index) +"%" );

			break;
		}
		
		

		randomNo = new Random();
		result = randomNo.nextInt(high - 1) + 1;

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
		valid=false;
		randomNo = new Random();

		if(temp==0) {
			
			System.out.println("entering (temp == 0) : temp value is: " + temp);

			// 4th level ... check if there is 2 matching buttons in missedButtons
			if(Integer.parseInt(difficulty)<=2) {
				
				for (int i = 0; i < missedButtons.size() - 8; i += 4) {
					if ( (missedButtons.get(missedButtons.size() - 3) !=  -1) && (missedButtons.get(i + 1) == missedButtons.get(missedButtons.size() - 3)) ) 
					{
						if (getRandomProbability(Integer.parseInt(difficulty), getRemainingCardsNo(),0)) {
							cardIndexX = missedButtons.get(missedButtons.size() - 2);
							cardIndexY = missedButtons.get(missedButtons.size() - 1);
							savedCardNumber = cardIndexX * boardDimension + cardIndexY;
							savedIndexX = cardIndexX;
							savedIndexY = cardIndexY;
							temp++;
							System.out.println("cardIndexX is: " +cardIndexX + "cardIndexY is: " + cardIndexY);
							return cards.get(missedButtons.get(missedButtons.size()-1));
						}
					
					} else if ( (missedButtons.get(missedButtons.size() - 7) !=  -1) && (missedButtons.get(i + 1) == missedButtons.get(missedButtons.size() - 7)) ) 
					{
						if (getRandomProbability(Integer.parseInt(difficulty), getRemainingCardsNo(),0)) {
							cardIndexX = missedButtons.get(missedButtons.size() - 6);
							cardIndexY = missedButtons.get(missedButtons.size() - 5);
							savedCardNumber = cardIndexX * boardDimension + cardIndexY;
							savedIndexX = cardIndexX;
							savedIndexY = cardIndexY;
							temp++;
							System.out.println("cardIndexX is: " +cardIndexX + "cardIndexY is: " + cardIndexY);
							return cards.get(missedButtons.get(missedButtons.size()-3));							
						}
					}
				}
			}

			//level 1 intelligence
			do {
				cardIndexX = randomNo.nextInt(boardDimension);
				cardIndexY = randomNo.nextInt(boardDimension);
			} while (cards.get(cardIndexX*boardDimension + cardIndexY).getState() != CardState.CLOSED);
			//end level 1 intelligence
			savedCardNumber = cardIndexX * boardDimension + cardIndexY;
			savedIndexX = cardIndexX;
			savedIndexY = cardIndexY;

			System.out.println(
					"returning true from gameModel.setRandomIndex. savedCardNumber & cardIndexX & cardIndexY = ("
							+ savedCardNumber + " - " + cardIndexX + "," + cardIndexY + ")");
			temp++;
			System.out.println("exiting temp==0 with temp value of " + temp);
			
		} else if (temp==1) {
			
			System.out.println("entering (temp == 1) hard : temp value is: " + temp);
			savedIcon = view.getCurrentIcons().get(savedCardNumber);
			System.out.println(savedIcon + " is the savedIcon.");

			// 3rd level ... Check if first opened card has it's pair in memory
			// (missedButtons arraylist)
			for (int i = 0; i < missedButtons.size(); i += 4) {
				System.out.println("I is equal to: " + i);
				System.out.println(missedButtons.get(i));
				if ((savedIcon == missedButtons.get(i + 1)) && (savedCardNumber != missedButtons.get(i))) {
					System.out.println("saved Icon is equal to " + missedButtons.get(i));
					System.out.println("index = " + (missedButtons.size()-4-i));
					if (getRandomProbability(Integer.parseInt(difficulty), getRemainingCardsNo(), missedButtons.size()-4-i)) {
						
						cardIndexX = missedButtons.get(i + 2);
						cardIndexY = missedButtons.get(i + 3);
						temp--;
						return cards.get(cardIndexX*boardDimension+cardIndexY);
					}
				}
			}
			
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

				// 2nd level intelligence ... prevent previously missed pair in  missedButtons from being selected
				for (int i = (missedButtons.size()/2)+8; i < missedButtons.size() - 4; i += 4) {
					if (savedCardNumber == missedButtons.get(i)) {
						if ((i % 2 == 4 || i % 4 == 0) && (tempIndexValue == missedButtons.get(i + 4))
								|| (i % 4 == 2) && (tempIndexValue == missedButtons.get(i - 4))) {
							valid = false;
						}
					}
				} // end check
			
				System.out.println(cards.get(cardIndexX*boardDimension+cardIndexY).getState() + " " + valid);
			} while ((cards.get(cardIndexX*boardDimension+cardIndexY).getState() != CardState.CLOSED) || (!valid));
			temp--;
			System.out.println("exiting temp==1 with temp value of " + temp);
		}
		
		return cards.get(cardIndexX*boardDimension+cardIndexY);
	}

	public void buttonIsOpen() throws ButtonNotAvailableException {
		if (selectedCard.getState() == CardState.OPEN) {
			System.out.println("Card Already open: \'selected card\'");
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
	public Boolean getStatus(int x, int y) {

		if (x == y) {
			return true;
		} else {
			return false;
		}
	}

	public int getInterruptionMessage(String s, GameView view) {
		
		String[] options = { "Resume", "Main Menu", "Save & Quit" };
		int dialogReturnValue=-1;
		dialogReturnValue = JOptionPane.showOptionDialog(null, "            GAME PAUSED", "Memory", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
		
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

				if(getWinnersName(winnerScore).equals("Computer")){
					dialogBoxReturnValue = JOptionPane.showOptionDialog(null,
							"                   I won!\n  would you like to play again?",
							"Memory", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options,
							options[0]);
				}
				else {
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

		for (int i = 0; i < cards.size(); i++) {
			
				if (cards.get(i).getState() != CardState.NONE) {
					System.out.println("will return false from gameOver");
					return false;
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
		return cardIndexX;
	}

	public void setCardIndexX(int cardIndexX) {
		this.cardIndexX = cardIndexX;
	}

	public int getCardIndexY() {
		return cardIndexY;
	}

	public void setCardIndexY(int cardIndexY) {
		this.cardIndexY = cardIndexY;
	}

	public int getSavedIndexX() {
		return savedIndexX;
	}

	public void setSavedIndexX(int savedIndexX) {
		this.savedIndexX = savedIndexX;
	}

	public int getSavedIndexY() {
		return savedIndexY;
	}

	public void setSavedIndexY(int savedIndexY) {
		this.savedIndexY = savedIndexY;
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