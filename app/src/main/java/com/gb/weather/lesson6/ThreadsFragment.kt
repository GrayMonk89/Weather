package com.gb.weather.lesson6

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.gb.weather.databinding.FragmentThreadsBinding
import com.gb.weather.utils.MIL_SEC_TO_SEC_MULTIPLIER
import java.lang.Thread.sleep

class ThreadsFragment : Fragment() {

    private var _binding: FragmentThreadsBinding? = null
    private val binding: FragmentThreadsBinding
        get() {
            return _binding!!
        }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentThreadsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val myEternalThread = EternalThread()
        var counter = 0
        myEternalThread.start()
        with(binding) {
            val periodOfTime = editTextRate.text.toString().toLong()
            calculationButton.setOnClickListener {
                Thread {
                    sleep(periodOfTime * MIL_SEC_TO_SEC_MULTIPLIER)
                    requireActivity().runOnUiThread() {
                        textViewResult.text = "hard work $periodOfTime seconds"
                        createTextView("${Thread.currentThread().name} ${++counter}")
                    }
                    /*Handler(Looper.getMainLooper()).post() {
                        textViewResult.text = "hard work $timeRate seconds 2"
                        createTextView("${Thread.currentThread().name} ${++counter}")
                    }*/
                }.start()
            }

            buttonEternalThread.setOnClickListener {
                myEternalThread.mHandler?.post() {
                    /*requireActivity().runOnUiThread() {
                        textViewEternalThread.text = "hard work $timeRate seconds 1"
                        createTextView("${Thread.currentThread().name} ${++counter}")
                    }*/
                    Handler(Looper.getMainLooper()).post() {
                        textViewEternalThread.text = "hard work $periodOfTime seconds"
                        createTextView("${Thread.currentThread().name} ${++counter}")
                    }
                }
            }
        }
    }


    private fun createTextView(name: String) {
        binding.fragmentContainer.addView(TextView(requireContext()).apply {
            text = name
            textSize = 14f
        })
    }

    class EternalThread : Thread() {
        var mHandler: Handler? = null
        override fun run() {
            Looper.prepare()
            mHandler = Handler(Looper.myLooper()!!)
            Looper.loop()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = ThreadsFragment()
    }
}
