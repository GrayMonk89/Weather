package com.gb.weather.view.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.gb.weather.R
import com.gb.weather.databinding.FragmentMainBinding
import com.gb.weather.viewmodel.AppState

class MainFragment : Fragment() {

    lateinit var binding: FragmentMainBinding

    override fun onDestroy() {
        super.onDestroy()
        //binding
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        //return inflater.inflate(R.layout.fragment_main, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //binding.sameButton.setOnClickListener() {}

        val viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        val observer = object : Observer<AppState> {
            override fun onChanged(data: AppState) {
                renderData(data)
            }
        }
        viewModel.getData().observe(viewLifecycleOwner, observer)

        viewModel.getWeather()
    }

    private fun renderData(data: AppState) {
        when(data){
            is AppState.Error -> {
                binding.loadingLayout.visibility = View.GONE
                binding.message.text = "Что-то не загрузилось ${data.error}"
            }
            is AppState.Loading -> {
                binding.loadingLayout.visibility = View.VISIBLE

            }
            is AppState.Success -> {
                binding.loadingLayout.visibility = View.GONE
                binding.message.text = "Что-то произошло"
                //Toast.makeText(requireContext(), "Bang", Toast.LENGTH_SHORT).show()
            }

        }

    }

    companion object {
        @JvmStatic
        fun newInstance() = MainFragment()
    }
}