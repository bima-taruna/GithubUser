package com.app.bima.githubuser.di.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.bima.githubuser.data.remote.response.ItemsItem
import com.app.bima.githubuser.data.UserRepository
import com.app.bima.githubuser.utility.Result
import kotlinx.coroutines.launch
import java.lang.Exception

class FollowersViewModel(private val repository: UserRepository): ViewModel() {
    private val _uiStateFollowers = MutableLiveData<Result<List<ItemsItem?>>>()
    val uiStateFollowers: MutableLiveData<Result<List<ItemsItem?>>> = _uiStateFollowers

    fun getFollowers(username: String) {
        _uiStateFollowers.value = Result.Loading
        viewModelScope.launch {
            try {
                _uiStateFollowers.value = Result.Success(repository.getFollowers(username))
            } catch (e:Exception) {
                _uiStateFollowers.value = Result.Error(e.message.toString())
            }
        }
    }


}