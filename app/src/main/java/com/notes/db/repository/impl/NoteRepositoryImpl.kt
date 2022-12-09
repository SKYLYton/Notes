package com.notes.db.repository.impl

import com.notes.db.dao.NoteDao
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
    private val noteDao: NoteDao
) : NoteRepository {
    override fun notes(): Flow<List<NoteModel>> = flow {
        deleteEmpty()
        emit(noteDao.notes().filter { !it.isDelete }.map { entity -> entity.toModel })
    }

    override fun deletedNotes(): Flow<List<NoteModel>> = flow {
        deleteEmpty()
        emit(noteDao.notes().filter { it.isDelete }.map { entity -> entity.toModel })
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

    override suspend fun restore(note: NoteModel) {
        noteDao.update(note.toEntity.apply {
            isDelete = false
        })
    }

    override suspend fun update(note: NoteModel) {
        noteDao.insert(note.toEntity)
    }

    override suspend fun delete(note: List<NoteModel>) {
        noteDao.update(note.map { model ->
            model.toEntity.apply {
                isDelete = true
            }
        })
    }

    override suspend fun delete(note: NoteModel) {
        noteDao.update(note.toEntity.apply {
            isDelete = true
        })
    }

    override suspend fun deleteFromDB(note: List<NoteModel>) {
        noteDao.delete(note.map { it.toEntity })
    }

    override suspend fun deleteFromDB(note: NoteModel) {
        noteDao.delete(note.toEntity)
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