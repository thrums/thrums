/**
 Copyright 2014-2015 Kristian Myrhaug

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
package no.thrums.validation;

import no.thrums.validation.instance.Instance;
import no.thrums.validation.path.Path;

/**
 * @author Kristian Myrhaug
 * @since 2014-12-19
 */
public interface Violation {

    Path getPath();
    String getMessage();
    Instance getInstance();
    Instance getSchema();
}
