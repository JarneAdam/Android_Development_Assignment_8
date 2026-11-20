package be.vives.jarne.assignment_8.di

import be.vives.jarne.assignment_8.data.BoredRepository
import be.vives.jarne.assignment_8.data.NetworkBoredRepository
import be.vives.jarne.assignment_8.data.ToDoRepository
import be.vives.jarne.assignment_8.data.ToDoRepositoryImpl
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
    abstract fun bindToDoRepository(implementation: ToDoRepositoryImpl): ToDoRepository

    @Binds
    @Singleton
    abstract fun bindBoredRepository(implementation: NetworkBoredRepository): BoredRepository
}
