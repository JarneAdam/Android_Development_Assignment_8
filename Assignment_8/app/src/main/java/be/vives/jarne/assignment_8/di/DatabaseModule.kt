package be.vives.jarne.assignment_8.di

import android.content.Context
import androidx.room3.Room
import androidx.sqlite.driver.AndroidSQLiteDriver
import be.vives.jarne.assignment_8.data.AppDatabase
import be.vives.jarne.assignment_8.data.ToDoDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder<AppDatabase>(context, "todo.db")
            .setDriver(AndroidSQLiteDriver())
            .build()

    @Provides
    @Singleton
    fun provideToDoDao(database: AppDatabase): ToDoDao = database.toDoDao()
}
