package com.example.lookatme

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.graphics.drawable.AnimationDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.VideoView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getColor
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.lookatme.databinding.DisplayFragmentBinding


class DisplayFragment: Fragment() {
    private lateinit var viewModel: DisplayViewModel

    private val args:DisplayFragmentArgs by navArgs()

    private lateinit var binding: DisplayFragmentBinding

    @SuppressLint("ResourceAsColor")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        requireActivity().window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        (activity as AppCompatActivity).supportActionBar?.let {
            it.hide()
        }

        setHasOptionsMenu(true)

        viewModel = ViewModelProvider(this).get(DisplayViewModel::class.java)

        binding = DisplayFragmentBinding.inflate(inflater, container, false)

        requireActivity().onBackPressedDispatcher.addCallback(
                viewLifecycleOwner,
                object : OnBackPressedCallback(true) {

                    override fun handleOnBackPressed() {
                        returnToMain()
                    }
                })

        viewModel.getNoteById(args.noteid)

        viewModel.currentNote.observe(viewLifecycleOwner, Observer {

            binding.displayText.setText(it.text)
            Log.i("fontTagT", it.fontStyle)

            var font_style = resources.getIdentifier(it.fontStyle,"font", activity?.packageName)

            Log.d("fontTag", font_style.toString())

            binding.displayText.typeface = ResourcesCompat.getFont(viewModel.getContext().applicationContext, font_style)

            Log.i("displayed_color", it.fontColor)

            var font_id = resources.getIdentifier(it.fontColor,"color", activity?.packageName)

            Log.i("displayed_color_id", font_id.toString())
            Log.i("display_color_cide", R.color.second_c.toString())
            binding.displayText?.setTextColor(getColor(viewModel.getContext().applicationContext, font_id))

            if(it.backType == 1) {
                val simple_id = resources.getIdentifier(it.backRes,"drawable", activity?.packageName)
                binding.displayBack?.setBackgroundResource(simple_id)
            }

            else if(it.backType == 2) {

                val grad_id = resources.getIdentifier(it.backRes,"drawable", activity?.packageName)
                binding.displayBack?.setBackgroundResource(grad_id)
                val animDrawable = binding.displayBack?.background as AnimationDrawable

                animDrawable.setEnterFadeDuration(10)
                animDrawable.setExitFadeDuration(5000)
                animDrawable.start()
            }

            else {

                val video_id = resources.getIdentifier(it.backRes,"raw", activity?.packageName)
                binding.displayBack?.setBackgroundResource(R.drawable.back_transparent)
                val videoView = binding.displayBackVideo as VideoView
                videoView.setOnPreparedListener { mp -> mp.isLooping = true }
                videoView!!.setVideoURI(Uri.parse("android.resource://" + activity?.packageName + "/" + video_id))
                videoView!!.requestFocus()
                videoView!!.start()
            }

            Handler(Looper.getMainLooper()).postDelayed({
                binding.displayText.isSelected = true
            }, 1000)
        })

        return binding.root
    }

    private fun returnToMain(): Boolean {
        findNavController().navigateUp()
        return true
    }

}