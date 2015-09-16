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
package no.thrums.validation.engine.keyword.any;

import no.thrums.validation.instance.Instance;
import no.thrums.validation.keyword.Keyword;
import no.thrums.validation.keyword.KeywordValidator;
import no.thrums.validation.keyword.KeywordValidatorContext;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Kristian Myrhaug
 * @since 2015-02-08
 */
public class AnyOf implements Keyword {

    @Override
    public KeywordValidator getKeywordValidator(Instance schema) {
        Instance keyword = schema.get("anyOf");
        if (keyword.isPresent()) {
            if (keyword.isArray()) {
                List<Instance> anyOf = keyword.items();
                if (anyOf.isEmpty()) {
                    throw new IllegalArgumentException("anyOf must have at least one element");
                }
                List<Instance> schemas = new ArrayList<>();
                for (Instance element : anyOf) {
                    if (element.isObject()) {
                        schemas.add(element);
                    } else {
                        throw new IllegalArgumentException("anyOf element must be object");
                    }
                }
                return new AnyOfKeywordValidator(schemas);
            } else {
                throw new IllegalArgumentException("anyOf must be array");
            }
        }
        return null;
    }

    private class AnyOfKeywordValidator implements KeywordValidator {

        private final List<Instance> schemas;

        private AnyOfKeywordValidator(List<Instance> schemas) {
            this.schemas = schemas;
        }

        @Override
        public void vaildate(KeywordValidatorContext context, Instance instance) {
            for (Instance schema : schemas) {
                if (context.test(schema).isEmpty()) {
                    return;
                }
            }
            context.addViolation("{no.thrums.validation.engine.keyword.any.AnyOf.message}");
        }
    }
}
