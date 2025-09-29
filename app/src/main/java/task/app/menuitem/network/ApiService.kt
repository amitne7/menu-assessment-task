package task.app.menuitem.network

import retrofit2.http.GET
import retrofit2.http.Path
import task.app.menuitem.model.MenuData

/**
 * Retrofit interface that defines the network API endpoints used to
 * fetch menu data from the backend server.
 */
interface ApiService {
    // Get menu list
    @GET("menu/")
    suspend fun getMenus(): List<MenuData>

    //Get menu item details
    @GET("/menu/{id}")
    suspend fun getMenuItem(@Path("id") id: Int): MenuData
}