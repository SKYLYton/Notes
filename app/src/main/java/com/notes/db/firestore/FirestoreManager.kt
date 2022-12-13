package com.notes.db.firestore

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.notes.auth.AuthManager
import com.notes.db.room.repository.NoteRepository
import com.notes.model.NoteModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first

/**
 * @author Fedotov Yakov
 */
class FirestoreManager(
    private val authManager: AuthManager,
    private val db: FirebaseFirestore,
    private val noteRepository: NoteRepository
) {
    private val coroutineContext =
        SupervisorJob() + Dispatchers.IO

    private val job = Job()
    private val scope = CoroutineScope(coroutineContext + job)

    private val collectionPath: String
        get() = "note_${authManager.currentUser?.uid}"

    fun updateNotes(notes: List<NoteModel>) {
        val notesNotSent = notes.filter { !it.isSent }
        val notesForUpdate = notes.filter { it.isSent }
        var isAllUpdate = notesForUpdate.isEmpty() || notesNotSent.isEmpty()
        val collection = db.collection(collectionPath)
        notesNotSent.forEach { note ->
            val newNote = hashMapOf(
                "name" to note.name,
                "text" to note.text,
                "date" to note.date,
                "delete" to note.isDeleted,
            )
            collection.add(newNote)
                .addOnSuccessListener { documentReference ->
                    scope.launch {
                        noteRepository.update(
                            note.clone(
                                isSent = true,
                                firestoreId = documentReference.id
                            )
                        )

                        if (isAllUpdate) {
                            val notes = noteRepository.notes().first()
                            deleteNotExist(notes)
                        }
                        isAllUpdate = true
                    }
                }
                .addOnFailureListener {
                    /* no-op */
                }
        }

        notesForUpdate.forEach { note ->
            val newNote = hashMapOf(
                "name" to note.name,
                "text" to note.text,
                "date" to note.date,
                "delete" to note.isDeleted,
            )
            collection.document(note.firestoreId)
                .update(newNote as Map<String, Any>)
                .addOnSuccessListener {
                    scope.launch {
                        if (isAllUpdate) {
                            val notes = noteRepository.notes().first()
                            deleteNotExist(notes)
                        }
                        isAllUpdate = true
                    }
                }
                .addOnFailureListener {
                    /* no-op */
                }
        }
    }

    private fun deleteNote(noteId: String) {
        db.collection(collectionPath)
            .document(noteId)
            .delete()
            .addOnSuccessListener { documentReference ->
                /* no-op */
            }
            .addOnFailureListener { e ->
                /* no-op */
            }
    }

    private fun deleteNotExist(notes: List<NoteModel>) {
        db.collection(collectionPath)
            .get()
            .addOnSuccessListener { documentReference ->
                val documents = documentReference.documents
                documents.forEach { doc ->
                    val notContains = notes.find { it.firestoreId == doc.id } == null
                    if (notContains) {
                        deleteNote(doc.id)
                    }
                }
            }
            .addOnFailureListener {
                /* no-op */
            }
    }

    fun getNotes() {
        db.collection(collectionPath)
            .get()
            .addOnSuccessListener { documentReference ->
                val documents = documentReference.documents
                val notes = documents.mapNotNull {
                    it.toObject(NoteModel::class.java)?.clone(
                        isSent = true,
                        firestoreId = it.id
                    )
                }
                scope.launch {
                    noteRepository.add(notes)
                }
            }
            .addOnFailureListener {
                /* no-op */
            }
    }

    private fun processErrorResult(
        onError: (msg: String) -> Unit,
        throwable: Throwable? = null
    ) {
        val msg = FirebaseFirestoreError.fetchMsgByCode(
            (throwable as? FirebaseFirestoreException)?.code?.value()
        )
        onError(msg)
    }

}