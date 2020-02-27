import control.MenuController;
import model.MemoryGame;
import view.GameView;
import view.MainMenuView;
import view.OptionsView;
import view.ScoresView;

public class Memory {
	public static void main(String[] args) {

		MainMenuView mMenu = new MainMenuView();
		OptionsView oView = new OptionsView();
		MemoryGame mGame = new MemoryGame();
		GameView gView = new GameView();
		ScoresView sView = new ScoresView();
		new MenuController(mMenu, oView, mGame, gView, sView);
	}
}