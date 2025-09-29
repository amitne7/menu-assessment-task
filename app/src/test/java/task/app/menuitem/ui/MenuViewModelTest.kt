package task.app.menuitem.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.*
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import task.app.menuitem.model.*

@OptIn(ExperimentalCoroutinesApi::class)
class MenuViewModelTest {

    // Rule to allow LiveData to emit instantly
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    // Use StandardTestDispatcher for controlled coroutine execution
    private val testDispatcher = StandardTestDispatcher()

    @Mock
    private lateinit var menuApiHelper: MenuApiHelper

    private lateinit var viewModel: MenuViewModel

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher) // Set dispatcher for coroutines
        viewModel = MenuViewModel(menuApiHelper)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `fetMenuData should emit success when API returns data`() = runTest {
        val fakeMenus = listOf(
            MenuData(1, "dessert", "Cupcake", 2.5, "Sweet cupcake", "image1.jpg"),
            MenuData(2, "main", "Burger", 5.0, "Tasty burger", "image2.jpg")
        )

        `when`(menuApiHelper.getMenus()).thenReturn(fakeMenus)

        val observer = mock<Observer<ResultState<List<MenuData>>>>()
        viewModel.getMenuResultStat().observeForever(observer)

        viewModel.getMenuData()
        advanceUntilIdle()

        // Verify Loading then Success are emitted
        verify(observer).onChanged(ResultState.Loading)
        verify(observer).onChanged(ResultState.Success(fakeMenus))
    }

    @Test
    fun `fetMenuData should emit error on API exception`() = runTest {
        `when`(menuApiHelper.getMenus()).thenThrow(RuntimeException("Network error"))

        val observer = mock<Observer<ResultState<List<MenuData>>>>()
        viewModel.getMenuResultStat().observeForever(observer)

        viewModel.getMenuData()
        advanceUntilIdle()

        // Verify Loading then Error are emitted
        verify(observer).onChanged(ResultState.Loading)
        verify(observer).onChanged(ResultState.Error("Network error"))

        // clean up observer
        viewModel.getMenuResultStat().removeObserver(observer)
    }

}