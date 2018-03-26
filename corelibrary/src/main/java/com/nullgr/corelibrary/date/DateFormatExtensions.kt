package com.nullgr.corelibrary.date

import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Grishko Nikita on 01.02.18.
 */
object SimpleDateFormatterCache {
    private val cache = WeakHashMap<String, SimpleDateFormat?>()

    operator fun get(name: String): SimpleDateFormat? {
        return cache[name]
    }

    operator fun set(name: String, typeface: SimpleDateFormat) {
        cache[name] = typeface
    }
}

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

fun getOrCreateFormatter(dateFormat: String, timeZone: TimeZone? = null): SimpleDateFormat {
    val key = "$dateFormat${timeZone?.displayName ?: ""}"
    var format = SimpleDateFormatterCache[key]
    if (format == null) {
        format = SimpleDateFormat(dateFormat, Locale.getDefault()).apply {
            timeZone?.let { this.timeZone = it }
        }
        SimpleDateFormatterCache[key] = format
    }
    return format
}

fun Date.toStringWithFormat(dateFormat: String, timeZone: TimeZone? = null): String {
    return getOrCreateFormatter(dateFormat, timeZone)
            .format(this)
}

fun Date.toStringWithFormat(format: SimpleDateFormat): String =
        format.format(this)


fun String.toDate(format: String, timeZone: TimeZone? = null): Date? {
    return try {
        getOrCreateFormatter(format, timeZone).parse(this)
    } catch (t: Throwable) {
        null
    }
}

fun String.toDate(format: SimpleDateFormat): Date? {
    return try {
        format.parse(this)
    } catch (t: Throwable) {
        null
    }
}



