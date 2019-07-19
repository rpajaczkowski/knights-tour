package rpajaczkowski.knightstour;

/*
Square class is responsible for handling a single field on board.
*/

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Square extends JPanel {
	
	private Color backgroundColor;
	private Color highlightColor = Color.ORANGE;
	private Color markColor = Color.GREEN;
	private Color textColor = Color.BLACK;
	private Color knightColor = Color.BLACK;
	
	public Position position; 
	
	public boolean highlightable = false; //can square be highlighted?
	public boolean highlight = false; //is square highlighted?
	                                  //(doesn't work when highlightable is set to false)
	public boolean mark = false; //used for showing knight possible next move
	
	public boolean knight = false; //is knight present on square?
	public int step = 0; //in which step square was visited by knight
	
	public static final int MIN_FONT_SIZE=10;
	public static final int MAX_FONT_SIZE=180;
	
	public Square(Color bg) {
		backgroundColor = bg;
		setBorder(BorderFactory.createLineBorder(Color.BLACK)); 
		setBackground(backgroundColor);
		//setOpaque(true);
	}
	
	public void paintComponent(Graphics g) {
		if(highlightable)
			setBackground(highlight ? highlightColor : backgroundColor);
		else
			setBackground(backgroundColor);
		super.paintComponent(g);
		
		if (knight)
			drawKnight(g);
		else if (step>0)
			drawStepNumber(g);
		
		if (mark)
			drawMark(g);
	}
	
	private void drawMark(Graphics g) {
		int width = getWidth();
		int height = getHeight();
		int r = (width<height)?width/4:height/4;
		g.setColor(markColor);
		g.fillOval((width-r)/2, (height-r)/2, r,r);
	}
	
	private void drawKnight(Graphics g) {
		int width = getWidth();
		int height = getHeight();
		//my art skills to draw icon of knight ;)
		//icon is drawn in resolution 10x10
		int[] x = {5, 6, 7, 7, 8, 8, 2, 5, 3, 2}; 
		int[] y = {2, 2, 1, 2, 4, 9, 9, 5, 6, 5}; 
		
		//setting scale
		int border = 1; //margin
		double scaleX = ((double)(width-2*border))/10;
		double scaleY = ((double)(height-2*border))/10;
		for(int i=0; i<x.length; i++) {
			x[i] = (int)(x[i] * scaleX + border);
			y[i] = (int)(y[i] * scaleY + border);
		}
		
		g.setColor(knightColor);
		g.fillPolygon(x,y,x.length);
	}
	
	private void drawStepNumber(Graphics g) {
		int width = getWidth();
		int height = getHeight();
		String text = ""+step;
		
		int fontSize = MIN_FONT_SIZE;
		Font font = new Font(Font.DIALOG,Font.BOLD,fontSize);
		
		//adapting font size
		while (fontSize < MAX_FONT_SIZE) {
			int fontSizeTest = fontSize + 1;
			Font fontTest = font.deriveFont((float)fontSizeTest);
			FontMetrics metricsTest = g.getFontMetrics(fontTest);
			int widthTest = metricsTest.stringWidth(text);
			int heightTest = metricsTest.getHeight();
			
			//width and height are decreased about 10 to set some border
			if ( (widthTest<width-10) && (heightTest<height-10) )
				fontSize = fontSizeTest;
			else
				break;
		}
		
		//updating size of font
		font = font.deriveFont((float)fontSize);
		FontMetrics metrics = g.getFontMetrics(font);
		
		//text should be drawn on center of square
		int x = (getWidth() - metrics.stringWidth(text)) / 2;
		int y = (getHeight() - metrics.getHeight()) / 2 + metrics.getAscent();
		
		g.setFont(font);
		g.setColor(textColor);
		g.drawString(text, x, y);
	}
	
	public Position getPosition() {
		return position;
	}
}
