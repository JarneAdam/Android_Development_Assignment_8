package be.vives.jarne.assignment_8.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import be.vives.jarne.assignment_8.data.ToDoRepository
import be.vives.jarne.assignment_8.models.ToDo
import be.vives.jarne.assignment_8.models.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ToDetailUiState(
    val toDo: ToDo? = null,
    val users: List<User> = emptyList(),
)

class ToDetailViewModel(toDoId: Int, repository: ToDoRepository) : ViewModel() {
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

    class Factory(
        private val toDoId: Int,
        private val repository: ToDoRepository,
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (!modelClass.isAssignableFrom(ToDetailViewModel::class.java)) {
                throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
            @Suppress("UNCHECKED_CAST")
            return ToDetailViewModel(toDoId, repository) as T
        }
    }
}
