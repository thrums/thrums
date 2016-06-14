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
package no.thrums.validation.engine.keyword;

import no.thrums.validation.engine.keyword.any.*;
import no.thrums.validation.engine.keyword.array.AdditionalItems;
import no.thrums.validation.engine.keyword.array.MaxItems;
import no.thrums.validation.engine.keyword.array.MinItems;
import no.thrums.validation.engine.keyword.array.UniqueItems;
import no.thrums.validation.engine.keyword.number.Maximum;
import no.thrums.validation.engine.keyword.number.Minimum;
import no.thrums.validation.engine.keyword.number.MultipleOf;
import no.thrums.validation.engine.keyword.object.*;
import no.thrums.validation.engine.keyword.string.MaxLength;
import no.thrums.validation.engine.keyword.string.MinLength;
import no.thrums.validation.engine.keyword.string.Pattern;
import no.thrums.validation.engine.keyword.string.format.*;
import no.thrums.validation.keyword.Keyword;

import javax.inject.Named;
import javax.inject.Provider;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Kristian Myrhaug
 * @since 2015-02-24
 */
@Named("no.thrums.validation.engine.KeywordProvider")
public class KeywordProvider implements Provider<List<Keyword>> {

    @Named("no.thrums.validation.engine.Keywords")
    @Override
    public List<Keyword> get() {
        return new LinkedList<>(Arrays.asList(
                new MultipleOf(),
                new Maximum(),
                new Minimum(),
                new MaxLength(),
                new MinLength(),
                new Pattern(),
                new AdditionalItems(),
                new MaxItems(),
                new MinItems(),
                new UniqueItems(),
                new MaxProperties(),
                new MinProperties(),
                new Required(),
                new AdditionalProperties(),
                new Dependencies(),
                new no.thrums.validation.engine.keyword.any.Enum(),
                new Type(),
                new AllOf(),
                new AnyOf(),
                new OneOf(),
                new Not(),
                new DateTime(),
                new Email(),
                new Hostname(),
                new Ipv4(),
                new Ipv4(),
                new Uri()
        ));
    }
}
