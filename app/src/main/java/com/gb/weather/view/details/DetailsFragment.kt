package com.gb.weather.view.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.gb.weather.databinding.FragmentDetailsBinding
import com.gb.weather.repository.OnServerResponse
import com.gb.weather.repository.OnServerResponseListener
import com.gb.weather.repository.Weather
import com.gb.weather.repository.WeatherLoader
import com.gb.weather.repository.dto.WeatherDTO
import com.gb.weather.utils.KEY_BUNDLE_WEATHER
import com.gb.weather.utils.showSnackBar
import com.gb.weather.viewmodel.ResponseState
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_details.*

class DetailsFragment : Fragment(), OnServerResponse, OnServerResponseListener {

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
            WeatherLoader(this@DetailsFragment, this@DetailsFragment).loadWeather(
                it.city.lat,
                it.city.lon
            )

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
        mainView.showSnackBar("Ура! Загрузилось!", "",{}, Snackbar.LENGTH_LONG)

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

    override fun onError(error: ResponseState) = when(error){
        is ResponseState.ErrorOnClientSide ->{mainView.showSnackBar(error.errorMessage,"",{},Snackbar.LENGTH_LONG)}
        is ResponseState.ErrorOnServerSide ->{mainView.showSnackBar(error.errorMessage,"",{},Snackbar.LENGTH_LONG)}
        is ResponseState.ErrorInJSONConversion ->{mainView.showSnackBar(error.errorMessage,"",{},Snackbar.LENGTH_LONG)}

    }
}