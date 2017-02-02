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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Kristian Myrhaug
 * @since 2015-02-23
 */
public class ListInstance extends RiInstance {

    private final List<Object> value;

    public ListInstance(InstanceFactory instanceFactory, Instance parent, List<Object> value) {
        super(instanceFactory, parent);
        this.value = Objects.requireNonNull(value, "Third argument may not be null");
    }

    @Override
    public boolean isArray() {
        return true;
    }

    @Override
    public Integer itemsSize() {
        return value.size();
    }

    @Override
    public List<Instance> items() {
        return getItems();
    }

    @Override
    public List<Object> asValue() {
        return value;
    }

    @Override
    public Instance get(Integer index) {
        return 0 <= index && index < value.size() ? getItems().get(index) : getInstanceFactory().undefined(this);
    }

    private List<Instance> getItems() {
        List<Instance> items = new ArrayList<>(value.size());
        for (Object item : value) {
            items.add(getInstanceFactory().defined(this, item));
        }
        return items;
    }

}
