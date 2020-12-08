package com.example.lookatme

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lookatme.data.NoteEntity
import com.example.lookatme.databinding.IntroFragmentBinding
import com.example.lookatme.databinding.MainFragmentBinding

class IntroFragment: Fragment() {

    private lateinit var binding: IntroFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        (activity as AppCompatActivity).supportActionBar?.let {
            it.hide()
        }

        requireActivity().window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        setHasOptionsMenu(false)

        binding = IntroFragmentBinding.inflate(inflater, container, false)

        binding.startButton.setOnClickListener{
            val action:NavDirections = IntroFragmentDirections.actionToMainFragment()
            findNavController().navigate(action)
        }

        return binding.root
    }
}