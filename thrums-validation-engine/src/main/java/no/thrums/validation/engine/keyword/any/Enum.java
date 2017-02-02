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
package no.thrums.validation.engine.keyword.any;

import no.thrums.validation.keyword.KeywordValidatorContext;
import no.thrums.instance.Instance;
import no.thrums.validation.keyword.Keyword;
import no.thrums.validation.keyword.KeywordValidator;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Kristian Myrhaug
 * @since 2015-02-05
 */
public class Enum implements Keyword {

    private static final String KEYWORD = "enum";

    @Override
    public KeywordValidator getKeywordValidator(Instance schema) {
        Instance keyword = schema.get(KEYWORD);
        if (keyword.isPresent()) {
            if (keyword.isArray()) {
                List<Instance> enumValues = keyword.items();
                if (enumValues.isEmpty()) {
                    return null;
                } else {
                    return new EnumKeywordValidator(enumValues);
                }
            } else {
                throw new IllegalArgumentException("Keyword must be array");
            }
        }
        return null;
    }

    private class EnumKeywordValidator implements KeywordValidator {

        private final Set<Instance> enumValues;

        private EnumKeywordValidator(Collection<Instance> enumValues) {
            this.enumValues = new HashSet<>(enumValues);
        }

        @Override
        public void vaildate(KeywordValidatorContext context, Instance instance) {
            if (!enumValues.contains(instance)) {
                context.addViolation("{no.thrums.validation.engine.keyword.any.Enum.message}");
            }
        }
    }
}
