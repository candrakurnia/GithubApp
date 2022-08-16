package com.project.githubapp.model

data class DetailsResponse(
    val login : String,
    val id : Int,
    val avatar_url : String,
    val followers_url : String,
    val following_url : String,
    val repos_url : String,
    val name : String,
    val location : String,
    val company : String,
    val url : String,
    val following : Int,
    val follower : Int
)

