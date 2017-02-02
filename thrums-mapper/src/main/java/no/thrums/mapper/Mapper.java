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
package no.thrums.mapper;

import javax.enterprise.util.TypeLiteral;
import java.io.Reader;
import java.io.Writer;

/**
 * @author Kristian Myrhaug
 * @since 2014-08-03
 */
public interface Mapper {

    /**
     * Read/deserialize/unmarshal reader into an object of clazz
     * @param reader The {@link Reader reader} to deserialize from
     * @param clazz The {@link Class class} to deserialize to
     * @param <Type>
     * @return A deserialized instance og {@code clazz}
     * @throws MapperException
     */
    <Type> Type read(Reader reader, Class<Type> clazz) throws MapperException;

    /**
     * Read/deserialize/unmarshal reader into an object of clazz
     * @param reader The {@link Reader reader} to deserialize from
     * @param typeLiteral The {@link TypeLiteral type literal} to deserialize to
     * @param <Type>
     * @return A deserialized instance og {@code typeLiteral}
     * @throws MapperException
     */
    <Type> Type read(Reader reader, TypeLiteral<Type> typeLiteral) throws MapperException;

    /**
     * Write/serialize/marshal an object into writer
     * @param writer The {@link Writer writer} to write to
     * @param object The {@link Object object} to serialize
     * @throws MapperException
     */
    void write(Writer writer, Object object) throws MapperException;

    /**
     * Read/deserialize/unmarshal string into an object of clazz
     * @param string The {@link String string} to deserialize from
     * @param clazz The {@link Class class} to deserialize to
     * @param <Type>
     * @return A deserialized instance og {@code clazz}
     * @throws MapperException
     */
    <Type> Type read(String string, Class<Type> clazz) throws MapperException;

    /**
     * Read/deserialize/unmarshal string into an object of clazz
     * @param string The {@link String string} to deserialize from
     * @param typeLiteral The {@link TypeLiteral type literal} to deserialize to
     * @param <Type>
     * @return A deserialized instance og {@code typeLiteral}
     * @throws MapperException
     */
    <Type> Type read(String string, TypeLiteral<Type> typeLiteral) throws MapperException;

    /**
     * Write/serialize/marshal an object to string
     * @param object The {@link Object object} to serialize
     * @return A serialized instance og {@code object}
     * @throws MapperException
     */
    String write(Object object) throws MapperException;
}
