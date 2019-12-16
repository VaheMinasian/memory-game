import control.MenuController;
import model.MemoryGame;
import view.GameView;
import view.MainMenuView;
import view.OptionsView;

public class Memory {
//	boolean willPlay=false; *
	public static void main(String[] args){

		MainMenuView mMenu = new MainMenuView();
		OptionsView oView = new OptionsView();
		// MemoryGame game = new MemoryGame();
		MemoryGame mGame = new MemoryGame();
		GameView gView = new GameView();
		new MenuController(mMenu, oView, mGame, gView);
		
	}
}