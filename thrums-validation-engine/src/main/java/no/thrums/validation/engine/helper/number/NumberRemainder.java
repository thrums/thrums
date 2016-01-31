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
package no.thrums.validation.engine.helper.number;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

/**
 * @author Kristian Myrhaug
 * @since 2014-10-16
 */
public class NumberRemainder {

    public Number remainder(Number first, Number second) {
        first = Objects.requireNonNull(first, "First argument may not be null");
        second = Objects.requireNonNull(second, "Second argument may not be null");
        if (first instanceof Byte || first instanceof Short || first instanceof Integer || first instanceof Long) {
            if (second instanceof Byte || second instanceof Short || second instanceof Integer || second instanceof Long) {
                return remainder(first.longValue(), second.longValue());
            } else if (second instanceof Float || second instanceof Double) {
                return remainder(first.longValue(), second.doubleValue());
            } else if (second instanceof BigInteger) {
                return remainder(first.longValue(), (BigInteger) second);
            } else if (second instanceof BigDecimal) {
                return remainder(first.longValue(), (BigDecimal) second);
            }
        } else if (first instanceof Float || first instanceof Double) {
            if (second instanceof Byte || second instanceof Short || second instanceof Integer || second instanceof Long) {
                return remainder(first.doubleValue(), second.longValue());
            } else if (second instanceof Float || second instanceof Double) {
                return remainder(first.doubleValue(), second.doubleValue());
            } else if (second instanceof BigInteger) {
                return remainder(first.doubleValue(), (BigInteger) second);
            } else if (second instanceof BigDecimal) {
                return remainder(first.doubleValue(), (BigDecimal) second);
            }
        } else if (first instanceof BigInteger) {
            if (second instanceof Byte || second instanceof Short || second instanceof Integer || second instanceof Long) {
                return remainder((BigInteger) first, second.longValue());
            } else if (second instanceof Float || second instanceof Double) {
                return remainder((BigInteger) first, second.doubleValue());
            } else if (second instanceof BigInteger) {
                return remainder((BigInteger) first, (BigInteger) second);
            } else if (second instanceof BigDecimal) {
                return remainder((BigInteger) first, (BigDecimal) second);
            }
        } else if (first instanceof BigDecimal) {
            if (second instanceof Byte || second instanceof Short || second instanceof Integer || second instanceof Long) {
                return remainder((BigDecimal) first, second.longValue());
            } else if (second instanceof Float || second instanceof Double) {
                return remainder((BigDecimal) first, second.doubleValue());
            } else if (second instanceof BigInteger) {
                return remainder((BigDecimal) first, (BigInteger) second);
            } else if (second instanceof BigDecimal) {
                return remainder((BigDecimal) first, (BigDecimal) second);
            }
        }
        throw new IllegalStateException("This should never happen");
    }

    public BigDecimal remainder(long numerator, long denominator) {
        return remainder(BigDecimal.valueOf(numerator), BigDecimal.valueOf(denominator));
    }

    public BigDecimal remainder(long numerator, BigInteger denominator) {
        return remainder(BigDecimal.valueOf(numerator), new BigDecimal(denominator));
    }

    public BigDecimal remainder(long numerator, double denominator) {
        return remainder(BigDecimal.valueOf(numerator), BigDecimal.valueOf(denominator));
    }

    public BigDecimal remainder(long numerator, BigDecimal denominator) {
        return remainder(BigDecimal.valueOf(numerator), denominator);
    }

    public BigDecimal remainder(BigInteger numerator, long denominator) {
        return remainder(new BigDecimal(numerator), BigDecimal.valueOf(denominator));
    }

    public BigDecimal remainder(BigInteger numerator, BigInteger denominator) {
        return remainder(new BigDecimal(numerator), new BigDecimal(denominator));
    }

    public BigDecimal remainder(BigInteger numerator, double denominator) {
        return remainder(new BigDecimal(numerator), BigDecimal.valueOf(denominator));
    }

    public BigDecimal remainder(BigInteger numerator, BigDecimal denominator) {
        return remainder(new BigDecimal(numerator), denominator);
    }

    public BigDecimal remainder(double numerator, long denominator) {
        return remainder(BigDecimal.valueOf(numerator), BigDecimal.valueOf(denominator));
    }

    public BigDecimal remainder(double numerator, BigInteger denominator) {
        return remainder(BigDecimal.valueOf(numerator), new BigDecimal(denominator));
    }

    public BigDecimal remainder(double numerator, double denominator) {
        return remainder(BigDecimal.valueOf(numerator), BigDecimal.valueOf(denominator));
    }

    public BigDecimal remainder(double numerator, BigDecimal denominator) {
        return remainder(BigDecimal.valueOf(numerator), denominator);
    }

    public BigDecimal remainder(BigDecimal numerator, long denominator) {
        return remainder(numerator, BigDecimal.valueOf(denominator));
    }

    public BigDecimal remainder(BigDecimal numerator, BigInteger denominator) {
        return remainder(numerator, new BigDecimal(denominator));
    }

    public BigDecimal remainder(BigDecimal numerator, double denominator) {
        return remainder(numerator, BigDecimal.valueOf(denominator));
    }

    public BigDecimal remainder(BigDecimal numerator, BigDecimal denominator) {
        return numerator.remainder(denominator);
    }
}
