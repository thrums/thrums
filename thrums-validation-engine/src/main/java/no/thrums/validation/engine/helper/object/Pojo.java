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
package no.thrums.validation.engine.helper.object;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.MethodDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
* @author Kristian Myrhaug
* @since 2015-02-17
*/
public class Pojo implements Map<String,Object> {

    private final Object pojo;
    private Map<String,PropertyDescriptor> propertyDescriptors;
    private Map<String,MethodDescriptor> methodDescriptors;

    public Pojo(Object pojo) {
        this.pojo = pojo;
    }

    public Object getPojo() {
        return pojo;
    }

    @Override
    public int size() {
        return getPropertyDescriptors().size() + getMethodDescriptors().size();
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        return getPropertyDescriptors().containsKey(key) || getMethodDescriptors().containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        for (PropertyDescriptor propertyDescriptor : getPropertyDescriptors().values()) {
            if (value.equals(getValue(propertyDescriptor))) {
                return true;
            }
        }
        for (MethodDescriptor methodDescriptor : getMethodDescriptors().values()) {
            if (value.equals(getValue(methodDescriptor))) {
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
        if (getMethodDescriptors().containsKey(key)) {
            return getValue(getMethodDescriptors().get(key));
        }
        return null;
    }

    @Override
    public Object put(String key, Object value) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Object remove(Object key) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void putAll(Map<? extends String, ?> m) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Set<String> keySet() {
        Set<String> keySet = new LinkedHashSet<>();
        keySet.addAll(getPropertyDescriptors().keySet());
        keySet.addAll(getMethodDescriptors().keySet());
        return keySet;
    }

    @Override
    public Collection<Object> values() {
        Set<String> read = new HashSet<>();
        List<Object> values = new ArrayList<>();
        for (PropertyDescriptor propertyDescriptor : getPropertyDescriptors().values()) {
            values.add(getValue(propertyDescriptor));
            read.add(propertyDescriptor.getName());
        }
        for (MethodDescriptor methodDescriptor : getMethodDescriptors().values()) {
            if (!read.contains(methodDescriptor.getName())) {
                values.add(getValue(methodDescriptor));
            }
        }
        return values;
    }

    @Override
    public Set<Entry<String, Object>> entrySet() {
        Set<String> read = new HashSet<>();
        Set<Entry<String,Object>> entrySet = new LinkedHashSet<>();
        for (Entry<String,PropertyDescriptor> entry : getPropertyDescriptors().entrySet()) {
            entrySet.add(new PojoEntry<>(entry.getKey(), getValue(entry.getValue())));
            read.add(entry.getKey());
        }
        for (Entry<String,MethodDescriptor> entry : getMethodDescriptors().entrySet()) {
            if (!read.contains(entry.getKey())) {
                entrySet.add(new PojoEntry<>(entry.getKey(), getValue(entry.getValue())));
            }
        }
        return entrySet;
    }

    private Map<String,PropertyDescriptor> getPropertyDescriptors() {
        if (Objects.isNull(propertyDescriptors)) {
            propertyDescriptors = new LinkedHashMap<>();
            try {
                for (PropertyDescriptor propertyDescriptor : Introspector.getBeanInfo(pojo.getClass(), Object.class).getPropertyDescriptors()) {
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

    private Map<String,MethodDescriptor> getMethodDescriptors() {
        if (Objects.isNull(methodDescriptors)) {
            methodDescriptors = new LinkedHashMap<>();
            try {
                for (MethodDescriptor methodDescriptor : Introspector.getBeanInfo(pojo.getClass(), Object.class).getMethodDescriptors()) {
                    if (isReadable(methodDescriptor)) {
                        methodDescriptors.put(methodDescriptor.getName(), methodDescriptor);
                    }
                }
            } catch (IntrospectionException cause) {
                throw new RuntimeException(cause);
            }
        }
        return methodDescriptors;
    }

    private Object getValue(PropertyDescriptor propertyDescriptor) {
        return getValue(propertyDescriptor.getReadMethod());
    }

    private Object getValue(MethodDescriptor methodDescriptor) {
        return getValue(methodDescriptor.getMethod());
    }

    private Object getValue(Method method) {
        try {
            return method.invoke(pojo);
        } catch (IllegalAccessException | InvocationTargetException cause) {
            throw new RuntimeException(cause);
        }
    }

    private boolean isReadable(PropertyDescriptor propertyDescriptor) {
        return Objects.nonNull(propertyDescriptor.getReadMethod()) &&
                Modifier.isPublic(propertyDescriptor.getReadMethod().getModifiers()) &&
                Objects.nonNull(propertyDescriptor.getName());
    }

    private boolean isReadable(MethodDescriptor methodDescriptor) {
        return Objects.nonNull(methodDescriptor.getMethod()) &&
                !containsMethod(getPropertyDescriptors(), methodDescriptor.getMethod()) &&
                !Void.TYPE.equals(methodDescriptor.getMethod().getReturnType()) &&
                methodDescriptor.getMethod().getParameterCount() == 0 &&
                Modifier.isPublic(methodDescriptor.getMethod().getModifiers()) &&
                Objects.nonNull(methodDescriptor.getName());
    }

    private boolean containsMethod(Map<String,PropertyDescriptor> propertyDescriptors, Method method) {
        for (PropertyDescriptor propertyDescriptor : propertyDescriptors.values()) {
            if (propertyDescriptor.getReadMethod().equals(method)) {
                return true;
            }
        }
        return false;
    }

    private class PojoEntry<K,V> implements Entry<K,V> {

        private final K key;
        private final V value;

        private PojoEntry(K key, V value) {
            this.key = key;
            this.value = value;
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
            throw new UnsupportedOperationException("Not implemented");
        }
    }
}
