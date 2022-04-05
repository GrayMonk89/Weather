package com.gb.weather.view.weatherlist

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.gb.weather.R
import com.gb.weather.databinding.FragmentWeatherListBinding
import com.gb.weather.repository.Weather
import com.gb.weather.utils.KEY_BUNDLE_WEATHER
import com.gb.weather.view.details.DetailsFragment
import com.gb.weather.viewmodel.AppState
import com.gb.weather.viewmodel.MainViewModel

class WeatherListFragment : Fragment(), OnItemListClickListener {

    private var _binding: FragmentWeatherListBinding? = null
    private val binding: FragmentWeatherListBinding
        get() {
            return _binding!!
        }

    val adapter = WeatherListAdapter(this)


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
        _binding = FragmentWeatherListBinding.inflate(inflater, container, false)
        //return inflater.inflate(R.layout.fragment_main, container, false)
        return binding.root
    }

    private var fromHere = true
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //binding.sameButton.setOnClickListener() {}
        binding.listRecyclerView.adapter = adapter
        val viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        val observer = Observer<AppState> { data -> renderData(data, viewModel) }
        viewModel.getData().observe(viewLifecycleOwner, observer)

        binding.floatingActionButton.setOnClickListener {
            fromHere = !fromHere
            if (fromHere) {
                viewModel.getWeatherFromHere()
                binding.floatingActionButton.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.ic_russia))
            } else {
                viewModel.getWeatherNotFromHere()
                binding.floatingActionButton.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.ic_earth))
            }
        }
        viewModel.getWeatherFromHere()
    }


    private fun setCurrentSource(currentSource: Int) {
        val sharedPreferences: SharedPreferences =
            requireContext().getSharedPreferences(SELECTION_KEY_RG, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putInt(SELECTION_KEY_RG_SOURCE, currentSource)
        editor.apply()
    }


    private fun getCurrentSource(): Int {
        val sharedPreference: SharedPreferences =
            requireContext().getSharedPreferences(SELECTION_KEY_RG, Context.MODE_PRIVATE)
        return sharedPreference.getInt(SELECTION_KEY_RG_SOURCE, SOURCE_LOCAL)
    }

    private fun renderData(data: AppState, viewModel: MainViewModel) = when (data) {//
        is AppState.Error -> {
            binding.loadingLayout.visibility = View.GONE
            //binding.message.text = "Что-то не загрузилось ${data.error}"
/*            val mySnack: Snackbar = Snackbar.make(
                binding.root,
                "Что-то не загрузилось ${data.error}",
                Snackbar.LENGTH_LONG
            )
            mySnack.setAction("Попробовать еще?", View.OnClickListener { viewModel.getWeather() })
                .show()*/
        }
        is AppState.Loading -> {
            binding.loadingLayout.visibility = View.VISIBLE

        }
        is AppState.Success -> {

            binding.loadingLayout.visibility = View.GONE
            adapter.setData(data.weatherListData)

/*            binding.loadingLayout.visibility = View.GONE
            binding.cityName.text = data.weatherData.city.cityName
            binding.temperatureValue.text = data.weatherData.temperature.toString()
            binding.feelsLikeValue.text = data.weatherData.feelsLike.toString()
            binding.cityCoordinates.text =
                "lat: ${data.weatherData.city.lat} lon: ${data.weatherData.city.lon}"
            Snackbar.make(binding.mainView, "Что-то загрузилось!", Snackbar.LENGTH_LONG).show()*/
        }

    }

    companion object {
        @JvmStatic
        fun newInstance() = WeatherListFragment()
    }

    override fun onItemClick(weather: Weather) {
        val bundle = Bundle()
        bundle.putParcelable(KEY_BUNDLE_WEATHER, weather)
        requireActivity().supportFragmentManager.beginTransaction().add(
            R.id.mainContainer,
            DetailsFragment.newInstance(bundle)
        ).addToBackStack("").commit()
    }
}