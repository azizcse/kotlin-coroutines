package com.dmytrodanylyk.examples

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dmytrodanylyk.R
import kotlinx.android.synthetic.main.fragment_button.*
import kotlinx.coroutines.experimental.*
import kotlinx.coroutines.experimental.android.Main
import java.util.*
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.experimental.CoroutineDispatcher
import kotlin.coroutines.experimental.CoroutineContext

abstract class ScopedFragment : Fragment(), CoroutineScope {

    private lateinit var job: Job
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        job = Job()
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}

class AndroidScopedFragment : ScopedFragment() {

    private val dataProvider = DataProvider()

    companion object {
        const val TAG = "AndroidScopedFragment"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_button, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        button.setOnClickListener { loadData() }
    }

    private fun loadData() = launch {
        showLoading() // ui thread

        val result = dataProvider.loadData() // non ui thread, suspend until finished

        showText(result) // ui thread
        hideLoading() // ui thread
    }

    private fun showLoading() {
        progressBar.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        progressBar.visibility = View.GONE
    }

    private fun showText(data: String) {
        textView.text = data
    }

    class DataProvider(private val dispatcher: CoroutineDispatcher = Dispatchers.IO) {

        suspend fun loadData(): String = withContext(dispatcher) {
            delay(2, TimeUnit.SECONDS) // imitate long running operation
            "Data is available: ${Random().nextInt()}"
        }
    }
}