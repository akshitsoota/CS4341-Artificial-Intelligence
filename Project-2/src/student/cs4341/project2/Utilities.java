package student.cs4341.project2;

public final class Utilities {
    /**
     * Private Constructor for Utilities class
     */
    private Utilities() {
        throw new IllegalStateException("Must disallow instantiation of: FileUtilities");
    }

    /**
     * Type-safe identity mapper<br />
     * Type is determined by the caller of the function
     * @param <T>
     * @return
     */
    public static <T> Function<T, T> identityMapper() {
        return new Function<T, T>() {
            @Override
            public T apply(T input) {
                return input;
            }
        };
    }

    /**
     * Converts a (col, row) pair to a move as described in the .go files<br />
     * Like: (1, 1) would translate to ("b", 2) and (0, 0) would translate to ("a", 1)<br />
     * This function does <b>not</b> do bound checks
     * @param columnNumber
     * @param rowNumber
     * @return
     */
    public static Pair<String, Integer> colRowToLetterNumberPair(final int columnNumber, final int rowNumber) {
        return new Pair<>(String.valueOf( (char) ('a' + columnNumber)), rowNumber + 1);
    }

    /**
     * Converts a move read in from the .go files to what can be mapped on the game board<br />
     * Like: ("a", 1) would map to (0, 0) and ("b", 2) will map to (1, 1)
     * @param pair
     * @return
     */
    public static Pair<Integer, Integer> letterNumberPairToColRow(final Pair<String, Integer> pair) {
        return new Pair<>(Character.getNumericValue(pair.first.charAt(0)) - Character.getNumericValue('a'), pair.second - 1);
    }
}
