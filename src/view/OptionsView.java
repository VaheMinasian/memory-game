package view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

@SuppressWarnings("serial")
public class OptionsView extends JFrame implements ActionListener, ListSelectionListener {

	public JFrame frame;
	private JPanel emptyPanel, mainPanel, gameModePanel, boardSizePanel, tableButtonsPanel, executionPanel;
	private JToggleButton soloButton, humanButton, computerButton, sqrFour, sqrSix, sqrEight, sqrTen;
	private JButton addButton, removeButton, saveButton;
	private JTextField playerName;
	private static JList<String> list;
	private static DefaultListModel<String> listModel;
	private static OptionsView view = null;
	private ArrayList<JButton> selectedButtons;

	private OptionsView() {
	}

	public static OptionsView getInstance() {
		if (view == null)
			view = new OptionsView();
		return view;
	}

	/*
	 * this method creates the options view dialog for the class SetupView.
	 */
	public void setOptionsMenu() {
		frame = new JFrame("Memory");
		ImageIcon frameIcon = new ImageIcon(OptionsView.class.getResource("/46.png"));

		mainPanel = new JPanel(new GridBagLayout());
		frame.getContentPane().add(mainPanel);
		mainPanel.setPreferredSize(new Dimension(350, 350));
		mainPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
				" G A M E    S E T U P ", TitledBorder.CENTER, TitledBorder.TOP));
		mainPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

		emptyPanel = new JPanel();
		emptyPanel.setPreferredSize(new Dimension(300, 20));

		soloButton = new JToggleButton();
		soloButton.setPreferredSize(new Dimension(70, 50));
		ImageIcon soloImageIcon = new ImageIcon(OptionsView.class.getResource("/solo.png"));
		soloButton.setIcon(soloImageIcon);
		soloButton.setForeground(Color.BLACK);
		soloButton.setBorder(BorderFactory.createEmptyBorder());
		soloButton.setOpaque(false);
		soloButton.setFocusable(false);
		soloButton.setActionCommand("s");

		humanButton = new JToggleButton();
		humanButton.setPreferredSize(new Dimension(70, 50));
		ImageIcon humanImageIcon = new ImageIcon(OptionsView.class.getResource("/human.png"));
		humanButton.setIcon(humanImageIcon);
		humanButton.setForeground(Color.BLACK);
		humanButton.setBorder(BorderFactory.createEmptyBorder());
		humanButton.setOpaque(false);
		humanButton.setFocusable(false);
		humanButton.setActionCommand("h");

		computerButton = new JToggleButton();
		computerButton.setPreferredSize(new Dimension(70, 50));
		ImageIcon computerImageIcon = new ImageIcon(OptionsView.class.getResource("/computer.png"));
		computerButton.setIcon(computerImageIcon);
		computerButton.setForeground(Color.BLACK);
		computerButton.setBorder(BorderFactory.createEmptyBorder());
		computerButton.setOpaque(false);
		computerButton.setFocusable(false);
		computerButton.setActionCommand("c");

		ButtonGroup gameMode = new ButtonGroup();
		gameMode.add(soloButton);
		gameMode.add(humanButton);
		gameMode.add(computerButton);
		gameModePanel = new JPanel();
		gameModePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
				" c h o o s e   g a m e   m o d e ", TitledBorder.CENTER, TitledBorder.TOP));
		gameModePanel.add(soloButton);
		gameModePanel.add(Box.createRigidArea(new Dimension(2, 0)));
		gameModePanel.add(humanButton);
		gameModePanel.add(Box.createRigidArea(new Dimension(2, 0)));
		gameModePanel.add(computerButton);

		sqrFour = new JToggleButton("4x4");
		sqrFour.setFont((new Font("dialog", Font.BOLD, 13)));
		sqrFour.setPreferredSize(new Dimension(65, 38));
		sqrFour.setBorderPainted(false);
		sqrFour.setActionCommand("4");

		sqrSix = new JToggleButton("6x6");
		sqrSix.setFont((new Font("dialog", Font.BOLD, 13)));
		sqrSix.setPreferredSize(new Dimension(65, 38));
		sqrSix.setBorderPainted(false);
		sqrSix.setActionCommand("6");

		sqrEight = new JToggleButton("8x8");
		sqrEight.setFont((new Font("dialog", Font.BOLD, 13)));
		sqrEight.setPreferredSize(new Dimension(65, 38));
		sqrEight.setBorderPainted(false);
		sqrEight.setActionCommand("8");

		sqrTen = new JToggleButton("10x10");
		sqrTen.setFont((new Font("dialog", Font.BOLD, 13)));
		sqrTen.setPreferredSize(new Dimension(75, 38));
		sqrTen.setBorderPainted(false);
		sqrTen.setActionCommand("10");

		ButtonGroup dimensions = new ButtonGroup();
		dimensions.add(sqrFour);
		dimensions.add(sqrSix);
		dimensions.add(sqrEight);
		dimensions.add(sqrTen);

		boardSizePanel = new JPanel();
		boardSizePanel.setPreferredSize(new Dimension(200, 70));
		boardSizePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
				" c h o o s e   b o a r d   s i z e ", TitledBorder.CENTER, TitledBorder.TOP));
		boardSizePanel.add(sqrFour);
		boardSizePanel.add(sqrSix);
		boardSizePanel.add(sqrEight);
		boardSizePanel.add(sqrTen);

		listModel = new DefaultListModel<String>();
		list = new JList<String>(listModel);
		list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		list.addListSelectionListener(this);
		JScrollPane tablePane = new JScrollPane(list);

		tablePane.setPreferredSize(new Dimension(100, 50));

		class AddListener implements ActionListener, DocumentListener {
			private boolean alreadyEnabled = false;
			private JButton button;

			public AddListener(JButton button) {
				this.button = button;
			}

			// Required by ActionListener.
			public void actionPerformed(ActionEvent e) {

				if (((soloButton.isSelected()) || (computerButton.isSelected())) && (listModel.getSize() + 1 > 1)) {
					Toolkit.getDefaultToolkit().beep();
					JOptionPane.showMessageDialog(null,
							"You can have only 1 player in \"solo\" or  \"computer\" game mode!", "Memory",
							JOptionPane.INFORMATION_MESSAGE);
					playerName.setText("");
					return;
				}

				else if ((humanButton.isSelected()) && (listModel.getSize() + 1 > 2)) {
					Toolkit.getDefaultToolkit().beep();
					JOptionPane.showMessageDialog(null, "You can have only 2 players in \"pair\" game mode!", "Memory",
							JOptionPane.INFORMATION_MESSAGE);
					playerName.setText("");
					return;
				}

				String name = playerName.getText();

				// User didn't type in a unique name...
				if (name.equals("") || alreadyInList(name)) {
					Toolkit.getDefaultToolkit().beep();
					JOptionPane.showMessageDialog(null, "The name already exists,  please choose another name",
							"Memory", JOptionPane.INFORMATION_MESSAGE);

					playerName.requestFocusInWindow();
					playerName.selectAll();
					return;
				}

				int index = list.getSelectedIndex();
				if (index == -1) {
					index = 0;
				} else {
					index++;
				}

				listModel.insertElementAt(playerName.getText(), index);
				list.clearSelection();

				playerName.requestFocusInWindow();
				playerName.setText("");

				list.setSelectedIndex(index);
				list.ensureIndexIsVisible(index);
			}

			protected boolean alreadyInList(String name) {
				return listModel.contains(name);
			}

			// Required by DocumentListener.
			public void insertUpdate(DocumentEvent e) {
				enableButton();
			}

			// Required by DocumentListener.
			public void removeUpdate(DocumentEvent e) {
				handleEmptyTextField(e);
			}

			// Required by DocumentListener.
			public void changedUpdate(DocumentEvent e) {
				if (!handleEmptyTextField(e)) {
					enableButton();
				}
			}

			private void enableButton() {
				if (!alreadyEnabled) {
					button.setEnabled(true);
				}
			}

			private boolean handleEmptyTextField(DocumentEvent e) {
				if (e.getDocument().getLength() <= 0) {
					button.setEnabled(false);
					alreadyEnabled = false;
					return true;
				}
				return false;
			}
		}

		addButton = new JButton("add");
		AddListener addListener = new AddListener(addButton);
		addButton.setFont((new Font("dialog", Font.BOLD, 12)));
		addButton.setPreferredSize(new Dimension(55, 28));
		addButton.setActionCommand("addPlayer");
		addButton.addActionListener(addListener);
		addButton.setEnabled(false);

		removeButton = new JButton("remove");
		removeButton.setFont((new Font("dialog", Font.BOLD, 12)));
		removeButton.setPreferredSize(new Dimension(77, 28));
		removeButton.setActionCommand("removePlayer");

		removeButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				DefaultListModel<String> defaultLM = (DefaultListModel<String>) list.getModel();
				if (list.getSelectedIndices().length > 0) {
					int selectedIndices[] = list.getSelectedIndices();
					for (int i = selectedIndices.length - 1; i >= 0; i--) {
						defaultLM.removeElementAt(selectedIndices[i]);
					}
				}

				int size = listModel.getSize();

				if (size == 0) {
					removeButton.setEnabled(false);
				}

				list.ensureIndexIsVisible(list.getSelectedIndex());
			}

		});

		playerName = new JTextField(17);
		playerName.setPreferredSize(new Dimension(200, 30));
		playerName.setFont((new Font("dialog", Font.PLAIN, 14)));

		playerName.addActionListener(addListener);
		playerName.getDocument().addDocumentListener(addListener);
		tableButtonsPanel = new JPanel();
		tableButtonsPanel.add(addButton);
		tableButtonsPanel.add(playerName);
		tableButtonsPanel.add(removeButton);

		saveButton = new JButton("save");
		saveButton.setFont((new Font("dialog", Font.BOLD, 13)));
		saveButton.setPreferredSize(new Dimension(90, 28));
		saveButton.setActionCommand("save");

		executionPanel = new JPanel();
		executionPanel.add(saveButton);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(2, 3, 2, 2);
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.weightx = 1;
		gbc.weighty = 1;

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		mainPanel.add(emptyPanel, gbc);

		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		mainPanel.add(gameModePanel, gbc);

		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.fill = GridBagConstraints.BOTH;
		mainPanel.add(boardSizePanel, gbc);

		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.fill = GridBagConstraints.BOTH;
		mainPanel.add(tablePane, gbc);

		gbc.gridx = 0;
		gbc.gridy = 4;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		mainPanel.add(tableButtonsPanel, gbc);

		gbc.gridx = 0;
		gbc.gridy = 5;
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.SOUTH;
		mainPanel.add(executionPanel, gbc);

		frame.setIconImage(frameIcon.getImage());
		frame.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setVisible(true);
	}

	// set action listener on all setup view buttons
	public void addOptionsViewListener(ActionListener selected) {
		sqrFour.addActionListener(selected);
		sqrSix.addActionListener(selected);
		sqrEight.addActionListener(selected);
		sqrTen.addActionListener(selected);
		soloButton.addActionListener(selected);
		humanButton.addActionListener(selected);
		computerButton.addActionListener(selected);
		saveButton.addActionListener(selected);
	}

	@Override
	public void valueChanged(ListSelectionEvent arg0) {
		if (arg0.getValueIsAdjusting() == false) {

			if (list.getSelectedIndex() == -1) {
				// No selection, disable remove button.
				removeButton.setEnabled(false);

			} else {
				// Selection, enable the remove button.
				removeButton.setEnabled(true);

			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
	}

	public JToggleButton getSqrFour() {
		return sqrFour;
	}

	public JToggleButton getSqrSix() {
		return sqrSix;
	}

	public JToggleButton getSqrEight() {
		return sqrEight;
	}

	public JToggleButton getSqrTen() {
		return sqrTen;
	}

	public JToggleButton getSoloButton() {
		return soloButton;
	}

	public JToggleButton getHumanButton() {
		return humanButton;
	}

	public JToggleButton getComputerButton() {
		return computerButton;
	}

	public static DefaultListModel<String> getListModel() {
		return listModel;
	}

	public static JList<String> getList() {
		return list;
	}

	public JButton getSaveButton() {
		return saveButton;
	}

	public ArrayList<JButton> getSelectedButtons() {
		return selectedButtons;
	}

	public void setSelectedButtons(ArrayList<JButton> selectedButtons) {
		this.selectedButtons = selectedButtons;
	}

	public void setSelectedButton(JButton selectedButton) {
		selectedButtons.add(selectedButton);
	}

	public JTextField getPlayerName() {
		return playerName;
	}

	public void setPlayerName(JTextField playerName) {
		this.playerName = playerName;
	}
}