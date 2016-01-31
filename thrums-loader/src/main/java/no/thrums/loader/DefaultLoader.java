/*
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
public class DefaultLoader implements Loader {

    @Override
    public <Output> Output loadFile(Function<Reader, Output> read, File file) {
        try (InputStream inputStream = new FileInputStream(file)) {
            try (Reader reader = new InputStreamReader(inputStream)) {
                return loadReader(read, reader);
            }
        } catch (IOException cause) {
            throw new RuntimeException("Could not load file: " + file.getAbsolutePath(), cause);
        }
    }

    @Override
    public <Output> Output loadPath(Function<Reader, Output> read, String path) {
        return loadFile(read, new File(path));
    }

    @Override
    public <Output> Output loadReader(Function<Reader, Output> read, Reader reader) {
        return read.apply(reader);
    }

    @Override
    public <Output> Output loadResource(Function<Reader, Output> read, String resource) {
        URL url = null;
        if (nonNull(resource)) {
            if (resource.startsWith("/")) {
                resource = resource.substring(1);
            }
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            if (isNull(classLoader)) {
                url = ClassLoader.getSystemResource(resource);
            } else {
                url = classLoader.getResource(resource);
            }
            if (isNull(url)) {
                url = this.getClass().getClassLoader().getResource(resource);
            }
        }
        if (isNull(url)) {
            throw new RuntimeException("Could not load resource", new IOException("Resource not found: " + resource));
        }
        return loadUrl(read, url);
    }

    @Override
    public <Output> Output loadUri(Function<Reader, Output> read, URI uri) {
        RuntimeException runtimeException = null;
        try {
            return loadUrl(read, uri.toURL());
        } catch (Exception cause) {
            runtimeException = new RuntimeException("Could not load uri", cause);
        }
        if ("classpath".equals(uri.getScheme())) {
            return loadResource(read, nonNull(uri.getAuthority()) ? uri.getAuthority() + uri.getPath() : uri.getPath());
        }
        if ("file".equals(uri.getScheme())) {
            return loadPath(read, nonNull(uri.getAuthority()) ? uri.getAuthority() + uri.getPath() : uri.getPath());
        }
        throw runtimeException;
    }

    @Override
    public <Output> Output loadUrl(Function<Reader, Output> read, URL url) {
        try (InputStream inputStream = url.openConnection().getInputStream()) {
            try (Reader reader = new InputStreamReader(inputStream)) {
                return loadReader(read, reader);
            }
        } catch (IOException cause) {
            throw new RuntimeException("Could not load url", cause);
        }
    }
}
