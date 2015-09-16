/*
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
package no.thrums.loader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.function.Function;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

/**
 * @author Kristian Myrhaug
 */
public interface Loader {

    <Output> Output loadFile(Function<Reader,Output> read, File file);
    <Output> Output loadPath(Function<Reader,Output> read, String path);
    <Output> Output loadReader(Function<Reader,Output> read, Reader reader);
    <Output> Output loadResource(Function<Reader,Output> read, String resource);
    <Output> Output loadUri(Function<Reader,Output> read, URI uri);
    <Output> Output loadUrl(Function<Reader,Output> read, URL url);

}
