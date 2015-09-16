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

import no.thrums.validation.keyword.KeywordValidator;
import no.thrums.validation.keyword.KeywordValidatorContext;
import no.thrums.validation.instance.Instance;
import no.thrums.validation.keyword.Keyword;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * @author Kristian Myrhaug
 * @since 2015-02-15
 */
public class Ipv4 implements Keyword {

    private static final Pattern OCTA_PATTERN = Pattern.compile("^0[0-8]+$");
    private static final Pattern DECA_PATTERN = Pattern.compile("^[0-9]+$");
    private static final Pattern HEXA_PATTERN = Pattern.compile("^0[xX][0-9a-fA-F]+$");

    @Override
    public KeywordValidator getKeywordValidator(Instance schema) {
        if ("ipv4".equals(schema.get("format").asString())) {
            return new Ipv4KeywordValidator();
        }
        return null;
    }

    private class Ipv4KeywordValidator implements KeywordValidator {

        @Override
        public void vaildate(KeywordValidatorContext context, Instance instance) {
            if (instance.isPresent()) {
                if (instance.isString()) {
                    String ipv4 = instance.asString();
                    Long value = null;
                    if (isOcta(ipv4) && ipv4.length() <= 12) {
                        value = Long.valueOf(ipv4, 8);
                    } else if (isDeca(ipv4) && ipv4.length() <= 10) {
                        value = Long.valueOf(ipv4, 10);
                    } else if (isHexa(ipv4) && ipv4.length() <= 10) {
                        value = Long.valueOf(ipv4.substring(2), 16);
                    } else {
                        String[] dotted = ipv4.split("\\.");
                        if (dotted.length == 4) {

                        }
                        value = 0L;
                        for (int index = 0; index < dotted.length; index++) {
                            String dot = dotted[index];
                            if (isOcta(dot) && dot.length() <= 4) {
                                value += Long.valueOf(dot, 8) << (dotted.length - index - 1) * 8;
                            } else if (isDeca(dot) && dot.length() <= 3) {
                                value += Long.valueOf(dot, 10) << (dotted.length - index - 1) * 8;
                            } else if (isHexa(dot) && dot.length() <= 4) {
                                value += Long.valueOf(dot.substring(2), 16) << (dotted.length - index - 1) * 8;
                            } else {
                                value = null;
                                break;
                            }
                        }
                    }
                    if (Objects.isNull(value)) {
                        context.addViolation("{no.thrums.validation.engine.keyword.string.format.Ipv4.message}");
                    } else if (0 > value && value > 4294967296L){
                        context.addViolation("{no.thrums.validation.engine.keyword.string.format.Ipv4.message}");
                    }
                } else {
                    context.addViolation("{no.thrums.validation.engine.keyword.string.format.Ipv4.type.message}");
                }
            }
        }

        private boolean isOcta(String string) {
            return OCTA_PATTERN.matcher(string).matches();
        }

        private boolean isDeca(String string) {
            return DECA_PATTERN.matcher(string).matches();
        }

        private boolean isHexa(String string) {
            return HEXA_PATTERN.matcher(string).matches();
        }
    }
}
