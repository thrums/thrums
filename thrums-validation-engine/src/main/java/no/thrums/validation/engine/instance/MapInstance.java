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

import no.thrums.validation.instance.Instance;
import no.thrums.validation.instance.InstanceFactory;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author Kristian Myrhaug
 * @since 2015-02-23
 */
public class MapInstance extends EngineInstance {

    private final Map<String,Object> value;
    private Map<String, Instance> properties;

    public MapInstance(InstanceFactory instanceFactory, Instance parent, Map<String, Object> value) {
        super(instanceFactory, parent);
        this.value = Objects.requireNonNull(value, "Third argument may not be null");
    }

    @Override
    public boolean isObject() {
        return true;
    }

    @Override
    public Instance get(String key) {
        return getProperties().containsKey(key) ? getProperties().get(key) : undefined();
    }

    @Override
    public Map<String, Instance> properties() {
        return getProperties();
    }

    @Override
    public Object asValue() {
        return value;
    }

    private Map<String, Instance> getProperties() {
        if (Objects.isNull(properties)) {
            properties = new LinkedHashMap<>();
            for (Map.Entry<String, Object> property : value.entrySet()) {
                properties.put(property.getKey(), defined(property.getValue()));
            }
        }
        return properties;
    }
}
