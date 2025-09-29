package task.app.menuitem.model

sealed interface ResultState<out T> {

    // Success state with the expected data
    data class Success<T>(val data: T) : ResultState<T>

    // Error state with message
    data class Error(val message: String) :ResultState<Nothing>

    // Loading state while in progress
    data object Loading: ResultState<Nothing>
}