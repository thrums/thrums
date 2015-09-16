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

import no.thrums.validation.engine.keyword.Utils;
import no.thrums.validation.instance.Instance;
import no.thrums.validation.engine.keyword.Helper;
import no.thrums.validation.engine.keyword.Performer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Kristian Myrhaug
 * @since 2014-10-30
 */
public class UniqueItemsTest {

    private Helper helper;

    @Before
    public void before() {
        helper = new Helper(
                new UniqueItems()
        );
    }

    @Test
    public void uniqueItems_is_of_wrong_type_throws_exception() {
        Utils.isThrowsRuntimeException(new Performer() {
            @Override
            public void perform() {
                helper.validate(helper.instance("{\"uniqueItems\":{}}"), "null");
            }
        });
    }

    @Test
    public void uniqueItems_absent_validates() {
        Instance schema = helper.instance("{}");
        Assert.assertTrue(helper.validate(schema, "[1, 2]").isEmpty());
        Assert.assertTrue(helper.validate(schema, "[1, 2, 1, 2]").isEmpty());
    }

    @Test
    public void uniqueItems_false_validates() {
        Instance schema = helper.instance("{\"uniqueItems\":false}");
        Assert.assertTrue(helper.validate(schema, "[1, 2]").isEmpty());
        Assert.assertTrue(helper.validate(schema, "[1, 2, 1, 2]").isEmpty());
    }

    @Test
    public void uniqueItems_true_validates() {
        Instance schema = helper.instance("{\"uniqueItems\":true}");
        Assert.assertTrue(helper.validate(schema, "[1, 2]").isEmpty());
        Assert.assertFalse(helper.validate(schema, "[1, 2, 1, 2]").isEmpty());
    }

}
