package model;

/*
 * class card which includes enums to check state of cards  
 * through enum state will be determined if card is allowed to be opened or not.
 */
public class Card {
	int[] cardIndex = new int[2];
	private CardState state;
	
	public enum CardState {
		OPEN, CLOSED, NONE
	}
		
	public Card(int i, int j){
		this.updateCard(CardState.CLOSED);
		this.cardIndex[0]=i;
		this.cardIndex[1]=j;
	}
	
	public void setCardIndex(int x, int y) {
		this.cardIndex[0]=x;
		this.cardIndex[1]=y;
	}
	
	public int[] getCardIndex() {
		return cardIndex;
	}

	public void updateCard(CardState state){
		this.state = state;
	}

	public CardState getState(){
		return this.state;
	}
}
