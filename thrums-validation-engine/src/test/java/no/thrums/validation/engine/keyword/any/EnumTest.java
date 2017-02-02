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
package no.thrums.validation.engine.keyword.any;

import no.thrums.instance.Instance;
import no.thrums.validation.engine.keyword.Helper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigInteger;

/**
 * @author Kristian Myrhaug
 * @since 2014-11-16
 */
public class EnumTest {

    private Helper helper;

    @Before
    public void before() {
        helper = new Helper(
                new Enum()
        );
    }

    @Test
    public void that_enum_null_validates() {
        Instance schema = helper.instance("{\"enum\":null}");

        Assert.assertTrue(helper.validate(schema, "null").isEmpty());
        Assert.assertTrue(helper.validate(schema, "1").isEmpty());
        Assert.assertTrue(helper.validate(schema, "\"two\"").isEmpty());
    }

    @Test
    public void that_enum_empty_validates() {
        Instance schema = helper.instance("{\"enum\":[]}");

        Assert.assertTrue(helper.validate(schema, "null").isEmpty());
        Assert.assertTrue(helper.validate(schema, "1").isEmpty());
        Assert.assertTrue(helper.validate(schema, "\"two\"").isEmpty());
    }

    @Test
    public void that_enum_not_empty_validates() {
        Instance schema = helper.instance("{\"enum\":[null, 1, \"two\"]}");

        Assert.assertTrue(helper.validate(schema, "null").isEmpty());
        Assert.assertTrue(helper.validate(schema, "1").isEmpty());
        Assert.assertTrue(helper.validate(schema, "\"two\"").isEmpty());
        Assert.assertTrue(helper.validate(schema, helper.defined(BigInteger.valueOf(1))).isEmpty());
    }

    @Test
    public void that_enum_not_empty_invalidates() {
        Instance schema = helper.instance("{\"enum\":[0, \"one\", 2]}");

        Assert.assertFalse(helper.validate(schema, "null").isEmpty());
        Assert.assertFalse(helper.validate(schema, "1").isEmpty());
        Assert.assertFalse(helper.validate(schema, "\"two\"").isEmpty());
    }
}
