package no.thrums.validation.engine.helper.number;

import java.math.BigDecimal;
import java.math.BigInteger;

import static java.util.Objects.nonNull;

/**
 * @author Kristian Myrhaug
 * @since 2015-12-18
 */
public class NumberTransformer {

    public BigDecimal toBigDecimal(Number number) {
        if (nonNull(number)) {
            if (number instanceof Byte || number instanceof Short || number instanceof Integer || number instanceof Long) {
                return BigDecimal.valueOf(number.longValue());
            } else if (number instanceof Float || number instanceof Double) {
                return BigDecimal.valueOf(number.doubleValue());
            } else if (number instanceof BigInteger) {
                return new BigDecimal((BigInteger)number);
            } else if (number instanceof BigDecimal) {
                return (BigDecimal)number;
            }
        }
        return null;
    }
}
