package com.project.githubapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.githubapp.api.ApiConfig
import com.project.githubapp.model.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowersViewModel : ViewModel() {
    companion object {
        private const val TAG = "FollowersViewModel"
    }
    val listFollower =  MutableLiveData<ArrayList<User>>()

    fun setListFollower(username : String){
        ApiConfig.getApiService().setFollowers(username)
            .enqueue(object : Callback<ArrayList<User>>{
                override fun onResponse(
                    call: Call<ArrayList<User>>,
                    response: Response<ArrayList<User>>
                ) {
                    if (response.isSuccessful) {
                        listFollower.postValue(response.body())
                    }
                }

                override fun onFailure(call: Call<ArrayList<User>>, t: Throwable) {
                    Log.e(TAG,"onFailure : ${t.message}")
                }

            })
    }

    fun getListFollowers(): LiveData<ArrayList<User>> {
        return listFollower
    }
}