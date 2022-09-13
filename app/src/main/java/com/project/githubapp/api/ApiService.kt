package com.project.githubapp.api

import com.project.githubapp.model.DetailsResponse
import com.project.githubapp.model.User
import com.project.githubapp.model.UserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @Headers("Authorization: token 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")
    @GET("search/users")
    fun search(
        @Query("q") query: String
    ): Call<UserResponse>

    @Headers("Authorization: token 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")
    @GET("users/{username}")
    fun detailUser(
        @Path("username") username : String
    ): Call<DetailsResponse>

    @Headers("Authorization: token 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")
    @GET("users/{username}/followers")
    fun setFollowers(
        @Path("username") username : String
    ): Call<ArrayList<User>>

@Headers("Authorization: token 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")
    @GET("users/{username}/following")
    fun setFollowing(
        @Path("username") username : String
    ): Call<ArrayList<User>>

}