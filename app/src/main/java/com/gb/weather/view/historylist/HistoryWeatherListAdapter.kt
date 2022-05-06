package com.gb.weather.view.historylist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.load
import coil.request.ImageRequest
import com.gb.weather.databinding.FragmentHistoryWeatherListBinding
import com.gb.weather.databinding.FragmentHistoryWeatherListRecyclerItemBinding
import com.gb.weather.databinding.FragmentWeatherListRecyclerItemBinding
import com.gb.weather.repository.weather.Weather
import com.gb.weather.view.weatherlist.OnItemListClickListener

class HistoryWeatherListAdapter(
    private var data: List<Weather> = listOf()
) :
    RecyclerView.Adapter<HistoryWeatherListAdapter.CityHolder>() {

    fun setData(data: List<Weather>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityHolder {
        val binding = FragmentHistoryWeatherListRecyclerItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CityHolder(binding.root)
    }

    override fun onBindViewHolder(holder: CityHolder, position: Int) {
        holder.bind(data.get(position))
        //holder.bind(data.get(position))
    }

    override fun getItemCount() = data.size

    inner class CityHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(weather: Weather) {
            val binding = FragmentHistoryWeatherListRecyclerItemBinding.bind(itemView)
//            binding.tvCityName.text = weather.city.cityName
//            binding.root.setOnClickListener {
//            onItemListClickListener.onItemClick(weather)
            with(binding) {
                tvCityName.text = weather.city.cityName
                tvTemperature.text = weather.temperature.toString()
                tvFeelsLike.text = weather.feelsLike.toString()
                //ivIcon.load(weather.icon)
                ivIcon.loadSvg("https://yastatic.net/weather/i/icons/blueye/color/svg/${weather.icon}.svg")

            }
        }

        private fun ImageView.loadSvg(url: String) {
            val imageLoader = ImageLoader.Builder(this.context)
                .componentRegistry { add(SvgDecoder(this@loadSvg.context)) }
                .build()
            val request = ImageRequest.Builder(this.context)
                .crossfade(true)
                .crossfade(500)
                .data(url)
                .target(this)
                .build()
            imageLoader.enqueue(request)
        }
    }
}