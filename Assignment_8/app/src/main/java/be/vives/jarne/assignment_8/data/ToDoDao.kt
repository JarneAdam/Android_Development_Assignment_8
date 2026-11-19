package be.vives.jarne.assignment_8.data

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.Query
import androidx.room3.Update
import be.vives.jarne.assignment_8.models.ToDo
import be.vives.jarne.assignment_8.models.User
import kotlinx.coroutines.flow.Flow

@Dao
interface ToDoDao {
    @Query("SELECT * FROM todos ORDER BY number")
    fun observeToDos(): Flow<List<ToDo>>

    @Query("SELECT * FROM todos WHERE number = :id")
    fun observeToDo(id: Int): Flow<ToDo?>

    @Query("SELECT * FROM users WHERE isActive = 1 ORDER BY lastName, firstName")
    fun observeActiveUsers(): Flow<List<User>>

    @Query("SELECT * FROM users ORDER BY id")
    suspend fun getUsers(): List<User>

    @Query("SELECT COUNT(*) FROM users")
    suspend fun countUsers(): Int

    @Query("SELECT COUNT(*) FROM todos")
    suspend fun countToDos(): Int

    @Query("SELECT COALESCE(MAX(number), 0) + 1 FROM todos")
    suspend fun nextToDoNumber(): Int

    @Insert
    suspend fun insertUsers(users: List<User>)

    @Insert
    suspend fun insertToDo(toDo: ToDo)

    @Update
    suspend fun updateToDo(toDo: ToDo)

    @Delete
    suspend fun deleteToDo(toDo: ToDo)
}
