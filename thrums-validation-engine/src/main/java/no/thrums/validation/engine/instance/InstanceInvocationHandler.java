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
package no.thrums.validation.engine.instance;

import no.thrums.instance.Instance;
import no.thrums.validation.instance.ReferenceResolver;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.LinkedList;
import java.util.List;

import static java.util.Objects.nonNull;

/**
 * @author Kristian Myrhaug
 * @since 2016-05-06
 */
public class InstanceInvocationHandler implements InvocationHandler {

    private final ReferenceResolver referenceResolver;
    private final Instance value;
    private String pointer = null;
    private Instance $refTarget = null;

    public InstanceInvocationHandler(ReferenceResolver referenceResolver, Instance value) {
        this.referenceResolver = referenceResolver;
        this.value = value;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object... arguments) throws Throwable {
        Instance $ref = value.get("$ref");
        if ($ref.isString()) {
            String pointer = $ref.asString();
            if (!pointer.equals(this.pointer)) {
                this.pointer = pointer;
                //TODO prevent infinite loop
                $refTarget = createProxy(referenceResolver, referenceResolver.resolve(value));
            }
        }
        if (nonNull($refTarget)) {
            return method.invoke($refTarget, arguments);
        }
        return method.invoke(value, arguments);
    }

    public static Class<?>[] getInterfaces(Class<?> clazz) {
        List<Class<?>> interfaces = new LinkedList<>();
        while (nonNull(clazz)) {
            Class<?>[] classes = clazz.getInterfaces();
            for (int index = 0; index < classes.length; index++) {
                interfaces.add(classes[index]);
            }
            clazz = clazz.getSuperclass();
        }
        return interfaces.toArray(new Class[interfaces.size()]);
    }

    public static Instance createProxy(ReferenceResolver referenceResolver, Instance instance) {
        return (Instance) Proxy.newProxyInstance(
                instance.getClass().getClassLoader(),
                getInterfaces(instance.getClass()),
                new InstanceInvocationHandler(referenceResolver, instance));
    }
}
