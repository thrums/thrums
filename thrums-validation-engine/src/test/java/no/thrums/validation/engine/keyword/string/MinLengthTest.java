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
package no.thrums.validation.engine.keyword.string;

import no.thrums.validation.engine.keyword.Utils;
import no.thrums.validation.engine.keyword.Performer;
import no.thrums.instance.Instance;
import no.thrums.validation.engine.keyword.Helper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Kristian Myrhaug
 * @since 2014-10-17
 */
public class MinLengthTest {

    private Helper helper;

    @Before
    public void before() {
        helper = new Helper(
                new MinLength()
        );
    }

    @Test
    public void that_minLength_keyword(){
        Assert.assertTrue(Utils.isThrowsRuntimeException(new Performer() {
            @Override
            public void perform() {
                helper.validate("{\"minLength\":[]}", "null");
            }
        }));
        Assert.assertTrue(Utils.isThrowsRuntimeException(new Performer() {
            @Override
            public void perform() {
                helper.validate("{\"minLength\":true}", "null");
            }
        }));
        Assert.assertFalse(Utils.isThrowsRuntimeException(new Performer() {
            @Override
            public void perform() {
                helper.validate("{\"minLength\":1}", "null");
            }
        }));
        Assert.assertTrue(Utils.isThrowsRuntimeException(new Performer() {
            @Override
            public void perform() {
                helper.validate("{\"minLength\":\"one\"}", "null");
            }
        }));
        Assert.assertFalse(Utils.isThrowsRuntimeException(new Performer() {
            @Override
            public void perform() {
                helper.validate("{\"minLength\":null}", "null");
            }
        }));
        Assert.assertTrue(Utils.isThrowsRuntimeException(new Performer() {
            @Override
            public void perform() {
                helper.validate("{\"minLength\":{}}", "null");
            }
        }));
    }

    @Test
    public void testIntegerMin() {
        Instance schema = helper.instance("{\"minLength\":1}");
        assertTrue(schema);
        assertFalse(schema);
    }

    @Test
    public void testFloatMin() {
        Instance schema = helper.instance("{\"minLength\":0.9}");
        assertTrue(schema);
        assertFalse(schema);
    }

    private void assertTrue(Instance schema) {
        Assert.assertTrue(helper.validate(schema, "\"1\"").isEmpty());
        Assert.assertTrue(helper.validate(schema, "\"12\"").isEmpty());
    }

    private void assertFalse(Instance schema) {
        Assert.assertFalse(helper.validate(schema, "\"\"").isEmpty());
    }
}
