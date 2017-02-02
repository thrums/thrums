/**
 Copyright 2014-2017 Kristian Myrhaug

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
import no.thrums.instance.Instance;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Kristian Myrhaug
 * @since 2015-02-12
 */
public class UriTest {

    private Helper helper;

    @Before
    public void before() {
        helper = new Helper(
                new Uri()
        );
    }

    @Test
    public void that_invalid_instances_invalidates() {
        Instance schema = helper.instance("{\"format\":\"uri\"}");

        Assert.assertFalse(helper.validate(schema, "\"? \"").isEmpty());

    }

    @Test
    public void that_valid_instances_validates() {
        Instance schema = helper.instance("{\"format\":\"email\"}");

        Assert.assertTrue(helper.validate(schema, "\"http://foo.com/blah_blah\"").isEmpty());
        Assert.assertTrue(helper.validate(schema, "\"http://foo.com/blah_blah/\"").isEmpty());
        Assert.assertTrue(helper.validate(schema, "\"http://foo.com/blah_blah_(wikipedia)\"").isEmpty());
        Assert.assertTrue(helper.validate(schema, "\"http://foo.com/blah_blah_(wikipedia)_(again)\"").isEmpty());
        Assert.assertTrue(helper.validate(schema, "\"http://www.example.com/wpstyle/?p=364\"").isEmpty());
        Assert.assertTrue(helper.validate(schema, "\"https://www.example.com/foo/?bar=baz&inga=42&quux\"").isEmpty());
        Assert.assertTrue(helper.validate(schema, "\"http://✪df.ws/123\"").isEmpty());
        Assert.assertTrue(helper.validate(schema, "\"http://userid:password@example.com:8080\"").isEmpty());
        Assert.assertTrue(helper.validate(schema, "\"http://userid:password@example.com:8080/\"").isEmpty());
        Assert.assertTrue(helper.validate(schema, "\"http://userid@example.com\"").isEmpty());
        Assert.assertTrue(helper.validate(schema, "\"http://userid@example.com/\"").isEmpty());
        Assert.assertTrue(helper.validate(schema, "\"http://userid@example.com:8080\"").isEmpty());
        Assert.assertTrue(helper.validate(schema, "\"http://userid@example.com:8080/\"").isEmpty());
        Assert.assertTrue(helper.validate(schema, "\"http://userid:password@example.com\"").isEmpty());
        Assert.assertTrue(helper.validate(schema, "\"http://userid:password@example.com/\"").isEmpty());
        Assert.assertTrue(helper.validate(schema, "\"http://142.42.1.1/\"").isEmpty());
        Assert.assertTrue(helper.validate(schema, "\"http://142.42.1.1:8080/\"").isEmpty());
        Assert.assertTrue(helper.validate(schema, "\"http://➡.ws/䨹\"").isEmpty());
        Assert.assertTrue(helper.validate(schema, "\"http://⌘.ws\"").isEmpty());
        Assert.assertTrue(helper.validate(schema, "\"http://⌘.ws/\"").isEmpty());
        Assert.assertTrue(helper.validate(schema, "\"http://foo.com/blah_(wikipedia)#cite-1\"").isEmpty());
        Assert.assertTrue(helper.validate(schema, "\"http://foo.com/blah_(wikipedia)_blah#cite-1\"").isEmpty());
        Assert.assertTrue(helper.validate(schema, "\"http://foo.com/unicode_(✪)_in_parens\"").isEmpty());
        Assert.assertTrue(helper.validate(schema, "\"http://foo.com/(something)?after=parens\"").isEmpty());
        Assert.assertTrue(helper.validate(schema, "\"http://☺.damowmow.com/\"").isEmpty());
        Assert.assertTrue(helper.validate(schema, "\"http://code.google.com/events/#&product=browser\"").isEmpty());
        Assert.assertTrue(helper.validate(schema, "\"http://j.mp\"").isEmpty());
        Assert.assertTrue(helper.validate(schema, "\"ftp://foo.bar/baz\"").isEmpty());
        Assert.assertTrue(helper.validate(schema, "\"http://foo.bar/?q=Test%20URL-encoded%20stuff\"").isEmpty());
        Assert.assertTrue(helper.validate(schema, "\"http://مثال.إختبار\"").isEmpty());
        Assert.assertTrue(helper.validate(schema, "\"http://例子.测试\"").isEmpty());
        Assert.assertTrue(helper.validate(schema, "\"http://उदाहरण.परीक्षा\"").isEmpty());
        Assert.assertTrue(helper.validate(schema, "\"http://-.~_!$&'()*+,;=:%40:80%2f::::::@example.com\"").isEmpty());
        Assert.assertTrue(helper.validate(schema, "\"http://1337.net\"").isEmpty());
        Assert.assertTrue(helper.validate(schema, "\"http://a.b-c.de\"").isEmpty());
        Assert.assertTrue(helper.validate(schema, "\"http://223.255.255.254\"").isEmpty());
    }
}
