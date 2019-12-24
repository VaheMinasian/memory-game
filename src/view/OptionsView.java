package view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
import javax.swing.JRadioButton;
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
	private JPanel emptyPanel, mainPanel, gameModePanel, boardSizePanel, tableButtonsPanel, executionPanel, levelPanel;
	private JToggleButton soloButton, humanButton, computerButton, sqrFour, sqrSix, sqrEight, sqrTen;
	private JButton addButton, removeButton, saveButton;
	private JTextField playerNameTextField;
	private JList<String> list;
	private DefaultListModel<String> listModel;

	public OptionsView() {

	}

//	  this method creates the options view dialog for the class SetupView. 
	public void setOptionsMenu() {
		frame = new JFrame("Memory");
		ImageIcon frameIcon = new ImageIcon(OptionsView.class.getResource("/46.png"));
		frame.setIconImage(frameIcon.getImage());

		mainPanel = new JPanel(new GridBagLayout());
		frame.getContentPane().add(mainPanel);
		mainPanel.setPreferredSize(new Dimension(370, 420));
		mainPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
				" G A M E    S E T U P ", TitledBorder.CENTER, TitledBorder.TOP));
		mainPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

		emptyPanel = new JPanel();
		emptyPanel.setPreferredSize(new Dimension(310, 10));

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

		
		
		
		
		JRadioButton novice   = new JRadioButton("Novice", true);
        JRadioButton easy    = new JRadioButton("Easy");
        JRadioButton medium = new JRadioButton("Medium");
        JRadioButton hard = new JRadioButton("Hard");

        
        //... Create a button group and add the buttons.
        ButtonGroup radioButtonGroup = new ButtonGroup();
        radioButtonGroup.add(novice);
        radioButtonGroup.add(easy);
        radioButtonGroup.add(medium);
        radioButtonGroup.add(hard);
        
        //... Arrange buttons vertically in a panel
        levelPanel = new JPanel();
        levelPanel.setPreferredSize(new Dimension(210, 50));
        levelPanel.setBorder(BorderFactory.createTitledBorder(
        		BorderFactory.createEtchedBorder(), "c h o o s e   l e v e l", TitledBorder.CENTER, TitledBorder.TOP));
//        levelPanel.setLayout(new GridLayout(1, 4));
        levelPanel.add(novice);
        levelPanel.add(easy);
        levelPanel.add(medium);
        levelPanel.add(hard);
        
        //... Add a titled border to the button panel.

      
		
		
		
		
		
		ButtonGroup dimensions = new ButtonGroup();
		dimensions.add(sqrFour);
		dimensions.add(sqrSix);
		dimensions.add(sqrEight);
		dimensions.add(sqrTen);

		boardSizePanel = new JPanel();
		boardSizePanel.setPreferredSize(new Dimension(210, 70));
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

		addButton = new JButton("add");
		addButton.setFont((new Font("dialog", Font.BOLD, 12)));
		addButton.setPreferredSize(new Dimension(55, 28));
		addButton.setActionCommand("addPlayer");
		addButton.setEnabled(false);

		removeButton = new JButton("remove");
		removeButton.setFont((new Font("dialog", Font.BOLD, 12)));
		removeButton.setPreferredSize(new Dimension(77, 28));
		removeButton.setActionCommand("removePlayer");

		tablePane.setPreferredSize(new Dimension(100, 34));

		class PlayerListListener implements ActionListener, DocumentListener {
			private boolean alreadyEnabled = false;
			private JButton addButton;
			private String name;
			public PlayerListListener(JButton button) {
				this.addButton = button;
			}

			public void actionPerformed(ActionEvent e) {

				//V A L I D A T I O N 
				if (listModel.getSize() + 1 > 2) {
					if ((humanButton.isSelected()) && (listModel.getSize() + 1 > 2)) {
						Toolkit.getDefaultToolkit().beep();
						JOptionPane.showMessageDialog(null, "You can have only 2 players in \"human\" mode!", "Memory",
								JOptionPane.INFORMATION_MESSAGE);
						playerNameTextField.setText("");
						return;
					} else if (((soloButton.isSelected()) || (computerButton.isSelected()))
							&& (listModel.getSize() + 1 > 1)) {
						Toolkit.getDefaultToolkit().beep();
						JOptionPane.showMessageDialog(null,
								"You can have only 1 player in \"solo\" or  \"computer\" mode!", "Memory",
								JOptionPane.INFORMATION_MESSAGE);
						playerNameTextField.setText("");
						playerNameTextField.requestFocusInWindow();
						return;
					}
					Toolkit.getDefaultToolkit().beep();
					JOptionPane.showMessageDialog(null, "You can't register more than 2 players!", "Memory",
							JOptionPane.INFORMATION_MESSAGE);
					playerNameTextField.setText("");
					playerNameTextField.requestFocusInWindow();
					return;
				}

				name = playerNameTextField.getText();

				// User didn't type in a unique name...
				if (name.equals("") || listModel.contains(name)) {
					Toolkit.getDefaultToolkit().beep();
					JOptionPane.showMessageDialog(null, "The name already exists,  please choose another name",
							"Memory", JOptionPane.INFORMATION_MESSAGE);

					playerNameTextField.requestFocusInWindow();
					playerNameTextField.selectAll();
					return;
				}

				int index = list.getSelectedIndex();
				if (index == -1) {
					index = 0;
				} else {
					index++;
				}

				listModel.insertElementAt(playerNameTextField.getText(), index);
				list.clearSelection();

				playerNameTextField.requestFocusInWindow();
				playerNameTextField.setText("");

				list.setSelectedIndex(index);
				list.ensureIndexIsVisible(index);
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
					addButton.setEnabled(true);
				}
			}

			private boolean handleEmptyTextField(DocumentEvent e) {
				if (e.getDocument().getLength() <= 0) {
					addButton.setEnabled(false);
					alreadyEnabled = false;
					return true;
				}
				return false;
			}
		}

		PlayerListListener addListener = new PlayerListListener(addButton);
		addButton.addActionListener(addListener);

		removeButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				DefaultListModel<String> defaultLM = (DefaultListModel<String>) list.getModel();
				if (list.getSelectedIndices().length > 0) {
					int selectedIndices[] = list.getSelectedIndices();
					for (int i = selectedIndices.length - 1; i >= 0; i--) {
						defaultLM.removeElementAt(selectedIndices[i]);
					}
				}
				int listSize = listModel.getSize();
				if (listSize == 0) {
					removeButton.setEnabled(false);
				}
				list.ensureIndexIsVisible(list.getSelectedIndex());
			}
		});

		playerNameTextField = new JTextField(14);
		playerNameTextField.setPreferredSize(new Dimension(200, 30));
		playerNameTextField.setFont((new Font("dialog", Font.PLAIN, 14)));

		playerNameTextField.addActionListener(addListener);
		playerNameTextField.getDocument().addDocumentListener(addListener);
		tableButtonsPanel = new JPanel();
		tableButtonsPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
				" a d d  p l a y e r s ", TitledBorder.CENTER, TitledBorder.TOP));
		tableButtonsPanel.add(addButton);
		tableButtonsPanel.add(playerNameTextField);
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
//		gbc.anchor = GridBagConstraints.SOUTH;
		mainPanel.add(levelPanel, gbc);

		gbc.gridx = 0;
		gbc.gridy = 4;
		gbc.fill = GridBagConstraints.BOTH;
		mainPanel.add(tablePane, gbc);

		gbc.gridx = 0;
		gbc.gridy = 5;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		mainPanel.add(tableButtonsPanel, gbc);

		gbc.gridx = 0;
		gbc.gridy = 6;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.SOUTH;
		mainPanel.add(executionPanel, gbc);
		
		frame.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setVisible(true);
	}

	// set action listener on all setup view buttons
	public void addOptionsViewListener(ActionListener selected) {
//		sqrFour.addActionListener(selected);
//		sqrSix.addActionListener(selected);
//		sqrEight.addActionListener(selected);
//		sqrTen.addActionListener(selected);
//		soloButton.addActionListener(selected);
//		humanButton.addActionListener(selected);
//		computerButton.addActionListener(selected);
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

	public DefaultListModel<String> getListModel() {
		return this.listModel;
	}

	public JList<String> getList() {
		return this.list;
	}

	public JButton getSaveButton() {
		return saveButton;
	}

	public JTextField getPlayerName() {
		return playerNameTextField;
	}

	public void setPlayerName(JTextField playerName) {
		this.playerNameTextField = playerName;
	}
}