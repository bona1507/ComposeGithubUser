package com.example.composegithubuser.data.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FUDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addFavorite(favoriteUser: FUEntity)

    @Query("SELECT * FROM FUEntity ORDER BY username ASC")
    suspend fun getAllFavoriteUsers(): List<FUEntity>

    @Query("SELECT EXISTS(SELECT * FROM FUEntity WHERE username = :username)")
    fun isUserFavorite(username: String): LiveData<Boolean>

    @Query("DELETE FROM FUEntity Where username = :username")
    suspend fun deleteFavorite(username: String)
}