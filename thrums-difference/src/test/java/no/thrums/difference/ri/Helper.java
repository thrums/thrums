package no.thrums.difference.ri;

import com.fasterxml.jackson.databind.ObjectMapper;
import no.thrums.difference.Comparator;
import no.thrums.difference.Difference;
import no.thrums.instance.Instance;
import no.thrums.instance.InstanceFactory;
import no.thrums.instance.InstanceLoader;
import no.thrums.instance.path.NodeFactory;
import no.thrums.instance.path.PathFactory;
import no.thrums.instance.ri.RiInstanceFactory;
import no.thrums.instance.ri.RiInstanceLoader;
import no.thrums.instance.ri.path.RiNodeFactory;
import no.thrums.instance.ri.path.RiPathFactory;
import no.thrums.mapper.Mapper;
import no.thrums.mapper.jackson.JacksonMapper;

import java.net.URI;
import java.util.List;

import static java.util.Objects.nonNull;

/**
 * @author Kristian Myrhaug
 * @since 2016-06-05
 */
public class Helper {

    private Mapper mapper;
    private InstanceLoader instanceLoader;
    private InstanceFactory instanceFactory;
    private NodeFactory nodeFactory;
    private PathFactory pathFactory;
    private Comparator comparator;

    public Helper() {
        mapper = new JacksonMapper(new ObjectMapper());
        instanceLoader = new RiInstanceLoader(mapper);
        instanceFactory = new RiInstanceFactory();
        nodeFactory = new RiNodeFactory();
        pathFactory = new RiPathFactory(nodeFactory);
        this.comparator = new RiComparator(pathFactory);
    }

    public Mapper getMapper() {
        return mapper;
    }

    public InstanceFactory getInstanceFactory() {
        return instanceFactory;
    }

    public NodeFactory getNodeFactory() {
        return nodeFactory;
    }

    public PathFactory getPathFactory() {
        return pathFactory;
    }

    public Comparator getComparator() {
        return comparator;
    }

    public Instance instance(URI uri) {
        return instanceLoader.loadUri(instanceFactory, uri);
    }

    public Instance instance(String json) {
        if (nonNull(json)) {
            return instanceFactory.defined(mapper.read(json, Object.class));
        }
        return instanceFactory.undefined();
    }

    public Instance defined(Object instance) {
        return instanceFactory.defined(instance);
    }

    public Instance undefined() {
        return instanceFactory.undefined();
    }

    public List<Difference> compare(Instance first, Instance second) {
        return comparator.compare(first, second);
    }

    public List<Difference> compare(String first, String second) {
        return comparator.compare(instance(first), instance(second));
    }

    public List<Difference> compare(Instance first, String second) {
        return comparator.compare(first, instance(second));
    }
}
