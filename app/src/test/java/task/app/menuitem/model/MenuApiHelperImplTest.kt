package task.app.menuitem.model


import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import task.app.menuitem.network.ApiService

class MenuApiHelperImplTest {

    private lateinit var apiService: ApiService
    private lateinit var menuApiHelper: MenuApiHelper

    @Before
    fun setUp() {
        apiService = mockk()
        menuApiHelper = MenuApiHelperImpl(apiService)
    }

    @Test
    fun `getMenus should return list of menus`() = runTest {
        // Given
        val menuList = listOf(
            MenuData(1, "Burger", "Beef Burger", 4.5, "Tasty", "burger.jpg"),
            MenuData(2, "Salad", "Caesar", 3.0, "Fresh", "salad.jpg")
        )
        coEvery { apiService.getMenus() } returns menuList

        // When
        val result = menuApiHelper.getMenus()

        // Then
        assertEquals(2, result.size)
        assertEquals("Burger", result[0].category)
        assertEquals("Salad", result[1].category)
    }

    @Test
    fun `getMenuItem should return single menu item`() = runTest {
        // Given
        val menuItem = MenuData(10, "Pizza", "Margherita", 5.0, "Cheesy", "pizza.jpg")
        coEvery { apiService.getMenuItem(10) } returns menuItem

        // When
        val result = menuApiHelper.getMenuItem(10)

        // Then
        assertEquals(10, result.id)
        assertEquals("Pizza", result.category)
        assertEquals("Margherita", result.name)
    }
}
