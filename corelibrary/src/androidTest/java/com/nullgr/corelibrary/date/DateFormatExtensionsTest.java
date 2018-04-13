package com.nullgr.corelibrary.date;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

/**
 * Created by Grishko Nikita on 01.02.18.
 */
@RunWith(AndroidJUnit4.class)
public class DateFormatExtensionsTest {

    @Test
    public void testDateCache() {
        TimeZone utcTimeZone = TimeZone.getTimeZone("UTC");
        SimpleDateFormat dateFormat = new SimpleDateFormat(CommonFormats.FORMAT_STANDARD_DATE_FULL_MILLIS_UTC, Locale.getDefault());
        dateFormat.setTimeZone(utcTimeZone);

        String cacheKey = String.format("%s%s", CommonFormats.FORMAT_STANDARD_DATE_FULL_MILLIS_UTC, utcTimeZone.getDisplayName());
        SimpleDateFormatterCache.INSTANCE.set(cacheKey, dateFormat);
        assertSame(dateFormat, SimpleDateFormatterCache.INSTANCE.get(cacheKey));
        assertEquals(dateFormat, SimpleDateFormatterCache.INSTANCE.get(cacheKey));
    }

    @Test
    public void testGetOrCreateFormatterWithTheSameTimeZone() {
        SimpleDateFormat dateFormat1 = SimpleDateFormatterCache.INSTANCE.getOrCreateFormatter(CommonFormats.FORMAT_SIMPLE_DATE, TimeZone.getTimeZone("UTC"), Locale.getDefault());
        SimpleDateFormat dateFormat2 = SimpleDateFormatterCache.INSTANCE.getOrCreateFormatter(CommonFormats.FORMAT_SIMPLE_DATE, TimeZone.getTimeZone("UTC"), Locale.getDefault());
        assertSame("getOrCreateFormatter function, must return equals formatter for equal format and time zone",
                dateFormat1, dateFormat2);
        assertEquals(dateFormat1, dateFormat2);
    }

    @Test
    public void testGetOrCreateFormatterWithTheDifferentTimeZone() {
        SimpleDateFormat dateFormat1 = SimpleDateFormatterCache.INSTANCE.getOrCreateFormatter(CommonFormats.FORMAT_SIMPLE_DATE, TimeZone.getDefault(), Locale.getDefault());
        SimpleDateFormat dateFormat2 = SimpleDateFormatterCache.INSTANCE.getOrCreateFormatter(CommonFormats.FORMAT_SIMPLE_DATE, TimeZone.getTimeZone("UTC"), Locale.getDefault());
        assertNotSame("getOrCreateFormatter function, must return different formatter for equal format and diff time zone",
                dateFormat1, dateFormat2);
        assertNotEquals(dateFormat1, dateFormat2);
    }

    @Test
    public void testGetOrCreateFormatterWithTheDifferentFormats() {
        SimpleDateFormat dateFormat1 = SimpleDateFormatterCache.INSTANCE.getOrCreateFormatter(CommonFormats.FORMAT_SIMPLE_DATE_TIME, TimeZone.getDefault(), Locale.getDefault());
        SimpleDateFormat dateFormat2 = SimpleDateFormatterCache.INSTANCE.getOrCreateFormatter(CommonFormats.FORMAT_SIMPLE_DATE, TimeZone.getDefault(), Locale.getDefault());
        assertNotSame("getOrCreateFormatter function, must return different formatter for diff format and equal time zone",
                dateFormat1, dateFormat2);
        assertNotEquals(dateFormat1, dateFormat2);
    }

    @Test
    public void testGetOrCreateFormatterWithNullTimeZone() {
        SimpleDateFormat dateFormat1 = SimpleDateFormatterCache.INSTANCE.getOrCreateFormatter(CommonFormats.FORMAT_SIMPLE_DATE, null, Locale.getDefault());
        SimpleDateFormat dateFormat2 = SimpleDateFormatterCache.INSTANCE.getOrCreateFormatter(CommonFormats.FORMAT_SIMPLE_DATE, null, Locale.getDefault());
        assertSame("getOrCreateFormatter function, must return equal formatter for equal format and null time zone",
                dateFormat1, dateFormat2);
        assertEquals(dateFormat1, dateFormat2);
    }

    @Test
    public void testGetOrCreateFormatterWithDifferentLocale() {
        SimpleDateFormat dateFormat1 = SimpleDateFormatterCache.INSTANCE.getOrCreateFormatter(CommonFormats.FORMAT_SIMPLE_DATE, TimeZone.getDefault(), Locale.ENGLISH);
        SimpleDateFormat dateFormat2 = SimpleDateFormatterCache.INSTANCE.getOrCreateFormatter(CommonFormats.FORMAT_SIMPLE_DATE, TimeZone.getDefault(), Locale.FRANCE);
        assertNotEquals("getOrCreateFormatter function, must return different formatter for different locale",
                dateFormat1, dateFormat2);
    }
}