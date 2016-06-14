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
package no.thrums.instance.ri.helper;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

import static java.util.Objects.nonNull;

/**
 * @author Kristian Myrhaug
 * @since 2016-06-02
 */
public class NumberUtilities {

    private NumberUtilities() {}

    public static int compare(Number first, Number second) {
        first = Objects.requireNonNull(first,"First argument may not be null");
        second = Objects.requireNonNull(second, "Second argument may not be null");
        if (first instanceof Byte || first instanceof Short || first instanceof Integer || first instanceof Long) {
            if (second instanceof Byte || second instanceof Short || second instanceof Integer || second instanceof Long) {
                return NumberUtilities.compare(first.longValue(), second.longValue());
            } else if (second instanceof Float || second instanceof Double) {
                return NumberUtilities.compare(first.longValue(), second.doubleValue());
            } else if (second instanceof BigInteger) {
                return NumberUtilities.compare(first.longValue(), (BigInteger)second);
            } else if (second instanceof BigDecimal) {
                return NumberUtilities.compare(first.longValue(), (BigDecimal)second);
            }
        } else if (first instanceof Float || first instanceof Double) {
            if (second instanceof Byte || second instanceof Short || second instanceof Integer || second instanceof Long) {
                return NumberUtilities.compare(first.doubleValue(), second.longValue());
            } else if (second instanceof Float || second instanceof Double) {
                return NumberUtilities.compare(first.doubleValue(), second.doubleValue());
            } else if (second instanceof BigInteger) {
                return NumberUtilities.compare(first.doubleValue(), (BigInteger)second);
            } else if (second instanceof BigDecimal) {
                return NumberUtilities.compare(first.doubleValue(), (BigDecimal)second);
            }
        } else if (first instanceof BigInteger) {
            if (second instanceof Byte || second instanceof Short || second instanceof Integer || second instanceof Long) {
                return NumberUtilities.compare((BigInteger)first, second.longValue());
            } else if (second instanceof Float || second instanceof Double) {
                return NumberUtilities.compare((BigInteger)first, second.doubleValue());
            } else if (second instanceof BigInteger) {
                return NumberUtilities.compare((BigInteger)first, (BigInteger)second);
            } else if (second instanceof BigDecimal) {
                return NumberUtilities.compare((BigInteger)first, (BigDecimal)second);
            }
        } else if (first instanceof BigDecimal) {
            if (second instanceof Byte || second instanceof Short || second instanceof Integer || second instanceof Long) {
                return NumberUtilities.compare((BigDecimal)first, second.longValue());
            } else if (second instanceof Float || second instanceof Double) {
                return NumberUtilities.compare((BigDecimal)first, second.doubleValue());
            } else if (second instanceof BigInteger) {
                return NumberUtilities.compare((BigDecimal)first, (BigInteger)second);
            } else if (second instanceof BigDecimal) {
                return NumberUtilities.compare((BigDecimal)first, (BigDecimal)second);
            }
        }
        throw new IllegalStateException("This should never happen");
    }

    public static int compare(Long number, Long minimum) {
        return number.compareTo(minimum) ;
    }

    public static int compare(Long number, BigInteger minimum) {
        return BigInteger.valueOf(number).compareTo(minimum) ;
    }

    public static int compare(Long number, Double minimum) {
        return Double.valueOf(number).compareTo(minimum) ;
    }

    public static int compare(Long number, BigDecimal minimum) {
        return BigDecimal.valueOf(number).compareTo(minimum) ;
    }

    public static int compare(BigInteger number, Long minimum) {
        return number.compareTo(BigInteger.valueOf(minimum)) ;
    }

    public static int compare(BigInteger number, BigInteger minimum) {
        return number.compareTo(minimum) ;
    }

    public static int compare(BigInteger number, Double minimum) {
        return new BigDecimal(number).compareTo(BigDecimal.valueOf(minimum)) ;
    }

    public static int compare(BigInteger number, BigDecimal minimum) {
        return new BigDecimal(number).compareTo(minimum) ;
    }

    public static int compare(Double number, Long minimum) {
        return number.compareTo(Double.valueOf(minimum)) ;
    }

    public static int compare(Double number, BigInteger minimum) {
        return BigDecimal.valueOf(number).compareTo(new BigDecimal(minimum)) ;
    }

    public static int compare(Double number, Double minimum) {
        return number.compareTo(minimum) ;
    }

    public static int compare(Double number, BigDecimal minimum) {
        return BigDecimal.valueOf(number).compareTo(minimum) ;
    }

    public static int compare(BigDecimal number, Long minimum) {
        return number.compareTo(BigDecimal.valueOf(minimum)) ;
    }

    public static int compare(BigDecimal number, BigInteger minimum) {
        return number.compareTo(new BigDecimal(minimum)) ;
    }

    public static int compare(BigDecimal number, Double minimum) {
        return number.compareTo(new BigDecimal(minimum)) ;
    }

    public static int compare(BigDecimal number, BigDecimal minimum) {
        return number.compareTo(minimum) ;
    }



    public static Number remainder(Number first, Number second) {
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

    public static BigDecimal remainder(long numerator, long denominator) {
        return remainder(BigDecimal.valueOf(numerator), BigDecimal.valueOf(denominator));
    }

    public static BigDecimal remainder(long numerator, BigInteger denominator) {
        return remainder(BigDecimal.valueOf(numerator), new BigDecimal(denominator));
    }

    public static BigDecimal remainder(long numerator, double denominator) {
        return remainder(BigDecimal.valueOf(numerator), BigDecimal.valueOf(denominator));
    }

    public static BigDecimal remainder(long numerator, BigDecimal denominator) {
        return remainder(BigDecimal.valueOf(numerator), denominator);
    }

    public static BigDecimal remainder(BigInteger numerator, long denominator) {
        return remainder(new BigDecimal(numerator), BigDecimal.valueOf(denominator));
    }

    public static BigDecimal remainder(BigInteger numerator, BigInteger denominator) {
        return remainder(new BigDecimal(numerator), new BigDecimal(denominator));
    }

    public static BigDecimal remainder(BigInteger numerator, double denominator) {
        return remainder(new BigDecimal(numerator), BigDecimal.valueOf(denominator));
    }

    public static BigDecimal remainder(BigInteger numerator, BigDecimal denominator) {
        return remainder(new BigDecimal(numerator), denominator);
    }

    public static BigDecimal remainder(double numerator, long denominator) {
        return remainder(BigDecimal.valueOf(numerator), BigDecimal.valueOf(denominator));
    }

    public static BigDecimal remainder(double numerator, BigInteger denominator) {
        return remainder(BigDecimal.valueOf(numerator), new BigDecimal(denominator));
    }

    public static BigDecimal remainder(double numerator, double denominator) {
        return remainder(BigDecimal.valueOf(numerator), BigDecimal.valueOf(denominator));
    }

    public static BigDecimal remainder(double numerator, BigDecimal denominator) {
        return remainder(BigDecimal.valueOf(numerator), denominator);
    }

    public static BigDecimal remainder(BigDecimal numerator, long denominator) {
        return remainder(numerator, BigDecimal.valueOf(denominator));
    }

    public static BigDecimal remainder(BigDecimal numerator, BigInteger denominator) {
        return remainder(numerator, new BigDecimal(denominator));
    }

    public static BigDecimal remainder(BigDecimal numerator, double denominator) {
        return remainder(numerator, BigDecimal.valueOf(denominator));
    }

    public static BigDecimal remainder(BigDecimal numerator, BigDecimal denominator) {
        return numerator.remainder(denominator);
    }

    public static BigDecimal toBigDecimal(Number number) {
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
            return BigDecimal.valueOf(number.doubleValue());
        }
        return null;
    }
}
