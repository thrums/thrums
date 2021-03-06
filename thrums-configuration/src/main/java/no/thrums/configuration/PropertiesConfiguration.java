/*

 */
package no.thrums.configuration;

import javax.inject.Inject;
import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.Period;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.Properties;
import java.util.Set;
import java.util.function.Function;

import static java.util.Objects.nonNull;

/**
 * @author Kristian Myrhaug
 */
public class PropertiesConfiguration implements Configuration {

    private Properties properties;

    @Inject
    public PropertiesConfiguration(Properties properties) {
        this.properties = properties;
    }
    
    @Override
    public Set<String> getNames() {
        return nonNull(properties) ? properties.stringPropertyNames() : Collections.emptySet();
    }

    @Override
    public Boolean getBoolean(String name) {
        return getBoolean(name, null);
    }

    @Override
    public Boolean getBoolean(String name, Boolean defaultValue){
        return getValue(name, Boolean::valueOf, defaultValue);
    }

    @Override
    public Byte getByte(String name) {
        return getByte(name, null);
    }

    @Override
    public Byte getByte(String name, Byte defaultValue) {
        return getValue(name, Byte::valueOf, defaultValue);
    }

    @Override
    public Short getShort(String name) {
        return getShort(name, null);
    }

    @Override
    public Short getShort(String name, Short defaultValue) {
        return getValue(name, Short::valueOf, defaultValue);
    }

    @Override
    public Integer getInteger(String name) {
        return getInteger(name, null);
    }

    @Override
    public Integer getInteger(String name, Integer defaultValue) {
        return getValue(name, Integer::valueOf, defaultValue);
    }

    @Override
    public BigInteger getBigInteger(String name) {
        return getBigInteger(name, null);
    }

    @Override
    public BigInteger getBigInteger(String name, BigInteger defaultValue) {
        return getValue(name, BigInteger::new, defaultValue);
    }

    @Override
    public Long getLong(String name) {
        return getLong(name, null);
    }

    @Override
    public Long getLong(String name, Long defaultValue) {
        return getValue(name, Long::valueOf, defaultValue);
    }

    @Override
    public Float getFloat(String name) {
        return getFloat(name, null);
    }

    @Override
    public Float getFloat(String name, Float defaultValue) {
        return getValue(name, Float::valueOf, defaultValue);
    }

    @Override
    public Double getDouble(String name) {
        return getDouble(name, null);
    }

    @Override
    public Double getDouble(String name, Double defaultValue) {
        return getValue(name, Double::valueOf, defaultValue);
    }

    @Override
    public BigDecimal getBigDecimal(String name) {
        return getBigDecimal(name, null);
    }

    @Override
    public BigDecimal getBigDecimal(String name, BigDecimal defaultValue) {
        return getValue(name, BigDecimal::new, defaultValue);
    }

    @Override
    public Character getCharacter(String name) {
        return getCharacter(name, null);
    }

    @Override
    public Character getCharacter(String name, Character defaultValue) {
        return getValue(name, string -> {
            if (string.length() > 1) {
                throw new IllegalArgumentException("String is to long: " + string.length());
            } else if (string.length() < 1) {
                throw new IllegalArgumentException("String is to short: " + string.length());
            }
            return string.charAt(0);
        }, defaultValue);
    }

    @Override
    public String getString(String name) {
        return getString(name, null);
    }

    @Override
    public String getString(String name, String defaultValue) {
        if (nonNull(properties)) {
            return properties.getProperty(name, defaultValue);
        }
        return defaultValue;
    }

    @Override
    public URI getUri(String name) {
        return getUri(name, null);
    }

    @Override
    public URI getUri(String name, URI defaultValue) {
        return getValue(name, string -> {
            try {
                return new URI(string);
            } catch (URISyntaxException cause) {
                throw new IllegalArgumentException("String is not an unified resource identifier: " + string);
            }
        }, defaultValue);
    }

    @Override
    public URL getUrl(String name) {
        return getUrl(name, null);
    }

    @Override
    public URL getUrl(String name, URL defaultValue) {
        return getValue(name, string -> {
            try {
                return new URL(string);
            } catch (MalformedURLException cause) {
                throw new IllegalArgumentException("String is not an unified resource locator: " + string);
            }
        }, defaultValue);
    }

    @Override
    public <T extends Enum<T>> T getEnumeration(Class<T> enumClass, String name) {
        return getEnumeration(enumClass, name, null);
    }

    @Override
    public <T extends Enum<T>> T getEnumeration(Class<T> enumClass, String name, T defaultValue) {
        return getValue(name, string -> Enum.valueOf(enumClass, string), defaultValue);
    }

    @Override
    public LocalTime getLocalTime(String name) {
        return getLocalTime(name, null);
    }

    @Override
    public LocalTime getLocalTime(String name, LocalTime defaultValue) {
        return getValue(name, LocalTime::parse, defaultValue);
    }

    @Override
    public OffsetTime getOffsetTime(String name) {
        return getOffsetTime(name, null);
    }

    @Override
    public OffsetTime getOffsetTime(String name, OffsetTime defaultValue) {
        return getValue(name, OffsetTime::parse, defaultValue);
    }

    @Override
    public LocalDate getLocalDate(String name) {
        return getLocalDate(name, null);
    }

    @Override
    public LocalDate getLocalDate(String name, LocalDate defaultValue) {
        return getValue(name, LocalDate::parse, defaultValue);
    }

    @Override
    public LocalDateTime getLocalDateTime(String name) {
        return getLocalDateTime(name, null);
    }

    @Override
    public LocalDateTime getLocalDateTime(String name, LocalDateTime defaultValue) {
        return getValue(name, LocalDateTime::parse, defaultValue);
    }

    @Override
    public OffsetDateTime getOffsetDateTime(String name) {
        return getOffsetDateTime(name, null);
    }

    @Override
    public OffsetDateTime getOffsetDateTime(String name, OffsetDateTime defaultValue) {
        return getValue(name, OffsetDateTime::parse, defaultValue);
    }

    @Override
    public ZonedDateTime getZonedDateTime(String name) {
        return getZonedDateTime(name, null);
    }

    @Override
    public ZonedDateTime getZonedDateTime(String name, ZonedDateTime defaultValue) {
        return getValue(name, ZonedDateTime::parse, defaultValue);
    }

    @Override
    public Instant getInstant(String name) {
        return getInstant(name, null);
    }

    @Override
    public Instant getInstant(String name, Instant defaultValue) {
        return getValue(name, Instant::parse, defaultValue);
    }

    @Override
    public Duration getDuration(String name) {
        return getDuration(name, null);
    }

    @Override
    public Duration getDuration(String name, Duration defaultValue) {
        return getValue(name, Duration::parse, defaultValue);
    }

    @Override
    public Period getPeriod(String name) {
        return getPeriod(name, null);
    }

    @Override
    public Period getPeriod(String name, Period defaultValue) {
        return getValue(name, Period::parse, defaultValue);
    }

    public <Output> Output getValue(String name, Function<String, Output> parse, Output defaultValue) {
        String value = getString(name, null);
        if (nonNull(value)) {
            return parse.apply(value);
        }
        return defaultValue;
    }

    public static Function<Reader,Properties> read = reader -> {
        Properties properties = new Properties();
        try {
            properties.load(reader);
        } catch (IOException cause) {
            throw new RuntimeException("Could not load properties", cause);
        }
        return properties;
    };
}
