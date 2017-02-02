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
package no.thrums.difference.ri;

import no.thrums.difference.Difference;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author Kristian Myrhaug
 * @since 2016-06-05
 */
public class RiComparatorTest {

    private Helper helper;

    @Before
    public void before() {
        helper = new Helper();
    }

    @Test
    public void should_compare_array() {
        assertDifferencesSize(0, "[[], true, 1, null, 1.1, {}, \"one\"]", "[[], true, 1, null, 1.1, {}, \"one\"]");
        assertDifferencesSize(0, "[[false], false, 2, null, 2.2, {}, \"two\"]", "[[false], false, 2, null, 2.2, {}, \"two\"]");

        assertDifferencesSize(8, "[[], true, 1, null, 1.1, {}, \"one\"]", "[]");
        assertDifferencesSize(11, "[[false, true], false, 2, null, 2.2, {\"1\":1,\"2\":2,\"3\":3}, \"two\"]", "[[true, false], true, 1, null, 1.1, {\"1\":3,\"2\":2,\"3\":1}, \"one\"]");
        assertDifferencesSize(1, "[[], true, 1, null, 1.1, {}, \"one\"]", "1");
        assertDifferencesSize(8, "null", "[[], true, 1, null, 1.1, {}, \"one\"]");
        assertDifferencesSize(8, "[[], true, 1, null, 1.1, {}, \"one\"]", null);
        assertDifferencesSize(1, "1.1", "[[], true, 1, null, 1.1, {}, \"one\"]");
        assertDifferencesSize(1, "[[], true, 1, null, 1.1, {}, \"one\"]", "{}");
        assertDifferencesSize(1, "\"one\"", "[[], true, 1, null, 1.1, {}, \"one\"]");

        assertDifferencesSize(8, "[false]", "[[false], false, 2, null, 2.2, {}, \"two\"]");
        assertDifferencesSize(1, "[[false], false, 2, null, 2.2, {}, \"two\"]", "true");
        assertDifferencesSize(1, "1", "[[false], false, 2, null, 2.2, {}, \"two\"]");
        assertDifferencesSize(9, "[[false], false, 2, null, 2.2, {}, \"two\"]", "null");
        assertDifferencesSize(9, null, "[[false], false, 2, null, 2.2, {}, \"two\"]");
        assertDifferencesSize(1, "[[false], false, 2, null, 2.2, {}, \"two\"]", "1.1");
        assertDifferencesSize(1, "{}", "[[false], false, 2, null, 2.2, {}, \"two\"]");
        assertDifferencesSize(1, "[[false], false, 2, null, 2.2, {}, \"two\"]", "\"one\"");
    }

    @Test
    public void should_compare_boolean() {
        assertDifferencesSize(0, "true", "true");
        assertDifferencesSize(0, "false", "false");

        assertDifferencesSize(1, "true", "[]");
        assertDifferencesSize(1, "false", "true");
        assertDifferencesSize(1, "true", "1");
        assertDifferencesSize(1, "null", "true");
        assertDifferencesSize(1, "true", null);
        assertDifferencesSize(1, "1.1", "true");
        assertDifferencesSize(1, "true", "{}");
        assertDifferencesSize(1, "\"one\"", "true");

        assertDifferencesSize(1, "[false]", "false");
        assertDifferencesSize(1, "false", "true");
        assertDifferencesSize(1, "1", "false");
        assertDifferencesSize(1, "false", "null");
        assertDifferencesSize(1, null, "false");
        assertDifferencesSize(1, "false", "1.1");
        assertDifferencesSize(1, "{}", "false");
        assertDifferencesSize(1, "false", "\"one\"");
    }

    @Test
    public void should_compare_integral() {
        assertDifferencesSize(0, "1", "1");
        assertDifferencesSize(0, "false", "false");

        assertDifferencesSize(1, "1", "[]");
        assertDifferencesSize(1, "false", "1");
        assertDifferencesSize(1, "2", "1");
        assertDifferencesSize(1, "null", "1");
        assertDifferencesSize(1, "1", null);
        assertDifferencesSize(1, "1.1", "1");
        assertDifferencesSize(1, "1", "{}");
        assertDifferencesSize(1, "\"one\"", "1");

        assertDifferencesSize(1, "[2]", "2");
        assertDifferencesSize(1, "2", "true");
        assertDifferencesSize(1, "1", "2");
        assertDifferencesSize(1, "2", "null");
        assertDifferencesSize(1, null, "2");
        assertDifferencesSize(1, "2", "1.1");
        assertDifferencesSize(1, "{}", "2");
        assertDifferencesSize(1, "2", "\"one\"");
    }

    @Test
    public void should_compare_null() {
        assertDifferencesSize(1, "null", "[]");
        assertDifferencesSize(1, "false", "null");
        assertDifferencesSize(1, "null", "1");
        assertDifferencesSize(0, "null", "null");
        assertDifferencesSize(1, "null", null);//null undefined
        assertDifferencesSize(1, "1.1", "null");
        assertDifferencesSize(1, "null", "{}");
        assertDifferencesSize(1, "\"one\"", "null");

        assertDifferencesSize(2, "[null]", "null");
        assertDifferencesSize(1, "null", "true");
        assertDifferencesSize(1, "1", "null");
        assertDifferencesSize(0, "null", "null");
        assertDifferencesSize(1, null, "null");//undefined null
        assertDifferencesSize(1, "null", "1.1");
        assertDifferencesSize(1, "{}", "null");
        assertDifferencesSize(1, "null", "\"one\"");
    }

    @Test
    public void should_compare_undefined() {

        assertDifferencesSize(1, null, "[]");
        assertDifferencesSize(1, "false", null);
        assertDifferencesSize(1, null, "1");
        assertDifferencesSize(1, "null", null);
        assertDifferencesSize(0, null, null);
        assertDifferencesSize(1, "1.1", null);
        assertDifferencesSize(1, null, "{}");
        assertDifferencesSize(1, "\"one\"", null);

        assertDifferencesSize(1, "[]", null);
        assertDifferencesSize(1, null, "true");
        assertDifferencesSize(1, "1", null);
        assertDifferencesSize(1, null, "null");
        assertDifferencesSize(0, null, null);
        assertDifferencesSize(1, null, "1.1");
        assertDifferencesSize(1, "{}", null);
        assertDifferencesSize(1, null, "\"one\"");
    }

    @Test
    public void should_compare_number() {
        assertDifferencesSize(0, "1.1", "1.1");
        assertDifferencesSize(0, "2.2", "2.2");

        assertDifferencesSize(1, "1.1", "[]");
        assertDifferencesSize(1, "false", "1.1");
        assertDifferencesSize(1, "1.1", "1");
        assertDifferencesSize(1, "null", "1.1");
        assertDifferencesSize(1, "1.1", null);
        assertDifferencesSize(1, "2.2", "1.1");
        assertDifferencesSize(1, "1.1", "{}");
        assertDifferencesSize(1, "\"one\"", "1.1");

        assertDifferencesSize(1, "[2.2]", "2.2");
        assertDifferencesSize(1, "2.2", "true");
        assertDifferencesSize(1, "1", "2.2");
        assertDifferencesSize(1, "2.2", "null");
        assertDifferencesSize(1, null, "2.2");
        assertDifferencesSize(1, "2.2", "1.1");
        assertDifferencesSize(1, "{}", "2.2");
        assertDifferencesSize(1, "2.2", "\"one\"");
    }

    @Test
    public void should_compare_object() {
        assertDifferencesSize(0, "{\"1\":1,\"2\":2,\"3\":3}", "{\"2\":2,\"3\":3,\"1\":1}");
        assertDifferencesSize(0,
                "{\"array\":[2],\"boolean\":false,\"integral\":2,\"null\":null,\"number\":2.2,\"object\":{\"array\":[2],\"boolean\":false,\"integral\":2,\"null\":null,\"number\":2.2,\"object\":{},\"string\":\"two\"},\"string\":\"two\"}",
                "{\"array\":[2],\"integral\":2,\"number\":2.2,\"string\":\"two\",\"boolean\":false,\"null\":null,\"object\":{\"boolean\":false,\"null\":null,\"object\":{},\"string\":\"two\",\"array\":[2],\"integral\":2,\"number\":2.2}}");

        assertDifferencesSize(1, "{\"1\":1,\"2\":2,\"3\":3}", "[]");
        assertDifferencesSize(1, "false", "{\"1\":1,\"2\":2,\"3\":3}");
        assertDifferencesSize(1, "{\"1\":1,\"2\":2,\"3\":3}", "1");
        assertDifferencesSize(4, "null", "{\"1\":1,\"2\":2,\"3\":3}");
        assertDifferencesSize(4, "{\"1\":1,\"2\":2,\"3\":3}", null);
        assertDifferencesSize(1, "1.1", "{\"1\":1,\"2\":2,\"3\":3}");
        assertDifferencesSize(20, "{\"1\":1,\"2\":2,\"3\":3}", "{\"array\":[2],\"boolean\":false,\"integral\":2,\"null\":null,\"number\":2.2,\"object\":{\"array\":[2],\"boolean\":false,\"integral\":2,\"null\":null,\"number\":2.2,\"object\":{},\"string\":\"two\"},\"string\":\"two\"}");
        assertDifferencesSize(1, "\"one\"", "{\"1\":1,\"2\":2,\"3\":3}");

        assertDifferencesSize(1,
                "[false]",
                "{\"array\":[2],\"boolean\":false,\"integral\":2,\"null\":null,\"number\":2.2,\"object\":{\"array\":[2],\"boolean\":false,\"integral\":2,\"null\":null,\"number\":2.2,\"object\":{},\"string\":\"two\"},\"string\":\"two\"}");
        assertDifferencesSize(1,
                "{\"array\":[2],\"boolean\":false,\"integral\":2,\"null\":null,\"number\":2.2,\"object\":{\"array\":[2],\"boolean\":false,\"integral\":2,\"null\":null,\"number\":2.2,\"object\":{},\"string\":\"two\"},\"string\":\"two\"}",
                "true");
        assertDifferencesSize(1,
                "1",
                "{\"array\":[2],\"boolean\":false,\"integral\":2,\"null\":null,\"number\":2.2,\"object\":{\"array\":[2],\"boolean\":false,\"integral\":2,\"null\":null,\"number\":2.2,\"object\":{},\"string\":\"two\"},\"string\":\"two\"}");
        assertDifferencesSize(17,
                "{\"array\":[2],\"boolean\":false,\"integral\":2,\"null\":null,\"number\":2.2,\"object\":{\"array\":[2],\"boolean\":false,\"integral\":2,\"null\":null,\"number\":2.2,\"object\":{},\"string\":\"two\"},\"string\":\"two\"}",
                "null");
        assertDifferencesSize(17,
                null,
                "{\"array\":[2],\"boolean\":false,\"integral\":2,\"null\":null,\"number\":2.2,\"object\":{\"array\":[2],\"boolean\":false,\"integral\":2,\"null\":null,\"number\":2.2,\"object\":{},\"string\":\"two\"},\"string\":\"two\"}");
        assertDifferencesSize(1,
                "{\"array\":[2],\"boolean\":false,\"integral\":2,\"null\":null,\"number\":2.2,\"object\":{\"array\":[2],\"boolean\":false,\"integral\":2,\"null\":null,\"number\":2.2,\"object\":{},\"string\":\"two\"},\"string\":\"two\"}",
                "1.1");
        assertDifferencesSize(17,
                "{}",
                "{\"array\":[2],\"boolean\":false,\"integral\":2,\"null\":null,\"number\":2.2,\"object\":{\"array\":[2],\"boolean\":false,\"integral\":2,\"null\":null,\"number\":2.2,\"object\":{},\"string\":\"two\"},\"string\":\"two\"}");
        assertDifferencesSize(1,
                "{\"array\":[2],\"boolean\":false,\"integral\":2,\"null\":null,\"number\":2.2,\"object\":{\"array\":[2],\"boolean\":false,\"integral\":2,\"null\":null,\"number\":2.2,\"object\":{},\"string\":\"two\"},\"string\":\"two\"}",
                "\"one\"");
    }

    @Test
    public void should_compare_string() {
        assertDifferencesSize(0, "\"two\"", "\"two\"");
        assertDifferencesSize(0, "\"three\"", "\"three\"");

        assertDifferencesSize(1, "\"two\"", "[]");
        assertDifferencesSize(1, "false", "\"two\"");
        assertDifferencesSize(1, "\"two\"", "1");
        assertDifferencesSize(1, "null", "\"two\"");
        assertDifferencesSize(1, "\"two\"", null);
        assertDifferencesSize(1, "1.1", "\"two\"");
        assertDifferencesSize(1, "\"two\"", "{}");
        assertDifferencesSize(1, "\"one\"", "\"two\"");

        assertDifferencesSize(1, "[\"three\"]", "\"three\"");
        assertDifferencesSize(1, "\"three\"", "true");
        assertDifferencesSize(1, "1", "\"three\"");
        assertDifferencesSize(1, "\"three\"", "null");
        assertDifferencesSize(1, null, "\"three\"");
        assertDifferencesSize(1, "\"three\"", "1.1");
        assertDifferencesSize(1, "{}", "\"three\"");
        assertDifferencesSize(1, "\"three\"", "\"one\"");
    }

    @Test
    public void should_compare_java_enum() {
        assertDifferencesSize(0, SomeEnum.two, SomeEnum.two);
        assertDifferencesSize(0, SomeEnum.three, SomeEnum.three);

        assertDifferencesSize(1, SomeEnum.two, "[]");
        assertDifferencesSize(1, false, SomeEnum.two);
        assertDifferencesSize(1, SomeEnum.two, 1);
        assertDifferencesSize(1, null, SomeEnum.two);
        assertDifferencesSize(1, SomeEnum.two, null);
        assertDifferencesSize(1, 1.1, SomeEnum.two);
        assertDifferencesSize(1, SomeEnum.two, new LinkedHashMap<>());
        assertDifferencesSize(1, SomeEnum.one, SomeEnum.two);

        assertDifferencesSize(1, Collections.singleton(SomeEnum.three), SomeEnum.three);
        assertDifferencesSize(1, SomeEnum.three, true);
        assertDifferencesSize(1, 1, SomeEnum.three);
        assertDifferencesSize(1, SomeEnum.three, null);
        assertDifferencesSize(1, null, SomeEnum.three);
        assertDifferencesSize(1, SomeEnum.three, 1.1);
        assertDifferencesSize(1, new LinkedHashMap<>(), SomeEnum.three);
        assertDifferencesSize(1, SomeEnum.three, SomeEnum.one);
    }

    private void assertDifferencesSize(int expectedDifferences, String first, String second) {
        List<Difference> differences = helper.compare(first, second);
        try {
            assertEquals(expectedDifferences, differences.size());
        } catch (AssertionError cause) {
            for (Difference difference : differences) {
                String f = !difference.getFirst().isUndefined() ? String.valueOf(difference.getFirst().asValue()) : "undefined";
                String s = !difference.getSecond().isUndefined() ? String.valueOf(difference.getSecond().asValue()) : "undefined";
                System.out.println(String.format("%s: %s %s", difference.getPath(), f, s));
            }
            throw cause;
        }
    }

    private void assertDifferencesSize(int expectedDifferences, Object first, Object second) {
        List<Difference> differences = helper.compare(helper.defined(first), helper.defined(second));
        try {
            assertEquals(expectedDifferences, differences.size());
        } catch (AssertionError cause) {
            for (Difference difference : differences) {
                String f = !difference.getFirst().isUndefined() ? String.valueOf(difference.getFirst().asValue()) : "undefined";
                String s = !difference.getSecond().isUndefined() ? String.valueOf(difference.getSecond().asValue()) : "undefined";
                System.out.println(String.format("%s: %s %s", difference.getPath(), f, s));
            }
            throw cause;
        }
    }

    public enum SomeEnum {
        one,
        two,
        three;
    }
}
