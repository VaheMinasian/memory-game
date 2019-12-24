package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JOptionPane;
import control.ButtonNotAvailableException;
import model.Card.CardState;

public class MemoryGame implements Game {

//	private static MemoryGame Single_Instance = null;

	private Card[][] cards;
	private Card selectedCard;
	private int boardDimension;
	private static Player player1;
	private static Player player2;
	private Player activePlayer;

	private Card firstCard = null, secondCard = null;
	private List <Integer> missedButtons= new ArrayList<>();
	
	public List<Integer> getMissedButtons() {
		return missedButtons;
	}

	
	  public void setMemorySize() {
		  for (int i=0; i< boardDimension * boardDimension * 2; i++) {
			  missedButtons.add(0);
		  }
		  //list.add(0)); list.set(9, 51); this.missedButtons = i*i;
	}


	public void setMissedButtons(Integer missedB1, Integer missedB2, String mode) { 
		 if(mode.equals("c")) {
			 System.out.println(mode);
			 int i1 = firstCard.getCardIndex()[0];
			 int j1 = firstCard.getCardIndex()[1];
			 int i2 = secondCard.getCardIndex()[0];
			 int j2 = secondCard.getCardIndex()[1];
			 missedButtons.set(i1*boardDimension+j1, missedB1);
			 missedButtons.set(i2*boardDimension+j2, missedB2);
			 missedButtons.set((i1*boardDimension+j1)+boardDimension*boardDimension, activePlayer.getTries()+1);
			 missedButtons.set((i2*boardDimension+j2)+boardDimension*boardDimension, activePlayer.getTries()+1);
			 
			 System.out.println("Button 1 is: JButton" + "["+i1+"]"+"["+j1+"]" +" = " + missedB1);
			 System.out.println("Button 2 is: JButton" + "["+i2+"]"+"["+j2+"]" +" = " + missedB2);
		 }
		 else return;
		  
	  }
	 

	private int counter = 0;
	private int wincheck = 0;
	Random randomNo;

//	private static String gameMode;
//	private static int[] buttonsIndex = new int[4];

	public MemoryGame() {

	}

	public void initializeParameters(ArrayList<String> profile) {
		// TODO Auto-generated method stub
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

	
	public Card getRandomCardIndex() {
		randomNo = new Random();
		int cardIndexX, cardIndexY;
		do {
			cardIndexX = randomNo.nextInt(boardDimension);
			cardIndexY = randomNo.nextInt(boardDimension);
		} while (getCards()[cardIndexX][cardIndexY].getState() != CardState.CLOSED);
		System.out.println("returning true from gameModel.setRandomIndex cardIndexX & cardIndexY = ("+cardIndexX +","+cardIndexY +")");
		System.out.println(this.getCards()[cardIndexX][cardIndexY].getState());
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
		int dialogBoxReturnValue=-1;
		
		String[] options = { "play", "Main Menu", "Quit"};
		wincheck = (s.equals("s")) ? 1 : 2;

		if (wincheck == 1) {
			dialogBoxReturnValue = JOptionPane.showOptionDialog(null,
					"Congratulations " + player1.getName() + ", you have won! \nYour socre is " + player1.getScore()
							+ " out of " + player1.getTries() + " moves.",
					"Memory", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
		
		} else if (wincheck == 2) {
			switch (player1.getScore() - player2.getScore()) {
			case 0:
				dialogBoxReturnValue = JOptionPane.showOptionDialog(null, "The game is drawn!", "Memory", JOptionPane.DEFAULT_OPTION,
						JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
			break;
			default:
				int triesOfWinner = (player1.getScore() > boardDimension * boardDimension / 4) ? player1.getTries() : player2.getTries();
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
			System.out.println("active player is: " + getActivePlayer().getName());
			return "player1";
		} else if (activePlayer.equals(player1)) {
			player1.setActive(false);
			player2.setActive(true);
			setActivePlayer(player2);
			System.out.println("active player is: " + getActivePlayer().getName());
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
				if (cards[i][j].getState() != CardState.NONE) {
					return false;
				}
			}
		}
		return true;
	}

	/*
	 * public void nullifyButtonsIndex() { for (int i = 0; i < buttonsIndex.length;
	 * i++) buttonsIndex[i] = 0; }
	 */
	/*
	 * public void resetCounter() { counter = 0; }
	 */

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

	/*
	 * public int getCounter() { return counter; }
	 */

	/*
	 * public static String getGameMode() { return gameMode; }
	 * 
	 * public static void setGameMode(String gameMode) { MemoryGame.gameMode =
	 * gameMode; }
	 */
}