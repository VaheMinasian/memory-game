package model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class ScoreModel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String playerName;
	private String boardSize;
	private int guessRatio;
	private Long duration;
	private LocalDateTime date;

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public String getBoardSize() {
		return boardSize;
	}

	public void setBoardSize(String boardSize) {
		this.boardSize = boardSize;
	}

	public int getGuessRatio() {
		return guessRatio;
	}

	public void setGuessRatio(int i) {
		this.guessRatio = i;
	}

	public Long getDuration() {
		return duration;
	}

	public void setDuration(Long duration) {
		this.duration = duration;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}
}
