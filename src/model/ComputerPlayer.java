package model;

import java.util.Random;

import model.Card.CardState;

public class ComputerPlayer extends Player {

	Random randomNo;
	private int cardIndexX=-1, cardIndexY=-1;
	
		
	public int getCardIndexX() {
		return cardIndexX;
	}

	public int getCardIndexY() {
		return cardIndexY;
	}
	
	@Override
	public boolean setRandomIndex(int i) {
		randomNo = new Random();
		System.out.println("\nprofile.get(1)= " + i);;
		do {
			cardIndexX = randomNo.nextInt(i);
			cardIndexY = randomNo.nextInt(i);
			System.out.println("cardIndexX= " + cardIndexX + ", cardIndexY= " + cardIndexY);
		} while (MemoryGame.getCards()[cardIndexX][cardIndexY].getState() != CardState.CLOSED);
		System.out.println("returning true from gameModel.setRandomIndex cardIndexX & cardIndexY = ("+cardIndexX +","+cardIndexY +")");
		return true;
	}
	
	
	public ComputerPlayer(String string) {
		// TODO Auto-generated constructor stub
		super(string);
		System.out.println("computers name is: " + this.getName());
	}

	public void setCardIndexX(int cardIndexX) {
		this.cardIndexX = cardIndexX;
	}

	public void setCardIndexY(int cardIndexY) {
		this.cardIndexY = cardIndexY;
	}

}
