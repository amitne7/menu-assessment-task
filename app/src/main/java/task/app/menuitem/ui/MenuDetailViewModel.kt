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
 * MenuDetailViewModel for managing the UI-related data of the DetailActivity.
 * It fetches the the details of a single menu item using MenuApiHelper and
 * exposes the result as LiveData to be observed by the DetailActivity.
 */
class MenuDetailViewModel(private val apiHelper: MenuApiHelper) : ViewModel() {

    // LiveData holding the state of the menu item (Loading, Success, Error)
    private val menuItemResultStat = MutableLiveData<ResultState<MenuData>>()

    /**
     * Initiates a coroutine to fetch menu Item data from the API.
     * Emits loading, success, or error state through LiveData.
     */
    fun getMenuItemById(id: Int) {
        viewModelScope.launch {
            menuItemResultStat.postValue(ResultState.Loading) // Loading state
            try {
                val item = apiHelper.getMenuItem(id)
                menuItemResultStat.postValue(ResultState.Success(item)) // Post successful result to the observer
            } catch (e: Exception) {
                menuItemResultStat.postValue(ResultState.Error(e.message.toString())) // Post error result with exception message

            }
        }
    }

    /**
     * Returns the LiveData with the result state of the menu item data.
     */
    fun getMenuItemResultState(): LiveData<ResultState<MenuData>> {
        return menuItemResultStat
    }
}