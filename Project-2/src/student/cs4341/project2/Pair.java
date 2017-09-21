package student.cs4341.project2;

public class Pair<F, S> {
    public final F first;
    public final S second;

    public Pair(final F first, final S second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == this) return true;
        if (obj == null) return false;
        if (!(obj instanceof Pair)) return false;

        final Pair other = Pair.class.cast(obj);
        if (first == null) {
            if (other.first != null) return false;
        } else if (!first.equals(other.first)) return false;

        if (second == null) {
            if (other.second != null) return false;
        } else if (!second.equals(other.second)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = result * 31 + first.hashCode();
        result = result * 31 + second.hashCode();

        return result;
    }
}
