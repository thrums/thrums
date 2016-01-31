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
package no.thrums.validation.engine;

import no.thrums.validation.Violation;
import no.thrums.reflection.Pojo;
import no.thrums.validation.instance.Instance;
import no.thrums.validation.instance.InstanceFactory;
import no.thrums.validation.path.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * @author Kristian Myrhaug
 * @since 2014-12-19
 */
public class EngineViolation implements Violation {

    private static final Logger LOG = LoggerFactory.getLogger(EngineViolation.class);

    private final Path path;
    private final String message;
    private final Instance instance;
    private final Instance schema;

    private final InstanceFactory instanceFactory;


    public EngineViolation(Path path, String message, Instance instance, Instance schema, InstanceFactory instanceFactory) {
        this.path = path;
        this.message = message;
        this.instance = instance;
        this.schema = schema;
        this.instanceFactory = instanceFactory;
    }

    @Override
    public Path getPath() {
        return path;
    }

    @Override
    public String getMessage(Locale locale) {
        String alteredMessageTemplate = message;
        try {
            ResourceBundle resourceBundle = ResourceBundle.getBundle("no.thrums.validation.engine.Message", locale);
            List<String> messageTemplates = extractMessageTemplates(message);
            for (String messageTemplate : messageTemplates) {
                if (resourceBundle.containsKey(messageTemplate)) {
                    alteredMessageTemplate = alteredMessageTemplate.replace(String.format("{%s}", messageTemplate), resourceBundle.getString(messageTemplate));
                }
            }
        } catch (MissingResourceException cause) {
            LOG.warn("Missing resource", cause);
        }
        List<String> messageTemplates = extractMessageTemplates(alteredMessageTemplate);
        for (String messageTemplate : messageTemplates) {
            String[] nodes = messageTemplate.split("\\.");
            Instance instance = null;
            if ("path".equals(nodes[0])) {
                instance = instanceFactory.defined(path);
            } else if ("instance".equals(nodes[0])) {
                instance = this.instance;
            } else if ("schema".equals(nodes[0])) {
                instance = this.schema;
            } else {
                break;
            }
            nodes = Arrays.copyOfRange(nodes, 1, nodes.length);
            for (int depth = 0; depth < nodes.length; depth++) {
                Integer index = getIndex(nodes[depth]);
                if (Objects.nonNull(index)) {
                    instance = instance.get(index);
                } else {
                    if (!instance.isObject()) {
                        instance = instance.defined(new Pojo(instance.asValue())).get(nodes[depth]);
                    } else {
                        instance = instance.get(nodes[depth]);
                    }
                }
            }
            alteredMessageTemplate = alteredMessageTemplate.replace(String.format("{%s}", messageTemplate), String.valueOf(instance.asValue()));
        }
        return alteredMessageTemplate;
    }

    @Override
    public String getMessage() {
        return getMessage(Locale.getDefault(Locale.Category.DISPLAY));
    }

    private Integer getIndex(String node) {
        Integer index = null;
        if (node.startsWith("[") && node.endsWith("]")) {
            try {
                index = Integer.valueOf(node.substring(1, node.length() - 1));
            } catch (NumberFormatException cause) {}
        }
        return index;
    }

    private List<String> extractMessageTemplates(String message) {
        List<String> messageTemplates = new ArrayList<>();
        StringBuffer buffer = new StringBuffer();
        int depth = 0;
        for (int index = 0; index < message.length(); index++) {
            char c = message.charAt(index);
            if ('{' == c) {
                depth++;
            } else if ('}' == c) {
                depth--;
                if (depth == 0 && buffer.length() > 0) {
                    messageTemplates.add(buffer.toString());
                    buffer.delete(0, buffer.length());
                }
            } else if (depth > 0) {
                buffer.append(c);
            }
        }
        return messageTemplates;
    }

    @Override
    public Instance getInstance() {
        return instance;
    }

    @Override
    public Instance getSchema() {
        return schema;
    }
}
