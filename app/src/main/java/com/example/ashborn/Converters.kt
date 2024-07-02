package com.example.ashborn

import androidx.room.TypeConverter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Converters {
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
    @TypeConverter
    fun fromLocalDateTime(localDateTime: LocalDateTime?): String? {
        return localDateTime?.format(formatter)
    }
    @TypeConverter
    fun toLocalDateTime(epochMilliseconds: String?): LocalDateTime? {
        return epochMilliseconds?.let { LocalDateTime.parse(it, formatter) }
    }
}