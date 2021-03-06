package com.gb.weather.view.weatherlist

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.gb.weather.R
import com.gb.weather.databinding.FragmentWeatherListBinding
import com.gb.weather.repository.weather.City
import com.gb.weather.repository.weather.Weather
import com.gb.weather.utils.*
import com.gb.weather.view.details.DetailsFragment

import com.gb.weather.viewmodel.AppState
import com.gb.weather.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar
import java.util.*
import kotlin.properties.Delegates

class WeatherListFragment : Fragment(), OnItemListClickListener {

    private var fromHere = DEFAULT_VALUE_BOOLEAN_FALSE

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
        fromHere = requireActivity().getSharedPreferences(
            PREFERENCE_KEY_FILE_NAME_SETTINGS,
            Context.MODE_PRIVATE
        )
            .getBoolean(PREFERENCE_KEY_FILE_NAME_SETTINGS_IS_RUSSIAN, DEFAULT_VALUE_BOOLEAN_FALSE)
        _binding = FragmentWeatherListBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecycler()

        redraw(viewModel, false)

        goToTheMaps()

        binding.floatingActionButton.setOnClickListener {
            redraw(viewModel, true)
        }

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

    private fun goToTheMaps() {
        binding.mainFragmentFABLocation.setOnClickListener {
            checkPermission()
        }
    }

    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            getLocation()
        } else if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
            explainToAFool()
        } else {
            mRequestPermission()
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        context?.let {
            val locationManager = it.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                //val providerGPS = locationManager.getProvider(LocationManager.GPS_PROVIDER)
                val providerGPS = locationManager.getProvider(LocationManager.GPS_PROVIDER)
//                providerGPS?.let {
//                    locationManager.requestLocationUpdates(
//                        LocationManager.GPS_PROVIDER,
//                        10000L,
//                        0f,
//                        locationListenerTime
//                    )
//                }
                providerGPS?.let {
                    locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        0,
                        100f,
                        locationListenerDistance
                    )
                }
            }
        }
    }

    fun getAddressByLocation(location: Location) {
        //val geocoder = Geocoder(requireContext())
        val geocoder = Geocoder(requireContext(), Locale("ru","RU"))
        val timeStump = System.currentTimeMillis()
        Thread {
            val addressText = geocoder.getFromLocation(
                location.latitude,
                location.longitude,
                1000000
            )[0].getAddressLine(0)
            requireActivity().runOnUiThread {
                showAddressDialog(addressText, location)
            }
        }.start()
        Log.d(LOG_KEY, " ???????????? ${System.currentTimeMillis() - timeStump}")
    }

    private fun showAddressDialog(address: String, location: Location) {
        activity?.let {
            AlertDialog.Builder(it)
                .setTitle(getString(R.string.dialog_address_title))
                .setMessage(address)
                .setPositiveButton(getString(R.string.dialog_address_get_weather)) { _, _ ->
                    onItemClick(
                        Weather(
                            City(
                                address,
                                location.latitude,
                                location.longitude
                            )
                        )
                    )
                }
                .setNegativeButton(getString(R.string.dialog_button_close)) { dialog, _ -> dialog.dismiss() }
                .create()
                .show()
        }
    }

    private val locationListenerTime = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            Log.d(LOG_KEY, location.toString())
            getAddressByLocation(location)
        }

        override fun onProviderDisabled(provider: String) {
            super.onProviderDisabled(provider)
        }

        override fun onProviderEnabled(provider: String) {
            super.onProviderEnabled(provider)
        }

    }

    private val locationListenerDistance = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            Log.d(LOG_KEY, location.toString())
            getAddressByLocation(location)
        }

        override fun onProviderDisabled(provider: String) {
            super.onProviderDisabled(provider)
        }

        override fun onProviderEnabled(provider: String) {
            super.onProviderEnabled(provider)
        }

    }

    private fun explainToAFool() {
        AlertDialog.Builder(requireContext())
            .setTitle(resources.getString(R.string.dialog_rationale_title))
            .setMessage(resources.getString(R.string.explain))
            .setPositiveButton(resources.getString(R.string.dialog_rationale_give_access)) { _, _ ->
                mRequestPermission()
            }
            .setNegativeButton(getString(R.string.dialog_rationale_decline)) { dialog, _ -> dialog.dismiss() }
            .create()
            .show()
    }

    private fun mRequestPermission() {
        requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_CODE_LOCATION)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        if (requestCode == REQUEST_CODE_LOCATION) {

            for (i in permissions.indices) {
                if (permissions[i] == Manifest.permission.ACCESS_FINE_LOCATION && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    getLocation()
                } else {
                    explainToAFool()
                }
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
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
            "??????-???? ???? ?????????????????????? \n${msg.toString()}",
            Snackbar.LENGTH_LONG
        )
        mySnack.setAction("?????????????????????? ???????", View.OnClickListener {
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