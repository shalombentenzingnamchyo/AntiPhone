package com.example.antiphone

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.antiphone.databinding.FragmentMainBinding
class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get()= _binding
    private var animationClock: Animation?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        activity?.setTitle("Challlenge")
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(false)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentMainBinding.inflate(inflater,container,false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        animationClock = AnimationUtils.loadAnimation(this.requireContext(),R.anim.clock_animation)
        binding?.imageView?.startAnimation(animationClock)
        animationClock = AnimationUtils.loadAnimation(this.requireContext(),R.anim.clock_animation_2)
        binding?.imageView2?.startAnimation(animationClock)
        binding?.imageView4?.startAnimation(animationClock)
        animationClock = AnimationUtils.loadAnimation(this.requireContext(),R.anim.clock_animation_3)
        binding?.imageView3?.startAnimation(animationClock)
        animationClock = AnimationUtils.loadAnimation(this.requireContext(),R.anim.clock_animation_3)
        binding?.imageView3?.startAnimation(animationClock)
        binding?.imageView5?.startAnimation(animationClock)
        animationClock = AnimationUtils.loadAnimation(this.requireContext(),R.anim.clock_animation_4)
        binding?.imageView6?.startAnimation(animationClock)

        animationClock = AnimationUtils.loadAnimation(this.requireContext(),R.anim.rotation_animation)
        binding?.clockPointer?.startAnimation(animationClock)

        binding?.nextButton?.setOnClickListener {
            val action=MainFragmentDirections.actionMainFragmentToChallengeFragment()
            binding?.root?.findNavController()?.navigate(action)
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding= null
    }


}