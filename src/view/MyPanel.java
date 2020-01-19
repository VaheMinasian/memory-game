package view;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;


@SuppressWarnings("serial")
public class MyPanel extends JPanel {

	private Image background;
	private int boardSize;
	public MyPanel(){}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		g.drawImage( background, 5, 12, boardSize-boardSize*2/250, boardSize-boardSize*3/100, this );
	}

	public void setImage( ImageIcon imgIcon ) {
		background = imgIcon.getImage();
	}

	public void setBoardSize(int boardDimension) {
		this.boardSize = boardDimension;
	}
}