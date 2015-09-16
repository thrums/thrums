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
package no.thrums.validation.engine.keyword.any;

import no.thrums.validation.engine.keyword.Performer;
import no.thrums.validation.engine.keyword.Utils;
import no.thrums.validation.instance.Instance;
import no.thrums.validation.engine.keyword.Helper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Kristian Myrhaug
 * @since 2014-11-16
 */
public class AnyOfTest {

    private Helper helper;

    @Before
    public void before() {
        helper = new Helper(
                new Type(),
                new AnyOf()
        );
    }

    @Test
    public void that_anyOf_null_validates() {
        Instance schema = helper.instance("{\"anyOf\":null}");

        Assert.assertTrue(helper.validate(schema, "null").isEmpty());
        Assert.assertTrue(helper.validate(schema, "1").isEmpty());
        Assert.assertTrue(helper.validate(schema, "\"two\"").isEmpty());
    }

    @Test
    public void that_anyOf_empty_invalidates() {
        final Instance schema = helper.instance("{\"anyOf\":[]}");

        Assert.assertTrue(Utils.isThrowsRuntimeException(new Performer() {
            @Override
            public void perform() {
                helper.validate(schema, "null");
            }
        }));
    }

    @Test
    public void that_anyOf_not_empty_validates() {
        Instance schema = helper.instance("{\"anyOf\":[{\"type\":\"array\"},{\"type\":\"null\"},{\"type\":\"integer\"},{\"type\":\"string\"}]}");

        Assert.assertTrue(helper.validate(schema, "[]").isEmpty());
        Assert.assertFalse(helper.validate(schema, "true").isEmpty());
        Assert.assertTrue(helper.validate(schema, "null").isEmpty());
        Assert.assertFalse(helper.validate(schema, "1.0").isEmpty());
        Assert.assertTrue(helper.validate(schema, "1").isEmpty());
        Assert.assertFalse(helper.validate(schema, "{}").isEmpty());
        Assert.assertTrue(helper.validate(schema, "\"string\"").isEmpty());


        schema = helper.instance("{\"anyOf\":[{\"type\":\"boolean\"},{\"type\":\"number\"},{\"type\":\"object\"}]}");

        Assert.assertFalse(helper.validate(schema, "[]").isEmpty());
        Assert.assertTrue(helper.validate(schema, "true").isEmpty());
        Assert.assertFalse(helper.validate(schema, "null").isEmpty());
        Assert.assertTrue(helper.validate(schema, "1.0").isEmpty());
        Assert.assertTrue(helper.validate(schema, "1").isEmpty());
        Assert.assertTrue(helper.validate(schema, "{}").isEmpty());
        Assert.assertFalse(helper.validate(schema, "\"string\"").isEmpty());
    }
}
