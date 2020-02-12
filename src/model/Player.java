package model;

public abstract class Player {

	private String name;
	private int score = 0;
	private int tries = 0;
	private boolean active = false;

	public Player() {
	}

	public String getName() {
		return name;
	}

	public Player(String name) {
		this.name = name;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getTries() {
		return tries;
	}

	public void setTries(int tries) {
		this.tries = tries;
	}

	void incrementScore() {
		++score;
	}

	void incrementTries() {
		++tries;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public int getCardIndexX() {
		return 0;
	}

	public int getCardIndexY() {
		return 0;
	}
}