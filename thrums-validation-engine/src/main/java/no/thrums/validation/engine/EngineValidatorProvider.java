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
package no.thrums.validation.engine;

import no.thrums.validation.Validator;
import no.thrums.instance.InstanceFactory;
import no.thrums.validation.instance.ReferenceResolver;
import no.thrums.validation.keyword.Keyword;
import no.thrums.instance.path.PathFactory;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import java.util.List;

/**
 * @author Kristian Myrhaug
 * @since 2015-02-24
 */
@Named("no.thrums.validation.engine.EngineValidatorProvider")
public class EngineValidatorProvider implements Provider<Validator> {

    private final InstanceFactory instanceFactory;
    private final ReferenceResolver referenceResolver;
    private final PathFactory pathFactory;
    private final List<Keyword> keywords;

    @Inject
    public EngineValidatorProvider(InstanceFactory instanceFactory, ReferenceResolver referenceResolver, PathFactory pathFactory, List<Keyword> keywords) {
        this.instanceFactory = instanceFactory;
        this.referenceResolver = referenceResolver;
        this.pathFactory = pathFactory;
        this.keywords = keywords;
    }

    @Named("no.thrums.validation.engine.EngineValidator")
    @Override
    public Validator get() {
        return new EngineValidator(instanceFactory, referenceResolver, pathFactory, keywords);
    }
}
