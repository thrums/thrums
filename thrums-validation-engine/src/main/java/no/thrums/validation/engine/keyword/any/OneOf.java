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

import no.thrums.instance.Instance;
import no.thrums.validation.keyword.Keyword;
import no.thrums.validation.keyword.KeywordValidator;
import no.thrums.validation.keyword.KeywordValidatorContext;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Kristian Myrhaug
 * @since 2015-02-08
 */
public class OneOf implements Keyword {

    @Override
    public KeywordValidator getKeywordValidator(Instance schema) {
        Instance keyword = schema.get("oneOf");
        if (keyword.isPresent()) {
            if (keyword.isArray()) {
                List<Instance> oneOf = keyword.items();
                if (oneOf.isEmpty()) {
                    throw new IllegalArgumentException("oneOf must have at least one element");
                }
                List<Instance> schemas = new ArrayList<>();
                for (Instance element : oneOf) {
                    if (element.isObject()) {
                        schemas.add(element);
                    } else {
                        throw new IllegalArgumentException("oneOf element must be object");
                    }
                }
                return new OneOfKeywordValidator(schemas);
            } else {
                throw new IllegalArgumentException("oneOf must be array");
            }
        }
        return null;
    }

    private class OneOfKeywordValidator implements KeywordValidator {

        private final List<Instance> schemas;

        private OneOfKeywordValidator(List<Instance> schemas) {
            this.schemas = schemas;
        }

        @Override
        public void vaildate(KeywordValidatorContext context, Instance instance) {
            int matches = 0;
            for (Instance schema : schemas) {
                if (context.test(schema).isEmpty()) {
                    matches++;
                }
            }
            if (matches < 1) {
                context.addViolation("{no.thrums.validation.engine.keyword.any.OneOf.noMatch.message}");
            } else if (matches > 1) {
                context.addViolation("{no.thrums.validation.engine.keyword.any.OneOf.severalMatches.message}");
            }
        }
    }
}
