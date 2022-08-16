package com.project.githubapp.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FavoriteDao {
    @Insert
    fun addFavorite(favorite: Favorite)

    @Query("SELECT * FROM favorite")
    fun getFavorite() : LiveData<List<Favorite>>

    @Query("SELECT count(*) FROM favorite WHERE favorite.id = :id")
    fun checkUser(id : Int) : Int

    @Query("DElETE FROM favorite WHERE favorite.id = :id")
    fun removeFromFavorite(id: Int) :Int
}