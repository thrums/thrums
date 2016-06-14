package no.thrums.difference;

import no.thrums.instance.Instance;
import no.thrums.instance.path.Path;

/**
 * @author Kristian Myrhaug
 * @since 2016-06-02
 */
public interface Difference {

    Path getPath();
    Instance getFirst();
    Instance getSecond();

}
