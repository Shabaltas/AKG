package entity;

import java.util.Objects;

public class Pair {

    public ScreenVertice pos1;

    public ScreenVertice pos2;

    public Pair(ScreenVertice pos1, ScreenVertice pos2) {
        this.pos1 = pos1;
        this.pos2 = pos2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair pair = (Pair) o;
        return Objects.equals(pos1, pair.pos1) &&
                Objects.equals(pos2, pair.pos2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pos1, pos2);
    }
}
