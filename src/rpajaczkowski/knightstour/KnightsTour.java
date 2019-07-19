package rpajaczkowski.knightstour;

/*
Main panel of game.
*/

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.LinkedList;

public class KnightsTour extends JPanel {
	
	public static final String PROGRAM_NAME = "Knight's tour";
	Board board;
	
	private int numberOfRows = 8;
	private int numberOfColumns = 8;
	
	private Action NewBoardAction = new AbstractAction("New") {
		public void actionPerformed(ActionEvent e) {
			if (showInputDialog()) {
				remove(board);
				
				board = new Board(numberOfRows, numberOfColumns);
				add(board, BorderLayout.CENTER);
				validate();
				repaint();
			}
		}
	};
	
	private Action openAction = new AbstractAction("Open") {
		public void actionPerformed(ActionEvent e) {
			board.openFromFile(new File("a"));
		}
	};
	
	private Action saveAction = new AbstractAction("Save") {
		public void actionPerformed(ActionEvent e) {
			board.saveToFile(new File("a"));
		}
	};
	
	private Action listOfMovesAction = new AbstractAction("List of moves") {
		public void actionPerformed(ActionEvent e) {
			JOptionPane.showMessageDialog(null, "List: " + board.getListOfVisited());
		}
	};
	
	private Action backAction = new AbstractAction("Back") {
		public void actionPerformed(ActionEvent e) {
			board.moveBack();
		}
	};
	
	private Action hintAction = new AbstractAction("Hint") {
		public void actionPerformed(ActionEvent e) {
			//Solver.showIntermediateResults = false;
			Position pos = Solver.hint(board.getBoardAsArray());
			//System.out.println("Solver.recursionCounter: " + Solver.recursionCounter);
			String str;
			if (pos==null)
				str = "Solution not found.";
			else
				str = String.format("Try %s.", pos);
			JOptionPane.showMessageDialog(null, str);
		}
	};
	
	private Action solveAction = new AbstractAction("Solve") {
		public void actionPerformed(ActionEvent e) {
			int[][] arrayBoard = board.getBoardAsArray();
			
			//Solver.showIntermediateResults = true;
			LinkedList<Position> solution = Solver.solution(arrayBoard);
			//System.out.println("Solver.recursionCounter: " + Solver.recursionCounter);
			
			if (solution==null)
				JOptionPane.showMessageDialog(null, "Solution not found.");
			else
				board.moveKnightAccordingToList(solution);
		}
	};
	
	private Action exitAction = new AbstractAction("Exit") {
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}
	};
	
	public KnightsTour() {
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		
		board = new Board();
		add(board, BorderLayout.CENTER);
	}
	
	public JMenuBar createMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		
		JMenu menuBoard = new JMenu("Board");
		menuBoard.setMnemonic(KeyEvent.VK_B);
		
		menuBoard.add(NewBoardAction);
		menuBoard.addSeparator();
		
		menuBoard.add(openAction);
		menuBoard.add(saveAction);
		menuBoard.addSeparator();
		
		menuBoard.add(listOfMovesAction);
		menuBoard.add(backAction);
		menuBoard.addSeparator();
		
		menuBoard.add(hintAction);
		menuBoard.add(solveAction);
		menuBoard.addSeparator();
		
		menuBoard.add(exitAction);
		
		menuBar.add(menuBoard);
		return menuBar;
	}
	
	public boolean showInputDialog() {
		String[] options = {"3", "4", "5", "6", "7", "8", "9", "10"};
		
		JComboBox<String> jcmbNumberOfRows = new JComboBox<String>(options);
		JComboBox<String> jcmbNumberOfColumns = new JComboBox<String>(options);
		
		jcmbNumberOfRows.setSelectedIndex(numberOfRows-3);
		jcmbNumberOfColumns.setSelectedIndex(numberOfColumns-3);
		
		JPanel jp = new JPanel();
		jp.add(new JLabel("Rows:"));
		jp.add(jcmbNumberOfRows);
		jp.add(Box.createHorizontalStrut(15)); // a spacer
		jp.add(new JLabel("Columns:"));
		jp.add(jcmbNumberOfColumns);
		
		int result = JOptionPane.showConfirmDialog(null, jp, "New board", JOptionPane.OK_CANCEL_OPTION);
		
		if (result == JOptionPane.OK_OPTION) {
			numberOfRows = Integer.parseInt((String)jcmbNumberOfRows.getSelectedItem());
			numberOfColumns = Integer.parseInt((String)jcmbNumberOfColumns.getSelectedItem());
			return true;
		}
		else
			return false;
	}



}
