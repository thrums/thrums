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
package no.thrums.validation.engine.keyword.array;

import no.thrums.validation.instance.Instance;
import no.thrums.validation.engine.keyword.Helper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Kristian Myrhaug
 * @since 2014-10-18
 */
public class MinItemsTest {

    private Helper helper;

    @Before
    public void before() {
        helper = new Helper(
                new MinItems()
        );
    }

    @Test
    public void test() {
        Instance schema = helper.instance("{\"minItems\":1}");

        Assert.assertFalse(helper.validate(schema, "[]").isEmpty());
        Assert.assertTrue(helper.validate(schema, "[1]").isEmpty());
        Assert.assertTrue(helper.validate(schema, "[1, 2]").isEmpty());
    }
}
