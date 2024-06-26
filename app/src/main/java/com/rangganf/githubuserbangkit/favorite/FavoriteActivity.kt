package com.rangganf.githubuserbangkit.favorite

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.rangganf.githubuserbangkit.data.adapter.UserAdapter
import com.rangganf.githubuserbangkit.data.local.DatabaseModule
import com.rangganf.githubuserbangkit.databinding.ActivityFavoriteBinding
import com.rangganf.githubuserbangkit.detail.DetailActivity

class FavoriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteBinding
    private val REQUEST_DETAIL = 1 // Constant for request code

    private val adapter by lazy {
        UserAdapter { user ->
            Intent(this, DetailActivity::class.java).apply {
                putExtra("item", user)
                startActivityForResult(this, REQUEST_DETAIL) // Start DetailActivity with requestCode
            }
        }
    }

    private val viewModel by viewModels<FavoriteViewModel> {
        // Use viewModels to get an instance of FavoriteViewModel
        // with a Factory that requires DbModule as a dependency
        FavoriteViewModel.Factory(DatabaseModule(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.rvFavorite.layoutManager = LinearLayoutManager(this)
        binding.rvFavorite.adapter = adapter // Set the adapter to the RecyclerView

        viewModel.getUserFavorite().observe(this) {
            adapter.setData(it) // Use LiveData from ViewModel to populate the adapter's data
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish() // Close the activity when the back button in the toolbar is clicked
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_DETAIL && resultCode == RESULT_OK) {
            val dataChanged = data?.getBooleanExtra("dataChanged", false) ?: false
            if (dataChanged) {
                // Call the function again to retrieve favorite data after returning from DetailActivity
                viewModel.getUserFavorite().observe(this) {
                    adapter.setData(it)
                }
            }
        }
    }
}
