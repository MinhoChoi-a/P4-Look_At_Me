package text.foryou.fragment

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.graphics.drawable.AnimationDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import android.view.animation.AnimationUtils
import android.widget.CompoundButton
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
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.LoadAdError
import text.foryou.R
import text.foryou.databinding.DisplayFragmentBinding
import text.foryou.viewmodel.DisplayViewModel

//Fragment: a piece of UI that can be placed in an Activity(defined in nav_graph.xml)
class DisplayFragment: Fragment() {

    private lateinit var viewModel: DisplayViewModel

    //can access to the argument of the fragment easier
    private val args: DisplayFragmentArgs by navArgs()

    private lateinit var binding: DisplayFragmentBinding
    var touched: Boolean = false

    //interstitial Ad
    private lateinit var mInterAd: InterstitialAd

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        //set window as a full screen
        requireActivity().window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        //setting for the action bar: hide
        //let: invoke one or more functions on results of all chains
        (activity as AppCompatActivity).supportActionBar?.let {
            it.hide()
        }

        //setHasOptionsMenu(true)

        //initialize ViewModel
        viewModel = ViewModelProvider(this).get(DisplayViewModel::class.java)

        //initialize view layout from the xml file
        binding = DisplayFragmentBinding.inflate(inflater, container, false)

        //load ad
        createAndLoadInterAd()

        //listener for the back button
        requireActivity().onBackPressedDispatcher.addCallback(
                viewLifecycleOwner,
                object : OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {
                        returnToMain(mInterAd)
                    }
                })

        viewModel.getNoteById(args.noteid)

        //set data with the selected note entity
        viewModel.currentNote.observe(viewLifecycleOwner, Observer {

            //set text
            binding.displayText.setText(it.text)

            //find font style
            var font_style = resources.getIdentifier(it.fontStyle, "font", activity?.packageName)

            //set font style
            binding.displayText.typeface = ResourcesCompat.getFont(viewModel.getContext().applicationContext, font_style)

            //find font color
            var font_id = resources.getIdentifier(it.fontColor, "color", activity?.packageName)

            //set font color
            binding.displayText?.setTextColor(getColor(viewModel.getContext().applicationContext, font_id))

            //There are 3 types of background: simple drawable resource, animated drawable resource, video resource
            if (it.backType == 1) {
                val simple_id = resources.getIdentifier(it.backRes, "drawable", activity?.packageName)
                binding.displayBack?.setBackgroundResource(simple_id)
            }

            else if (it.backType == 2) {
                val grad_id = resources.getIdentifier(it.backRes, "drawable", activity?.packageName)
                binding.displayBack?.setBackgroundResource(grad_id)
                val animDrawable = binding.displayBack?.background as AnimationDrawable

                animDrawable.setEnterFadeDuration(10)
                animDrawable.setExitFadeDuration(5000)
                animDrawable.start()
            }

            else {
                val video_id = resources.getIdentifier(it.backRes, "raw", activity?.packageName)
                binding.displayBack?.setBackgroundResource(R.drawable.back_transparent)
                val videoView = binding.displayBackVideo as VideoView
                videoView.setOnPreparedListener { mp -> mp.isLooping = true }
                videoView!!.setVideoURI(Uri.parse("android.resource://" + activity?.packageName + "/" + video_id))
                videoView!!.requestFocus()
                videoView!!.start()
            }
        })

        //set switch button for the blink effect
        val checkedChangeListener = CompoundButton.OnCheckedChangeListener {
                compoundButton,
                isChecked ->

            //also: unlike let, it returns the original data
            if (isChecked) {
                AnimationUtils.loadAnimation(context, R.anim.blink).also {
                        animation ->
                    binding.displayText.startAnimation(animation)
                }
            } else {
                binding.displayText.clearAnimation()
            }
        }

        //apply blink switch to the fragment
        binding.blinkSwitch?.setOnCheckedChangeListener(checkedChangeListener)

        //move to the main menu with interstitial Ad
        binding.closeBtn?.setOnClickListener {
            returnToMain(mInterAd)
        }

        //listener for the touch event
        touchListener(binding.displayText)

        return binding.root
    }

    //return to Main menu with the interstitial Ad
    private fun returnToMain(intAd: InterstitialAd): Boolean {
        intAd.show()
        findNavController().navigateUp()
        return true
    }

    //screen touch listener
    private fun touchListener(view: View) {

        view.setOnTouchListener(object : View.OnTouchListener {

            override fun onTouch(v: View?, event: MotionEvent): Boolean {

                if (event.action == MotionEvent.ACTION_DOWN) {

                    if (!touched) {
                        binding.closeBtn?.setVisibility(View.VISIBLE)
                        binding.blinkSwitch?.setVisibility(View.VISIBLE)

                        touched = true

                    } else {
                        binding.closeBtn?.setVisibility(View.INVISIBLE)
                        binding.blinkSwitch?.setVisibility(View.INVISIBLE)

                        touched = false
                    }
                }
                return true
            }
        })
    }

    //load interstitial Ad
    private fun createAndLoadInterAd() {
        mInterAd = viewModel.loadInterAd()
        mInterAd.adUnitId = "ca-app-pub-3940256099942544/1033173712"
        mInterAd.loadAd(AdRequest.Builder().build())
    }
}