package com.nullgr.corelibrary.date

import org.joda.time.DateTime
import org.joda.time.Interval
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by Grishko Nikita on 01.02.18.
 */
fun Date.isToday(): Boolean {
    val today = DateTime().withTimeAtStartOfDay()
    val tomorrow = today.plusDays(1).withTimeAtStartOfDay()
    val interval = Interval(today, tomorrow)
    return interval.contains(this.time)
}

fun Date.isYesterday(): Boolean {
    val today = DateTime().withTimeAtStartOfDay()
    val yesterday = today.minusDays(1).withTimeAtStartOfDay()
    val interval = Interval(yesterday, today)
    return interval.contains(this.time)
}

fun Date.minusMonths(count: Int): Date {
    return DateTime(this.time).withTimeAtStartOfDay().minusMonths(count).toDate()
}

fun Date.plusMonths(count: Int): Date {
    return DateTime(this.time).withTimeAtStartOfDay().plusMonths(count).toDate()
}

fun Date.plusDay(count: Int): Date {
    return DateTime(this.time).withTimeAtStartOfDay().plusDays(count).toDate()
}

fun Date.minusDay(count: Int): Date {
    return DateTime(this.time).withTimeAtStartOfDay().minusDays(count).toDate()
}

fun Date.plusAny(timeUnit: TimeUnit, count: Long): Date {
    return DateTime(this.time).plusMillis(timeUnit.toMillis(count).toInt()).toDate()
}

fun Date.minusAny(timeUnit: TimeUnit, count: Long): Date {
    return DateTime(this.time).minusMillis(timeUnit.toMillis(count).toInt()).toDate()
}

fun Date.withoutTime(): Date {
    return DateTime(this.time).withTimeAtStartOfDay().toDate()
}

fun Date.isInRange(dateStart: Date, dateEnd: Date): Boolean {
    return Interval(DateTime(dateStart), DateTime(dateEnd)).contains(this.time)
}

fun Long.toDate(): Date {
    return Date(this)
}