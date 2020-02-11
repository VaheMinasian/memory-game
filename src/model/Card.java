package model;

/*
 * class card which includes enums to check state of cards  
 * through enum state will be determined if card is allowed to be opened or not.
 */
public class Card {
		private CardState state;
//		private int index;
		
	public enum CardState {
		OPEN, CLOSED, NONE
	}
		
	public Card(){
		this.updateCard(CardState.CLOSED);
	}
	
//	public void setCardIndex(int x, int y) {
//		this.cardIndex[0]=x;
//		this.cardIndex[1]=y;
//	}
	
	public void updateCard(CardState state){
		this.state = state;
	}

	public CardState getState(){
		return this.state;
	}

//	public int getIndex() {
//		return this.index;
//	}

//	public void setIndex(int index) {
//		this.index = index;
//	}
}
