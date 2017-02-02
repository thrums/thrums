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

import no.thrums.instance.Instance;
import no.thrums.instance.ri.helper.NumberComparator;
import no.thrums.validation.keyword.Keyword;
import no.thrums.validation.keyword.KeywordValidator;
import no.thrums.validation.keyword.KeywordValidatorContext;

import static no.thrums.instance.ri.helper.NumberUtilities.remainder;

/**
* @author Kristian Myrhaug
* @since 2014-10-15
*/
public class MultipleOf implements Keyword {

    private static final NumberComparator NUMBER_COMPARATOR = new NumberComparator();

    @Override
    public KeywordValidator getKeywordValidator(Instance schema) {
        Instance keyword = schema.get("multipleOf");
        if (keyword.isPresent()) {
            if (keyword.isNumber()) {
                return new MultipleOfKeywordValidator(keyword.asNumber());
            } else {
                throw new IllegalArgumentException("Keyword must be number");
            }
        }
        return null;
    }

    private class MultipleOfKeywordValidator implements KeywordValidator {

        private final Number multipleOf;

        private MultipleOfKeywordValidator(Number multipleOf) {
            this.multipleOf = multipleOf;
        }

        @Override
        public void vaildate(KeywordValidatorContext context, Instance instance) {
            if (instance.isPresent()) {
                if (instance.isNumber()) {
                    if (NUMBER_COMPARATOR.compare(0, remainder(instance.asNumber(), multipleOf)) != 0) {
                        context.addViolation("{no.thrums.validation.engine.keyword.number.MultipleOf.message}");
                    }
                } else {
                    context.addViolation("{no.thrums.validation.engine.keyword.number.MultipleOf.message}");
                }
            }
        }
    }
}
