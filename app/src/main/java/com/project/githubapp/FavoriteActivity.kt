package com.project.githubapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.githubapp.adapter.ListUserAdapter
import com.project.githubapp.data.Favorite
import com.project.githubapp.databinding.ActivityFavoriteBinding
import com.project.githubapp.model.User
import com.project.githubapp.viewmodel.FavoriteViewModel

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var adapter: ListUserAdapter
    private lateinit var viewModel: FavoriteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = ListUserAdapter()
        adapter.notifyDataSetChanged()

        adapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback{
            override fun onItemClicked(data: User) {
                Intent(this@FavoriteActivity, DetailActivity::class.java).also {
                    it.putExtra(DetailActivity.EXTRA_USER, data.login)
                    it.putExtra(DetailActivity.EXTRA_ID, data.id)
                    it.putExtra(DetailActivity.EXTRA_AVATAR, data.avatar_url)
                    startActivity(it)
                }
            }

        })


        viewModel = ViewModelProvider(
            this
        ).get(FavoriteViewModel::class.java)

        binding.apply {
            rvFavorite.setHasFixedSize(true)
            rvFavorite.layoutManager = LinearLayoutManager(this@FavoriteActivity)
            rvFavorite.adapter = adapter
        }

        viewModel.getFavorite()?.observe(this) {
            if (it != null) {
                val list = listing(it)
                adapter.setList(list)
            }
        }

    }

    private fun listing(users: List<Favorite>) : ArrayList<User> {
        val listUser = ArrayList<User>()
        for (user in users) {
            val userMap = User(
                user.login,
                user.id,
                user.avatar_url,
            )
            listUser.add(userMap)
        }
        return listUser
    }

}