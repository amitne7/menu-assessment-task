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
class MenuDetailViewModelTest {

    // Rule to allow LiveData to emit instantly
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    // Use StandardTestDispatcher for controlled coroutine execution
    private val testDispatcher = StandardTestDispatcher()

    @Mock
    private lateinit var menuApiHelper: MenuApiHelper

    private lateinit var viewModel: MenuDetailViewModel

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher) // Set dispatcher for coroutines
        viewModel = MenuDetailViewModel(menuApiHelper)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getMenuItemById should emit Success when API returns item`() = runTest {
        val menuItem = MenuData(1, "starter", "Tea", 1.5, "Green tea", "image.jpg")
        `when`(menuApiHelper.getMenuItem(1)).thenReturn(menuItem)

        val observer = mock<Observer<ResultState<MenuData>>>()
        viewModel.getMenuItemResultState().observeForever(observer)

        viewModel.getMenuItemById(1)
        advanceUntilIdle()

        // Verify Loading then Success are emitted
        verify(observer).onChanged(ResultState.Loading)
        verify(observer).onChanged(ResultState.Success(menuItem))

    }

    @Test
    fun `getMenuItemById should emit Error when API throws exception`() = runTest {
        `when`(menuApiHelper.getMenuItem(99)).thenThrow(RuntimeException("Not found"))

        val observer = mock<Observer<ResultState<MenuData>>>()
        viewModel.getMenuItemResultState().observeForever(observer)

        viewModel.getMenuItemById(99)
        advanceUntilIdle()

        // Verify Loading then Error are emitted
        verify(observer).onChanged(ResultState.Loading)
        verify(observer).onChanged(ResultState.Error("Not found"))

        // clean up observer
        viewModel.getMenuItemResultState().removeObserver(observer)
    }
}