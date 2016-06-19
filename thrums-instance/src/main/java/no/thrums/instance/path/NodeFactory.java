package no.thrums.instance.path;

/**
 * @author Kristian Myrhaug
 * @since 2016-06-19
 */
public interface NodeFactory {

    Node createKey(String key);
    Node createIndex(Integer index);
}
