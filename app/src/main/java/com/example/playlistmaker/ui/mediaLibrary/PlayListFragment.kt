package com.example.playlistmaker.ui.mediaLibrary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentPlaylistBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayListFragment : Fragment() {
    companion object {
        fun newInstance() = PlayListFragment()
    }

    private var _binding: FragmentPlaylistBinding? = null
    private val binding get() = _binding!!
    private val plViewModel by viewModel<PlayListViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

}