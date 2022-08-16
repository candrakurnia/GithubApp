package com.project.githubapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.githubapp.*
import com.project.githubapp.adapter.ListUserAdapter
import com.project.githubapp.databinding.FragmentFollowingBinding


class FollowingFragment : Fragment(R.layout.fragment_following) {


    private var _binding: FragmentFollowingBinding? = null
    private val binding get() = _binding!!
    private lateinit var username: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args = arguments
        username = args?.getString(DetailActivity.EXTRA_USER).toString()
        _binding = FragmentFollowingBinding.bind(view)


        showLoading(true)
        val viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(FollowingViewModel::class.java)
        viewModel.setListFollowing(username)
        viewModel.getListFollowing().observe(viewLifecycleOwner) { itemnama ->
            setListUser()
        }


    }

    private fun setListUser() {
        val listUser = ListUserAdapter()
        binding.apply {
            rvFollowing.setHasFixedSize(true)
            rvFollowing.layoutManager = LinearLayoutManager(activity)
            rvFollowing.adapter = listUser
            showLoading(false)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showLoading(b: Boolean) {
        binding.progressBar.visibility = if (b) View.VISIBLE else View.GONE


    }
}