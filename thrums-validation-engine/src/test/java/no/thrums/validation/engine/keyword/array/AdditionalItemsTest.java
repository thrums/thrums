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
package no.thrums.validation.engine.keyword.array;

import no.thrums.validation.engine.keyword.Helper;
import no.thrums.validation.engine.keyword.Performer;
import no.thrums.validation.engine.keyword.Utils;
import no.thrums.validation.engine.keyword.any.Type;
import no.thrums.validation.instance.Instance;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Kristian Myrhaug
 * @since 2014-10-29
 */
public class AdditionalItemsTest {

    private Helper helper;

    @Before
    public void before() {
        helper = new Helper(
                new Type(),
                new AdditionalItems()
        );
    }

    @Test
    public void either_is_absent_is_valid() {
        Instance schema = helper.instance("{}");
        Assert.assertTrue(helper.validate(schema, "null").isEmpty());
        Assert.assertTrue(helper.validate(schema, "[]").isEmpty());
        Assert.assertTrue(helper.validate(schema, "[1]").isEmpty());
        Assert.assertTrue(helper.validate(schema, "[1, 2]").isEmpty());
    }

    @Test
    public void items_is_absent_is_valid() {
        Instance schema = helper.instance("{\"additionalItems\":false}");
        Assert.assertTrue(helper.validate(schema, "null").isEmpty());
        Assert.assertTrue(helper.validate(schema, "[]").isEmpty());
        Assert.assertFalse(helper.validate(schema, "[1]").isEmpty());
        Assert.assertFalse(helper.validate(schema, "[1, 2]").isEmpty());
    }

    @Test
    public void additionalItems_is_absent_is_valid() {
        Instance schema = helper.instance("{\"items\":[]}");
        Assert.assertTrue(helper.validate(schema, "null").isEmpty());
        Assert.assertTrue(helper.validate(schema, "[]").isEmpty());
        Assert.assertTrue(helper.validate(schema, "[1]").isEmpty());
        Assert.assertTrue(helper.validate(schema, "[1, 2]").isEmpty());
    }

    @Test
    public void additionalItems_is_true_is_valid() {
        Instance schema = helper.instance("{\"additionalItems\":true,\"items\":[]}");
        Assert.assertTrue(helper.validate(schema, "null").isEmpty());
        Assert.assertTrue(helper.validate(schema, "[]").isEmpty());
        Assert.assertTrue(helper.validate(schema, "[1]").isEmpty());
        Assert.assertTrue(helper.validate(schema, "[1, 2]").isEmpty());
    }

    @Test
    public void additionalItems_is_false_and_items_is_array_behaves() {
        Instance schema = helper.instance("{\"additionalItems\":false,\"items\":[{\"type\":\"integer\"}]}");
        Assert.assertTrue(helper.validate(schema, "null").isEmpty());
        Assert.assertTrue(helper.validate(schema, "[]").isEmpty());
        Assert.assertTrue(helper.validate(schema, "[1]").isEmpty());
        Assert.assertFalse(helper.validate(schema, "[1, 2.0]").isEmpty());
    }

    @Test
    public void items_is_not_object_or_array() {
        Assert.assertTrue(Utils.isThrowsRuntimeException(new Performer() {
            @Override
            public void perform() {
                helper.validate(helper.instance("{\"items\":false}"), "null");
            }
        }));
    }

    @Test
    public void additionalItems_is_not_object_or_boolean() {
        Assert.assertTrue(Utils.isThrowsRuntimeException(new Performer() {
            @Override
            public void perform() {
                helper.validate(helper.instance("{\"additionalItems\":1,\"items\":[]}"), "null");
            }
        }));
    }
}
