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
import com.gb.weather.databinding.FragmentDetailsBinding
import com.gb.weather.databinding.FragmentWeatherListBinding
import com.gb.weather.viewmodel.AppState
import com.gb.weather.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar

class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding: FragmentDetailsBinding
        get(){
            return _binding!!
        }
//    private val binding: FragmentWeatherListBinding
//    get(){
//        return _binding!!
//    }


    val SOURCE_LOCAL = 1
    val SOURCE_SERVER = 2

    private val SELECTION_KEY_RG = "Select RadioButton"
    val SELECTION_KEY_RG_SOURCE = "Selected RadioButton"

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        //return inflater.inflate(R.layout.fragment_main, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }



/*
    private fun renderData(data: AppState, viewModel: MainViewModel) = when (data) {
        is AppState.Error -> {
            binding.loadingLayout.visibility = View.GONE
            //binding.message.text = "Что-то не загрузилось ${data.error}"
            val mySnack: Snackbar = Snackbar.make(
                binding.mainView,
                "Что-то не загрузилось ${data.error}",
                Snackbar.LENGTH_LONG
            )
            mySnack.setAction("Попробовать еще?", View.OnClickListener { viewModel.getWeather() })
                .show()
        }
        is AppState.Loading -> {
            binding.loadingLayout.visibility = View.VISIBLE

        }
        is AppState.Success -> {

            binding.loadingLayout.visibility = View.GONE
            binding.cityName.text = data.weatherData.city.cityName
            binding.temperatureValue.text = data.weatherData.temperature.toString()
            binding.feelsLikeValue.text = data.weatherData.feelsLike.toString()
            binding.cityCoordinates.text =
                "lat: ${data.weatherData.city.lat} lon: ${data.weatherData.city.lon}"
            Snackbar.make(binding.mainView, "Что-то загрузилось!", Snackbar.LENGTH_LONG).show()
        }

    }
*/

    companion object {
        @JvmStatic
        fun newInstance() = DetailsFragment()
    }
}