package student.cs4341.project2.game;

import student.cs4341.project2.Pair;
import student.cs4341.project2.Utilities;

public class Game {

    private static final int ROW_NUMBERS = 15;
    private static final int COL_NUMBERS = 15;

    private static final SquareState MY_COLOR = SquareState.WHITE;
    private static final SquareState OPPONENT_COLOR = SquareState.BLACK;

    private SquareState[][] board;
    private int moveNumber;

    private Game() {
        this.board = new SquareState[Game.ROW_NUMBERS][Game.COL_NUMBERS];
        this.moveNumber = 0;
    }

    public Pair<String, Integer> playFirstMove() {
        // TODO: Fill this out
        return new Pair<>("A", 1);
    }

    public Pair<String, Integer> playWithOpponentMove(final Pair<String, Integer> movePlayed) {

        Pair<Integer, Integer> playedMove = Utilities.letterNumberPairToColRow(movePlayed);
        this.board[playedMove.first][playedMove.second] = Game.OPPONENT_COLOR;
        // TODO: Implement the logic
        // SquareState[][] potentialNewState = new SquareState[Game.ROW_NUMBERS][Game.COL_NUMBERS]
        // potentialNewState = board + a possibility
        // int heuristicValue = Heuristic.heuristic(potentialNewState)
        // int evalValue = evalState(potentialNewState)

        this.moveNumber++;
        return new Pair<>("A", 1);
    }

    public static Game newInstance() {

        Game game = new Game();
        for (int i = 0; i < Game.ROW_NUMBERS; i++) {
            for (int j = 0; j < Game.COL_NUMBERS; j++) {
                game.board[i][j] = SquareState.PINK;
            }
        }
        return new Game();
    }
}
