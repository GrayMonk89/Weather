package com.gb.weather.view.details

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.gb.weather.BuildConfig
import com.gb.weather.databinding.FragmentDetailsBinding
import com.gb.weather.repository.OnServerResponse
import com.gb.weather.repository.OnServerResponseListener
import com.gb.weather.repository.Weather
import com.gb.weather.repository.dto.WeatherDTO
import com.gb.weather.utils.*
import com.gb.weather.viewmodel.ResponseState
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_details.*
import okhttp3.*
import java.io.IOException
import java.net.URL

class DetailsFragment : Fragment(), OnServerResponse, OnServerResponseListener {

    private var _binding: FragmentDetailsBinding? = null
    private val binding: FragmentDetailsBinding
        get() {
            return _binding!!
        }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        requireContext().unregisterReceiver(receiver)
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            intent.let { intent ->
                intent.getParcelableExtra<WeatherDTO>(SERVICE_BROADCAST_WEATHER_KEY)?.let {
                    onResponse(it)
                }
            }
        }
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

        requireContext().registerReceiver(
            receiver,
            IntentFilter(BROADCAST_RECEIVER_CHANNEL_WEATHER_KEY)
        )

        requireArguments().getParcelable<Weather>(BUNDLE_WEATHER_KEY)?.let {
            currentCityName = it.city.cityName
/*            WeatherLoader(this@DetailsFragment, this@DetailsFragment).loadWeather(
                it.city.lat,
                it.city.lon
            )*/

            /*requireActivity().startService(Intent(requireContext(),DetailsService::class.java).apply {
                putExtra(LON_KEY,it.city.lon)
                putExtra(LAT_KEY,it.city.lat)
            })*/

            getWeather(it.city.lat, it.city.lon)

        }
    }


    private fun getWeather(lat: Double, lon: Double) {
        binding.loadingLayout.visibility = View.VISIBLE

        val client = OkHttpClient()
        val requestBuilder = Request.Builder()
        requestBuilder.url("$YANDEX_DOMAIN_HARD_MODE_PART$YANDEX_ENDPOINT$LAT_KEY=$lat&$LON_KEY=$lon")
        requestBuilder.addHeader(YANDEX_API_KEY, BuildConfig.WEATHER_API_KEY)
        val request = requestBuilder.build()

        val callback: Callback = object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                //mainView.let { Snackbar.make(it, "все плохо! $e", Snackbar.LENGTH_LONG).show() }
                mainView.showSnackBar("все еще плохее! $e", "", {}, Snackbar.LENGTH_LONG)
                binding.loadingLayout.visibility = View.GONE
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {

                    //val serverResponse:String =

                    val weatherDTO: WeatherDTO = Gson().fromJson(response.body()!!.string(),WeatherDTO::class.java)
                    requireActivity().runOnUiThread{
                        renderData(weatherDTO)
                    }
                } else{
                    requireActivity().runOnUiThread{
                        binding.loadingLayout.visibility = View.GONE
                    }

                }
            }
        }

        val call = client.newCall(request)
        //client.newCall(requestBuilder.build()).enqueue(callback)
        call.enqueue(callback)

    }

    private fun renderData(weather: WeatherDTO) {
        with(binding) {
            loadingLayout.visibility = View.GONE
            cityName.text = currentCityName
            temperatureValue.text = weather.factDTO.temperature.toString()
            feelsLikeValue.text = weather.factDTO.feelsLike.toString()
            cityCoordinates.text = "lat: ${weather.infoDTO.lat} lon: ${weather.infoDTO.lon}"
        }
        mainView.showSnackBar("Ура! Загрузилось!", "", {}, Snackbar.LENGTH_LONG)

    }

    private fun showMessage(msg: String) {
        Snackbar.make(binding.mainView, msg, Snackbar.LENGTH_LONG).show()
    }


    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle): DetailsFragment {
            return DetailsFragment().apply { arguments = bundle }
        }
    }

    override fun onResponse(weatherDTO: WeatherDTO) {
        renderData(weatherDTO)
    }

    override fun onError(error: ResponseState) = when (error) {
        is ResponseState.ErrorOnClientSide -> {
            mainView.showSnackBar(error.errorMessage, "", {}, Snackbar.LENGTH_LONG)
        }
        is ResponseState.ErrorOnServerSide -> {
            mainView.showSnackBar(error.errorMessage, "", {}, Snackbar.LENGTH_LONG)
        }
        is ResponseState.ErrorInJSONConversion -> {
            mainView.showSnackBar(error.errorMessage, "", {}, Snackbar.LENGTH_LONG)
        }

    }
}