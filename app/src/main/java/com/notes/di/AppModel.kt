package com.notes.di

import android.content.Context
import androidx.room.Room
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.notes.BuildConfig
import com.notes.auth.AuthManager
import com.notes.db.firestore.FirestoreManager
import com.notes.db.room.AppDatabase
import com.notes.db.room.repository.NoteRepository
import com.notes.db.room.repository.impl.NoteRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * @author Fedotov Yakov
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModel {

    @Provides
    @Singleton
    fun provideContext(@ApplicationContext context: Context) = context

    @Provides
    @Singleton
    fun provideDatabase(context: Context) =
        Room.databaseBuilder(context, AppDatabase::class.java, BuildConfig.DB_FILE_NAME)
            //.addMigrations(*migrationsTen)
            .fallbackToDestructiveMigration().build()

    @Provides
    @Singleton
    fun provideNoteRepository(appDatabase: AppDatabase): NoteRepository =
        NoteRepositoryImpl(appDatabase.noteDao())

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = Firebase.auth

    @Provides
    @Singleton
    fun provideAuthManager(aut: FirebaseAuth): AuthManager =
        AuthManager(aut)

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore =
        Firebase.firestore

    @Provides
    @Singleton
    fun provideFirestoreManager(
        aut: AuthManager,
        db: FirebaseFirestore,
        noteRepository: NoteRepository
    ): FirestoreManager =
        FirestoreManager(aut, db, noteRepository)


}