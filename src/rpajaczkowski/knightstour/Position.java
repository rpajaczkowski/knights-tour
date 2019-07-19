package rpajaczkowski.knightstour;

/*
Class to store postions of moves.
*/

public class Position {
	public int row;
	public int column;
	
	public Position(int r, int c) {
		row = r;
		column = c;
	}
	
	public String toString() {
		return ("" + (char)('A'+column) + (row+1));
	}
	
	public boolean equals(Position p) {
		return ( (row==p.row) && (column==p.column));	
	}
}
