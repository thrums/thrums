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

import no.thrums.validation.engine.keyword.Helper;
import no.thrums.validation.instance.Instance;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Kristian Myrhaug
 * @since 2015-02-12
 */
public class Ipv6Test {

    private Helper helper;

    @Before
    public void before() {
        helper = new Helper(
                new Ipv6()
        );
    }

    @Test
    public void that_invalid_instances_invalidates() {
        Instance schema = helper.instance("{\"format\":\"ipv6\"}");

        Assert.assertFalse(helper.validate(schema, "\"FFFFF:2233:4455:6677:8899:aAbB:CcDd:eEfF\"").isEmpty());

    }

    @Test
    public void that_valid_instances_validates() {
        Instance schema = helper.instance("{\"format\":\"ipv6\"}");

        Assert.assertTrue(helper.validate(schema, "\"0011:2233:4455:6677:8899:aAbB:CcDd:eEfF\"").isEmpty());
    }
}
