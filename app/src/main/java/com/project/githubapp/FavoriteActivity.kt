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

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var adapter: ListUserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = ListUserAdapter()
        adapter.notifyDataSetChanged()


        val favoriteViewModel = ViewModelProvider(
            this
        ).get(FavoriteViewModel::class.java)
        favoriteViewModel.listUser.observe(this) {
            setListUser()

        }

        binding.apply {
            rvFavorite.setHasFixedSize(true)
            val layoutManager = LinearLayoutManager(this@FavoriteActivity)
            binding.rvFavorite.layoutManager = layoutManager
            val itemDecoration =
                DividerItemDecoration(this@FavoriteActivity, layoutManager.orientation)
            binding.rvFavorite.addItemDecoration(itemDecoration)
            rvFavorite.adapter = adapter
        }

        favoriteViewModel.getFavorite()?.observe(this) {
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
                user.avatar_url
            )
            listUser.add(userMap)
        }
        return listUser
    }


    private fun setListUser() {
        val listUser = ListUserAdapter()
        binding.rvFavorite.adapter = listUser
        getUser(listUser)
    }

    private fun getUser(listUser: ListUserAdapter) {
        listUser.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                Toast.makeText(this@FavoriteActivity, "Berhasil", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@FavoriteActivity, DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_USER, data.login)
                intent.putExtra(DetailActivity.EXTRA_ID, data.id)
                intent.putExtra(DetailActivity.EXTRA_ID, data.avatar_url)
                startActivity(intent)
            }

        })
    }

}

//        favoriteViewModel.getFavorite()?.observe(this) {
//            setFavorite(it)
//        }


//    private fun setFavorite(it: List<Favorite>): ArrayList<List<ItemsItem>> {
//        val fav = ArrayList<List<ItemsItem>>()
//        for (user in it) {
//            val userMapped = ItemsItem {
//                user.login,
//                user.avatar_url,
//                user.id
//
//            }
//            fav.add(userMapped)
//        }
//        return fav
//    }