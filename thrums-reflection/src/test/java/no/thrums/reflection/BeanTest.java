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
