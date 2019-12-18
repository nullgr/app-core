package com.nullgr.core.date

import org.junit.Assert.*
import org.junit.Test
import org.threeten.bp.format.DateTimeFormatter
import java.util.Locale

/**
 * @author Grishko Nikita
 */
class DateFormatExtensionsTest {

    @Test
    fun simpleDateFormatterCache_SetAndGetFormatter_SameAndEquals() {
        val dateFormat = DateTimeFormatter.ofPattern(CommonFormats.FORMAT_STANDARD_DATE_FULL_MILLIS_UTC)

        val cacheKey = CommonFormats.FORMAT_STANDARD_DATE_FULL_MILLIS_UTC
        DateTimeFormatterCache[cacheKey] = dateFormat
        assertSame(dateFormat, DateTimeFormatterCache[cacheKey])
        assertEquals(dateFormat, DateTimeFormatterCache[cacheKey])
    }

    @Test
    fun getOrCreateFormatter_TheSameFormat_TheSameLocale_SameAndEqual() {
        val dateFormat1 = DateTimeFormatterCache.getOrCreateFormatter(CommonFormats.FORMAT_SIMPLE_DATE)
        val dateFormat2 = DateTimeFormatterCache.getOrCreateFormatter(CommonFormats.FORMAT_SIMPLE_DATE)
        assertSame("getOrCreateFormatter function, must return equals formatter for equal format and time zone",
            dateFormat1, dateFormat2)
        assertEquals(dateFormat1, dateFormat2)
    }

    @Test
    fun getOrCreateFormatter_DifferentFormats_SameLocale_NotSameAndNotEqual() {
        val dateFormat1 = DateTimeFormatterCache.getOrCreateFormatter(CommonFormats.FORMAT_SIMPLE_DATE_TIME)
        val dateFormat2 = DateTimeFormatterCache.getOrCreateFormatter(CommonFormats.FORMAT_SIMPLE_DATE)
        assertNotSame("getOrCreateFormatter function, must return different formatter for diff format and equal time zone",
            dateFormat1, dateFormat2)
        assertNotEquals(dateFormat1, dateFormat2)
    }

    @Test
    fun getOrCreateFormatter_SameFormat_SameLocale_SameAndEqual() {
        val dateFormat1 = DateTimeFormatterCache.getOrCreateFormatter(CommonFormats.FORMAT_SIMPLE_DATE)
        val dateFormat2 = DateTimeFormatterCache.getOrCreateFormatter(CommonFormats.FORMAT_SIMPLE_DATE)
        assertSame("getOrCreateFormatter function, must return equal formatter for equal format and null time zone",
            dateFormat1, dateFormat2)
        assertEquals(dateFormat1, dateFormat2)
    }

    @Test
    fun getOrCreateFormatter_SameFormat_DifferentLocale_NotEquals() {
        val dateFormat1 = DateTimeFormatterCache.getOrCreateFormatter(CommonFormats.FORMAT_SIMPLE_DATE, Locale.ENGLISH)
        val dateFormat2 = DateTimeFormatterCache.getOrCreateFormatter(CommonFormats.FORMAT_SIMPLE_DATE, Locale.FRANCE)
        assertNotEquals("getOrCreateFormatter function, must return different formatter for different locale",
            dateFormat1, dateFormat2)
    }
}