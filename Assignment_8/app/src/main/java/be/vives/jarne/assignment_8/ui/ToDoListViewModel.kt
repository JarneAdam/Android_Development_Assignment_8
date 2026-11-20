package be.vives.jarne.assignment_8.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import be.vives.jarne.assignment_8.data.ToDoRepository
import be.vives.jarne.assignment_8.models.ToDo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ToDoListUiState(
    val toDos: List<ToDo> = emptyList(),
)

@HiltViewModel
class ToDoListViewModel @Inject constructor(private val repository: ToDoRepository) : ViewModel() {
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
}
