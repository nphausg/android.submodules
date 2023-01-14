/*
 * Created by nphau on 01/11/2021, 00:48
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 01/11/2021, 00:39
 */

package sg.nphau.android.shared.libs.timezone

import sg.nphau.android.shared.common.functional.CallBack
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.concurrent.schedule

object TimeUtils {

    private const val SECOND_MILLIS = 1_000
    private const val MINUTE_MILLIS = 60 * SECOND_MILLIS
    private const val HOUR_MILLIS = 60 * MINUTE_MILLIS
    private const val DAY_MILLIS = 24 * HOUR_MILLIS

    // “14/07/2018”
    private const val PATTERN = "dd/MM/yyyy"

    fun getDate(timestamp: Long, pattern: String = PATTERN): String {
        val dateFormat = SimpleDateFormat(pattern, Locale.getDefault())
        return dateFormat.format(Date(timestamp)).toString()
    }

    fun diffDays(dateFrom: Date, dateTo: Date): Int {
        val diff: Long = dateFrom.time - dateTo.time
        return (diff / 86400000).toInt()
    }

    fun diffDays(dateFromInMillis: Long, dateToInMillis: Long): Int {
        val diff: Long = dateFromInMillis - dateToInMillis
        return (diff / 86400000).toInt()
    }

    fun addDays(date: Date, days: Int): Date {
        val c = Calendar.getInstance()
        c.time = date
        c.add(Calendar.DAY_OF_YEAR, days)
        return c.time
    }

    fun isToday(timestamp: Long): Boolean {
        return diffDays(timestamp, System.currentTimeMillis()) == 0
    }

    fun getTimeAgo(timeUnit: Long?): String? {
        if (timeUnit == null) return null
        var time = timeUnit
        // if timestamp given in seconds, convert to millis
        if (time < 1000000000000L) {
            time *= 1000
        }
        val now = System.currentTimeMillis()
        if (time > now || time <= 0)
            return null
        val diff = now - time
        return when {
            diff < MINUTE_MILLIS -> {
                "just now"
            }
            diff < 2 * MINUTE_MILLIS -> {
                "a minute ago"
            }
            diff < 50 * MINUTE_MILLIS -> {
                "${diff / MINUTE_MILLIS} minutes ago"
            }
            diff < 90 * MINUTE_MILLIS -> {
                "an hour ago"
            }
            diff < 24 * HOUR_MILLIS -> {
                "${diff / HOUR_MILLIS} hours ago"
            }
            diff < 48 * HOUR_MILLIS -> {
                "yesterday"
            }
            else -> {
                "${diff / DAY_MILLIS} days ago"
            }
        }
    }
}

fun timestamp(): Long = System.currentTimeMillis()

/**
 * Convenience method to convert a day/month/year date into a UNIX timestamp.
 * We're suppressing the lint warning because we're not actually using the date formatter
 * to format the date to display, just to specify a format to use to parse it, and so the
 * locale warning doesn't apply.
 */
fun timestamp(day: Int, month: Int, year: Int): Long =
    SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).let { formatter ->
        TimeUnit.MICROSECONDS.toSeconds(formatter.parse("$day.$month.$year")?.time ?: 0)
    }

fun delay(timeout: Long, callBack: CallBack): TimerTask {
    return Timer().schedule(timeout) { callBack() }
}

fun Date.diffDays(date: Date): Int {
    return TimeUtils.diffDays(this, date)
}

fun Date.addDays(days: Int): Date {
    return TimeUtils.addDays(this, days)
}