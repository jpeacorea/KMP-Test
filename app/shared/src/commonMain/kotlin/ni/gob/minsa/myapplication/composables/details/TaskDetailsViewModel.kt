package ni.gob.minsa.myapplication.composables.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime
import ni.gob.minsa.myapplication.data.TaskRepository
import ni.gob.minsa.myapplication.model.Task
import ni.gob.minsa.myapplication.model.TaskStatus

class TaskDetailsViewModel(private val repository: TaskRepository) : ViewModel() {
    private val id = MutableStateFlow<Long?>(null)

    private val _deleted = MutableStateFlow(false)
    val deleted = _deleted.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val task = id.filterNotNull()
        .flatMapLatest {
            repository.getTaskByIdAsFlow(it)
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val lastUpdated = task.filterNotNull()
        .mapLatest { t ->
            Instant.fromEpochMilliseconds(t.lastUpdated)
                .toLocalDateTime(TimeZone.currentSystemDefault())
                .format(
                    LocalDateTime.Format {
                        date(
                            LocalDate.Format {
                                monthName(MonthNames.ENGLISH_FULL)
                                char(' ')
                                dayOfMonth()
                                char(' ')
                                year()
                            }
                        )
                        char(' ')
                        time(
                            LocalTime.Format {
                                hour()
                                char(':')
                                minute()
                            }
                        )
                    }
                )
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

    fun getTask(id: Long) {
        this.id.value = id
    }

    fun delete(task: Task) {
        repository.deleteTask(task)
        _deleted.value = true
    }

    fun changeStatus(status: TaskStatus) {
        id.value?.let {
            repository.changeStatus(id = it, taskStatus = status)
        }
    }
}