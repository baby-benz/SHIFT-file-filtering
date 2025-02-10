package ru.cft.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static ru.cft.util.NumericUtil.getNumberType;

class GetNumberTypeTest {
    @Test
    void regular_integer() {
        Assertions.assertEquals(NumericUtil.NumberType.INTEGER, getNumberType("1"));
    }

    @Test
    void long_integer() {
        assertEquals(NumericUtil.NumberType.INTEGER, getNumberType("1L"));
        assertEquals(NumericUtil.NumberType.INTEGER, getNumberType("1l"));
    }

    @Test
    void scientific_notation_integer() {
        assertEquals(NumericUtil.NumberType.INTEGER, getNumberType("1E1"));
        assertEquals(NumericUtil.NumberType.INTEGER, getNumberType("1e1"));
    }

    @Test
    void sign_integer() {
        assertEquals(NumericUtil.NumberType.INTEGER, getNumberType("+1"));
        assertEquals(NumericUtil.NumberType.INTEGER, getNumberType("-1"));
    }

    @Test
    void octal_integer() {
        assertEquals(NumericUtil.NumberType.OCTAL_INTEGER, getNumberType("0757"));
    }

    @Test
    void hexadecimal_integer() {
        assertEquals(NumericUtil.NumberType.HEX_INTEGER, getNumberType("0x1EF"));
        assertEquals(NumericUtil.NumberType.HEX_INTEGER, getNumberType("0XBBB"));
        assertEquals(NumericUtil.NumberType.HEX_INTEGER, getNumberType("0xff"));
    }

    @Test
    void regular_floating() {
        assertEquals(NumericUtil.NumberType.FLOATING_POINT, getNumberType("1.1"));
    }

    @Test
    void double_float_floating() {
        assertEquals(NumericUtil.NumberType.FLOATING_POINT, getNumberType("1.1D"));
        assertEquals(NumericUtil.NumberType.FLOATING_POINT, getNumberType("1.1d"));
        assertEquals(NumericUtil.NumberType.FLOATING_POINT, getNumberType("1.1F"));
        assertEquals(NumericUtil.NumberType.FLOATING_POINT, getNumberType("1.1f"));
    }

    @Test
    void no_digit_after_dot_floating() {
        assertEquals(NumericUtil.NumberType.FLOATING_POINT, getNumberType("1."));
    }

    @Test
    void scientific_notation_floating() {
        assertEquals(NumericUtil.NumberType.FLOATING_POINT, getNumberType("1.1E10"));
        assertEquals(NumericUtil.NumberType.FLOATING_POINT, getNumberType("1.1e10"));
        assertEquals(NumericUtil.NumberType.FLOATING_POINT, getNumberType("1.1e-1"));
        assertEquals(NumericUtil.NumberType.FLOATING_POINT, getNumberType("1e-1"));
        assertEquals(NumericUtil.NumberType.FLOATING_POINT, getNumberType("100e-3"));
        assertEquals(NumericUtil.NumberType.FLOATING_POINT, getNumberType("1e-8"));
    }

    @Test
    void octal_floating() {
        assertEquals(NumericUtil.NumberType.FLOATING_POINT, getNumberType("08.125"));
    }

    @Test
    void weird_numbers_not_a_number() {
        assertEquals(NumericUtil.NumberType.NOT_A_NUMBER, getNumberType("1+1"));
        assertEquals(NumericUtil.NumberType.NOT_A_NUMBER, getNumberType("1E-"));
        assertEquals(NumericUtil.NumberType.NOT_A_NUMBER, getNumberType("1.1L"));
        assertEquals(NumericUtil.NumberType.NOT_A_NUMBER, getNumberType("1E1L"));
        assertEquals(NumericUtil.NumberType.NOT_A_NUMBER, getNumberType("L"));
        assertEquals(NumericUtil.NumberType.NOT_A_NUMBER, getNumberType("l"));
        assertEquals(NumericUtil.NumberType.NOT_A_NUMBER, getNumberType("1.1.1"));
        assertEquals(NumericUtil.NumberType.NOT_A_NUMBER, getNumberType("1e1.1.1"));
        assertEquals(NumericUtil.NumberType.NOT_A_NUMBER, getNumberType("1.1e11e1"));
        assertEquals(NumericUtil.NumberType.NOT_A_NUMBER, getNumberType(".e1"));
        assertEquals(NumericUtil.NumberType.NOT_A_NUMBER, getNumberType("."));
        assertEquals(NumericUtil.NumberType.NOT_A_NUMBER, getNumberType("F"));
        assertEquals(NumericUtil.NumberType.NOT_A_NUMBER, getNumberType("f"));
        assertEquals(NumericUtil.NumberType.NOT_A_NUMBER, getNumberType("D"));
        assertEquals(NumericUtil.NumberType.NOT_A_NUMBER, getNumberType("d"));
        assertEquals(NumericUtil.NumberType.NOT_A_NUMBER, getNumberType("b"));
        assertEquals(NumericUtil.NumberType.NOT_A_NUMBER, getNumberType("0x/"));
    }

    @Test
    void scientific_notation_no_digit_after_exponent_not_a_number() {
        assertEquals(NumericUtil.NumberType.NOT_A_NUMBER, getNumberType("1.1E"));
        assertEquals(NumericUtil.NumberType.NOT_A_NUMBER, getNumberType("1E"));
        assertEquals(NumericUtil.NumberType.NOT_A_NUMBER, getNumberType("1.1e"));
        assertEquals(NumericUtil.NumberType.NOT_A_NUMBER, getNumberType("1e"));
    }

    @Test
    void binary_not_a_number() {
        assertEquals(NumericUtil.NumberType.NOT_A_NUMBER, getNumberType("0b111101111"));
    }

    @Test
    void bad_octal_not_a_number() {
        assertEquals(NumericUtil.NumberType.NOT_A_NUMBER, getNumberType("018"));
        assertEquals(NumericUtil.NumberType.NOT_A_NUMBER, getNumberType("017/"));
    }

    @Test
    void bad_hexadecimal_not_a_number() {
        assertEquals(NumericUtil.NumberType.NOT_A_NUMBER, getNumberType("0X"));
        assertEquals(NumericUtil.NumberType.NOT_A_NUMBER, getNumberType("0xg"));
        assertEquals(NumericUtil.NumberType.NOT_A_NUMBER, getNumberType("0XRT"));
    }

    @Test
    void empty_or_null_string_not_a_number() {
        assertEquals(NumericUtil.NumberType.NOT_A_NUMBER, getNumberType(""));
        assertEquals(NumericUtil.NumberType.NOT_A_NUMBER, getNumberType(null));
    }
}