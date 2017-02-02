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
package no.thrums.jts;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.PrecisionModel;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import no.thrums.io.CharSequenceReader;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

/**
 * @author Kristian Myrhaug
 * @since 2015-09-21
 */
public class EwktReader {

    public static final String PARAMETER_SEPARATOR = ";";
    public static final String KEY_VALUE_SEPARATOR = "=";
    public static final String SRID_KEY = "SRID";

    public Geometry read(Reader reader) {
        try {
            StringBuilder wellKnownText = toStringBuilder(reader);
            int semiColonIndex = wellKnownText.lastIndexOf(";");
            CharSequence extension = null;
            if (semiColonIndex > -1) {
                extension = wellKnownText.subSequence(0, semiColonIndex);
                wellKnownText = new StringBuilder(wellKnownText.subSequence(semiColonIndex + 1, wellKnownText.length()));
            }
            try (CharSequenceReader<StringBuilder> charSequenceReader = new CharSequenceReader<>(wellKnownText)) {
                Map<String,String> userData = readUserData(extension);
                Integer srid = getIgnoreCase(userData, SRID_KEY);
                GeometryFactory geometryFactory = null;
                if (nonNull(srid)) {
                    geometryFactory = new GeometryFactory(new PrecisionModel(PrecisionModel.FLOATING), srid);
                } else {
                    geometryFactory = new GeometryFactory(new PrecisionModel(PrecisionModel.FLOATING));
                }
                Geometry geometry = new WKTReader(geometryFactory).read(charSequenceReader);
                geometry.setUserData(userData);
                return geometry;
            } catch (ParseException cause) {
                throw new RuntimeException("Could not parse extended well-known text", cause);
            }
        } catch (IOException cause) {
            throw new RuntimeException("Could not read extended well-known text", cause);
        }
    }

    public Geometry read(String string) {
        try (StringReader stringReader = new StringReader(string)){
            return read(stringReader);
        }
    }

    public Map<String, String> readUserData(CharSequence extension) throws ParseException {
        Map<String, String> userData = null;
        if (nonNull(extension)) {
            String[] parameters = extension.toString().split(PARAMETER_SEPARATOR);
            for (int index = 0; index < parameters.length; index++) {
                String[] entry = parameters[index].split(KEY_VALUE_SEPARATOR);
                if (entry.length != 2) {
                    throw new ParseException("Expected key=value, found: " + parameters[index]);
                }
                if (isNull(userData)) {
                    userData = new HashMap<>();
                }
                userData.put(entry[0].trim(), entry[1].trim());
            }
        }
        return userData;
    }

    private Integer getIgnoreCase(Map<String,String> userData, String key){
        if (nonNull(userData)) {
            for (Map.Entry<String,String> entry : userData.entrySet()) {
                if (key.equalsIgnoreCase(entry.getKey())) {
                    return Integer.valueOf(entry.getValue());
                }
            }
        }
        return null;
    }

    private StringBuilder toStringBuilder(Reader reader) throws IOException {
        StringBuilder wellKnownText = new StringBuilder();
        char[] characterBuffer = new char[4096];
        for (int read = reader.read(characterBuffer, 0, characterBuffer.length); read > -1; read = reader.read(characterBuffer, 0, characterBuffer.length)) {
            wellKnownText.append(characterBuffer, 0, read);
        }
        return wellKnownText;
    }
}
