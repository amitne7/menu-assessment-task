package task.app.menuitem.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import task.app.menuitem.model.MenuApiHelper
import task.app.menuitem.ui.MenuDetailViewModel
import task.app.menuitem.ui.MenuViewModel

/**
 * A generic ViewModelFactory used to instantiate ViewModels that require constructor dependencies.
 */
class ViewModelFactory(
    private val dependency: Any
): ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // Check which ViewModel class is requested and return it with the correct dependency
        if (modelClass.isAssignableFrom(MenuViewModel::class.java)) {
            return MenuViewModel(dependency as MenuApiHelper) as T
        }
        if (modelClass.isAssignableFrom(MenuDetailViewModel::class.java)) {
            return MenuDetailViewModel(dependency as MenuApiHelper) as T
        }
        // If the class is not recognized, throw an exception
        throw IllegalArgumentException("Unknown class name")
    }
}