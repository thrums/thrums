/*

 */
package no.thrums.configuration;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
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
import java.util.Set;

/**
 * @author Kristian Myrhaug
 */
public interface Configuration {

    Set<String> getNames();

    Boolean getBoolean(String name, Boolean defaultValue);

    Byte getByte(String name, Byte defaultValue);
    Short getShort(String name, Short defaultValue);
    Integer getInteger(String name, Integer defaultValue);
    BigInteger getBigInteger(String name, BigInteger defaultValue);

    Long getLong(String name, Long defaultValue);
    Float getFloat(String name, Float defaultValue);
    Double getDouble(String name, Double defaultValue);
    BigDecimal getBigDecimal(String name, BigDecimal defaultValue);

    Character getCharacter(String name, Character defaultValue);
    String getString(String name, String defaultValue);

    URI getUri(String name, URI defaultValue);
    URL getUrl(String name, URL defaultValue);

    <T extends Enum<T>> T getEnumeration(Class<T> enumClass, String name, T defaultValue);

    LocalTime getLocalTime(String name, LocalTime defaultValue);
    OffsetTime getOffsetTime(String name, OffsetTime defaultValue);

    LocalDate getLocalDate(String name, LocalDate defaultValue);

    LocalDateTime getLocalDateTime(String name, LocalDateTime defaultValue);
    OffsetDateTime getOffsetDateTime(String name, OffsetDateTime defaultValue);
    ZonedDateTime getZonedDateTime(String name, ZonedDateTime defaultValue);

    Instant getInstant(String name, Instant defaultValue);

    Duration getDuration(String name, Duration defaultValue);
    Period getPeriod(String name, Period defaultValue);
}
