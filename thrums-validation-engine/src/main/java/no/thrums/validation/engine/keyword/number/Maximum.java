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
package no.thrums.validation.engine.keyword.number;

import no.thrums.validation.engine.helper.number.NumberComparator;
import no.thrums.validation.keyword.KeywordValidatorContext;
import no.thrums.validation.instance.Instance;
import no.thrums.validation.keyword.Keyword;
import no.thrums.validation.keyword.KeywordValidator;

/**
 * @author Kristian Myrhaug
 * @since 2014-10-16
 */
public class Maximum implements Keyword {

    private static final NumberComparator NUMBER_COMPARATOR = new NumberComparator();

    private static final int EXCLUSIVE_MAXIMUM = 0;
    private static final int INCLUSIVE_MAXIMUM = 1;

    @Override
    public KeywordValidator getKeywordValidator(Instance schema) {
        Instance keyword = schema.get("maximum");
        if (keyword.isPresent()) {
            if (keyword.isNumber()) {
                Number maximum = keyword.asNumber();
                keyword = schema.get("exclusiveMaximum");
                if (keyword.isPresent()) {
                    if (keyword.isBoolean()) {
                        return new MaximumKeywordValidator(maximum, keyword.asBoolean() ? EXCLUSIVE_MAXIMUM : INCLUSIVE_MAXIMUM);
                    } else {
                        throw new IllegalArgumentException("exclusiveMaximum must be boolean");
                    }
                } else {
                    return new MaximumKeywordValidator(maximum, INCLUSIVE_MAXIMUM);
                }
            } else {
                throw new IllegalArgumentException("Keyword must be number");
            }
        }
        return null;
    }

    private class MaximumKeywordValidator implements KeywordValidator {

        private final Number maximum;
        private final int inclusiveMaximum;


        private MaximumKeywordValidator(Number maximum, int inclusiveMaximum) {
            this.maximum = maximum;
            this.inclusiveMaximum = inclusiveMaximum;
        }

        @Override
        public void vaildate(KeywordValidatorContext context, Instance instance) {
            if (instance.isPresent()) {
                if (instance.isNumber()) {
                    if (NUMBER_COMPARATOR.compare(instance.asNumber(), maximum) >= inclusiveMaximum) {
                        context.addViolation("{no.thrums.validation.engine.keyword.number.Maximum.message}");
                    }
                } else {
                    context.addViolation("{no.thrums.validation.engine.keyword.number.Maximum.type.message}");
                }
            }
        }
    }
}
