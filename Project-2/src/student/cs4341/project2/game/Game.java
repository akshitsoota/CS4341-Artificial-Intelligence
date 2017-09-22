package student.cs4341.project2.game;

import student.cs4341.project2.Pair;
import student.cs4341.project2.Utilities;
import student.cs4341.project2.evaluation.Evaluator;

public class Game {

    public static final int ROW_NUMBERS = 15;
    public static final int COL_NUMBERS = 15;

    private SquareState MY_COLOR = SquareState.BLACK;
    private SquareState OPPONENT_COLOR = SquareState.WHITE;

    private SquareState[][] board;
    private int moveNumber;

    private Game() {
        this.board = new SquareState[Game.ROW_NUMBERS][Game.COL_NUMBERS];
        this.moveNumber = 0;
    }

    public Pair<String, Integer> playFirstMove() {
        // TODO: Fill this out
    	try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return new Pair<>("a", 1);
    }

    public Pair<String, Integer> playWithOpponentMove(final Pair<String, Integer> movePlayed) {

        Pair<Integer, Integer> playedMove = Utilities.letterNumberPairToColRow(movePlayed);

        this.board[playedMove.first][playedMove.second] = OPPONENT_COLOR;

        int depth = 1;

        // TODO: remove this as there's a better way
        SquareState[][] currentState = Game.copySquareStateArray(this.board);

        int bestMoveSoFarI = Integer.MIN_VALUE;
        int bestMoveSoFarJ = Integer.MIN_VALUE;

        // TODO: think about if we want to compare
        int bestMoveMax = Integer.MIN_VALUE;

        while (true) {

            if (depth >= 2) {
                break;
            }
            System.out.println("beginning depth " + depth);
            
            int maxI = Integer.MIN_VALUE;
            int maxJ = Integer.MIN_VALUE;
            int currentMax = Integer.MIN_VALUE;

            int alpha = Integer.MIN_VALUE;
            int beta = Integer.MAX_VALUE;

            for (int i = 0; i < currentState.length; i++) {
                for (int j = 0; j < currentState[0].length; j++) {
                    if (currentState[i][j] == SquareState.PINK) {
                        currentState[i][j] = MY_COLOR;
                        int currentStateValue = iterativeDeepeningMove(currentState, depth, OPPONENT_COLOR, alpha, beta);
                        if (currentStateValue > currentMax) {
                            currentMax = currentStateValue;
                            maxI = i;
                            maxJ = j;
                        }
                        currentState[i][j] = SquareState.PINK;
                    }
                }
            }

            bestMoveSoFarI = maxI;
            bestMoveSoFarJ = maxJ;
            depth++;

        }

        this.moveNumber++;
        return Utilities.colRowToLetterNumberPair(bestMoveSoFarI, bestMoveSoFarJ);
    }

    private int iterativeDeepeningMove(SquareState[][] board, int depth, SquareState turn, int alpha, int beta) {
        if (depth == 0) {
            return Evaluator.evaluateMove(board, MY_COLOR, OPPONENT_COLOR);
        }

        // MAX function
        if (turn == MY_COLOR) {
            int max = Integer.MIN_VALUE;
            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board[0].length; j++) {
                    if (board[i][j] == SquareState.PINK) {
                        board[i][j] = MY_COLOR;

                        max = Math.max(max, iterativeDeepeningMove(board, depth - 1, OPPONENT_COLOR, alpha, beta));
                        board[i][j] = SquareState.PINK;
                        if (max >= beta) {
                            return max;
                        }
                        alpha = Math.max(alpha, max);
                    }
                }
            }
            return max;
        }

        // MIN function
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j] == SquareState.PINK) {
                    board[i][j] = OPPONENT_COLOR;

                    min = Math.min(min, iterativeDeepeningMove(board, depth - 1, OPPONENT_COLOR, alpha, beta));
                    board[i][j] = SquareState.PINK;
                    if (min <= alpha) {
                        return min;
                    }
                    beta = Math.min(beta, min);
                }
            }
        }
        return min;

    }
    
    public SquareState getMyColor() {
    	return MY_COLOR;
    }
    
    public SquareState getEnemyColor() {
    	return OPPONENT_COLOR;
    }
    
    public void setMyColor(SquareState color) {
    	MY_COLOR = color;
    }
    
    public void setEnemyColor(SquareState color) {
    	OPPONENT_COLOR = color;
    }

    public static Game newInstance() {

        Game game = new Game();
        for (int i = 0; i < Game.ROW_NUMBERS; i++) {
            for (int j = 0; j < Game.COL_NUMBERS; j++) {
                game.board[i][j] = SquareState.PINK;
            }
        }
        return game;
    }

    private static SquareState[][] copySquareStateArray(SquareState[][] board) {
        SquareState[][] newState = new SquareState[ROW_NUMBERS][COL_NUMBERS];
        for (int i = 0; i < board.length; i++) {
            System.arraycopy(board[i], 0, newState[i], 0, board[0].length);
        }
        return newState;
    }
}
