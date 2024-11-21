package com.hadesmori.wealthy.util

import androidx.room.TypeConverter
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeParseException

class DateConverters {
    @TypeConverter
    fun fromLocalDateTime(dateTime: LocalDateTime): String {
        return dateTime.toString()
    }

    @TypeConverter
    fun toLocalDateTime(dateTimeString: String): LocalDateTime {
        return try {
            LocalDateTime.parse(dateTimeString)
        } catch (e: DateTimeParseException) {
            LocalDate.parse(dateTimeString).atStartOfDay()
        }
    }
}