package student.cs4341.project2.evaluation;

import student.cs4341.project2.game.Game;
import student.cs4341.project2.game.SquareState;

public class Evaluator {
	
	/**
	 * Primary function within Evaluator to rate a given board state. This function determines whether the
	 * given board state is terminal, and if not, assigns a value to it for AB pruning and selection.
	 * @param currentState The SquareState[][] of the hypothetical move
	 * @param myColor The color of our pieces
	 * @param enemyColor The color of opponent pieces
	 * @return Returns the int evaluation score of this board state. 
	 */
	public static int evaluateMove(SquareState[][] currentState, SquareState myColor, SquareState enemyColor) {
		// Check if provided move is terminal (for us or enemy)
		// If not, initialize vars to count number of 2 in-a-rows, 3 in-a-rows and 4 in-a-rows for
		// us and enemy in currentState for evaluation
		int enemyFours = 0;
		int ourFours = 0;
		int enemyThrees = 0;
		int ourThrees = 0;
		int enemyTwos = 0;
		int ourTwos = 0;

		// In every column, go through all rows and count number of 2s, 3s and 4s (includes overlap)
		for(int i = 0; i < Game.COL_NUMBERS; i++) {
			// (F)irst SquareState (L)ength(1) (C)ount, FL2C, FL3C, SL1C, SL2C, SL3C
			int[] values = countInACol(currentState, myColor, enemyColor, i, 4, 3, 2);

			enemyFours += values[3];
			ourFours += values[0];
			enemyThrees += values[4];
			ourThrees += values[1];
			enemyTwos += values[5];
			ourTwos += values[2];
		}

		// In every row, go through all columns and count number of 2s, 3s and 4s (includes overlap)
		for(int i = 0; i < Game.ROW_NUMBERS; i++) {
			// FL1C, FL2C, FL3C, SL1C, SL2C, SL3C
			int[] values = countInARow(currentState, myColor, enemyColor, i, 4, 3, 2);

			enemyFours += values[3];
			ourFours += values[0];
			enemyThrees += values[4];
			ourThrees += values[1];
			enemyTwos += values[5];
			ourTwos += values[2];
		}



		// Go through all NWSE diagonals and count number of 2s, 3s and 4s
		for(int i = 0; i < Game.ROW_NUMBERS; i++) {
			int[] values = countInADiagNWSE(currentState, myColor, enemyColor, i, 0, 4, 3, 2);
			int[] valuesOther =  countInADiagNESW(currentState, myColor, enemyColor, i, Game.COL_NUMBERS - 1, 4, 3, 2);

			enemyFours = enemyFours + values[3] + valuesOther[3];
			ourFours = ourFours + values[0] + valuesOther[0];
			enemyThrees = enemyThrees + values[4] + valuesOther[4];
			ourThrees = ourThrees + values[1] + valuesOther[1];
			enemyTwos = enemyTwos + values[5] + values[5];
			ourTwos = ourTwos + values[2] + valuesOther[2];
		}


		for(int i = 1; i < Game.COL_NUMBERS; i++) {
			int[] values = countInADiagNWSE(currentState, myColor, enemyColor, 0, i, 4, 3, 2);
			int[] valuesOther = countInADiagNESW(currentState, myColor, enemyColor, 0, i, 4, 3, 2);

			enemyFours = enemyFours + values[3] + valuesOther[3];
			ourFours = ourFours + values[0] + valuesOther[0];
			enemyThrees = enemyThrees + values[4] + valuesOther[4];
			ourThrees = ourThrees + values[1] + valuesOther[1];
			enemyTwos = enemyTwos + values[5] + values[5];
			ourTwos = ourTwos + values[2] + valuesOther[2];
		}
		
//		// Go through all SWNE diagonals and count number of 2s, 3s and 4s
//		for(int i = (Game.ROW_NUMBERS - 6); i >= 0; i--) {
//			int[] values = countInADiagNESW(currentState, myColor, enemyColor, i, Game.COL_NUMBERS - 1, 4, 3, 2);
//
//			enemyFours += values[3];
//			ourFours += values[0];
//			enemyThrees += values[4];
//			ourThrees += values[1];
//			enemyTwos += values[5];
//			ourTwos += values[2];
//		}
//
//		for(int i = (Game.COL_NUMBERS - 6); i >= 0 ; i--) {
//			int[] values = countInADiagNESW(currentState, myColor, enemyColor, 0, i, 4, 3, 2);
//
//			enemyFours += values[3];
//			ourFours += values[0];
//			enemyThrees += values[4];
//			ourThrees += values[1];
//			enemyTwos += values[5];
//			ourTwos += values[2];
//		}

//		return ((263*ourFours) + (37*ourThrees) + (3*ourTwos) - (257*enemyFours) - (31*enemyThrees) - (2*enemyTwos));
		return ((257*ourFours) + (31*ourThrees) + (2*ourTwos) - (263*enemyFours) - (37*enemyThrees) - (3*enemyTwos));
	}
	
	/**
	 * Determines whether the provided board is terminal or not, for either us or the opponent
	 * @param currentState The hypothetical board state
	 * @param myColor Our stone color
	 * @param enemyColor Enemy stone color
	 * @return Returns 10000 if the board is terminal (we win), -10000 if board is terminal (opponent wins), or
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
	 * Counts the number of X in-a-rows with the given lengths of the first and second color state
	 * @param currentState The hypothetical board state
	 * @param first The first color to count in-a-row of
	 * @param second The second color to count in-a-row of
	 * @param row The row of the board to check
	 * @param length1 How many stones in-a-row we are looking for
	 * @param length2 Secondary number of stones in-a-row we are looking for
	 * @param length3 Tertiary number of stones in-a-row we are looking for
	 * @return An array with six elements: the first SquareState with length1 count, length2 count,
	 * and length3 count, and the second SquareState with length1 count, length2 count, and length3 count
	 */
		// RT = runningTotal, or number of contiguous in-a-line we've found of a given color
		// L1 counter keeps track of the number of contiguous elements of certain color with length1; Similarly for L2 and L3
	public static int[] countInARow(SquareState[][] currentState, SquareState first, SquareState second, int row, int length1, int length2, int length3) {
		int firstRT = 0; int firstL1Counter = 0; int firstL2Counter = 0; int firstL3Counter = 0;
		int secondRT = 0; int secondL1Counter = 0; int secondL2Counter = 0; int secondL3Counter = 0;

		for (int i = 0; i < Game.COL_NUMBERS; i++) {
			final SquareState thisPosition = currentState[row][i];
			if (thisPosition == first) {
				secondRT = 0;
				firstRT++;

				if (firstRT == length1) {
					// Check if our length found so far is open or half-open, and do the same for the remaining lengths
					if (openLengthRow(currentState, first, length1, row, i)) {
						firstL1Counter++;
					}
				}

				if (firstRT == length2) {
					if (openLengthRow(currentState, first, length2, row, i)) {
						firstL2Counter++;
					}
				}

				if (firstRT == length3) {
					if (openLengthRow(currentState, first, length3, row, i)) {
						firstL3Counter++;
					}
				}

			} else if (thisPosition == second) {
				firstRT = 0;
				secondRT++;

				if (secondRT == length1) {
					if (openLengthRow(currentState, second, length1, row, i)) {
						secondL1Counter++;
					}
				}

				if (secondRT == length2) {
					if (openLengthRow(currentState, second, length2, row, i)) {
						secondL2Counter++;
					}
				}

				if (secondRT == length3) {
					if (openLengthRow(currentState, second, length3, row, i)) {
						secondL3Counter++;
					}
				}

			}
		}

		// Return a new array to contain the lengths found
		return new int[]{firstL1Counter, firstL2Counter, firstL3Counter, secondL1Counter, secondL2Counter, secondL3Counter};
	}

	/**
	 * See countInARow(). This function has a similar premise, but counts with columns instead
	 * @param currentState The hypothetical board state
	 * @param first The first color to count in-a-row of
	 * @param second The second color to count in-a-row of
	 * @param col The column of the board to check
	 * @param length1 How many stones in-a-row we are looking for
	 * @param length2 Secondary number of stones in-a-row we are looking for
	 * @param length3 Tertiary number of stones in-a-row we are looking for
	 * @return An array with six elements: the first SquareState with length1 count, length2 count,
	 * and length3 count, and the second SquareState with length1 count, length2 count, and length3 count
	 */
		// RT is the running total
		// L1 counter keeps track of the number of contiguous elements of certain color with length1; Similarly for L2 and L3
	public static int[] countInACol(SquareState[][] currentState, SquareState first, SquareState second, int col, int length1, int length2, int length3) {
		int firstRT = 0; int firstL1Counter = 0; int firstL2Counter = 0; int firstL3Counter = 0;
		int secondRT = 0; int secondL1Counter = 0; int secondL2Counter = 0; int secondL3Counter = 0;

		for (int i = 0; i < Game.ROW_NUMBERS; i++) {
			final SquareState thisPosition = currentState[i][col];
			if (thisPosition == first) {
				secondRT = 0;
				firstRT++;

				if (firstRT == length1) {
					if (openLengthCol(currentState, first, length1, i, col)) {
						firstL1Counter++;
					}
				}

				if (firstRT == length2) {
					if (openLengthCol(currentState, first, length2, i, col)) {
						firstL2Counter++;
					}
				}

				if (firstRT == length3) {
					if (openLengthCol(currentState, first, length3, i, col)) {
						firstL3Counter++;
					}
				}

			} else if (thisPosition == second) {
				firstRT = 0;
				secondRT++;

				if (secondRT == length1) {
					if (openLengthCol(currentState, second, length1, i, col)) {
						secondL1Counter++;
					}
				}

				if (secondRT == length2) {
					if (openLengthCol(currentState, second, length2, i, col)) {
						secondL2Counter++;
					}
				}

				if (secondRT == length3) {
					if (openLengthCol(currentState, second, length3, i, col)) {
						secondL3Counter++;
					}
				}

			}
		}
		
		// Return a new array to contain the lengths found
		return new int[]{firstL1Counter, firstL2Counter, firstL3Counter, secondL1Counter, secondL2Counter, secondL3Counter};
	}
	
	public static int[] countInADiagNWSE(SquareState[][] currentState, SquareState first, SquareState second, int row, int col, int length1, int length2, int length3) {
		int firstRT = 0; int firstL1Counter = 0; int firstL2Counter = 0; int firstL3Counter = 0;
		int secondRT = 0; int secondL1Counter = 0; int secondL2Counter = 0; int secondL3Counter = 0;

		for(int i = row, j = col; i < Game.ROW_NUMBERS && j < Game.COL_NUMBERS; i++, j++) {
			final SquareState thisPosition = currentState[i][j];
			if (thisPosition == first) {
				secondRT = 0; // We found the first color so break the running total of the second color
				firstRT++;

				if ((firstRT >= length1)) {
					if (openLengthDiagonalNWSE(currentState, first, length1, i, j)) {
						firstL1Counter++;
					}
				}

				if (firstRT >= length2) {
					if (openLengthDiagonalNWSE(currentState, first, length2, i, j)) {
						firstL2Counter++;
					}
				}

				if (firstRT >= length3) {
					if (openLengthDiagonalNWSE(currentState, first, length3, i, j)) {
						firstL3Counter++;
					}
				}

			} else if (thisPosition == second) {
				firstRT = 0; // We found the second color so break the running total for the first color
				secondRT++;

				if (secondRT >= length1) {
					if (openLengthDiagonalNWSE(currentState, second, length1, i, j)) {
						secondL1Counter++;
					}
				}

				if (secondRT >= length2) {
					if (openLengthDiagonalNWSE(currentState, second, length2, i, j)) {
						secondL2Counter++;
					}
				}

				if (secondRT >= length3) {
					if (openLengthDiagonalNWSE(currentState, second, length3, i, j)) {
						secondL3Counter++;
					}
				}

			}
		}
		
		// Return a new array to contain the lengths found
		return new int[]{firstL1Counter, firstL2Counter, firstL3Counter, secondL1Counter, secondL2Counter, secondL3Counter};
	}
	
	private static int[] countInADiagNESW(SquareState[][] currentState, SquareState first, SquareState second, int row, int col, int length1, int length2, int length3) {
		int firstRT = 0; int firstL1Counter = 0; int firstL2Counter = 0; int firstL3Counter = 0;
		int secondRT = 0; int secondL1Counter = 0; int secondL2Counter = 0; int secondL3Counter = 0;

		for(int i = row, j = col; i < Game.ROW_NUMBERS && j >= 0; i++, j--) {
			final SquareState thisPosition = currentState[i][j];
			if (thisPosition == first) {
				secondRT = 0; // We found the first color so break the running total of the second color
				firstRT++;

				if (firstRT >= length1) {
					if (openLengthDiagonalNESW(currentState, first, length1, i, j)) {
						firstL1Counter++;
					}
				}

				if (firstRT >= length2) {
					if (openLengthDiagonalNESW(currentState, first, length2, i, j)) {
						firstL2Counter++;
					}
				}

				if (firstRT >= length3) {
					if (openLengthDiagonalNESW(currentState, first, length3, i, j)) {
						firstL3Counter++;
					}
				}

			} else if (thisPosition == second) {
				firstRT = 0; // We found the second color so break the running total for the first color
				secondRT++;

				if (secondRT >= length1) {
					if (openLengthDiagonalNESW(currentState, second, length1, i, j)) {
						secondL1Counter++;
					}
				}

				if (secondRT >= length2) {
					if (openLengthDiagonalNESW(currentState, second, length2, i, j)) {
						secondL2Counter++;
					}
				}

				if (secondRT >= length3) {
					if (openLengthDiagonalNESW(currentState, second, length3, i, j)) {
						secondL3Counter++;
					}
				}
			}
		}
		// Return a new array to contain the lengths found
		return new int[]{firstL1Counter, firstL2Counter, firstL3Counter, secondL1Counter, secondL2Counter, secondL3Counter};
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
		
		// And the same from diagonals from NorthWest to SouthEast
		for(int i = 0; i < Game.COL_NUMBERS - 4; i++) {
			int maxInNWSE = getMaxDiagNWSE(currentState, 0, i, color);
			
			// If there are at least 5 in a row in a given row, the board is terminal- return
			if(maxInNWSE >= 5) {
				return 5;
			}
			
			// Otherwise, update our maximum
			if(maxInNWSE > maxInARow) {
				maxInARow = maxInNWSE;
			}
		}
		
		// No need to double check diag at 0,0- start at 1
		for(int i = 1; i < Game.ROW_NUMBERS - 4; i++) {
			int maxInNWSE = getMaxDiagNWSE(currentState, i, 0, color);
			
			// If there are at least 5 in a row in a given row, the board is terminal- return
			if(maxInNWSE >= 5) {
				return 5;
			}
			
			// Otherwise, update our maximum
			if(maxInNWSE > maxInARow) {
				maxInARow = maxInNWSE;
			}
		}
		
		
		// And the same from diagonals from SouthWest to NorthEast
		for(int i = 4; i < Game.COL_NUMBERS; i++) {
			int maxInSWNE = getMaxDiagNESW(currentState, 0, i, color);
			
			// If there are at least 5 in a row in a given row, the board is terminal- return
			if(maxInSWNE >= 5) {
				return 5;
			}
			
			// Otherwise, update our maximum
			if(maxInSWNE > maxInARow) {
				maxInARow = maxInSWNE;
			}
		}
		
		for(int i = 1; i < Game.ROW_NUMBERS - 4 ; i++) {
			int maxInSWNE = getMaxDiagNESW(currentState, i, Game.COL_NUMBERS - 1, color);
			
			// If there are at least 5 in a row in a given row, the board is terminal- return
			if(maxInSWNE >= 5) {
				return 5;
			}
			
			// Otherwise, update our maximum
			if(maxInSWNE > maxInARow) {
				maxInARow = maxInSWNE;
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
	
	/**
	 * Iterates through the NorthWest to SouthEast diagonal starting at the given row and column, looking for longest contiguous in-a-row of stones
	 * @param currentState The current board state
	 * @param row The row index to start at
	 * @param col The column index to start at
	 * @param color The color of the stone to check with
	 * @return The longest contiguous in-a-row of stones in this diagonal
	 */
	private static int getMaxDiagNWSE(SquareState[][] currentState, int row, int col, SquareState color) {
		int longestDiag = 0;
		int currentDiag = 0;
		for(int i = row, j = col; i < Game.ROW_NUMBERS && j < Game.COL_NUMBERS; i++, j++) {
			if(currentState[i][j] == color) {
				currentDiag++;
			} else {
				if(longestDiag < currentDiag) {
					longestDiag = currentDiag;
				}
				currentDiag = 0;
			}
		}
		return longestDiag;
	}
	
	/**
	 * Iterates through the SouthWest to NorthEast diagonal starting at the given row and column, looking for longest contiguous in-a-row of stones
	 * @param currentState The current board state
	 * @param row The row index to start at
	 * @param col The column index to start at
	 * @param color The color of the stone to check with
	 * @return The longest contiguous in-a-row of stones in this diagonal
	 */
	private static int getMaxDiagNESW(SquareState[][] currentState, int row, int col, SquareState color) {
		int longestDiag = 0;
		int currentDiag = 0;
		for(int i = row, j = col; i < Game.ROW_NUMBERS && j >= 0; i++, j--) {
			if(currentState[i][j] == color) {
				currentDiag++;
			} else {
				if(longestDiag < currentDiag) {
					longestDiag = currentDiag;
				}
				currentDiag = 0;
			}
		}
		return longestDiag;
	}

	/**
	 * Helper function to determine if a row is open or half-open
	 * @param currentState The current board state
	 * @param color The color of stone to check with
	 * @param length The length to look for
	 * @param i The row of the board to check
	 * @param j The column of the board to check
	 * @return Returns true if it's open, and false if it's half-open
	 */
	private static boolean openLengthCol(SquareState[][] currentState, SquareState color, int length, int i, int j) {
		// if left boundary or not free, check if right side is free
		if ((i - length < 0) || (currentState[i - length][j] != SquareState.PINK)) {
			return i + 1 < Game.ROW_NUMBERS && currentState[i + 1][j] == SquareState.PINK;
		}

		// If left is free, check if right side is not my color
		return (i + 1 >= Game.ROW_NUMBERS) || currentState[i + 1][j] != color;
	}

	/**
	 * Helper function to determine if a column is open or half-open
	 * @param currentState The current board state
	 * @param color The color of stone to check with
	 * @param length The length to look for
	 * @param i The row of the board to check
	 * @param j The column of the board to check
	 * @return Returns true if it's open, and false if it's half-open
	 */
	private static boolean openLengthRow(SquareState[][] currentState, SquareState color, int length, int i, int j) {
		// if top boundary or not free, check if right side is free
		if ((j - length < 0) || (currentState[i][j - length] != SquareState.PINK)) {
			return j + 1 < Game.COL_NUMBERS && currentState[i][j + 1] == SquareState.PINK;
		}

		// If top is free, check if bottom side is not my color
		return (j + 1 >= Game.COL_NUMBERS) || currentState[i][j + 1] != color;
	}

	private static boolean openLengthDiagonalNWSE(SquareState[][] currentState, SquareState color, int length, int i, int j) {
		if (((j - length < 0) || (i - length < 0)) || (currentState[i - length][j - length] != SquareState.PINK)) {
			return j + 1 < Game.COL_NUMBERS && i + 1 < Game.ROW_NUMBERS && currentState[i+1][j+1] == SquareState.PINK;
		}
		return (j + 1 >= Game.COL_NUMBERS || i + 1 >= Game.ROW_NUMBERS) || currentState[i+1][j+1] != color;
	}

	private static boolean openLengthDiagonalNESW(SquareState[][] currentState, SquareState color, int length, int i, int j) {
		if ((i - length < 0) || (j + length >= Game.COL_NUMBERS) || (currentState[i - length][j + length] != SquareState.PINK)) {
			return ((((j - 1) >= 0) && (i + 1 < Game.ROW_NUMBERS)) && currentState[i+1][j-1] == SquareState.PINK);
		}
		return (j - 1 < 0 || i + 1 >= Game.ROW_NUMBERS) || currentState[i + 1][j - 1] != color;
	}
	
}
