package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableColumn;

import model.ScoreModel;


public class ScoresView {
	public JFrame frame;
	JPanel mainPanel, buttonPanel, jTablePanel/* textAreaPanel */;
	JTable table;
	String[] columns =  {"Name", "guess%", "board", "time score", "date & time"};
	private String playerName;
	private String boardSize;
	private String guessRatio;
	private String duration;
	private String date, newDate, newTime;
	//isCellEditable().
    Object[][] data = new Object[11][5];
    int[] columnsWidth = {135, 58, 42, 70, 135};     
    
	JButton okButton;
	
	public ScoresView() {
		frame = new JFrame();
		
		table = new JTable(data, columns);
		table.setPreferredSize(new Dimension(440, 345));
		table.setEnabled(false);
		int k = 0;
        for (int width : columnsWidth) {
            TableColumn column = table.getColumnModel().getColumn(k++);
            column.setMinWidth(width);
            column.setMaxWidth(width);
            column.setPreferredWidth(width);
            table.setColumnSelectionAllowed(false);
        }	
        for(int i=0; i<1; i++) {
        	for (int j=0; j<5; j++) {
        		
        		data[i][j]=columns[j];
        	}
        }
        table.setIntercellSpacing(new Dimension(8,5));
		table.setRowHeight(31);
		table.setRowHeight(0, 36);
		
		table.setBackground(Color.LIGHT_GRAY);
        table.setForeground(Color.black);
       
		mainPanel = new JPanel();
//		textAreaPanel = new JPanel();
		jTablePanel= new JPanel();
		buttonPanel = new JPanel();
		
		mainPanel.setPreferredSize(new Dimension(470, 440));
//		textAreaPanel.setPreferredSize(new Dimension(335, 280));
//		textAreaPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), " HIGH SCORES ",
//				TitledBorder.CENTER, TitledBorder.TOP));
		jTablePanel.setPreferredSize(new Dimension(460, 380));
		jTablePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), " HIGH SCORES ",
				TitledBorder.CENTER, TitledBorder.TOP));
		table.setFillsViewportHeight(true);
		
		jTablePanel.add(table);
//		table.isCellEditable(10, 6);
//		textAreaPanel.add(table);
//		textArea = new JTextArea();
//		textArea.setPreferredSize(new Dimension(318, 250));
//		textArea.setEditable(false);
//		textAreaPanel.add(textArea);
		
		okButton = new JButton("close");
		okButton.setFont((new Font("dialog", Font.BOLD, 13)));
		buttonPanel.add(okButton);
		
//		mainPanel.add(textAreaPanel);
		mainPanel.add(jTablePanel);
		mainPanel.add(buttonPanel);

//		frame.setUndecorated(true);
		frame.setAlwaysOnTop(true);
		
		frame.add(mainPanel);
		frame.pack();
		frame.setVisible(true);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);

//		okButton.addActionListener(new ActionListener() {
//
//			@Override
//			public void actionPerformed(ActionEvent arg0) {
//				frame.dispose();
//			}
//		});
	}
	
	public JButton getOkButton() {
		return okButton;
	}

	public void setOkButton(JButton okButton) {
		this.okButton = okButton;
	}

	public void displayScores(int row) {
		data[row+1][0] =  this.playerName;
		data[row+1][1] =  this.guessRatio;
		data[row+1][2] =  this.boardSize;		
		data[row+1][3] =  this.duration;
		data[row+1][4] =  this.date;
	}
	
	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public void setGuessRatio(String ratio) {
		this.guessRatio = ratio;
	}

	public void setBoardSize(String boardSize) {
		this.boardSize = boardSize;
	}
	
	public void setDuration(String duration) {
		this.duration = duration;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public void setScores(ScoreModel score) {
		
		
		
		System.out.println("score.getDuration is: " + score.getDuration());
		long second = (score.getDuration() / 1000) % 60;
		long minute = (score.getDuration() / (1000 * 60)) % 60;
		long hour = (score.getDuration() / (1000 * 60 * 60)) % 24;

		String time = String.format("%02d:%02d:%02d", hour, minute, second);
		
		
		newDate = score.getDate().toString().substring(0,10);
		newTime = score.getDate().toString().substring(11,19);
		setDate(newDate + " " + newTime); // date and time of win
		setPlayerName(score.getPlayerName()); //player name
		setGuessRatio(String.valueOf(score.getGuessRatio())); //guess %
		setBoardSize(score.getBoardSize()+"x"+score.getBoardSize()); //board Size
		setDuration(time); //time score
		
		
	} 
}
