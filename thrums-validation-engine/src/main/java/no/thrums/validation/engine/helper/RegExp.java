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
package no.thrums.validation.engine.helper;

import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;

/**
 * @author Kristian Myrhaug
 * @since 2014-10-26
 */
public class RegExp {

    public static final String RESOURCE_NAME = "/" + RegExp.class.getName().replace('.', '/') + ".js";
    private static final Invocable ENGINE_SCOPE;

    static {
        ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
        ScriptEngine scriptEngine = scriptEngineManager.getEngineByName("js");
        if (scriptEngine instanceof Compilable) {
            Compilable compilable = (Compilable) scriptEngine;
            URL url = RegExp.class.getResource(RESOURCE_NAME);
            if (url == null) {
                throw new RuntimeException("Could not find resource by name: " + RESOURCE_NAME);
            }
            try (Reader reader = new InputStreamReader(url.openStream())) {
                CompiledScript compiledScript = compilable.compile(reader);
                compiledScript.eval();
                ENGINE_SCOPE = (Invocable) compiledScript.getEngine();
            } catch (IOException | ScriptException cause) {
                throw new RuntimeException("This should never happen", cause);
            }
        } else {
            throw new RuntimeException(scriptEngine.getClass().getName() + " is not instance of " + Compilable.class.getName());
        }
    }

    public static boolean isValidRegExp(String regExp) {
        try {
            return (boolean) ENGINE_SCOPE.invokeFunction("isValidRegExp", regExp);
        } catch (NoSuchMethodException | ScriptException cause) {
            throw new RuntimeException("This should never happen", cause);
        }
    }

    private RegExp(){}

    public static boolean test(String regExp, String instance) {
        try {
            return (boolean) ENGINE_SCOPE.invokeFunction("test", regExp, instance);
        } catch (NoSuchMethodException | ScriptException cause) {
            throw new RuntimeException("This should never happen", cause);
        }
    }
}
