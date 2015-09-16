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

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.util.Objects.*;

/**
 * @author Kristian Myrhaug
 * @since 2015-02-23
 */
public abstract class EngineInstance implements Instance {

    protected final InstanceFactory instanceFactory;
    private final Instance parent;

    public EngineInstance(InstanceFactory instanceFactory, Instance parent) {
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
    public boolean isReference() {
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
        return instanceFactory.undefined(this);
    }

    @Override
    public Instance get(Integer index) {
        return instanceFactory.undefined(this);
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
    public Instance defined(Object object) {
        return instanceFactory.defined(this, object);
    }

    @Override
    public Instance undefined() {
        return instanceFactory.undefined(this);
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof Instance && equals((Instance)other);
    }

    public boolean equals(Instance other) {
        return Objects.equals(isArray(), other.isArray()) &&
                Objects.equals(isBoolean(), other.isBoolean()) &&
                Objects.equals(isIntegral(), other.isIntegral()) &&
                Objects.equals(isNull(), other.isNull()) &&
                Objects.equals(isReference(), other.isReference()) &&
                Objects.equals(isUndefined(), other.isUndefined()) &&
                Objects.equals(isNumber(), other.isNumber()) &&
                Objects.equals(isObject(), other.isObject()) &&
                Objects.equals(isString(), other.isString()) &&
                Objects.equals(asValue(), other.asValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(isArray(), isBoolean(), isIntegral(), isNull(), isReference(), isUndefined(), isNumber(), isObject(), isString(), asValue());
    }
}
