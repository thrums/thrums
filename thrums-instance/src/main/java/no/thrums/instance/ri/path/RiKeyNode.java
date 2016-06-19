package no.thrums.instance.ri.path;

import no.thrums.instance.path.Node;

import static java.util.Objects.requireNonNull;

/**
 * @author Kristian Myrhaug
 * @since 2015-02-24
 */
public class RiKeyNode implements Node<String> {

    private String value;

    public RiKeyNode(String value) {
        this.value = requireNonNull(value, "May not be null: value");
    }

    @Override
    public Type getType() {
        return Type.KEY;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "." + getValue();
    }
}
