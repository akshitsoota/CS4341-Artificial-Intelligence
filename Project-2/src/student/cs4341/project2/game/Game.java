package student.cs4341.project2.game;

import student.cs4341.project2.Pair;
import student.cs4341.project2.Utilities;
import student.cs4341.project2.evaluation.Evaluator;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class Game {
    public static final int ROW_NUMBERS = 15;
    public static final int COL_NUMBERS = 15;
    private static final int EVALUATION_SLEEP = 4000; // ms

    private SquareState MY_COLOR = SquareState.WHITE;
    private SquareState OPPONENT_COLOR = SquareState.BLACK;

    private SquareState[][] board;
    private int moveNumber;

    private Game() {
        this.board = new SquareState[Game.ROW_NUMBERS][Game.COL_NUMBERS];
        this.moveNumber = 0;
    }

    public Pair<String, Integer> playFirstMove() {
    	final Pair<String, Integer> playedMove = new Pair<>("h", 8);

        final Pair<Integer, Integer> boardMovePlayed = Utilities.letterNumberPairToColRow(playedMove);
    	this.board[boardMovePlayed.first][boardMovePlayed.second] = MY_COLOR;

        return playedMove;
    }

    public Pair<String, Integer> playWithOpponentMove(final Pair<String, Integer> movePlayed) {
        final Pair<Integer, Integer> playedMove = Utilities.letterNumberPairToColRow(movePlayed);

        this.board[playedMove.first][playedMove.second] = OPPONENT_COLOR;

        // TODO: remove this as there's a better way
        SquareState[][] currentState = Game.copySquareStateArray(this.board);

        int bestMoveSoFarI = Integer.MIN_VALUE;
        int bestMoveSoFarJ = Integer.MIN_VALUE;

        // Spawn game playing thread
        ScheduledExecutorService threading = Executors.newSingleThreadScheduledExecutor();
        PlayRunnable player = new PlayRunnable(currentState, bestMoveSoFarI, bestMoveSoFarJ);

        threading.execute(player);

        // Give the thread it's own sweet time
        try {
            Thread.sleep(Game.EVALUATION_SLEEP);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Fetch latest state from the thread
        Pair<Integer, Integer> bestMoveSoFar = player.bestMoveSoFar;
        bestMoveSoFarI = bestMoveSoFar.first;
        bestMoveSoFarJ = bestMoveSoFar.second;

        // Shutdown after we've pulled out our values
        threading.shutdownNow();

        // Perform the best move found so far!
        this.moveNumber++;
        this.board[bestMoveSoFarI][bestMoveSoFarJ] = MY_COLOR;
        return Utilities.colRowToLetterNumberPair(bestMoveSoFarI, bestMoveSoFarJ);
    }

    private class PlayRunnable implements Runnable {
        private final SquareState[][] currentState;
        Pair<Integer, Integer> bestMoveSoFar;

        PlayRunnable(final SquareState[][] currentState, final int bestMoveSoFarI, final int bestMoveSoFarJ) {
            this.currentState = currentState;
            this.bestMoveSoFar = new Pair<>(bestMoveSoFarI, bestMoveSoFarJ);
        }

        @Override
        public void run() {
            int depth = 1;

            // TODO: think about if we want to compare
            int bestMoveMax = Integer.MIN_VALUE;

            while (!Thread.currentThread().isInterrupted()) {
                System.out.println("beginning depth " + depth);
                if (depth >= 2) break;

                int maxI = Integer.MIN_VALUE;
                int maxJ = Integer.MIN_VALUE;
                int currentMax = Integer.MIN_VALUE;

                int alpha = Integer.MIN_VALUE;
                int beta = Integer.MAX_VALUE;

                for (int i = 0; i < currentState.length; i++) {
                    for (int j = 0; j < currentState[0].length; j++) {
                        if (currentState[i][j] == SquareState.PINK) {
                            currentState[i][j] = MY_COLOR;
                            if(Evaluator.isStateWorthExpanding(currentState, i, j)) {
                                //System.out.println(i + "," + j + " is worth expanding");
                                int currentStateValue = iterativeDeepeningMove(currentState, depth, OPPONENT_COLOR, alpha, beta);
                                if (currentStateValue > currentMax) {
                                    currentMax = currentStateValue;
                                    maxI = i;
                                    maxJ = j;
                                }
                                if(currentStateValue != 0) {
                                    System.out.println("Value for row " + i + " and col " + j +" is " + currentStateValue );                              	
                                }
                            } else {
                                //System.out.println(i + "," + j + " is not worth expanding");
                            }
                            currentState[i][j] = SquareState.PINK;
                        }
                    }
                }

                this.bestMoveSoFar = new Pair<>(maxI, maxJ);
                depth++;
            }
        }
    }

    private int iterativeDeepeningMove(SquareState[][] board, int depth, SquareState turn, int alpha, int beta) {
        if (Thread.currentThread().isInterrupted()) {
            return Integer.MIN_VALUE;
        }

        final int terminalValue = Evaluator.isTerminal(board, MY_COLOR, OPPONENT_COLOR);

        if (terminalValue != 0) {
        	System.out.println(terminalValue + " is terminal");
            return terminalValue;
        }

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
