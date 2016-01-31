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
package no.thrums.validation.engine;

import no.thrums.validation.Validator;
import no.thrums.validation.instance.InstanceFactory;
import no.thrums.validation.keyword.Keyword;
import no.thrums.validation.path.PathFactory;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import java.util.List;

/**
 * @author Kristian Myrhaug
 * @since 2015-02-24
 */
@Engine
@Named("no.thrums.validation.engine.ValidatorProvider")
public class ValidatorProvider implements Provider<Validator> {

    private final InstanceFactory instanceFactory;
    private final PathFactory pathFactory;
    private final List<Keyword> keywords;

    @Inject
    public ValidatorProvider(InstanceFactory instanceFactory, PathFactory pathFactory, List<Keyword> keywords) {
        this.instanceFactory = instanceFactory;
        this.pathFactory = pathFactory;
        this.keywords = keywords;
    }

    @Engine
    @Named("no.thrums.validation.engine.EngineValidator")
    @Override
    public Validator get() {
        return new EngineValidator(instanceFactory, pathFactory, keywords);
    }
}
