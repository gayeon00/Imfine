package com.example.imfine.todolist.di

import com.example.imfine.todolist.data.repository.TodoRepositoryImpl
import com.example.imfine.todolist.domain.TodoRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun provideTodoRepository(
        todoRepositoryImpl: TodoRepositoryImpl
    ): TodoRepository
    
}