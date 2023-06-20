package com.app.bima.githubuser.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.app.bima.githubuser.data.local.entity.GithubUserEntity

@Dao
interface GithubUserDao {
    @Query("SELECT * FROM GithubUserEntity")
    fun getUsers(): LiveData<List<GithubUserEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(userEntity: GithubUserEntity)

    @Query("SELECT * FROM GithubUserEntity WHERE username = :username")
    fun getFavoriteUserByUsername(username: String): LiveData<GithubUserEntity>

    @Delete
    suspend fun deleteUser(user:GithubUserEntity)
}