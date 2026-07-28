package ni.gob.minsa.myapplication

import kotlinx.serialization.Serializable

@Serializable
sealed class MainRoute {
    @Serializable
    data object Home : MainRoute()
    @Serializable
    data object Create : MainRoute()
    @Serializable
    data class Details(val taskId: Long) : MainRoute()
    @Serializable
    data class Edit(val taskId: Long) : MainRoute()
}