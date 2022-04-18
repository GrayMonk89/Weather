package com.gb.weather.view.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.gb.weather.databinding.FragmentDetailsBinding
import com.gb.weather.repository.OnServerResponse
import com.gb.weather.repository.Weather
import com.gb.weather.repository.WeatherLoader
import com.gb.weather.repository.dto.WeatherDTO
import com.gb.weather.utils.KEY_BUNDLE_WEATHER
import com.google.android.material.snackbar.Snackbar

class DetailsFragment : Fragment(), OnServerResponse {

    private var _binding: FragmentDetailsBinding? = null
    private val binding: FragmentDetailsBinding
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
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)

        return binding.root
    }

    lateinit var currentCityName: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireArguments().getParcelable<Weather>(KEY_BUNDLE_WEATHER)?.let {
            currentCityName = it.city.cityName
            WeatherLoader(this@DetailsFragment).loadWeather(it.city.lat, it.city.lon)

        }
    }


    private fun renderData(weather: WeatherDTO) {
        with(binding) {
            loadingLayout.visibility = View.GONE
            cityName.text = currentCityName
            temperatureValue.text = weather.factDTO.temperature.toString()
            feelsLikeValue.text = weather.factDTO.feelsLike.toString()
            cityCoordinates.text = "lat: ${weather.infoDTO.lat} lon: ${weather.infoDTO.lon}"
        }
        showMessage("Что-то загрузилось!")
    }

    private fun showMessage(msg: String) {
        Snackbar.make(binding.mainView, msg, Snackbar.LENGTH_LONG).show()
    }


    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle): DetailsFragment {
//            val fragment = DetailsFragment()
//            fragment.arguments = bundle
//            return fragment
            return DetailsFragment().apply { arguments = bundle }
        }
    }

    override fun onResponse(weatherDTO: WeatherDTO) {
        renderData(weatherDTO)
    }
}