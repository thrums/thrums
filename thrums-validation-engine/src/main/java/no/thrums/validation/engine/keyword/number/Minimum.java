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
package no.thrums.validation.engine.keyword.number;

import no.thrums.instance.ri.helper.NumberComparator;
import no.thrums.validation.keyword.KeywordValidatorContext;
import no.thrums.instance.Instance;
import no.thrums.validation.keyword.Keyword;
import no.thrums.validation.keyword.KeywordValidator;

/**
 * @author Kristian Myrhaug
 * @since 2014-10-16
 */
public class Minimum implements Keyword {

    private static final NumberComparator NUMBER_COMPARATOR = new NumberComparator();

    private static final int EXCLUSIVE_MINIMUM = 0;
    private static final int INCLUSIVE_MINIMUM = 1;

    @Override
    public KeywordValidator getKeywordValidator(Instance schema) {
        Instance keyword = schema.get("minimum");
        if (keyword.isPresent()) {
            if (keyword.isNumber()) {
                Number minimum = keyword.asNumber();
                keyword = schema.get("exclusiveMinimum");
                if (keyword.isPresent()) {
                    if (keyword.isBoolean()) {
                        return new MinimumKeywordValidator(minimum, keyword.asBoolean() ? EXCLUSIVE_MINIMUM : INCLUSIVE_MINIMUM);
                    } else {
                        throw new IllegalArgumentException("exclusiveMinimum must be boolean");
                    }
                } else {
                    return new MinimumKeywordValidator(minimum, INCLUSIVE_MINIMUM);
                }
            } else {
                throw new IllegalArgumentException("Keyword must be number");
            }
        }
        return null;
    }

    private class MinimumKeywordValidator implements KeywordValidator {

        private final Number minimum;
        private final int inclusiveMinimum;


        private MinimumKeywordValidator(Number minimum, int inclusiveMinimum) {
            this.minimum = minimum;
            this.inclusiveMinimum = inclusiveMinimum;
        }

        @Override
        public void vaildate(KeywordValidatorContext context, Instance instance) {
            if (instance.isPresent()) {
                if (instance.isNumber()) {
                    if (NUMBER_COMPARATOR.compare(minimum, instance.asNumber()) >= inclusiveMinimum) {
                        context.addViolation("{no.thrums.validation.engine.keyword.number.Minimum.message}");
                    }
                } else {
                    context.addViolation("{no.thrums.validation.engine.keyword.number.Minimum.type.message}");
                }
            }
        }
    }
}
