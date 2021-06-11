package text.foryou.fragment

import android.app.Activity
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import text.foryou.*
import text.foryou.adapter.BackgroundListAdapter
import text.foryou.adapter.FontColorListAdapter
import text.foryou.adapter.FontStyleListAdapter
import text.foryou.data.model.NoteEntity
import text.foryou.databinding.EditorFragmentBinding
import text.foryou.viewmodel.EditorViewModel

//Fragment: a piece of UI that can be placed in an Activity(defined in nav_graph.xml)
class EditorFragment: Fragment() {

    private lateinit var viewModel: EditorViewModel

    //can access to the argument of the fragment easier
    private val args: EditorFragmentArgs by navArgs()

    private lateinit var binding: EditorFragmentBinding
    private lateinit var adapter: BackgroundListAdapter
    private lateinit var fontColorAdapter: FontColorListAdapter
    private lateinit var fontStyleAdapter: FontStyleListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //setting for the action bar
        //let: invoke one or more functions on results of all chains
        (activity as AppCompatActivity).supportActionBar?.let {
            it.setHomeButtonEnabled(true)
            it.setDisplayShowHomeEnabled(true)
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(R.drawable.ic_check)
            it.show()
        }

        //fix the orientation to prevent the activity enters a new state
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        //set the option menu
        setHasOptionsMenu(true)

        //set the title of this activity
        requireActivity().title =
                if(args.noteid == NEW_NOTE_ID) {
                    "New Text Board"
                }
                else {
                    "Edit Text Board"
                }

        //initialize ViewModel
        viewModel = ViewModelProvider(this).get(EditorViewModel::class.java)

        viewModel.getNoteById(args.noteid)

        //initialize view layout from the xml file
        binding = EditorFragmentBinding.inflate(inflater, container, false)

        binding.editor.setText("")

        //request Ad
        val adRequest = viewModel.requestAd()
        binding.addViewBanner.loadAd(adRequest)

        //listener for the back button
        requireActivity().onBackPressedDispatcher.addCallback(
                viewLifecycleOwner,
                object : OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {
                        findNavController().navigateUp()
                    }
                })

        //set observer for the background item list
        viewModel.setList?.observe(viewLifecycleOwner, Observer {
            adapter = BackgroundListAdapter(it, viewModel.currentNote)
            binding.recyclerViewBackStyle.adapter = adapter
            binding.recyclerViewBackStyle.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

            viewModel.getSelectedBackPosition()
        })

        //feature to focus on the selected option for editing
        viewModel.selectedBackground?.observe(viewLifecycleOwner, Observer {
            binding.recyclerViewBackStyle.layoutManager?.scrollToPosition(viewModel.returnBackPosition()-1)
        })

        //set observer for the font color item list
        viewModel.fontList?.observe(viewLifecycleOwner, Observer {
            fontColorAdapter = FontColorListAdapter(it, viewModel.currentNote)
            binding.recyclerViewFontColor.adapter = fontColorAdapter
            binding.recyclerViewFontColor.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

            viewModel.getSelectedFontColorPosition()
        })

        //feature to focus on the selected option for editing
        viewModel.selectedFontColor?.observe(viewLifecycleOwner, Observer {
            binding.recyclerViewFontColor.layoutManager?.scrollToPosition(viewModel.returnFontColorPosition()-1)
        })

        //feature to focus on the selected option for editing
        viewModel.fontStyleList?.observe(viewLifecycleOwner, Observer {
            fontStyleAdapter = FontStyleListAdapter(it, viewModel.currentNote)
            binding.recyclerViewFontStyle.adapter = fontStyleAdapter
            binding.recyclerViewFontStyle.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

            viewModel.getSelectedFontStylePosition()
        })

        //feature to focus on the selected option for editing
        viewModel.selectedFontStyle?.observe(viewLifecycleOwner, Observer {
            binding.recyclerViewFontStyle.layoutManager?.scrollToPosition(viewModel.returnFontStylePosition()-1)
        })

        //set observer for the note data, in case of the state of the activity was changed and create again
        viewModel.currentNote.observe(viewLifecycleOwner, Observer {
            val savedString = savedInstanceState?.getString(NOTE_TEXT_KEY)
            val curusorPosition = savedInstanceState?.getInt(CURSOR_POSITION_KEY) ?: 0
            binding.editor.setText(savedString ?: it.text)
            binding.editor.setSelection(curusorPosition)
        })

        //to close the keyboard after completed to fill the text
        touchListener(binding.root)

        return binding.root
    }

    //create option menu
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        val menuId = R.menu.menu_delete
        inflater.inflate(menuId, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    //method for selected option on option menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            android.R.id.home -> saveAndReturn()
            R.id.action_delete -> deleteAndReturn()
            else -> super.onOptionsItemSelected(item)
        }
    }

    //save note and back to the main menu
    private fun saveAndReturn(): Boolean {

        //hide keyboard
        val imm = requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.editor.windowToken, 0)

        //get data of selected option
        var selectedBack = adapter.getCheckedRB()
        var selectedFont = fontColorAdapter.getCheckedRBfont()
        var selectedFontStyle = fontStyleAdapter.getCheckedRBfontStyle()

        if(selectedBack == null || selectedFont == null || selectedFontStyle == null) {
            var errMessage = "You should select the option"
            viewModel.setToast(errMessage)
        }

        else {
            //update the data of note(MutableLiveData)
            viewModel.currentNote.value?.text = binding.editor.text.toString()
            viewModel.currentNote.value?.fontColor = selectedFont?.getTag().toString()
            viewModel.currentNote.value?.fontStyle = selectedFontStyle?.getTag().toString()
            //update database
            viewModel.findSetAndAddToNote(Integer.parseInt(selectedBack?.getTag().toString()))

        Handler(Looper.getMainLooper()).postDelayed({
            findNavController().navigateUp()
            //added delay time to give a time to 'save note' process.
        }, 100) }

        return true
    }

    //delete note and back to main menu
    private fun deleteAndReturn(): Boolean {

        val imm = requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.root.windowToken, 0)

        val note = viewModel?.currentNote?.value?: NoteEntity()

        viewModel.deleteNote(note)

        findNavController().navigateUp()
        return true
    }

    //for now this method will be utilized, however I added it for later
    //With this method, we can store the input data and use it when the activity is recreated
    override fun onSaveInstanceState(outState: Bundle) {
        with(binding.editor) {
            outState.putString(NOTE_TEXT_KEY, text.toString())
            outState.putInt(CURSOR_POSITION_KEY, selectionStart)
        }
        super.onSaveInstanceState(outState)
    }

    //to remove the keyboard from the screen
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