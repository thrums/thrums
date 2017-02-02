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
package no.thrums.reflection;

import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

/**
 * @author Kristian Myrhaug
 * @since 2016-05-01
 */
public class BeanTest {

    @Test
    public void that_bean_is_writable() {
        Pojo pojo = new Pojo(true);
        Map<String,Object> bean = new Bean(pojo);
        Assert.assertTrue(pojo.getBoolean());
        Assert.assertTrue((Boolean)bean.get("boolean"));

        bean.put("boolean", false);
        Assert.assertFalse((Boolean) bean.get("boolean"));
        Assert.assertFalse(pojo.getBoolean());
    }

    public class Pojo {

        private Boolean _boolean;

        public Pojo(Boolean _boolean) {
            this._boolean = _boolean;
        }
        public Boolean getBoolean() {
            return _boolean;
        }

        public void setBoolean(Boolean _boolean) {
            this._boolean = _boolean;
        }
    }
}
