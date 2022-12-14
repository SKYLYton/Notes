package com.notes.db.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.notes.BuildConfig
import com.notes.db.room.convert.Converters
import com.notes.db.room.dao.NoteDao
import com.notes.db.room.entity.NoteEntity

/**
 * База данных приложения
 *
 * @author Fedotov Yakov
 */
@Database(
    version = BuildConfig.DB_VERSION,
    exportSchema = false,
    entities = [
        NoteEntity::class
    ]
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
}