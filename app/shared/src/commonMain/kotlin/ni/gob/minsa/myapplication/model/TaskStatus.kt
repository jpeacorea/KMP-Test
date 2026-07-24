package ni.gob.minsa.myapplication.model

import androidx.compose.ui.graphics.Color

enum class TaskStatus(val displayName: String, val color: Color) {
    ToDo(
        displayName = "To Do",
        color = Color.Gray
    ),
    InProgress(
        displayName = "In Progress",
        color = Color.Yellow
    ),
    Completed(
        displayName = "Completed",
        color = Color.Green
    )
}