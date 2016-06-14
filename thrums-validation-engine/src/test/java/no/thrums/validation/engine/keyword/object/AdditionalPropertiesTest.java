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

import no.thrums.validation.engine.keyword.any.*;
import no.thrums.instance.Instance;
import no.thrums.validation.engine.keyword.Helper;
import no.thrums.validation.engine.keyword.Performer;
import no.thrums.validation.engine.keyword.Utils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Kristian Myrhaug
 * @since 2014-10-30
 */
public class AdditionalPropertiesTest {

    private Helper helper;

    @Before
    public void before() {
        helper = new Helper(
                new Type(),
                new no.thrums.validation.engine.keyword.any.Enum(),
                new AdditionalProperties()
        );
    }

    @Test
    public void test_that_additionalProperties_keyword() {

        Assert.assertTrue(Utils.isThrowsRuntimeException(new Performer() {
            @Override
            public void perform() {
                helper.validate("{\"additionalProperties\":[]}", "null");
            }
        }));
        Assert.assertFalse(Utils.isThrowsRuntimeException(new Performer() {
            @Override
            public void perform() {
                helper.validate("{\"additionalProperties\":true}", "null");
            }
        }));
        Assert.assertTrue(Utils.isThrowsRuntimeException(new Performer() {
            @Override
            public void perform() {
                helper.validate("{\"additionalProperties\":1}", "null");
            }
        }));
        Assert.assertTrue(Utils.isThrowsRuntimeException(new Performer() {
            @Override
            public void perform() {
                helper.validate("{\"additionalProperties\":\"one\"}", "null");
            }
        }));
        Assert.assertFalse(Utils.isThrowsRuntimeException(new Performer() {
            @Override
            public void perform() {
                helper.validate("{\"additionalProperties\":null}", "null");
            }
        }));
        Assert.assertFalse(Utils.isThrowsRuntimeException(new Performer() {
            @Override
            public void perform() {
                helper.validate("{\"additionalProperties\":{}}", "null");
            }
        }));

        Assert.assertTrue(Utils.isThrowsRuntimeException(new Performer() {
            @Override
            public void perform() {
                helper.validate("{\"additionalProperties\":false, \"properties\":true}", "null");
            }
        }));

        Assert.assertTrue(Utils.isThrowsRuntimeException(new Performer() {
            @Override
            public void perform() {
                helper.validate("{\"additionalProperties\":false, \"patternProperties\":true}", "null");
            }
        }));

    }

    @Test
    public void either_keyword_are_absent_validates() {
        Instance schema = helper.instance("{}");

        Assert.assertTrue(helper.validate(schema, "null").isEmpty());
        Assert.assertTrue(helper.validate(schema, "{\"key1\": 1, \"key2\": 2, \"3key\": 3, \"4key\": 4}").isEmpty());
    }

    @Test
    public void additionalProperties_true_and_other_keywords_absent_validates() {
        Instance schema = helper.instance("{\"additionalProperties\": true, \"properties\": null, \"patternProperties\": null}");

        Assert.assertTrue(helper.validate(schema, "null").isEmpty());
        Assert.assertTrue(helper.validate(schema, "{\"key1\": 1, \"key2\": 2, \"3key\": 3, \"4key\": 4}").isEmpty());
    }

    @Test
    public void additionalProperties_false_and_other_keywords_absent_invalidates() {
        Instance schema = helper.instance("{\"additionalProperties\": false, \"properties\": null, \"patternProperties\": null}");

        Assert.assertTrue(helper.validate(schema, "null").isEmpty());
        Assert.assertFalse(helper.validate(schema, "{\"key1\": 1, \"key2\": 2, \"3key\": 3, \"4key\": 4}").isEmpty());
    }

    @Test
    public void additionalProperties_false_and_patternProperties_matches_validates() {
        Instance schema = helper.instance("{\"additionalProperties\": false, \"properties\": null, \"patternProperties\": {\"key\":{}}}");

        Assert.assertTrue(helper.validate(schema, "null").isEmpty());
        Assert.assertTrue(helper.validate(schema, "{\"key1\": 1, \"key2\": 2, \"3key\": 3, \"4key\": 4}").isEmpty());
    }

    @Test
    public void additionalProperties_false_and_properties_matches_validates() {
        Instance schema = helper.instance("{\"additionalProperties\": false, \"properties\": {\"key1\": {}, \"key2\": {}, \"3key\": {}, \"4key\": {}}, \"patternProperties\": null}");

        Assert.assertTrue(helper.validate(schema, "null").isEmpty());
        Assert.assertTrue(helper.validate(schema, "{\"key1\": 1, \"key2\": 2, \"3key\": 3, \"4key\": 4}").isEmpty());
    }

    @Test
    public void additionalProperties_false_and_properties_matches_some_and_pattern_properties_others_validates() {
        Instance schema = helper.instance("{\"additionalProperties\": false, \"properties\": {\"key1\": {}, \"key2\": {}}, \"patternProperties\": {\"[0-9]key\":{}}}");

        Assert.assertTrue(helper.validate(schema, "null").isEmpty());
        Assert.assertTrue(helper.validate(schema, "{\"key1\": 1, \"key2\": 2, \"3key\": 3, \"4key\": 4}").isEmpty());
    }

    @Test
    public void additionalProperties_object_and_other_keywords_absent_invalidates() {
        Instance schema = helper.instance("{\"additionalProperties\": {\"enum\":[{\"key1\":1}]}, \"properties\": null, \"patternProperties\": null}");

        Assert.assertTrue(helper.validate(schema, "null").isEmpty());
        Assert.assertFalse(helper.validate(schema, "{\"key1\": 1, \"key2\": 2, \"3key\": 3, \"4key\": 4}").isEmpty());
        Assert.assertTrue(helper.validate(schema, "{\"something\":{\"key1\": 1}}").isEmpty());
    }
}
