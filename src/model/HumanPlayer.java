package model;

public class HumanPlayer extends Player {

	public HumanPlayer(String name) {
		super(name);
		System.out.println("Human player's name is: " + this.getName());
	}
	
	
	@Override
	public boolean setRandomIndex(int i) {
		// TODO Auto-generated method stub
		return false;
	}
}
