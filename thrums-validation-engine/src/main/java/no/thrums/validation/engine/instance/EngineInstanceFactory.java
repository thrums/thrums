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
import no.thrums.validation.instance.InstanceLoader;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author Kristian Myrhaug
 * @since 2015-02-13
 */
public class EngineInstanceFactory implements InstanceFactory {

    private final InstanceLoader instanceLoader;

    public EngineInstanceFactory(InstanceLoader instanceLoader) {
        this.instanceLoader = instanceLoader;
    }

    @Override
    public Instance defined(Instance parent, Object object) {
        Instance instance = null;
        if (Objects.isNull(object)) {
            instance = new NullInstance(this, parent);
        } else if (object instanceof List) {
            instance = new ListInstance(this, parent, (List<Object>)object);
        } else if (object instanceof Boolean) {
            instance = new BooleanInstance(this, parent, (Boolean)object);
        } else if (object instanceof Number) {
            instance = new NumberInstance(this, parent, (Number)object);
        } else if (object instanceof Map) {
            instance = reference(new MapInstance(this, parent, (Map<String,Object>)object));
        } else if (object instanceof String) {
            instance = new StringInstance(this, parent, (String)object);
        } else if (object.getClass().isArray()) {
            return new ArrayInstance(this, parent, object);
        } else if (object instanceof Instance) {
            return defined(parent, ((Instance)object).asValue());
        } else {
            instance = reference(new BeanInstance(this, parent, object));
        }
        return instance;
    }

    @Override
    public Instance undefined(Instance parent) {
        return new UndefinedInstance(this, parent);
    }

    @Override
    public Instance defined(Object object) {
        return defined(null, object);
    }

    @Override
    public Instance undefined() {
        return undefined(null);
    }

    public Instance reference(Instance instance) {
        if (instance.isObject()) {
            Instance $ref = instance.get("$ref");
            if ($ref.isString()) {
                return new ReferenceInstance(this, instanceLoader, instance);
            }
        }
        return instance;
    }
}
