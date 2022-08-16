package com.project.githubapp

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.project.githubapp.data.DatabaseFavorite
import com.project.githubapp.data.Favorite
import com.project.githubapp.data.FavoriteDao
import com.project.githubapp.model.User

class FavoriteViewModel(application: Application) : AndroidViewModel(application){

    private var userDao: FavoriteDao?
    private var userDb: DatabaseFavorite?

    private val _listUser = MutableLiveData<List<User>>()
    val listUser: LiveData<List<User>> = _listUser

    init {
        userDb = DatabaseFavorite.getDatabase(application)
        userDao = userDb?.favoriteDao()
    }

    fun getFavorite() : LiveData<List<Favorite>>? {
        return userDao?.getFavorite()
    }
}