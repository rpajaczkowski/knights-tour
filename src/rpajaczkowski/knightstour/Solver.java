package rpajaczkowski.knightstour;

/*
Solving knight's tour.
*/

import java.util.ArrayList;
import java.util.LinkedList;

public class Solver {
	
	public static int recursionCounter; //just as a curiosity
	public static boolean showIntermediateResults= false;
	
	public static Position hint(int[][] board) {
		LinkedList<Position> moveList = Solver.solution(board);
		if (moveList==null)
			return null;
		else
			return moveList.peekFirst();
	}
	
	public static LinkedList<Position> solution(int[][] board) {
		if (board == null)
			return null;
		
		LinkedList<Position> moveList = new LinkedList<Position>();
		
		//setting last position of knight and last step number
		Position pos = new Position(0,0);
		int step = 0;
		for (int r=0; r<board.length; r++)
			for (int c=0; c<board[r].length; c++)
				if (board[r][c] > step) {
					pos.row = r;
					pos.column = c;
					step = board[r][c];
				}
		
		boolean tmeporaryRemoved = false;
		if (step>0) {
			//temporary removing last position from board
			//algorithm will begin from this position
			step--;
			board[pos.row][pos.column] = 0;
			//set flag that last step was took back
			tmeporaryRemoved = true;
			
		}
		
		recursionCounter = 0;
		if (solve(board,moveList,pos,step+1)) {
			if (tmeporaryRemoved)
				moveList.remove(0); //remove first step because it was already done
				                    //it was just temporary removed before 
			return moveList;
		}
		else
			return null;
	}
	
	//Main part of algorithm solving knight's tour issue.
	private static boolean solve(int[][] board, LinkedList<Position> moveList, Position pos, int step) {
		int rows = board.length;
		int columns = board[0].length;
		boolean result;
		
		recursionCounter++;
		result = false;
		if (board[pos.row][pos.column] == 0) {
			
			board[pos.row][pos.column] = step;
			moveList.add(pos);
			
			if(showIntermediateResults) {
				System.out.println("step: " + step +", recursionCounter: " + recursionCounter);
				System.out.println("moveList: " + moveList);
				showBoardOnColsole(board);
			}
			
			if (step==rows*columns)
				result = true;
			else {
				ArrayList<Position> posList = KnightModel.getPossibleMovesFrom(pos, board);
				if (hasPotentialSolution(board, step, posList)) {
					//current board has potential to be solvable
					while ( (!result) && (posList.size()>0) ) {
						Position checkPos = posList.remove(0);
						result = result || solve(board,moveList,checkPos,step+1);
					}
				}
				if (!result) {
					//undo last move
					board[pos.row][pos.column] = 0;
					moveList.remove(pos);
				}
			}
		}
		return result;
	}
	
	private static 	boolean hasPotentialSolution(int[][] board, int step, ArrayList<Position> proposeList) {
		int rows = board.length;
		int columns = board[0].length;
		boolean result = true;
		int singles = 0; //number of squars with one neighbor where knight can jump
			//if singles>2 then board can not be solved. One neighbor can have only
			//next move and last move.
		boolean mayBeNext = false;
		
		if (step >= rows*columns-1) //only one move left to check or solved
			return true;
		
		//Checking if each empty square, except two (next move and last move), 
		//has at least two free neighbors.
		for (int r=0; r<rows; r++) {
			if (!result)
				break;
			for (int c=0; c<columns; c++) 
				if (board[r][c] == 0) {
					Position pos = new Position(r,c);
					ArrayList<Position> neighbors = KnightModel.getPossibleMovesFrom(pos, board);
					int count = neighbors.size();
					
					if (count == 0) {
						result = false;
						if (showIntermediateResults) System.out.println("single " + (singles+1) + " is " + pos + " neighbors " +  neighbors + ", proposed: " + proposeList + ", mayBeNext " + mayBeNext);
						break;
					} else if (count == 1) {
						
						if (!mayBeNext) {
							for(Position p : proposeList)
								if (p.equals(pos)) {
									mayBeNext = true;
									proposeList.clear();
									proposeList.add(p);
									break;
								}
						}
						
						if (showIntermediateResults) System.out.println("single " + (singles+1) + " is " + pos + " neighbors " +  neighbors + ", proposed: " + proposeList + ", mayBeNext " + mayBeNext);
						singles++;
						if (((singles==2) && (!mayBeNext)) || (singles > 2))  {
							result = false;
							break;
						}
					}
				}
		}
		return result;
	}
	
	public static void showBoardOnColsole(int[][] board) {
		for(int r=-1; r<board.length; r++) {
			for(int c=-1; c<board[0].length; c++) {
				if(r<0) { //row with column letters
					System.out.print("\t");
					if(c>=0)
						System.out.print("("+(char)(65+c)+")");
				} else { //proper row
					if(c<0) //row number
						System.out.print("\t("+(r+1)+")|");
					else // step on square
						System.out.print("\t"+board[r][c]);
				}
			}
			System.out.println("");
		}
	}
	
}
