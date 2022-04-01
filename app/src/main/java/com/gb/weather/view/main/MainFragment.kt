package com.gb.weather.view.main

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.gb.weather.databinding.FragmentMainBinding
import com.gb.weather.viewmodel.AppState
import com.google.android.material.snackbar.Snackbar

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding

    val SOURCE_LOCAL = 1
    val SOURCE_SERVER = 2

    private val SELECTION_KEY_RG = "Select RadioButton"
    val SELECTION_KEY_RG_SOURCE = "Selected RadioButton"

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
        val observer = Observer<AppState> { data -> renderData(data, viewModel) }
        viewModel.getData().observe(viewLifecycleOwner, observer)

        viewModel.getWeather()
    }



    private fun setCurrentSource(currentSource:Int){
        val sharedPreferences: SharedPreferences = requireContext().getSharedPreferences(SELECTION_KEY_RG, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putInt(SELECTION_KEY_RG_SOURCE, currentSource)
        editor.apply()
    }


    private fun getCurrentSource():Int{
        val sharedPreference:SharedPreferences = requireContext().getSharedPreferences(SELECTION_KEY_RG, Context.MODE_PRIVATE)
        return sharedPreference.getInt(SELECTION_KEY_RG_SOURCE, SOURCE_LOCAL)
    }

    private fun renderData(data: AppState, viewModel: MainViewModel) = when(data){
        is AppState.Error -> {
            binding.loadingLayout.visibility = View.GONE
            //binding.message.text = "Что-то не загрузилось ${data.error}"
            val mySnack:Snackbar =  Snackbar.make(binding.mainView,"Что-то не загрузилось ${data.error}",Snackbar.LENGTH_LONG)
                mySnack.setAction("Попробовать еще?", View.OnClickListener { viewModel.getWeather() }).show()
        }
        is AppState.Loading -> {
            binding.loadingLayout.visibility = View.VISIBLE

        }
        is AppState.Success -> {

            binding.loadingLayout.visibility = View.GONE
            binding.cityName.text = data.weatherData.city.cityName
            binding.temperatureValue.text = data.weatherData.temperature.toString()
            binding.feelsLikeValue.text = data.weatherData.feelsLike.toString()
            binding.cityCoordinates.text = "lat: ${data.weatherData.city.lat} lon: ${data.weatherData.city.lon}"
            Snackbar.make(binding.mainView,"Что-то загрузилось!",Snackbar.LENGTH_LONG).show()
        }

    }

    companion object {
        @JvmStatic
        fun newInstance() = MainFragment()
    }
}