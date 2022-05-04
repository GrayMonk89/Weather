package com.gb.weather.view.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.gb.weather.databinding.FragmentDetailsBinding
import com.gb.weather.repository.Weather
import com.gb.weather.utils.BUNDLE_WEATHER_KEY
import com.gb.weather.utils.showSnackBar
import com.gb.weather.viewmodel.DetailsState
import com.gb.weather.viewmodel.DetailsViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_details.*

class DetailsFragment : Fragment() {

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

    private val viewModel: DetailsViewModel by lazy {
        ViewModelProvider(this).get(DetailsViewModel::class.java)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getLiveData().observe(viewLifecycleOwner,object :Observer<DetailsState>{
            override fun onChanged(t: DetailsState) {
                renderData(t)
            }

        })

        requireArguments().getParcelable<Weather>(BUNDLE_WEATHER_KEY)?.let {
            viewModel.getWeather(it.city)
        }
    }


    /*private fun getWeather(lat: Double, lon: Double) {
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

    }*/

    private fun renderData(detailsState: DetailsState) {

        when(detailsState){
            is DetailsState.Error -> {
                loadingLayout.visibility = View.GONE
                mainView.showSnackBar(detailsState.error.toString(), "", {}, Snackbar.LENGTH_LONG)
            }
            DetailsState.Loading -> {
                loadingLayout.visibility = View.VISIBLE
            }
            is DetailsState.Success -> {
                loadingLayout.visibility = View.GONE
                val weather = detailsState.weather
                with(binding) {
                    loadingLayout.visibility = View.GONE
                    cityName.text = weather.city.cityName
                    temperatureValue.text = weather.temperature.toString()
                    feelsLikeValue.text = weather.feelsLike.toString()
                    cityCoordinates.text = "lat: ${weather.city.lat} lon: ${weather.city.lon}"
                }
                mainView.showSnackBar("Ура! Загрузилось!", "", {}, Snackbar.LENGTH_LONG)
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle): DetailsFragment {
            return DetailsFragment().apply { arguments = bundle }
        }
    }


}