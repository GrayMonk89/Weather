package com.gb.weather.view.historylist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.gb.weather.R
import com.gb.weather.databinding.FragmentHistoryWeatherListBinding
import com.gb.weather.databinding.FragmentWeatherListBinding
import com.gb.weather.repository.weather.Weather
import com.gb.weather.utils.*
import com.gb.weather.view.details.DetailsFragment
import com.gb.weather.viewmodel.AppState
import com.gb.weather.viewmodel.HistoryViewModel
import com.gb.weather.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar

class HistoryWeatherListFragment : Fragment() {

    private val adapter = HistoryWeatherListAdapter()

    private var _binding: FragmentHistoryWeatherListBinding? = null
    private val binding: FragmentHistoryWeatherListBinding
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
        _binding = FragmentHistoryWeatherListBinding.inflate(inflater, container, false)
        return binding.root
    }

    private var fromHere = true
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler()
        val viewModel = ViewModelProvider(this).get(HistoryViewModel::class.java)
        val observer = Observer<AppState> { data -> renderData(data, viewModel) }
        viewModel.getData().observe(viewLifecycleOwner, observer)

        viewModel.getAll()
    }


    private fun initRecycler() {
        binding.listRecyclerView.also { it.adapter = adapter }
    }



/*    private fun redraw(viewModel: HistoryViewModel, redraw:Boolean) {

        if (redraw) {
            fromHere = !fromHere
        }
        if (fromHere) {
            viewModel.getWeatherFromHere()
*//*            binding.floatingActionButton.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_russia
                )
            )*//*
        } else {
            viewModel.getWeatherNotFromHere()
*//*            binding.floatingActionButton.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_earth
                )
            )*//*
        }
    }*/

    private fun renderData(data: AppState, viewModel: HistoryViewModel) = when (data) {//
        is AppState.Error -> {
            //binding.loadingLayout.apply{ visibility = View.GONE }
            //showMessage(data,viewModel)
        }
        is AppState.Loading -> {
            //binding.loadingLayout.apply{visibility = View.VISIBLE}
        }
        is AppState.Success -> {
            //binding.loadingLayout.apply{ visibility = View.GONE }
            adapter.setData(data.getWeatherListData())//weatherListData
        }

    }

/*    private fun showMessage(msg: AppState, viewModel: HistoryViewModel){
        val mySnack: Snackbar = Snackbar.make(
            binding.root,
            "Что-то не загрузилось \n${msg.toString()}",
            Snackbar.LENGTH_LONG
        )
        mySnack.setAction("Попробовать еще?", View.OnClickListener {
            redraw(viewModel,false)
        })
            .show()
    }*/

    companion object {
        @JvmStatic
        fun newInstance() = HistoryWeatherListFragment()
    }
}