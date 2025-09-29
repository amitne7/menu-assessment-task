package task.app.menuitem.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import task.app.menuitem.R
import task.app.menuitem.databinding.ActivityDetailBinding
import task.app.menuitem.model.MenuApiHelperImpl
import task.app.menuitem.model.ResultState
import task.app.menuitem.network.RetrofitClient
import task.app.menuitem.utils.ConstantUtils
import task.app.menuitem.utils.NetworkUtils
import task.app.menuitem.utils.ViewModelFactory

/**
 * DetailActivity displays the detailed information of a single menu item.
 * It fetches the item by ID from the MenuDetailViewModel.
 */
class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var menuDetailViewModel: MenuDetailViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.details)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Retrieve item ID passed from MainActivity
        val menuItemId = intent.getIntExtra(ConstantUtils.ITEM_ID, 0)
        setupToolbar() // Set up toolbar with back button
        initViewModel() // Initialize MenuDetailViewModel

        // Fetch data if internet is available
        if (NetworkUtils.isInternetAvailable(this)) {
            menuDetailViewModel.getMenuItemById(menuItemId)
        } else
            Toast.makeText(this, getString(R.string.error_no_internet), Toast.LENGTH_LONG).show()
        // Observe data and update UI
        initData()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
    }

    private fun initData() {
        menuDetailViewModel.getMenuItemResultState().observe(this) {
            when(it) {
                is ResultState.Loading -> {
                    binding.detailProgressbar.visibility = View.VISIBLE
                }
                is ResultState.Success -> {
                    binding.detailProgressbar.visibility = View.GONE
                    it.data.let { menuItem ->
                        // CollapsingToolbar title
                        binding.collapsingToolbar.title = menuItem.name

                        // Image
                        Glide.with(this)
                            .load(menuItem.image)
                            .placeholder(R.mipmap.ic_launcher)
                            .into(binding.ivMenuItem)

                        // Details
                        binding.tvName.text = menuItem.name
                        binding.tvCategory.text = menuItem.category
                        binding.tvPrice.text = "£%.2f".format(menuItem.price)
                        binding.tvDescription.text = menuItem.description
                    }
                }
                is ResultState.Error -> {
                    binding.detailProgressbar.visibility = View.GONE
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                }
            }
        }

    }

    private fun initViewModel() {
        menuDetailViewModel = ViewModelProvider(
            this,
            ViewModelFactory(
                MenuApiHelperImpl(RetrofitClient.apiService)
            )
        )[MenuDetailViewModel::class.java]
    }
}