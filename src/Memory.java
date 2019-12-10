import control.MenuController;
import model.MemoryGame;
import view.GameView;
import view.MainMenuView;
import view.OptionsView;

public class Memory {
	boolean willPlay=false;
	public static void main(String[] args){

		MainMenuView menu = new MainMenuView();
		OptionsView view = OptionsView.getInstance();
		// MemoryGame game = new MemoryGame();
		MemoryGame mGame = MemoryGame.getInstance();
		GameView gView = new GameView();
		MenuController controller = MenuController.getInstance(menu, view, mGame, gView);
		
	}
}