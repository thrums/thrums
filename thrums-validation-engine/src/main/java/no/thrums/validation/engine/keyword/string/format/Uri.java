/**
 Copyright 2014-2017 Kristian Myrhaug

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */
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
