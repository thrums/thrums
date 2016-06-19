package no.thrums.instance.ri.path;

import no.thrums.instance.path.Node;

import static java.util.Objects.requireNonNull;

/**
* @author Kristian Myrhaug
* @since 2015-02-24
*/
public class RiIndexNode implements Node<Integer> {

    private Integer value;

    public RiIndexNode(Integer value) {
        this.value = requireNonNull(value, "May not be null: value");
    }

    @Override
    public Type getType() {
        return Type.INDEX;
    }

    @Override
    public Integer getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "[" + String.valueOf(getValue()) + ']';
    }
}
