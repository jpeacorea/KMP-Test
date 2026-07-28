package ni.gob.minsa.myapplication.composables.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import ni.gob.minsa.myapplication.composables.TaskStatusSelector
import ni.gob.minsa.myapplication.data.TaskRepository
import ni.gob.minsa.myapplication.model.Task

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDetailsScreen(
    taskId: Long,
    onNavigateUp: () -> Unit,
    onEdit: (Task) -> Unit,
    onDeleted: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TaskDetailsViewModel = viewModel { TaskDetailsViewModel(TaskRepository) }
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    val task by viewModel.task.collectAsStateWithLifecycle()
    val lastUpdated by viewModel.lastUpdated.collectAsStateWithLifecycle()
    val deleted by viewModel.deleted.collectAsStateWithLifecycle()

    val topAppBarScrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text(text = "Confirm Delete") },
            text = { Text(text = "Delete task ${task!!.title}?") },
            confirmButton = {
                TextButton(onClick = { viewModel.delete(task!!) }) {
                    Text(text = "Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text(text = "Cancel")
                }
            }
        )
    }

    LaunchedEffect(Unit) {
        viewModel.getTask(taskId)
    }

    LaunchedEffect(deleted) {
        if (deleted) onDeleted()
    }

    Scaffold(
        modifier = modifier.nestedScroll(topAppBarScrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                scrollBehavior = topAppBarScrollBehavior,
                title = { task?.title?.let { Text(text = it) } },
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "back")
                    }
                },
                actions = {
                    task?.let {
                        IconButton(onClick = { onEdit(it) }) {
                            Icon(Icons.Default.Create, contentDescription = "edit ${it.title}")
                        }
                        IconButton(onClick = { showDeleteDialog = true }) {
                            Icon(Icons.Default.Delete, contentDescription = "delete ${it.title}")
                        }
                    }
                }
            )
        },
        bottomBar = {
            Surface(modifier = Modifier.navigationBarsPadding()) {
                Box(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Last modified: $lastUpdated",
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .consumeWindowInsets(it)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            task?.let { taskDetails ->
                TaskStatusSelector(
                    modifier = Modifier.fillMaxWidth(),
                    selectedStatus = taskDetails.status,
                    onStatusSelected = viewModel::changeStatus
                )
                Text(text = taskDetails.description)
            }
        }
    }
}