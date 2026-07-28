package ni.gob.minsa.myapplication

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import ni.gob.minsa.myapplication.composables.create.CreateTaskScreen
import ni.gob.minsa.myapplication.composables.details.TaskDetailsScreen
import ni.gob.minsa.myapplication.composables.edit.EditTaskScreen
import ni.gob.minsa.myapplication.composables.home.HomeScreen

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    val isDark = isSystemInDarkTheme()
    MaterialTheme(
        colorScheme = if (isDark) {
            darkColorScheme()
        } else {
            lightColorScheme()
        },
        content = content
    )
}

@Composable
fun App() {
    AppTheme {
        val navController = rememberNavController()

        NavHost(navController = navController, startDestination = MainRoute.Home) {
            composable<MainRoute.Home> {
                HomeScreen(
                    onCreateTask = { navController.navigate(MainRoute.Create) },
                    onOpenTask = { navController.navigate(MainRoute.Details(it.id)) }
                )
            }
            composable<MainRoute.Create> {
                CreateTaskScreen(
                    onNavigateUp = { navController.popBackStack() },
                    onSaved = { navController.popBackStack() }
                )
            }
            composable<MainRoute.Details> {
                val taskId = it.toRoute<MainRoute.Details>().taskId
                TaskDetailsScreen(
                    taskId = taskId,
                    onNavigateUp = { navController.popBackStack() },
                    onEdit = { task ->
                        navController.navigate(MainRoute.Edit(task.id))
                    },
                    onDeleted = {
                        navController.popBackStack()
                    }
                )
            }
            composable<MainRoute.Edit> {
                val taskId = it.toRoute<MainRoute.Edit>().taskId
                EditTaskScreen(
                    taskId = taskId,
                    onNavigateUp = { navController.popBackStack() },
                    onSaved = { navController.popBackStack() }
                )
            }
        }
    }
}