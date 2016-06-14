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
package no.thrums.validation.engine.keyword.object;

import no.thrums.validation.engine.keyword.Performer;
import no.thrums.validation.engine.keyword.Utils;
import no.thrums.instance.Instance;
import no.thrums.validation.engine.keyword.Helper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Kristian Myrhaug
 * @since 2015-02-06
 */
public class DependenciesTest {

    private Helper helper;

    @Before
    public void before() {
        helper = new Helper(
                new no.thrums.validation.engine.keyword.any.Enum(),
                new Dependencies()
        );
    }

    @Test
    public void test_that_dependencies_keyword() {

        Assert.assertTrue(Utils.isThrowsRuntimeException(new Performer() {
            @Override
            public void perform() {
                helper.validate("{\"dependencies\":[]}", "null");
            }
        }));
        Assert.assertTrue(Utils.isThrowsRuntimeException(new Performer() {
            @Override
            public void perform() {
                helper.validate("{\"dependencies\":true}", "null");
            }
        }));
        Assert.assertTrue(Utils.isThrowsRuntimeException(new Performer() {
            @Override
            public void perform() {
                helper.validate("{\"dependencies\":1}", "null");
            }
        }));
        Assert.assertTrue(Utils.isThrowsRuntimeException(new Performer() {
            @Override
            public void perform() {
                helper.validate("{\"dependencies\":\"one\"}", "null");
            }
        }));
        Assert.assertFalse(Utils.isThrowsRuntimeException(new Performer() {
            @Override
            public void perform() {
                helper.validate("{\"dependencies\":null}", "null");
            }
        }));
        Assert.assertFalse(Utils.isThrowsRuntimeException(new Performer() {
            @Override
            public void perform() {
                helper.validate("{\"dependencies\":{}}", "null");
            }
        }));
    }

    @Test
    public void test_that_keyword_null_validates() {
        Instance schema = helper.instance("{\"dependencies\":null}");

        Assert.assertTrue(helper.validate(schema, "null").isEmpty());
        Assert.assertTrue(helper.validate(schema, "1").isEmpty());
        Assert.assertTrue(helper.validate(schema, "\"two\"").isEmpty());
        Assert.assertTrue(helper.validate(schema, "{\"key\":null}").isEmpty());
        Assert.assertTrue(helper.validate(schema, "{\"key\":1}").isEmpty());
        Assert.assertTrue(helper.validate(schema, "{\"key\":\"two\"}").isEmpty());
    }

    @Test
    public void test_that_keyword_empty_validates() {
        Instance schema = helper.instance("{\"dependencies\":{}}");

        Assert.assertTrue(helper.validate(schema, "null").isEmpty());
        Assert.assertTrue(helper.validate(schema, "1").isEmpty());
        Assert.assertTrue(helper.validate(schema, "\"two\"").isEmpty());
        Assert.assertTrue(helper.validate(schema, "{\"key\":null}").isEmpty());
        Assert.assertTrue(helper.validate(schema, "{\"key\":1}").isEmpty());
        Assert.assertTrue(helper.validate(schema, "{\"key\":\"two\"}").isEmpty());
    }

    @Test
    public void test_that_keyword_property_dependency() {
        Instance schema = helper.instance("{\"dependencies\": {\"c\": [\"a\", \"b\"], \"a\": \"b\"}}");

        Assert.assertFalse(helper.validate(schema, "{\"a\":\"value\"}").isEmpty());
        Assert.assertTrue(helper.validate(schema, "{\"b\":\"value\"}").isEmpty());
        Assert.assertFalse(helper.validate(schema, "{\"c\":\"value\"}").isEmpty());


        Assert.assertTrue(helper.validate(schema, "{\"a\":\"value\",\"b\":\"value\",\"c\":\"value\"}").isEmpty());
    }

    @Test
    public void test_that_keyword_schema_dependency() {
        Instance schema = helper.instance("{\"dependencies\": {\"c\": {\"enum\": [\"b\", \"a\"]}, \"a\": {\"enum\": [\"b\"]}}}");

        Assert.assertFalse(helper.validate(schema, "{\"c\":\"c\"}").isEmpty());
        Assert.assertTrue(helper.validate(schema, "{\"c\":\"a\"}").isEmpty());
        Assert.assertTrue(helper.validate(schema, "{\"c\":\"b\"}").isEmpty());

        Assert.assertFalse(helper.validate(schema, "{\"a\":\"a\"}").isEmpty());
        Assert.assertTrue(helper.validate(schema, "{\"a\":\"b\"}").isEmpty());
        Assert.assertFalse(helper.validate(schema, "{\"a\":\"c\"}").isEmpty());
    }
}
