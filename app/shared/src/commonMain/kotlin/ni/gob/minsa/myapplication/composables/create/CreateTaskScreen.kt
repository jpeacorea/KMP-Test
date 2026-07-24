package ni.gob.minsa.myapplication.composables.create

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import ni.gob.minsa.myapplication.data.TaskRepository

@Composable
fun CreateTaskScreen(
    onNavigateUpdater: () -> Unit,
    onSaved: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CreateTaskViewModel = viewModel { CreateTaskViewModel(TaskRepository) }
): Unit {
    val title: String by viewModel.title.collectAsStateWithLifecycle()
    val description by viewModel.description.collectAsStateWithLifecycle()

}