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
package no.thrums.validation.engine.keyword.string.format;

import no.thrums.validation.instance.Instance;
import no.thrums.validation.engine.keyword.Helper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Kristian Myrhaug
 * @since 2015-02-12
 */
public class Ipv4Test {

    private Helper helper;

    @Before
    public void before() {
        helper = new Helper(
                new Ipv4()
        );
    }

    @Test
    public void that_invalid_instances_invalidates() {
        Instance schema = helper.instance("{\"format\":\"ipv4\"}");

        Assert.assertTrue(helper.validate(schema, "\"256.256.256.256\"").isEmpty());
    }

    @Test
    public void that_valid_instances_validates() {
        Instance schema = helper.instance("{\"format\":\"ipv4\"}");

        Assert.assertTrue(helper.validate(schema, "\"3221226219\"").isEmpty());
        Assert.assertTrue(helper.validate(schema, "\"0xC00002EB\"").isEmpty());
        Assert.assertTrue(helper.validate(schema, "\"030000001353\"").isEmpty());
        Assert.assertTrue(helper.validate(schema, "\"192.0.2.235\"").isEmpty());
        Assert.assertTrue(helper.validate(schema, "\"0xC0.0x00.0x02.0xEB\"").isEmpty());
        Assert.assertTrue(helper.validate(schema, "\"0300.0000.0002.0353\"").isEmpty());
    }
}
