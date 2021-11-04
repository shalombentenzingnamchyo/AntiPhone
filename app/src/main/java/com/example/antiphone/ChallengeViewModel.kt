package com.example.antiphone
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.os.Handler
import android.util.Log

class ChallengeViewModel :ViewModel(){
    private var actualTime: Int =0
    private val _hrs = MutableLiveData(0)
    val hrs: LiveData<Int>
        get()=_hrs

    private val _mins = MutableLiveData(0)
    val mins: LiveData<Int>
        get()=_mins

    private val _degrees =MutableLiveData(0)
    val degrees: LiveData<Int>
        get()=_degrees
    var completed= false
    var onPlay=false
    val handler= Handler()
    val runnable = Runnable { countDown() }
    fun initialize(hrs_val:Int,mins_val:Int){
        actualTime= hrs_val*60+mins_val
        _hrs.value=hrs_val
        _mins.value=mins_val
        Log.d("GameViewModel","actualTime: $actualTime, hrs_val:$hrs_val, mins_val:$mins_val")
    }
    fun startCountDown(){
        runnable.run()
    }
    fun countDown(){
        if(_hrs.value==0 && _mins.value==0){
            handler.removeCallbacksAndMessages(null);
            completed= true
        }else {
            handler.postDelayed(runnable, 60000)
            if (_mins.value == 0) {
                _hrs.value = _hrs.value?.minus(1)
                _mins.value = 59
            } else {
                _mins.value = _mins.value?.dec()
            }
            _degrees.value = ((actualTime - _mins.value!! - _hrs.value!!*60) * 360) / actualTime
        }
    }
    fun pauseCount(){
        handler.removeCallbacksAndMessages(null);
    }
    /** Previous code
     * fun startCountDown(){
    if(_hrs.value==0 && _mins.value==0) return
    if(_mins.value==0){
    _hrs.value= _hrs.value?.minus(1)
    _mins.value= 59
    }
    else _mins.value= _mins.value?.inc()
    tempDataX=((actualTime-_mins.value!!+_hrs.value!!)*360)/actualTime
    }

     */
}