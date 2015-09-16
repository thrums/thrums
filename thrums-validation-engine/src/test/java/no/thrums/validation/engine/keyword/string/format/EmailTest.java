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
public class EmailTest {

    private Helper helper;

    @Before
    public void before() {
        helper = new Helper(
                new Email()
        );
    }

    @Test
    public void that_invalid_instances_invalidates() {
        Instance schema = helper.instance("{\"format\":\"email\"}");

        Assert.assertFalse(helper.validate(schema, "\"Abc.example.com\"").isEmpty());
        Assert.assertFalse(helper.validate(schema, "\"A@b@c@example.com\"").isEmpty());
        Assert.assertFalse(helper.validate(schema, "\"a\"b(c)d,e:f;g<h>i[j\\k]l@example.com\"").isEmpty());
        Assert.assertFalse(helper.validate(schema, "\"just\"not\"right@example.com\"").isEmpty());
        Assert.assertFalse(helper.validate(schema, "\"this is\"not\\allowed@example.com\"").isEmpty());
        Assert.assertFalse(helper.validate(schema, "\"this still\\\"not\\\\allowed@example.com\"").isEmpty());
        Assert.assertFalse(helper.validate(schema, "\"john..doe@example.com\"").isEmpty());
        Assert.assertFalse(helper.validate(schema, "\"john.doe@example..com\"").isEmpty());
        Assert.assertFalse(helper.validate(schema, "\" niceandsimple@example.com\"").isEmpty());
        Assert.assertFalse(helper.validate(schema, "\"niceandsimple@example.com \"").isEmpty());
    }

    @Test
    public void that_valid_instances_validates() {
        Instance schema = helper.instance("{\"format\":\"email\"}");

        Assert.assertTrue(helper.validate(schema, "\"niceandsimple@example.com\"").isEmpty());
        Assert.assertTrue(helper.validate(schema, "\"very.common@example.com\"").isEmpty());
        Assert.assertTrue(helper.validate(schema, "\"a.little.lengthy.but.fine@dept.example.com\"").isEmpty());
        Assert.assertTrue(helper.validate(schema, "\"disposable.style.email.with+symbol@example.com\"").isEmpty());
        Assert.assertTrue(helper.validate(schema, "\"other.email-with-dash@example.com\"").isEmpty());
        Assert.assertTrue(helper.validate(schema, "\"\\\"much.more unusual\\\"@example.com\"").isEmpty());
        Assert.assertTrue(helper.validate(schema, "\"\\\"very.unusual.@.unusual.com\\\"@example.com\"").isEmpty());
        Assert.assertTrue(helper.validate(schema, "\"\\\"very.(),:;<>[]\\\\\\\".VERY.\\\\\\\"very@\\\\\\\\ \\\\\\\"very\\\\\\\".unusual\\\"@strange.example.com\"").isEmpty());
        Assert.assertTrue(helper.validate(schema, "\"admin@mailserver1\"").isEmpty());
        Assert.assertTrue(helper.validate(schema, "\"!#$%&'*+-/=?^_`{}|~@example.org\"").isEmpty());
        Assert.assertTrue(helper.validate(schema, "\"\\\"()<>[]:,;@\\\\\\\\\\\\\\\"!#$%&'*+-/=?^_`{}| ~.a\\\"@example.org\"").isEmpty());
        Assert.assertTrue(helper.validate(schema, "\"\\\" \\\"@example.org\"").isEmpty());
        Assert.assertTrue(helper.validate(schema, "\"üñîçøðé@example.com\"").isEmpty());
        Assert.assertTrue(helper.validate(schema, "\"üñîçøðé@üñîçøðé.com\"").isEmpty());
    }
}
