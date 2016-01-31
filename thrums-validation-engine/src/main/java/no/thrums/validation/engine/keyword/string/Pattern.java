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
package no.thrums.validation.engine.keyword.string;

import no.thrums.validation.engine.helper.RegExp;
import no.thrums.validation.keyword.KeywordValidatorContext;
import no.thrums.validation.instance.Instance;
import no.thrums.validation.keyword.Keyword;
import no.thrums.validation.keyword.KeywordValidator;

/**
 * @author Kristian Myrhaug
 * @since 2014-10-18
 */
public class Pattern implements Keyword {

    @Override
    public KeywordValidator getKeywordValidator(Instance schema) {
        Instance keyword = schema.get("pattern");
        if (!keyword.isNull() && ! keyword.isUndefined()) {
            if (keyword.isString()) {
                if (!RegExp.isValidRegExp(keyword.asString())) {
                    throw new IllegalArgumentException("Invalid pattern");
                }
                return new PatternKeywordValidator(keyword.asString());
            } else {
                throw new IllegalArgumentException("Keyword must be string");
            }
        }
        return null;
    }

    private class PatternKeywordValidator implements KeywordValidator {

        private final String pattern;

        private PatternKeywordValidator(String pattern) {
            this.pattern = pattern;
        }

        @Override
        public void vaildate(KeywordValidatorContext context, Instance instance) {
            if (instance.isPresent()) {
                if (instance.isString()) {
                    if (!RegExp.test(pattern, instance.asString())) {
                        context.addViolation("{no.thrums.validation.engine.keyword.string.Pattern.message}");
                    }
                } else {
                    context.addViolation("{no.thrums.validation.engine.keyword.string.Pattern.type.message}");
                }
            }
        }
    }
}
