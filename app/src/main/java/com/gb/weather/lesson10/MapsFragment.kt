package com.gb.weather.lesson10

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.text.isDigitsOnly
import androidx.fragment.app.Fragment
import com.gb.weather.R
import com.gb.weather.databinding.FragmentMapsMainBinding
import com.gb.weather.repository.weather.City
import com.gb.weather.repository.weather.Weather
import com.gb.weather.utils.BUNDLE_WEATHER_KEY
import com.gb.weather.utils.REQUEST_CODE_LOCATION
import com.gb.weather.utils.showSnackBar
import com.gb.weather.view.details.DetailsFragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import java.util.*

class MapsFragment : Fragment() {


    private lateinit var map: GoogleMap
    private val markers: ArrayList<Marker> = arrayListOf()
    private var _binding: FragmentMapsMainBinding? = null
    private val binding: FragmentMapsMainBinding
        get() {
            return _binding!!
        }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private val callback = OnMapReadyCallback { googleMap ->
        map = googleMap
        val default = LatLng(56.12, 47.48)
        googleMap.addMarker(MarkerOptions().position(default).title("Marker in Sydney"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(default))

        map.setOnMapLongClickListener {
            addMarkerToArray(it)
            drawLine()
        }

        map.setOnMyLocationClickListener {
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(default, 7f))
        }

        fun getAddressByLocation(location: LatLng): String {
            val geocoder = Geocoder(requireContext(), Locale.getDefault())
            val addressText = geocoder.getFromLocation(
                location.latitude,
                location.longitude,
                1000000
            )[0].getAddressLine(0)
            return addressText
        }


        map.setOnMapClickListener {
            val weather = Weather(city = City(getAddressByLocation(it), it.latitude, it.longitude))
            requireActivity().supportFragmentManager.beginTransaction().add(
                R.id.mainContainer,
                DetailsFragment.newInstance(Bundle().apply {
                    putParcelable(
                        BUNDLE_WEATHER_KEY,
                        weather
                    )
                })
            ).addToBackStack("").commit()
        }



        map.uiSettings.isZoomControlsEnabled = true
        map.uiSettings.isMyLocationButtonEnabled = true

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            mRequestPermission()
        } else if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) ||
            shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)
        ) {
            explainToAFool()
        } else {
            map.isMyLocationEnabled = true
        }


    }


    private fun addMarkerToArray(location: LatLng) {
        val marker = setMarker(location, markers.size.toString(), R.drawable.ic_map_pin)
        markers.add(marker)
    }

    private fun setMarker(
        location: LatLng,
        searchText: String,
        resourceId: Int
    ): Marker {
        return map.addMarker(
            MarkerOptions().apply {
                position(location)
                title(searchText)
                icon(BitmapDescriptorFactory.fromResource(resourceId))
            }
        )!!
    }

    private fun drawLine() {
        var previousMarker: Marker? = null
        markers.forEach() { current ->
            previousMarker?.let { previous ->
                map.addPolyline(
                    PolylineOptions()
                        .add(previous.position, current.position)
                        .color(R.color.purple_200)
                        .width(5f)
                )

            }
            previousMarker = current
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMapsMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
        searchButtonInit()
    }

    private fun searchButtonInit() {
        binding.buttonSearch.setOnClickListener {
            val searchText = binding.searchAddress.text.toString()
            val geocoder = Geocoder(requireContext(), Locale.getDefault())
            if (!searchText.isDigitsOnly() && searchText.isNotEmpty()) {
                try {
                    val latLon = LatLng(
                        geocoder.getFromLocationName(searchText, 1)[0].latitude,
                        geocoder.getFromLocationName(searchText, 1)[0].longitude
                    )
                    map.addMarker(
                        MarkerOptions().position(latLon)
                            .title(searchText)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_marker))
                    )
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLon, 7f))

                } catch (e: IndexOutOfBoundsException) {
                    view?.showSnackBar("Беда. Не нужно вводить фигню! $e", "", {})
                }
            } else {
                view?.showSnackBar("Беда. Не нужно вводить фигню", "", {})
            }
            binding.buttonSearch.hideKeyboard()
        }
    }

    private fun View.hideKeyboard() {// как вынести этот метод например в ViewUtils
        (requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
            windowToken,
            0
        )
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
        requestPermissions(
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
            REQUEST_CODE_LOCATION
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        if (requestCode == REQUEST_CODE_LOCATION) {

            for (i in permissions.indices) {
                if (permissions[i] == Manifest.permission.ACCESS_FINE_LOCATION && grantResults[i] == PackageManager.PERMISSION_GRANTED ||
                    permissions[i] == Manifest.permission.ACCESS_COARSE_LOCATION && grantResults[i] == PackageManager.PERMISSION_GRANTED
                ) {
                    map.isMyLocationEnabled = true
                } else {
                    explainToAFool()
                }
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }
}