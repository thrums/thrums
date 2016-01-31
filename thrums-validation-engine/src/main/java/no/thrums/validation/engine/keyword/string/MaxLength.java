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

import no.thrums.validation.engine.helper.number.NumberComparator;
import no.thrums.validation.instance.Instance;
import no.thrums.validation.keyword.Keyword;
import no.thrums.validation.keyword.KeywordValidator;
import no.thrums.validation.keyword.KeywordValidatorContext;

/**
 * @author Kristian Myrhaug
 * @since 2014-10-17
 */
public class MaxLength implements Keyword {

    private static final NumberComparator NUMBER_COMPARATOR = new NumberComparator();

    @Override
    public KeywordValidator getKeywordValidator(Instance schema) {
        Instance keyword = schema.get("maxLength");
        if (keyword.isPresent()) {
            if (keyword.isNumber()) {
                return new MaxLengthKeywordValidator(keyword.asNumber());
            } else {
                throw new IllegalArgumentException("Keyword must be number");
            }
        }
        return null;
    }

    private class MaxLengthKeywordValidator implements KeywordValidator {

        private final Number maxLength;

        private MaxLengthKeywordValidator(Number maxLength) {
            this.maxLength = maxLength;
        }

        @Override
        public void vaildate(KeywordValidatorContext context, Instance instance) {
            if (instance.isPresent()) {
                if (instance.isString()) {
                    if (NUMBER_COMPARATOR.compare((instance.asString()).length(), maxLength) > 0) {
                        context.addViolation("{no.thrums.validation.engine.keyword.string.MaxLength.message}");
                    }
                } else {
                    context.addViolation("{no.thrums.validation.engine.keyword.string.MaxLength.type.message}");
                }
            }
        }
    }
}
