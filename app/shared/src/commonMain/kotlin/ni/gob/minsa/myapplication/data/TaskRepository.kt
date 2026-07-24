package ni.gob.minsa.myapplication.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import ni.gob.minsa.myapplication.model.Task
import ni.gob.minsa.myapplication.model.TaskStatus
import kotlin.time.Clock


object TaskRepository {
    private val _tasks = MutableStateFlow(listOf<Task>())
    val tasks: StateFlow<List<Task>> = _tasks.asStateFlow()

    fun getTaskbyIdAsFlow(id: Long) {
        tasks.map { it.firstOrNull { task -> task.id == id } }
    }

    fun getTaskByIdAsOneShot(id: Long): Task? {
        return tasks.value.firstOrNull { it.id == id }
    }

    fun addTask(title: String, description: String, status: TaskStatus) {
        val now = Clock.System.now().toEpochMilliseconds()
        val newTask = Task(
            id = now,
            title = title,
            description = description,
            status = status,
            created = now,
            lastUpdated = now
        )
        _tasks.update { it + newTask }
    }

    fun updateTask(id: Long, title: String, description: String, status: TaskStatus) {
        val now = Clock.System.now().toEpochMilliseconds()
        _tasks.update { tasks ->
            tasks.map { task ->
                if (task.id == id) {
                    task.copy(
                        title = title,
                        description = description,
                        status = status,
                        lastUpdated = now
                    )
                } else {
                    task
                }
            }
        }
    }

    fun deleteTask(task: Task) {
        _tasks.update { it.filterNot { it.id == task.id } }
    }

    fun clearAllTasks() {
        _tasks.value = emptyList()
    }


    fun changeStatus(id: Long, taskStatus: TaskStatus) {
        _tasks.update {
            it.map { task ->
                if (task.id == id) {
                    task.copy(status = taskStatus)
                } else {
                    task
                }
            }
        }
    }
}