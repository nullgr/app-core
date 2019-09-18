package com.nullgr.core.date

import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.LocalTime
import org.threeten.bp.ZonedDateTime
import java.util.Calendar
import java.util.Date

/**
 * Check if this [LocalDate] it today
 *
 * @return [Boolean] result of check
 */
inline fun LocalDate.isToday(): Boolean = LocalDate.now() == this

/**
 * Check if this [Date] it yesterday
 *
 * @return [Boolean] result of check
 */
inline fun LocalDate.isYesterday(): Boolean = LocalDate.now().minusDays(1) == this

/**
 * Adjust this [LocalDateTime] object to start of date.
 *
 * @return [LocalDateTime] with [LocalTime.MIDNIGHT]
 */
inline fun LocalDateTime.atStartOfDay() = this.with(LocalTime.MIDNIGHT)

/**
 * Adjust this [LocalDateTime] object to end of date.
 *
 * @return [LocalDateTime] with [LocalTime.MAX]
 */
inline fun LocalDateTime.atEndOfDay() = this.with(LocalTime.MAX)

/**
 * Adjust this [ZonedDateTime] object to start of date.
 *
 * @return [ZonedDateTime] with [LocalTime.MIDNIGHT]
 */
inline fun ZonedDateTime.atStartOfDay() = this.with(LocalTime.MIDNIGHT)

/**
 * Adjust this [ZonedDateTime] object to end of date.
 *
 * @return [ZonedDateTime] with [LocalTime.MAX]
 */
inline fun ZonedDateTime.atEndOfDay() = this.with(LocalTime.MAX)

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

/**
 * Converts this [Date] object to [Long] that represents timestamp in seconds
 *
 * @return [Long]
 */
fun Date.toTimestamp(): Long = this.time / 1000

/**
 * Creates new [Date] from long that represents timestamp in seconds
 *
 * @return [Date]
 */
fun Long.dateFromTimestamp(): Date = Date(this * 1000)

/**
 * Compares [field] of this [Date] object with [other].
 * [field] can be of [Calendar.ERA]..[Calendar.DST_OFFSET]
 *
 * @return True if [field] is same in this and other [Date] objects
 */
fun Date.areFieldsTheSame(other: Date, field: Int): Boolean {
    val c = Calendar.getInstance()
    val thisField = c.also { it.time = this }[field]
    val otherField = c.also { it.time = other }[field]
    return thisField == otherField
}