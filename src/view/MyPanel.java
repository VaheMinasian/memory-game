package view;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;


@SuppressWarnings("serial")
public class MyPanel extends JPanel {

	private static Image background;
	private static int boardSize;
	public MyPanel(){}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		g.drawImage( background, 5, 5, boardSize, boardSize, this );
	}

	public static void setImage( ImageIcon imgIcon ) {
		background = imgIcon.getImage();
	}

	public static void setBoardSize(int boardDimension) {
		// TODO Auto-generated method stub
		boardSize = boardDimension;
	}
}