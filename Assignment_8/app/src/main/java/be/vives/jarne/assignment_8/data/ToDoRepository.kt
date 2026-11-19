package be.vives.jarne.assignment_8.data

import be.vives.jarne.assignment_8.models.ToDo
import be.vives.jarne.assignment_8.models.MockupToDo
import be.vives.jarne.assignment_8.models.User
import kotlinx.coroutines.flow.Flow

class ToDoRepository(private val dao: ToDoDao) {
    val toDos: Flow<List<ToDo>> = dao.observeToDos()
    val activeUsers: Flow<List<User>> = dao.observeActiveUsers()

    fun observeToDo(id: Int): Flow<ToDo?> = dao.observeToDo(id)

    suspend fun seedIfNeeded() {
        if (dao.countUsers() == 0) {
            dao.insertUsers(MockupToDo.getUsers())
        }
        if (dao.countToDos() == 0) {
            MockupToDo.getToDos().forEach { dao.insertToDo(it) }
        }
    }

    suspend fun save(toDo: ToDo) {
        if (toDo.number == 0) {
            dao.insertToDo(toDo.copy(number = dao.nextToDoNumber()))
        } else {
            dao.updateToDo(toDo)
        }
    }

    suspend fun newToDo(title: String, description: String, assignedToUserId: Int?, flags: ToDoFlags): ToDo {
        return ToDo(
            number = 0,
            title = title,
            description = description,
            createdByUserId = dao.getUsers().firstOrNull()?.id
                ?: error("Cannot create a ToDo without users"),
            createOnDate = System.currentTimeMillis(),
            assignedToUserId = assignedToUserId,
            finishedOnDate = if (flags.acceptanceDone) System.currentTimeMillis() else null,
            timeEstimated = 0,
            analysisDone = flags.analysisDone,
            developmentDone = flags.developmentDone,
            reviewAndTestingDone = flags.reviewAndTestingDone,
            acceptanceDone = flags.acceptanceDone,
        )
    }
}

data class ToDoFlags(
    val analysisDone: Boolean,
    val developmentDone: Boolean,
    val reviewAndTestingDone: Boolean,
    val acceptanceDone: Boolean,
)
