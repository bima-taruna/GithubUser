package com.app.bima.githubuser.di

import android.content.Context
import com.app.bima.githubuser.data.local.room.GithubUserDatabase
import com.app.bima.githubuser.data.remote.retrofit.ApiConfig
import com.app.bima.githubuser.data.UserRepository
import com.app.bima.githubuser.utility.SettingPreferences
import com.app.bima.githubuser.utility.dataStore

object Injection {
    fun provideRepository(context:Context): UserRepository {
        val apiService = ApiConfig.getApiService()
        val database = GithubUserDatabase.getInstance(context)
        val dao = database.getUsersDao()
        val pref = SettingPreferences.getInstance(context.dataStore)
        return UserRepository.getInstance(apiService, dao, pref)
    }
}