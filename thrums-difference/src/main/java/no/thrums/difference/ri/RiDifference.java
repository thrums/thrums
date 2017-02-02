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
package no.thrums.difference.ri;

import no.thrums.difference.Difference;
import no.thrums.instance.Instance;
import no.thrums.instance.path.Path;

import java.util.Objects;

import static java.util.Objects.requireNonNull;

/**
 * @author Kristian Myrhaug
 * @since 2016-06-05
 */
public class RiDifference implements Difference {

    private Path path;
    private Instance first;
    private Instance second;

    public RiDifference(Path path, Instance first, Instance second) {
        this.path = requireNonNull(path, "May not be null: path");
        this.first = requireNonNull(first, "May not be null: first");
        this.second = requireNonNull(second, "May not be null: second");
    }

    @Override
    public Path getPath() {
        return path;
    }

    @Override
    public Instance getFirst() {
        return first;
    }

    @Override
    public Instance getSecond() {
        return second;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof Difference && equals((Difference)other);
    }

    public boolean equals(Difference other) {
        return  Objects.equals(path, other.getPath()) &&
                Objects.equals(first, other.getFirst()) &&
                Objects.equals(second, other.getSecond());
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                path,
                first,
                second
        );
    }
}
