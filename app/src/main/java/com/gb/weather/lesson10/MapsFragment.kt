package com.gb.weather.lesson10

import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gb.weather.R
import com.gb.weather.databinding.FragmentMapsMainBinding

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsFragment : Fragment() {

    private var _binding:FragmentMapsMainBinding? = null
    private val binding:FragmentMapsMainBinding
    get() {
        return _binding!!
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private val callback = OnMapReadyCallback { googleMap ->
        val default = LatLng(56.12, 47.48)
        googleMap.addMarker(MarkerOptions().position(default).title("Marker in Sydney"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(default))
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
}