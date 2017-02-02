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
package no.thrums.validation.engine.keyword.object;

import no.thrums.instance.Instance;
import no.thrums.validation.keyword.KeywordValidator;
import no.thrums.validation.keyword.KeywordValidatorContext;
import no.thrums.validation.keyword.Keyword;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author Kristian Myrhaug
 * @since 2015-02-05
 */
public class Dependencies implements Keyword {

    private static final String KEYWORD = "dependencies";

    @Override
    public KeywordValidator getKeywordValidator(Instance schema) {
        Instance keyword = schema.get(KEYWORD);
        if (keyword.isPresent()) {
            if (keyword.isObject()) {
                Map<String,Set<String>> propertyDependencies = new HashMap<>();
                Map<String,Instance> schemaDepandencies = new HashMap<>();
                for (Map.Entry<String,Instance> dependency : keyword.properties().entrySet()) {
                    Instance value = dependency.getValue();
                    if (value.isString()) {
                        Set<String> propertyset = new TreeSet<>();
                        propertyset.add(value.asString());
                        propertyDependencies.put(dependency.getKey(), propertyset);
                    } else if (value.isArray()) {
                        List<Instance> keys = value.items();
                        Set<String> propertyset = new TreeSet<>();
                        for (Instance key : keys) {
                            if (key.isString()) {
                                propertyset.add(key.asString());
                            } else {
                                throw new  IllegalArgumentException("property dependencies must be string");
                            }
                        }
                        propertyDependencies.put(dependency.getKey(), propertyset);
                    } else if (value.isObject()) {
                        schemaDepandencies.put(dependency.getKey(), value);
                    } else {
                        throw new IllegalArgumentException("Property must be array, object or string");
                    }
                }
                return new DependenciesKeywordValidator(propertyDependencies, schemaDepandencies);
            } else {
                throw new IllegalArgumentException("Keyword must be object");
            }
        }
        return null;
    }

    private class DependenciesKeywordValidator implements KeywordValidator {

        private final Map<String,Set<String>> propertyDependencies;
        private final Map<String,Instance> schemaDependencies;

        private DependenciesKeywordValidator(Map<String, Set<String>> propertyDependencies, Map<String, Instance> schemaDependencies) {
            this.propertyDependencies = propertyDependencies;
            this.schemaDependencies = schemaDependencies;
        }

        @Override
        public void vaildate(KeywordValidatorContext context, Instance instance) {
            if (instance.isObject()) {
                Map<String, Instance> object = instance.properties();
                Set<String> keys = object.keySet();
                for (String key : keys) {
                    if (propertyDependencies.containsKey(key)) {
                        for (String required : propertyDependencies.get(key)) {
                            if (!keys.contains(required)) {
                                context.addViolation("{no.thrums.validation.engine.keyword.object.Dependencies.propertyDependency.message}");
                            }
                        }
                    }
                    if (schemaDependencies.containsKey(key)) {
                        context.validate(key, schemaDependencies.get(key));
                    }
                }
            }
        }
    }
}
