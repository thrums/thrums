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
package no.thrums.validation.engine;

import no.thrums.validation.engine.keyword.any.*;
import no.thrums.validation.engine.keyword.object.AdditionalProperties;
import no.thrums.validation.engine.keyword.object.MinProperties;
import no.thrums.validation.Violation;
import no.thrums.validation.engine.keyword.array.AdditionalItems;
import no.thrums.validation.engine.keyword.array.MaxItems;
import no.thrums.validation.engine.keyword.array.MinItems;
import no.thrums.validation.engine.keyword.array.UniqueItems;
import no.thrums.validation.engine.keyword.number.Maximum;
import no.thrums.validation.engine.keyword.number.Minimum;
import no.thrums.validation.engine.keyword.number.MultipleOf;
import no.thrums.validation.engine.keyword.object.Dependencies;
import no.thrums.validation.engine.keyword.object.MaxProperties;
import no.thrums.validation.engine.keyword.object.Required;
import no.thrums.validation.engine.keyword.string.MaxLength;
import no.thrums.validation.engine.keyword.string.MinLength;
import no.thrums.validation.engine.keyword.string.Pattern;
import no.thrums.validation.engine.keyword.string.format.DateTime;
import no.thrums.validation.engine.keyword.string.format.Email;
import no.thrums.validation.engine.keyword.string.format.Hostname;
import no.thrums.validation.engine.keyword.string.format.Ipv4;
import no.thrums.validation.engine.keyword.string.format.Ipv6;
import no.thrums.validation.engine.keyword.string.format.Uri;
import no.thrums.validation.instance.Instance;
import no.thrums.validation.engine.keyword.Helper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import no.thrums.validation.engine.keyword.any.AnyOf;

import java.net.URI;
import java.util.List;

/**
 * @author Kristian Myrhaug
 * @since 2015-02-08
 */
public class ValidationTest {

    private Helper helper;

    @Before
    public void before() {
        helper = new Helper(
                new AllOf(),
                new AnyOf(),
                new no.thrums.validation.engine.keyword.any.Enum(),
                new Not(),
                new OneOf(),
                new Type(),
                new AdditionalItems(),
                new MaxItems(),
                new MinItems(),
                new UniqueItems(),
                new Maximum(),
                new Minimum(),
                new MultipleOf(),
                new AdditionalProperties(),
                new Dependencies(),
                new MaxProperties(),
                new MinProperties(),
                new Required(),
                new MaxLength(),
                new MinLength(),
                new Pattern(),
                new DateTime(),
                new Email(),
                new Hostname(),
                new Ipv4(),
                new Ipv6(),
                new Uri()
        );
    }

    @Test
    public void test(){
        Instance schema = helper.instance(URI.create("classpath:/feature-collection-schema.json"));
        Instance instance = helper.instance(URI.create("classpath:/NVDB_Skiltpunkt_95.geo.json"));

        List<Violation> violations = helper.getValidator().validate(schema, instance);
        for (Violation violation : violations) {
            System.out.println(violation.getMessage());
        }
        Assert.assertTrue(violations.isEmpty());

    }
}
