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

import no.thrums.difference.Comparator;
import no.thrums.difference.Difference;
import no.thrums.instance.Instance;
import no.thrums.instance.path.Path;
import no.thrums.instance.path.PathFactory;
import no.thrums.instance.ri.helper.NumberComparator;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Kristian Myrhaug
 * @since 2016-06-05
 */
public class RiComparator implements Comparator {

    private static final NumberComparator NUMBER_COMPARATOR = new NumberComparator();

    private PathFactory pathFactory;

    public RiComparator(PathFactory pathFactory) {
        this.pathFactory = pathFactory;
    }

    @Override
    public List<Difference> compare(Instance first, Instance second) {
        Set<Difference> differences = new LinkedHashSet<>();
        List<Difference> queue = new LinkedList<>();
        queue.add(new RiDifference(pathFactory.root(), first, second));
        for (int index = 0; index < queue.size(); index++) {
            Difference difference = queue.remove(index--);
            Path path = difference.getPath();
            first = difference.getFirst();
            second = difference.getSecond();
            if (    Objects.equals(first.isArray(), second.isArray()) &&
                    Objects.equals(first.isBoolean(), second.isBoolean()) &&
                    Objects.equals(first.isIntegral(), second.isIntegral()) &&
                    Objects.equals(first.isNull(), second.isNull()) &&
                    Objects.equals(first.isUndefined(), second.isUndefined()) &&
                    Objects.equals(first.isNumber(), second.isNumber()) &&
                    Objects.equals(first.isObject(), second.isObject()) &&
                    Objects.equals(first.isString(), second.isString()) &&
                    Objects.equals(first.isEnum(), second.isEnum())) {
                if (first.isNumber()) {
                    if (NUMBER_COMPARATOR.compare(first.asNumber(), second.asNumber()) != 0) {
                        addParent(differences, difference);
                        differences.add(difference);
                    }
                } else if (first.isObject()) {
                    Set<String> keySet = keys(first);
                    keySet.addAll(second.keys());
                    for (String key : keySet) {
                        queue.add(new RiDifference(path.withKey(key), first.get(key), second.get(key)));
                    }
                } else if (first.isArray()) {
                    int length = Math.max(itemsSize(first), itemsSize(second));
                    for (int itemIndex = 0; itemIndex < length; itemIndex++) {
                        queue.add(new RiDifference(path.withIndex(itemIndex), first.get(itemIndex), second.get(itemIndex)));
                    }
                } else if (!Objects.equals(first.asValue(), second.asValue())) {
                    addParent(differences, difference);
                    differences.add(difference);
                }
            } else {
                addParent(differences, difference);
                differences.add(difference);
                if (!first.isPresent() || !second.isPresent()) {
                    if (first.isObject() || second.isObject()) {
                        Set<String> keySet = keys(first);
                        keySet.addAll(keys(second));
                        for (String key : keySet) {
                            queue.add(new RiDifference(path.withKey(key), first.get(key), second.get(key)));
                        }
                    }
                    if (first.isArray() || second.isArray()) {
                        int length = Math.max(itemsSize(first), itemsSize(second));
                        for (int itemIndex = 0; itemIndex < length; itemIndex++) {
                            queue.add(new RiDifference(path.withIndex(itemIndex), first.get(itemIndex), second.get(itemIndex)));
                        }
                    }
                }
            }
        }
        return differences.stream().collect(Collectors.toList());
    }

    private Integer itemsSize(Instance instance) {
        if (instance.isArray()) {
            return instance.itemsSize();
        }
        return 0;
    }

    private LinkedHashSet<String> keys(Instance instance) {
        if (instance.isObject()) {
            return new LinkedHashSet<>(instance.keys());
        }
        return new LinkedHashSet<>();
    }

    private void addParent(Set<Difference> differences, Difference difference) {
        if (difference.getPath().size() > 0) {
            differences.add(new RiDifference(difference.getPath().pop(), difference.getFirst().getParent(), difference.getSecond().getParent()));
        }
    }

    public PathFactory getPathFactory() {
        return pathFactory;
    }
}
