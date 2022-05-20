package com.gb.weather.lesson10

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import android.graphics.Color
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.gb.weather.R
import com.gb.weather.databinding.FragmentMapsBinding
import com.gb.weather.databinding.FragmentMapsMainBinding
import com.gb.weather.utils.REQUEST_CODE_LOCATION

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*

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
        } else if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)||
                   shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)) {
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
        requestPermissions(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), REQUEST_CODE_LOCATION)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        if (requestCode == REQUEST_CODE_LOCATION) {

            for (i in permissions.indices) {
                if (permissions[i] == Manifest.permission.ACCESS_FINE_LOCATION && grantResults[i] == PackageManager.PERMISSION_GRANTED||
                    permissions[i] == Manifest.permission.ACCESS_COARSE_LOCATION && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
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