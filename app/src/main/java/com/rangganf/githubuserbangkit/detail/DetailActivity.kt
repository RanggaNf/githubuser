package com.rangganf.githubuserbangkit.detail

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import coil.load
import coil.transform.CircleCropTransformation
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.rangganf.githubuserbangkit.R
import com.rangganf.githubuserbangkit.data.adapter.DetailAdapter
import com.rangganf.githubuserbangkit.data.local.DatabaseModule
import com.rangganf.githubuserbangkit.model.DetailResponse
import com.rangganf.githubuserbangkit.model.UserResponse
import com.rangganf.githubuserbangkit.databinding.ActivityDetailBinding
import com.rangganf.githubuserbangkit.utils.Result
import androidx.appcompat.app.AppCompatDelegate

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private val viewModel by viewModels<DetailViewModel> {
        DetailViewModel.Factory(DatabaseModule(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // This is for Get the UserResponse.Item object passed from the previous activity
        val item = intent.getParcelableExtra<UserResponse.Item>("item")
        val username = item?.login ?: ""

        // Observe the detail user data from the ViewModel
        viewModel.resultDetailUser.observe(this) { result ->
            when (result) {
                is Result.Success<*> -> {
                    val user = result.data as DetailResponse
                    updateUI(user)
                }
                is Result.Error -> {
                    Toast.makeText(this, result.exception.message.toString(), Toast.LENGTH_SHORT).show()
                }
                is Result.Loading -> {
                    binding.progressbar.isVisible = result.isLoading
                }
            }
        }

        // Retrieve detailed user information from the API
        viewModel.getDetailUser(username)

        // Create fragments for followers and following tabs
        val fragments = mutableListOf<Fragment>(
            FollowsFragment.newInstance(FollowsFragment.FOLLOWERS),
            FollowsFragment.newInstance(FollowsFragment.FOLLOWING)
        )

        // Define tab titles
        val titleFragments = mutableListOf(
            getString(R.string.followers),
            getString(R.string.following)
        )

        // Create an adapter for the ViewPager
        val adapter = DetailAdapter(this, fragments)
        binding.viewpager.adapter = adapter

        // Attach the TabLayout to the ViewPager
        TabLayoutMediator(binding.tab, binding.viewpager) { tab, position ->
            tab.text = titleFragments[position]
        }.attach()

        // Listen to tab selection events to fetch followers or following
        binding.tab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab?.position == 0) {
                    viewModel.getFollowers(username)
                } else {
                    viewModel.getFollowing(username)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        // Fetch initial followers
        viewModel.getFollowers(username)

        // Setup favorite button and handle changes in its state
        setupFavoriteButton(item)

        // Setup night mode and adjust text colors accordingly
        setupNightMode()
    }

    // Update UI with detailed user information
    private fun updateUI(user: DetailResponse) {
        binding.apply {
            imgAvatar.load(user.avatarUrl) {
                transformations(CircleCropTransformation())
            }
            tvName.text = user.name
            tvUsername.text = user.login
            tvRepository.text = user.publicRepos.toString()
            tvFollowers.text = user.followers.toString()
            tvFollowing.text = user.following.toString()
        }
    }

    // Setup favorite button and its behavior
    private fun setupFavoriteButton(item: UserResponse.Item?) {
        binding.btnFavorite.setOnClickListener {
            viewModel.setFavorite(item)
            val resultIntent = Intent()
            resultIntent.putExtra("dataChanged", true)
            setResult(RESULT_OK, resultIntent)
        }

        viewModel.findFavorite(item?.id ?: 0) {
            binding.btnFavorite.changeIconColor(R.color.red)
        }

        viewModel.resultSuksesFavorite.observe(this) {
            binding.btnFavorite.changeIconColor(R.color.red)
        }

        viewModel.resultDeleteFavorite.observe(this) {
            binding.btnFavorite.changeIconColor(R.color.white)
        }
    }

    // Setup night mode and adjust text colors
    private fun setupNightMode() {
        val currentNightMode = AppCompatDelegate.getDefaultNightMode()
        val textColorRes = if (currentNightMode == AppCompatDelegate.MODE_NIGHT_YES) {
            R.color.white
        } else {
            R.color.black
        }

        setTextColorForMode(textColorRes)
    }

    // Handle options menu item selection
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    // Set text colors for UI elements based on the selected night mode
    private fun setTextColorForMode(@ColorRes textColor: Int) {
        binding.apply {
            tvName.setTextColor(ContextCompat.getColor(this@DetailActivity, textColor))
            tvUsername.setTextColor(ContextCompat.getColor(this@DetailActivity, textColor))
            tvRepository.setTextColor(ContextCompat.getColor(this@DetailActivity, textColor))
            tvFollowers.setTextColor(ContextCompat.getColor(this@DetailActivity, textColor))
            tvFollowing.setTextColor(ContextCompat.getColor(this@DetailActivity, textColor))
            textRepository.setTextColor(ContextCompat.getColor(this@DetailActivity, textColor))
            textFollowers.setTextColor(ContextCompat.getColor(this@DetailActivity, textColor))
            textFollowing.setTextColor(ContextCompat.getColor(this@DetailActivity, textColor))

        }
    }

    // Change the icon color of the FloatingActionButton
    fun FloatingActionButton.changeIconColor(@ColorRes color: Int) {
        imageTintList = ColorStateList.valueOf(ContextCompat.getColor(this.context, color))
    }
}

