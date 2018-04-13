package com.nullgr.corelibrary.date

import org.joda.time.DateTime
import org.joda.time.Interval
import org.joda.time.LocalDateTime
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Check if this [Date] it today
 *
 * @return [Boolean] result of check
 */
fun Date.isToday(): Boolean {
    val today = DateTime().withTimeAtStartOfDay()
    val tomorrow = today.plusDays(1).withTimeAtStartOfDay()
    val interval = Interval(today, tomorrow)
    return interval.contains(this.time)
}

/**
 * Check if this [Date] it yesterday
 *
 * @return [Boolean] result of check
 */
fun Date.isYesterday(): Boolean {
    val today = DateTime().withTimeAtStartOfDay()
    val yesterday = today.minusDays(1).withTimeAtStartOfDay()
    val interval = Interval(yesterday, today)
    return interval.contains(this.time)
}

/**
 * Returns the date minus the [count] of months from the current
 * @param count The number of months that need to be taken away
 * @return [Date]
 */
infix fun Date.minusMonths(count: Int): Date {
    return DateTime(this.time).minusMonths(count).toDate()
}

/**
 * Returns the date plus the [count] of months from the current
 * @param count The number of months that need to be added
 * @return [Date]
 */
infix fun Date.plusMonths(count: Int): Date {
    return DateTime(this.time).plusMonths(count).toDate()
}

/**
 * Returns the date plus the [count] of days from the current
 * @param count The number of days that need to be added
 * @return [Date]
 */
infix fun Date.plusDay(count: Int): Date {
    return DateTime(this.time).plusDays(count).toDate()
}

/**
 * Returns the date minus the [count] of days from the current
 * @param count The number of days that need to be taken away
 * @return [Date]
 */
infix fun Date.minusDay(count: Int): Date {
    return DateTime(this.time).minusDays(count).toDate()
}

/**
 * Returns the date plus the [count] of any time units, based on given [TimeUnit] from the current date.
 * The given [count] will be transformed to milliseconds by using [TimeUnit.toMillis] and result will be added to original date.
 *
 * For example:
 * ```
 * Date().plusAny(TimeUnit.HOURS,2)
 * ```
 * or
 * ```
 * Date().plusAny(TimeUnit.DAYS,1)
 * ```
 * @param count The number of something that need to be added
 * @param timeUnit Time unit representing some type of time units, like [TimeUnit.SECONDS], [TimeUnit.HOURS] etc.
 * @return [Date]
 */
fun Date.plusAny(timeUnit: TimeUnit, count: Long): Date {
    return DateTime(this.time).plusMillis(timeUnit.toMillis(count).toInt()).toDate()
}

/**
 * Returns the date minus the [count] of any time units, based on given [TimeUnit] from the current date.
 * The given [count] will be transformed to milliseconds by using [TimeUnit.toMillis]
 * and result will be taken away from original date.
 *
 * For example:
 * ```
 * Date().minusAny(TimeUnit.HOURS,2)
 * ```
 * or
 * ```
 * Date().minusAny(TimeUnit.DAYS,1)
 * ```
 * @param count The number of something that need to be added
 * @param timeUnit Time unit representing some type of time units, like [TimeUnit.SECONDS], [TimeUnit.HOURS] etc.
 * @return [Date]
 */
fun Date.minusAny(timeUnit: TimeUnit, count: Long): Date {
    return DateTime(this.time).minusMillis(timeUnit.toMillis(count).toInt()).toDate()
}

/**
 * Returns the date with time at start of day
 * @return [Date]
 */
fun Date.withoutTime(): Date {
    return LocalDateTime(this.time)
            .withTime(0, 0, 0, 0)
            .toDate()
}

/**
 * Check if this [Date] is between [dateStart] and [dateEnd]
 * @param dateStart start [Date] of interval
 * @param dateEnd end [Date] of interval
 * @return [Boolean] result of check
 */
fun Date.isInRange(dateStart: Date, dateEnd: Date): Boolean {
    return Interval(DateTime(dateStart), DateTime(dateEnd)).contains(this.time)
}

/**
 * Create new [Date] from long representation of date/time
 *
 * For example:
 * ```
 * System.currentTimeMillis().toDate()
 * ```
 * @return [Date]
 */
fun Long.toDate(): Date {
    return Date(this)
}