package com.notes.db.repository.impl

import com.notes.db.dao.DeleteNoteDao
import com.notes.db.dao.NoteDao
import com.notes.db.entity.toDeleteEntity
import com.notes.db.repository.NoteRepository
import com.notes.model.NoteModel
import com.notes.model.toEntity
import com.notes.model.toEntityWithoutId
import com.notes.model.toModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 *
 *
 * @author Fedotov Yakov
 */
class NoteRepositoryImpl(
    private val noteDao: NoteDao,
    private val deleteNoteDao: DeleteNoteDao
) : NoteRepository {
    override fun notes(): Flow<List<NoteModel>> = flow {
        deleteEmpty()
        emit(noteDao.notes().map { entity -> entity.toModel })
    }

    override fun lastNote(): Flow<NoteModel> = flow {
        emit(noteDao.lastNote().toModel)
    }

    override fun note(id: Int): Flow<NoteModel> = flow {
        emit(noteDao.note(id).toModel)
    }

    override suspend fun add(note: NoteModel) {
        noteDao.insert(note.toEntityWithoutId)
    }

    override suspend fun add(notes: List<NoteModel>) {
        noteDao.insert(notes.map { model -> model.toEntityWithoutId })
    }

    override suspend fun update(note: NoteModel) {
        noteDao.insert(note.toEntity)
    }

    override suspend fun delete(note: List<NoteModel>) {
        val listEntity = note.map { model -> model.toEntity }
        deleteNoteDao.insert(listEntity.map { it.toDeleteEntity })
        noteDao.delete(listEntity)
    }

    override suspend fun delete(note: NoteModel) {
        val noteEntity = note.toEntity
        deleteNoteDao.insert(noteEntity.toDeleteEntity)
        noteDao.delete(noteEntity)
    }

    override suspend fun deleteEmpty() {
        noteDao.delete(
            noteDao.notes().filter { it.name.isEmpty() }
        )
    }

    override suspend fun clear() {
        noteDao.delete()
    }

}