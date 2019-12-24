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

import javax.swing.AbstractButton;
import javax.swing.JOptionPane;

import model.Card;
import model.MemoryGame;
import view.GameView;
import view.MainMenuView;
import view.OptionsView;




//Controller class for Main menu and options menu using singleton controller object
public class MenuController {

	private static MainMenuView mainMenuView;
	private static OptionsView optionsView;
	private static BufferedReader reader;
	private static FileReader file;
	private static ArrayList<String> profile = new ArrayList<>();

	private MemoryGame gameModel;
	private GameView gameView;
	
	private int validClicksOnCards = 0;
	private boolean firstTimeProfileSave = false;

	String activePlayerLabel = null;
	int firstNumber, secondNumber;

	public MenuController() {
		System.out.println("Main constructor invoked");
	}
	
	
	public MenuController(MainMenuView mView, OptionsView oView, MemoryGame mGame, GameView gView) {
		mainMenuView = mView;
		optionsView = oView;
		gameModel = mGame; 
		gameView = gView;
		isPlayable();
		mainMenuView.addMainMenuViewListener(new MainMenuViewListener());
	}

		
	// Check if profile file is empty to enable play button (for first run)
	private static void isPlayable() {

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
	private static void loadProfileUI() {

		switch (profile.get(0)) {
		case "s":
			optionsView.getSoloButton().setSelected(true);
			break;
		case "h":
			optionsView.getHumanButton().setSelected(true);
			break;
		case "c":
			optionsView.getComputerButton().setSelected(true);
			break;
		default:
			break;
		}

		// checking board size in profile and loading to UI
		switch (profile.get(1)) {
		case "4":
			optionsView.getSqrFour().setSelected(true);
			break;
		case "6":
			optionsView.getSqrSix().setSelected(true);
			break;
		case "8":
			optionsView.getSqrEight().setSelected(true);
			break;
		case "10":
			optionsView.getSqrTen().setSelected(true);
			break;
		default:
			break;
		}
		
		switch (profile.get(4)) {
		case "n":
			optionsView.getNovice().setSelected(true);
			break;
		case "e":
			optionsView.getEasy().setSelected(true);
			break;
		case "m":
			optionsView.getMedium().setSelected(true);
			break;
		case "h":
			optionsView.getHard().setSelected(true);
			break;
		default:
			break;
		
		}
		// checking player name(s) in profile and loading to UI
		if (!profile.get(2).equals("")) {
			optionsView.getListModel().insertElementAt(profile.get(2), 0);
			optionsView.getList().setSelectedIndex(0);
			optionsView.getList().ensureIndexIsVisible(0);

		}
		if (!profile.get(3).equals("") && !profile.get(3).equals("Computer")) {
			optionsView.getListModel().insertElementAt(profile.get(3), 1);
			optionsView.getList().setSelectedIndex(1);
			optionsView.getList().ensureIndexIsVisible(1);

		} // End of Reading profile data from file
	}

	
	
	
	class MainMenuViewListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {

			// exit application if     'Q U I T'     button is clicked in main menu
			if (e.getSource() == mainMenuView.getExitButton()) {
				System.exit(0);
			}
			
			// actions performed if    'O P T I O N S'    button is clicked in main menu
			if (e.getSource() == mainMenuView.getOptionsButton()) {

				// hide main menu when switching to options menu.
				mainMenuView.frame.setVisible(false);
				
				// setup and display options menu
				optionsView.setOptionsMenu();
				if ((profile.size() != 0) || (firstTimeProfileSave == true) ) {
					loadProfile();
					//used when options button is clicked more than once within the one game session
					loadProfileUI();
				}
				// Add listeners to options menu
				optionsView.addOptionsViewListener(new OptionsViewListener());
				
				//marker to ensure loading the "profile" file version of profile is loaded into options menu UI
				firstTimeProfileSave = true;
				
				// override system.exit(0) on options menu to return to main menu
				optionsView.frame.addWindowListener(new WindowAdapter() {
					@Override
					public void windowClosing(WindowEvent e) {
						optionsView.frame.setVisible(false);
						mainMenuView.frame.setVisible(true);
					}
				});
			}
			
			// Initialize game when    'P L A Y'    button is clicked in main menu
			if (e.getSource() == mainMenuView.getPlayButton()) {
				loadProfile();
				gameModel.initializeParameters(profile);
				mainMenuView.frame.setVisible(false);
				gameView.displayGameWindow(profile);
//				gameView.getBoardPanel().setBoardSize(gameView.getBoardDimension());
				gameView.addGameViewListener(new GameListener());
//				gameModel.setActivePlayer(gameModel.getPlayer1());
				gameModel.setMemorySize();
				if (!profile.get(0).equals("s")) {
					gameModel.randomFirstPlayer();
				}

				if (gameModel.getActivePlayer().getName().equals("Computer")) {
					new java.util.Timer().schedule(new java.util.TimerTask() {
						@Override
						public void run() {
							playTheComputer();
						}
					}, 1000);
				}
				String p1Label = gameModel.getActivePlayer()==gameModel.getPlayer1() ? "player1" : "player2";
				gameView.setActivePlayerFont(p1Label);
			}
		}// End of Action performed
	} // End of MainMenuViewListener

	
	
	
	
	class OptionsViewListener implements ActionListener {
		
		String player1 = "", player2 = "", player3 = "";
		String selectedGameMode, difficulty;
		String board;

		// Actions to perform when 'save' is clicked in options menu
		public void actionPerformed(ActionEvent e) {

			if (e.getSource() == optionsView.getSaveButton()) {
				if (optionsView.getrButtonGroup().getSelection()==null){
					System.out.println("no radio button selected");
					
				}else {
					System.out.println(optionsView.getrButtonGroup().getSelection());
				}
				// V A L I D A T I O N
				// narrowing down possible combinations of selections of game mode, board size
				// and player names.
				if((optionsView.getGameMode().getSelection()!=null)&&(optionsView.getDimensions().getSelection()!=null)){
					if (optionsView.getSoloButton().isSelected()) {
						if (optionsView.getListModel().getSize() != 1) {
							Toolkit.getDefaultToolkit().beep();
							JOptionPane.showMessageDialog(null, "Please register a player!", "Memory",
									JOptionPane.INFORMATION_MESSAGE);
							return;
						}
						player1 = optionsView.getListModel().getElementAt(0);
					}
					// V A L I D A T I O N
					// Play against computer
					else if (optionsView.getComputerButton().isSelected()) {
						if (optionsView.getListModel().getSize() != 1) {
							Toolkit.getDefaultToolkit().beep();
							JOptionPane.showMessageDialog(null, "Please register a player!", "Memory",
									JOptionPane.INFORMATION_MESSAGE);
							return;
						} else if (optionsView.getrButtonGroup().getSelection()==null) {
							Toolkit.getDefaultToolkit().beep();
							JOptionPane.showMessageDialog(null, "Please select difficulty level!", "Memory",
									JOptionPane.INFORMATION_MESSAGE);
							return;
						}

						player1 = optionsView.getListModel().getElementAt(0);
						player2 = "Computer";

					}
					// V A L I D A T I O N
					// Play between 2 human
					else if (optionsView.getHumanButton().isSelected()) {
						if (optionsView.getListModel().getSize() != 2) {
							Toolkit.getDefaultToolkit().beep();
							JOptionPane.showMessageDialog(null, "Please register 2 player names.", "Memory",
									JOptionPane.INFORMATION_MESSAGE);
							return;
						}
						player1 = optionsView.getListModel().getElementAt(0);
						player2 = optionsView.getListModel().getElementAt(1);

					} // END OF OPTIONS MENU VALIDATIONS

					// Writing to profile file.
					if (optionsView.getSoloButton().isSelected()) {
						selectedGameMode = optionsView.getSoloButton().getActionCommand();
					} else if (optionsView.getHumanButton().isSelected()) {
						selectedGameMode = optionsView.getHumanButton().getActionCommand();
					} else if (optionsView.getComputerButton().isSelected()) {
						selectedGameMode = optionsView.getComputerButton().getActionCommand();
						for (AbstractButton button : optionsView.getListRadioButton()) {
							if (button.isSelected()) {
								difficulty=button.getActionCommand();
								System.out.println(difficulty);
							}	
						}
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
					sbr.append("difficulty:" + difficulty + '\n');
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

	

	
	class GameListener implements ActionListener {
		
		public void actionPerformed(ActionEvent e) {
			// Double loop to find the clicked button
			if (gameModel.getActivePlayer().getClass() != null) {
				System.out.println("human playing");
				for (int i = 0; i < gameView.getEmojiButtons().length; i++) {
					for (int j = 0; j < gameView.getEmojiButtons()[i].length; j++) {
						// selecting the card/button clicked
						if ((e.getSource() == gameView.getEmojiButtons()[i][j]) && (validClicksOnCards < 2)
								&& (!gameModel.getActivePlayer().getName().equals("Computer"))) {

							validClicksOnCards++;
							gameModel.setSelectedCard(gameModel.getCards()[i][j]);
//							System.out.println("i=" + i + ", j=" + j);
//							System.out.println(gameModel.getSelectedCard().getClass());
							try {
								gameModel.buttonIsOpen();
							} catch (ButtonNotAvailableException e1) {
								System.err.println("IndexOutOfBoundsException: " + e1.getMessage());
								validClicksOnCards--;
							}
							// controls if card will be opened, and if yes opens it and does necesary
							// asignments
							// for Card and it's icon.
							System.out.println("Player1 name is: " + gameModel.getPlayer1().getName());
//								System.out.println("Player2 name is: " + gameModel.getPlayer2().getName());

							if ((secondNumber == 0) && (gameModel.move(i, j))) {
								if(validClicksOnCards==1) { 
									firstNumber=gameView.getCurrentIcons().get(i * gameView.getBoardDimension() + j);
									}
								else if (validClicksOnCards==2){ 
									secondNumber=gameView.getCurrentIcons().get(i * gameView.getBoardDimension() + j);
									}
								// if(singletonModel.move(i, j));
								gameView.updateCardBoard(i, j);
							}
							if (secondNumber != 0) {

								if (!gameModel.getStatus(firstNumber, secondNumber)) {
									new java.util.Timer().schedule(new java.util.TimerTask() {
										@Override
										public void run() {
											gameModel.setMissedButtons(firstNumber, secondNumber, profile.get(0));
											gameView.restoreDefaultIcon(gameModel.getFirstCard());
											gameView.restoreDefaultIcon(gameModel.getSecondCard());
//											gameModel.nullifyButtonsIndex();
											firstNumber=0;
											secondNumber=0;
											gameModel.revoke();
											updateScoreBoard();
											validClicksOnCards = 0;
											if (!profile.get(0).equals("s")){
												activePlayerLabel = gameModel.switchActivePlayer();
												gameView.setActivePlayerFont(activePlayerLabel);
												System.out.println("active player now is the player named: "+ gameModel.getActivePlayer().getClass().getSimpleName());
												if (gameModel.getActivePlayer().getClass().getSimpleName().equals("ComputerPlayer"))
												playTheComputer();
											}
										}
									}, 1000);

								} else if (gameModel.getStatus(firstNumber, secondNumber)) {
									new java.util.Timer().schedule(new java.util.TimerTask() {
										@Override
										public void run() {
											gameView.removeCards(gameModel.getFirstCard(), gameModel.getSecondCard());
//											gameModel.nullifyButtonsIndex();
											firstNumber=0;
											secondNumber=0;
											gameModel.update();
											updateScoreBoard();
											validClicksOnCards = 0;
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

	
	public void playTheComputer() {
		Card card;
		int xIndexComp;
		int yIndexComp;

		System.out.println("Beginning of StartGame...active player is: " + gameModel.getActivePlayer().getName());


		do {
//				if ((gameModel.getActivePlayer().setRandomIndex(gameModel, Integer.parseInt(profile.get(1))))) {
			
					card = gameModel.getRandomCardIndex();
					xIndexComp= card.getCardIndex()[0];
					yIndexComp = card.getCardIndex()[1];
					System.out.println("after assigning x and y are: " + xIndexComp + ", " + yIndexComp);

					try {
						Thread.sleep(300);
					} catch (InterruptedException interrupt) {
						// TODO Auto-generated catch block
						interrupt.printStackTrace();
					}

					gameModel.setSelectedCard(card);
					validClicksOnCards++;
					// Check condition to execute first card open
					if ((secondNumber == 0) && (gameModel.move(xIndexComp, yIndexComp))) {

						if(validClicksOnCards==1) { 
							firstNumber=gameView.getCurrentIcons().get(xIndexComp * gameView.getBoardDimension() + yIndexComp);
							System.out.println("FirstNumber is: " + firstNumber);

						}
						else if (validClicksOnCards==2){ 
							secondNumber=gameView.getCurrentIcons().get(xIndexComp * gameView.getBoardDimension() + yIndexComp);
							System.out.println("SecondNumber is: " + secondNumber);

						}
						gameView.updateCardBoard(xIndexComp, yIndexComp);
					}

					if (secondNumber != 0) {
						System.out.println("inside second number !=0");
						if (!gameModel.getStatus(firstNumber, secondNumber)) {
							System.out.println("inside !gameModel.getStatus()");

							try {
								Thread.sleep(300);
							} catch (InterruptedException interrupt) {
								// TODO Auto-generated catch block
								interrupt.printStackTrace();
							}
							gameModel.setMissedButtons(firstNumber, secondNumber, profile.get(0));
							gameView.restoreDefaultIcon(gameModel.getFirstCard());
							gameView.restoreDefaultIcon(gameModel.getSecondCard());
//							gameModel.nullifyButtonsIndex();
							validClicksOnCards = 0;
							firstNumber=0;
							secondNumber=0;
							gameModel.revoke();
							updateScoreBoard();
							activePlayerLabel = gameModel.switchActivePlayer();
							gameView.setActivePlayerFont(activePlayerLabel);
							

						} else if (gameModel.getStatus(firstNumber, secondNumber)) {
							System.out.println("inside gameModel.getStatus()");
							try {
								Thread.sleep(400);
							} catch (InterruptedException interrupt) {
								// TODO Auto-generated catch block
								interrupt.printStackTrace();
							}
							gameView.removeCards(gameModel.getFirstCard(), gameModel.getSecondCard());
//							gameModel.nullifyButtonsIndex();
							validClicksOnCards = 0;
							firstNumber=0;
							secondNumber=0;
							gameModel.update();
							updateScoreBoard();
							if (gameModel.gameOver()) {
									resetGame();
									return;
							}
						}
					}
//				}
	} while (gameModel.getActivePlayer().getName().equals("Computer")&& !gameModel.gameOver());
	}

	void updateScoreBoard() {
		if (gameModel.getActivePlayer().equals(gameModel.getPlayer1())) {
			System.out.println("player1 update inside updateScoreBoard");
			gameView.setScore1Label(gameModel.getActivePlayer().getScore(), gameModel.getActivePlayer().getTries());
		} else {
			System.out.println(
					"player2 update inside updateScoreBoard... player2 is: " + gameModel.getActivePlayer().getName());
			gameView.setScore2Label(gameModel.getActivePlayer().getScore(), gameModel.getActivePlayer().getTries());
		}
	}

	void resetGame() {
		switch (gameModel.getMessage(profile.get(0))){
		case 0:
			System.out.println("inside case 0");
//			gameModel = null;
			/*
			 * gameModel.getPlayer1().setScore(0); gameModel.getPlayer1().setTries(0);
			 * if(!profile.get(0).equals("s")) { gameModel.getPlayer2().setScore(0);
			 * gameModel.getPlayer2().setTries(0); }
			 */
//			gameView.initialize(profile);
//			gameView.addGameViewListener(new GameListener());
//			gameView.setScore1Label(0, 0);
//			gameView.setScore2Label(0, 0);
			gameModel = new MemoryGame();
			gameView.dispose();
			gameView = new GameView();
			mainMenuView.getPlayButton().doClick();
			break;
		case 1:
			System.out.println("inside case 1");
//			gameModel = null;
			gameModel = new MemoryGame();
			/*
			 * gameModel.getPlayer1().setScore(0); gameModel.getPlayer1().setTries(0);
			 * if(!profile.get(0).equals("s")) { gameModel.getPlayer2().setScore(0);
			 * gameModel.getPlayer2().setTries(0); }
			 */
			gameView.dispose();		
			gameView = new GameView();
			gameView.setVisible(false);
			mainMenuView.frame.setVisible(true);
			break;
		case 2:
			System.out.println("inside case 2");
			System.exit(0);	
			break;
		default:
			System.exit(0);	
		}
	}
}