package task.app.menuitem.model

/**
 * MenuApiHelper Interface for fetching menu data.
 */
interface MenuApiHelper {
    suspend fun getMenus(): List<MenuData> // Fetches a list of all menu items.
    suspend fun getMenuItem(id: Int): MenuData //Fetches a single menu item by its ID.
}