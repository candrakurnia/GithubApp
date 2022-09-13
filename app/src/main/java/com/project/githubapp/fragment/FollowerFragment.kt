package com.project.githubapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.githubapp.*
import com.project.githubapp.adapter.ListUserAdapter
import com.project.githubapp.databinding.FragmentFollowerBinding
import com.project.githubapp.viewmodel.FollowersViewModel


class FollowerFragment : Fragment(R.layout.fragment_follower) {

    private var _binding: FragmentFollowerBinding? = null
    private val binding get() = _binding!!
    private lateinit var username: String
    private lateinit var viewModel: FollowersViewModel
    private lateinit var adapter: ListUserAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args = arguments
        username = args?.getString(DetailActivity.EXTRA_USER).toString()
        _binding = FragmentFollowerBinding.bind(view)

        adapter = ListUserAdapter()
        adapter.notifyDataSetChanged()

        binding.apply {
            rvFollower.setHasFixedSize(true)
            rvFollower.layoutManager = LinearLayoutManager(activity)
            rvFollower.adapter = adapter
        }
        showLoading(true)

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(FollowersViewModel::class.java)
        viewModel.setListFollower(username)
        viewModel.getListFollowers().observe(viewLifecycleOwner) {
            if (it != null) {
                adapter.setList(it)
                showLoading(false)
            }
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

