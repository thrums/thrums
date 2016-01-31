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
package no.thrums.validation.engine.keyword.any;

import no.thrums.validation.instance.Instance;
import no.thrums.validation.keyword.KeywordValidator;
import no.thrums.validation.keyword.KeywordValidatorContext;
import no.thrums.validation.keyword.Keyword;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Kristian Myrhaug
 * @since 2015-02-07
 */
public class Type implements Keyword {

    @Override
    public KeywordValidator getKeywordValidator(Instance schema) {
        Instance keyword = schema.get("type");
        if (keyword.isPresent()) {
            if (keyword.isString()) {
                return new TypeKeywordValidator(keyword.asString());
            } else if (keyword.isArray()) {
                List<Instance> values = keyword.items();
                Set<String> types = new HashSet<>();
                for (Instance value : values) {
                    if (value.isString()) {
                        if (types.contains(value.asString())) {
                            throw new IllegalArgumentException("Items must be unique");
                        }
                        types.add(value.asString());
                    } else {
                        throw new IllegalArgumentException("Item must be string");
                    }
                }
                return new TypeKeywordValidator(types);
            } else {
                throw new IllegalArgumentException("Keyword must be array or string");
            }
        }
        return null;
    }

    private class TypeKeywordValidator implements KeywordValidator {

        private final Set<String> types;

        private TypeKeywordValidator(String type) {
            types = new HashSet<>();
            types.add(type);
        }

        private TypeKeywordValidator(Set<String> types) {
            this.types = types;
        }

        @Override
        public void vaildate(KeywordValidatorContext context, Instance instance) {
            if (instance.isArray()) {
                if (!types.contains("array")) {
                    context.addViolation("{no.thrums.validation.engine.keyword.any.Type.array.message}");
                }
            } else if (instance.isBoolean()) {
                if (!types.contains("boolean")) {
                    context.addViolation("{no.thrums.validation.engine.keyword.any.Type.boolean.message}");
                }
            } else if (instance.isNull()) {
                if (!types.contains("null")) {
                    context.addViolation("{no.thrums.validation.engine.keyword.any.Type.null.message}");
                }
            } else if (instance.isNumber()) {
                if (instance.isIntegral()) {
                    if (!(types.contains("number") || types.contains("integer"))) {
                        context.addViolation("{no.thrums.validation.engine.keyword.any.Type.integer.message}");
                    }
                } else if (!types.contains("number")) {
                    context.addViolation("{no.thrums.validation.engine.keyword.any.Type.number.message}");
                }
            } else if (instance.isObject()) {
                if (!types.contains("object")) {
                    context.addViolation("{no.thrums.validation.engine.keyword.any.Type.object.message}");
                }
            } else if (instance.isString()) {
                if (!types.contains("string")) {
                    context.addViolation("{no.thrums.validation.engine.keyword.any.Type.string.message}");
                }
            }
        }
    }
}
