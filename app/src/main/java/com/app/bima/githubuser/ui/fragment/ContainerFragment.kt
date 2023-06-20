package com.app.bima.githubuser.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import com.app.bima.githubuser.R
import com.app.bima.githubuser.ui.adapter.PagerAdapter
import com.app.bima.githubuser.databinding.FragmentContainerBinding
import com.google.android.material.tabs.TabLayoutMediator



class ContainerFragment : Fragment() {
    private var _containerBinding: FragmentContainerBinding? = null
    private val containerBinding get() = _containerBinding!!

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _containerBinding = FragmentContainerBinding.inflate(inflater,container,false)
        val view = containerBinding.root
        val username = arguments?.getString("username_dari_activity")

        val pagerAdapter = PagerAdapter(this, username)
        containerBinding.viewPager.adapter = pagerAdapter
        TabLayoutMediator(containerBinding.tabs,containerBinding.viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        _containerBinding = null
    }

}