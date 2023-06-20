package com.app.bima.githubuser.di.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.bima.githubuser.data.local.entity.GithubUserEntity
import com.app.bima.githubuser.data.remote.response.UserDetailResponse
import com.app.bima.githubuser.data.UserRepository
import com.app.bima.githubuser.utility.Result
import kotlinx.coroutines.launch

class DetailViewModel(private val repository: UserRepository): ViewModel() {

    private val _detailGithubUser = MutableLiveData<UserDetailResponse>()
    val detailGithubUser:LiveData<UserDetailResponse> = _detailGithubUser

    private val _uiStateDetail = MutableLiveData<Result<UserDetailResponse>>()
    val uiStateDetail: LiveData<Result<UserDetailResponse>> = _uiStateDetail


    fun getUserDetail(username: String?) {
        _uiStateDetail.value = Result.Loading
        viewModelScope.launch {
            try {
                val response = repository.getDetailUsers(username)
                _uiStateDetail.value = Result.Success(response)
                _detailGithubUser.value = response

            }   catch (e:Exception) {
                _uiStateDetail.value = Result.Error(e.message.toString())
            }
        }
    }

    fun addFavoriteUser() {
        viewModelScope.launch {
            val user = GithubUserEntity(
                username = _detailGithubUser.value?.login.toString(),
                avatarUrl = _detailGithubUser.value?.avatarUrl
            )
            repository.insertFavorite(user)
        }
    }

    fun getFavoriteUserByUsername(username: String): LiveData<GithubUserEntity> {
        return repository.getFavoriteUser(username)
    }

    fun deleteFavoriteUser() {
        viewModelScope.launch {
            val user = GithubUserEntity(
                username = _detailGithubUser.value?.login.toString(),
                avatarUrl = _detailGithubUser.value?.avatarUrl
            )
            repository.deleteFavorite(user)
        }
    }



}