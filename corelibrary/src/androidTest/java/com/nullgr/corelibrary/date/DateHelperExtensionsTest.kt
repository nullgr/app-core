package com.nullgr.corelibrary.date

import android.support.test.runner.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by Grishko Nikita on 01.02.18.
 */
@RunWith(AndroidJUnit4::class)
class DateHelperExtensionsTest {

    @Test
    fun isToday_TheSameDate_True() {
        val date = Date()
        assertTrue(date.isToday())
    }

    @Test
    fun isYesterday_DateMinusOneDayFromThis_True() {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -1)

        assertTrue(calendar.time.isYesterday())
    }

    @Test
    fun plusMonth_DatePlusOneMonthFromThis_True() {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        calendar.set(Calendar.MONTH, Calendar.AUGUST)

        val augustDate = calendar.time
        val resultDate = augustDate.plusMonths(2)

        calendar.time = resultDate

        assertTrue(calendar.get(Calendar.MONTH) == Calendar.OCTOBER)
    }

    @Test
    fun testMinusMonth() {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        calendar.set(Calendar.MONTH, Calendar.AUGUST)

        val augustDate = calendar.time
        val resultDate = augustDate.minusMonths(2)

        calendar.time = resultDate

        assertTrue(calendar.get(Calendar.MONTH) == Calendar.JUNE)
    }

    @Test
    fun plusAny_PlusTenSeconds_True() {
        val date = Date()
        val millis = date.time

        val next = date.plusAny(TimeUnit.SECONDS, 10)

        assertTrue(next.time - millis == (10 * 1000).toLong())
    }

    @Test
    fun minusAny_MinusFiveMinutes_True() {
        val date = Date()
        val millis = date.time

        val prev = date.minusAny(TimeUnit.MINUTES, 5)

        assertTrue(millis - prev.time == (5 * 60 * 1000).toLong())
    }

    @Test
    fun isInRange_DatePlusThreeDaysInRangeFromTodayToPlusTenDays_True() {
        val today = Date()
        val plus10Date = today.plusDay(10)
        val plus3Date = today.plusDay(3)
        assertTrue(plus3Date.isInRange(today, plus10Date))
    }

    @Test
    fun isInRange_DateMinusThreeDaysInRangeFromTodayToPlusTenDays_False() {
        val today = Date()
        val plus10Date = today.plusDay(10)
        val minus3Date = today.minusDay(3)
        assertFalse(minus3Date.isInRange(today, plus10Date))
    }

    @Test
    fun toDate_MillisToDate_Equals() {
        val date = Date()
        val millis = date.time
        assertEquals(date, millis.toDate())
    }
}
