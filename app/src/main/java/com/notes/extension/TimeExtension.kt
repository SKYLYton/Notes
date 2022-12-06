package com.notes.extension

import java.text.SimpleDateFormat
import java.util.*

/**
 * @author Fedotov Yakov
 */
fun Date.getTimeToString(): String {
    val sdf = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
    return sdf.format(Date())
}