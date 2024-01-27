package com.example.playlistmaker.ui.mediaLibrary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentMediaLibraryBinding
import com.google.android.material.tabs.TabLayoutMediator


class MediaLibraryFragment: Fragment() {

    private var binding: FragmentMediaLibraryBinding? = null
    private var tabMediator: TabLayoutMediator? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMediaLibraryBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding!!.viewPager.adapter = LibraryViewPagerAdapter(childFragmentManager, lifecycle)
        tabMediator = TabLayoutMediator(binding!!.tabLayout, binding!!.viewPager) { tab, position ->
            when(position) {
                0-> tab.text = resources.getString(R.string.live_tracks)
                else -> tab.text = resources.getString((R.string.playlist))
            }
        }
        tabMediator!!.attach()

    }
    override fun onDestroy() {
        super.onDestroy()
        tabMediator?.detach()
    }
}

