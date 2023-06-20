package com.app.bima.githubuser.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.app.bima.githubuser.data.local.entity.GithubUserEntity

@Database(entities = [GithubUserEntity::class], version = 1)
abstract class GithubUserDatabase : RoomDatabase() {
    abstract fun getUsersDao() : GithubUserDao

    companion object {
        @Volatile
        private var instance:GithubUserDatabase? = null
        fun getInstance(context:Context) : GithubUserDatabase =
            instance ?: synchronized(this) {
               instance ?: Room.databaseBuilder(
                   context.applicationContext,
                   GithubUserDatabase::class.java, "GithubUser.db"
               ).build()
            }
    }
}