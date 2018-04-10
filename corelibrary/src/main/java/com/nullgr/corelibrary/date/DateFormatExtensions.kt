package com.nullgr.corelibrary.date

import android.support.annotation.Nullable
import android.support.annotation.VisibleForTesting
import android.util.LruCache
import java.text.SimpleDateFormat
import java.util.*

/**
 * Class to cache and provide SimpleDateFormat objects.
 * @author Grishko Nikita
 */
@VisibleForTesting
private object SimpleDateFormatterCache {
    private val cache = LruCache<String, SimpleDateFormat?>(16)

    operator fun get(name: String): SimpleDateFormat? {
        return cache[name]
    }

    operator fun set(name: String, typeface: SimpleDateFormat) {
        cache.put(name, typeface)
    }

    fun getOrCreateFormatter(dateFormat: String, timeZone: TimeZone? = null, locale: Locale? = Locale.getDefault()): SimpleDateFormat {
        val key = "$dateFormat${locale?.displayName ?: ""}${timeZone?.displayName ?: ""}"
        var format = this[key]
        if (format == null) {
            format = SimpleDateFormat(dateFormat, locale ?: Locale.getDefault()).apply {
                timeZone?.let { this.timeZone = it }
            }
            this[key] = format
        }
        return format
    }
}

/**
 * Simple class which contains number of common and wide useful date formats.
 * @author Grishko Nikita
 */
object CommonFormats {
    const val FORMAT_SIMPLE_DATE_TIME_SECONDS = "dd.MM.yyyy HH:mm:ss"
    const val FORMAT_SIMPLE_DATE_TIME = "dd.MM.yyyy HH:mm"
    const val FORMAT_SIMPLE_DATE = "dd.MM.yyyy"
    const val FORMAT_DATE_WITH_MONTH_NAME = "dd LLL yyyy"
    const val FORMAT_STANDARD_DATE_TIME_SECONDS = "yyyy-MM-dd HH:mm:ss"
    const val FORMAT_STANDARD_DATE_TIME = "yyyy-MM-dd HH:mm"
    const val FORMAT_STANDARD_DATE = "yyyy-MM-dd"
    const val FORMAT_STANDARD_DATE_FULL_UTC = "yyyy-MM-dd'T'HH:mm:ss'Z'"
    const val FORMAT_STANDARD_DATE_FULL_MILLIS_UTC = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"

    const val FORMAT_TIME = "HH:mm"
    const val FORMAT_TIME_2 = "HH:mm:ss"
}

/**
 * Format [Date] to date/time [String] with given format pattern.
 * SimpleDateFormatter objects will be created once and cached by params [dateFormat], [timeZone] and [locale].
 * For different params will be cached different objects
 *
 * @param dateFormat the pattern describing the date and time format
 * @param timeZone the given new time zone. Can be null
 * @param locale the locale whose date format symbols should be used. Can be null
 *
 * @return formatted date/time [String]
 * 
 * @author Grishko Nikita
 */
fun Date.toStringWithFormat(dateFormat: String, @Nullable timeZone: TimeZone? = null, @Nullable locale: Locale? = Locale.getDefault()): String {
    return SimpleDateFormatterCache.getOrCreateFormatter(dateFormat, timeZone, locale)
            .format(this)
}

/**
 * Format [Date] to date/time [String] with given instance of [SimpleDateFormat] without caching.
 *
 * @param format instance of [SimpleDateFormat]
 *
 * @return formatted date/time [String]
 */
fun Date.toStringWithFormat(format: SimpleDateFormat): String =
        format.format(this)

/**
 * Parse [String] to [Date] with given format pattern.
 * SimpleDateFormatter objects for parsing given string will be created
 * once and cached by params [dateFormat], [timeZone] and [locale].
 * For different params will be cached different objects
 *
 * @param dateFormat the pattern describing the date and time format
 * @param timeZone the given new time zone. Can be null
 * @param locale the locale whose date format symbols should be used. Can be null
 *
 * @return parsed [Date] object or <b>null</b> if something goes wrong while parsing date
 */
fun String.toDate(dateFormat: String, @Nullable timeZone: TimeZone? = null, @Nullable locale: Locale? = Locale.getDefault()): Date? {
    return try {
        SimpleDateFormatterCache.getOrCreateFormatter(dateFormat, timeZone, locale).parse(this)
    } catch (t: Throwable) {
        null
    }
}

/**
 *  Parse [String] to [Date] with given [SimpleDateFormat] instance, without caching.
 *
 * @param format instance of [SimpleDateFormat]
 *
 * @return parsed [Date] object or <b>null</b> if something goes wrong while parsing date
 */
fun String.toDate(format: SimpleDateFormat): Date? {
    return try {
        format.parse(this)
    } catch (t: Throwable) {
        null
    }
}