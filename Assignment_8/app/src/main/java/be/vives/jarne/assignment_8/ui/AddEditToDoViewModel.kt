package be.vives.jarne.assignment_8.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import be.vives.jarne.assignment_8.data.BoredRepository
import be.vives.jarne.assignment_8.data.ToDoFlags
import be.vives.jarne.assignment_8.data.ToDoRepository
import be.vives.jarne.assignment_8.data.toDescription
import be.vives.jarne.assignment_8.models.ToDo
import be.vives.jarne.assignment_8.models.User
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AddEditToDoUiState(
    val title: String = "",
    val description: String = "",
    val selectedUser: User? = null,
    val users: List<User> = emptyList(),
    val analysisDone: Boolean = false,
    val developmentDone: Boolean = false,
    val reviewAndTestingDone: Boolean = false,
    val acceptanceDone: Boolean = false,
    val initialToDo: ToDo? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
) {
    val isNewToDo: Boolean
        get() = initialToDo == null
}

@HiltViewModel(assistedFactory = AddEditToDoViewModel.Factory::class)
class AddEditToDoViewModel @AssistedInject constructor(
    @Assisted toDoId: Int?,
    private val repository: ToDoRepository,
    private val boredRepository: BoredRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(AddEditToDoUiState())
    val uiState: StateFlow<AddEditToDoUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.seedIfNeeded()
            launch {
                repository.activeUsers.collect { users ->
                    _uiState.update {
                        it.copy(
                            users = users,
                            selectedUser = it.initialToDo?.assignedToUserId?.let { id ->
                                users.firstOrNull { user -> user.id == id }
                            } ?: it.selectedUser,
                        )
                    }
                }
            }
            if (toDoId != null) {
                repository.observeToDo(toDoId).collect { toDo ->
                    if (toDo != null) {
                        _uiState.update {
                            it.copy(
                                title = toDo.title,
                                description = toDo.description,
                                selectedUser = it.users.firstOrNull { user -> user.id == toDo.assignedToUserId },
                                analysisDone = toDo.analysisDone,
                                developmentDone = toDo.developmentDone,
                                reviewAndTestingDone = toDo.reviewAndTestingDone,
                                acceptanceDone = toDo.acceptanceDone,
                                initialToDo = toDo,
                            )
                        }
                    }
                }
            }
        }
    }

    fun onSurpriseMe() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            boredRepository.getRandomActivity()
                .onSuccess { activityDto ->
                    _uiState.update {
                        it.copy(
                            title = activityDto.activity,
                            description = activityDto.toDescription(),
                            isLoading = false,
                            errorMessage = null,
                        )
                    }
                }
                .onFailure { exception ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = exception.localizedMessage ?: "Failed to fetch surprise activity",
                        )
                    }
                }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    fun onTitleChange(newTitle: String) {
        _uiState.update { it.copy(title = newTitle) }
    }

    fun onDescriptionChange(newDescription: String) {
        _uiState.update { it.copy(description = newDescription) }
    }

    fun onUserSelected(user: User?) {
        _uiState.update { it.copy(selectedUser = user) }
    }

    fun onAnalysisDoneChange(done: Boolean) {
        _uiState.update { it.copy(analysisDone = done) }
    }

    fun onDevelopmentDoneChange(done: Boolean) {
        _uiState.update { it.copy(developmentDone = done) }
    }

    fun onReviewAndTestingDoneChange(done: Boolean) {
        _uiState.update { it.copy(reviewAndTestingDone = done) }
    }

    fun onAcceptanceDoneChange(done: Boolean) {
        _uiState.update { it.copy(acceptanceDone = done) }
    }

    fun save() {
        val state = _uiState.value
        viewModelScope.launch {
            val flags = ToDoFlags(
                analysisDone = state.analysisDone,
                developmentDone = state.developmentDone,
                reviewAndTestingDone = state.reviewAndTestingDone,
                acceptanceDone = state.acceptanceDone,
            )
            val toDo = state.initialToDo?.copy(
                title = state.title,
                description = state.description,
                assignedToUserId = state.selectedUser?.id,
                finishedOnDate = if (state.acceptanceDone) {
                    state.initialToDo.finishedOnDate ?: System.currentTimeMillis()
                } else {
                    null
                },
                analysisDone = flags.analysisDone,
                developmentDone = flags.developmentDone,
                reviewAndTestingDone = flags.reviewAndTestingDone,
                acceptanceDone = flags.acceptanceDone,
            ) ?: repository.newToDo(
                title = state.title,
                description = state.description,
                assignedToUserId = state.selectedUser?.id,
                flags = flags,
            )
            repository.save(toDo)
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(toDoId: Int?): AddEditToDoViewModel
    }
}
