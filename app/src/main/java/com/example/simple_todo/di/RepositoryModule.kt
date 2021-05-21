package com.example.simple_todo.di

import com.example.simple_todo.database.TodoDao
import com.example.simple_todo.repository.TodoRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped


@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {

    @Provides
    @ViewModelScoped
    fun provideTodoRepository(
        todoDao: TodoDao
    ): TodoRepository = TodoRepository(todoDao)

}