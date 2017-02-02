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
package no.thrums.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

/**
 * @author Kristian Myrhaug
 * @since 2015-09-21
 */
public class CharSequenceInputStream<T extends CharSequence> extends InputStream {

    private T charSequence;
    private int index;
    private int mark;

    public CharSequenceInputStream(T charSequence) {
        this.charSequence = charSequence;
    }

    public T getCharSequence() {
        return charSequence;
    }

    @Override
    public boolean markSupported() {
        return true;
    }

    @Override
    public void mark(int readAheadLimit) {
        mark = index;
    }

    @Override
    public void reset() throws IOException {
        index = mark;
    }

    @Override
    public int read() throws IOException {
        if (nonNull(charSequence) && index < charSequence.length()) {
            return charSequence.charAt(index++);
        }
        return -1;
    }

    @Override
    public void close() throws IOException {
        charSequence = null;
        index = 0;
        mark = 0;
    }
}
