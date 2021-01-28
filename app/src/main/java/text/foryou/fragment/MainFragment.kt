package text.foryou.fragment

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
import text.foryou.NEW_NOTE_ID
import text.foryou.R
import text.foryou.SELECTED_NOTES_KEY
import text.foryou.adapter.NoteListAdapter
import text.foryou.databinding.MainFragmentBinding
import text.foryou.viewmodel.MainViewModel


class MainFragment: Fragment(), NoteListAdapter.ListItemListener {

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: MainFragmentBinding   //need to check
    private lateinit var adapter: NoteListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED

        (activity as AppCompatActivity).supportActionBar?.let {
            //it.setHomeButtonEnabled(true)
            //it.setDisplayShowHomeEnabled(true)
            //it.setDisplayHomeAsUpEnabled(true)
            //it.setHomeAsUpIndicator(R.drawable.ic_check)
            it.hide()
        }

        setHasOptionsMenu(true)

        requireActivity().title = "Text For You"

        binding = MainFragmentBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        val adRequest = viewModel.requestAd()
        binding.addViewBanner.loadAd(adRequest)

        with(binding.recyclerView) {
            setHasFixedSize(true)
            val divider = DividerItemDecoration(context, LinearLayoutManager(context).orientation
            )
            addItemDecoration(divider)
        }

        viewModel.noteList?.observe(viewLifecycleOwner, Observer {

            adapter = NoteListAdapter(it, this@MainFragment)
            binding.recyclerView.adapter = adapter
            binding.recyclerView.layoutManager = LinearLayoutManager(activity)

            //val selectedNotes = savedInstanceState?.getParcelableArrayList<NoteEntity>(SELECTED_NOTES_KEY)
            //adapter.selectedNotes.addAll(selectedNotes ?: emptyList())
        })


        binding.floatingActionButton2.setOnClickListener {
            addNote(NEW_NOTE_ID)
        }



        return binding.root
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        val menuId =
                if(this::adapter.isInitialized && adapter.selectedNotes.isNotEmpty()) {
                    R.menu.menu_delete
                }
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

    fun addNote(noteId: Int) {
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

/**
 *  alert dialog example
 *
 *  private fun showAdAlertDialog(noteId:Int, ad:RewardedAd, intAd: InterstitialAd) {

val alertDialog: AlertDialog.Builder = AlertDialog.Builder(this.context, R.style.AlertDialogCustom)
alertDialog.setMessage("Watch the trailer? then you can add more :)")

if(ad.isLoaded) {
alertDialog.setPositiveButton(
"Yes Sounds good") { _, _ -> showRewardAd(noteId, ad) }
}

else if(intAd.isLoaded) {
alertDialog.setPositiveButton(
"Yes Sounds good") { _, _ ->  intAdShow(noteId, intAd)}
}

alertDialog.setNegativeButton(
"No") {_,_ -> }
val alert: AlertDialog = alertDialog.create()
alert.setCanceledOnTouchOutside(false)

alert.show()

val nButton = alert.getButton(DialogInterface.BUTTON_NEGATIVE)
val pButton = alert.getButton(DialogInterface.BUTTON_POSITIVE)

nButton.setTextColor(getColor(viewModel.getContext(), R.color.trans))
pButton.setTextColor(getColor(viewModel.getContext(), R.color.trans))

}
 */

