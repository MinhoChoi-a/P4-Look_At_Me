package com.example.lookatme

import android.app.Activity
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lookatme.data.NoteEntity
import com.example.lookatme.databinding.EditorFragmentBinding


class EditorFragment: Fragment() {

    private lateinit var viewModel: EditorViewModel

    private val args:EditorFragmentArgs by navArgs()

    private lateinit var binding: EditorFragmentBinding

    private lateinit var adapter:BackgroundListAdapter

    private lateinit var fontColorAdapter:FontColorListAdapter
    private lateinit var fontStyleAdapter:FontStyleListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        (activity as AppCompatActivity).supportActionBar?.let {
            it.setHomeButtonEnabled(true)
            it.setDisplayShowHomeEnabled(true)
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(R.drawable.ic_check)
            it.show()
        }
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED

        setHasOptionsMenu(true)

        requireActivity().title =
                if(args.noteid == NEW_NOTE_ID) {
                    "New Note"
                }
                else {
                    "Edit Note"
                }

        viewModel = ViewModelProvider(this).get(EditorViewModel::class.java)

        binding = EditorFragmentBinding.inflate(inflater, container, false)
        binding.editor.setText("")

        requireActivity().onBackPressedDispatcher.addCallback(
                viewLifecycleOwner,
                object : OnBackPressedCallback(true) {

                    override fun handleOnBackPressed() {
                        saveAndReturn()
                    }
                })

        viewModel.setList?.observe(viewLifecycleOwner, Observer {
            adapter = BackgroundListAdapter(it, viewModel.currentNote)
            binding.recyclerViewBackStyle.adapter = adapter
            binding.recyclerViewBackStyle.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        })

        viewModel.fontList?.observe(viewLifecycleOwner, Observer {
            fontColorAdapter = FontColorListAdapter(it, viewModel.currentNote)
            binding.recyclerViewFontColor.adapter = fontColorAdapter
            binding.recyclerViewFontColor.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        })


        viewModel.fontStyleList?.observe(viewLifecycleOwner, Observer {
            fontStyleAdapter = FontStyleListAdapter(it, viewModel.currentNote)
            binding.recyclerViewFontStyle.adapter = fontStyleAdapter
            binding.recyclerViewFontStyle.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        })


        viewModel.currentNote.observe(viewLifecycleOwner, Observer {
            val savedString = savedInstanceState?.getString(NOTE_TEXT_KEY)
            val curusorPosition = savedInstanceState?.getInt(CURSOR_POSITION_KEY) ?: 0
            binding.editor.setText(savedString ?: it.text)
            binding.editor.setSelection(curusorPosition)
        })

        touchListener(binding.root)

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
            R.id.action_delete -> deleteAndReturn()
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun saveAndReturn(): Boolean {
        val imm = requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.editor.windowToken, 0)

        var selectedBack = adapter.getCheckedRB()
        var selectedFont = fontColorAdapter.getCheckedRBfont()
        var selectedFontStyle = fontStyleAdapter.getCheckedRBfontStyle()

        if(selectedBack == null || selectedFont == null || selectedFontStyle == null) {
            var errMessage = "You should select the option"
            viewModel.setToast(errMessage)
        }

        else {

        viewModel.currentNote.value?.text = binding.editor.text.toString()
        viewModel.currentNote.value?.fontColor = selectedFont?.getTag().toString()
        Log.i("updated_font_color", viewModel.currentNote.value?.fontColor.toString())

        viewModel.currentNote.value?.fontStyle = selectedFontStyle?.getTag().toString()
        viewModel.findSetAndAddToNote(Integer.parseInt(selectedBack?.getTag().toString()))

        Handler(Looper.getMainLooper()).postDelayed({

            Log.i("current_note_res", viewModel.currentNote.value?.backRes.toString())
            findNavController().navigateUp()

        }, 100) }

        return true
    }

    private fun deleteAndReturn(): Boolean {
        val imm = requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.root.windowToken, 0)

        val note = viewModel?.currentNote?.value?: NoteEntity()

        viewModel.deleteNote(note)

        findNavController().navigateUp()
        return true
    }


    override fun onSaveInstanceState(outState: Bundle) {
        with(binding.editor) {
            outState.putString(NOTE_TEXT_KEY, text.toString())
            outState.putInt(CURSOR_POSITION_KEY, selectionStart)
        }
        super.onSaveInstanceState(outState)
    }



    private fun touchListener(view: View) {
        view.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent): Boolean {

                if(binding.editor.isFocused) {
                    binding.editor.clearFocus()
                    val imm = requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(binding.root.windowToken, 0)
                }
                return true
            }
        })
    }

}