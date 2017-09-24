package student.cs4341.project2.evaluation;

import student.cs4341.project2.game.Game;
import student.cs4341.project2.game.SquareState;

public class Evaluator {
	
	// TODO: Check diagonals (both)
	
	public static int evaluateMove(SquareState[][] currentState, SquareState myColor, SquareState enemyColor) {
		// Check if provided move is terminal (for us or enemy)
		int isTerminal = isTerminal(currentState, myColor, enemyColor);
		if(isTerminal != 0) {		
			return isTerminal;
		} else {					// If not, initialize vars to count number of 3s and 4s for
			int enemyFours = 0;		// us and enemy in the provided move for evaluation
			int ourFours = 0;
			int enemyThrees = 0;
			int ourThrees = 0;
			
			// In every column, go through all rows and count number of 3s and 4s (includes overlap)
			//for(int i = 0; i < Game.COL_NUMBERS; i++) {
				//enemyFours += countInACol(currentState, enemyColor, i, 4);
				//ourFours += countInACol(currentState, myColor, i, 4);
				//enemyThrees += countInACol(currentState, enemyColor, i, 3);
				//ourThrees += countInACol(currentState, myColor, i, 3);
			//}
			
			// In every row, go through all columns and count number of 3s and 4s (includes overlap)
			//for(int i = 0; i < Game.ROW_NUMBERS; i++) {
				//enemyFours += countInARow(currentState, enemyColor, i, 4);
				//ourFours += countInARow(currentState, myColor, i, 4);
				//enemyThrees += countInARow(currentState, enemyColor, i, 3);
				//ourThrees += countInARow(currentState, myColor, i, 3);	
			//}
			
			// Finally, return these values with a factor for weighting
			//return (15*ourFours) + (5*ourThrees) - (15*enemyFours) - (5*enemyThrees);
			return (int)Math.random()*1000;
		}
	}
	
	private static int isTerminal(SquareState[][] currentState, SquareState myColor, SquareState enemyColor) {
		// First check if either we or enemy have 5 in a row in this state (terminal)
		int usMaxInRow = getInALine(currentState, myColor);
		int enemyMaxInRow = getInALine(currentState, enemyColor);
		
		// If so, return with the corresponding terminal value
		if(usMaxInRow >= 5) {
			return 10000;
		} else if(enemyMaxInRow >= 5) {
			return -10000;
		} else {
			return 0;
		}
	}
	
	public static boolean isStateWorthExpanding(SquareState[][] currentState, int row, int col) {
		SquareState myColor = currentState[row][col];
		return (withinBounds(row+1,col+1) && currentState[row+1][col+1] == myColor) ||
				(withinBounds(row+1,col) && currentState[row+1][col] == myColor) ||
				(withinBounds(row+1,col-1) && currentState[row+1][col-1] == myColor) ||
				(withinBounds(row,col+1) && currentState[row][col+1] == myColor) ||
				(withinBounds(row,col) && currentState[row][col] == myColor) ||
				(withinBounds(row,col-1) && currentState[row][col-1] == myColor) ||
				(withinBounds(row-1,col+1) && currentState[row-1][col+1] == myColor) ||
				(withinBounds(row-1,col) && currentState[row-1][col] == myColor) ||
				(withinBounds(row-1,col-1) && currentState[row-1][col-1] == myColor);
	}
	
	private static boolean withinBounds(int row, int col) {
		return (row >= 0 && col >= 0 && row < Game.ROW_NUMBERS && col < Game.COL_NUMBERS);
	}
	
	public static int countInARow(SquareState[][] currentState, SquareState color, int row, int length) {
		// Iterate through all columns in this row to determine number of contiguous stones of length X (overlapping)
		int frequency = 0;
		int runningTotal = 0;
		for(int i = 0; i < Game.COL_NUMBERS; i++) {
			if(currentState[row][i] == color) {
				if(runningTotal >= length) {
					frequency++;
				} else {
					runningTotal++;
				}
			} else {
				runningTotal = 0;
			}
		}
		
		return frequency;
	}
	
	public static int countInACol(SquareState[][] currentState, SquareState color, int col, int length) {
		// Iterate through all rows in this column to determine number of contiguous stones of length X (overlapping)
		int frequency = 0;
		int runningTotal = 0;
		for(int i = 0; i < Game.ROW_NUMBERS; i++) {
			if(currentState[i][col] == color) {
				if(runningTotal >= length) {
					frequency++;
				} else {
					runningTotal++;
				}
			} else {
				runningTotal = 0;
			}
		}
		
		return frequency;
	}
	
	public static int getInALine(SquareState[][] currentState, SquareState color) {
		// Maintain the longest contiguous line of stones so far
		int maxInARow = 0;
		for(int i = 0; i < Game.ROW_NUMBERS; i++) {
			int maxInRow = getMaxRow(currentState, i, color);
			
			// If there are at least 5 in a row in a given column, the board is terminal- return
			if(maxInRow >= 5) {
				return 5;
			}
			
			// Otherwise, set maximum properly
			if(maxInRow > maxInARow) {
				maxInARow = maxInRow;
			}
		}
		
		// Do the same for rows
		for(int i = 0; i < Game.COL_NUMBERS; i++) {
			int maxInCol = getMaxCol(currentState, i, color);
			
			if(maxInCol >= 5) {
				return 5;
			}
			
			if(maxInCol > maxInARow) {
				maxInARow = maxInCol;
			}
		}		
		
		return maxInARow;
	}
	
	private static int getMaxRow(SquareState[][] currentState, int row, SquareState color) {
		int longestRow = 0;
		int currentRow = 0;
		for(int i = 0; i < Game.COL_NUMBERS; i++) {
			if(currentState[row][i] == color) {
				currentRow++;
			} else {
				if(longestRow < currentRow) {
					longestRow = currentRow;
				}
				currentRow = 0;
			}
		}
		return longestRow;
	}
	
	private static int getMaxCol(SquareState[][] currentState, int col, SquareState color) {
		int longestCol = 0;
		int currentCol = 0;
		for(int i = 0; i < Game.ROW_NUMBERS; i++) {
			if(currentState[i][col] == color) {
				currentCol++;
			} else {
				if(longestCol < currentCol) {
					longestCol = currentCol;
				}
				currentCol = 0;
			}
		}
		return longestCol;
	}
}
