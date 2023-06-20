package com.app.bima.githubuser.di.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.bima.githubuser.data.remote.response.ItemsItem
import com.app.bima.githubuser.data.UserRepository
import com.app.bima.githubuser.utility.Result
import kotlinx.coroutines.launch
import java.lang.Exception

class FollowingViewModel(private val repository: UserRepository): ViewModel() {
    private val _uiStateFollowing = MutableLiveData<Result<List<ItemsItem?>>>()
    val uiStateFollowing: MutableLiveData<Result<List<ItemsItem?>>> = _uiStateFollowing

    fun getFollowing(username: String?) {
        _uiStateFollowing.value = Result.Loading
        viewModelScope.launch {
            try {
                _uiStateFollowing.value = Result.Success(repository.getFollowing(username))
            } catch (e: Exception) {
                _uiStateFollowing.value = Result.Error(e.message.toString())
            }
        }
    }
}