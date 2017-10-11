package student.cs4341.project2.game;

import student.cs4341.project2.Pair;
import student.cs4341.project2.Utilities;
import student.cs4341.project2.evaluation.Evaluator;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class Game {
	// Definitions for the board size
    public static final int ROW_NUMBERS = 15;
    public static final int COL_NUMBERS = 15;
    private static final int EVALUATION_SLEEP = 8000; // ms

    // Definitions for our and opponents color
    private SquareState MY_COLOR = SquareState.WHITE;
    private SquareState OPPONENT_COLOR = SquareState.BLACK;

    // Our board
    private SquareState[][] board;

    /**
     * Default constructor for game. Initializes the board to the correct dimensions
     */
    private Game() {
        this.board = new SquareState[Game.ROW_NUMBERS][Game.COL_NUMBERS];
    }

    /**
     * Plays the first move in this game, which is always the center of the board
     * @return Returns a Pair<String, Integer> indicating the coordinate of the played move
     */
    public Pair<String, Integer> playFirstMove() {
    	final Pair<String, Integer> playedMove = new Pair<>("h", 8);

        final Pair<Integer, Integer> boardMovePlayed = Utilities.letterNumberPairToColRow(playedMove);
    	this.board[boardMovePlayed.first][boardMovePlayed.second] = MY_COLOR;

        return playedMove;
    }

    /**
     * Considers the move played by the opponent and determines what move to play next
     * @param movePlayed The opponent's move
     * @return Returns a Pair<String, Integer> indicating the coordinate of the played move
     */
    public Pair<String, Integer> playWithOpponentMove(final Pair<String, Integer> movePlayed) {
        final Pair<Integer, Integer> playedMove = Utilities.letterNumberPairToColRow(movePlayed);

        this.board[playedMove.first][playedMove.second] = OPPONENT_COLOR;

        SquareState[][] currentState = Game.copySquareStateArray(this.board);

        // Hold onto the x,y for our best move so far
        Pair<Integer, Integer> firstMove = Game.firstAvailableMoveIndex(currentState);
        int bestMoveSoFarI = firstMove.first;
        int bestMoveSoFarJ = firstMove.second;

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

        System.out.println("EARLY THREAD CHECK: Thread interrupted? =  " + Thread.currentThread().isInterrupted());
        System.out.println("PLAY WITH OPPONENT BEST MOVE - i : " + bestMoveSoFarI + ", j : " + bestMoveSoFarJ + ", VAL = " + player.maxValue);

        // Shutdown after we've pulled out our values
        threading.shutdownNow();

        // Perform the best move found so far!
        this.board[bestMoveSoFarI][bestMoveSoFarJ] = MY_COLOR;
        return Utilities.colRowToLetterNumberPair(bestMoveSoFarI, bestMoveSoFarJ);
    }

    /**
     * Private internal class to be spawned as a separate thread. This will run the computations for the best move and keep
     * BestMovePossibleI/J up to date for the parent thread
     */
    private class PlayRunnable implements Runnable {
        private final SquareState[][] currentState;
        Pair<Integer, Integer> bestMoveSoFar;
        int maxValue;

        /**
         * Default constructor for this thread class
         *
         * @param currentState   The current state of the board
         * @param bestMoveSoFarI The row index of the best move so far
         * @param bestMoveSoFarJ The column index of the best move so far
         */
        PlayRunnable(final SquareState[][] currentState, final int bestMoveSoFarI, final int bestMoveSoFarJ) {
            this.currentState = currentState;
            this.bestMoveSoFar = new Pair<>(bestMoveSoFarI, bestMoveSoFarJ);
            this.maxValue = -2;
        }

        @Override
        /**
         * Main method for this thread. This runs our alpha-beta pruning and expansion heuristics
         */
        public void run() {
            int depth = 1;

            System.out.println("THREAD CHECK: depth = " + depth + ", Thread interrupted? =  " + Thread.currentThread().isInterrupted());

            while (!Thread.currentThread().isInterrupted()) {

                Pair<Integer, Integer> firstMove = Game.firstAvailableMoveIndex(currentState);
                // Initialize our values to hold onto our best move coordinates for this depth
                int maxI = firstMove.first;
                int maxJ = firstMove.second;
                int currentMax = Integer.MIN_VALUE;


                // Initialize our alpha and beta for pruning
                int alpha = Integer.MIN_VALUE;
                int beta = Integer.MAX_VALUE;

                // Iterate through all possible moves on the board for us
                for (int i = 0; i < currentState.length; i++) {
                    for (int j = 0; j < currentState[0].length; j++) {
                        System.out.println("CURRENT_STATE = " + currentState[i][j]);
                        if (currentState[i][j] == SquareState.PINK) {
                            currentState[i][j] = MY_COLOR;

                            // Determine if this board is worth expanding (is the move adjacent to any existing stones?)
                            if (Evaluator.isStateWorthExpanding(currentState, i, j)) {
                                int currentStateValue = iterativeDeepeningMove(currentState, depth, OPPONENT_COLOR, alpha, beta);
                                // If the value found here is greater than our best so far, update the variables
                                System.out.println("CURRENT_STATE_VALUE = " + currentStateValue);
                                if (currentStateValue > currentMax) {
                                    currentMax = currentStateValue;
                                    maxI = i;
                                    maxJ = j;
                                }
                            }

                            // Reset this move to check the next permutation
                            currentState[i][j] = SquareState.PINK;
                        }
                    }
                }

                System.out.println("PLAY WITH OPPONENT BEST MOVE - i : " + maxI + ", j : " + maxJ + ", VAL = " + currentMax);

                this.bestMoveSoFar = new Pair<>(maxI, maxJ);
                this.maxValue = currentMax;
                depth++;
            }
        }
    }

        /**
         * Begins our Iterative Deepening Search with alpha-beta pruning
         *
         * @param board The state of the board with the hypothetical move added
         * @param depth The current depth of our IDS
         * @param turn  Which player's move it is
         * @param alpha Our alpha value
         * @param beta  Our beta value
         * @return Returns the evaluation value for this move, or the utility value if this is a terminal board
         */
    private int iterativeDeepeningMove(SquareState[][] board, int depth, SquareState turn, int alpha, int beta) {
        // If our turn has ended, kill the thread and return a negative value
        if (Thread.currentThread().isInterrupted()) {
            System.out.println("THREAD IS INTERRUPTED");
            return Integer.MIN_VALUE;
        }

        // First check if this board is terminal. If it is, return and halt execution
        final int terminalValue = Evaluator.isTerminal(board, MY_COLOR, OPPONENT_COLOR);
        if (terminalValue != 0) {
            System.out.println("TERMINAL IS RETURNED " + terminalValue);
            return terminalValue; //+ Evaluator.evaluateMove(board, MY_COLOR, OPPONENT_COLOR);
        }

        // If we have reached the top of our depth, evaluate this move and return
        if (depth == 0) {
            int eval = Evaluator.evaluateMove(board, MY_COLOR, OPPONENT_COLOR);
            System.out.println("EVALUATOR IS RETURNED " + eval);
            return eval;
        }

        // Otherwise, begin alpha-beta pruning
        // MAX function
        if (turn == MY_COLOR) {
            int max = Integer.MIN_VALUE;
            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board[0].length; j++) {
                    if ((board[i][j] == SquareState.PINK)) {
                        board[i][j] = MY_COLOR;

                        max = Math.max(max, iterativeDeepeningMove(board, depth - 1, OPPONENT_COLOR, alpha, beta));
                        board[i][j] = SquareState.PINK;
                        if (max >= beta) {
                            System.out.println("FIRST MAX IS RETURNED " + max);
                            return max;
                        }
                        alpha = Math.max(alpha, max);
                    }
                }
            }
            System.out.println("SECOND MAX IS RETURNED " + max);
            return max;
        }

        // MIN function
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if ((board[i][j] == SquareState.PINK)) {
                    board[i][j] = OPPONENT_COLOR;

                    min = Math.min(min, iterativeDeepeningMove(board, depth - 1, OPPONENT_COLOR, alpha, beta));
                    board[i][j] = SquareState.PINK;
                    if (min <= alpha) {
                        System.out.println("FIRST MIN IS RETURNED " + min);
                        return min;
                    }
                    beta = Math.min(beta, min);
                }
            }
        }

        System.out.println("SECOND MIN IS RETURNED " + min);
        return min;
    }

    /**
     * @return Returns our stone color
     */
    public SquareState getMyColor() {
        return MY_COLOR;
    }

    /**
     * @return Returns the enemy's stone color
     */
    public SquareState getEnemyColor() {
        return OPPONENT_COLOR;
    }

    /**
     * Factory method for the Game object
     *
     * @return Returns an initialized instance of the Game with an empty board
     */
    public static Game newInstance() {
        Game game = new Game();
        for (int i = 0; i < Game.ROW_NUMBERS; i++) {
            for (int j = 0; j < Game.COL_NUMBERS; j++) {
                game.board[i][j] = SquareState.PINK;
            }
        }
        return game;
    }

    /**
     * Helper function to copy our board state to a new array to prevent mutation
     *
     * @param board The current board state
     * @return Returns a new board matching the provided board
     */
    private static SquareState[][] copySquareStateArray(SquareState[][] board) {
        SquareState[][] newState = new SquareState[ROW_NUMBERS][COL_NUMBERS];
        for (int i = 0; i < board.length; i++) {
            System.arraycopy(board[i], 0, newState[i], 0, board[0].length);
        }
        return newState;
    }

    private static Pair<Integer, Integer> firstAvailableMoveIndex(SquareState[][] board) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j] == SquareState.PINK) {
                    return new Pair<>(i, j);
                }
            }
        }
        // Should never execute
        return new Pair<>(0,0);
    }
}
