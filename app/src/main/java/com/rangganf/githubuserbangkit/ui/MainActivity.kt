package com.rangganf.githubuserbangkit.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.rangganf.githubuserbangkit.R
import com.rangganf.githubuserbangkit.data.adapter.UserAdapter
import com.rangganf.githubuserbangkit.data.local.SettingPreferences
import com.rangganf.githubuserbangkit.databinding.ActivityMainBinding
import com.rangganf.githubuserbangkit.detail.DetailActivity
import com.rangganf.githubuserbangkit.favorite.FavoriteActivity
import com.rangganf.githubuserbangkit.model.UserResponse
import com.rangganf.githubuserbangkit.setting.SettingActivity
import com.rangganf.githubuserbangkit.utils.Result

class MainActivity : AppCompatActivity() {

    // Ngapalne gawe View Binding
    private lateinit var binding: ActivityMainBinding

    // Ngapalne the Adapter gawe Lazy
    private val adapter by lazy {
        UserAdapter { user ->
            // Create an Intent to open DetailActivity with user data
            Intent(this, DetailActivity::class.java).apply {
                putExtra("item", user)
                startActivity(this)
            }
        }
    }

    // Initialize ViewModel nggunakake Delegate Properties
    private val viewModel by viewModels<MainViewModel> {
        MainViewModel.Factory(SettingPreferences(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Gunakake View Binding kanggo inflate tata letak
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Mirsani owah-owahan tema ing app lan ngganti mode wengi yen perlu tapi aku raiso.
        viewModel.getTheme().observe(this) {
            if (it) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        // Setel manajer tata letak utowo layout lan adaptor kanggo RecyclerView
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.adapter = adapter

        // Setel SearchView kanggo telusuran pangguna GitHub
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                // Call the function to search for users based on the keyword
                viewModel.getUser(p0.toString())
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean = false
        })

        // Mirsani hasil panelusuran pangguna
        viewModel.resultUser.observe(this) { result ->
            when (result) {
                //Gawe nguwehi pemberitahuan hasil
                is Result.Success<*> -> {
                    if (result.data is UserResponse) {
                        val userResponse = result.data as UserResponse
                        adapter.setData(userResponse.items)
                        showText(adapter.itemCount)
                    }
                }
                is Result.Error -> {
                    // Gawe Nguwehi Pemberitahuan Eror
                    Toast.makeText(this, result.exception.message.toString(), Toast.LENGTH_SHORT).show()
                }
                is Result.Loading -> {
                    // Gawe nampilne Progres bar seng kluwer"
                    binding.progressBar.isVisible = result.isLoading
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Noto menu layout menyang action bar
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.favorite -> {
                // Mbukak FavoriteActivity pas Menu favorit Di pilih.
                Intent(this, FavoriteActivity::class.java).apply {
                    startActivity(this)
                }
            }
            R.id.setting -> {
                // Bukak SettingActivity nalika menu Setelan dipilih
                Intent(this, SettingActivity::class.java).apply {
                    startActivity(this)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    // Fungsine kanggo nuduhake utawa ndhelikake pesen teks nde dasar count total
    private fun showText(totalCount: Int) {
        with(binding) {
            if (totalCount == 0) {
                tvNotice.visibility = View.VISIBLE
                tvNotice.text = resources.getString(R.string.not_found) // Use the correct message
            } else {
                tvNotice.visibility = View.INVISIBLE
            }
        }
    }

    // Override onResume () ing njaba onCreate ()
    override fun onResume() {
        super.onResume()

        // Mulai nggoleki pangguna GitHub maneh nalika activity di buka maneh
        viewModel.getUser("google")
    }
}
