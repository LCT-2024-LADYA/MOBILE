package ru.gozerov.domain.utils

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

fun getCurrentUtcTime(): String {
    val currentTime = Instant.now()
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
        .withZone(ZoneOffset.UTC)
    return formatter.format(currentTime)
}

fun resetTimeToMidnight(dateString: String): String {
    val formatter = DateTimeFormatter.ISO_INSTANT
    val instant = Instant.from(formatter.parse(dateString))
    val zonedDateTime = instant.atZone(ZoneOffset.UTC).toLocalDate().atStartOfDay(ZoneOffset.UTC)
    return formatter.format(zonedDateTime.toInstant())
}

fun compareDates(date1: String, date2: String): Int {
    val formatter = DateTimeFormatter.ISO_INSTANT
    val instant1 = Instant.from(formatter.parse(date1))
    val instant2 = Instant.from(formatter.parse(date2))

    return instant1.compareTo(instant2)
}

fun addTimeToDate(date: String, timeStart: String): String {
    val dateFormatter = DateTimeFormatter.ISO_INSTANT
    val zonedDateTime = ZonedDateTime.parse(date, dateFormatter.withZone(ZoneOffset.UTC))
    val time = LocalTime.parse(timeStart.substring(11, 19))
    val resultDateTime =
        zonedDateTime.withHour(time.hour).withMinute(time.minute).withSecond(time.second)

    return dateFormatter.format(resultDateTime.toInstant())
}

fun parseDateToHoursAndMinutes(date: String, time: String): String {
    val timeString = addTimeToDate(date, time)
    val formatter = DateTimeFormatter.ISO_DATE_TIME
    val zonedDateTimeUtc =
        ZonedDateTime.parse(timeString, formatter.withZone(ZoneOffset.UTC))
    val zonedDateTimeLocal = zonedDateTimeUtc.withZoneSameInstant(ZoneId.systemDefault())
    val hours = zonedDateTimeLocal.hour
    val minutes = zonedDateTimeLocal.minute
    return String.format("%02d:%02d", hours, minutes)
}

fun parseDateToHoursAndMinutes(time: String): String {
    val formatter = DateTimeFormatter.ISO_DATE_TIME
    val zonedDateTimeUtc =
        ZonedDateTime.parse(time, formatter.withZone(ZoneOffset.UTC))
    val zonedDateTimeLocal = zonedDateTimeUtc.withZoneSameInstant(ZoneId.systemDefault())
    val hours = zonedDateTimeLocal.hour
    val minutes = zonedDateTimeLocal.minute
    return String.format("%02d:%02d", hours, minutes)
}

fun parseDateToDDMMYYYY(timeString: String): String {
    val formatter = DateTimeFormatter.ISO_DATE_TIME
    val zonedDateTimeUtc =
        ZonedDateTime.parse(timeString, formatter.withZone(ZoneOffset.UTC))
    val zonedDateTimeLocal = zonedDateTimeUtc.withZoneSameInstant(ZoneId.systemDefault())
    val localDate = zonedDateTimeLocal.toLocalDate()

    val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
    return localDate.format(dateFormatter)
}

fun convertToUTC(timeString: String, timeZone: ZoneId = ZoneId.systemDefault()): String {
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
    val localTime = LocalTime.parse(timeString, timeFormatter)
    val today = ZonedDateTime.now(timeZone).toLocalDate()
    val localDateTime = today.atTime(localTime)
    val localZonedDateTime = localDateTime.atZone(timeZone)
    val utcZonedDateTime = localZonedDateTime.withZoneSameInstant(ZoneId.of("UTC"))
    val isoFormatter = DateTimeFormatter.ISO_INSTANT
    val utcDateTimeString = isoFormatter.format(utcZonedDateTime.toInstant())

    return utcDateTimeString
}

fun convertDateToUTC(
    dateString: String,
    start: String,
    timeZone: ZoneId = ZoneId.systemDefault()
): String {
    val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
    val localDate = LocalDate.parse(dateString, dateFormatter)
    val localTime = LocalTime.parse(start, timeFormatter)
    val localDateTime = LocalDateTime.of(localDate, localTime)
    val localZonedDateTime = localDateTime.atZone(timeZone)
    val utcZonedDateTime = localZonedDateTime.withZoneSameInstant(ZoneId.of("UTC"))
    val isoFormatter = DateTimeFormatter.ISO_INSTANT
    val utcDateTimeString = isoFormatter.format(utcZonedDateTime.toInstant())

    return utcDateTimeString
}

fun convertLocalDateDateToUTC(
    date: LocalDate
): String {
    val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val localDate = LocalDate.parse(date.toString(), dateFormatter)
    val localDateTime = localDate.atTime(0, 0)
    val isoFormatter = DateTimeFormatter.ISO_INSTANT
    val utcDateTimeString = isoFormatter.format(localDateTime.toInstant(ZoneOffset.UTC))

    return utcDateTimeString
}


fun convertLocalDateDateToUTCMidnight(
    date: LocalDate
): String {
    val formatter = DateTimeFormatter.ISO_INSTANT
    return formatter.format(date.atStartOfDay(ZoneOffset.UTC))
}

fun convertDateToDDMMYYYY(localDate: LocalDate): String {
    val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
    return localDate.format(formatter)
}

fun convertDateToDDMMYYYYNullable(localDate: LocalDate?): String? {
    val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
    return localDate?.format(formatter)
}

fun getAgeString(age: Int): String {
    val lastDigit = age % 10
    val lastTwoDigits = age % 100

    val ageSuffix = when {
        lastTwoDigits in 11..19 -> "лет"
        lastDigit == 1 -> "год"
        lastDigit in 2..4 -> "года"
        else -> "лет"
    }

    return "$age $ageSuffix"
}