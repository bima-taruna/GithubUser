package com.app.bima.githubuser.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.bima.githubuser.ui.adapter.UserCardAdapter
import com.app.bima.githubuser.data.local.entity.GithubUserEntity
import com.app.bima.githubuser.data.remote.response.ItemsItem
import com.app.bima.githubuser.databinding.ActivityFavoriteBinding
import com.app.bima.githubuser.utility.Result
import com.app.bima.githubuser.di.viewmodel.FavoriteViewModel
import com.app.bima.githubuser.di.viewmodel.ViewModelFactory

class FavoriteActivity : AppCompatActivity() {
    private val favoriteViewModel by viewModels<FavoriteViewModel>() {
        ViewModelFactory.getInstance(application)
    }
    private  lateinit var favoriteBinding: ActivityFavoriteBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        favoriteBinding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(favoriteBinding.root)
        val layoutManager = LinearLayoutManager(this)
        favoriteBinding.rvFavorite.layoutManager = layoutManager


        favoriteViewModel.favoriteUiState.observe(this) { uiState ->
            Log.d("check", uiState.toString())
            when(uiState) {
                is Result.Loading -> {
                    showLoading(true)
                }
                is Result.Success -> {
                    setFavoriteUsers(uiState.data)
                    showLoading(false)
                }
                is Result.Error -> {
                    Toast.makeText(this, uiState.error, Toast.LENGTH_SHORT).show()
                    showLoading(false)
                }

                else -> {
                    showLoading(false)
                }
            }
        }

    }

    private fun setFavoriteUsers(listUser:LiveData<List<GithubUserEntity>>) {
        listUser.observe(this) { user ->
            val items = arrayListOf<ItemsItem>()
            user.map {
                val item = ItemsItem(
                    login = it.username,
                    avatar_url = it.avatarUrl.toString()
                )
                items.add(item)
            }
            val adapter = UserCardAdapter(items)
            favoriteBinding.rvFavorite.adapter = adapter
        }
    }


    private fun showLoading(isLoading: Boolean) {
        favoriteBinding.favoriteProgressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}