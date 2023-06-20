package com.app.bima.githubuser.ui.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.app.bima.githubuser.ui.fragment.ContainerFragment
import com.app.bima.githubuser.ui.fragment.FollowersFragment


class PagerAdapter(fragment: ContainerFragment, params: String? = ""):FragmentStateAdapter(fragment) {
    var username: String? = params

    override fun createFragment(position: Int): Fragment {
        val fragment = FollowersFragment()
       fragment.arguments = Bundle().apply {
           putInt(FollowersFragment.ARG_POSITION, position + 1)
           putString(FollowersFragment.ARG_USERNAME, username)
       }
        return fragment
    }
    override fun getItemCount(): Int {
        return 2
    }
}