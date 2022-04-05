package com.gb.weather.view.weatherlist

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
import com.google.android.material.snackbar.Snackbar

class WeatherListFragment : Fragment(), OnItemListClickListener {

    private val adapter = WeatherListAdapter(this)

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

    private var fromHere = true
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.listRecyclerView.adapter = adapter
        val viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        val observer = object : Observer<AppState> {
            override fun onChanged(data: AppState) {
                renderData(data, viewModel)
            }
        }
        viewModel.getData().observe(viewLifecycleOwner, observer)

        binding.floatingActionButton.setOnClickListener {
            redraw(viewModel)
        }
        viewModel.getWeatherFromHere()
    }

    private fun redraw(viewModel: MainViewModel) {
        fromHere = !fromHere
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
            binding.loadingLayout.visibility = View.GONE
            val mySnack: Snackbar = Snackbar.make(
                binding.root,
                "Что-то не загрузилось ${data.getError()}",
                Snackbar.LENGTH_LONG
            )
            mySnack.setAction("Попробовать еще?", View.OnClickListener {
                redraw(viewModel)
            })
                .show()
        }
        is AppState.Loading -> {
            binding.loadingLayout.visibility = View.VISIBLE
        }
        is AppState.Success -> {
            binding.loadingLayout.visibility = View.GONE
            adapter.setData(data.getWeatherListData())//weatherListData
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