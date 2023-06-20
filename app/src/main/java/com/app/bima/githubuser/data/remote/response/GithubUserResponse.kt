package com.app.bima.githubuser.data.remote.response
import com.google.gson.annotations.SerializedName
data class GithubUserResponse(
	val totalCount: Int,
	val incompleteResults: Boolean,
	val items: List<ItemsItem>
)

data class ItemsItem(

	@field:SerializedName("login")
	val login: String,

	@field:SerializedName("avatar_url")
	val avatar_url: String,
)

