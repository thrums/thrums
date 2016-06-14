package no.thrums.difference.ri;

import no.thrums.difference.Difference;
import no.thrums.instance.Instance;
import no.thrums.instance.path.Path;

import java.util.Objects;

import static java.util.Objects.requireNonNull;

/**
 * @author Kristian Myrhaug
 * @since 2016-06-05
 */
public class RiDifference implements Difference {

    private Path path;
    private Instance first;
    private Instance second;

    public RiDifference(Path path, Instance first, Instance second) {
        this.path = requireNonNull(path, "May not be null: path");
        this.first = requireNonNull(first, "May not be null: first");
        this.second = requireNonNull(second, "May not be null: second");
    }

    @Override
    public Path getPath() {
        return path;
    }

    @Override
    public Instance getFirst() {
        return first;
    }

    @Override
    public Instance getSecond() {
        return second;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof Difference && equals((Difference)other);
    }

    public boolean equals(Difference other) {
        return  Objects.equals(path, other.getPath()) &&
                Objects.equals(first, other.getFirst()) &&
                Objects.equals(second, other.getSecond());
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                path,
                first,
                second
        );
    }
}
