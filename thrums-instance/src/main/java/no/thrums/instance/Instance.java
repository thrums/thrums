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
package no.thrums.instance;

import no.thrums.instance.path.Path;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Kristian Myrhaug
 * @since 2015-02-12
 */
public interface Instance {

    boolean isArray();

    boolean isBoolean();

    boolean isIntegral();

    boolean isEnum();

    boolean isNull();

    boolean isUndefined();

    boolean isNumber();

    boolean isObject();

    boolean isString();

    Instance get(String key);

    Instance get(Integer index);

    Instance get(Path path);

    Integer itemsSize();

    List<Instance> items();

    Boolean asBoolean();

    Enum asEnum();

    Number asNumber();

    Set<String> keys();

    Map<String,Instance> properties();

    String asString();

    boolean isPresent();

    Object asValue();

    Instance getParent();

    Instance getRoot();

    InstanceFactory getInstanceFactory();



}
