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
package no.thrums.validation.engine.keyword.object;

import no.thrums.validation.keyword.KeywordValidatorContext;
import no.thrums.validation.instance.Instance;
import no.thrums.validation.keyword.Keyword;
import no.thrums.validation.keyword.KeywordValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Kristian Myrhaug
 * @since 2014-10-19
 */
public class Required implements Keyword {

    @Override
    public KeywordValidator getKeywordValidator(Instance schema) {
        Instance keyword = schema.get("required");
        if (keyword.isPresent()) {
            if (keyword.isArray()) {
                List<String> required = new ArrayList<>();
                for (Instance item : keyword.items()) {
                    if (item.isString()) {
                        required.add(item.asString());
                    } else {
                        throw new IllegalArgumentException("Item must be array");
                    }
                }
                return new RequiredKeywordValidator(required);
            } else {
                throw new IllegalArgumentException("Keyword must be array");
            }
        }
        return null;
    }

    private class RequiredKeywordValidator implements KeywordValidator {

        private final List<String> required;

        private RequiredKeywordValidator(List<String> required) {
            this.required = required;
        }

        @Override
        public void vaildate(KeywordValidatorContext context, Instance instance) {
            if (instance.isPresent()) {
                if (instance.isObject()) {
                    Map<String,Instance> object = instance.properties();
                    for (String key : required) {
                        if (!object.containsKey(key)) {
                            context.addViolation(key, "{no.thrums.validation.engine.keyword.object.Required.message}");
                        }
                    }
                } else {
                    context.addViolation("{no.thrums.validation.engine.keyword.object.Dependencies.type.message}");
                }
            }
        }
    }
}
