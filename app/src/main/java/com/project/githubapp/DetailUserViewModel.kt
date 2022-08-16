package com.project.githubapp

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.githubapp.data.DatabaseFavorite
import com.project.githubapp.data.Favorite
import com.project.githubapp.data.FavoriteDao
import com.project.githubapp.model.DetailsResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailUserViewModel(application: Application) : AndroidViewModel(application) {
    companion object {
        private const val TAG = "DetailUserViewModel"
    }

    private val _detailUser = MutableLiveData<DetailsResponse>()
    private var userDao: FavoriteDao?
    private var userDb: DatabaseFavorite?

    init {
        userDb = DatabaseFavorite.getDatabase(application)
        userDao = userDb?.favoriteDao()
    }

    fun setUserDetail(username: String) {
        ApiConfig.getApiService().detailUser(username)
            .enqueue(object : Callback<DetailsResponse> {
                override fun onResponse(
                    call: Call<DetailsResponse>,
                    response: Response<DetailsResponse>
                ) {
                    if (response.isSuccessful) {
                        _detailUser.postValue(response.body())
                    }
                }

                override fun onFailure(call: Call<DetailsResponse>, t: Throwable) {
                    Log.e(TAG, "failure :${t.message}")
                }

            })
    }

    fun getUserDetail(): LiveData<DetailsResponse> {
        return _detailUser
    }

    fun addFavorite(username: String, id: Int, avatarUrl : String) {
        CoroutineScope(Dispatchers.IO).launch {
            val user = Favorite(
                username,
                id,
                avatarUrl
            )
            userDao?.addFavorite(user)
        }
    }

    suspend fun checkUser(id: Int) = userDao?.checkUser(id)

    fun removeFavorite(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            userDao?.removeFromFavorite(id)
        }
    }
}