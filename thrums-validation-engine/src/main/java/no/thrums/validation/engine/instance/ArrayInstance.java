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
package no.thrums.validation.engine.instance;

import no.thrums.validation.instance.Instance;
import no.thrums.validation.instance.InstanceFactory;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Kristian Myrhaug
 * @since 2015-02-23
 */
public class ArrayInstance extends EngineInstance {

    private final Object value;
    private final List<Object> array;
    private List<Instance> items;

    public ArrayInstance(InstanceFactory instanceFactory, Instance parent, Object value) {
        super(instanceFactory, parent);
        this.value = Objects.requireNonNull(value, "Third argument may not be null");
        if (!value.getClass().isArray()) {
            throw new IllegalArgumentException("Third argument must be array");
        }
        array = new ArrayList<>();
        for (int index = 0; index < Array.getLength(value); index++) {
            array.add(Array.get(value, index));
        }
    }

    @Override
    public boolean isArray() {
        return true;
    }

    @Override
    public List<Instance> items() {
        return getItems();
    }

    @Override
    public Object asValue() {
        return value;
    }

    @Override
    public Instance get(Integer index) {
        return containsIndex(index) ? getItems().get(index) : undefined();
    }

    private boolean containsIndex(Integer index) {
        return index < getItems().size();
    }

    private List<Instance> getItems() {
        if (Objects.isNull(items)) {
            List<Instance> items = new ArrayList<>();
            for (Object item : this.array) {
                items.add(defined(item));
            }
            this.items = items;
        }
        return items;
    }
}
