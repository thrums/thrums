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
package no.thrums.instance.ri.path;

import no.thrums.instance.path.Node;
import no.thrums.instance.path.NodeFactory;

import javax.inject.Named;

/**
 * @author Kristian Myrhaug
 * @since 2015-02-16
 */
@Named("no.thrums.instance.ri.path.RiNodeFactory")
public class RiNodeFactory implements NodeFactory {

    @Named("no.thrums.instance.ri.path.RiKeyNode")
    @Override
    public Node createKey(String key) {
        return new RiKeyNode(key);
    }

    @Named("no.thrums.instance.ri.path.RiIndexNode")
    @Override
    public Node createIndex(Integer index) {
        return new RiIndexNode(index);
    }
}
