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
import com.gb.weather.repository.weather.Weather
import com.gb.weather.utils.*
import com.gb.weather.view.details.DetailsFragment

import com.gb.weather.viewmodel.AppState
import com.gb.weather.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar
import kotlin.properties.Delegates

class WeatherListFragment : Fragment(), OnItemListClickListener {

    private val adapter = WeatherListAdapter(this)

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }


    private var _binding: FragmentWeatherListBinding? = null
    private val binding: FragmentWeatherListBinding
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
        _binding = FragmentWeatherListBinding.inflate(inflater, container, false)
        return binding.root
    }

    /*DEFAULT_VALUE_BOOLEAN_TRUE*/
    private var fromHere = false
//        requireActivity().getSharedPreferences(
//        PREFERENCE_KEY_FILE_NAME_SETTINGS,
//        Context.MODE_PRIVATE
//    ).getBoolean(PREFERENCE_KEY_FILE_NAME_SETTINGS_IS_RUSSIAN, DEFAULT_VALUE_BOOLEAN_TRUE)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecycler()
        //val viewModel = ViewModelProvider(this).get(MainViewModel::class.java)


        binding.floatingActionButton.setOnClickListener {
            redraw(viewModel, true)
        }
        viewModel.getWeatherFromHere()
    }


    private fun initRecycler() {
        val observer = Observer<AppState> { data -> renderData(data, viewModel) }
        viewModel.getData().observe(viewLifecycleOwner, observer)
        binding.listRecyclerView.also { it.adapter = adapter }
    }


    private fun redraw(viewModel: MainViewModel, redraw: Boolean) {
        if (redraw) {
            fromHere = !fromHere
            requireActivity().getSharedPreferences(
                PREFERENCE_KEY_FILE_NAME_SETTINGS,
                Context.MODE_PRIVATE
            )
                .edit()
                .putBoolean(PREFERENCE_KEY_FILE_NAME_SETTINGS_IS_RUSSIAN, fromHere)
                .apply()
        }
        if (fromHere) {
            viewModel.getWeatherFromHere()
            binding.floatingActionButton.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_russia
                )
            )
        } else {
            viewModel.getWeatherNotFromHere()
            binding.floatingActionButton.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_earth
                )
            )
        }
    }

    private fun renderData(data: AppState, viewModel: MainViewModel) = when (data) {//
        is AppState.Error -> {
            binding.loadingLayout.apply { visibility = View.GONE }
            showMessage(data, viewModel)
        }
        is AppState.Loading -> {
            binding.loadingLayout.apply { visibility = View.VISIBLE }
        }
        is AppState.Success -> {
            binding.loadingLayout.apply { visibility = View.GONE }
            adapter.setData(data.getWeatherListData())//weatherListData
        }

    }

    private fun showMessage(msg: AppState, viewModel: MainViewModel) {
        val mySnack: Snackbar = Snackbar.make(
            binding.root,
            "Что-то не загрузилось \n${msg.toString()}",
            Snackbar.LENGTH_LONG
        )
        mySnack.setAction("Попробовать еще?", View.OnClickListener {
            redraw(viewModel, false)
        })
            .show()
    }

    companion object {
        @JvmStatic
        fun newInstance() = WeatherListFragment()
    }

    override fun onItemClick(weather: Weather) {
        val bundle = Bundle()
        requireActivity().supportFragmentManager.beginTransaction().add(
            R.id.mainContainer,
            DetailsFragment.newInstance(bundle.apply { putParcelable(BUNDLE_WEATHER_KEY, weather) })
        ).addToBackStack("").commit()
    }
}