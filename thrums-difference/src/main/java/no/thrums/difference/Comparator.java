package no.thrums.difference;

import no.thrums.instance.Instance;

import java.util.List;

/**
 * @author Kristian Myrhaug
 * @since 2016-06-02
 */
public interface Comparator {

    List<Difference> compare(Instance first, Instance second);
}
