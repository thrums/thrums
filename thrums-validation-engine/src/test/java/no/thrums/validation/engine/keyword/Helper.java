package no.thrums.validation.engine.keyword;

import com.fasterxml.jackson.databind.ObjectMapper;
import no.thrums.mapper.Mapper;
import no.thrums.mapper.jackson.JacksonMapper;
import no.thrums.validation.Violation;
import no.thrums.validation.engine.EngineValidator;
import no.thrums.validation.engine.instance.EngineInstanceFactory;
import no.thrums.validation.engine.instance.EngineInstanceLoader;
import no.thrums.validation.engine.path.EnginePathFactory;
import no.thrums.validation.instance.Instance;
import no.thrums.validation.instance.InstanceLoader;
import no.thrums.validation.path.PathFactory;
import no.thrums.validation.Validator;
import no.thrums.validation.instance.InstanceFactory;
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
    private InstanceFactory instanceFactory;
    private PathFactory pathFactory;
    private Validator validator;

    public Helper(Keyword... keywords) {
        mapper = new JacksonMapper(new ObjectMapper());
        instanceLoader = new EngineInstanceLoader(mapper);
        instanceFactory = new EngineInstanceFactory(instanceLoader);
        pathFactory = new EnginePathFactory();
        this.validator = new EngineValidator(instanceFactory, pathFactory, keywords);
    }

    public Mapper getMapper() {
        return mapper;
    }

    public InstanceLoader getInstanceLoader() {
        return instanceLoader;
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
