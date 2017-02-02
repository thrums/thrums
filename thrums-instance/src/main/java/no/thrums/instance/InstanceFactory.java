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

import java.util.List;
import java.util.Map;

/**
 * @author Kristian Myrhaug
 * @since 2015-02-13
 */
public interface InstanceFactory {

    Instance defined(Instance parent ,Object object);
    Instance undefined(Instance parent);
    Instance defined(Object object);
    Instance undefined();

    Instance createArray(Instance parent, Object[] value);
    Instance createArrayFromList(Instance parent, List<Object> value);
    Instance createBoolean(Instance parent, Boolean value);
    Instance createJavaEnum(Instance parent, Enum value);
    Instance createNull(Instance parent);
    Instance createNumber(Instance parent, Number number);
    Instance createObject(Instance parent, Object value);
    Instance createObjectFromMap(Instance parent, Map<String,Object> value);
    Instance createString(Instance parent, String value);
    Instance createUndefined(Instance parent);
}
