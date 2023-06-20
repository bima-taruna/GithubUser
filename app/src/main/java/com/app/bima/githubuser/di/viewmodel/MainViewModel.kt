package com.app.bima.githubuser.di.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.app.bima.githubuser.data.remote.response.ItemsItem
import com.app.bima.githubuser.data.UserRepository
import com.app.bima.githubuser.utility.Result
import kotlinx.coroutines.launch

class MainViewModel(private val repository: UserRepository): ViewModel() {

    private val _uiState = MutableLiveData<Result<List<ItemsItem?>>>()
    val uiState: LiveData<Result<List<ItemsItem?>>> = _uiState


    companion object{
        private const val USERNAME = "a"
    }

    init {
        getGithubUser()
    }

     fun getGithubUser(query:String = USERNAME) {
        _uiState.value = Result.Loading
         viewModelScope.launch {
             try {
                 _uiState.value = Result.Success(repository.getListUsers(query).items)
             } catch (e:Exception) {
                 _uiState.value = Result.Error(e.message.toString())
             }
         }
    }

    fun getThemeSettings(): LiveData<Boolean> {
        return repository.getThemeSettings().asLiveData()
    }

    fun saveThemeSetting(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            repository.saveThemeSetting(isDarkModeActive)
        }
    }

}