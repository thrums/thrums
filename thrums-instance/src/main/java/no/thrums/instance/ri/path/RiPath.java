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
package no.thrums.instance.ri.path;

import no.thrums.instance.path.Node;
import no.thrums.instance.path.NodeFactory;
import no.thrums.instance.path.Path;

import java.util.*;

import static java.util.Objects.requireNonNull;

/**
* @author Kristian Myrhaug
* @since 2015-02-24
*/
public class RiPath implements Path {

    private NodeFactory nodeFactory;

    private List<Node> nodes = new ArrayList<>();

    public RiPath(NodeFactory nodeFactory, Node... nodes) {
        this.nodeFactory = nodeFactory;
        for(Node node : nodes) {
            this.nodes.add(node);
        }
    }

    @Override
    public int size() {
        return nodes.size();
    }

    @Override
    public boolean isEmpty() {
        return nodes.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return nodes.contains(o);
    }

    @Override
    public Iterator<Node> iterator() {
        return nodes.iterator();
    }

    @Override
    public Object[] toArray() {
        return nodes.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return nodes.toArray(a);
    }

    @Override
    public boolean add(Node node) {
        return nodes.add(node);
    }

    @Override
    public boolean remove(Object o) {
        return nodes.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return nodes.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends Node> c) {
        return nodes.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends Node> c) {
        return nodes.addAll(index, c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return nodes.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return nodes.retainAll(c);
    }

    @Override
    public void clear() {
        nodes.clear();
    }

    @Override
    public boolean equals(Object o) {
        return nodes.equals(o);
    }

    @Override
    public int hashCode() {
        return nodes.hashCode();
    }

    @Override
    public Node get(int index) {
        return nodes.get(index);
    }

    @Override
    public Node set(int index, Node element) {
        return nodes.set(index, element);
    }

    @Override
    public void add(int index, Node element) {
        nodes.add(index, element);
    }

    @Override
    public Node remove(int index) {
        return nodes.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return nodes.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return nodes.lastIndexOf(o);
    }

    @Override
    public ListIterator<Node> listIterator() {
        return nodes.listIterator();
    }

    @Override
    public ListIterator<Node> listIterator(int index) {
        return nodes.listIterator(index);
    }

    @Override
    public List<Node> subList(int fromIndex, int toIndex) {
        return nodes.subList(fromIndex, toIndex);
    }

    @Override
    public Path withKey(String key) {
        Node[] nodes = getNodes(this.nodes.size() + 1);
        nodes[nodes.length - 1] = nodeFactory.createKey(key);
        return new RiPath(nodeFactory, nodes);
    }

    @Override
    public Path withIndex(Integer index) {
        Node[] nodes = getNodes(this.nodes.size() + 1);
        nodes[nodes.length - 1] = nodeFactory.createIndex(index);
        return new RiPath(nodeFactory, nodes);
    }

    @Override
    public Path pop() {
        Node[] nodes = getNodes(this.nodes.size() - 1);
        return new RiPath(nodeFactory, nodes);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Node node : nodes) {
            stringBuilder.append(node.toString());
        }
        return stringBuilder.toString();
    }

    public NodeFactory getNodeFactory() {
        return nodeFactory;
    }

    private Node[] getNodes(int length) {
        length = Math.max(0, length);
        Node[] nodes = new Node[length];
        length = Math.min(length, this.nodes.size());
        for (int index = 0; index < length; index++) {
            nodes[index] = this.nodes.get(index);
        }
        return nodes;
    }

}
