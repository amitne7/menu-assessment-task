package task.app.menuitem.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import task.app.menuitem.R
import task.app.menuitem.databinding.ActivityMainBinding
import task.app.menuitem.model.MenuApiHelperImpl
import task.app.menuitem.model.ResultState
import task.app.menuitem.network.RetrofitClient
import task.app.menuitem.utils.ConstantUtils
import task.app.menuitem.utils.NetworkUtils
import task.app.menuitem.utils.ViewModelFactory


/**
 * MainActivity displays the list of menu items grouped by category (e.g. starter, main or dessert, etc.).
 * It uses a RecyclerView to show grouped menu data and handles navigation to the detail screen.
 */
class MainActivity : AppCompatActivity(), MenuAdapter.OnItemClickListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var menuViewModel: MenuViewModel
    private lateinit var adapter: MenuAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Enable full-screen layout handling
        binding = ActivityMainBinding.inflate(layoutInflater) // Inflate the layout using ViewBinding
        setContentView(binding.root)

        // Apply system bar insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // Initialize views and adapter
        initViews()

        // Set up MenuViewModel
        initViewModel()

        // Check internet connection and load menu data
        if (NetworkUtils.isInternetAvailable(this)) {
            menuViewModel.getMenuData()
        } else
            Toast.makeText(this, getString(R.string.error_no_internet), Toast.LENGTH_LONG).show()

        // Observe data state changes and update UI accordingly
        initData()
    }

    private fun initViews() {
        val toolbar = binding.toolbarHome
        setSupportActionBar(toolbar.toolbar)
        supportActionBar?.title = getString(R.string.text_menu)
        val rvMenu = binding.rvMenu
        rvMenu.layoutManager = LinearLayoutManager(this)
        adapter = MenuAdapter(this)
        rvMenu.adapter = adapter
    }

    //Observes the LiveData from MenuViewModel and updates the UI based on the ResultState.
    private fun initData() {
        menuViewModel.getMenuResultStat().observe(this) { it ->
            when(it) {
                is ResultState.Loading -> {
                    binding.mainProgressbar.visibility = View.VISIBLE
                }
                is ResultState.Success -> {
                    binding.mainProgressbar.visibility = View.GONE
                    adapter.submitGrouped(it.data.groupBy { it.category })
                }
                is ResultState.Error -> {
                    binding.mainProgressbar.visibility = View.GONE
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }


    /**
     * Handles click events on RecyclerView items.
     * Navigates to DetailActivity and passes the selected item's ID.
     */
    override fun onItemClicked(id: Int) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra(ConstantUtils.ITEM_ID, id)
        startActivity(intent)
    }

    /**
     * Initializes the MenuViewModel with a custom factory.
     * Injects MenuApiHelperImpl.
     */
    private fun initViewModel() {
        menuViewModel = ViewModelProvider(
            this,
            ViewModelFactory(
                MenuApiHelperImpl(RetrofitClient.apiService)
            )
        )[MenuViewModel::class.java]
    }


}