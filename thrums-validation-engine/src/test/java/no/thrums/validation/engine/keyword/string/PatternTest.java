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

import no.thrums.validation.engine.keyword.Performer;
import no.thrums.instance.Instance;
import no.thrums.validation.engine.keyword.Helper;
import no.thrums.validation.engine.keyword.Utils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Kristian Myrhaug
 * @since 2014-10-18
 */
public class PatternTest {

    private Helper helper;

    @Before
    public void before() {
        helper = new Helper(
                new Pattern()
        );
    }

    @Test
    public void that_pattern_keyword(){
        Assert.assertTrue(Utils.isThrowsRuntimeException(new Performer() {
            @Override
            public void perform() {
                helper.validate("{\"pattern\":[]}", "null");
            }
        }));
        Assert.assertTrue(Utils.isThrowsRuntimeException(new Performer() {
            @Override
            public void perform() {
                helper.validate("{\"pattern\":true}", "null");
            }
        }));
        Assert.assertTrue(Utils.isThrowsRuntimeException(new Performer() {
            @Override
            public void perform() {
                helper.validate("{\"pattern\":1}", "null");
            }
        }));
        Assert.assertFalse(Utils.isThrowsRuntimeException(new Performer() {
            @Override
            public void perform() {
                helper.validate("{\"pattern\":\"one\"}", "null");
            }
        }));
        Assert.assertTrue(Utils.isThrowsRuntimeException(new Performer() {
            @Override
            public void perform() {
                helper.validate("{\"pattern\":\"(\"}", "null");
            }
        }));
        Assert.assertFalse(Utils.isThrowsRuntimeException(new Performer() {
            @Override
            public void perform() {
                helper.validate("{\"pattern\":null}", "null");
            }
        }));
        Assert.assertTrue(Utils.isThrowsRuntimeException(new Performer() {
            @Override
            public void perform() {
                helper.validate("{\"pattern\":{}}", "null");
            }
        }));
    }

    @Test
    public void test_non_anchored_pattern() {
        Instance schema = helper.instance("{\"pattern\":\"bcd\"}");

        Assert.assertTrue(helper.validate(schema, "\"abcde\"").isEmpty());
        Assert.assertTrue(helper.validate(schema, "\"bcde\"").isEmpty());
        Assert.assertTrue(helper.validate(schema, "\"abcd\"").isEmpty());
        Assert.assertTrue(helper.validate(schema, "\"bcd\"").isEmpty());

        Assert.assertFalse(helper.validate(schema, "\"cde\"").isEmpty());
    }

    @Test
    public void pattern_is_null_validates() {
        Instance schema = helper.instance("{\"pattern\":null}");

        Assert.assertTrue(helper.validate(schema, "\"abcde\"").isEmpty());
        Assert.assertTrue(helper.validate(schema, "\"bcde\"").isEmpty());
        Assert.assertTrue(helper.validate(schema, "\"abcd\"").isEmpty());
        Assert.assertTrue(helper.validate(schema, "\"bcd\"").isEmpty());
        Assert.assertTrue(helper.validate(schema, "\"cde\"").isEmpty());
        Assert.assertTrue(helper.validate(schema, "null").isEmpty());
    }
}
