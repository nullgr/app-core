package com.nullgr.core.date

import android.support.test.runner.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotSame
import org.junit.Assert.assertSame
import org.junit.Test
import org.junit.runner.RunWith
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author Grishko Nikita
 */
@RunWith(AndroidJUnit4::class)
class DateFormatExtensionsTest {

    @Test
    fun simpleDateFormatterCache_SetAndGetFormatter_SameAndEquals() {
        val utcTimeZone = TimeZone.getTimeZone("UTC")
        val dateFormat = SimpleDateFormat(CommonFormats.FORMAT_STANDARD_DATE_FULL_MILLIS_UTC, Locale.getDefault())
        dateFormat.timeZone = utcTimeZone

        val cacheKey = String.format("%s%s", CommonFormats.FORMAT_STANDARD_DATE_FULL_MILLIS_UTC, utcTimeZone.displayName)
        SimpleDateFormatterCache[cacheKey] = dateFormat
        assertSame(dateFormat, SimpleDateFormatterCache[cacheKey])
        assertEquals(dateFormat, SimpleDateFormatterCache[cacheKey])
    }

    @Test
    fun getOrCreateFormatter_TheSameFormat_TheSameTimeZone_TheSameLocale_SameAndEqual() {
        val dateFormat1 = SimpleDateFormatterCache.getOrCreateFormatter(CommonFormats.FORMAT_SIMPLE_DATE, TimeZone.getTimeZone("UTC"), Locale.getDefault())
        val dateFormat2 = SimpleDateFormatterCache.getOrCreateFormatter(CommonFormats.FORMAT_SIMPLE_DATE, TimeZone.getTimeZone("UTC"), Locale.getDefault())
        assertSame("getOrCreateFormatter function, must return equals formatter for equal format and time zone",
                dateFormat1, dateFormat2)
        assertEquals(dateFormat1, dateFormat2)
    }

    @Test
    fun getOrCreateFormatter_SameFormat_DifferentTimeZone_SameLocale_NotSameAndNotEqual() {
        val dateFormat1 = SimpleDateFormatterCache.getOrCreateFormatter(CommonFormats.FORMAT_SIMPLE_DATE, TimeZone.getDefault(), Locale.getDefault())
        val dateFormat2 = SimpleDateFormatterCache.getOrCreateFormatter(CommonFormats.FORMAT_SIMPLE_DATE, TimeZone.getTimeZone("UTC"), Locale.getDefault())
        assertNotSame("getOrCreateFormatter function, must return different formatter for equal format and diff time zone",
                dateFormat1, dateFormat2)
        assertNotEquals(dateFormat1, dateFormat2)
    }

    @Test
    fun getOrCreateFormatter_DifferentFormats_SameTimeZone_SameLocale_NotSameAndNotEqual() {
        val dateFormat1 = SimpleDateFormatterCache.getOrCreateFormatter(CommonFormats.FORMAT_SIMPLE_DATE_TIME, TimeZone.getDefault(), Locale.getDefault())
        val dateFormat2 = SimpleDateFormatterCache.getOrCreateFormatter(CommonFormats.FORMAT_SIMPLE_DATE, TimeZone.getDefault(), Locale.getDefault())
        assertNotSame("getOrCreateFormatter function, must return different formatter for diff format and equal time zone",
                dateFormat1, dateFormat2)
        assertNotEquals(dateFormat1, dateFormat2)
    }

    @Test
    fun getOrCreateFormatter_SameFormat_NullTimeZone_SameLocale_SameAndEqual() {
        val dateFormat1 = SimpleDateFormatterCache.getOrCreateFormatter(CommonFormats.FORMAT_SIMPLE_DATE, null, Locale.getDefault())
        val dateFormat2 = SimpleDateFormatterCache.getOrCreateFormatter(CommonFormats.FORMAT_SIMPLE_DATE, null, Locale.getDefault())
        assertSame("getOrCreateFormatter function, must return equal formatter for equal format and null time zone",
                dateFormat1, dateFormat2)
        assertEquals(dateFormat1, dateFormat2)
    }

    @Test
    fun getOrCreateFormatter_SameFormat_SameTimeZone_DifferentLocale_NotEquals() {
        val dateFormat1 = SimpleDateFormatterCache.getOrCreateFormatter(CommonFormats.FORMAT_SIMPLE_DATE, TimeZone.getDefault(), Locale.ENGLISH)
        val dateFormat2 = SimpleDateFormatterCache.getOrCreateFormatter(CommonFormats.FORMAT_SIMPLE_DATE, TimeZone.getDefault(), Locale.FRANCE)
        assertNotEquals("getOrCreateFormatter function, must return different formatter for different locale",
                dateFormat1, dateFormat2)
    }
}