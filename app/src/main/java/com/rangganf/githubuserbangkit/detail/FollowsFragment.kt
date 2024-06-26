package com.rangganf.githubuserbangkit.detail

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.rangganf.githubuserbangkit.data.adapter.UserAdapter
import com.rangganf.githubuserbangkit.model.UserResponse
import com.rangganf.githubuserbangkit.databinding.FragmentFollowsBinding
import com.rangganf.githubuserbangkit.utils.Result

class FollowsFragment : Fragment() {

    // Declaration of binding for the fragment's view
    private var binding: FragmentFollowsBinding? = null

    // Initialization of RecyclerView adapter
    private val adapter by lazy {
        UserAdapter { user ->
            onUserItemClicked(user)
        }
    }

    // Initialization of the ViewModel used in the activity
    private val viewModel by activityViewModels<DetailViewModel>()

    // Variable to determine the view type (FOLLOWERS or FOLLOWING)
    var type = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for the fragment
        binding = FragmentFollowsBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configure the RecyclerView
        binding?.rvFollows?.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            setHasFixedSize(true)
            adapter = this@FollowsFragment.adapter
        }

        // Determine the type of data to be fetched (FOLLOWERS or FOLLOWING)
        when (type) {
            FOLLOWERS -> {
                // Observer for followers data result
                viewModel.resultFollowersUser.observe(viewLifecycleOwner, this::manageResultFollows)
            }

            FOLLOWING -> {
                // Observer for following data result
                viewModel.resultFollowingUser.observe(viewLifecycleOwner, this::manageResultFollows)
            }
        }
    }

    // Function to manage the result of followers or following data
    private fun manageResultFollows(state: Result) {
        when (state) {
            is Result.Success<*> -> {
                // If the data is successfully loaded, set it to the RecyclerView adapter
                adapter.setData(state.data as MutableList<UserResponse.Item>)
            }
            is Result.Error -> {
                // If an error occurs, display an error message using Toast
                Toast.makeText(
                    requireActivity(),
                    state.exception.message.toString(),
                    Toast.LENGTH_SHORT
                ).show()
            }
            is Result.Loading -> {
                // Show or hide the ProgressBar based on the loading status
                binding?.progressBar?.isVisible = state.isLoading
            }
        }
    }

    // Function to handle user item clicks
    private fun onUserItemClicked(user: UserResponse.Item) {
        // Here you can handle actions when a user item is clicked
        // Example: Navigating to a new user detail page
        val intent = Intent(requireContext(), DetailActivity::class.java)
        intent.putExtra("item", user)
        startActivity(intent)
    }

    companion object {
        // Constants to determine the view type
        const val FOLLOWING = 100
        const val FOLLOWERS = 101

        // This function is created for newInstance to create a fragment instance with a specific view type
        fun newInstance(type: Int) = FollowsFragment()
            .apply {
                this.type = type
            }
    }
}
