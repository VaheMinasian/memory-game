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
import java.util.ArrayList;
import java.util.logging.Handler;

import javax.swing.AbstractButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.Timer;

import model.Card;
import model.Card.CardState;
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

	Cursor waitingCursor = new Cursor(Cursor.WAIT_CURSOR);
	Cursor normalCursor = new Cursor(Cursor.DEFAULT_CURSOR);
	private MemoryGame gameModel;
	private GameView gameView;
	private final ClockListener clock = new ClockListener();
	private int validClicksOnCards = 0;
	private boolean firstTimeProfileSave = false;
	String activePlayerLabel = null;
	int firstNumber, secondNumber;
	private int factor;
	private long startTime, stopTime;
	Timer timer = new Timer(53, clock);
	ArrayList<Integer> temp = new ArrayList<>();
	File serializeFile = new File("resources/data.txt");
	BufferedReader br;

	public MenuController() {
	}

	void checkDataFile() {
		try {
			serializeFile.createNewFile(); // if file already exists will do nothing
			br = new BufferedReader(new FileReader("resources/data.txt"));
			if (br.readLine() != null) {
				mainMenuView.getResumeButton().setEnabled(true);
//				return;
			}
			br.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public MenuController(MainMenuView mView, OptionsView oView, MemoryGame mGame, GameView gView) {
		mainMenuView = mView;
		optionsView = oView;
		gameModel = mGame;
		gameView = gView;
		isPlayable();

		mainMenuView.addMainMenuViewListener(new MainMenuViewListener());
		checkDataFile();
	}

	void startTimer() {
		timer = new Timer(53, clock);
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

	void removeSerialization() {
		try {
			FileOutputStream fos = new FileOutputStream("resources/data.txt");
			fos.write(("").getBytes());
			fos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		System.out.println("Icon set combo box value is: " + optionsView.getIconSetComboBox().getSelectedItem());

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

			if (e.getSource() == mainMenuView.getResumeButton()) {
				mainMenuView.getPlayButton().doClick();
				deserialize();
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

			// Initialize game when 'P L A Y' button is clicked in main menu
			if (e.getSource() == mainMenuView.getPlayButton()) {
				loadProfile();
				gameModel.initializeParameters(profile);
				mainMenuView.frame.setVisible(false);
				gameView.displayGameWindow(profile);
//				gameView.getBoardPanel().setBoardSize(gameView.getBoardDimension());
				gameView.addGameViewListener(new GameListener());
				gameView.addBackgroundButtonListener(new BackgroundButtonListener());
//				gameModel.setActivePlayer(gameModel.getPlayer1());
//				if (profile.get(0).equals("c") && !profile.get(4).equals("4")) {
//				gameModel.setMemorySize(profile.get(4));

				gameView.addWindowListener(new WindowAdapter() {

					public void windowClosing(WindowEvent e) {
						System.out.println("window closing listener should work here");
						if (!gameModel.getActivePlayer().getName().equals("Computer")) {
							System.out.println("timer should stop here");
							stopTimer();
							System.out.println("stoptime before switching: " + stopTime);
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
				startTime = System.currentTimeMillis();
				System.out.println("in listener " + startTime);
				timer.start(); // gameView.getTimerLabel().setText(sdf.format(new java.util.Date()));
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
			}
		}// End of Action performed

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
				for (int i = 0; i < Integer.parseInt(profile.get(1)); i++) {
					for (int j = 0; j < Integer.parseInt(profile.get(1)); j++) {
						if (gameModel.getCards()[i][j].getState() == CardState.NONE) {
							temp.add(i);
							temp.add(j);
						}
					}
				}
				oos.writeObject(temp);
				oos.writeObject(startTime);
				oos.writeObject(stopTime);
				System.out.println("temp items are: " + temp);
//				oos.writeObject(gameModel.getCardIndexX());
//				oos.writeObject(gameModel.getCardIndexY());
//				oos.writeObject(gameModel.getSavedIndexX());
//				oos.writeObject(gameModel.getSavedIndexY());
//				oos.writeObject(gameModel.getSavedIcon());
//				oos.writeObject(gameModel.getTempIndexValue());
//				oos.writeObject(gameModel.getSavedCardNumber());

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
					FileInputStream fis = new FileInputStream("resources/data.txt");
					ObjectInputStream ois = new ObjectInputStream(fis);

					System.out.println(ois.readObject()); // game mode
					System.out.println(ois.readObject()); // board size
					System.out.println(ois.readObject()); // player 1
					// set player 1 score
					gameModel.getPlayer1().setScore(Integer.parseInt(ois.readObject().toString()));
					System.out.println("player 1 score is: " + gameModel.getPlayer1().getScore());// check that player 1
																									// score is restored
					gameView.setScore1Label(gameModel.getPlayer1().getScore());

					// set player 1 tries
					gameModel.getPlayer1().setTries(Integer.parseInt(ois.readObject().toString()));
					System.out.println("player 1 tries is: " + gameModel.getPlayer1().getTries());// check that player 1
																									// tries is restored

					if (!profile.get(0).equals("s")) {

						System.out.println(ois.readObject()); // player 2
						// set player 2 score
						gameModel.getPlayer2().setScore(Integer.parseInt(ois.readObject().toString()));
						System.out.println("player 2 score is: " + gameModel.getPlayer2().getScore());// check that
																										// player 2
																										// score is
																										// restored
						gameView.setScore2Label(gameModel.getPlayer2().getScore());

						// set player 2 tries
						gameModel.getPlayer2().setTries(Integer.parseInt(ois.readObject().toString()));
						System.out.println("player 2 tries is: " + gameModel.getPlayer2().getTries());// check that
																										// player 2
																										// tries is
																										// restored
					}
					System.out.println(ois.readObject()); // difficulty

					// showRemovedIcons button state
					if ((boolean) ois.readObject()) {
						gameView.getMatchesButton().doClick();
					}
					System.out.println("Button state is: " + gameView.getButtonState());// check if button is pressed

					// restoring active player
					if (gameModel.getPlayer1().getName().equals(ois.readObject().toString())) {
						gameModel.setActivePlayer(gameModel.getPlayer1());
					} else
						gameModel.setActivePlayer(gameModel.getPlayer2());
					System.out.println("deserialized current player is: " + gameModel.getActivePlayer().getName());

					// restoring currentIcons
					temp.clear();
					temp = (ArrayList) ois.readObject();

					// Verify list data
					for (int i = 0; i < temp.size(); i++) {
						gameView.getCurrentIcons().set(i, temp.get(i));
						System.out.println(gameView.getCurrentIcons().get(i));
					}
					temp.clear();
					temp.trimToSize();

					// restore removed guessed cards
					temp = (ArrayList) ois.readObject();
					System.out.println(temp);
					for (int i = 0; i < temp.size(); i += 2) {
						gameView.updateCardBoard(temp.get(i), temp.get(i + 1));
						gameView.removeCard(gameModel.getCards()[temp.get(i)][temp.get(i + 1)]);
						gameModel.getCards()[temp.get(i)][temp.get(i + 1)].updateCard(CardState.NONE);

//					oos.writeObject(stopTime);
					}

					startTime = Long.parseLong(ois.readObject().toString());
					stopTime = Long.parseLong(ois.readObject().toString());
					System.out.println("startTime is " + startTime);
					System.out.println("stopTime is " + stopTime);
//				timer = new Timer(53, clock);
					startTime = System.currentTimeMillis() - stopTime + startTime;
//				timer.start();

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

			if (e.getSource() == optionsView.getSaveButton()) {
				if (optionsView.getrButtonGroup().getSelection() == null) {
					System.out.println("no radio button selected");

				} else {
					System.out.println(optionsView.getrButtonGroup().getSelection());
				}
				// V A L I D A T I O N
				// narrowing down possible combinations of selections of game mode, board size
				// and player names.
				if ((optionsView.getGameMode().getSelection() != null)
						&& (optionsView.getDimensions().getSelection() != null)) {
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
						} else if (optionsView.getrButtonGroup().getSelection() == null) {
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
						System.out.println("profile is: " + profile);
						if (selectedGameMode != profile.get(0) || board != profile.get(1) || player1 != profile.get(2)
								|| player2 != profile.get(3) || difficulty != profile.get(4)
								|| iconSet != profile.get(5)) {
							removeSerialization();
							mainMenuView.getResumeButton().setEnabled(false);
							System.out.println("resume button set to false");
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
						fos = new FileOutputStream("resources/profile.txt");
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
				} else {
					Toolkit.getDefaultToolkit().beep();
					JOptionPane.showMessageDialog(null, "Please Fill all the necessary fields before saving", "Memory",
							JOptionPane.INFORMATION_MESSAGE);
					return;

				}
			}
		}
	}

	class BackgroundButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			if (arg0.getSource() == gameView.getMatchesButton()) {
				gameView.switchBackground();
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
//				
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
								if (validClicksOnCards == 1) {
									firstNumber = gameView.getCurrentIcons().get(i * gameView.getBoardDimension() + j);
									gameModel.getFirstCard().setIsClicked();
								} else if (validClicksOnCards == 2) {
									secondNumber = gameView.getCurrentIcons().get(i * gameView.getBoardDimension() + j);
									gameModel.getSecondCard().setIsClicked();

								}
								// if(singletonModel.move(i, j));
								gameView.updateCardBoard(i, j);
							}
							if (secondNumber != 0) {

								if (!gameModel.getStatus(firstNumber, secondNumber)) {
									new java.util.Timer().schedule(new java.util.TimerTask() {
										@Override
										public void run() {
//											if (profile.get(0).equals("c") && !profile.get(4).equals("n")) {
////												gameModel.setMissedButtons(firstNumber, secondNumber);
//											}
											gameModel.addToMemory(firstNumber, secondNumber, gameView, profile.get(4));
											gameView.restoreDefaultIcon(gameModel.getFirstCard());
											gameView.restoreDefaultIcon(gameModel.getSecondCard());
											firstNumber = 0;
											secondNumber = 0;
											gameModel.revoke();
											updateScoreBoard();
											validClicksOnCards = 0;
											if (!profile.get(0).equals("s")) {
												activePlayerLabel = gameModel.switchActivePlayer();
												gameView.switchActivePlayerLight(activePlayerLabel);
												System.out.println("active player now is the player named: "
														+ gameModel.getActivePlayer().getClass().getSimpleName());
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
											gameView.removeCard(gameModel.getFirstCard());
											gameView.removeCard(gameModel.getSecondCard());
//											gameView.removeIcons(firstNumber, secondNumber);

//											gameModel.nullifyButtonsIndex();
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
		int xIndexComp;
		int yIndexComp;

		gameView.setCursor(waitingCursor);
		System.out.println("Beginning of StartGame...active player is: " + gameModel.getActivePlayer().getName());

		do {
			card = gameModel.getRandomCardIndex(profile.get(4), gameView);
			xIndexComp = card.getCardIndex()[0];
			yIndexComp = card.getCardIndex()[1];
			System.out.println("after assigning x and y are: " + xIndexComp + ", " + yIndexComp);

			try {
				Thread.sleep(generateDifficultyFactor());
			} catch (InterruptedException interrupt) {
				interrupt.printStackTrace();
			}
			gameModel.setSelectedCard(card);
			validClicksOnCards++;

			if ((secondNumber == 0) && (gameModel.move(xIndexComp, yIndexComp))) {

				if (validClicksOnCards == 1) {
					firstNumber = gameView.getCurrentIcons()
							.get(xIndexComp * gameView.getBoardDimension() + yIndexComp);
					gameModel.getFirstCard().setIsClicked();
					System.out.println("FirstNumber is: " + firstNumber);

				} else if (validClicksOnCards == 2) {
					secondNumber = gameView.getCurrentIcons()
							.get(xIndexComp * gameView.getBoardDimension() + yIndexComp);
					gameModel.getSecondCard().setIsClicked();

					System.out.println("SecondNumber is: " + secondNumber);
				}
				gameView.updateCardBoard(xIndexComp, yIndexComp);
			}

			if (secondNumber != 0) {
				System.out.println("inside second number !=0");

//				I F   N O T   M A T C H E D
				if (!gameModel.getStatus(firstNumber, secondNumber)) {
					System.out.println("inside !gameModel.getStatus()");

					try {
						Thread.sleep(generateDifficultyFactor());
					} catch (InterruptedException interrupt) {
						interrupt.printStackTrace();
					}
					gameModel.addToMemory(firstNumber, secondNumber, gameView, profile.get(4));
					gameView.restoreDefaultIcon(gameModel.getFirstCard());
					gameView.restoreDefaultIcon(gameModel.getSecondCard());
					validClicksOnCards = 0;
					firstNumber = 0;
					secondNumber = 0;
					gameModel.revoke();
					updateScoreBoard();
					activePlayerLabel = gameModel.switchActivePlayer();
					gameView.switchActivePlayerLight(activePlayerLabel);
					gameView.setCursor(normalCursor);

//					I F    M A T C H E D
				} else if (gameModel.getStatus(firstNumber, secondNumber)) {
					System.out.println("inside (gameModel.getStatus())");
					try {
						Thread.sleep(generateDifficultyFactor());
					} catch (InterruptedException interrupt) {
						interrupt.printStackTrace();
					}

					gameModel.addToMemory(firstNumber, secondNumber, gameView, profile.get(4));
					gameView.removeCard(gameModel.getFirstCard());
					gameView.removeCard(gameModel.getSecondCard());
//					gameView.removeIcons(firstNumber, secondNumber);
					validClicksOnCards = 0;
					firstNumber = 0;
					secondNumber = 0;
					gameModel.update();
					updateScoreBoard();
					if (gameModel.gameOver()) {
						gameView.setCursor(normalCursor);
						System.out.println("going to game over");
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
			System.out.println("player1 update inside updateScoreBoard");
			gameView.setScore1Label(gameModel.getActivePlayer().getScore());
		} else {
			System.out.println(
					"player2 update inside updateScoreBoard... player2 is: " + gameModel.getActivePlayer().getName());
			gameView.setScore2Label(gameModel.getActivePlayer().getScore());
		}
	}

	void resetGame() {
		stopTimer();
		switch (gameModel.getMessage(profile.get(0))) {
		case 0:
			System.out.println("inside case 0");
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