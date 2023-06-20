package com.app.bima.githubuser.di.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.bima.githubuser.data.local.entity.GithubUserEntity
import com.app.bima.githubuser.data.UserRepository
import com.app.bima.githubuser.utility.Result
import kotlinx.coroutines.launch

class FavoriteViewModel(private val repository: UserRepository) : ViewModel() {
    private val _favoriteUiState = MutableLiveData<Result<LiveData<List<GithubUserEntity>>>>()
    val favoriteUiState: LiveData<Result<LiveData<List<GithubUserEntity>>>> = _favoriteUiState


    init {
        getFavoriteUser()
    }

    private fun getFavoriteUser() {
        _favoriteUiState.value = Result.Loading
        viewModelScope.launch {
            try {
                _favoriteUiState.value = Result.Success(repository.getAllFavoriteUser())
                Log.d("tes", _favoriteUiState.value.toString())
            } catch (e:Exception) {
                _favoriteUiState.value = Result.Error(e.message.toString())
            }
        }
    }

}