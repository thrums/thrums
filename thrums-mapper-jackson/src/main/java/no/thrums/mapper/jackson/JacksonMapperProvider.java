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
package no.thrums.mapper.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import no.thrums.mapper.Mapper;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;

/**
 * @author Kristian Myrhaug
 * @since 2015-02-24
 */
@Jackson
@Named("no.thrums.mapper.jackson.JacksonMapperProvider")
public class JacksonMapperProvider implements Provider<Mapper> {

    private final ObjectMapper objectMapper;

    @Inject
    public JacksonMapperProvider(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Jackson
    @Named("no.thrums.mapper.jackson.JacksonMapper")
    @Override
    public Mapper get() {
        return new JacksonMapper(new ObjectMapper());
    }
}
