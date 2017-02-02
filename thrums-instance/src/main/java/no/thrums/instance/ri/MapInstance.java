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
package no.thrums.instance.ri;

import no.thrums.instance.Instance;
import no.thrums.instance.InstanceFactory;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * @author Kristian Myrhaug
 * @since 2015-02-23
 */
public class MapInstance extends RiInstance {

    private final Map<String,Object> value;

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
        return getProperties().containsKey(key) ? getProperties().get(key) : getInstanceFactory().undefined(this);
    }

    @Override
    public Set<String> keys() {
        return value.keySet();
    }

    @Override
    public Map<String, Instance> properties() {
        return getProperties();
    }

    @Override
    public Map<String,Object> asValue() {
        return value;
    }

    private Map<String, Instance> getProperties() {
        Map<String,Instance> properties = new LinkedHashMap<>();
        for (Map.Entry<String, Object> property : value.entrySet()) {
            properties.put(property.getKey(), getInstanceFactory().defined(this, property.getValue()));
        }
        return properties;
    }

}
