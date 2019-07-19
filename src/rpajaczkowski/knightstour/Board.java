package rpajaczkowski.knightstour;

/*
Board class is responsible for handling board. 
Also a kind of judge watching the rules of play and not allowing for prohibited movements.
*/

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.util.*;
import java.io.*;

public class Board extends JPanel implements MouseListener {
	
	private static final Color DARK_COLOR = Color.GRAY;
	private static final Color LIGHT_COLOR = Color.LIGHT_GRAY;
	
	private Square[][] squareBoard;
	private int stepCounter = 0;
	private int winningStep = -1;
	private LinkedList<Position> listOfVisited = new LinkedList<Position>();
	private JFileChooser fileChooser;
	
	public Board(int rows, int columns) {
		createNewSquareBoard(rows,columns);
		
		fileChooser = new JFileChooser();
		FileNameExtensionFilter knightsTourFilter = new FileNameExtensionFilter("Knight's tour save file (*.sav)","sav");
		fileChooser.setFileFilter(knightsTourFilter);
	}
	
	public Board() {
		this(8,8);
	}
	
	public void mouseClicked(MouseEvent e) {
		Square square = (Square)e.getSource();
		if(square.highlightable)
			moveKnightTo(square.getPosition());
	}
	
	public void mouseEntered(MouseEvent e) {
		Square square = (Square)e.getSource();
		square.highlight = true;
		square.repaint();
	}
	
	public void mouseExited(MouseEvent e) {
		Square square = (Square)e.getSource();
		square.highlight = false;
		square.repaint();
	}
	
	public void mousePressed(MouseEvent e) { }	//not used
	
	public void mouseReleased(MouseEvent e) { } //not used
	
	public void clearBoard() {
		for (int r=0; r<squareBoard.length; r++) {
			for (int c=0; c<squareBoard[r].length; c++) {
				squareBoard[r][c].step = 0;
				squareBoard[r][c].highlightable = true;
				squareBoard[r][c].mark = false;
				squareBoard[r][c].knight = false;
				squareBoard[r][c].repaint();
			}
		}
		stepCounter = 0;
		listOfVisited.clear();
	}
	
	private void createNewSquareBoard(int rows, int columns) {
		listOfVisited = new LinkedList<Position>();
		squareBoard = new Square[rows][columns];
		winningStep = rows*columns;
		
		removeAll();
		
		/*
		//grid without row and column names
		setLayout(new GridLayout(rows,columns));
		for (int r=0; r<rows; r++) {
			for (int c=0; c<columns; c++) {
				Square square = new Square(
					((r+c)%2==0)?DARK_COLOR:LIGHT_COLOR
				);
				square.addMouseListener(this);
				square.position = new Position(r,c);
				add(square);
				squareBoard[r][c] = square;
			}
		}
		*/
		
		//grid with row numbers and column letters
		setLayout(new GridLayout(rows+2,columns+2));
		for (int r=-1; r<rows+1; r++) {
			for (int c=-1; c<columns+1; c++) {
				if ((r >= rows) || (c >= columns)) { //last row and column is empty
					add(new JLabel("")); //empty
				}
				else if (c==-1) { //column with row numbers
					if (r==-1) { 
						JLabel jl = new JLabel("");
						add(jl); //empty
					}
					else {
						JLabel jl = new JLabel("" + (r+1));
						jl.setHorizontalAlignment(SwingConstants.RIGHT);
						jl.setVerticalAlignment(SwingConstants.CENTER);
						//jl.setBorder(BorderFactory.createLineBorder(Color.BLACK));
						add(jl); //row number
					}
				} 
				else
				{ //c>-1
					if (r==-1) { //row with colmn letters
						JLabel jl = new JLabel("" + (char)(65+c));
						jl.setHorizontalAlignment(SwingConstants.CENTER);
						jl.setVerticalAlignment(SwingConstants.BOTTOM);
						//jl.setBorder(BorderFactory.createLineBorder(Color.BLACK));
						add(jl); //column letter
					}
					else {
						Square square = new Square(
							((r+c)%2==0)?DARK_COLOR:LIGHT_COLOR
						);
						square.addMouseListener(this);
						square.position = new Position(r,c);
						add(square);
						squareBoard[r][c] = square;
					}
				}
			}
		}
		
		validate();
		repaint();
		clearBoard();
	}
	
	public int[][] getBoardAsArray() {
		int rows = squareBoard.length;
		int columns = squareBoard[0].length;
		
		int[][] board = new int[rows][columns];
		for(int r=0; r<rows; r++)
			for(int c=0; c<columns; c++)
				board[r][c] = squareBoard[r][c].step;
		
		return board;
	}
	
	public LinkedList<Position> getListOfVisited() {
		return listOfVisited;
	}
	
	public Square getSquare(Position p) {
		return squareBoard[p.row][p.column];
	}
	
	public void clearAllHighlightable() {
		for (int r=0; r<squareBoard.length; r++) {
			for (int c=0; c<squareBoard[r].length; c++) {
				squareBoard[r][c].highlightable = false;
				squareBoard[r][c].mark = false;
				squareBoard[r][c].repaint();
			}
		}
	}
	
	public void moveKnightTo(Position pos) {
		//remove icon of knight on board from last position
		Position lastPos = listOfVisited.peekLast();
		if (lastPos!=null) {
			getSquare(lastPos).knight = false;
			getSquare(lastPos).repaint();
		}
		Square s = getSquare(pos);
		listOfVisited.add(pos);
		getSquare(pos).step = ++stepCounter;
		getSquare(pos).knight = true;
		if(stepCounter == winningStep) {
			clearAllHighlightable();
			JOptionPane.showMessageDialog(null, "Congratulations! You've filled up the entire board.");
		}
		else
			markPossibleMovesFrom(pos);
		getSquare(pos).repaint();
	}
	
	public void moveKnightAccordingToList(LinkedList<Position> moves) {
		for (Position pos : moves) {
			moveKnightTo(pos);
		}
	}
	
	public void moveBack() {
		Position pos = listOfVisited.pollLast();
		if (pos!=null) {
			stepCounter--;
			Square s = getSquare(pos);
			s.step = 0;
			s.highlightable = true;
			s.mark = false;
			s.knight = false;
			
			//mark new possible moves
			pos=listOfVisited.peekLast();
			if (pos == null)
				clearBoard();
			else {
				getSquare(pos).knight = true;
				markPossibleMovesFrom(pos);
			}
		}	
	}
	
	private void markPossibleMovesFrom(Position pos) {
		//clear old marks
		clearAllHighlightable();
		//new marks
		ArrayList<Position> markedList = KnightModel.getPossibleMovesFrom(pos, getBoardAsArray());
		if (markedList.size() == 0) 
			JOptionPane.showMessageDialog(null, "No possible moves.");
		for(Position p : markedList) {
			getSquare(p).highlightable = true;
			getSquare(p).mark=true;
			getSquare(p).repaint();
		}
	}
	
	public void saveToFile(File file) {
		if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
			try {
				String filename = fileChooser.getSelectedFile().getAbsolutePath();
				if (!filename.endsWith(".sav"))
					filename += ".sav";
				BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
				int rows = squareBoard.length;
				int columns = squareBoard[0].length;
				//size of board
				writer.write("Board size");
				writer.newLine();
				writer.write("" + rows + "\t" + columns);
				writer.newLine();
				//content of board
				writer.write("Board");
				writer.newLine();
				for (int r=0; r<rows; r++) {
					for (int c=0; c<columns; c++) {
						writer.write(""+squareBoard[r][c].step + "\t");
					}
					writer.newLine();
				}
				//list of moves
				writer.write("List of moves");
				writer.newLine();
				for(Position p : listOfVisited) {
					writer.write("" + p.row + "\t" + p.column);
					writer.newLine();
				}
				writer.newLine();
				writer.flush();
				writer.close();
			}
			catch (Exception e) { }
		}
	}
	
	public void openFromFile(File file) {
		if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			try {
				//String filename = fileChooser.getSelectedFile().getAbsolutePath();
				//if (!filename.endsWith(".sav")) filename += ".sav";
				Scanner sc = new Scanner(fileChooser.getSelectedFile());
				String line;
				//size of board
				line=sc.nextLine();
				System.out.println(line);
				int rows = sc.nextInt(); // line "Board size"
				int columns = sc.nextInt();
				
				System.out.println(rows + " " + columns);
				createNewSquareBoard(rows,columns);
				
				line=sc.nextLine(); // finishing reading curent line
				System.out.println(line);
				line=sc.nextLine(); // line ""Board"
				System.out.println(line);
				
				winningStep = rows * columns;
				stepCounter = 0;
				for (int r=0; r<rows; r++) {
					for (int c=0; c<columns; c++) {
						int i = sc.nextInt();
						System.out.print("\t"+i);
						squareBoard[r][c].step = i;
						if (i>stepCounter)
							stepCounter = i; //setting last step number
					}
					System.out.print("\n");
				}
				line=sc.nextLine(); // finishing reading curent line
				System.out.println(line);
				line=sc.nextLine(); //line "List of moves"
				listOfVisited = new LinkedList<Position>();
				while (sc.hasNextInt()) {
					int r = sc.nextInt();
					int c = sc.nextInt();
					listOfVisited.add(new Position(r,c));
					System.out.println(r + " " + c);
				}
				sc.close();
				//correcting display of last read move
				Position lastPos = listOfVisited.peekLast();
				if (lastPos!=null) {
					getSquare(lastPos).knight = true;
					getSquare(lastPos).repaint();
					markPossibleMovesFrom(lastPos);
					getSquare(lastPos).repaint();
				}
			}
			catch (FileNotFoundException e) {
			}
		}
		
	}
	
}
