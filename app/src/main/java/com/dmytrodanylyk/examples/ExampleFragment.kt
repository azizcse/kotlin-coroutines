package com.dmytrodanylyk.examples

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dmytrodanylyk.R
import kotlinx.android.synthetic.main.fragment_button.*
import kotlinx.coroutines.experimental.*


/*
*  ****************************************************************************
*  * Created by : Md. Azizul Islam on 10/16/2018 at 4:59 PM.
*  * Email : azizul@w3engineers.com
*  * 
*  * Purpose:
*  *
*  * Last edited by : Md. Azizul Islam on 10/16/2018.
*  * 
*  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>  
*  ****************************************************************************
*/

class ExampleFragment : Fragment() {
    companion object {
        const val TAG = "ExampleFragment"
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_button, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        button.setOnClickListener {
            //loadData()
            //scopeBuilder()
            //suspendCheck()
            areCorotin()
        }
    }

    private fun loadData() = runBlocking{
        val job = GlobalScope.launch {
            delay(1000L)
            Log.e(TAG,"Inside coroutin!")
        }
        Log.e(TAG,"Hello,")
        job.join() // wait until child coroutine completes
        Log.e(TAG,"Outside coroutin!")


    }

    private fun scopeBuilder() = runBlocking{
        launch {
            delay(200L)
            Log.e(TAG,"Task from runBlocking")
        }

        coroutineScope { // Creates a new coroutine scope
            launch {
                delay(500L)
                Log.e(TAG,"Task from nested launch")
            }

            delay(100L)
            Log.e(TAG,"Task from coroutine scope") // This line will be printed before nested launch
        }

        Log.e(TAG,"Coroutine scope is over") // This line is not printed until nested launch completes
    }

    private fun suspendCheck()= runBlocking {
        launch { doWorld() }
        Log.e(TAG,"Supend main")
    }
    suspend fun doWorld() {
        delay(1000L)
        Log.e(TAG,"Suspend word")
    }

    private fun areCorotin() = runBlocking{
        val job = launch {
            try {
                repeat(1000) { i ->
                    Log.e(TAG,"I'm sleeping $i ...")
                    delay(500L)
                }
            } finally {
                withContext(NonCancellable){
                    Log.e(TAG,"I'm running finally")
                    delay(1000)
                    Log.e(TAG,"After 1 minitue because i,m non cancelable")
                }

            }
        }
        delay(1300L) // delay a bit
        Log.e(TAG,"main: I'm tired of waiting!")
        job.cancelAndJoin() // cancels the job and waits for its completion
        Log.e(TAG,"main: Now I can quit.")
    }
}