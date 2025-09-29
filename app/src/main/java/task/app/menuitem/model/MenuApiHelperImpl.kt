package task.app.menuitem.model

import task.app.menuitem.network.ApiService

/**
 * Implementation of MenuApiHelper that uses ApiService to perform actual network operations.
 */
class MenuApiHelperImpl(private val apiService: ApiService) : MenuApiHelper {
    override suspend fun getMenus() = apiService.getMenus() //Fetch the full list of menu items from the API.

    override suspend fun getMenuItem(id: Int) = apiService.getMenuItem(id) // Fetch details of menu item by its ID.
}