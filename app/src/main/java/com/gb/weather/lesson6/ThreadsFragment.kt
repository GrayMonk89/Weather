package com.gb.weather.lesson6

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.gb.weather.databinding.FragmentThreadsBinding
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
        binding.calculationButton.setOnClickListener() {
            with(binding)
            {
                Thread {
                    val timeRate = editTextRate.text.toString().toLong()
                    sleep(timeRate * 1000)
                    requireActivity().runOnUiThread() {
                        textViewResult.text = "hard work $timeRate seconds"
                    }
                }.start()
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = ThreadsFragment()
    }
}