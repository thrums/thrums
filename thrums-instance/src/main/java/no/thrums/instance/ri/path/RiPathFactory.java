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

import no.thrums.instance.path.NodeFactory;
import no.thrums.instance.path.Path;
import no.thrums.instance.path.PathFactory;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author Kristian Myrhaug
 * @since 2015-02-16
 */
@Named("no.thrums.instance.ri.path.RiPathFactory")
public class RiPathFactory implements PathFactory {

    private NodeFactory nodeFactory;

    @Inject
    public RiPathFactory(NodeFactory nodeFactory) {
        this.nodeFactory = nodeFactory;
    }

    @Named("no.thrums.instance.ri.path.Path")
    @Override
    public Path root() {
        return new RiPath(nodeFactory);
    }

}
