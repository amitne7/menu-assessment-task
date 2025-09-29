package task.app.menuitem.model

import com.google.gson.annotations.SerializedName

/**
 * Data class representing a menu item fetched from the API.
 * Each property is annotated with [SerializedName] to match the JSON keys returned by the backend service.
 */
data class MenuData(
    @SerializedName("id")
    val id: Int,

    @SerializedName("category")
    val category: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("price")
    val price: Double,

    @SerializedName("description")
    val description: String,

    @SerializedName("image")
    val image: String
)
