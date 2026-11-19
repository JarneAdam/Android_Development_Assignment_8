package be.vives.jarne.assignment_8.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import be.vives.jarne.assignment_8.data.ToDoRepository
import be.vives.jarne.assignment_8.models.ToDo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ToDoListUiState(
    val toDos: List<ToDo> = emptyList(),
)

class ToDoListViewModel(private val repository: ToDoRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(ToDoListUiState())
    val uiState: StateFlow<ToDoListUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.seedIfNeeded()
            repository.toDos.collect { toDos ->
                _uiState.value = ToDoListUiState(toDos)
            }
        }
    }

    class Factory(private val repository: ToDoRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (!modelClass.isAssignableFrom(ToDoListViewModel::class.java)) {
                throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
            @Suppress("UNCHECKED_CAST")
            return ToDoListViewModel(repository) as T
        }
    }
}
