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
package no.thrums.validation.engine.instance;

import no.thrums.instance.Instance;
import no.thrums.validation.instance.ReferenceResolver;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * @author Kristian Myrhaug
 * @since 2016-05-07
 */
public class EngineReferenceResolver implements ReferenceResolver {

    @Override
    public Instance resolve(Instance reference) {
        if (reference.isObject()) {
            Instance $ref = reference.get("$ref");
            if ($ref.isString()) {
                try {
                    URI uri = getBase(reference).resolve(new URI($ref.asString()));
                    String scheme = uri.getScheme();
                    String authority = uri.getAuthority();
                    String query = uri.getQuery();
                    String path = uri.getPath();
                    String fragment = uri.getFragment();
                    if (uri.isAbsolute()) {
                        throw new UnsupportedOperationException("Not implemented: " + uri);
                    } else {
                        if (Objects.nonNull(path) && !path.isEmpty()) {
                            throw new UnsupportedOperationException("Not implemented: " + uri);
                        }
                    }
                    return getInstance(reference.getRoot(), fragment);
                } catch (Exception cause) {
                    cause.printStackTrace();
                }
            }
        }
        return reference;
    }

    private URI getBase(Instance instance) {
        List<URI> candidates = new LinkedList<>();
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

    public static List<Instance> getIds(Instance instance) {
        List<Instance> candidates = new LinkedList<>();
        for (Instance candidate = instance; Objects.nonNull(candidate.getParent()); candidate = candidate.getParent()) {
            Instance id = candidate.get("id");
            if (id.isPresent()) {
                candidates.add(candidate);
            }
        }
        return candidates;
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
