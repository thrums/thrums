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
package no.thrums.validation.engine.keyword.object;

import no.thrums.instance.Instance;
import no.thrums.validation.keyword.Keyword;
import no.thrums.validation.keyword.KeywordValidator;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.function.Predicate;

import static java.util.Objects.nonNull;

/**
 * @author Kristian Myrhaug
 * @since 2016-05-08
 */
public class Id implements Keyword {

    @Override
    public KeywordValidator getKeywordValidator(Instance schema) {
        Instance id = schema.get("id");
        if (id.isPresent()) {
            if (id.isString()) {
                try {
                    String value = id.asString();
                    if (isEmpty(value)) {
                        throw new IllegalArgumentException("May not be empty: id");
                    }
                    URI uri = new URI(id.asString());
                    if (!uri.normalize().equals(uri)) {
                        throw new IllegalArgumentException("Must be normalized: id");
                    }
                    String fragment = uri.getFragment();
                    if (isEmptyFragment(fragment)) {
                        throw new IllegalArgumentException("Should not be an empty fragment: id");
                    }

                } catch (URISyntaxException cause) {
                    throw new IllegalArgumentException("Must be an valid URI: id", cause);
                }
            } else {
                throw new IllegalArgumentException("Must be a string: id");
            }
        }
        return null;
    }

    private boolean isEmpty(CharSequence charSequence) {
        return !atLeastOneOccurance(charSequence, character -> !Character.isWhitespace(character));
    }

    private boolean isEmptyFragment(String fragment) {
        if (nonNull(fragment)) {
            if (fragment.startsWith("#")) {
                fragment = fragment.substring(1);
            }
        }
        return isEmpty(fragment);
    }

    private boolean atLeastOneOccurance(CharSequence charSequence, Predicate<Character> predicate) {
        if (nonNull(charSequence)) {
            for (int index = 0; index < charSequence.length(); index++) {
                if (predicate.test(charSequence.charAt(index))) {
                    return true;
                }
            }
        }
        return false;
    }
}
