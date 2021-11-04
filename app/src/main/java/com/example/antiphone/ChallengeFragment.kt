package com.example.antiphone

import android.content.DialogInterface
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.example.antiphone.databinding.FragmentChallengeBinding
import com.google.android.material.textfield.TextInputLayout
import java.util.concurrent.Future

class ChallengeFragment : Fragment() {
    private val viewModel: ChallengeViewModel by viewModels()
    private lateinit var _binding: FragmentChallengeBinding
    private val binding get()= _binding!!
    private var hrsNum: Int = 0
    private var minsNum: Int = 0
    private var totalTime: Int= 0
    var handler= Handler()
    var runnable= Runnable{ completionChecker()}
    private var animationClock: Animation?= null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding= DataBindingUtil.inflate(inflater,R.layout.fragment_challenge,container,false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.challengeViewModel = viewModel
        animationClock = AnimationUtils.loadAnimation(this.requireContext(),R.anim.rotation_animation_minute)
        viewModel.hrs.observe(viewLifecycleOwner,{
            newData->
            binding.timeHrs.text="%02d".format(newData)
        })
        viewModel.mins.observe(viewLifecycleOwner,{
            newData->
            binding.timeMins.text="%02d".format(newData)

        })
        viewModel.degrees.observe(viewLifecycleOwner,{
            newData->
            binding.clockPointerSmall.rotation=newData.toFloat()
        })

        if(viewModel.onPlay== false){
            showInputDialog()
        }else{
            //may be due to configuration change
                viewModel.startCountDown()
                Runnable{completionChecker()}.run()
                showStop()
                binding.clockPointer.startAnimation(animationClock)

        }
        binding.startButton.setOnClickListener {
            if(viewModel.onPlay){
                //when playing stop is pressed
                viewModel.pauseCount()//stop should stop the counting
                viewModel.onPlay=false
                showStart()
            }else{
                showInputDialog()
            }

        }
    }
    fun initializeTime(){ viewModel.initialize(hrsNum,minsNum) }
    fun startCountDown(){ viewModel.startCountDown() }
    fun showStart(){ binding.startButton.setAlpha(1F)//start should be shown
        binding.stopButton.setAlpha(0F) }
    fun showStop(){ binding.startButton.setAlpha(0F)//stop be shown
        binding.stopButton.setAlpha(1F) }
    fun showInputDialog(){
        val alertDialog: AlertDialog? = activity?.let {
            //this builds and inflates the dialog
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            val view = inflater.inflate(R.layout.dialog_input_picker,null)
            //this is for referencing input
            val editTextHrs = view.findViewById<EditText>(R.id.hrs_input)
            val editTextMin = view.findViewById<EditText>(R.id.min_input)
            //the content of the Alert Dialog is mentioned here
            builder?.setMessage("Enter the time you want to stop using your phone. Please enter less than 3 hrs. Don't forget to respond to important notifictions.")
                    .setTitle("Challenge: Stop using phone")
                    .setView(view)
                    .setPositiveButton("OK",null)//listener will be later set
            //the positive button should not work if teh values are not correct
            builder.create().apply{
                setOnShowListener{
                    getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener{
                        var dismiss= true
                        if(editTextHrs.text?.toString()!=""){
                            hrsNum = editTextHrs.text?.toString()?.toInt() ?: 0
                            val til: TextInputLayout = view.findViewById(R.id.hrs_input_layout)
                            if (hrsNum > 2) {
                                til.setError("Choose less than 2 hrs")
                                dismiss = false
                            } else if (hrsNum < 0) {
                                til.setError("Choose 0 hrs or more")
                                dismiss = false
                            } else {
                                til.isErrorEnabled = false
                            }
                        }else{
                            hrsNum=0
                        }
                        if(editTextMin.text?.toString()!=""){
                            minsNum = editTextMin.text?.toString()?.toInt() ?: 0
                            val til2: TextInputLayout = view.findViewById(R.id.min_input_layout)
                            if (minsNum > 59) {
                                til2.setError("Choose less than than 60 minutes")
                                dismiss = false
                            } else if (hrsNum < 0) {
                                til2.setError("No negative values")
                                dismiss = false
                            } else {
                                til2.isErrorEnabled = false
                            }
                        }else{
                            minsNum= 0
                        }
                        if(dismiss){
                            totalTime=minsNum+hrsNum*60
                            dismiss()//dismisses the dialog
                            showStop()//updates the U/I
                            viewModel.onPlay=true
                            initializeTime()
                            startCountDown()//will be game view model
                            runnable.run()
                            binding.clockPointer.startAnimation(animationClock)
                        }
                    }//end of positive button on click listener
                }//end of setOnShowListener
            }//end of apply
        }//end of lambda function to create a dialog
        alertDialog?.show()

    }
    fun congratulate(){
        val alertDialog: AlertDialog? = activity?.let {
            //this builds and inflates the dialog
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            val view = inflater.inflate(R.layout.dialog_congratulate,null)
            //this is for referencing input
            //the content of the Alert Dialog is mentioned here
            builder?.setMessage("Time: $totalTime minutes")
                    .setView(view)
                    .setPositiveButton("OK",
                            DialogInterface.OnClickListener { dialog, id ->
                                // User cancelled the dialog
                            })
            builder.create()
        }//end of lambda function to create a dialog
        alertDialog?.show()

    }
    fun completionChecker(){
        if(viewModel.completed==true){
            congratulate()
            viewModel.onPlay=false
            showStart()
            handler.removeCallbacksAndMessages(null)
            viewModel.completed=false
        }
        handler.postDelayed(runnable, 1000)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.pauseCount()
    }
}