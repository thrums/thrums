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

import no.thrums.instance.ri.helper.RegExp;
import no.thrums.instance.Instance;
import no.thrums.validation.keyword.Keyword;
import no.thrums.validation.keyword.KeywordValidator;
import no.thrums.validation.keyword.KeywordValidatorContext;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

/**
 * @author Kristian Myrhaug
 * @since 2014-10-30
 */
public class AdditionalProperties implements Keyword {

    private static final String KEYWORD_ADDITIONAL_PROPERTIES = "additionalProperties";
    private static final String KEYWORD_PROPERTIES = "properties";
    private static final String KEYWORD_PATTERN_PROPERTIES = "patternProperties";

    @Override
    public KeywordValidator getKeywordValidator(Instance schema) {
        Instance keyword = schema.get(KEYWORD_ADDITIONAL_PROPERTIES);
        Instance additionalProperties = null;
        if (keyword.isPresent()) {
            if (keyword.isBoolean()) {
                if (keyword.asBoolean()) {
                    additionalProperties = schema.getInstanceFactory().defined(schema, new LinkedHashMap<>());
                } else {
                    additionalProperties = null;
                }
            } else if (keyword.isObject()) {
                additionalProperties = keyword;
            } else {
                throw new IllegalArgumentException("additionalProperties must be boolean or object");
            }
        } else {
            additionalProperties = schema.getInstanceFactory().defined(schema, new LinkedHashMap<>());
        }
        keyword = schema.get(KEYWORD_PROPERTIES);
        Map<String,Instance> properties = null;
        if (keyword.isPresent()) {
            properties = new TreeMap<>();
            if (keyword.isObject()) {
                for (Map.Entry<String,Instance> element : keyword.properties().entrySet()) {
                    Instance value = element.getValue();
                    if (value.isObject()) {
                        properties.put(element.getKey(), value);
                    } else {
                        throw new IllegalArgumentException("properties element value must be object");
                    }
                }
            } else {
                throw new IllegalArgumentException("properties must be object");
            }
        }
        keyword = schema.get(KEYWORD_PATTERN_PROPERTIES);
        Map<String,Instance> patternProperties = null;
        if (keyword.isPresent()) {
            patternProperties = new TreeMap<>();
            if (keyword.isObject()) {
                for (Map.Entry<String,Instance> element : keyword.properties().entrySet()) {
                    Instance value = element.getValue();
                    if (value.isObject()) {
                        if (!RegExp.isValidRegExp(element.getKey())) {
                            throw new IllegalArgumentException("patternProperties key must be regex");
                        }
                        patternProperties.put(element.getKey(), value);
                    } else {
                        throw new IllegalArgumentException("patternProperties element value must be object");
                    }
                }
            } else {
                throw new IllegalArgumentException("patternProperties must be object");
            }
        }
        return new AdditionalPropertiesKeywordValidator(additionalProperties, properties, patternProperties);
    }

    private class AdditionalPropertiesKeywordValidator implements KeywordValidator {

        private final Instance additionalProperties;
        private final Map<String,Instance> properties;
        private final Map<String,Instance> patternPropperties;

        private AdditionalPropertiesKeywordValidator(Instance additionalProperties, Map<String, Instance> properties, Map<String, Instance> patternPropperties) {
            this.additionalProperties = additionalProperties;
            this.properties = properties;
            this.patternPropperties = patternPropperties;
        }

        @Override
        public void vaildate(KeywordValidatorContext context, Instance instance) {
            if (instance.isPresent()) {
                if (instance.isObject()) {
                    Map<String,Instance> object = instance.properties();
                    for (Map.Entry<String,Instance> element : object.entrySet()) {
                        if (Objects.nonNull(properties)) {
                            if (properties.containsKey(element.getKey())) {
                                context.validate(element.getKey(), properties.get(element.getKey()));
                                continue;
                            }
                        }
                        boolean match = false;
                        if (Objects.nonNull(patternPropperties)) {
                            for (String pattern : patternPropperties.keySet()) {
                                if (RegExp.test(pattern, element.getKey())) {
                                    context.validate(element.getKey(), patternPropperties.get(pattern));
                                    match = true;
                                    break;
                                }
                            }
                        }
                        if (match) continue;
                        if (Objects.nonNull(additionalProperties)) {
                            context.validate(element.getKey(), additionalProperties);
                            continue;
                        }
                        context.addViolation(element.getKey(), "{no.thrums.validation.engine.keyword.object.AdditionalProperties.message}");
                    }
                } else if (!isAdditionalProperties() || !isPropertyEmpty() || !isPatternPropertyEmpty()) {
                    context.addViolation("{no.thrums.validation.engine.keyword.object.AdditionalProperties.type.message}");
                }
            }
        }

        private boolean isAdditionalProperties() {
            return Objects.nonNull(additionalProperties);
        }

        private boolean isPropertyEmpty() {
            return Objects.isNull(properties) || properties.isEmpty();
        }

        private boolean isPatternPropertyEmpty() {
            return Objects.isNull(patternPropperties) || patternPropperties.isEmpty();
        }
    }
}
