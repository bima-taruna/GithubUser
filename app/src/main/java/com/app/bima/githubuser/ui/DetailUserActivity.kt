package com.app.bima.githubuser.ui

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.fragment.app.commit
import com.app.bima.githubuser.R
import com.app.bima.githubuser.data.remote.response.UserDetailResponse
import com.app.bima.githubuser.databinding.ActivityDetailUserBinding
import com.app.bima.githubuser.ui.fragment.ContainerFragment
import com.app.bima.githubuser.utility.Result
import com.app.bima.githubuser.utility.loadImage
import com.app.bima.githubuser.di.viewmodel.DetailViewModel
import com.app.bima.githubuser.di.viewmodel.ViewModelFactory

class DetailUserActivity : AppCompatActivity() {
    private val detailViewModel by viewModels<DetailViewModel>(){
        ViewModelFactory.getInstance(application)
    }
    private lateinit var detailUserBinding: ActivityDetailUserBinding
    private var favoriteStatus: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detailUserBinding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(detailUserBinding.root)

        window.statusBarColor = ContextCompat.getColor(this, R.color.black_900)

        layoutListener()

        detailViewModel.getFavoriteUserByUsername(intent?.getStringExtra(USERNAME)
            .toString()).observe(this) { favUser ->
            if (favUser != null) {
                detailUserBinding.floatingActionButton.setImageResource(R.drawable.ic_favorite)
                favoriteStatus = true
            } else {
                detailUserBinding.floatingActionButton.setImageResource(R.drawable.ic_unfavorite)
                favoriteStatus = false
            }
            }

        detailUserBinding.floatingActionButton.setOnClickListener {
            if (favoriteStatus){
                detailViewModel.deleteFavoriteUser()

            } else {
                detailViewModel.addFavoriteUser()
            }
        }

        val username = intent.getStringExtra(USERNAME)
        val bundle = Bundle()
        bundle.putString("username_dari_activity", username)

        detailViewModel.detailGithubUser.observe(this) { detail ->
            setDetailUser(detail)
        }

        if (detailViewModel.detailGithubUser.value == null) {
            detailViewModel.getUserDetail(username)
        }

        detailViewModel.uiStateDetail.observe(this) { uiStateDetail ->
            when(uiStateDetail) {
                is Result.Loading -> {
                    showLoading(true)
                }
                is Result.Success -> {
                    setDetailUser(uiStateDetail.data)
                    showLoading(false)
                }
                is Result.Error -> {
                    Toast.makeText(this, uiStateDetail.error, Toast.LENGTH_SHORT).show()
                    showLoading(false)
                }

                else -> {
                    showLoading(false)
                }
            }

        }

        val fragmentManager = supportFragmentManager
        val fragmentContainer = ContainerFragment()
        fragmentContainer.arguments = bundle
        val fragment = fragmentManager.findFragmentByTag(ContainerFragment::class.java.simpleName)
        if (fragment !is ContainerFragment) {
            fragmentManager.commit {
                add(R.id.fr_detail, fragmentContainer, ContainerFragment::class.java.simpleName)
            }
        }
    }

    private fun setDetailUser(detail: UserDetailResponse) {
        detailUserBinding.ivProfilePicture.loadImage(detail.avatarUrl)
        detailUserBinding.tvUsername.text = detail.login
        detailUserBinding.tvName.text = if (detail.name != null) detail.name.toString() else ""
        detailUserBinding.tvFollowers.text = detail.followers.toString()
        detailUserBinding.tvFollowing.text = detail.following.toString()

    }

    private fun showLoading(isLoading: Boolean) {
        detailUserBinding.detailProgressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun layoutListener() {
        detailUserBinding.svDetail.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            @SuppressLint("ClickableViewAccessibility")
            override fun onGlobalLayout() {
                val orientation = resources.configuration.orientation
                if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                    detailUserBinding.svDetail.setOnTouchListener { _, _ -> true }
                } else {
                    detailUserBinding.svDetail.setOnTouchListener { _, _ -> false }
                }
                detailUserBinding.svDetail.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })
    }

    companion object {
        const val USERNAME = "username"
    }

}