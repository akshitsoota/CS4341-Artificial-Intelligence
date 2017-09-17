package student.cs4341.project2.game;

import student.cs4341.project2.Pair;

public class Game {
    private Game() {

    }

    public Pair<String, Integer> playFirstMove() {
        // TODO: Fill this out
        return new Pair<>("A", 1);
    }

    public Pair<String, Integer> playWithOpponentMove(final Pair<String, Integer> movePlayed) {
        // TODO: Fill this out

        // Convert movePlayed to col, row pair by using Utilities.letterNumberPairToColRow
        // Convert your new col, row move to Utilities.colRowToLetterNumberPair

        return new Pair<>("A", 1);
    }

    public static Game newInstance() {
        return new Game();
    }
}
