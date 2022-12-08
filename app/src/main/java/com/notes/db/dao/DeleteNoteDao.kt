package com.notes.db.dao

import androidx.room.*
import com.notes.db.entity.DeleteNoteEntity

/**
 * Dao заметок
 *
 * @author Fedotov Yakov
 */
@Dao
interface DeleteNoteDao {
    @Query("SELECT * FROM note")
    suspend fun notes(): List<DeleteNoteEntity>

    @Query("SELECT * FROM note ORDER BY id DESC LIMIT 1")
    suspend fun lastNote(): DeleteNoteEntity

    @Query("SELECT * FROM note WHERE :id = id LIMIT 1")
    suspend fun note(id: Int): DeleteNoteEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: DeleteNoteEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(list: List<DeleteNoteEntity>)

    @Update
    suspend fun update(note: DeleteNoteEntity)

    @Delete
    suspend fun delete(list: List<DeleteNoteEntity>)

    @Delete
    suspend fun delete(list: DeleteNoteEntity)

    @Query("DELETE FROM note")
    suspend fun delete()

}