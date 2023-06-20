package com.app.bima.githubuser.data

import androidx.lifecycle.LiveData
import com.app.bima.githubuser.data.local.entity.GithubUserEntity
import com.app.bima.githubuser.data.local.room.GithubUserDao
import com.app.bima.githubuser.data.remote.response.GithubUserResponse
import com.app.bima.githubuser.data.remote.response.ItemsItem
import com.app.bima.githubuser.data.remote.response.UserDetailResponse
import com.app.bima.githubuser.data.remote.retrofit.ApiService
import com.app.bima.githubuser.utility.SettingPreferences
import kotlinx.coroutines.flow.Flow

class UserRepository private constructor(
    private val apiService: ApiService,
    private val userDao: GithubUserDao,
    private val settingPreferences: SettingPreferences
) {

    suspend fun getListUsers(query:String) : GithubUserResponse {
        return apiService.getGithubUser(query)
    }

    suspend fun getDetailUsers(username: String?) : UserDetailResponse {
        return apiService.getDetailUser(username)
    }

    suspend fun getFollowers(username: String?) : List<ItemsItem> {
        return apiService.getFollow(username, "followers")
    }

    suspend fun getFollowing(username: String?) : List<ItemsItem> {
        return apiService.getFollow(username, "following")
    }

    fun getFavoriteUser(username:String): LiveData<GithubUserEntity> {
        return userDao.getFavoriteUserByUsername(username)
    }

    fun getAllFavoriteUser(): LiveData<List<GithubUserEntity>> {
        return userDao.getUsers()
    }

    suspend fun insertFavorite(user: GithubUserEntity) {
       userDao.insertFavorite(user)
    }



    suspend fun deleteFavorite(user: GithubUserEntity) {
        userDao.deleteUser(user)
    }

    fun getThemeSettings(): Flow<Boolean> {
        return settingPreferences.getThemeSetting()
    }

    suspend fun saveThemeSetting(isDarkModeActive: Boolean) {
        settingPreferences.saveThemeSetting(isDarkModeActive)
    }

    companion object {

        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            apiService: ApiService,
            userDao: GithubUserDao,
            settingPreferences: SettingPreferences
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService,userDao, settingPreferences)
            }.also { instance = it }
    }
}