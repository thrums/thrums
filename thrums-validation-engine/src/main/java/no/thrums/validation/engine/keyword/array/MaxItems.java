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
package no.thrums.validation.engine.keyword.array;

import no.thrums.instance.ri.helper.NumberComparator;
import no.thrums.instance.Instance;
import no.thrums.validation.keyword.KeywordValidator;
import no.thrums.validation.keyword.KeywordValidatorContext;
import no.thrums.validation.keyword.Keyword;

/**
 * @author Kristian Myrhaug
 * @since 2014-10-18
 */
public class MaxItems implements Keyword {

    private static final NumberComparator NUMBER_COMPARATOR = new NumberComparator();

    @Override
    public KeywordValidator getKeywordValidator(Instance schema) {
        Instance keyword = schema.get("maxItems");
        if (keyword.isPresent()) {
            if (keyword.isNumber()) {
                return new MaxItemsKeywordValidator(keyword.asNumber());
            }
            throw new IllegalArgumentException("maxItems must be number");
        }
        return null;
    }

    private class MaxItemsKeywordValidator implements KeywordValidator {

        private final Number max;

        private MaxItemsKeywordValidator(Number max) {
            this.max = max;
        }

        @Override
        public void vaildate(KeywordValidatorContext context, Instance instance) {
            if (instance.isPresent()) {
                if (instance.isArray()) {
                    if (NUMBER_COMPARATOR.compare(instance.items().size(), max) > 0) {
                        context.addViolation("{no.thrums.validation.engine.keyword.array.MaxItems.message}");
                    }
                } else {
                    context.addViolation("{no.thrums.validation.engine.keyword.array.MaxItems.type.message}");
                }
            }
        }
    }
}
