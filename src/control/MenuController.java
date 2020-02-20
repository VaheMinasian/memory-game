package control;

import java.awt.Cursor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

import javax.swing.AbstractButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.Timer;

import model.Card;
import model.Card.CardState;
import model.Counter;
import model.MemoryGame;
import model.ScoreModel;
import view.GameView;
import view.MainMenuView;
import view.OptionsView;
import view.ScoresView;

//Controller class for Main menu and options menu using singleton controller object
public class MenuController {

	private static MainMenuView mainMenuView;
	private static OptionsView optionsView;
	private static ScoresView hsView;
	private static BufferedReader bfReader;
	private static FileReader file;
	private static ArrayList<String> profile = new ArrayList<>();

	Cursor waitingCursor = new Cursor(Cursor.WAIT_CURSOR);
	Cursor normalCursor = new Cursor(Cursor.DEFAULT_CURSOR);
	private MemoryGame gameModel;
	private GameView gameView;
	private final ClockListener clock = new ClockListener();
	private int validClicksOnCards = 0;
	private boolean firstTimeProfileSave = false;
	int firstNumber, secondNumber;
	private int factor, serializedIndex;
	private long startTime, stopTime;
	Duration duration;
	Timer timer = new Timer(60, clock);
	ArrayList<Integer> temp = new ArrayList<>();
	File serializeFile = new File("data.txt"), serializeScore = new File("scores.ser");
	static File profileFile = new File("profile.txt");
	private ArrayList<ScoreModel> scoresList = new ArrayList<>();
	private ScoreModel score = new ScoreModel();
	BufferedReader br;
	private Counter counter;

	public MenuController() {
	}

	public MenuController(MainMenuView mView, OptionsView oView, MemoryGame mGame, GameView gView) {
		mainMenuView = mView;
		optionsView = oView;
		gameModel = mGame;
		gameView = gView;
		isPlayable();
		mainMenuView.addMainMenuViewListener(new MainMenuViewListener());
		checkDataFile();
		checkScoreFile();
	}

	void startTimer() {
		timer = new Timer(60, clock);
		startTime = System.currentTimeMillis() - stopTime + startTime;
		timer.start();
	}

	void stopTimer() {
		timer.stop();
		stopTime = System.currentTimeMillis();
	}

	// Check if profile file is empty to enable play button (for first run)
	private static void isPlayable() {

		String line = "";
		try {
			profileFile.createNewFile();
			file = new FileReader("profile.txt");
			bfReader = new BufferedReader(file);
			line = bfReader.readLine();
			profile.clear();
			while (line != null) {

				profile.add(line.substring(line.indexOf(':') + 1));
				line = bfReader.readLine();
			}
			bfReader.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		if (((profile.size() != 0) && profile.get(profile.size() - 1).equals("true"))) {
			mainMenuView.getPlayButton().setEnabled(true);
		}
	}

	void removeSerialization() {
		try {
			FileOutputStream fos = new FileOutputStream("data.txt");
			fos.write(("").getBytes());
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Reading profile data from file
	private static void loadProfile() {
		profile.clear();
		try {
			file = new FileReader("profile.txt");
			bfReader = new BufferedReader(file);
			String line = bfReader.readLine();

			while (line != null) {
				profile.add(line.substring(line.indexOf(':') + 1));
				line = bfReader.readLine();
			}
			bfReader.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
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
		case "4":
			optionsView.getNovice().setSelected(true);
			break;
		case "3":
			optionsView.getEasy().setSelected(true);
			break;
		case "2":
			optionsView.getMedium().setSelected(true);
			break;
		case "1":
			optionsView.getHard().setSelected(true);
			break;
		default:
			break;
		}

		optionsView.getIconSetComboBox().setSelectedItem(profile.get(5));

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

			// exit application if 'Q U I T' button is clicked in main menu
			if (e.getSource() == mainMenuView.getExitButton()) {
				System.exit(0);
			}

			// display high scores if 'high scores' button is clicked in main menu
			if (e.getSource() == mainMenuView.getScoresButton()) {
				mainMenuView.frame.setEnabled(false);
				scoresList.clear();
				scoresList.trimToSize();
				try (FileInputStream fis = new FileInputStream("scores.ser");
						ObjectInputStream ois = new ObjectInputStream(fis)) {

					counter = (Counter) ois.readObject();
					serializedIndex = counter.getCounter();
					System.out.println(serializedIndex + "is the serializedIndex");
					
						for (int i = 0; i < serializedIndex; i++) {
							score = (ScoreModel) ois.readObject();
							System.out.println("score is: " + score);
							scoresList.add(score);
						}

				} catch (Exception ex) {
					ex.printStackTrace();
				}

				hsView = new ScoresView();
				for (int i = 0; i < scoresList.size(); i++) {
					hsView.setScores(scoresList.get(i));
					hsView.displayScores(i);
					hsView.getOkButton().addActionListener(new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent arg0) {
							hsView.frame.dispose();
							mainMenuView.frame.setEnabled(true);
							mainMenuView.frame.setVisible(true);
						}
					});
				}
			}

			// if 'R E S U M E' button is clicked in main menu
			if (e.getSource() == mainMenuView.getResumeButton()) {
				loadGame();
				startTimer();
				deserialize();
				if (!profile.get(0).equals("s")) {
					String p1Label = gameModel.getActivePlayer() == gameModel.getPlayer1() ? "player1" : "player2";
					gameView.setActivePlayerLight(p1Label);
				}
			}

			// actions performed if 'O P T I O N S' button is clicked in main menu
			if (e.getSource() == mainMenuView.getOptionsButton()) {

				// hide main menu when switching to options menu.
				mainMenuView.frame.setVisible(false);

				// setup and display options menu
				optionsView.setOptionsMenu();
				if ((profile.size() != 0) || (firstTimeProfileSave == true)) {
					loadProfile();
					// used when options button is clicked more than once within the one game
					// session
					loadProfileUI();
				}
				// Add listeners to options menu
				optionsView.addOptionsViewListener(new OptionsViewListener());

				// marker to ensure loading the "profile" file version of profile is loaded into
				// options menu UI

				// override system.exit(0) on options menu to return to main menu
				optionsView.frame.addWindowListener(new WindowAdapter() {
					@Override
					public void windowClosing(WindowEvent e) {
						optionsView.frame.setVisible(false);
						mainMenuView.frame.setVisible(true);
					}
				});
			}

			// Initialize game when 'N E W G A M E' button is clicked in main menu
			if (e.getSource() == mainMenuView.getPlayButton()) {
				loadGame();

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

				String p1Label = gameModel.getActivePlayer() == gameModel.getPlayer1() ? "player1" : "player2";
				gameView.setActivePlayerLight(p1Label);
				startTime = System.currentTimeMillis();

				timer.start();
			}
		}// End of Action performed

		void loadGame() {
			loadProfile();
			gameModel.initializeParameters(profile);
			mainMenuView.frame.setVisible(false);
			gameView.displayGameWindow(profile);
			gameView.addGameViewListener(new GameListener());
			gameView.addBackgroundButtonListener(new BackgroundButtonListener());

			gameView.addWindowListener(new WindowAdapter() {

				public void windowClosing(WindowEvent e) {
					if (!gameModel.getActivePlayer().getName().equals("Computer")) {
						stopTimer();
						switch (gameModel.getInterruptionMessage(profile.get(0), gameView)) {
						case -1:
						case 0:// resuming game
							startTimer();
							gameView.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
							break;
						case 1:// go to main menu
							serialize();
							gameModel = new MemoryGame();
							gameView.dispose();
							gameView = new GameView();
							gameView.setVisible(false);
							mainMenuView.frame.setVisible(true);
							mainMenuView.getResumeButton().setEnabled(true);

							break;
						case 2:// quit game
							serialize();
							System.exit(0);
							break;
						default:
							gameView.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
						}
					}
				}
			});

		}

		@SuppressWarnings("null")
		void serialize() {
			try {
				temp.clear();
				// create a new file with an ObjectOutputStream

				serializeFile.createNewFile(); // if file already exists will do nothing

				FileOutputStream fos = new FileOutputStream(serializeFile, false);
				ObjectOutputStream oos = new ObjectOutputStream(fos);

				// write something in the file
				oos.writeObject(profile.get(0));// game mode
				oos.writeObject(profile.get(1));// board size
				oos.writeObject(profile.get(2));// player 1
				oos.writeObject(gameModel.getPlayer1().getScore());
				oos.writeObject(gameModel.getPlayer1().getTries());
				if (!profile.get(0).equals("s")) {
					oos.writeObject(profile.get(3));// player 2
					oos.writeObject(gameModel.getPlayer2().getScore());
					oos.writeObject(gameModel.getPlayer2().getTries());
				}
				oos.writeObject(profile.get(4));// difficulty
				oos.writeObject(gameView.getButtonState());
//				oos.writeObject(gameModel.getMissedButtons());
				oos.writeObject(gameModel.getActivePlayer().getName());

				// save current icons
				oos.writeObject(gameView.getCurrentIcons());

				// save removed guessed cards
				for (int i = 0; i < Integer.parseInt(profile.get(1)) * Integer.parseInt(profile.get(1)); i++) {
					if (gameModel.getCards().get(i).getState() == CardState.NONE) {
						temp.add(i);
					}
				}
				oos.writeObject(temp);
				oos.writeObject(startTime);
				oos.writeObject(stopTime);

				oos.close();
				fos.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		@SuppressWarnings({ "unchecked", "resource", "rawtypes" })
		void deserialize() {

			if (mainMenuView.getResumeButton().isEnabled() == true) {
				try {
					FileInputStream fis = new FileInputStream("data.txt");
					ObjectInputStream ois = new ObjectInputStream(fis);

					System.out.println(ois.readObject()); // game mode
					System.out.println(ois.readObject()); // board size
					System.out.println(ois.readObject()); // player 1
					// set player 1 score
					gameModel.getPlayer1().setScore(Integer.parseInt(ois.readObject().toString()));
					gameView.setScore1Label(gameModel.getPlayer1().getScore());

					// set player 1 tries
					gameModel.getPlayer1().setTries(Integer.parseInt(ois.readObject().toString()));
					if (!profile.get(0).equals("s")) {

						System.out.println(ois.readObject()); // player 2
						// set player 2 score
						gameModel.getPlayer2().setScore(Integer.parseInt(ois.readObject().toString()));
						gameView.setScore2Label(gameModel.getPlayer2().getScore());

						// set player 2 tries
						gameModel.getPlayer2().setTries(Integer.parseInt(ois.readObject().toString()));
					}
					System.out.println(ois.readObject()); // difficulty

					// showRemovedIcons button state
					if ((boolean) ois.readObject()) {
						gameView.getMatchesButton().doClick();
					}

					// restoring active player
					if (gameModel.getPlayer1().getName().equals(ois.readObject().toString())) {
						gameModel.setActivePlayer(gameModel.getPlayer1());
					} else {
						gameModel.setActivePlayer(gameModel.getPlayer2());
					}

					// restoring currentIcons
					temp.clear();
					temp = (ArrayList) ois.readObject();

					// Verify list data
					for (int i = 0; i < temp.size(); i++) {
						gameView.getCurrentIcons().set(i, temp.get(i));
					}
					temp.clear();
					temp.trimToSize();

					// restore removed guessed cards
					temp = (ArrayList) ois.readObject();
					for (int i = 0; i < temp.size(); i++) {
						gameView.updateCardBoard(temp.get(i));
						gameView.removeCard(temp.get(i));
						gameModel.getCards().get(temp.get(i)).updateCard(CardState.NONE);
					}

					startTime = Long.parseLong(ois.readObject().toString());
					stopTime = Long.parseLong(ois.readObject().toString());
					startTime = System.currentTimeMillis() - stopTime + startTime;

					ois.close();
					fis.close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

		}
	} // End of MainMenuViewListener

	private class ClockListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			gameView.updateClock(startTime);
		}
	}

	class OptionsViewListener implements ActionListener {

		String player1 = "", player2 = "", player3 = "";
		String selectedGameMode, board, iconSet, difficulty;

		// Actions to perform when 'save' is clicked in options menu
		public void actionPerformed(ActionEvent e) {

			if (optionsView.getGameMode().getSelection() == null) {
				Toolkit.getDefaultToolkit().beep();
				JOptionPane.showMessageDialog(null, "Please choose game mode!", "Memory",
						JOptionPane.INFORMATION_MESSAGE);
				return;
			}

			if (optionsView.getDimensions().getSelection() == null) {
				Toolkit.getDefaultToolkit().beep();
				JOptionPane.showMessageDialog(null, "Please select board Size!", "Memory",
						JOptionPane.INFORMATION_MESSAGE);
				return;
			}

			if (e.getSource() == optionsView.getSaveButton()) {
				if (optionsView.getComputerButton().isSelected()) {
					if (optionsView.getrButtonGroup().getSelection() == null) {
						Toolkit.getDefaultToolkit().beep();
						JOptionPane.showMessageDialog(null, "Please select difficulty level!", "Memory",
								JOptionPane.INFORMATION_MESSAGE);
						return;
					}
				}
				// V A L I D A T I O N
				// narrowing down possible combinations of selections of game mode, board size
				// and player names.
				if ((optionsView.getGameMode().getSelection() != null)
						&& (optionsView.getDimensions().getSelection() != null)) {
					if (optionsView.getSoloButton().isSelected()) {
						if (optionsView.getListModel().getSize() == 0) {
							Toolkit.getDefaultToolkit().beep();
							JOptionPane.showMessageDialog(null, "Please add player(s)!", "Memory",
									JOptionPane.INFORMATION_MESSAGE);
							return;
						}
						player1 = optionsView.getListModel().getElementAt(0);
					}
					// V A L I D A T I O N
					// Play against computer
					if (optionsView.getComputerButton().isSelected() || optionsView.getSoloButton().isSelected()) {
						if (optionsView.getListModel().getSize() != 1) {
							Toolkit.getDefaultToolkit().beep();
							JOptionPane.showMessageDialog(null, "Please register one player!", "Memory",
									JOptionPane.INFORMATION_MESSAGE);

							return;
						}
					} else if (optionsView.getHumanButton().isSelected()) {
						if (optionsView.getListModel().getSize() != 2) {
							Toolkit.getDefaultToolkit().beep();
							JOptionPane.showMessageDialog(null, "Please register 2 players!", "Memory",
									JOptionPane.INFORMATION_MESSAGE);
							return;
						}
					}

					if (optionsView.getComputerButton().isSelected()) {
						player1 = optionsView.getListModel().getElementAt(0);
						player2 = "Computer";
					} else if (optionsView.getHumanButton().isSelected()) {
						player1 = optionsView.getListModel().getElementAt(0);
						player2 = optionsView.getListModel().getElementAt(1);
					} else if (optionsView.getSoloButton().isSelected()) {
						player1 = optionsView.getListModel().getElementAt(0);
					}

					// Writing to profile file.
					if (optionsView.getSoloButton().isSelected()) {
						selectedGameMode = optionsView.getSoloButton().getActionCommand();
					} else if (optionsView.getHumanButton().isSelected()) {
						selectedGameMode = optionsView.getHumanButton().getActionCommand();
					} else if (optionsView.getComputerButton().isSelected()) {
						selectedGameMode = optionsView.getComputerButton().getActionCommand();
						for (AbstractButton button : optionsView.getListRadioButton()) {
							if (button.isSelected()) {
								difficulty = button.getActionCommand();
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

					iconSet = (String) optionsView.getIconSetComboBox().getSelectedItem();

					if (profile.size() != 0) {
						if (!selectedGameMode.equals(profile.get(0)) || !board.equals(profile.get(1))
								|| !player1.equals(profile.get(2))
								|| (!profile.get(0).equals("s") && !player2.equals(profile.get(3)))
								|| (optionsView.getComputerButton().isSelected() && !difficulty.equals(profile.get(4)))
								|| !iconSet.equals(profile.get(5))) {
							if (mainMenuView.getResumeButton().isEnabled()) {
								int dialogButton = JOptionPane.YES_NO_OPTION;
								int dialogResult = JOptionPane.showConfirmDialog(null,
										"your current game will be lost! \n                 coninue?", "Warning",
										dialogButton);
								if (dialogResult == JOptionPane.NO_OPTION) {
									return;
								}
							}

							removeSerialization();
							mainMenuView.getResumeButton().setEnabled(false);
						}
					}

					StringBuffer sbr = new StringBuffer();
					sbr.append("mode:" + selectedGameMode + '\n');
					sbr.append("boardsize:" + board + '\n');
					sbr.append("player1:" + player1 + '\n');
					sbr.append("player2:" + player2 + '\n');
					sbr.append("difficulty:" + difficulty + '\n');
					sbr.append("iconSet:" + iconSet + '\n');
					sbr.append("playable:true");

					FileOutputStream fos = null;
					try {
						fos = new FileOutputStream("profile.txt");
						fos.write(sbr.toString().getBytes());
						fos.close();
					} catch (FileNotFoundException e1) {
						e1.printStackTrace();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					// End of writing to a file
					sbr.delete(0, sbr.length());
					firstTimeProfileSave = true;
					optionsView.frame.setVisible(false);
					mainMenuView.getPlayButton().setEnabled(true);
					mainMenuView.frame.setVisible(true);
				}
			}
		}
	}

	class BackgroundButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			if (arg0.getSource() == gameView.getMatchesButton()) {
				gameView.switchBackground();
			}
		}
	}

	class GameListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			// Double loop to find the clicked button
			if (gameModel.getActivePlayer().getClass() != null) {
				for (int i = 0; i < gameView.getIconButtons().size(); i++) {

					// selecting the clicked card/button
					if ((e.getSource() == gameView.getIconButtons().get(i)) && (validClicksOnCards < 2)
							&& (!gameModel.getActivePlayer().getName().equals("Computer"))) {

						validClicksOnCards++;
						gameModel.setSelectedCard(gameModel.getCards().get(i));
						try {
							gameModel.buttonIsOpen();
						} catch (ButtonNotAvailableException e1) {
							System.err.println("IndexOutOfBoundsException: " + e1.getMessage());
							validClicksOnCards--;
						}
						// controls and if card is free opens it and does necessary assignments for Card
						// and it's icon.

						if ((secondNumber == 0) && (gameModel.move(i))) {
							if (validClicksOnCards == 1) {
								firstNumber = gameView.getCurrentIcons().get(i);
							} else if (validClicksOnCards == 2) {
								secondNumber = gameView.getCurrentIcons().get(i);

							}
							gameView.updateCardBoard(i);
						}
						if (secondNumber != 0) {

							if (!gameModel.getStatus(firstNumber, secondNumber)) {
								new java.util.Timer().schedule(new java.util.TimerTask() {
									@Override
									public void run() {

										gameModel.addToMemory(firstNumber, secondNumber, gameView, profile.get(4));
										gameView.restoreDefaultIcon(
												gameModel.getCards().indexOf(gameModel.getFirstCard()));
										gameView.restoreDefaultIcon(
												gameModel.getCards().indexOf(gameModel.getSecondCard()));
										firstNumber = 0;
										secondNumber = 0;
										gameModel.revoke();
										updateScoreBoard();
										validClicksOnCards = 0;
										if (!profile.get(0).equals("s")) {
											gameView.switchActivePlayerLight(gameModel.switchActivePlayer());
											if (gameModel.getActivePlayer().getClass().getSimpleName()
													.equals("ComputerPlayer"))
												playTheComputer();
										}
									}
								}, 1000);

							} else if (gameModel.getStatus(firstNumber, secondNumber)) {
								new java.util.Timer().schedule(new java.util.TimerTask() {
									@Override
									public void run() {

										gameModel.addToMemory(firstNumber, secondNumber, gameView, profile.get(4));
										gameView.removeCard(gameModel.getCards().indexOf(gameModel.getFirstCard()));
										gameView.removeCard(gameModel.getCards().indexOf(gameModel.getSecondCard()));

										firstNumber = 0;
										secondNumber = 0;
										gameModel.update();
										updateScoreBoard();
										validClicksOnCards = 0;
										if (gameModel.gameOver()) {
											removeSerialization();

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

	int generateDifficultyFactor() {
		factor = 50;
		switch (Integer.parseInt(profile.get(4))) {
		case 3:
			factor = factor * 14;
			break;
		case 2:
			factor = factor * 9;
			break;
		case 1:
			factor = factor * 6;
			break;
		default:// novice level
			factor = factor * 20;
			break;
		}
		return factor;
	}

	public void playTheComputer() {
		Card card;

		gameView.setCursor(waitingCursor);

		do {
			card = gameModel.getRandomCardIndex(profile.get(4), gameView);

			try {
				Thread.sleep(generateDifficultyFactor());
			} catch (InterruptedException interrupt) {
				interrupt.printStackTrace();
			}
			gameModel.setSelectedCard(card);
			validClicksOnCards++;

			if ((secondNumber == 0) && (gameModel.move(gameModel.getCards().indexOf(card)))) {

				if (validClicksOnCards == 1) {
					firstNumber = gameView.getCurrentIcons().get(gameModel.getCards().indexOf(card));

				} else if (validClicksOnCards == 2) {
					secondNumber = gameView.getCurrentIcons().get(gameModel.getCards().indexOf(card));
				}
				gameView.updateCardBoard(gameModel.getCards().indexOf(card));
			}

			if (secondNumber != 0) {

//				I F   N O T   M A T C H E D
				if (!gameModel.getStatus(firstNumber, secondNumber)) {

					try {
						Thread.sleep(generateDifficultyFactor());
					} catch (InterruptedException interrupt) {
						interrupt.printStackTrace();
					}
					gameModel.addToMemory(firstNumber, secondNumber, gameView, profile.get(4));
					gameView.restoreDefaultIcon(gameModel.getCards().indexOf(gameModel.getFirstCard()));
					gameView.restoreDefaultIcon(gameModel.getCards().indexOf(gameModel.getSecondCard()));
					validClicksOnCards = 0;
					firstNumber = 0;
					secondNumber = 0;
					gameModel.revoke();
					updateScoreBoard();
					gameView.switchActivePlayerLight(gameModel.switchActivePlayer());
					gameView.setCursor(normalCursor);

//					I F    M A T C H E D
				} else if (gameModel.getStatus(firstNumber, secondNumber)) {
					try {
						Thread.sleep(generateDifficultyFactor());
					} catch (InterruptedException interrupt) {
						interrupt.printStackTrace();
					}

					gameModel.addToMemory(firstNumber, secondNumber, gameView, profile.get(4));
					gameView.removeCard(gameModel.getCards().indexOf(gameModel.getFirstCard()));
					gameView.removeCard(gameModel.getCards().indexOf(gameModel.getSecondCard()));
					validClicksOnCards = 0;
					firstNumber = 0;
					secondNumber = 0;
					gameModel.update();
					updateScoreBoard();
					if (gameModel.gameOver()) {
						gameView.setCursor(normalCursor);
						removeSerialization();
						resetGame();
						return;
					}
				}
			}
		} while (gameModel.getActivePlayer().getName().equals("Computer") && !gameModel.gameOver());
	}

	void updateScoreBoard() {
		if (gameModel.getActivePlayer().equals(gameModel.getPlayer1())) {
			gameView.setScore1Label(gameModel.getActivePlayer().getScore());
		} else {
			gameView.setScore2Label(gameModel.getActivePlayer().getScore());
		}
	}

	void resetGame() {
		mainMenuView.getResumeButton().setEnabled(false);
		stopTimer();
		if (profile.get(0).equals("s")) {
			serializeScore();
			mainMenuView.getScoresButton().setEnabled(true);

		}

		switch (gameModel.getMessage(profile.get(0))) {
		case 0:// play
			gameModel = new MemoryGame();
			gameView.dispose();
			gameView = new GameView();
			mainMenuView.getPlayButton().doClick();
			break;
		case 1:// main menu
			gameModel = new MemoryGame();
			gameView.dispose();
			gameView = new GameView();
			gameView.setVisible(false);
			mainMenuView.frame.setVisible(true);
			mainMenuView.getResumeButton().setEnabled(false);

			break;
		case 2:// quit
			System.exit(0);
			break;
		default:
			System.exit(0);
		}
	}

	void checkDataFile() {
		try {
			serializeFile.createNewFile(); // if file already exists will do nothing
			br = new BufferedReader(new FileReader("data.txt"));
			if (br.readLine() != null) {
				mainMenuView.getResumeButton().setEnabled(true);
			}
			br.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	void checkScoreFile() {
		serializedIndex = 0;
		

			if (!serializeScore.exists()) {
				try (FileOutputStream fosScore = new FileOutputStream(serializeScore);
						ObjectOutputStream oosScore = new ObjectOutputStream(fosScore)) {
				counter = new Counter();
				counter.setCounter(0);
				serializeScore.createNewFile(); // if file already exists will do nothing
				mainMenuView.getScoresButton().setEnabled(false);
				oosScore.writeObject(counter);
				System.out.println("doesn't exist");
			} 
		 catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception exp) {
			exp.printStackTrace();
		}}
	}

	void serializeScore() {
		scoresList.clear();
		scoresList.trimToSize();
//			if(!mainMenuView.getScoresButton().isEnabled()) {
//				serializedIndex=0;
//				try {
//					FileOutputStream fosScore = new FileOutputStream(serializeScore);
//					ObjectOutputStream oosScore = new ObjectOutputStream(fosScore);
//						oosScore.write(serializedIndex);				
//					oosScore.close();
//					fosScore.close();
//				} catch (Exception ex) {
//					ex.printStackTrace();
//				}	
//			} else if(mainMenuView.getScoresButton().isEnabled()) {
		try (FileInputStream fis = new FileInputStream("scores.ser");
				ObjectInputStream ois = new ObjectInputStream(fis)) {
			counter = (Counter) ois.readObject();
			serializedIndex = counter.getCounter();
			System.out.println("serializedIndex after first reading from file with serializeScore() is: " + serializedIndex);
			for (int i = 0; i < serializedIndex; i++) {
				score = (ScoreModel) ois.readObject();
				scoresList.add(score);
			}
			System.out.println("scoresList size before serializing= " + scoresList.size());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (Exception exp) {
			exp.printStackTrace();
		}

		score = new ScoreModel();
		score.setPlayerName(profile.get(2));
		score.setGuessRatio((100 * ((Integer.parseInt(profile.get(1)) * Integer.parseInt(profile.get(1))) / 2)
				/ gameModel.getPlayer1().getTries()));
		score.setBoardSize(profile.get(1));
		score.setDuration(stopTime - startTime);
		System.out.println("System.currentTimeMillis() - stopTime + startTime = " + score.getDuration());
		score.setDate(LocalDateTime.now());
		if (scoresList.size() == 0) {
			scoresList.add(score);
			serializedIndex++;
			System.out.println("==0 serializedIndex= " + serializedIndex);

		} else if ((scoresList.size() < 10) && (scoresList.size() > 0)) {
			//if score.getGuessRatio()> the smaller one in list
			//else add
			if (score.getGuessRatio() > scoresList.get(scoresList.size()-1).getGuessRatio() ) {
			
				for (int i = 0; i < scoresList.size(); i++) {
					if (score.getGuessRatio() > scoresList.get(i).getGuessRatio()) {
						scoresList.add(i, score);
						serializedIndex++;
						System.out.println(">9th index serializedIndex= " + serializedIndex);
						break;
					}
				}	
			} else {
				scoresList.add(score);
				serializedIndex++;
				System.out.println("added, smaller than existant " + serializedIndex);				
			}
		}  else if (scoresList.size() == 10) {
			if (score.getGuessRatio() > scoresList.get(9).getGuessRatio()) {
				for (int i = 0; i < scoresList.size(); i++) {
					if (score.getGuessRatio() > scoresList.get(i).getGuessRatio()) {
						scoresList.remove(9);
						scoresList.add(i, score);
						System.out.println(">9th index serializedIndex= " + serializedIndex);
						break;
					}
				}
			}
		}
		try (FileOutputStream fosScore = new FileOutputStream(serializeScore);
				ObjectOutputStream oosScore = new ObjectOutputStream(fosScore)) {
			counter.setCounter(serializedIndex);
			oosScore.writeObject(counter);
			int g=0;
			for (int i = 0; i < scoresList.size(); i++) {
				oosScore.writeObject(scoresList.get(i));
				g++;
			}
			System.out.println("g is: "+g);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		System.out.println("exiting serialize()");
	}
}