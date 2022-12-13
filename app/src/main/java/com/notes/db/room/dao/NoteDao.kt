package com.notes.db.room.dao

import androidx.room.*
import com.notes.db.room.entity.NoteEntity

/**
 * Dao заметок
 *
 * @author Fedotov Yakov
 */
@Dao
interface NoteDao {
    @Query("SELECT * FROM note")
    suspend fun notes(): List<NoteEntity>

    @Query("SELECT * FROM note ORDER BY id DESC LIMIT 1")
    suspend fun lastNote(): NoteEntity

    @Query("SELECT * FROM note WHERE :id = id LIMIT 1")
    suspend fun note(id: Int): NoteEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: NoteEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(list: List<NoteEntity>)

    @Update
    suspend fun update(note: List<NoteEntity>)

    @Update
    suspend fun update(note: NoteEntity)

    @Delete
    suspend fun delete(list: List<NoteEntity>)

    @Delete
    suspend fun delete(list: NoteEntity)

    @Query("DELETE FROM note")
    suspend fun delete()

}