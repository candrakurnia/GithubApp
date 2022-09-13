package com.project.githubapp.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.project.githubapp.api.ApiConfig
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

    private val _isLoading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _isLoading
    val detailUser = MutableLiveData<DetailsResponse>()
    private var userDao: FavoriteDao?
    private var userDb: DatabaseFavorite?

    init {
        userDb = DatabaseFavorite.getDatabase(application)
        userDao = userDb?.favoriteDao()
    }

    fun setUserDetail(username: String) {
        _isLoading.value = true
        ApiConfig.getApiService().detailUser(username)
            .enqueue(object : Callback<DetailsResponse> {
                override fun onResponse(
                    call: Call<DetailsResponse>,
                    response: Response<DetailsResponse>
                ) {
                    _isLoading.value = (false)
                    if (response.isSuccessful) {
                        detailUser.postValue(response.body())
                    }
                }

                override fun onFailure(call: Call<DetailsResponse>, t: Throwable) {
                    _isLoading.value = (false)
                    Log.e(TAG, "failure :${t.message}")
                }

            })
    }

    fun getUserDetail(): LiveData<DetailsResponse> {
        return detailUser
    }

    fun addFavorite(username: String, id: Int, avatarUrl: String) {
        CoroutineScope(Dispatchers.IO).launch {
            var user = Favorite(
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