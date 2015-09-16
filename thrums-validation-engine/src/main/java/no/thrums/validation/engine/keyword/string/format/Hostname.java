/**
 Copyright 2014-2015 Kristian Myrhaug

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

import no.thrums.validation.instance.Instance;
import no.thrums.validation.keyword.Keyword;
import no.thrums.validation.keyword.KeywordValidator;
import no.thrums.validation.keyword.KeywordValidatorContext;

/**
 * @author Kristian Myrhaug
 * @since 2015-02-11
 */
public class Hostname implements Keyword {

    @Override
    public KeywordValidator getKeywordValidator(Instance schema) {
        Instance keyword = schema.get("format");
        if ("hostname".equals(keyword.asString())) {
            return new HostNameKeywordValidator();
        }
        return null;
    }

    private class HostNameKeywordValidator implements KeywordValidator {

        @Override
        public void vaildate(KeywordValidatorContext context, Instance instance) {
            if (instance.isPresent()) {
                if (instance.isString()) {
                    String string = instance.asString();
                    if (string.length() > 253) {
                        context.addViolation("{no.thrums.validation.engine.keyword.string.format.Hostname.message}");
                    }
                    String[] domains = string.split("\\.");
                    for (String domain : domains) {
                        if (domain.length() < 1) {
                            context.addViolation("{no.thrums.validation.engine.keyword.string.format.Hostname.message}");
                        }
                        if (domain.length() > 63) {
                            context.addViolation("{no.thrums.validation.engine.keyword.string.format.Hostname.message}");
                        }
                        if ('-' == domain.charAt(0)) {
                            context.addViolation("{no.thrums.validation.engine.keyword.string.format.Hostname.message}");
                        }
                        if ('-' == domain.charAt(domain.length() - 1)) {
                            context.addViolation("{no.thrums.validation.engine.keyword.string.format.Hostname.message}");
                        }
                        for (int index = 0; index < domain.length(); index++) {
                            if (!(Character.isLetterOrDigit(domain.charAt(index)) || '-' == domain.charAt(index))) {
                                context.addViolation("{no.thrums.validation.engine.keyword.string.format.Hostname.message}");
                            }
                        }
                    }
                } else {
                    context.addViolation("{no.thrums.validation.engine.keyword.string.format.Hostname.type.message}");
                }
            }
        }
    }
}
