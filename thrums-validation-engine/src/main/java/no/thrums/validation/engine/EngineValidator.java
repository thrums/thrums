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
package no.thrums.validation.engine;

import no.thrums.validation.Violation;
import no.thrums.validation.keyword.KeywordValidator;
import no.thrums.validation.keyword.KeywordValidatorContext;
import no.thrums.validation.path.PathFactory;
import no.thrums.validation.Validator;
import no.thrums.validation.instance.Instance;
import no.thrums.validation.instance.InstanceFactory;
import no.thrums.validation.keyword.Keyword;
import no.thrums.validation.path.Path;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Kristian Myrhaug
 * @since 2015-02-05
 */
public class EngineValidator implements Validator {

    private final InstanceFactory instanceFactory;
    private final PathFactory pathFactory;
    private final Keyword[] keywords;

    @Inject
    public EngineValidator(InstanceFactory instanceFactory, PathFactory pathFactory, Keyword... keywords) {
        this.instanceFactory = instanceFactory;
        this.pathFactory = pathFactory;
        this.keywords = keywords;
    }

    @Override
    public List<Violation> validate(Instance schema, Instance instance) {
        final List<Violation> violations = new ArrayList<>();
        List<KeywordValidatorContextImpl> queue = new ArrayList<>();
        queue.add(new KeywordValidatorContextImpl(schema, instance, pathFactory.root(), queue, violations));
        for (int index = 0; index < queue.size(); index++) {
            KeywordValidatorContextImpl context = queue.get(index);
            for (Keyword keyword : keywords) {
                KeywordValidator keywordValidator = keyword.getKeywordValidator(context.getSchema());
                if (Objects.nonNull(keywordValidator)) {
                    keywordValidator.vaildate(context, context.getInstance());
                }
            }
        }
        return violations;
    }

    private class KeywordValidatorContextImpl implements KeywordValidatorContext {

        private final Instance schema;
        private final Instance instance;
        private final Path path;
        private final List<KeywordValidatorContextImpl> validations;
        private final List<Violation> violations;

        private KeywordValidatorContextImpl(Instance schema, Instance instance, Path path, List<KeywordValidatorContextImpl> validations, List<Violation> violations) {
            this.schema = schema;
            this.instance = instance;
            this.path = path;
            this.validations = validations;
            this.violations = violations;
        }

        @Override
        public void addViolation(String message) {
            violations.add(new EngineViolation(path, message, instance, schema, instanceFactory));
        }

        @Override
        public void addViolation(String key, String message) {
            violations.add(new EngineViolation(path.push(key), message, instance, schema, instanceFactory));
        }

        @Override
        public void addViolation(Integer index, String message) {
            violations.add(new EngineViolation(path.push(index), message, instance, schema, instanceFactory));
        }

        @Override
        public void validate(Instance schema) {
            validations.add(new KeywordValidatorContextImpl(schema, instance, path, validations, violations));
        }

        @Override
        public void validate(String key, Instance schema) {
            validations.add(new KeywordValidatorContextImpl(schema, instance.get(key), path.push(key), validations, violations));
        }

        @Override
        public void validate(Integer index, Instance schema) {
            validations.add(new KeywordValidatorContextImpl(schema, instance.get(index), path.push(index), validations, violations));
        }

        @Override
        public List<Violation> test(Instance schema) {
            return EngineValidator.this.validate(schema, instance);
        }

        public Instance getInstance() {
            return instance;
        }

        public Instance getSchema() {
            return schema;
        }
    }
}
