package ni.gob.minsa.myapplication.composables

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.util.fastForEachIndexed
import ni.gob.minsa.myapplication.model.TaskStatus

@Composable
fun TaskStatusSelector(
    selectedStatus: TaskStatus?,
    onStatusSelected: (TaskStatus) -> Unit,
    modifier: Modifier = Modifier
) {
    val taskStatuses = remember { TaskStatus.entries.toList() }

    SingleChoiceSegmentedButtonRow(
        modifier = modifier
    ) {
        taskStatuses.fastForEachIndexed { i, status ->
            SegmentedButton(
                selected = status == selectedStatus,
                onClick = { onStatusSelected(status) },
                shape = SegmentedButtonDefaults.itemShape(index = i, count = taskStatuses.size),
                icon = { SegmentedButtonDefaults.Icon(status == selectedStatus) },
                label = {
                    val label = status.name.lowercase().replace("_", " ")
                        .replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
                    Text(label)
                }
            )
        }
    }
}

@Preview
@Composable
fun TaskStatusSelectorPreview() {
    MaterialTheme {
        TaskStatusSelector(
            selectedStatus = TaskStatus.InProgress,
            onStatusSelected = {}
        )
    }
}

