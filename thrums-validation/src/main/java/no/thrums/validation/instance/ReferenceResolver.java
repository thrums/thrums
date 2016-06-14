package no.thrums.validation.instance;

import no.thrums.instance.Instance;

/**
 * @author Kristian Myrhaug
 * @since 2016-05-07
 */
public interface ReferenceResolver {

    Instance resolve(Instance reference);
}
