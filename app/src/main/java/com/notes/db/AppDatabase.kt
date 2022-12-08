package com.notes.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.notes.BuildConfig
import com.notes.db.convert.Converters
import com.notes.db.dao.DeleteNoteDao
import com.notes.db.dao.NoteDao
import com.notes.db.entity.DeleteNoteEntity
import com.notes.db.entity.NoteEntity

/**
 * База данных приложения
 *
 * @author Fedotov Yakov
 */
@Database(
    version = BuildConfig.DB_VERSION,
    exportSchema = false,
    entities = [
        NoteEntity::class,
        DeleteNoteEntity::class
    ]
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
    abstract fun deleteNoteDao(): DeleteNoteDao
}