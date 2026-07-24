package ni.gob.minsa.myapplication.composables.home

import androidx.lifecycle.ViewModel
import ni.gob.minsa.myapplication.data.TaskRepository

class HomeViewModel(repository: TaskRepository) : ViewModel() {
    val tasks = repository.tasks
}