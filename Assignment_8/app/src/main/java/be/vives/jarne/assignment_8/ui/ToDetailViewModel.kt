package be.vives.jarne.assignment_8.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import be.vives.jarne.assignment_8.data.ToDoRepository
import be.vives.jarne.assignment_8.models.ToDo
import be.vives.jarne.assignment_8.models.User
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ToDetailUiState(
    val toDo: ToDo? = null,
    val users: List<User> = emptyList(),
)

@HiltViewModel(assistedFactory = ToDetailViewModel.Factory::class)
class ToDetailViewModel @AssistedInject constructor(
    @Assisted toDoId: Int,
    repository: ToDoRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(ToDetailUiState())
    val uiState: StateFlow<ToDetailUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.seedIfNeeded()
            launch {
                repository.observeToDo(toDoId).collect { toDo ->
                    _uiState.value = _uiState.value.copy(toDo = toDo)
                }
            }
            repository.activeUsers.collect { users ->
                _uiState.value = _uiState.value.copy(users = users)
            }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(toDoId: Int): ToDetailViewModel
    }
}
