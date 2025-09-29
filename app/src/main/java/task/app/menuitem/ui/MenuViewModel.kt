package task.app.menuitem.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import task.app.menuitem.model.MenuApiHelper
import task.app.menuitem.model.ResultState
import task.app.menuitem.model.MenuData

/**
 * MenuViewModel for managing the UI-related data of the MainActivity.
 * It fetches the list of menu items asynchronously using MenuApiHelper and
 * exposes the result as LiveData to be observed by the MainActivity.
 */
class MenuViewModel(private val menuApiHelper: MenuApiHelper) : ViewModel() {

    // LiveData holding the state of the menu list (Loading, Success, Error)
    private val menuResultStat = MutableLiveData<ResultState<List<MenuData>>>()

    /**
     * Initiates a coroutine to fetch menu data from the API.
     * Emits loading, success, or error state through LiveData.
     */
    fun getMenuData() {
        viewModelScope.launch {
            menuResultStat.postValue(ResultState.Loading)
            try {
                val item = menuApiHelper.getMenus()
                menuResultStat.postValue(ResultState.Success(item)) // Post successful result to the observer
            } catch (e: Exception) {
                menuResultStat.postValue(ResultState.Error(e.message.toString())) // Post error result with exception message
            }
        }
    }

    /**
     * Returns the LiveData with the result state of the menu data.
     */
    fun getMenuResultStat() : LiveData<ResultState<List<MenuData>>> {
        return menuResultStat
    }
}