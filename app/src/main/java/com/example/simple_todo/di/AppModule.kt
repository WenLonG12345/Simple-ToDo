package com.example.simple_todo.di

import android.content.Context
import androidx.room.Room
import com.example.simple_todo.database.TodoDB
import com.example.simple_todo.utils.Constants.TODO_DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context,
        TodoDB::class.java,
        TODO_DATABASE_NAME
    )
        .fallbackToDestructiveMigration()
        .build()

    @Singleton
    @Provides
    fun provideTodoDao(db: TodoDB) = db.todoDao()
}