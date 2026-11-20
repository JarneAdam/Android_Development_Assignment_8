package be.vives.jarne.assignment_8.ui

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import kotlinx.serialization.Serializable

@Serializable
private data object ToDoListRoute : NavKey

@Serializable
private data class ToDoDetailRoute(val id: Int) : NavKey

@Serializable
private data class AddEditToDoRoute(val id: Int? = null) : NavKey

@Composable
fun ToDoApp(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val backStack = rememberNavBackStack(ToDoListRoute)

    NavDisplay(
        backStack = backStack,
        onBack = {
            if (backStack.size > 1) backStack.removeLastOrNull()
        },
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
        ),
        entryProvider = entryProvider {
            entry<ToDoListRoute> {
                ToDoListScreen(
                    modifier = modifier,
                    viewModel = hiltViewModel(),
                    onToDoClick = { toDo ->
                        backStack.add(ToDoDetailRoute(toDo.number))
                    },
                    onCreateClick = {
                        backStack.add(AddEditToDoRoute())
                    },
                    onNavigateBack = { (context as? Activity)?.finish() },
                )
            }

            entry<ToDoDetailRoute> { route ->
                val detailViewModel = hiltViewModel<ToDetailViewModel, ToDetailViewModel.Factory> { factory ->
                    factory.create(route.id)
                }
                ToDetailScreen(
                    modifier = modifier,
                    viewModel = detailViewModel,
                    onNavigateBack = { backStack.removeLastOrNull() },
                    onNavigateToEdit = {
                        backStack.add(AddEditToDoRoute(route.id))
                    },
                )
            }

            entry<AddEditToDoRoute> { route ->
                val addEditViewModel = hiltViewModel<AddEditToDoViewModel, AddEditToDoViewModel.Factory> { factory ->
                    factory.create(route.id)
                }
                AddEditToDoScreen(
                    modifier = modifier,
                    viewModel = addEditViewModel,
                    onNavigateBack = { backStack.removeLastOrNull() },
                    onCancel = { backStack.removeLastOrNull() },
                    onSave = {
                        backStack.removeLastOrNull()
                    },
                )
            }
        },
    )
}
