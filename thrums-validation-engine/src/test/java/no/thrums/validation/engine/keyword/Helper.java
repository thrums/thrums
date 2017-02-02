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
package no.thrums.validation.engine.keyword;

import com.fasterxml.jackson.databind.ObjectMapper;
import no.thrums.instance.path.NodeFactory;
import no.thrums.instance.ri.path.RiNodeFactory;
import no.thrums.mapper.Mapper;
import no.thrums.mapper.jackson.JacksonMapper;
import no.thrums.validation.Violation;
import no.thrums.validation.engine.EngineValidator;
import no.thrums.instance.ri.RiInstanceFactory;
import no.thrums.instance.ri.RiInstanceLoader;
import no.thrums.validation.engine.instance.EngineReferenceResolver;
import no.thrums.instance.ri.path.RiPathFactory;
import no.thrums.instance.Instance;
import no.thrums.instance.InstanceLoader;
import no.thrums.validation.instance.ReferenceResolver;
import no.thrums.instance.path.PathFactory;
import no.thrums.validation.Validator;
import no.thrums.instance.InstanceFactory;
import no.thrums.validation.keyword.Keyword;

import java.net.URI;
import java.util.List;

/**
 * @author Kristian Myrhaug
 * @since 2015-02-13
 */
public class Helper {

    private Mapper mapper;
    private InstanceLoader instanceLoader;
    private ReferenceResolver referenceResolver;
    private InstanceFactory instanceFactory;
    private NodeFactory nodeFactory;
    private PathFactory pathFactory;
    private Validator validator;

    public Helper(Keyword... keywords) {
        mapper = new JacksonMapper(new ObjectMapper());
        instanceLoader = new RiInstanceLoader(mapper);
        referenceResolver = new EngineReferenceResolver();
        instanceFactory = new RiInstanceFactory();
        nodeFactory = new RiNodeFactory();
        pathFactory = new RiPathFactory(nodeFactory);
        this.validator = new EngineValidator(instanceFactory, referenceResolver, pathFactory, keywords);
    }

    public Mapper getMapper() {
        return mapper;
    }

    public ReferenceResolver getReferenceResolver() {
        return referenceResolver;
    }

    public InstanceFactory getInstanceFactory() {
        return instanceFactory;
    }

    public PathFactory getPathFactory() {
        return pathFactory;
    }

    public Validator getValidator() {
        return validator;
    }

    public Instance instance(URI uri) {
        return instanceLoader.loadUri(instanceFactory, uri);
    }

    public Instance instance(String json) {
        return instanceFactory.defined(mapper.read(json, Object.class));
    }

    public Instance defined(Object instance) {
        return instanceFactory.defined(instance);
    }

    public List<Violation> validate(Instance schema, Instance instance) {
        return validator.validate(schema, instance);
    }

    public List<Violation> validate(String schema, String instance) {
        return validator.validate(instance(schema), instance(instance));
    }

    public List<Violation> validate(Instance schema, String instance) {
        return validator.validate(schema, instance(instance));
    }
}
