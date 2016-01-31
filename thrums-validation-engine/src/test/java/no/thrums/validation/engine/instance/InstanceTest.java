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
package no.thrums.validation.engine.instance;

import com.fasterxml.jackson.databind.ObjectMapper;
import no.thrums.mapper.Mapper;
import no.thrums.mapper.jackson.JacksonMapper;
import no.thrums.reflection.Bean;
import no.thrums.validation.instance.Instance;
import no.thrums.validation.instance.InstanceFactory;
import no.thrums.validation.instance.InstanceLoader;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Kristian Myrhaug
 * @since 2015-02-15
 */
public class InstanceTest {

    private InstanceLoader instanceLoader;
    private InstanceFactory instanceFactory;

    @Before
    public void before(){
        Mapper mapper = new JacksonMapper(new ObjectMapper());
        instanceLoader = new EngineInstanceLoader(mapper);
        instanceFactory = new EngineInstanceFactory(instanceLoader);
    }

    @Test
    public void that_bean_is_supported() {
        Instance instance = instanceFactory.defined(new Pojo(
                new int[]{1, 2, 3},
                true,
                2,
                new ArrayList<>(),
                3,
                new LinkedHashMap<String, Object>(),
                new Pojo(null, null, null, null, null, null, null, null),
                "four"
        ));
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
        Assert.assertTrue(instance.get("size").isUndefined());
    }

    @Test
    public void that_bean_is_writable() {
        Pojo pojo = new Pojo(
                new int[]{1, 2, 3},
                true,
                2,
                new ArrayList<>(),
                3,
                new LinkedHashMap<String, Object>(),
                new Pojo(null, null, null, null, null, null, null, null),
                "four"
        );
        Map<String,Object> bean = new Bean(pojo);
        Assert.assertTrue(pojo.getBoolean());
        Assert.assertTrue((Boolean)bean.get("boolean"));

        bean.put("boolean", false);
        Assert.assertFalse((Boolean) bean.get("boolean"));
        Assert.assertFalse(pojo.getBoolean());

        /*bean.remove("boolean");
        Assert.assertNull(bean.get("boolean"));
        Assert.assertNull(pojo.getBoolean());

        bean.put("boolean", true);
        Assert.assertTrue(pojo.getBoolean());
        Assert.assertTrue((Boolean) bean.get("boolean"));*/
    }

    @Test
    public void that_objects_are_equal() {
        Instance beanInstance = instanceFactory.defined(new Pojo(
                new int[]{1, 2, 3},
                true,
                2,
                new ArrayList<>(),
                3,
                new LinkedHashMap<String, Object>(),
                new Pojo(null, null, null, null, null, null, null, null),
                "four"
        ));
        Instance mapInstance = instanceLoader.loadReader(instanceFactory, new StringReader(
                "{\"ints\":[1, 2, 3],\"boolean\":true,\"integer\":2,\"list\":[],\"number\":3,\"map\":{},\"pojo\":{},\"string\":\"four\"}"
        ));
        Assert.assertEquals(beanInstance, mapInstance);

        mapInstance = instanceLoader.loadReader(instanceFactory, new StringReader(
                "{\"ints\":[1, 2, 3],\"boolean\":true,\"integer\":2,\"list\":[],\"number\":3,\"map\":{},\"pojo\":{\"hi\":\"hello\"},\"string\":\"four\"}"
        ));
        Assert.assertNotEquals(beanInstance, mapInstance);

        mapInstance = instanceLoader.loadReader(instanceFactory, new StringReader(
                "{\"ints\":[1, 2, 3],\"boolean\":true,\"integer\":2,\"list\":[],\"number\":3,\"map\":{\"hi\":\"hello\"},\"pojo\":{},\"string\":\"four\"}"
        ));
        Assert.assertNotEquals(beanInstance, mapInstance);
    }

    public class Pojo {

        private int[] ints;
        private Boolean _boolean;
        private Number integer;
        private List<Object> list;
        private Object _null;
        private Object undefined;
        private Number number;
        private Map<String,Object> map;
        private Pojo pojo;
        private String string;

        public Pojo(int[] ints, Boolean _boolean, Number integer, List<Object> list, Number number, Map<String, Object> map, Pojo pojo, String string) {
            this.ints = ints;
            this._boolean = _boolean;
            this.integer = integer;
            this.list = list;
            this.number = number;
            this.map = map;
            this.pojo = pojo;
            this.string = string;
        }

        public int[] getInts() {
            return ints;
        }

        public void setInts(int[] ints) {
            this.ints = ints;
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

        public void setInteger(Number integer) {
            this.integer = integer;
        }

        public List<Object> getList() {
            return list;
        }

        public void setList(List<Object> list) {
            this.list = list;
        }

        public Object getNull() {
            return _null;
        }

        public void setNull(Object _null) {
            this._null = _null;
        }

        public void setUndefined(Object undefined) {
            this.undefined = undefined;
        }

        public Number getNumber() {
            return number;
        }

        public void setNumber(Number number) {
            this.number = number;
        }

        public Map<String, Object> getMap() {
            return map;
        }

        public void setMap(Map<String, Object> map) {
            this.map = map;
        }

        public Pojo getPojo() {
            return pojo;
        }

        public void setPojo(Pojo pojo) {
            this.pojo = pojo;
        }

        public String getString() {
            return string;
        }

        public void setString(String string) {
            this.string = string;
        }

        public int size() {
            return 9;
        }
    }
}
