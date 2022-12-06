package com.notes.di

import android.content.Context
import androidx.room.Room
import com.notes.BuildConfig
import com.notes.db.AppDatabase
import com.notes.db.repository.NoteRepository
import com.notes.db.repository.impl.NoteRepositoryImpl
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
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun provideNoteRepository(appDatabase: AppDatabase): NoteRepository =
        NoteRepositoryImpl(
            appDatabase.noteDao()
        )

}