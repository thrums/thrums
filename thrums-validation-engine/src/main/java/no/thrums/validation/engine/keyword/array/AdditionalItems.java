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
package no.thrums.validation.engine.keyword.array;

import no.thrums.instance.Instance;
import no.thrums.validation.keyword.KeywordValidator;
import no.thrums.validation.keyword.KeywordValidatorContext;
import no.thrums.validation.keyword.Keyword;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

/**
 * @author Kristian Myrhaug
 * @since 2014-10-29
 */
public class AdditionalItems implements Keyword {

    public AdditionalItems() {
    }

    @Override
    public KeywordValidator getKeywordValidator(Instance schema) {
        Instance keyword = schema.get("items");
        List<Instance> items = null;
        if (keyword.isPresent()) {
            items = new ArrayList<>();
            if (keyword.isObject()) {
                items.add(keyword);
            } else if (keyword.isArray()) {
                for (Instance item : keyword.items()) {
                    if (item.isObject()) {
                        items.add(item);
                    } else {
                        throw new IllegalArgumentException("items element must be object");
                    }
                }
            } else {
                throw new IllegalArgumentException("items must be object or array");
            }
        }
        keyword = schema.get("additionalItems");
        Instance additionalItems = null;
        if (keyword.isPresent()) {
            if (keyword.isBoolean()) {
                if (keyword.asBoolean()) {
                    additionalItems = schema.getInstanceFactory().defined(schema, new LinkedHashMap<>());
                } else {
                    additionalItems = null;
                }
            } else if (keyword.isObject()) {
                additionalItems = keyword;
            } else {
                throw new IllegalArgumentException("additionalItems must be boolean or object");
            }
        } else {
            additionalItems = schema.getInstanceFactory().defined(schema, new LinkedHashMap<>());
        }
        return new AdditionalItemsKeywordValidator(additionalItems, items);
    }

    private class AdditionalItemsKeywordValidator implements KeywordValidator {

        private final Instance additionalItems;
        private final List<Instance> items;

        private AdditionalItemsKeywordValidator(Instance additionalItems, List<Instance> items) {
            this.additionalItems = additionalItems;
            this.items = items;
        }

        @Override
        public void vaildate(KeywordValidatorContext context, Instance instance) {
            if (instance.isPresent()) {
                if (instance.isArray()) {
                    List<Instance> array = instance.items();
                    for (int index = 0; index < array.size(); index++){
                        Instance schema = getItem(index);
                        if (Objects.nonNull(schema)) {
                            context.validate(index, schema);
                            continue;
                        }
                        context.addViolation(index, "{no.thrums.validation.engine.keyword.array.AdditionalItems.message}");
                    }
                } else if (!isAdditionalItems() || !isItems()) {
                    context.addViolation("{no.thrums.validation.engine.keyword.array.AdditionalItems.type.message}");
                }
            }
        }

        private boolean isAdditionalItems() {
            return Objects.nonNull(additionalItems);
        }

        private boolean isItems() {
            return Objects.isNull(items) || items.isEmpty();
        }

        private int size() {
            if (Objects.nonNull(items)) {
                if (items.size() == 1) {
                    return Integer.MAX_VALUE;
                }
                return items.size();
            }
            return 0;
        }

        private Instance getItem(int index) {
            if (Objects.nonNull(items)) {
                if (items.size() == 1) {
                    return items.get(0);
                }
                if (index < items.size()) {
                    return items.get(index);
                }
            }
            return additionalItems;
        }

    }
}
