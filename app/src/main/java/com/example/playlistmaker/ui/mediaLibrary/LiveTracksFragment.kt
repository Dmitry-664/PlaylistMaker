package com.example.playlistmaker.ui.mediaLibrary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentLiveTracksBinding

class LiveTracksFragment : Fragment() {
    companion object {
        fun newInstance() = LiveTracksFragment()
    }

    private var _binding: FragmentLiveTracksBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLiveTracksBinding.inflate(inflater, container, false)
        return binding.root
    }

}