package be.vives.jarne.assignment_8.data

import be.vives.jarne.assignment_8.models.MockupToDo
import be.vives.jarne.assignment_8.models.ToDo
import be.vives.jarne.assignment_8.models.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject

interface ToDoRepository {
    val toDos: Flow<List<ToDo>>
    val activeUsers: Flow<List<User>>

    fun observeToDo(id: Int): Flow<ToDo?>
    suspend fun seedIfNeeded()
    suspend fun save(toDo: ToDo)
    suspend fun newToDo(title: String, description: String, assignedToUserId: Int?, flags: ToDoFlags): ToDo
}

class ToDoRepositoryImpl @Inject constructor(private val dao: ToDoDao) : ToDoRepository {
    private val seedMutex = Mutex()

    override val toDos: Flow<List<ToDo>> = dao.observeToDos()
    override val activeUsers: Flow<List<User>> = dao.observeActiveUsers()

    override fun observeToDo(id: Int): Flow<ToDo?> = dao.observeToDo(id)

    override suspend fun seedIfNeeded() = seedMutex.withLock {
        if (dao.countUsers() == 0) {
            dao.insertUsers(MockupToDo.getUsers())
        }
        if (dao.countToDos() == 0) {
            MockupToDo.getToDos().forEach { dao.insertToDo(it) }
        }
    }

    override suspend fun save(toDo: ToDo) {
        if (toDo.number == 0) {
            dao.insertToDo(toDo.copy(number = dao.nextToDoNumber()))
        } else {
            dao.updateToDo(toDo)
        }
    }

    override suspend fun newToDo(title: String, description: String, assignedToUserId: Int?, flags: ToDoFlags): ToDo {
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
