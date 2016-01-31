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
package no.thrums.io;

import java.io.IOException;
import java.io.Writer;

import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

/**
 * @author Kristian Myrhaug
 * @since 2015-10-06
 */
public class AppendableWriter<T extends Appendable> extends Writer {

    private T appendable;

    public AppendableWriter(T appendable) {
        this.appendable = appendable;
    }

    public T getAppendable() {
        return appendable;
    }

    @Override
    public void write(char[] characters, int offset, int length) throws IOException {
        if (nonNull(appendable)) {
            characters = requireNonNull(characters, "May not be null: characters");
            if (offset < 0) {
                throw new IndexOutOfBoundsException("To small: offset");
            }
            if (offset > characters.length) {
                throw new IndexOutOfBoundsException("To large: offset");
            }
            if (length < 0) {
                throw new IndexOutOfBoundsException("To small: length");
            }
            int size = offset + length;
            if (size > characters.length) {
                throw new IndexOutOfBoundsException("To large: offset + length");
            }
            if (size < 0) {
                throw new IndexOutOfBoundsException("To small: offset + length");
            }
            if (length == 0) {
                return;
            }
            for (int index = offset; index < size; index++) {
                appendable.append(characters[index]);
            }
        }
    }

    @Override
    public void flush() throws IOException {

    }

    @Override
    public String toString() {
        return nonNull(appendable) ? appendable.toString() : null;
    }

    @Override
    public void close() {
        this.appendable = null;
    }
}
