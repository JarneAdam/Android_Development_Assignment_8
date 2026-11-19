package be.vives.jarne.assignment_8.data

import android.content.Context
import androidx.room3.Database
import androidx.room3.Room
import androidx.room3.RoomDatabase
import androidx.sqlite.driver.AndroidSQLiteDriver
import be.vives.jarne.assignment_8.models.ToDo
import be.vives.jarne.assignment_8.models.User

@Database(entities = [User::class, ToDo::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun toDoDao(): ToDoDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder<AppDatabase>(
                    context.applicationContext,
                    "todo.db",
                ).setDriver(AndroidSQLiteDriver())
                    .build()
                    .also { instance = it }
            }
    }
}
