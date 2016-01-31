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
package no.thrums.mapper.jackson;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import no.thrums.mapper.Mapper;
import no.thrums.mapper.MapperException;

import javax.enterprise.util.TypeLiteral;
import javax.inject.Inject;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Type;

/**
 * @author Kristian Myrhaug
 * @since 2014-08-03
 */
public class JacksonMapper implements Mapper {

    private ObjectMapper objectMapper;

    @Inject
    public JacksonMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public <Type> Type read(Reader reader, Class<Type> type) throws MapperException {
        try {
            return objectMapper.readValue(reader, type);
        } catch (IOException cause) {
            throw new MapperException(cause.getMessage(), cause);
        }
    }

    @Override
    public <Type> Type read(Reader reader, TypeLiteral<Type> typeLiteral) throws MapperException {
        try {
            return objectMapper.readValue(reader, new TypeLiteralTypeReference<>(typeLiteral));
        } catch (IOException cause) {
            throw new MapperException(cause.getMessage(), cause);
        }
    }

    @Override
    public void write(Writer writer, Object value) throws MapperException {
        try {
            objectMapper.writeValue(writer, value);
        } catch (IOException cause) {
            throw new MapperException(cause.getMessage(), cause);
        }
    }

    @Override
    public <Type> Type read(String string, Class<Type> clazz) throws MapperException {
        try (StringReader stringReader = new StringReader(string)){
            return read(stringReader, clazz);
        }
    }

    @Override
    public <Type> Type read(String string, TypeLiteral<Type> typeLiteral) throws MapperException {
        try (StringReader stringReader = new StringReader(string)){
            return read(stringReader, typeLiteral);
        }
    }

    @Override
    public String write(Object object) throws MapperException {
        try (StringWriter stringWriter = new StringWriter()) {
            write(stringWriter, object);
            return stringWriter.toString();
        } catch (IOException cause) {
            throw new MapperException(cause.getMessage(), cause);
        }
    }

    private static class TypeLiteralTypeReference<T> extends TypeReference<T> {

        private TypeLiteral<T> typeLiteral;

        public TypeLiteralTypeReference(TypeLiteral<T> typeLiteral) {
            this.typeLiteral = typeLiteral;
        }

        @Override
        public Type getType() {
            return typeLiteral.getType();
        }
    }
}
