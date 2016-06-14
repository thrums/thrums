/**
 Copyright 2014-2016 Kristian Myrhaug

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

/**
 * @author Kristian Myrhaug
 * @since 2015-02-15
 */
public class Ipv6 implements Keyword {

    private static final String ALPHABET = "0123456789abcdefABCDEF";

    @Override
    public KeywordValidator getKeywordValidator(Instance schema) {
        if ("ipv6".equals(schema.get("format").asString())) {
            return new Ipv6KeywordValidator();
        }
        return null;
    }

    private class Ipv6KeywordValidator implements KeywordValidator {

        @Override
        public void vaildate(KeywordValidatorContext context, Instance instance) {
            if (instance.isPresent()) {
                if (instance.isString()) {
                    String ipv6 = instance.asString();
                    String[] groups = ipv6.split(":");
                    if (groups.length != 8) {
                        context.addViolation("{no.thrums.validation.engine.keyword.string.format.Ipv6.message}");
                    } else {
                        for (int groupIndex = 0; groupIndex < groups.length; groupIndex++) {
                            String group = groups[groupIndex];
                            if (group.length() > 4) {
                                context.addViolation("{no.thrums.validation.engine.keyword.string.format.Ipv6.message}");
                            }
                            for (int characterIndex = 0; characterIndex < group.length(); characterIndex++) {
                                if (ALPHABET.indexOf(group.charAt(characterIndex)) < 0) {
                                    context.addViolation("{no.thrums.validation.engine.keyword.string.format.Ipv6.message}");
                                    return;
                                }
                            }
                        }
                    }
                } else {
                    context.addViolation("{no.thrums.validation.engine.keyword.string.format.Ipv6.type.message}");
                }
            }
        }
    }
}
