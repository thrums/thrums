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

import java.math.BigInteger;
import java.util.Objects;

/**
 * @author Kristian Myrhaug
 * @since 2015-02-23
 */
public class NumberInstance extends RiInstance {

    private final Number value;
    private final boolean integral;

    public NumberInstance(InstanceFactory instanceFactory, Instance parent, Number value) {
        super(instanceFactory, parent);
        this.value = Objects.requireNonNull(value, "Third argument may not be null");
        this.integral = value instanceof Byte || value instanceof Short || value instanceof Integer || value instanceof Long || value instanceof BigInteger;
    }

    @Override
    public boolean isIntegral() {
        return integral;
    }

    @Override
    public boolean isNumber() {
        return true;
    }

    @Override
    public Number asNumber() {
        return value;
    }

    @Override
    public Object asValue() {
        return value;
    }
}
