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
package no.thrums.reflection;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
* @author Kristian Myrhaug
* @since 2015-02-17
*/
public class Bean implements Map<String,Object> {

    private final Object bean;
    private Map<String,PropertyDescriptor> propertyDescriptors;

    public Bean(Object bean) {
        this.bean = bean;
    }

    public Object getBean() {
        return bean;
    }

    @Override
    public int size() {
        return getPropertyDescriptors().size();
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        return getPropertyDescriptors().containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        for (PropertyDescriptor propertyDescriptor : getPropertyDescriptors().values()) {
            if (value.equals(getValue(propertyDescriptor))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Object get(Object key) {
        if (getPropertyDescriptors().containsKey(key)) {
            return getValue(getPropertyDescriptors().get(key));
        }
        return null;
    }

    @Override
    public Object put(String key, Object value) {
        Object old = null;
        if (getPropertyDescriptors().containsKey(key)) {
            PropertyDescriptor propertyDescriptor = getPropertyDescriptors().get(key);
            old = getValue(propertyDescriptor);
            setValue(propertyDescriptor, value);
        }
        return old;
    }

    @Override
    public Object remove(Object key) {
        Object old = null;
        if (getPropertyDescriptors().containsKey(key)) {
            PropertyDescriptor propertyDescriptor = getPropertyDescriptors().get(key);
            old = getValue(propertyDescriptor);
            setValue(propertyDescriptor, null);
        }
        return old;
    }

    @Override
    public void putAll(Map<? extends String, ?> map) {
        Map<String,Object> olds = new LinkedHashMap<>();
        try {
            for (Entry<? extends String, ?> entry : map.entrySet()) {
                olds.put(entry.getKey(), this.put(entry.getKey(), entry.getValue()));
            }
        } catch (RuntimeException cause) {
            for (Entry<String,Object> entry : olds.entrySet()) {
                this.put(entry.getKey(), entry.getValue());
            }
            throw cause;
        }
    }

    @Override
    public void clear() {
        Map<String,Object> map = new LinkedHashMap<>(this);
        for (Entry<String,Object> entry : this.entrySet()) {
            map.put(entry.getKey(), null);
        }
        this.putAll(map);
    }

    @Override
    public Set<String> keySet() {
        return getPropertyDescriptors().keySet();
    }

    @Override
    public Collection<Object> values() {
        List<Object> values = new LinkedList<>();
        for (PropertyDescriptor propertyDescriptor : getPropertyDescriptors().values()) {
            values.add(getValue(propertyDescriptor));
        }
        return values;
    }

    @Override
    public Set<Entry<String, Object>> entrySet() {
        Set<Entry<String,Object>> entrySet = new LinkedHashSet<>();
        for (Entry<String,PropertyDescriptor> entry : getPropertyDescriptors().entrySet()) {
            entrySet.add(new BeanEntry<>(entry.getKey(), getValue(entry.getValue()), this));
        }
        return entrySet;
    }

    private Map<String,PropertyDescriptor> getPropertyDescriptors() {
        if (Objects.isNull(propertyDescriptors)) {
            propertyDescriptors = new LinkedHashMap<>();
            try {
                for (PropertyDescriptor propertyDescriptor : Introspector.getBeanInfo(bean.getClass(), Object.class).getPropertyDescriptors()) {
                    if (isReadable(propertyDescriptor)) {
                        propertyDescriptors.put(propertyDescriptor.getName(), propertyDescriptor);
                    }
                }
            } catch (IntrospectionException cause) {
                throw new RuntimeException(cause);
            }
        }
        return propertyDescriptors;
    }

    private Object getValue(PropertyDescriptor propertyDescriptor) {
        return getValue(propertyDescriptor.getReadMethod());
    }

    private void setValue(PropertyDescriptor propertyDescriptor, Object value) {
        setValue(propertyDescriptor.getWriteMethod(), value);
    }

    private Object getValue(Method method) {
        try {
            return method.invoke(bean);
        } catch (IllegalAccessException | InvocationTargetException cause) {
            throw new RuntimeException("Could not get value", cause);
        }
    }

    private void setValue(Method method, Object value) {
        try {
            method.invoke(bean, value);
        } catch (InvocationTargetException | IllegalAccessException cause) {
            throw new RuntimeException("Could not set value", cause);
        }
    }

    private boolean isReadable(PropertyDescriptor propertyDescriptor) {
        return Objects.nonNull(propertyDescriptor.getReadMethod()) &&
                Modifier.isPublic(propertyDescriptor.getReadMethod().getModifiers()) &&
                Objects.nonNull(propertyDescriptor.getName());
    }

    private boolean isWritable(PropertyDescriptor propertyDescriptor) {
        return Objects.nonNull(propertyDescriptor.getWriteMethod()) &&
                Modifier.isPublic(propertyDescriptor.getWriteMethod().getModifiers()) &&
                Objects.nonNull(propertyDescriptor.getName());
    }

    private class BeanEntry<K,V> implements Entry<K,V> {

        private final K key;
        private final V value;
        private Map<K,V> membership;

        private BeanEntry(K key, V value, Map<K,V> membership) {
            this.key = key;
            this.value = value;
            this.membership = membership;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V value) {
            return membership.put(this.getKey(), value);
        }
    }
}
