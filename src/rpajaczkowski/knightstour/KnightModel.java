package rpajaczkowski.knightstour;

import java.util.ArrayList;

public class KnightModel {
	
	/*
	public static final Position[] moveMatrix = {
		new Position(-2,-1),
		new Position(-2, 1),
		new Position(-1, 2),
		new Position( 1, 2),
		new Position( 2,-1),
		new Position( 2, 1),
		new Position( 1,-2),
		new Position(-1,-2)
	};
	*/
	
	public static final Position[] moveMatrix = {
		new Position(-1, 2),
		new Position( 1, 2),
		new Position(-2, 1),
		new Position( 2, 1),
		new Position(-2,-1),
		new Position( 2,-1),
		new Position(-1,-2),
		new Position( 1,-2)
	};
	
	public static ArrayList<Position> getPossibleMovesFrom(Position pos, int[][] board) {
		if ( (pos==null) || (board==null))
			return null;
		
		ArrayList<Position> positionList = new ArrayList<Position>();
		
		int r = pos.row;
		int c = pos.column;
		int maxr = board.length;
		int maxc = board[0].length;
		
		for(Position p : moveMatrix) {
			if ( (r+p.row>=0)    && (r+p.row<maxr) &&
			     (c+p.column>=0) && (c+p.column<maxc) &&
			     (board[r+p.row][c+p.column]==0) )
			         positionList.add(new Position(r+p.row, c+p.column));
		}
		
		return positionList;
	}
}
