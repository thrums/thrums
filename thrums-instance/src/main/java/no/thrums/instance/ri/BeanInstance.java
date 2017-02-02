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

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

import static java.util.Objects.nonNull;

/**
 * @author Kristian Myrhaug
 * @since 2015-02-23
 */
public class BeanInstance extends RiInstance {

    private final Object value;
    private BeanInfo beanInfo;

    public BeanInstance(InstanceFactory instanceFactory, Instance parent, Object value) {
        super(instanceFactory, parent);
        this.value = Objects.requireNonNull(value, "Third argument may not be null");
    }

    @Override
    public boolean isObject() {
        return true;
    }

    @Override
    public Instance get(String key) {
        try {
            for (PropertyDescriptor propertyDescriptor : getBeanInfo().getPropertyDescriptors()) {
                String name = propertyDescriptor.getName();
                if (nonNull(name) && name.equals(key)) {
                    Method readMethod = propertyDescriptor.getReadMethod();
                    if (nonNull(readMethod)) {
                        if (Modifier.isPublic(propertyDescriptor.getReadMethod().getModifiers())) {
                            try {
                                return getInstanceFactory().defined(this, readMethod.invoke(this.value));
                            } catch (IllegalAccessException | InvocationTargetException cause) {
                                throw new RuntimeException("Could not get value", cause);
                            }
                        }
                    }
                }
            }
        } catch (IntrospectionException cause) {
            throw new RuntimeException(cause);
        }
        return getInstanceFactory().undefined(this);

    }

    @Override
    public Set<String> keys() {
        Set<String> properties = new LinkedHashSet<>();
        try {
            for (PropertyDescriptor propertyDescriptor : getBeanInfo().getPropertyDescriptors()) {
                String name = propertyDescriptor.getName();
                if (nonNull(name)) {
                    Method readMethod = propertyDescriptor.getReadMethod();
                    if (nonNull(readMethod)) {
                        if (Modifier.isPublic(propertyDescriptor.getReadMethod().getModifiers())) {
                            try {
                                readMethod.invoke(this.value);
                                properties.add(name);
                            } catch (IllegalAccessException | InvocationTargetException cause) {
                                throw new RuntimeException("Could not get key", cause);
                            }
                        }
                    }
                }
            }
        } catch (IntrospectionException cause) {
            throw new RuntimeException(cause);
        }
        return properties;
    }

    @Override
    public Map<String, Instance> properties() {
        Map<String, Instance> properties = new LinkedHashMap<>();
        try {
            for (PropertyDescriptor propertyDescriptor : getBeanInfo().getPropertyDescriptors()) {
                String name = propertyDescriptor.getName();
                if (nonNull(name)) {
                    Method readMethod = propertyDescriptor.getReadMethod();
                    if (nonNull(readMethod)) {
                        if (Modifier.isPublic(propertyDescriptor.getReadMethod().getModifiers())) {
                            try {
                                Object value = readMethod.invoke(this.value);
                                if (nonNull(value)) {
                                    properties.put(name, getInstanceFactory().defined(this, value));
                                }
                            } catch (IllegalAccessException | InvocationTargetException cause) {
                                throw new RuntimeException("Could not get value", cause);
                            }
                        }
                    }
                }
            }
        } catch (IntrospectionException cause) {
            throw new RuntimeException(cause);
        }
        return properties;

    }

    @Override
    public Object asValue() {
        return value;
    }

    private BeanInfo getBeanInfo() throws IntrospectionException {
        if (Objects.isNull(beanInfo)) {
            beanInfo = Introspector.getBeanInfo(this.value.getClass(), Object.class);
        }
        return beanInfo;
    }

}
