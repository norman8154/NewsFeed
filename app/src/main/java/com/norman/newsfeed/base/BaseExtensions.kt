package com.norman.newsfeed.base


import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.math.abs

fun <T> T?.or(defaultValue: T): T {
    return this ?: defaultValue
}

fun Long.isToday(): Boolean {
    val cal1 = Calendar.getInstance()
    cal1.time = Date(this)

    val cal2 = Calendar.getInstance()
    cal2.time = Date(System.currentTimeMillis())

    return cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA) &&
            cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
            cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
}

private val articleDateFormatter = DateTimeFormatter.ofPattern("yyyy-M-d", Locale.US)

fun Long.toArticleTime(): String {
    val elapsedMinutes = ((System.currentTimeMillis() - this) / 60_000L).coerceAtLeast(0L)

    return when {
        elapsedMinutes < 60L -> "$elapsedMinutes min"

        elapsedMinutes < 24L * 60L -> "${elapsedMinutes / 60L} hr"

        else -> Instant.ofEpochMilli(this)
            .atZone(ZoneId.systemDefault())
            .format(articleDateFormatter)
    }
}

inline fun <T> T?.ifNull(block: () -> Unit): T? {
    if (this == null) block()
    return this@ifNull
}

inline fun <T> T?.ifNonNull(block: (T) -> Unit): T? {
    this?.let(block)
    return this@ifNonNull
}

fun Int.toNumberFormat(): String {
    return NumberFormat.getNumberInstance(Locale.US).format(this)
}

fun Float.toUSNumberFormat(fullDisplayValue: Float = 1000f): String {
    val symbols = DecimalFormatSymbols.getInstance(Locale.US)
    val oneDecimal = DecimalFormat().apply {
        maximumFractionDigits = 1
        minimumFractionDigits = 0
        isGroupingUsed = false
        decimalFormatSymbols = symbols
    }
    val fullFmt = DecimalFormat("#,##0.#", symbols)

    val sign = if (this < 0f) "-" else ""
    val v = abs(this)

    fun floor1(x: Double): String =
        oneDecimal.format(BigDecimal(x).setScale(1, RoundingMode.FLOOR))

    return when {
        v < fullDisplayValue -> sign + fullFmt.format(v.toDouble())
        v < 1_000_000f -> sign + floor1(v / 1_000.0) + "K"
        v < 100_000_000f -> sign + floor1(v / 1_000_000.0) + "M"
        else -> sign + String.format(Locale.US, "%s00M+", (v / 100_000_000f).toInt())
    }
}

fun Int.toUSNumberFormat(fullDisplayValue: Int = 1000): String =
    this.toFloat().toUSNumberFormat(fullDisplayValue.toFloat())

fun Int.toUSNumberFormatWithPlus(): String {
    val df = DecimalFormat().apply {
        maximumFractionDigits = 1
        minimumFractionDigits = 0
        isGroupingUsed = false
    }

    return if (this < 1000) {
        NumberFormat.getNumberInstance(Locale.US).format(this)
    } else if (this < 1000000) {
        val bd: BigDecimal = BigDecimal(this / (1000 * 1.0)).setScale(0, RoundingMode.FLOOR)
        df.format(bd) + "K" + if (this % 1000 == 0) "" else "+"
    } else {
        val bd: BigDecimal = BigDecimal(this / (1000000 * 1.0)).setScale(0, RoundingMode.FLOOR)
        df.format(bd) + "M" + if (this % 1000000 == 0) "" else "+"
    }
}

fun Float.toMaxNumberFormat(max: Float = 1.0E9F): String {
    val numberFormat = NumberFormat.getNumberInstance(Locale.US)
    if (this % 1 == 0f) {
        numberFormat.minimumFractionDigits = 0
        numberFormat.maximumFractionDigits = 0
    } else {
        numberFormat.minimumFractionDigits = 1
        numberFormat.maximumFractionDigits = 1
    }

    return if (this > max) {
        numberFormat.format(max)
    } else {
        numberFormat.format(this)
    }
}

fun Int.toMaxNumberFormat(max: Int = 999999999): String {
    return if (this > max) {
        NumberFormat.getNumberInstance(Locale.US).format(max)
    } else {
        NumberFormat.getNumberInstance(Locale.US).format(this)
    }
}

public inline fun <T, R> Iterable<T>?.mapOrEmpty(transform: (T) -> R): List<R> {
    return this?.map(transform) ?: listOf()
}

public inline fun <T, R> Iterable<T>?.mapOrDefault(
    defaultList: List<R>,
    transform: (T) -> R,
): List<R> {
    return this?.map(transform) ?: defaultList
}

fun String.toEpochMilli(): Long =
    runCatching { Instant.parse(this).toEpochMilli() }.getOrNull().or(0L)