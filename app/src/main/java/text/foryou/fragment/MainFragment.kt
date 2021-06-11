package text.foryou.fragment

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import text.foryou.NEW_NOTE_ID
import text.foryou.adapter.NoteListAdapter
import text.foryou.databinding.MainFragmentBinding
import text.foryou.viewmodel.MainViewModel

//Fragment: a piece of UI that can be placed in an Activity(defined in nav_graph.xml)
//Adapter: extends RecyclerView which can help to display large set of data
class MainFragment: Fragment(), NoteListAdapter.ListItemListener {

    private lateinit var viewModel: MainViewModel

    //generates XML layout file as a Class
    private lateinit var binding: MainFragmentBinding

    private lateinit var adapter: NoteListAdapter

    //instantiate View
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle? //if not null, this fragment is being re-constructed from a previous state
    ): View?
        //return View
        {
            //fix the orientation to prevent the activity enters a new state
            activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED

            //let: invoke one or more functions on results of all chains
            (activity as AppCompatActivity).supportActionBar?.let {
            //hide action bar
            it.hide()
        }

        //setHasOptionsMenu(true)

        //requireActivity().title = "Text For You"

            //initialize view layout from the xml file
        binding = MainFragmentBinding.inflate(inflater, container, false)

            //initialize ViewModel
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

            //request Ad
        val adRequest = viewModel.requestAd()
        binding.addViewBanner.loadAd(adRequest)

            //with (object), do the following commands => initialize set for the recyclerview
        with(binding.recyclerView) {
            setHasFixedSize(true)
            val divider = DividerItemDecoration(context, LinearLayoutManager(context).orientation
            )
            addItemDecoration(divider)
        }

            //set observer for each item of recyclerView
        viewModel.noteList?.observe(viewLifecycleOwner, Observer {

            //it <= List<NoteEntity> noteList
            adapter = NoteListAdapter(it, this@MainFragment)

            binding.recyclerView.adapter = adapter
            binding.recyclerView.layoutManager = LinearLayoutManager(activity)
        })

            //set Onclick Listener for the floating button
        binding.floatingActionButton2.setOnClickListener {
            addNote(NEW_NOTE_ID)
        }

        return binding.root
    }

    //override editNote method of ListItemListener(Adapter: RecyclerView)
    //navigate to edit menu
    override fun editNote(noteId: Int) {
        val action = MainFragmentDirections.actionToEditorFragment((noteId))
        findNavController().navigate(action)
    }

    //override playNote method of ListItemListener(Adapter: RecyclerView)
    //navigate to play menu
    override fun playNote(noteId: Int) {
        var action = MainFragmentDirections.actionToDisplayFragment((noteId))
        findNavController().navigate(action)
    }

    //navigate to edit menu
    fun addNote(noteId: Int) {
        val action = MainFragmentDirections.actionToEditorFragment((noteId))
        findNavController().navigate(action)
    }
}