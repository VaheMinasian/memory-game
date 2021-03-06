package model;

/*
 * class card which includes enums to check state of cards  
 * through enum state will be determined if card is allowed to be opened or not.
 */
public class Card {
	int[] cardIndex = new int[2];
	
	public enum CardState {
		OPEN, CLOSED, NONE
	}

	private CardState state;

	public Card(int i, int j){
		this(CardState.CLOSED);
		this.cardIndex[0]=i;
		this.cardIndex[1]=j;
	}

	public int[] getCardIndex() {
		return cardIndex;
	}

	public Card(CardState state){
		updateCard(state);
	}

	public void updateCard(CardState state){
		this.state = state;
	}

	public CardState getState(){
		return this.state;
	}
}
