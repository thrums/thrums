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
package no.thrums.validation.engine.keyword.number;

import no.thrums.validation.engine.keyword.Helper;
import no.thrums.validation.engine.keyword.Performer;
import no.thrums.validation.engine.keyword.Utils;
import no.thrums.instance.Instance;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author Kristian Myrhaug
 * @since 2014-10-16
 */
public class MaximumTest {

    private Helper helper;

    @Before
    public void before() {
        helper = new Helper(
                new Maximum()
        );
    }

    @Test
    public void that_maximum_keyword(){
        Assert.assertTrue(Utils.isThrowsRuntimeException(new Performer() {
            @Override
            public void perform() {
                helper.validate("{\"maximum\":[]}", "null");
            }
        }));
        Assert.assertTrue(Utils.isThrowsRuntimeException(new Performer() {
            @Override
            public void perform() {
                helper.validate("{\"maximum\":true}", "null");
            }
        }));
        Assert.assertFalse(Utils.isThrowsRuntimeException(new Performer() {
            @Override
            public void perform() {
                helper.validate("{\"maximum\":1}", "null");
            }
        }));
        Assert.assertTrue(Utils.isThrowsRuntimeException(new Performer() {
            @Override
            public void perform() {
                helper.validate("{\"maximum\":\"one\"}", "null");
            }
        }));
        Assert.assertFalse(Utils.isThrowsRuntimeException(new Performer() {
            @Override
            public void perform() {
                helper.validate("{\"maximum\":null}", "null");
            }
        }));
        Assert.assertTrue(Utils.isThrowsRuntimeException(new Performer() {
            @Override
            public void perform() {
                helper.validate("{\"maximum\":{}}", "null");
            }
        }));
    }

    @Test
    public void that_maximum_inclusive_validates_correctly(){
        Instance schema = helper.instance("{\"maximum\":4}");

        assertTrue(schema, 3);
        assertTrue(schema, 4);
        assertFalse(schema, 5);

        schema = helper.instance("{\"maximum\":4,\"exclusiveMaximum\":false}");

        assertTrue(schema, 3);
        assertTrue(schema, 4);
        assertFalse(schema, 5);
    }

    @Test
    public void testShortMaximumExclusiveMaximum(){
        Instance schema = helper.instance("{\"maximum\":4,\"exclusiveMaximum\":true}");

        assertTrue(schema, 3);
        assertFalse(schema, 4);
        assertFalse(schema, 5);
    }

    private void assertTrue(Instance schema, int number) {
        Assert.assertTrue(helper.validate(schema, helper.defined(Byte.valueOf((byte) number))).isEmpty());
        Assert.assertTrue(helper.validate(schema, helper.defined(Short.valueOf((short) number))).isEmpty());
        Assert.assertTrue(helper.validate(schema, helper.defined(Integer.valueOf(number))).isEmpty());
        Assert.assertTrue(helper.validate(schema, helper.defined(Long.valueOf((long) number))).isEmpty());
        Assert.assertTrue(helper.validate(schema, helper.defined(BigInteger.valueOf((long) number))).isEmpty());
        Assert.assertTrue(helper.validate(schema, helper.defined(Float.valueOf((float) number))).isEmpty());
        Assert.assertTrue(helper.validate(schema, helper.defined(Double.valueOf((double) number))).isEmpty());
        Assert.assertTrue(helper.validate(schema, helper.defined(BigDecimal.valueOf((double) number))).isEmpty());
    }

    private void assertFalse(Instance schema, int number) {
        Assert.assertFalse(helper.validate(schema, helper.defined(Byte.valueOf((byte) number))).isEmpty());
        Assert.assertFalse(helper.validate(schema, helper.defined(Short.valueOf((short) number))).isEmpty());
        Assert.assertFalse(helper.validate(schema, helper.defined(Integer.valueOf(number))).isEmpty());
        Assert.assertFalse(helper.validate(schema, helper.defined(Long.valueOf((long) number))).isEmpty());
        Assert.assertFalse(helper.validate(schema, helper.defined(BigInteger.valueOf((long) number))).isEmpty());
        Assert.assertFalse(helper.validate(schema, helper.defined(Float.valueOf((float) number))).isEmpty());
        Assert.assertFalse(helper.validate(schema, helper.defined(Double.valueOf((double) number))).isEmpty());
        Assert.assertFalse(helper.validate(schema, helper.defined(BigDecimal.valueOf((double) number))).isEmpty());
    }

}
