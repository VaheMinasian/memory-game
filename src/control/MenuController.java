package control;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import model.MemoryGame;
import view.GameView;
import view.MainMenuView;
import view.MyPanel;
import view.OptionsView;

//Controller class for Main menu and options menu using singleton controller object
public class MenuController {

	private static MenuController singletonMenuControlObject = null;

	private static MainMenuView mainMenuView;
	private static OptionsView optionsView;
	private static MemoryGame gameModel;
	private static GameView gameView;
	static boolean loaded = false;
	static BufferedReader reader;
	static ArrayList<String> profile = new ArrayList<>();
	private static int humanCounter = 0;
	private static int toMain;

	public MenuController() {
	}

	// creates singleton for controller
	public static MenuController getInstance(MainMenuView mView, OptionsView oView, MemoryGame mGame, GameView gView) {

		if (singletonMenuControlObject == null) {
			singletonMenuControlObject = new MenuController();
			mainMenuView = mView;
			optionsView = oView;
			gameModel = mGame;
			gameView = gView;

			mainMenuView.addMainMenuViewListener(new MainMenuListener());
			isPlayable();
		}
		return singletonMenuControlObject;
	}

	// Check if profile file is empty to enable play button (for first run)
	private static void isPlayable() {

		FileReader file;
		String line = "";
		try {
			file = new FileReader("resources/profile.txt");
			reader = new BufferedReader(file);
			line = reader.readLine();
			profile.clear();
			while (line != null) {

				profile.add(line.substring(line.indexOf(':') + 1));
				line = reader.readLine();
			}
			reader.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		if (((profile.size() != 0) && profile.get(profile.size() - 1).equals("true"))) {
			mainMenuView.getPlayButton().setEnabled(true);
		}
	}

	// Reading profile data from file
	private static void loadProfile() {
		FileReader file;
		profile.clear();

		try {
			file = new FileReader("resources/profile.txt");
			reader = new BufferedReader(file);
			String line = reader.readLine();

			while (line != null) {

				profile.add(line.substring(line.indexOf(':') + 1));
				line = reader.readLine();

			}
			reader.close();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	// Loading profile components to Options Menu UI
	private static void loadOptionsProfile(OptionsView view) {

		switch (profile.get(0)) {
		case "s":
			view.getSoloButton().setSelected(true);
			break;
		case "h":
			view.getHumanButton().setSelected(true);
			break;
		case "c":
			view.getComputerButton().setSelected(true);
			break;
		default:
			break;
		}

		// checking board size in profile and loading to UI
		switch (profile.get(1)) {
		case "4":
			view.getSqrFour().setSelected(true);
			break;
		case "6":
			view.getSqrSix().setSelected(true);
			break;
		case "8":
			optionsView.getSqrEight().setSelected(true);
			break;
		case "10":
			view.getSqrTen().setSelected(true);
			break;
		default:
			break;
		}
		// checking player name(s) in profile and loading to UI
		if (!profile.get(2).equals("")) {
			OptionsView.getListModel().insertElementAt(profile.get(2), 0);
			OptionsView.getList().setSelectedIndex(0);
			OptionsView.getList().ensureIndexIsVisible(0);

		}
		if (!profile.get(3).equals("") && !profile.get(3).equals("Computer")) {
			OptionsView.getListModel().insertElementAt(profile.get(3), 1);
			OptionsView.getList().setSelectedIndex(1);
			OptionsView.getList().ensureIndexIsVisible(1);

		} // End of Reading profile data from file
	}

	static class MainMenuListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {

			if (e.getSource() == mainMenuView.getPlayButton()) {
				loadProfile();
				gameModel.initializeParameters(profile);
				mainMenuView.frame.setVisible(false);
				gameView.initialize(profile);
				MyPanel.setBoardSize(gameView.getBoardDimension());
				gameView.addGameViewListener(new GameListener());
				gameModel.setActivePlayer(gameModel.getPlayer1());
				if (!profile.get(0).equals("s")) {
					gameModel.randomFirstPlayer();
				}
				System.out.println("Active player is: " + gameModel.getActivePlayer().getName());

				if (gameModel.getActivePlayer().getName().equals("Computer")) {
					new java.util.Timer().schedule(new java.util.TimerTask() {
						@Override
						public void run() {
							playTheComputer();
						}
					}, 1000);
				}
			}

			// exit application if 'Quit' button is clicked in main menu
			if (e.getSource() == mainMenuView.getExitButton()) {
				System.exit(0);
			}

			// actions performed if Options button is clicked in main menu
			if (e.getSource() == mainMenuView.getOptionsButton()) {
				// setup and display options menu
				optionsView.setOptionsMenu();
				if ((profile.size() != 0) || (loaded == true)) {
					loadProfile();
					loadOptionsProfile(optionsView);
				}
				// Add listeners to options menu
				optionsView.addOptionsViewListener(new OptionsViewListener());

				// hide main menu when switching to options menu.
				mainMenuView.frame.setVisible(false);
				loaded = true;

				// override system.exit(0) on options menu to return to main menu
				optionsView.frame.addWindowListener(new WindowAdapter() {
					@Override
					public void windowClosing(WindowEvent e) {
						optionsView.frame.setVisible(false);
						mainMenuView.frame.setVisible(true);
					}
				});
			}
		}// End of Action performed
	} // End of MainMenuListener

	static class OptionsViewListener implements ActionListener {

		String player1 = "", player2 = "", player3 = "";
		String selectedGameMode;
		String board;

		// Actions to perform when 'save' is clicked in options menu
		public void actionPerformed(ActionEvent e) {

			if (e.getSource() == optionsView.getSaveButton()) {

				// V A L I D A T I O N
				// narrowing down possible combinations of selections of game mode, board size
				// and player names.
				if (((optionsView.getSoloButton().isSelected()) || (optionsView.getHumanButton().isSelected())
						|| (optionsView.getComputerButton().isSelected()))
						&& ((optionsView.getSqrFour().isSelected()) || (optionsView.getSqrSix().isSelected())
								|| (optionsView.getSqrEight().isSelected())
								|| (optionsView.getSqrTen().isSelected()))) {
					if (optionsView.getSoloButton().isSelected()) {
						if (OptionsView.getListModel().getSize() != 1) {
							Toolkit.getDefaultToolkit().beep();
							JOptionPane.showMessageDialog(null, "You should have only one name!", "Memory",
									JOptionPane.INFORMATION_MESSAGE);
							return;
						}
						player1 = OptionsView.getListModel().getElementAt(0);
					}
					// V A L I D A T I O N
					// Play against computer
					else if (optionsView.getComputerButton().isSelected()) {
						if (OptionsView.getListModel().getSize() != 1) {
							Toolkit.getDefaultToolkit().beep();
							JOptionPane.showMessageDialog(null, "You should have only one name!", "Memory",
									JOptionPane.INFORMATION_MESSAGE);
							return;
						}

						player1 = OptionsView.getListModel().getElementAt(0);
						player2 = "Computer";

					}
					// V A L I D A T I O N
					// Play between 2 human
					else if (optionsView.getHumanButton().isSelected()) {
						if (OptionsView.getListModel().getSize() != 2) {
							Toolkit.getDefaultToolkit().beep();
							JOptionPane.showMessageDialog(null, "Please register 2 player names.", "Memory",
									JOptionPane.INFORMATION_MESSAGE);
							return;
						}
						player1 = OptionsView.getListModel().getElementAt(0);
						player2 = OptionsView.getListModel().getElementAt(1);

					} // END OF OPTIONS MENU VALIDATIONS

					// Writing to profile file.
					if (optionsView.getSoloButton().isSelected()) {
						selectedGameMode = optionsView.getSoloButton().getActionCommand();
					} else if (optionsView.getHumanButton().isSelected()) {
						selectedGameMode = optionsView.getHumanButton().getActionCommand();
					} else if (optionsView.getComputerButton().isSelected()) {
						selectedGameMode = optionsView.getComputerButton().getActionCommand();
					}

					if (optionsView.getSqrFour().isSelected()) {
						board = optionsView.getSqrFour().getActionCommand();
					} else if (optionsView.getSqrSix().isSelected()) {
						board = optionsView.getSqrSix().getActionCommand();
					} else if (optionsView.getSqrEight().isSelected()) {
						board = optionsView.getSqrEight().getActionCommand();
					} else if (optionsView.getSqrTen().isSelected()) {
						board = optionsView.getSqrTen().getActionCommand();
					}

					StringBuffer sbr = new StringBuffer();
					sbr.append("mode:" + selectedGameMode + '\n');
					sbr.append("boardsize:" + board + '\n');
					sbr.append("player1:" + player1 + '\n');
					sbr.append("player2:" + player2 + '\n');
					sbr.append("playable:true");

					FileOutputStream fos = null;
					try {
						fos = new FileOutputStream("resources/profile.txt");
						fos.write(sbr.toString().getBytes());
						fos.close();
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					// End of writing to a file
					sbr.delete(0, sbr.length());
					optionsView.frame.setVisible(false);
					mainMenuView.getPlayButton().setEnabled(true);
					mainMenuView.frame.setVisible(true);
				} else {
					Toolkit.getDefaultToolkit().beep();
					JOptionPane.showMessageDialog(null, "Please Fill all the necessary fields before saving", "Memory",
							JOptionPane.INFORMATION_MESSAGE);
					return;

				}
			}
		}
	}

	//
	static class GameListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			// Double loop to find the clicked button
			if (gameModel.getActivePlayer().getClass() != null) {
				System.out.println("human playing");
				for (int i = 0; i < gameView.getEmojiButtons().length; i++) {
					for (int j = 0; j < gameView.getEmojiButtons()[i].length; j++) {
						// selecting the card/button clicked
						if ((e.getSource() == gameView.getEmojiButtons()[i][j]) && (humanCounter < 2)
								&& (!gameModel.getActivePlayer().getName().equals("Computer"))) {

							humanCounter++;
							gameModel.setSelectedCard(MemoryGame.getCards()[i][j]);
//							System.out.println("i=" + i + ", j=" + j);
//							System.out.println(gameModel.getSelectedCard().getClass());
							try {
								gameModel.buttonIsOpen();
							} catch (ButtonNotAvailableException e1) {
								System.err.println("IndexOutOfBoundsException: " + e1.getMessage());
								humanCounter--;
							}
							// controls if card will be opened, and if yes opens it and does necesary
							// asignments
							// for Card and it's icon.
							System.out.println("Player1 name is: " + gameModel.getPlayer1().getName());
//								System.out.println("Player2 name is: " + gameModel.getPlayer2().getName());

							if (((gameModel.getSecondNumber()) == 0) && (gameModel.move(i, j))) {
								// if(singletonModel.move(i, j));
								gameView.updateCardBoard(i, j);
							}
							if (gameModel.getSecondNumber() != 0) {

								if (!gameModel.getStatus()) {
									new java.util.Timer().schedule(new java.util.TimerTask() {
										@Override
										public void run() {

											gameView.closeButton(gameModel.getFirstCard());
											gameView.closeButton(gameModel.getSecondCard());
											gameModel.nullifyButtonsIndex();
											gameModel.resetCounter();
											gameModel.revoke();
											updateScoreBoard();
											humanCounter = 0;
											if (profile.get(0).equals("s")) {
											} else {
												gameModel.switchActivePlayer();
												playTheComputer();
											}
										}
									}, 1000);

								} else if (gameModel.getStatus()) {
									new java.util.Timer().schedule(new java.util.TimerTask() {
										@Override
										public void run() {
											gameView.removeButtons(gameModel.getFirstCard(), gameModel.getSecondCard());
											gameModel.nullifyButtonsIndex();
											gameModel.resetCounter();
											gameModel.update();
											updateScoreBoard();
											humanCounter = 0;
											if (gameModel.gameOver()) {
												resetGame();
											}
										}
									}, 1000);
								}
							}

						}
					}
				}
			}
		}
	}

	public static void playTheComputer() {
		int xIndexComp;
		int yIndexComp;

		System.out.println("Beginning of StartGame...active player is: " + gameModel.getActivePlayer().getName());

//		if (gameModel.getActivePlayer().getName().equals("Computer")) {

		do {
			for (int c = 0; c < 2; c++) {
				if ((!gameModel.gameOver())
						&& (gameModel.getActivePlayer().setRandomIndex(Integer.parseInt(profile.get(1))))) {
					xIndexComp = gameModel.getActivePlayer().getCardIndexX();
					yIndexComp = gameModel.getActivePlayer().getCardIndexY();
					System.out.println("after assigning x and y are: " + xIndexComp + ", " + yIndexComp);

					try {
						Thread.sleep(600);
					} catch (InterruptedException interrupt) {
						// TODO Auto-generated catch block
						interrupt.printStackTrace();
					}

					gameModel.setSelectedCard(MemoryGame.getCards()[xIndexComp][yIndexComp]);
					// Check condition to execute first card open
					if (((gameModel.getSecondNumber()) == 0) && (gameModel.move(xIndexComp, yIndexComp))) {
						gameView.updateCardBoard(xIndexComp, yIndexComp);
					}

					if (gameModel.getSecondNumber() != 0) {
						System.out.println("inside second number !=0");
						if (!gameModel.getStatus()) {
							System.out.println("inside !gameModel.getStatus()");

							try {
								Thread.sleep(1000);
							} catch (InterruptedException interrupt) {
								// TODO Auto-generated catch block
								interrupt.printStackTrace();
							}
							gameView.closeButton(gameModel.getFirstCard());
							gameView.closeButton(gameModel.getSecondCard());
							gameModel.nullifyButtonsIndex();
							gameModel.resetCounter();
							gameModel.revoke();
							updateScoreBoard();
							gameModel.switchActivePlayer();

						} else if (gameModel.getStatus()) {
							System.out.println("inside gameModel.getStatus()");
							try {
								Thread.sleep(1000);
							} catch (InterruptedException interrupt) {
								// TODO Auto-generated catch block
								interrupt.printStackTrace();
							}
							gameView.removeButtons(gameModel.getFirstCard(), gameModel.getSecondCard());
							gameModel.nullifyButtonsIndex();
							gameModel.resetCounter();
							gameModel.update();
							updateScoreBoard();
							if (gameModel.gameOver()) {
									resetGame();
							}
						}
					}
				}
			}

		} while (gameModel.getActivePlayer().getName().equals("Computer"));
	}

	static void updateScoreBoard() {
		if (gameModel.getActivePlayer().equals(gameModel.getPlayer1())) {
			System.out.println("player1 update inside updateScoreBoard");
			gameView.setScore1Label(gameModel.getActivePlayer().getScore(), gameModel.getActivePlayer().getTries());
		} else {
			System.out.println(
					"player2 update inside updateScoreBoard... player2 is: " + gameModel.getActivePlayer().getName());
			gameView.setScore2Label(gameModel.getActivePlayer().getScore(), gameModel.getActivePlayer().getTries());
		}
	}

	static void resetGame() {
		toMain = gameModel.getMessage(profile.get(0));
		switch (toMain){
		case 0:
			gameModel = null;
			gameModel = MemoryGame.getInstance();
			gameModel.getPlayer1().setScore(0);
			gameModel.getPlayer1().setTries(0);
			if(!profile.get(0).equals("s")) {
				gameModel.getPlayer2().setScore(0);
				gameModel.getPlayer2().setTries(0);														
			}
			gameView.dispose();
			gameView = new GameView();
			gameView.initialize(profile);
			gameView.revalidate();
			gameView.addGameViewListener(new GameListener());
			gameView.setScore1Label(0, 0);
			gameView.setScore2Label(0, 0);
			break;
		case 1:
			gameModel = null;
			gameModel = MemoryGame.getInstance();
			gameModel.getPlayer1().setScore(0);
			gameModel.getPlayer1().setTries(0);
			if(!profile.get(0).equals("s")) {
				gameModel.getPlayer2().setScore(0);
				gameModel.getPlayer2().setTries(0);														
			}
			gameView.dispose();		
			gameView = new GameView();
			gameView.setVisible(false);
			mainMenuView.frame.setVisible(true);
			gameView.revalidate();
			break;
		case 2:
			System.exit(0);	
		default:
			System.exit(0);	
		}
	}
}