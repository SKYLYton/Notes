package com.notes.db.room.convert

import androidx.room.TypeConverter

/**
 * Конвертер для Room
 *
 * @author Onanov Aleksey (@onanov)
 */
class Converters {
    @TypeConverter
    fun stringListToString(value: List<String?>): String {
        return value.joinToString("#;#")
    }

    @TypeConverter
    fun stringToStringList(value: String): List<String> {
        return value.split("#;#")
    }

    @TypeConverter
    fun intListToString(value: List<Int>): String {
        return value.joinToString("#;#")
    }

    @TypeConverter
    fun stringToIntList(value: String): List<Int> {
        return value.split("#;#").map { it.toInt() }
    }
}