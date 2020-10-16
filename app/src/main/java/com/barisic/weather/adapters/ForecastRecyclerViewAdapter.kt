package com.barisic.weather.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.barisic.weather.databinding.ItemWeatherBinding
import com.barisic.weather.models.Weather
import com.barisic.weather.util.ICON_URL
import com.squareup.picasso.Picasso

class ForecastRecyclerViewAdapter(private var forecastWeatherList: ArrayList<Weather>) :
    RecyclerView.Adapter<ForecastRecyclerViewAdapter.WeatherViewHolder>() {
    private lateinit var binding: ItemWeatherBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = ItemWeatherBinding.inflate(inflater, parent, false)

        return WeatherViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        if (position == 0) holder.bind(forecastWeatherList[position], null)
        else holder.bind(forecastWeatherList[position], forecastWeatherList[position - 1].getDay())
    }

    override fun getItemCount(): Int {
        return forecastWeatherList.count()
    }

    class WeatherViewHolder(private val binding: ItemWeatherBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(weather: Weather, dayOfPrevious: String?) {
            binding.weather = weather
            binding.forecastTime = weather.getHours()
            binding.forecastDate = when (dayOfPrevious) {
                null, weather.getDay() -> ""
                else -> weather.getDate()
            }
            Picasso.get()
                .load("$ICON_URL${weather.getWeatherData().icon}@4x.png")
                .into(binding.icon)
        }
    }
}