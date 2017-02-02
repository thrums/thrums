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

import no.thrums.instance.Instance;
import no.thrums.validation.keyword.KeywordValidator;
import no.thrums.validation.keyword.KeywordValidatorContext;
import no.thrums.validation.keyword.Keyword;

/**
 * @author Kristian Myrhaug
 * @since 2015-02-08
 */
public class Not implements Keyword {

    @Override
    public KeywordValidator getKeywordValidator(Instance schema) {
        Instance keyword = schema.get("not");
        if (keyword.isPresent()) {
            if (keyword.isObject()) {
                return new NotKeywordValidator(keyword);
            } else {
                throw new IllegalArgumentException("not must be object");
            }
        }
        return null;
    }

    private class NotKeywordValidator implements KeywordValidator {

        private final Instance schema;

        private NotKeywordValidator(Instance schema) {
            this.schema = schema;
        }

        @Override
        public void vaildate(KeywordValidatorContext context, Instance instance) {
            if (context.test(schema).isEmpty()) {
                context.addViolation("{no.thrums.validation.engine.keyword.any.Not.message}");
            }
        }
    }
 }
