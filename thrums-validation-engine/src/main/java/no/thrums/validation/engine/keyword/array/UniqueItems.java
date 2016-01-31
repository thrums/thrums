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
package no.thrums.validation.engine.keyword.array;

import no.thrums.validation.keyword.KeywordValidatorContext;
import no.thrums.validation.instance.Instance;
import no.thrums.validation.keyword.Keyword;
import no.thrums.validation.keyword.KeywordValidator;

import java.util.HashSet;
import java.util.List;

/**
 * @author Kristian Myrhaug
 * @since 2014-10-30
 */
public class UniqueItems implements Keyword {

    @Override
    public KeywordValidator getKeywordValidator(Instance schema) {
        Instance keyword = schema.get("uniqueItems");
        if (keyword.isPresent()) {
            if (keyword.isBoolean()) {
                if (keyword.asBoolean()) {
                    return new UniqueItemsKeywordValidator();
                } else {
                    return null;
                }
            }
            throw new IllegalArgumentException("Keyword must be boolean");
        }
        return null;
    }

    private class UniqueItemsKeywordValidator implements KeywordValidator {

        @Override
        public void vaildate(KeywordValidatorContext context, Instance instance) {
            if (instance.isPresent()) {
                if (instance.isArray()) {
                    List<Instance> array = instance.items();
                    if (new HashSet<>(array).size() != array.size()) {
                        context.addViolation("{no.thrums.validation.engine.keyword.array.UniqueItems.message}");
                    }
                } else {
                    context.addViolation("{no.thrums.validation.engine.keyword.array.UniqueItems.type.message}");
                }
            }
        }
    }
}
