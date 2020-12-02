package com.example.lookatme

import androidx.fragment.app.Fragment
import android.app.Activity
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.lookatme.data.NoteEntity
import com.example.lookatme.databinding.DisplayFragmentBinding

class DisplayFragment: Fragment() {
    private lateinit var viewModel: DisplayViewModel

    private val args:DisplayFragmentArgs by navArgs()

    private lateinit var binding: DisplayFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        (activity as AppCompatActivity).supportActionBar?.let {
            it.setHomeButtonEnabled(true)
            it.setDisplayShowHomeEnabled(true)
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(R.drawable.ic_check)
        }

        (activity as AppCompatActivity).supportActionBar?.hide()

        requireActivity().window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        setHasOptionsMenu(true)

        viewModel = ViewModelProvider(this).get(DisplayViewModel::class.java)

        binding = DisplayFragmentBinding.inflate(inflater, container, false)

        requireActivity().onBackPressedDispatcher.addCallback(
                viewLifecycleOwner,
                object: OnBackPressedCallback(true) {

                    override fun handleOnBackPressed() {
                        saveAndReturn()
                    }
                })

        viewModel.currentNote.observe(viewLifecycleOwner, Observer {
            binding.displayText.setText(it.text)
            binding.displayText.isSelected = true
        })

        viewModel.getNoteById(args.noteid)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        val menuId = R.menu.menu_delete
        inflater.inflate(menuId, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            android.R.id.home -> saveAndReturn()
            //R.id.action_delete -> deleteAndReturn()
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun saveAndReturn(): Boolean {
        val imm = requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.root.windowToken, 0)

        findNavController().navigateUp()
        return true
    }


    override fun onSaveInstanceState(outState: Bundle) {
        with(binding.displayText) {
            outState.putString(NOTE_TEXT_KEY, text.toString())
        }
        super.onSaveInstanceState(outState)
    }

}