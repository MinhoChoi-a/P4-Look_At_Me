package com.example.lookatme

import android.app.Activity
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
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
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.reward.RewardItem
import com.google.android.gms.ads.reward.RewardedVideoAd
import com.google.android.gms.ads.reward.RewardedVideoAdListener
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdCallback
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import kotlinx.android.synthetic.main.main_fragment.*

class MainFragment: Fragment(), NoteListAdapter.ListItemListener {

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: MainFragmentBinding   //need to check
    private lateinit var adapter: NoteListAdapter
    private lateinit var rewardAd: RewardedAd

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED

        (activity as AppCompatActivity).supportActionBar?.let {
            it.setHomeButtonEnabled(true)
            it.setDisplayShowHomeEnabled(true)
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(R.drawable.ic_check)
            it.hide()
        }

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

        val adRequest = viewModel.requestAd()
        binding.addViewBanner.loadAd(adRequest)

        createAndLoadRewardAd()

        binding.floatingActionButton2.setOnClickListener {
            addNote(NEW_NOTE_ID, rewardAd)
        }

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

    fun addNote(noteId: Int, ad: RewardedAd) {

        if(viewModel.checkSize() > 4) {

            if(ad.isLoaded) {
                var rewardEarned = false;
                val activityContext: Activity = (activity as AppCompatActivity)
                val adCallback = object: RewardedAdCallback() {
                    override fun onUserEarnedReward(p0: com.google.android.gms.ads.rewarded.RewardItem) {
                        rewardEarned = true;
                        val action = MainFragmentDirections.actionToEditorFragment((noteId))
                        findNavController().navigate(action)


                    }

                    override fun onRewardedAdClosed() {
                        super.onRewardedAdClosed()

                        if(!rewardEarned) {

                        var errMessage = "You should watch a video"

                        viewModel.setToast(errMessage)

                        createAndLoadRewardAd() }

                        return
                    }
                }
                ad.show(activityContext, adCallback)
            }

            else {
                Log.d("rewardAd", "The ad wasn't loaded yet")
            }

        }

        else {

        val action = MainFragmentDirections.actionToEditorFragment((noteId))
        findNavController().navigate(action) }
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

    fun createAndLoadRewardAd() {
        rewardAd = viewModel.loadRewardAd()

        val adLoadCallback = object: RewardedAdLoadCallback() {
            override fun onRewardedAdLoaded() {
                Log.i("adMob_check", "ad_is_loaded")
            }

            override fun onRewardedAdFailedToLoad(adError: LoadAdError) {
                Log.i("adMob_check", adError.message)
            }}

        rewardAd.loadAd(AdRequest.Builder().build(),adLoadCallback)
    }

}
