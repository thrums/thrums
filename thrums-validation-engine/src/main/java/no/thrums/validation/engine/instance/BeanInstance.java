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
package no.thrums.validation.engine.instance;

import no.thrums.reflection.Bean;
import no.thrums.validation.instance.Instance;
import no.thrums.validation.instance.InstanceFactory;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author Kristian Myrhaug
 * @since 2015-02-23
 */
public class BeanInstance extends EngineInstance {

    private final Object value;
    private final Bean bean;

    public BeanInstance(InstanceFactory instanceFactory, Instance parent, Object value) {
        super(instanceFactory, parent);
        this.value = Objects.requireNonNull(value, "Third argument may not be null");
        this.bean = new Bean(value);
    }

    @Override
    public boolean isObject() {
        return true;
    }

    @Override
    public Instance get(String key) {
        Map<String, Instance> properties = getProperties(true);
        return properties.containsKey(key) ? properties.get(key) : undefined();
    }

    @Override
    public Map<String, Instance> properties() {
        return getProperties(false);
    }

    @Override
    public Object asValue() {
        return value;
    }

    private Map<String, Instance> getProperties(boolean includeNull) {
        Map<String, Instance> properties = new LinkedHashMap<>();
        for (Map.Entry<String, Object> property : bean.entrySet()) {
            Instance instance = defined(property.getValue());
            if (instance.isNull()) {
                if (includeNull) {
                    properties.put(property.getKey(), instance);
                }
            } else {
                properties.put(property.getKey(), instance);
            }
        }
        return properties;
    }
}
