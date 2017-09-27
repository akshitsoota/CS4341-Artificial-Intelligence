package student.cs4341.project2.evaluation;

import student.cs4341.project2.game.Game;
import student.cs4341.project2.game.SquareState;

public class Evaluator {
	
	// TODO: Check all diagonals (in both directions)
	
	/**
	 * Primary function within Evaluator to rate a given board state. This function determines whether the
	 * given board state is terminal, and if not, assigns a value to it for AB pruning and selection.
	 * @param currentState The SquareState[][] of the hypothetical move
	 * @param myColor The color of our pieces
	 * @param enemyColor The color of opponent pieces
	 * @return Returns the int evaluation score of this board state. These values can be -1000 if opponent wins, 1000 if we win, 
	 * or anywhere in between from evaluation
	 */
	public static int evaluateMove(SquareState[][] currentState, SquareState myColor, SquareState enemyColor) {
		// Check if provided move is terminal (for us or enemy)
		int isTerminal = isTerminal(currentState, myColor, enemyColor);
		if(isTerminal != 0) {		
			return isTerminal;
		} else {
			// If not, initialize vars to count number of 3 in-a-rows and 4 in-a-rows for
			// us and enemy in currentState for evaluation
			/*
			int enemyFours = 0;		
			int ourFours = 0;
			int enemyThrees = 0;
			int ourThrees = 0;
			
			// In every column, go through all rows and count number of 3s and 4s (includes overlap)
			for(int i = 0; i < Game.COL_NUMBERS; i++) {
				enemyFours += countInACol(currentState, enemyColor, i, 4);
				ourFours += countInACol(currentState, myColor, i, 4);
				enemyThrees += countInACol(currentState, enemyColor, i, 3);
				ourThrees += countInACol(currentState, myColor, i, 3);
			}
			
			// In every row, go through all columns and count number of 3s and 4s (includes overlap)
			for(int i = 0; i < Game.ROW_NUMBERS; i++) {
				enemyFours += countInARow(currentState, enemyColor, i, 4);
				ourFours += countInARow(currentState, myColor, i, 4);
				enemyThrees += countInARow(currentState, enemyColor, i, 3);
				ourThrees += countInARow(currentState, myColor, i, 3);	
			}
			
			// Here, we would go through all possible diagonals, but this part has not been implemented yet
			
			// Finally, return these values with a factor for weighting
			return (15*ourFours) + (5*ourThrees) - (15*enemyFours) - (5*enemyThrees);
			 */
			return ((int) (Math.random()*1000));
		}
	}
	
	/**
	 * Determines whether the provided board is terminal or not, for either us or the opponent
	 * @param currentState The hypothetical board state
	 * @param myColor Our stone color
	 * @param enemyColor Enemy stone color
	 * @return Returns 1000 if the board is terminal (we win), -1000 if board is terminal (opponent wins), or
	 * 0 if not terminal
	 */
	public static int isTerminal(SquareState[][] currentState, SquareState myColor, SquareState enemyColor) {
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
	
	/**
	 * This is the heuristic to determine whether a board is worth expanding/calculating utility and evaluation on.
	 * This ensures we only expand boards where our move neighbors (in any of the 8 directions) an existing move
	 * @param currentState The hypothetical board state
	 * @param row The row of the move to analyze
	 * @param col The column of the move to analyze
	 * @return Returns true if (row,col) neighbor an existing stone on the board and the board is worth expanding, and false otherwise
	 */
	public static boolean isStateWorthExpanding(SquareState[][] currentState, int row, int col) {
		return (withinBounds(row+1,col+1) && currentState[row+1][col+1] != SquareState.PINK) ||
				(withinBounds(row+1,col) && currentState[row+1][col] != SquareState.PINK) ||
				(withinBounds(row+1,col-1) && currentState[row+1][col-1] != SquareState.PINK) ||
				(withinBounds(row,col+1) && currentState[row][col+1] != SquareState.PINK) ||
				(withinBounds(row,col-1) && currentState[row][col-1] != SquareState.PINK) ||
				(withinBounds(row-1,col+1) && currentState[row-1][col+1] != SquareState.PINK) ||
				(withinBounds(row-1,col) && currentState[row-1][col] != SquareState.PINK) ||
				(withinBounds(row-1,col-1) && currentState[row-1][col-1] != SquareState.PINK);
	}
	
	/*
	 * Helper function for isStateWorthExpanding. This ensures that we don't try to check squares that are off the board when
	 * considering corner or edge squares in a move
	 */
	private static boolean withinBounds(int row, int col) {
		return (row >= 0 && col >= 0 && row < Game.ROW_NUMBERS && col < Game.COL_NUMBERS);
	}
	
	/**
	 * Counts the number of X in-a-rows that can be generated in a given row. This function does not yet account for
	 * holes in play, such as XX_XX, which can lead to a terminal state
	 * @param currentState The hypothetical board state
	 * @param color The color to count in-a-row of
	 * @param row The row of the board to check
	 * @param length Represents X in the description- how many stones in a row are we looking for?
	 * @return The number of X in-a-rows from this row
	 */
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
	
	/**
	 * See countInARow(). This function has a similar premise, but counts with columns instead
	 * @param currentState The hypothetical board state
	 * @param color The color to count in-a-row of
	 * @param col The column of the board to check
	 * @param length How many stones in-a-row we are looking for
	 * @return How many possible in-a-rows exist in this column
	 */
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
	
	/**
	 * Used to search for any 5 in-a-rows on the board, indicating terminal state
	 * Checks rows and columns, but not diagonals yet
	 * @param currentState The current board state
	 * @param color The color to check this condition on
	 * @return Returns the longest contiguous row of color found
	 */
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
			
			// If there are at least 5 in a row in a given row, the board is terminal- return
			if(maxInCol >= 5) {
				return 5;
			}
			
			// Otherwise, update our maximum
			if(maxInCol > maxInARow) {
				maxInARow = maxInCol;
			}
		}		
		
		return maxInARow;
	}
	
	/**
	 * Iterates through the columns in the given row, looking for the longest contiguous in-a-row of stones
	 * @param currentState The current board state
	 * @param row The row index to look through
	 * @param color The color of stone to check with
	 * @return The longest contiguous in-a-row of stones in this row
	 */
	private static int getMaxRow(SquareState[][] currentState, int row, SquareState color) {
		int longestRow = 0;
		int currentRow = 0;
		for(int i = 0; i < Game.COL_NUMBERS; i++) {
			if(currentState[row][i] == color) {
				currentRow++;
			} else {
				// If our contiguous row is broken, update the maximum if needed and reset our counter to 0
				if(longestRow < currentRow) {
					longestRow = currentRow;
				}
				currentRow = 0;
			}
		}
		return longestRow;
	}
	
	/**
	 * Iterates through the rows in the given column, looking for the longest contiguous in-a-row of stones
	 * @param currentState The current board state
	 * @param col The column index to look through
	 * @param color The color of stone to check with
	 * @return The longest contiguous in-a-row of stones in this row
	 */
	private static int getMaxCol(SquareState[][] currentState, int col, SquareState color) {
		int longestCol = 0;
		int currentCol = 0;
		for(int i = 0; i < Game.ROW_NUMBERS; i++) {
			if(currentState[i][col] == color) {
				currentCol++;
			} else {
				// If our contiguous row is broken, update the maximum if needed and reset our counter to 0
				if(longestCol < currentCol) {
					longestCol = currentCol;
				}
				currentCol = 0;
			}
		}
		return longestCol;
	}
}
