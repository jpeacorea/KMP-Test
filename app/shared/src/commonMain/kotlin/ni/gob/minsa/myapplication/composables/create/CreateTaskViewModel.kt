package ni.gob.minsa.myapplication.composables.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import ni.gob.minsa.myapplication.data.TaskRepository
import ni.gob.minsa.myapplication.model.TaskStatus

/**
 * ViewModel responsible for managing the state and business logic of the task creation screen.
 *
 * This class handles user input for a new task's title, description, and status,
 * provides validation to determine if a task can be saved, and coordinates with the
 * [TaskRepository] to persist the task data.
 *
 * @property repository The data source used to save the newly created task.
 */
class CreateTaskViewModel(
    private val repository: TaskRepository = TaskRepository
) : ViewModel() {
    /**
     * The current title, description, status, and saved state of the task being created.
     */
    private val _title = MutableStateFlow("")
    val title = _title.asStateFlow()

    private val _description = MutableStateFlow("")
    val description = _description.asStateFlow()

    private val _selectedStatus = MutableStateFlow(TaskStatus.ToDo)
    val selectedStatus = _selectedStatus.asStateFlow()

    private val _saved = MutableStateFlow(false)
    val saved = _saved.asStateFlow()

    /**
     * A StateFlow that emits true if the task can be saved (title and description are not blank).
     */
    val enableSave = combine(_title, _description) { newTitle, newDescription ->
        newTitle.isNotBlank() && newDescription.isNotBlank()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), false)

    /**
     * Updates the task title and trims any leading or trailing whitespace.
     *
     * @param newTitle The new title text to be set.
     */
    fun onTitleChange(newTitle: String) {
        _title.value = newTitle.trim()
    }

    /**
     * Updates the task description.
     *
     * @param newDescription The new description text to be set.
     */
    fun onDescriptionChange(newDescription: String) {
        _description.value = newDescription.trim()
    }

    /**
     * Updates the selected status for the new task.
     *
     * @param newStatus The new [TaskStatus] to be assigned to the task.
     */
    fun onStatusChange(newStatus: TaskStatus) {
        _selectedStatus.value = newStatus
    }

    /**
     * Saves the new task to the repository using the current title, description, and status.
     * Sets the saved state to true upon successful completion.
     */
    fun submit() {
        repository.addTask(
            title = _title.value.trim(),
            description = _description.value.trim(),
            status = _selectedStatus.value
        )
        _saved.value = true
    }
}