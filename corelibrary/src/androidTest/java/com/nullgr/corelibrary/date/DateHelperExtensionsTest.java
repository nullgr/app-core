package com.nullgr.corelibrary.date;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static com.nullgr.corelibrary.date.DateHelperExtensionsKt.isInRange;
import static com.nullgr.corelibrary.date.DateHelperExtensionsKt.isToday;
import static com.nullgr.corelibrary.date.DateHelperExtensionsKt.isYesterday;
import static com.nullgr.corelibrary.date.DateHelperExtensionsKt.minusAny;
import static com.nullgr.corelibrary.date.DateHelperExtensionsKt.minusDay;
import static com.nullgr.corelibrary.date.DateHelperExtensionsKt.minusMonths;
import static com.nullgr.corelibrary.date.DateHelperExtensionsKt.plusAny;
import static com.nullgr.corelibrary.date.DateHelperExtensionsKt.plusDay;
import static com.nullgr.corelibrary.date.DateHelperExtensionsKt.plusMonths;
import static com.nullgr.corelibrary.date.DateHelperExtensionsKt.toDate;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by Grishko Nikita on 01.02.18.
 */
@RunWith(AndroidJUnit4.class)
public class DateHelperExtensionsTest {

    @Test
    public void testToday() {
        Date date = new Date();
        assertTrue(isToday(date));
    }

    @Test
    public void testYesterday() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -1);

        assertTrue(isYesterday(calendar.getTime()));
    }

    @Test
    public void testPlusMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.MONTH, Calendar.AUGUST);

        Date augustDate = calendar.getTime();
        Date resultDate = plusMonths(augustDate, 2);

        calendar.setTime(resultDate);

        assertTrue(calendar.get(Calendar.MONTH) == Calendar.OCTOBER);
    }

    @Test
    public void testMinusMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.MONTH, Calendar.AUGUST);

        Date augustDate = calendar.getTime();
        Date resultDate = minusMonths(augustDate, 2);

        calendar.setTime(resultDate);

        assertTrue(calendar.get(Calendar.MONTH) == Calendar.JUNE);
    }

    @Test
    public void testPlusAny() {
        Date date = new Date();
        long millis = date.getTime();

        Date next = plusAny(date, TimeUnit.SECONDS, 10);

        assertTrue(next.getTime() - millis == 10 * 1000);
    }

    @Test
    public void testMinusAny() {
        Date date = new Date();
        long millis = date.getTime();

        Date prev = minusAny(date, TimeUnit.MINUTES, 5);

        assertTrue(millis - prev.getTime() == 5 * 60 * 1000);
    }

    @Test
    public void testIsInRangeSuccess() {
        Date today = new Date();
        Date plus10Date = plusDay(today, 10);
        Date plus3Date = plusDay(today, 3);
        assertTrue(isInRange(plus3Date, today, plus10Date));
    }

    @Test
    public void testIsInRangeFailed() {
        Date today = new Date();
        Date plus10Date = plusDay(today, 10);
        Date minus3Date = minusDay(today, 3);
        assertFalse(isInRange(minus3Date, today, plus10Date));
    }

    @Test
    public void testToDate() {
        Date date = new Date();
        long millis = date.getTime();
        assertEquals(date, toDate(millis));
    }
}
