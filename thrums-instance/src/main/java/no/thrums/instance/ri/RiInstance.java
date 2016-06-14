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
package no.thrums.instance.ri;

import no.thrums.instance.Instance;
import no.thrums.instance.InstanceFactory;
import no.thrums.instance.path.Node;
import no.thrums.instance.path.Path;
import no.thrums.instance.ri.helper.NumberComparator;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;
import static no.thrums.instance.ri.helper.NumberUtilities.toBigDecimal;

/**
 * @author Kristian Myrhaug
 * @since 2015-02-23
 */
public abstract class RiInstance implements Instance {

    private static final NumberComparator NUMBER_COMPARATOR = new NumberComparator();
    protected final InstanceFactory instanceFactory;
    private final Instance parent;

    public RiInstance(InstanceFactory instanceFactory, Instance parent) {
        this.instanceFactory = instanceFactory;
        this.parent = parent;
    }

    @Override
    public boolean isArray() {
        return false;
    }

    @Override
    public boolean isBoolean() {
        return false;
    }

    @Override
    public boolean isIntegral() {
        return false;
    }

    @Override
    public boolean isNull() {
        return false;
    }

    @Override
    public boolean isUndefined() {
        return false;
    }

    @Override
    public boolean isNumber() {
        return false;
    }

    @Override
    public boolean isObject() {
        return false;
    }

    @Override
    public boolean isString() {
        return false;
    }

    @Override
    public Instance get(String key) {
        return getInstanceFactory().undefined(this);
    }

    @Override
    public Instance get(Integer index) {
        return getInstanceFactory().undefined(this);
    }

    @Override
    public Instance get(Path path) {
        if (nonNull(path) && path.size() > 0) {
            Instance instance = this;
            for (int index = 0; index < path.size(); index++) {
                Node node = requireNonNull(path.get(index), "May not be null: [" + index + "]");
                Object value = node.getValue();
                if (value instanceof String) {
                    instance = instance.get((String)value);
                } else if (value instanceof Integer) {
                    instance = instance.get((Integer) value);
                } else {
                    return getInstanceFactory().undefined(instance);
                }
            }
            return instance;
        }
        return this;
    }

    @Override
    public Integer itemsSize() {
        return null;
    }

    @Override
    public List<Instance> items() {
        return null;
    }

    @Override
    public Boolean asBoolean() {
        return null;
    }

    @Override
    public Number asNumber() {
        return null;
    }

    @Override
    public Set<String> keys() {
        return null;
    }

    @Override
    public Map<String, Instance> properties() {
        return null;
    }

    @Override
    public String asString() {
        return null;
    }

    @Override
    public boolean isPresent() {
        return !(isNull() || isUndefined());
    }

    @Override
    public Object asValue() {
        return null;
    }

    @Override
    public Instance getParent() {
        return parent;
    }

    @Override
    public Instance getRoot() {
        Instance candidate = this;
        while (nonNull(candidate.getParent())) {
            candidate = candidate.getParent();
        }
        return candidate;
    }

    @Override
    public InstanceFactory getInstanceFactory() {
        return instanceFactory;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof Instance && equals((Instance)other);
    }

    public boolean equals(Instance other) {
        if (    Objects.equals(isArray(), other.isArray()) &&
                Objects.equals(isBoolean(), other.isBoolean()) &&
                Objects.equals(isIntegral(), other.isIntegral()) &&
                Objects.equals(isNull(), other.isNull()) &&
                Objects.equals(isUndefined(), other.isUndefined()) &&
                Objects.equals(isNumber(), other.isNumber()) &&
                Objects.equals(isObject(), other.isObject()) &&
                Objects.equals(isString(), other.isString())) {
            if (isNumber()) {
                return NUMBER_COMPARATOR.compare(asNumber(), other.asNumber()) == 0;
            } else if (isObject()) {
                Map<String,Instance> thisProperties = this.properties();
                Map<String,Instance> otherProperties = other.properties();
                List<String> thisKeySet = thisProperties.keySet().stream().sorted(String::compareTo).collect(Collectors.toList());
                List<String> otherKeySet = otherProperties.keySet().stream().sorted(String::compareTo).collect(Collectors.toList());
                if (thisKeySet.equals(otherKeySet)) {
                    for (String key : thisKeySet) {
                        if (!thisProperties.get(key).equals(otherProperties.get(key))) {
                            return false;
                        }
                    }
                    return true;
                }
                return false;
            } else if (isArray()) {
                List<Instance> thisItems = this.items().stream().collect(Collectors.toList());
                List<Instance> otherItems = other.items().stream().collect(Collectors.toList());
                return thisItems.equals(otherItems);
            }
            return Objects.equals(asValue(), other.asValue());
        }
        return false;
    }

    @Override
    public int hashCode() {
        Object value = asValue();
        if (isNumber()) {
            value = toBigDecimal(asNumber());
        } else if (isObject()) {
            value = properties().entrySet().stream()
                    .sorted((a, b) -> a.getKey().compareTo(b.getKey()))
                    .map(Map.Entry::getValue)
                    .collect(Collectors.toList());
        } else if (isArray()) {
            value = items().stream()
                    .collect(Collectors.toList());
        }
        return Objects.hash(isArray(), isBoolean(), isIntegral(), isNull(), isUndefined(), isNumber(), isObject(), isString(), value);
    }
}
