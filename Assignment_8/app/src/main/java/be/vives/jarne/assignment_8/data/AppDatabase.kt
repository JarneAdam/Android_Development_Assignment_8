package be.vives.jarne.assignment_8.data

import androidx.room3.Database
import androidx.room3.RoomDatabase
import be.vives.jarne.assignment_8.models.ToDo
import be.vives.jarne.assignment_8.models.User

@Database(entities = [User::class, ToDo::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun toDoDao(): ToDoDao
}
