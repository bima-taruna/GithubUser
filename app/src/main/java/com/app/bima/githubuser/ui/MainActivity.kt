package com.app.bima.githubuser.ui

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.CompoundButton
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SwitchCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.bima.githubuser.R
import com.app.bima.githubuser.ui.adapter.UserCardAdapter
import com.app.bima.githubuser.data.remote.response.ItemsItem
import com.app.bima.githubuser.databinding.ActivityMainBinding
import com.app.bima.githubuser.utility.Result
import com.app.bima.githubuser.di.viewmodel.MainViewModel
import com.app.bima.githubuser.di.viewmodel.ViewModelFactory

@Suppress("UNCHECKED_CAST")
class MainActivity : AppCompatActivity() {
    private val mainViewModel by viewModels<MainViewModel>() {
        ViewModelFactory.getInstance(application)
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.myToolbar)
        val layoutManager = LinearLayoutManager(this)
        binding.rvUser.layoutManager = layoutManager

        mainViewModel.uiState.observe(this) { uiState ->
            when(uiState) {
                is Result.Loading -> {
                    showLoading(true)
                }
                is Result.Success -> {
                    setUserData(uiState.data as List<ItemsItem>)
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



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu?.findItem(R.id.search)?.actionView as SearchView
        val switchButton = menu.findItem(R.id.app_bar_switch)?.actionView as SwitchCompat
        val goToFavorite = menu.findItem(R.id.favorite)

        goToFavorite.setOnMenuItemClickListener {
            val favoriteIntent = Intent(this, FavoriteActivity::class.java)
            startActivity(favoriteIntent)
            true
        }

        mainViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                switchButton.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                switchButton.isChecked = false
            }

        }

        switchButton.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            mainViewModel.saveThemeSetting(isChecked)
        }

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                mainViewModel.getGithubUser(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        return true
    }


    private fun setUserData(UserList: List<ItemsItem>) {
        val listUser = ArrayList<ItemsItem>()
        for (i in UserList) {
            val user = ItemsItem(i.login,i.avatar_url)
            listUser.add(user)
        }
        val adapter = UserCardAdapter(listUser)
        binding.rvUser.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }


    override fun recreate() {
        finish()
        overridePendingTransition(
            androidx.appcompat.R.anim.abc_fade_in,
            androidx.appcompat.R.anim.abc_fade_out,
        )
        startActivity(intent)
    }
}


