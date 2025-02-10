package ru.cft.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class NumericUtil {
    public enum NumberType {
        INTEGER, HEX_INTEGER, OCTAL_INTEGER, FLOATING_POINT, NOT_A_NUMBER
    }

    /**
     * Проверяет является ли строка целочисленным числом <p>
     * При этом шестнадцатеричная, восьмеричная, а также экспоненциальная формы записи числа не считаются целочисленными.
     * Для обработки всех перечисленных форм записи используйте {@link NumericUtil#getNumberType(String)}
     *
     * @param str строка, которая проверяется
     * @return true - целое число, false - не целое, согласно правилам
     */
    public static boolean isSimpleInteger(String str) {
        if (str == null || str.isEmpty()) return false;

        boolean signed = (str.charAt(0) == '-' || str.charAt(0) == '+');

        if (signed && str.length() == 1) return false;

        for (int i = signed ? 1 : 0; i < str.length(); i++) {
            if (Character.digit(str.charAt(i), 10) < 0) return false;
        }

        return true;
    }

    /**
     * Проверяет является ли строка числом с плавающей точкой<p>
     * При этом шестнадцатеричная, восьмеричная, а также экспоненциальная формы записи числа не считаются целочисленными.
     * Для обработки всех перечисленных форм записи используйте {@link NumericUtil#getNumberType(String)}
     *
     * @param str строка, которая проверяется
     * @return true - число с плавающей точкой, false - не является числом с плавающей точкой, согласно правилам
     */
    public static boolean isSimpleFloatingPointNumber(String str) {
        if (str == null || str.isEmpty() || str.charAt(str.length() - 1) == '.') return false;

        int decimalPoints = 0;
        for (int i = 0; i < str.length(); i++) {
            boolean isSign = str.charAt(i) == '+' || str.charAt(i) == '-';
            if (isSign) {
                if (i == 0) {
                    if (str.length() == 1) return false;
                } else return false;
            }

            boolean isDecimalPoint = str.charAt(i) == '.';
            if (isDecimalPoint) {
                if (decimalPoints == 1) return false;
                decimalPoints++;
            }

            if (!isSign && !isDecimalPoint && Character.digit(str.charAt(i), 10) < 0) return false;
        }

        return true;
    }

    /**
     * Возвращает тип числа, который хранится в переданной строке<p>
     * Поддерживает восьмеричный целочисленный, шестнадцатеричный целочисленный, научный (1.2E10),
     * Java (100L, 10.d) форматы записи чисел
     *
     * @param str строка, для которой нужно получить тип числа
     * @return <b>NumberType.INTEGER</b> - число целочисленное<p>
     * <b>NumberType.FLOATING_TYPE</b> - число с плавающей точкой<p>
     * <b>NumberType.NOT_A_NUMBER</b> - строка не является числом
     */
    public static NumberType getNumberType(final String str) {
        if (str == null || str.isEmpty()) return NumberType.NOT_A_NUMBER;

        int sz = str.length();
        boolean hasExp = false;
        boolean hasDecPoint = false;
        boolean allowSigns = false;
        boolean foundDigit = false;
        boolean exponentPositive = false;
        int exponentIdx = 0;

        final int start = str.charAt(0) == '-' || str.charAt(0) == '+' ? 1 : 0;
        if (sz > start + 1 && str.charAt(start) == '0' && !str.contains(".")) {
            if (str.charAt(start + 1) == 'x' || str.charAt(start + 1) == 'X') {
                int i = start + 2;
                if (i == sz) return NumberType.NOT_A_NUMBER;
                for (; i < str.length(); i++) {
                    if ((str.charAt(i) < '0' || str.charAt(i) > '9')
                            && (str.charAt(i) < 'a' || str.charAt(i) > 'f')
                            && (str.charAt(i) < 'A' || str.charAt(i) > 'F')) {
                        return NumberType.NOT_A_NUMBER;
                    }
                }
                return NumberType.HEX_INTEGER;
            }
            if (Character.isDigit(str.charAt(start + 1))) {
                int i = start + 1;
                for (; i < str.length(); i++) {
                    if (str.charAt(i) < '0' || str.charAt(i) > '7') {
                        return NumberType.NOT_A_NUMBER;
                    }
                }
                return NumberType.OCTAL_INTEGER;
            }
        }

        sz--;
        int i = start;
        while (i < sz) {
            if (str.charAt(i) >= '0' && str.charAt(i) <= '9') {
                foundDigit = true;
                allowSigns = false;
            } else if (str.charAt(i) == '.') {
                if (hasDecPoint || hasExp) return NumberType.NOT_A_NUMBER;
                hasDecPoint = true;
            } else if (str.charAt(i) == 'e' || str.charAt(i) == 'E') {
                if (hasExp) return NumberType.NOT_A_NUMBER;
                if (!foundDigit) return NumberType.NOT_A_NUMBER;
                hasExp = true;
                allowSigns = true;
            } else if (str.charAt(i) == '+' || str.charAt(i) == '-') {
                if (!allowSigns) return NumberType.NOT_A_NUMBER;
                exponentIdx = i + 1;
                exponentPositive = str.charAt(i) == '+';
                allowSigns = false;
                foundDigit = false;
            } else {
                return NumberType.NOT_A_NUMBER;
            }

            i++;
        }

        if (str.charAt(i) >= '0' && str.charAt(i) <= '9') {
            if (hasDecPoint) {
                return NumberType.FLOATING_POINT;
            }
            if (hasExp && !allowSigns && !exponentPositive) {
                long exponent = Long.parseLong(str.substring(exponentIdx));
                if (exponent >= exponentIdx - 2) {
                    return NumberType.FLOATING_POINT;
                }
            }
            return NumberType.INTEGER;
        }
        if (str.charAt(i) == 'e' || str.charAt(i) == 'E') {
            return NumberType.NOT_A_NUMBER;
        }
        if (str.charAt(i) == '.') {
            if (hasDecPoint || hasExp) return NumberType.NOT_A_NUMBER;
            return foundDigit ? NumberType.FLOATING_POINT : NumberType.NOT_A_NUMBER;
        }
        if (!allowSigns && (str.charAt(i) == 'd' || str.charAt(i) == 'D' || str.charAt(i) == 'f' || str.charAt(i) == 'F')) {
            return foundDigit ? NumberType.FLOATING_POINT : NumberType.NOT_A_NUMBER;
        }
        if (str.charAt(i) == 'l' || str.charAt(i) == 'L') {
            return (foundDigit && !hasExp && !hasDecPoint) ? NumberType.INTEGER : NumberType.NOT_A_NUMBER;
        }

        return NumberType.NOT_A_NUMBER;
    }
}
