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

import java.lang.reflect.Array;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author Kristian Myrhaug
 * @since 2015-02-13
 */
public class RiInstanceFactory implements InstanceFactory {

    public RiInstanceFactory() {}

    @Override
    public Instance defined(Instance parent, Object object) {
        if (Objects.isNull(object)) {
            return createNull(parent);
        } else if (object instanceof List) {
            return createArrayFromList(parent, (List<Object>)object);
        } else if (object instanceof Boolean) {
            return createBoolean(parent, (Boolean)object);
        } else if (object instanceof Enum){
            return createJavaEnum(parent, (Enum)object);
        } else if (object instanceof Number) {
            return createNumber(parent, (Number)object);
        } else if (object instanceof Map) {
            return createObjectFromMap(parent, (Map<String, Object>) object);
        } else if (object instanceof String) {
            return createString(parent, (String)object);
        } else if (object.getClass().isArray()) {
            Object[] array = new Object[Array.getLength(object)];
            for (int index = 0; index < array.length; index++) {
                array[index] = Array.get(object, index);
            }
            return createArray(parent, array);

        } else if (object instanceof Instance) {
            return defined(parent, ((Instance)object).asValue());
        }
        return createObject(parent, object);
    }

    @Override
    public Instance undefined(Instance parent) {
        return createUndefined(parent);
    }

    @Override
    public Instance defined(Object object) {
        return defined(null, object);
    }

    @Override
    public Instance undefined() {
        return undefined(null);
    }

    @Override
    public Instance createArray(Instance parent, Object[] value) {
        return new ArrayInstance(this, parent, value);
    }

    @Override
    public Instance createArrayFromList(Instance parent, List<Object> value) {
        return new ListInstance(this, parent, value);
    }

    @Override
    public Instance createBoolean(Instance parent, Boolean value) {
        return new BooleanInstance(this, parent, value);
    }

    @Override
    public Instance createJavaEnum(Instance parent, Enum value) {
        return new EnumInstance(this, parent, value);
    }

    @Override
    public Instance createNull(Instance parent) {
        return new NullInstance(this, parent);
    }

    @Override
    public Instance createNumber(Instance parent, Number value) {
        return new NumberInstance(this, parent, value);
    }

    @Override
    public Instance createObject(Instance parent, Object value) {
        return new BeanInstance(this, parent, value);
    }

    @Override
    public Instance createObjectFromMap(Instance parent, Map<String, Object> value) {
        return new MapInstance(this, parent, value);
    }

    @Override
    public Instance createString(Instance parent, String value) {
        return new StringInstance(this, parent, value);
    }

    @Override
    public Instance createUndefined(Instance parent) {
        return new UndefinedInstance(this, parent);
    }

}
