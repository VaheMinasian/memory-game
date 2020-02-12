package model;


public class Card {
		private CardState state;
		
	public enum CardState {
		OPEN, CLOSED, NONE
	}
		
	public Card(){
		this.updateCard(CardState.CLOSED);
	}
	
	public void updateCard(CardState state){
		this.state = state;
	}

	public CardState getState(){
		return this.state;
	}
}
