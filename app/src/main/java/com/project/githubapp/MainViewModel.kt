package com.project.githubapp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.githubapp.model.User
import com.project.githubapp.model.UserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {


    private val _listUser = MutableLiveData<List<User>>()
    val listUser: LiveData<List<User>> = _listUser


    private val _isLoading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _isLoading



    companion object{
        private const val TAG = "MainViewModel"
    }

    fun searchUser(query: String) {
        _isLoading.value =(true)
        val client = ApiConfig.getApiService().search(query)
        client.enqueue(object : Callback<UserResponse> {
            override fun onResponse(
                call: Call<UserResponse>,
                response: Response<UserResponse>
            ) {
                _isLoading.value = (false)
                if (response.isSuccessful) {
                    _listUser.value = response.body()?.items
                    Log.d(TAG,"MASUK KOK")
                } else {
                    Log.e(TAG, "onFailure : ${response.message()}")
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                _isLoading.value = (false)
                Log.e(TAG, "onFailure : ${t.message}")
            }

        })
    }
}