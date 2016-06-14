package no.thrums.validation.engine.keyword.string.format;

import no.thrums.instance.Instance;
import no.thrums.validation.keyword.KeywordValidator;
import no.thrums.validation.keyword.KeywordValidatorContext;
import no.thrums.validation.keyword.Keyword;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author Kristian Myrhaug
 * @since 2015-02-15
 */
public class Uri implements Keyword {

    @Override
    public KeywordValidator getKeywordValidator(Instance schema) {
        if ("uri".equals(schema.get("format").asString())) {
            return new UriKeywordValidator();
        }
        return null;
    }

    private class UriKeywordValidator implements KeywordValidator {

        @Override
        public void vaildate(KeywordValidatorContext context, Instance instance) {
            if (instance.isPresent()) {
                if (instance.isString()) {
                    try {
                        new URI(instance.asString());
                    } catch (URISyntaxException cause) {
                        context.addViolation("{no.thrums.validation.engine.keyword.string.format.Uri.message}");
                    }
                } else {
                    context.addViolation("{no.thrums.validation.engine.keyword.string.format.Uri.type.message}");
                }
            }
        }
    }
}
