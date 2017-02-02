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
package no.thrums.instance.ri;

import no.thrums.mapper.Mapper;
import no.thrums.instance.Instance;
import no.thrums.instance.InstanceFactory;
import no.thrums.instance.InstanceLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Objects;

/**
 * @author Kristian Myrhaug
 * @since 2015-02-22
 */
public class RiInstanceLoader implements InstanceLoader {

    private final Mapper mapper;

    public RiInstanceLoader(Mapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public Instance loadFile(InstanceFactory instanceFactory, File file) {
        try (InputStream inputStream = new FileInputStream(file)) {
            try (Reader reader = new InputStreamReader(inputStream)) {
                return loadReader(instanceFactory, reader);
            }
        } catch (IOException cause) {
            throw new RuntimeException("Could not load file", cause);
        }
    }

    @Override
    public Instance loadPath(InstanceFactory instanceFactory, String path) {
        return loadFile(instanceFactory, new File(path));
    }

    @Override
    public Instance loadReader(InstanceFactory instanceFactory, Reader reader) {
        return instanceFactory.defined(mapper.read(reader, Object.class));
    }

    @Override
    public Instance loadResource(InstanceFactory instanceFactory, String resource) {;
        URL url = getUrl(Thread.currentThread().getContextClassLoader(), resource);
        if (Objects.isNull(url)) {
            throw new RuntimeException("Could not load resource", new IOException("Resource not found: " + resource));
        }
        return loadUrl(instanceFactory, url);
    }

    @Override
    public Instance loadUri(InstanceFactory instanceFactory, URI uri) {
        RuntimeException runtimeException = null;
        try {
            return loadUrl(instanceFactory, uri.toURL());
        } catch (MalformedURLException cause) {
            runtimeException = new RuntimeException("Could not load uri", cause);
        }
        if ("classpath".equals(uri.getScheme())) {
            return loadResource(instanceFactory, uri.getPath());
        }
        if ("file".equals(uri.getScheme())) {
            return loadPath(instanceFactory, uri.getPath());
        }
        throw runtimeException;
    }

    @Override
    public Instance loadUrl(InstanceFactory instanceFactory, URL url) {
        try (InputStream inputStream = url.openConnection().getInputStream()) {
            try (Reader reader = new InputStreamReader(inputStream)) {
                return loadReader(instanceFactory, reader);
            }
        } catch (IOException cause) {
            throw new RuntimeException("Could not load url: " + url, cause);
        }
    }

    public URL getUrl(ClassLoader classLoader, String resource) {
        if (Objects.isNull(resource)) {
            return null;
        }
        URL url = null;
        if (resource.startsWith("/")) {
            resource = resource.substring(1);
        }
        if (Objects.isNull(classLoader)) {
            url = ClassLoader.getSystemResource(resource);
        } else {
            url = classLoader.getResource(resource);
        }
        if (Objects.isNull(url)) {
            url = RiInstanceLoader.class.getClassLoader().getResource(resource);
        }
        return url;
    }


}
