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
package no.thrums.validation.engine.instance;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.ObjectMapper;
import no.thrums.instance.Instance;
import no.thrums.instance.InstanceFactory;
import no.thrums.instance.InstanceLoader;
import no.thrums.instance.ri.RiInstanceFactory;
import no.thrums.instance.ri.RiInstanceLoader;
import no.thrums.mapper.Mapper;
import no.thrums.mapper.jackson.JacksonMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.StringReader;
import java.util.*;

/**
 * @author Kristian Myrhaug
 * @since 2015-02-15
 */
public class InstanceTest {

    private Mapper mapper;
    private InstanceLoader instanceLoader;
    private InstanceFactory instanceFactory;

    @Before
    public void before(){
        mapper = new JacksonMapper(new ObjectMapper());
        instanceLoader = new RiInstanceLoader(mapper);
        instanceFactory = new RiInstanceFactory();
    }

    @Test
    public void that_bean_is_supported() {
        Instance instance = instanceFactory.defined(new Pojo(
                new int[]{1, 2, 3},
                true,
                2,
                new ArrayList<>(),
                3,
                new LinkedHashMap<>(),
                new Pojo(null, null, null, null, null, null, null, null, null),
                "four",
                SomeEnum.one));
        Assert.assertTrue(instance.isObject());
        Assert.assertTrue(instance.get("ints").isArray());
        Assert.assertTrue(instance.get("boolean").isBoolean());
        Assert.assertTrue(instance.get("integer").isIntegral());
        Assert.assertTrue(instance.get("list").isArray());
        Assert.assertTrue(instance.get("null").isNull());
        Assert.assertTrue(instance.get("undefined").isUndefined());
        Assert.assertTrue(instance.get("number").isNumber());
        Assert.assertTrue(instance.get("map").isObject());
        Assert.assertTrue(instance.get("pojo").isObject());
        Assert.assertTrue(instance.get("string").isString());
        Assert.assertTrue(instance.get("enum").isEnum());
        Assert.assertTrue(instance.get("size").isUndefined());
    }

    @Test
    public void that_objects_are_equal() {
        Instance beanInstance = instanceFactory.defined(new Pojo(
                new int[]{1, 2, 3},
                true,
                2,
                new ArrayList<>(),
                3,
                new LinkedHashMap<>(),
                new Pojo(null, null, null, null, null, null, null, null, null),
                "four",
                SomeEnum.one));
        Instance mapInstance = instanceFactory.defined(mapper.read(new StringReader(
                "{\"ints\":[1, 2, 3],\"boolean\":true,\"integer\":2,\"list\":[],\"number\":3,\"map\":{},\"pojo\":{},\"string\":\"four\",\"enum\":\"one\"}"
        ), Pojo.class));
        Assert.assertEquals(beanInstance, mapInstance);

        mapInstance = instanceLoader.loadReader(instanceFactory, new StringReader(
                "{\"ints\":[1, 2, 3],\"boolean\":true,\"integer\":2,\"list\":[],\"number\":3,\"map\":{},\"pojo\":{},\"string\":\"four\",\"enum\":\"one\"}"
        ));
        Assert.assertNotEquals(beanInstance, mapInstance);

        mapInstance = instanceLoader.loadReader(instanceFactory, new StringReader(
                "{\"ints\":[1, 2, 3],\"boolean\":true,\"integer\":2,\"list\":[],\"number\":3,\"map\":{},\"pojo\":{\"hi\":\"hello\"},\"string\":\"four\",\"enum\":\"one\"}"
        ));
        Assert.assertNotEquals(beanInstance, mapInstance);

        mapInstance = instanceLoader.loadReader(instanceFactory, new StringReader(
                "{\"ints\":[1, 2, 3],\"boolean\":true,\"integer\":2,\"list\":[],\"number\":3,\"map\":{\"hi\":\"hello\"},\"pojo\":{},\"string\":\"four\",\"enum\":\"one\"}"
        ));
        Assert.assertNotEquals(beanInstance, mapInstance);
    }

    public static class Pojo {

        private int[] ints;
        private Boolean _boolean;
        private Number integer;
        private List<Object> list;
        private Object _null;
        private Number number;
        private Map<String,Object> map;
        private Pojo pojo;
        private String string;
        private SomeEnum _enum;

        private Pojo() {}

        public Pojo(int[] ints, Boolean _boolean, Number integer, List<Object> list, Number number, Map<String, Object> map, Pojo pojo, String string, SomeEnum _enum) {
            this.ints = ints;
            this._boolean = _boolean;
            this.integer = integer;
            this.list = list;
            this.number = number;
            this.map = map;
            this.pojo = pojo;
            this.string = string;
            this._enum = _enum;
        }

        public int[] getInts() {
            return ints;
        }

        public Boolean getBoolean() {
            return _boolean;
        }

        public void setBoolean(Boolean _boolean) {
            this._boolean = _boolean;
        }

        public Number getInteger() {
            return integer;
        }

        public List<Object> getList() {
            return list;
        }

        public Object getNull() {
            return _null;
        }

        public Number getNumber() {
            return number;
        }

        public Map<String, Object> getMap() {
            return map;
        }

        public Pojo getPojo() {
            return pojo;
        }

        public String getString() {
            return string;
        }

        public SomeEnum getEnum() {
            return _enum;
        }

        public void setEnum(SomeEnum _enum) {
            this._enum = _enum;
        }

        public int size() {
            return 9;
        }
    }

    public enum SomeEnum {
        one,
        teo,
        three;

        @JsonCreator
        public static SomeEnum forValue(String value) {
            return Arrays.stream(values()).filter(someEnum -> someEnum.name().equals(value)).findFirst().orElse(null);
        }

        @JsonValue
        public String toValue() {
            return this.name();
        }

    }
}
