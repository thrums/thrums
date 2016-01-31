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
package no.thrums.validation.engine.path;

import no.thrums.validation.path.Node;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
* @author Kristian Myrhaug
* @since 2015-02-24
*/
public class Path implements no.thrums.validation.path.Path {

    private List<Node> nodes = new ArrayList<>();

    public Path(Node... nodes) {
        for(Node node : nodes) {
            this.nodes.add(node);
        }
    }

    @Override
    public no.thrums.validation.path.Path push(String key) {
        Node[] nodes = getNodes(this.nodes.size() + 1);
        nodes[nodes.length - 1] = new KeyNode(key);
        return new Path(nodes);
    }

    @Override
    public no.thrums.validation.path.Path push(Integer index) {
        Node[] nodes = getNodes(this.nodes.size() + 1);
        nodes[nodes.length - 1] = new IndexNode(index);
        return new Path(nodes);
    }

    @Override
    public no.thrums.validation.path.Path pop() {
        return new Path(getNodes(Math.max(this.nodes.size() - 1, 0)));
    }

    @Override
    public Iterator<Node> iterator() {
        return nodes.iterator();
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Node node : nodes) {
            stringBuilder.append(node.toString());
        }
        return stringBuilder.toString();
    }

    private Node[] getNodes(int length) {
        Node[] nodes = new Node[length];
        length = Math.min(length, this.nodes.size());
        for (int index = 0; index < length; index++) {
            nodes[index] = this.nodes.get(index);
        }
        return nodes;
    }
}
