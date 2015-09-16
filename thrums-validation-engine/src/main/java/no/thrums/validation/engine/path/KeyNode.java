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
package no.thrums.validation.engine.path;

import no.thrums.validation.path.Node;

/**
* @author Kristian Myrhaug
* @since 2015-02-24
*/
public class KeyNode implements Node<String> {

    private String value;

    public KeyNode(String value) {
        this.value = value;
    }

    @Override
    public Type getType() {
        return Type.KEY;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "." + getValue();
    }
}
