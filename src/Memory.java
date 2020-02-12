import control.MenuController;
import model.MemoryGame;
import view.GameView;
import view.MainMenuView;
import view.OptionsView;

public class Memory {
	public static void main(String[] args) {

		MainMenuView mMenu = new MainMenuView();
		OptionsView oView = new OptionsView();
		MemoryGame mGame = new MemoryGame();
		GameView gView = new GameView();
		new MenuController(mMenu, oView, mGame, gView);
	}
}