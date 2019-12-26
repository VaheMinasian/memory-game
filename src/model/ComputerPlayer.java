package model;

public class ComputerPlayer extends Player {

//	Random randomNo;
//	private int cardIndexX=-1, cardIndexY=-1;
	
		
//	public int getCardIndexX() {
//		return cardIndexX;
//	}
//
//	public int getCardIndexY() {
//		return cardIndexY;
//	}
	
	
//	public boolean setRandomIndex(MemoryGame model, int i) {
//		randomNo = new Random();
//		do {
//			cardIndexX = randomNo.nextInt(i);
//			cardIndexY = randomNo.nextInt(i);
//		} while (model.getCards()[cardIndexX][cardIndexY].getState() != CardState.CLOSED);
//		System.out.println("returning true from gameModel.setRandomIndex cardIndexX & cardIndexY = ("+cardIndexX +","+cardIndexY +")");
//		return true;
//	}
	
	
	public ComputerPlayer(String string) {
		super(string);
	}

//	public void setCardIndexX(int cardIndexX) {
//		this.cardIndexX = cardIndexX;
//	}
//
//	public void setCardIndexY(int cardIndexY) {
//		this.cardIndexY = cardIndexY;
//	}
}
