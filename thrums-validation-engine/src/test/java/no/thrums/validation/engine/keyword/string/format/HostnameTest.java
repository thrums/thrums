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
package no.thrums.validation.engine.keyword.string.format;

import no.thrums.instance.Instance;
import no.thrums.validation.engine.keyword.Helper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Kristian Myrhaug
 * @since 2015-02-12
 */
public class HostnameTest {

    private Helper helper;

    @Before
    public void before() {
        helper = new Helper(
                new Hostname()
        );
    }

    @Test
    public void that_invalid_instances_invalidates() {
        Instance schema = helper.instance("{\"format\":\"hostname\"}");

        Assert.assertFalse(helper.validate(schema, "\"-a.b.c\"").isEmpty());
        Assert.assertFalse(helper.validate(schema, "\"abc.d-.e\"").isEmpty());
        Assert.assertFalse(helper.validate(schema, "\"a.*.e\"").isEmpty());
        Assert.assertFalse(helper.validate(schema, "\"a.b.cd?e\"").isEmpty());
        Assert.assertFalse(helper.validate(schema, "\"abc.123.d,ef\"").isEmpty());
    }

    @Test
    public void that_valid_instances_validates() {
        Instance schema = helper.instance("{\"format\":\"hostname\"}");

        Assert.assertTrue(helper.validate(schema, "\"a.b.c\"").isEmpty());
        Assert.assertTrue(helper.validate(schema, "\"abc.d-1.e\"").isEmpty());
        Assert.assertTrue(helper.validate(schema, "\"a.1---2.e\"").isEmpty());
        Assert.assertTrue(helper.validate(schema, "\"1a.b.cde0\"").isEmpty());
        Assert.assertTrue(helper.validate(schema, "\"abc.123.def\"").isEmpty());
    }
}
