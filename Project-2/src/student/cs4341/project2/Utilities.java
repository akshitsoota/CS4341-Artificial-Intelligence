package student.cs4341.project2;

public final class Utilities {
    private Utilities() {

    }

    public static <T> Function<T, T> identityMapper() {
        return (T original) -> original;
    }

    public static Pair<String, Integer> colRowToLetterNumberPair(final int columnNumber, final int rowNumber) {
        return new Pair<>(String.valueOf( (char) ('a' + columnNumber)), rowNumber + 1);
    }

    public static Pair<Integer, Integer> letterNumberPairToColRow(final Pair<String, Integer> pair) {
        return new Pair<>(Character.getNumericValue(pair.first.charAt(0)) - Character.getNumericValue('a'), pair.second - 1);
    }
}
