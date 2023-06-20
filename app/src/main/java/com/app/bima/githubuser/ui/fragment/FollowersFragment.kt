package com.app.bima.githubuser.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.bima.githubuser.data.remote.response.ItemsItem
import com.app.bima.githubuser.ui.adapter.UserCardAdapter
import com.app.bima.githubuser.databinding.FragmentFollowersBinding
import com.app.bima.githubuser.utility.Result
import com.app.bima.githubuser.di.viewmodel.FollowersViewModel
import com.app.bima.githubuser.di.viewmodel.FollowingViewModel
import com.app.bima.githubuser.di.viewmodel.ViewModelFactory

@Suppress("UNCHECKED_CAST")
class FollowersFragment : Fragment() {

    private var position:Int = 0
    private var username:String? = ""
    private var _followBinding:FragmentFollowersBinding? = null
    private val followBinding get() = _followBinding!!
    private val followersViewModel by viewModels<FollowersViewModel>(){
        ViewModelFactory.getInstance(requireActivity())
    }
    private val followingViewModel by viewModels<FollowingViewModel>(){
        ViewModelFactory.getInstance(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _followBinding = FragmentFollowersBinding.inflate(inflater,container,false)
        val view = followBinding.root
        val layoutManager = LinearLayoutManager(requireActivity())
        followBinding.rvFollow.layoutManager = layoutManager

        followersViewModel.uiStateFollowers.observe(viewLifecycleOwner) { uiState ->
            when(uiState) {
                is Result.Loading -> {
                    showLoading(true)
                }
                is Result.Success -> {
                    setFollowUser(uiState.data as List<ItemsItem>)
                    showLoading(false)
                }
                is Result.Error -> {
                    showLoading(false)
                }

                else -> {
                    showLoading(false)
                }
            }
        }

        followingViewModel.uiStateFollowing.observe(viewLifecycleOwner) { uiState ->
            when(uiState) {
                is Result.Loading -> {
                    showLoading(true)
                }
                is Result.Success -> {
                    setFollowUser(uiState.data as List<ItemsItem>)
                    showLoading(false)
                }
                is Result.Error -> {
                    showLoading(false)
                }

                else -> {
                    showLoading(false)
                }
            }
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            position = it.getInt(ARG_POSITION)
            username = it.getString(ARG_USERNAME)
        }
        when(position) {
            1 -> {
                if (followersViewModel.uiStateFollowers.value == null) {
                    followersViewModel.getFollowers(username.toString())
                }
            }
            2 -> {
                if (followingViewModel.uiStateFollowing.value == null) {
                    followingViewModel.getFollowing(username)
                }
            }
        }
    }


    private fun setFollowUser(UserList:List<ItemsItem>) {
        val listUser = ArrayList<ItemsItem>()
        for (i in UserList) {
            val user = ItemsItem(i.login,i.avatar_url)
            listUser.add(user)
        }
        val adapter = UserCardAdapter(listUser)
        followBinding.rvFollow.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        followBinding.followProgressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val ARG_POSITION = "position"
        const val ARG_USERNAME = "username"
    }

}