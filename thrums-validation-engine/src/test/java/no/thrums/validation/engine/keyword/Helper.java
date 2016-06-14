package no.thrums.validation.engine.keyword;

import com.fasterxml.jackson.databind.ObjectMapper;
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
    private PathFactory pathFactory;
    private Validator validator;

    public Helper(Keyword... keywords) {
        mapper = new JacksonMapper(new ObjectMapper());
        instanceLoader = new RiInstanceLoader(mapper);
        referenceResolver = new EngineReferenceResolver();
        instanceFactory = new RiInstanceFactory();
        pathFactory = new RiPathFactory();
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
