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
import java.io.OutputStream;

import static java.util.Objects.nonNull;

/**
 * @author Kristian Myrhaug
 * @since 2015-10-06
 */
public class AppendableOutputStream<T extends Appendable> extends OutputStream {

    private T appendable;

    public AppendableOutputStream(T appendable) {
        this.appendable = appendable;
    }

    public T getAppendable() {
        return appendable;
    }

    @Override
    public void write(int b) throws IOException {
        if (nonNull(appendable)) {
            appendable.append((char) b);
        }
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
