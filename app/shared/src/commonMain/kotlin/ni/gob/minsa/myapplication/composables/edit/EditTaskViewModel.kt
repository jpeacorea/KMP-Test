package ni.gob.minsa.myapplication.composables.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import ni.gob.minsa.myapplication.data.TaskRepository
import ni.gob.minsa.myapplication.model.Task
import ni.gob.minsa.myapplication.model.TaskStatus

class EditTaskViewModel(private val repository: TaskRepository) : ViewModel() {
    private val savedTask = MutableStateFlow<Task?>(null)

    private val _title = MutableStateFlow("")
    val title = _title.asStateFlow()

    private val _description = MutableStateFlow("")
    val description = _description.asStateFlow()

    private val _selectedStatus = MutableStateFlow(TaskStatus.ToDo)
    val selectedStatus = _selectedStatus.asStateFlow()

    private val _saved = MutableStateFlow(false)
    val saved = _saved.asStateFlow()

    val enableSave =
        combine(
            _title,
            _description,
            _selectedStatus,
            savedTask
        ) { newTitle, newDescription, newStatus, savedTask ->
            (newTitle.isNotBlank() && newTitle != savedTask?.title) || (newDescription.isNotBlank()
                    && newDescription != savedTask?.description) || newStatus != savedTask?.status
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), false)

    fun setInitialData(taskId: Long) {
        repository.getTaskByIdAsOneShot(taskId)?.let { task ->
            savedTask.value = task
            _title.value = task.title
            _description.value = task.description
            _selectedStatus.value = task.status
        }
    }

    fun onTitleChange(value: String) {
        _title.value = value.trim()
    }

    fun onDescriptionChange(value: String) {
        _description.value = value.trim()
    }

    fun onSelectedStatusChange(value: TaskStatus) {
        _selectedStatus.value = value
    }

    fun submit() {
        savedTask.value?.let {
            repository.updateTask(
                id = it.id,
                title = _title.value,
                description = _description.value,
                status = _selectedStatus.value
            )
            _saved.value = true
        }
    }
}