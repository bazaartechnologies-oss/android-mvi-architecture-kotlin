package com.tech.bazaar.template

import com.tech.bazaar.template.extention.doubleDigitString
import com.tech.bazaar.template.extention.formatNumber
import com.tech.bazaar.template.extention.getRoundPrice
import com.tech.bazaar.template.extention.parseDouble
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.ValueSource

class NumberExtTest {

    @Nested
    inner class FormatNumberProvidedAsInt {

        @ParameterizedTest
        @CsvSource(
            "1|1.0",
            "12|12.0",
            "12.0|12.0",
            "null|0.0",
            "''|0.0",
            delimiter = '|'
        )
        fun whenNumberIsLessThanZeroExpectFormattedString(number: String, expected: Double) {
            val actual = number.parseDouble()
            Assertions.assertEquals(expected, actual, "should match the expected double values")
        }

    }

    @Test
    fun whenNumberIsZeroExpectZeroString() {
        val expected = "0"
        val actual = formatNumber("0")
        Assertions.assertEquals(expected, actual, "should be zero string")
    }

    @ParameterizedTest
    @CsvSource(
        "1|1",
        "12|12",
        "123|123",
        "1234|1,234",
        "12345|12,345",
        "123456|123,456",
        "1234567|1,234,567",
        delimiter = '|'
    )
    fun whenNumberIsGreaterThanZeroExpectFormattedString(number: String, expected: String) {
        val actual = formatNumber(number)
        Assertions.assertEquals(expected, actual, "should match the expected formatted string")
    }

    @ParameterizedTest
    @CsvSource(
        "-1|-1",
        "-12|-12",
        "-123|-123",
        "-1234|-1,234",
        "-12345|-12,345",
        "-123456|-123,456",
        "-1234567|-1,234,567",
        delimiter = '|'
    )
    fun whenNumberIsLessThanZeroExpectFormattedString(number: String, expected: String) {
        val actual = formatNumber(number)
        Assertions.assertEquals(expected, actual, "should match the expected formatted string")
    }

    @Nested
    inner class FormatNumberProvidedAsString {
        @ParameterizedTest
        @ValueSource(strings = ["", " ", "   "])
        fun whenStringIsBlankExpectZeroString(value: String) {
            val expected = "0"
            val actual = formatNumber(value)
            Assertions.assertEquals(expected, actual, "should be zero string")
        }

        @Test
        fun whenStringIsZeroDoubleExpectZeroString() {
            val expected = "0"
            val actual = formatNumber("0.0")
            Assertions.assertEquals(expected, actual, "should be zero string")
        }

        @Test
        fun whenStringIsZeroIntExpectZeroString() {
            val expected = "0"
            val actual = formatNumber("0")
            Assertions.assertEquals(expected, actual, "should be zero string")
        }

        @ParameterizedTest
        @CsvSource(
            "1|1",
            "12|12",
            "123|123",
            "1234|1,234",
            "12345|12,345",
            "123456|123,456",
            "1234567|1,234,567",
            delimiter = '|'
        )
        fun whenNumberIsGreaterThanZeroExpectFormattedString(number: String, expected: String) {
            val actual = formatNumber(number)
            Assertions.assertEquals(expected, actual, "should match the expected formatted string")
        }

        @ParameterizedTest
        @CsvSource(
            "-1|-1",
            "-12|-12",
            "-123|-123",
            "-1234|-1,234",
            "-12345|-12,345",
            "-123456|-123,456",
            "-1234567|-1,234,567",
            delimiter = '|'
        )
        fun whenNumberIsLessThanZeroExpectFormattedString(number: String, expected: String) {
            val actual = formatNumber(number)
            Assertions.assertEquals(expected, actual, "should match the expected formatted string")
        }

        @ParameterizedTest
        @CsvSource(
            "1.0|1",
            "12.02|12.02",
            "123.03|123.03",
            "1234.04|1,234.04",
            "12345.05|12,345.05",
            "123456.1284|123,456.13",
            "1234567.112233|1,234,567.11",
            delimiter = '|'
        )
        fun whenNumberIsGreaterWithDecimalPointsThanZeroExpectFormattedString(
            number: String,
            expected: String
        ) {
            val actual = formatNumber(number)
            Assertions.assertEquals(expected, actual, "should match the expected formatted string")
        }
    }

    @Nested
    inner class GetRoundPrice {
        @ParameterizedTest
        @ValueSource(strings = ["", "0as", "012", "01a"])
        fun whenEmptyOrStartingWithZeroStringIsProvidedExpectZeroString(value: String) {
            val actual = getRoundPrice(value)
            Assertions.assertEquals("0", actual, "Should be a zero string")
        }

        @Test
        fun whenNullIsProvided() {
            val actual = getRoundPrice(null);
            Assertions.assertEquals("0", actual, "should be a zero string")
        }

        @ParameterizedTest
        @CsvSource(
            "1.0|1",
            "12.02|12.02",
            "123.03|123.03",
            "1234.04|1,234.04",
            "12345|12,345",
            "123456.1284|123,456.13",
            "1234567.112233|1,234,567.11",
            delimiter = '|'
        )
        fun whenNumberIsGreaterWithDecimalPointsThanZeroExpectFormattedString(
            number: String,
            expected: String
        ) {
            val actual = getRoundPrice(number)
            Assertions.assertEquals(expected, actual, "should match the expected formatted string")
        }
    }

    @ParameterizedTest
    @CsvSource(
        "0|00",
        "1|01",
        "2|02",
        "9|09",
        "11|11",
        "12|12",
        delimiter = '|'
    )
    fun doubleDigitString(
        number: Int,
        expected: String
    ) {
        val actual = number.doubleDigitString()
        Assertions.assertEquals(expected, actual, "should match the expected formatted string")
    }
} 