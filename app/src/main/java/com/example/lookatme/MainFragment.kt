package com.example.lookatme

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lookatme.data.NoteEntity
import com.example.lookatme.databinding.MainFragmentBinding
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import kotlinx.android.synthetic.main.main_fragment.*

class MainFragment: Fragment(), NoteListAdapter.ListItemListener {

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: MainFragmentBinding   //need to check
    private lateinit var adapter: NoteListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED

        setHasOptionsMenu(true)

        requireActivity().title = "Look At Me"

        binding = MainFragmentBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.addDefaultSet()

        with(binding.recyclerView) {
            setHasFixedSize(true)
            val divider = DividerItemDecoration(context, LinearLayoutManager(context).orientation
            )
            addItemDecoration(divider)
        }

        viewModel.checkSize()

        viewModel.noteList?.observe(viewLifecycleOwner, Observer {

            adapter = NoteListAdapter(it, this@MainFragment)
            binding.recyclerView.adapter = adapter
            binding.recyclerView.layoutManager = LinearLayoutManager(activity)

            val selectedNotes = savedInstanceState?.getParcelableArrayList<NoteEntity>(SELECTED_NOTES_KEY)

            adapter.selectedNotes.addAll(selectedNotes ?: emptyList())
        })

        binding.floatingActionButton2.setOnClickListener {
            editNote(NEW_NOTE_ID)
        }

        val adRequest = viewModel.requestAd()
        binding.addViewBanner.loadAd(adRequest)

        return binding.root
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        val menuId =
                if(this::adapter.isInitialized && adapter.selectedNotes.isNotEmpty()) {
                R.menu.menu_delete }
                else {
                    R.menu.menu_main
                }

        inflater.inflate(menuId, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.action_delete -> deleteSelectedNotes()
            //R.id.action_about ->
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun deleteSelectedNotes(): Boolean {

        viewModel.deleteNotes(adapter.selectedNotes)
        Handler(Looper.getMainLooper()).postDelayed({
            adapter.selectedNotes.clear()
            requireActivity().invalidateOptionsMenu()
        }, 100)

        return true
    }

    override fun editNote(noteId: Int) {
        val action = MainFragmentDirections.actionToEditorFragment((noteId))
        findNavController().navigate(action)
    }

    override fun playNote(noteId: Int) {
        var action = MainFragmentDirections.actionToDisplayFragment((noteId))
        findNavController().navigate(action)
    }

    override fun onItemSelectionChanged() {
        requireActivity().invalidateOptionsMenu()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        if(this::adapter.isInitialized) {
            outState.putParcelableArrayList(SELECTED_NOTES_KEY, adapter.selectedNotes)
        }
        super.onSaveInstanceState(outState)
    }

}
