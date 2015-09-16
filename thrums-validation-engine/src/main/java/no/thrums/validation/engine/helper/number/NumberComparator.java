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
package no.thrums.validation.engine.helper.number;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Comparator;
import java.util.Objects;

/**
 * @author Kristian Myrhaug
 * @since 2014-10-16
 */
public class NumberComparator implements Comparator<Number> {
    
    @Override
    public int compare(Number first, Number second) {
        first = Objects.requireNonNull(first,"First argument may not be null");
        second = Objects.requireNonNull(second, "Second argument may not be null");
        if (first instanceof Byte || first instanceof Short || first instanceof Integer || first instanceof Long) {
            if (second instanceof Byte || second instanceof Short || second instanceof Integer || second instanceof Long) {
                return compare(first.longValue(), second.longValue());
            } else if (second instanceof Float || second instanceof Double) {
                return compare(first.longValue(), second.doubleValue());
            } else if (second instanceof BigInteger) {
                return compare(first.longValue(), (BigInteger)second);
            } else if (second instanceof BigDecimal) {
                return compare(first.longValue(), (BigDecimal)second);
            }
        } else if (first instanceof Float || first instanceof Double) {
            if (second instanceof Byte || second instanceof Short || second instanceof Integer || second instanceof Long) {
                return compare(first.doubleValue(), second.longValue());
            } else if (second instanceof Float || second instanceof Double) {
                return compare(first.doubleValue(), second.doubleValue());
            } else if (second instanceof BigInteger) {
                return compare(first.doubleValue(), (BigInteger)second);
            } else if (second instanceof BigDecimal) {
                return compare(first.doubleValue(), (BigDecimal)second);
            }
        } else if (first instanceof BigInteger) {
            if (second instanceof Byte || second instanceof Short || second instanceof Integer || second instanceof Long) {
                return compare((BigInteger)first, second.longValue());
            } else if (second instanceof Float || second instanceof Double) {
                return compare((BigInteger)first, second.doubleValue());
            } else if (second instanceof BigInteger) {
                return compare((BigInteger)first, (BigInteger)second);
            } else if (second instanceof BigDecimal) {
                return compare((BigInteger)first, (BigDecimal)second);
            }
        } else if (first instanceof BigDecimal) {
            if (second instanceof Byte || second instanceof Short || second instanceof Integer || second instanceof Long) {
                return compare((BigDecimal)first, second.longValue());
            } else if (second instanceof Float || second instanceof Double) {
                return compare((BigDecimal)first, second.doubleValue());
            } else if (second instanceof BigInteger) {
                return compare((BigDecimal)first, (BigInteger)second);
            } else if (second instanceof BigDecimal) {
                return compare((BigDecimal)first, (BigDecimal)second);
            }
        }
        throw new IllegalStateException("This should never happen");
    }

    public int compare(Long number, Long minimum) {
        return number.compareTo(minimum) ;
    }

    public int compare(Long number, BigInteger minimum) {
        return BigInteger.valueOf(number).compareTo(minimum) ;
    }

    public int compare(Long number, Double minimum) {
        return Double.valueOf(number).compareTo(minimum) ;
    }

    public int compare(Long number, BigDecimal minimum) {
        return BigDecimal.valueOf(number).compareTo(minimum) ;
    }

    public int compare(BigInteger number, Long minimum) {
        return number.compareTo(BigInteger.valueOf(minimum)) ;
    }

    public int compare(BigInteger number, BigInteger minimum) {
        return number.compareTo(minimum) ;
    }

    public int compare(BigInteger number, Double minimum) {
        return new BigDecimal(number).compareTo(BigDecimal.valueOf(minimum)) ;
    }

    public int compare(BigInteger number, BigDecimal minimum) {
        return new BigDecimal(number).compareTo(minimum) ;
    }

    public int compare(Double number, Long minimum) {
        return number.compareTo(Double.valueOf(minimum)) ;
    }

    public int compare(Double number, BigInteger minimum) {
        return BigDecimal.valueOf(number).compareTo(new BigDecimal(minimum)) ;
    }

    public int compare(Double number, Double minimum) {
        return number.compareTo(minimum) ;
    }

    public int compare(Double number, BigDecimal minimum) {
        return BigDecimal.valueOf(number).compareTo(minimum) ;
    }

    public int compare(BigDecimal number, Long minimum) {
        return number.compareTo(BigDecimal.valueOf(minimum)) ;
    }

    public int compare(BigDecimal number, BigInteger minimum) {
        return number.compareTo(new BigDecimal(minimum)) ;
    }

    public int compare(BigDecimal number, Double minimum) {
        return number.compareTo(new BigDecimal(minimum)) ;
    }

    public int compare(BigDecimal number, BigDecimal minimum) {
        return number.compareTo(minimum) ;
    }
}
