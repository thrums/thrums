/**
 Copyright 2014-2015 Kristian Myrhaug

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

import no.thrums.validation.engine.Engine;
import no.thrums.validation.instance.InstanceFactory;
import no.thrums.validation.instance.InstanceLoader;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;

/**
 * @author Kristian Myrhaug
 * @since 2015-02-24
 */
@Engine
@Named("no.thrums.validation.engine.InstanceFactoryProvider")
public class InstanceFactoryProvider implements Provider<InstanceFactory> {

    private final InstanceLoader instanceLoader;

    @Inject
    public InstanceFactoryProvider(InstanceLoader instanceLoader) {
        this.instanceLoader = instanceLoader;
    }

    @Engine
    @Named("no.thrums.validation.engine.InstanceFactory")
    @Override
    public InstanceFactory get() {
        return new EngineInstanceFactory(instanceLoader);
    }
}
