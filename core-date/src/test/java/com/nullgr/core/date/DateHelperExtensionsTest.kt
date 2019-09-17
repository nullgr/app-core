package com.nullgr.core.date

import org.junit.Assert.*
import org.junit.Test
import org.threeten.bp.LocalDate
import java.util.Date

/**
 * @author Grishko Nikita
 */
class DateHelperExtensionsTest {

    @Test
    fun isToday_TheSameDate_True() {
        val date = LocalDate.now()
        assertTrue(date.isToday())
    }

    @Test
    fun isYesterday_DateMinusOneDayFromThis_True() {
        val date = LocalDate.now().minusDays(1)
        assertTrue(date.isYesterday())
    }

    @Test
    fun toDate_MillisToDate_Equals() {
        val date = Date()
        val millis = date.time
        assertEquals(date, millis.toDate())
    }
}
