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

import no.thrums.validation.keyword.KeywordValidatorContext;
import no.thrums.instance.Instance;
import no.thrums.validation.keyword.Keyword;
import no.thrums.validation.keyword.KeywordValidator;

import java.util.regex.Pattern;

/**
 * @author Kristian Myrhaug
 * @since 2015-02-11
 */
public class DateTime implements Keyword {

    private static Pattern DATE_TIME_PATTERN = Pattern.compile("^([\\+-]?\\d{4}(?!\\d{2}\\b))((-?)((0[1-9]|1[0-2])(\\3([12]\\d|0[1-9]|3[01]))?|W([0-4]\\d|5[0-2])(-?[1-7])?|(00[1-9]|0[1-9]\\d|[12]\\d{2}|3([0-5]\\d|6[1-6])))([T\\s]((([01]\\d|2[0-3])((:?)[0-5]\\d)?|24\\:?00)([\\.,]\\d+(?!:))?)?(\\17[0-5]\\d([\\.,]\\d+)?)?([zZ]|([\\+-])([01]\\d|2[0-3]):?([0-5]\\d)?)?)?)?$");



    @Override
    public KeywordValidator getKeywordValidator(Instance schema) {
        if ("date-time".equals(schema.get("format").asString())) {
            return new DateTimeKeywordValidator();
        }
        return null;
    }

    private class DateTimeKeywordValidator implements KeywordValidator {

        @Override
        public void vaildate(KeywordValidatorContext context, Instance instance) {
            if (instance.isPresent()) {
                if (instance.isString()) {
                    if (!DATE_TIME_PATTERN.matcher(instance.asString()).find()) {
                        context.addViolation("{no.thrums.validation.engine.keyword.string.format.DateTime.message}");
                    }
                } else {
                    context.addViolation("{no.thrums.validation.engine.keyword.string.format.DateTime.type.message}");
                }
            }
        }
    }
}
