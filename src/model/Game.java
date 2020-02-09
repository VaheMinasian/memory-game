package model;

public interface Game {

	public boolean move(int index);

	public Boolean getStatus(int x, int y);

	public int getMessage(String s);
}
