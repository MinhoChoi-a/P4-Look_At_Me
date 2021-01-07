package text.foryou

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
import text.foryou.databinding.DisplayFragmentBinding

class DisplayFragment: Fragment() {
    private lateinit var viewModel: DisplayViewModel

    private val args: DisplayFragmentArgs by navArgs()

    private lateinit var binding: DisplayFragmentBinding

    var touched:Boolean = false

    private var checkedSwitch: CompoundButton? = null

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

            var font_style = resources.getIdentifier(it.fontStyle,"font", activity?.packageName)

            binding.displayText.typeface = ResourcesCompat.getFont(viewModel.getContext().applicationContext, font_style)

            var font_id = resources.getIdentifier(it.fontColor,"color", activity?.packageName)

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

        binding.closeBtn?.setOnClickListener {
            returnToMain()
        }

        val checkedChangeListener = CompoundButton.OnCheckedChangeListener { compoundButton, isChecked ->

            if(isChecked) {
                AnimationUtils.loadAnimation(context, R.anim.blink).also {animation ->
                    binding.displayText.startAnimation(animation)
                }
                //
            }
            else {
                binding.displayText.clearAnimation()
            }
        }

        binding.blinkSwitch?.setOnCheckedChangeListener(checkedChangeListener)

        touchListener(binding.displayText)

        return binding.root
    }

    private fun returnToMain(): Boolean {
        findNavController().navigateUp()
        return true
    }

    private fun touchListener(view: View) {
        view.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent): Boolean {

                if(event.action == MotionEvent.ACTION_DOWN) {

                if(!touched) {
                    binding.closeBtn?.setVisibility(View.VISIBLE)
                    binding.blinkSwitch?.setVisibility(View.VISIBLE)

                    touched=true
                }

                else{
                    binding.closeBtn?.setVisibility(View.INVISIBLE)
                    binding.blinkSwitch?.setVisibility(View.INVISIBLE)

                    touched=false

                }
            }
                return true
            }
        })
    }
}