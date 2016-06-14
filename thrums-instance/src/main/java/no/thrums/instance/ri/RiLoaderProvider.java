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
package no.thrums.instance.ri;

import no.thrums.mapper.Mapper;
import no.thrums.instance.InstanceLoader;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;

/**
 * @author Kristian Myrhaug
 * @since 2015-02-24
 */
@Named("no.thrums.instance.ri.RiLoaderProvider")
public class RiLoaderProvider implements Provider<InstanceLoader> {

    private final Mapper mapper;

    @Inject
    public RiLoaderProvider(Mapper mapper) {
        this.mapper = mapper;
    }

    @Named("no.thrums.instance.ri.RiInstanceLoader")
    @Override
    public InstanceLoader get() {
        return new RiInstanceLoader(mapper);
    }
}
