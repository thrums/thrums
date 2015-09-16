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
package no.thrums.validation.engine.instance;

import no.thrums.validation.instance.Instance;
import no.thrums.validation.instance.InstanceFactory;
import no.thrums.validation.instance.InstanceLoader;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author Kristian Myrhaug
 * @since 2015-02-23
 */
public class ReferenceInstance extends EngineInstance {

    private final InstanceLoader instanceLoader;
    private final Instance value;
    private Instance referenced;

    public ReferenceInstance(InstanceFactory instanceFactory, InstanceLoader instanceLoader, Instance value) {
        super(instanceFactory, value.getParent());
        this.instanceLoader = instanceLoader;
        this.value = Objects.requireNonNull(value, "Third argument may not be null");
    }

    @Override
    public boolean isArray() {
        return getReference().isArray();
    }

    @Override
    public boolean isBoolean() {
        return getReference().isBoolean();
    }

    @Override
    public boolean isIntegral() {
        return getReference().isIntegral();
    }

    @Override
    public boolean isNull() {
        return getReference().isNull();
    }

    @Override
    public boolean isReference() {
        return getReference().isReference();
    }

    @Override
    public boolean isUndefined() {
        return getReference().isUndefined();
    }

    @Override
    public boolean isNumber() {
        return getReference().isNumber();
    }

    @Override
    public boolean isObject() {
        return getReference().isObject();
    }

    @Override
    public boolean isString() {
        return getReference().isString();
    }

    @Override
    public Instance get(String key) {
        return getReference().get(key);
    }

    @Override
    public Instance get(Integer index) {
        return getReference().get(index);
    }

    @Override
    public List<Instance> items() {
        return getReference().items();
    }

    @Override
    public Boolean asBoolean() {
        return getReference().asBoolean();
    }

    @Override
    public Number asNumber() {
        return getReference().asNumber();
    }

    @Override
    public Map<String, Instance> properties() {
        return getReference().properties();
    }

    @Override
    public String asString() {
        return getReference().asString();
    }

    @Override
    public boolean isPresent() {
        return getReference().isPresent();
    }

    @Override
    public Object asValue() {
        return getReference().asValue();
    }

    @Override
    public Instance getParent() {
        return getReference().getParent();
    }

    @Override
    public Instance getRoot() {
        return getReference().getRoot();
    }

    @Override
    public Instance defined(Object object) {
        return getReference().defined(object);
    }

    @Override
    public Instance undefined() {
        return getReference().undefined();
    }

    public Instance getReference(){
        if (Objects.isNull(referenced)) {
            referenced = resolve(value);
        }
        return referenced;
    }

    private Instance resolve(Instance instance) {
        Instance $ref = instance.get("$ref");
        if ($ref.isString()) {
            try {
                URI uri = getBase(instance).resolve(new URI($ref.asString()));
                String scheme = uri.getScheme();
                String authority = uri.getAuthority();
                String query = uri.getQuery();
                String path = uri.getPath();
                String fragment = uri.getFragment();
                if (uri.isAbsolute()) {
                    instance = instanceLoader.loadUri(instanceFactory, uri);
                } else {
                    if (Objects.nonNull(path) && !path.isEmpty()) {
                        throw new UnsupportedOperationException("Not implemented: " + uri);
                    }
                }
                return getInstance(instance.getRoot(), fragment);
            } catch (Exception cause) {
                cause.printStackTrace();
            }
        }
        return instance;
    }

    private URI getBase(Instance instance) {
        List<URI> candidates = new ArrayList<>();
        for (Instance candidate = instance; Objects.nonNull(candidate.getParent()); candidate = candidate.getParent()) {
            Instance id = candidate.get("id");
            if (id.isString()) {
                try {
                    candidates.add(new URI(id.asString()));
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }
        }
        for (int index = 0; index < candidates.size(); index++) {
            if (candidates.get(index).isAbsolute()) {
                return candidates.get(index);
            }
        }
        for (int index = 0; index < candidates.size(); index++) {
            if (candidates.get(index).isOpaque()) {
                return candidates.get(index);
            }
        }
        try {
            return new URI("");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Instance getInstance(Instance instance, String fragment) {
        String[] path = null;
        if (fragment.startsWith("#")) {
            path = fragment.substring(1).split("/");
        } else {
            path = fragment.split("/");
        }
        for (String node : path) {
            if (!node.isEmpty()) {
                if (isOnlyDigits(node) && instance.isArray()) {
                    instance = instance.get(Integer.valueOf(node));
                } else {
                    instance = instance.get(node);
                }
            }
        }
        return instance;
    }

    private boolean isOnlyDigits(String fragment) {
        if (fragment.length() == 0) {
            return false;
        }
        for (int index = 0; index < fragment.length(); index ++) {
            if (!Character.isDigit(fragment.charAt(index))) {
                return false;
            }
        }
        return true;
    }
}
