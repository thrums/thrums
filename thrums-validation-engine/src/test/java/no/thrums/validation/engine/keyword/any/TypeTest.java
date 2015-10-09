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
package no.thrums.validation.engine.keyword.any;

import no.thrums.validation.Violation;
import no.thrums.validation.engine.keyword.object.AdditionalProperties;
import no.thrums.validation.engine.keyword.object.Required;
import no.thrums.validation.instance.Instance;
import no.thrums.validation.engine.keyword.Helper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author Kristian Myrhaug
 * @since 2014-11-16
 */
public class TypeTest {

    private Helper helper;

    @Before
    public void before() {
        helper = new Helper(
                new Type()
        );
    }

    @Test
    public void that_type_nihili_validates() {
        assertTrue(helper.instance("{\"type\":null}"), Arrays.asList(), false, null, 1, 1.0, new LinkedHashMap(), "one");
    }

    @Test
    public void that_type_all_validates() {
        assertTrue(helper.instance("{\"type\":[\"array\", \"boolean\", \"integer\", \"null\", \"number\", \"object\", \"string\"]}"), Arrays.asList(), false, null, 1, 1.0, new LinkedHashMap(), "one");
    }

    @Test
    public void that_type_number_not_present_validates_and_invalidates() {
        Instance schema = helper.instance("{\"type\":[\"array\", \"boolean\", \"integer\", \"null\", \"object\", \"string\"]}");
        assertTrue(schema, Arrays.asList(), false, null, 1, new LinkedHashMap(), "one");
        assertFalse(schema, 1.0);
    }

    @Test
    public void that_type_array_validates_and_invalidates() {
        Instance schema = helper.instance("{\"type\":\"array\"}");
        assertTrue(schema, Arrays.asList());
        assertFalse(schema, false, null, 1, 1.0, new LinkedHashMap(), "one");
    }

    @Test
    public void that_type_boolean_validates_and_invalidates() {
        Instance schema = helper.instance("{\"type\":\"boolean\"}");
        assertTrue(schema, false);
        assertFalse(schema, Arrays.asList(), null, 1, 1.0, new LinkedHashMap(), "one");
    }

    @Test
    public void that_type_integer_validates_and_invalidates() {
        Instance schema = helper.instance("{\"type\":\"integer\"}");
        assertTrue(schema, 1);
        assertFalse(schema, Arrays.asList(), false, null, 1.0, new LinkedHashMap(), "one");
    }

    @Test
    public void that_type_null_validates_and_invalidates() {
        Instance schema = helper.instance("{\"type\":\"null\"}");
        assertTrue(schema, (Object)null);
        assertFalse(schema, Arrays.asList(), false, 1, 1.0, new LinkedHashMap(), "one");
    }

    @Test
    public void that_type_number_validates_and_invalidates() {
        Instance schema = helper.instance("{\"type\":\"number\"}");
        assertTrue(schema, 1, 1.0);
        assertFalse(schema, Arrays.asList(), false, null, new LinkedHashMap(), "one");
    }

    @Test
    public void that_type_object_validates_and_invalidates() {
        Instance schema = helper.instance("{\"type\":\"object\"}");
        assertTrue(schema, new LinkedHashMap());
        assertFalse(schema, Arrays.asList(), false, null, 1, 1.0, "one");
    }

    @Test
    public void that_type_string_validates_and_invalidates() {
        Instance schema = helper.instance("{\"type\":\"string\"}");
        assertTrue(schema, "one");
        assertFalse(schema, Arrays.asList(), false, null, 1, 1.0, new LinkedHashMap());
    }

    @Test
    public void that_null_value_properties_in_beans_are_reported_as_not_present_rather_than_null() {
        Instance schema = helper.instance("{\"type\":\"object\",\"required\":[\"id\"],\"properties\":{\"id\":{\"type\":\"integer\"}}}");
        assertTrue(schema, helper.defined(new IdentifiableInstance(null)));
    }

    public static class IdentifiableInstance {

        private Long id;

        public IdentifiableInstance(Long id) {
            this.id = id;
        }

        public Long getId() {
            return id;
        }
    }

    private void assertTrue(Instance schema, Object... objects) {
        for (Object object : objects) {
            List<Violation> violations = helper.validate(schema, helper.defined(object));
            for (Violation violation : violations) {
                System.out.println(violation.getMessage());
            }
            Assert.assertTrue(violations.isEmpty());
        }
    }

    private void assertFalse(Instance schema, Object... objects) {
        for (Object object : objects) {
            Assert.assertFalse(helper.validate(schema, helper.defined(object)).isEmpty());
        }
    }
}
