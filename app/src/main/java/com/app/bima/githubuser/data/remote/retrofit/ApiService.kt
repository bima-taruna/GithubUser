package com.app.bima.githubuser.data.remote.retrofit

import com.app.bima.githubuser.data.remote.response.GithubUserResponse
import com.app.bima.githubuser.data.remote.response.ItemsItem
import com.app.bima.githubuser.data.remote.response.UserDetailResponse
import retrofit2.http.*

interface ApiService {
    @GET("search/users")
    suspend fun getGithubUser(
        @Query("q") username: String
    ): GithubUserResponse

    @GET("users/{username}")
    suspend fun getDetailUser(
        @Path("username") username: String?
    ) : UserDetailResponse

    @GET("users/{username}/{type}")
    suspend fun getFollow(
    @Path("username") username: String?,
    @Path("type") type: String
    ): List<ItemsItem>
}