package io.hoogland.weer2track.model.response;

import com.google.gson.annotations.SerializedName;

/**
 * Model used to serialize the response data from multiple different OpenWeatherMap API calls.
 *
 * @author dan
 * @see <a href="https://openweathermap.org/forecast5">OpenWeatherMap 5 day weather forecast</a>
 * @see <a href="https://openweathermap.org/current">OpenWeatherMap current weather data</a>
 */
public class MainResponse {
    @SerializedName("temp")
    private double currentTemp;

    @SerializedName("feels_like")
    private double feelsLikeTemp;

    @SerializedName("temp_min")
    private double minTemp;

    @SerializedName("temp_max")
    private double maxTemp;

    public double getCurrentTemp() {
        return currentTemp;
    }

    public void setCurrentTemp(double currentTemp) {
        this.currentTemp = currentTemp;
    }

    public double getFeelsLikeTemp() {
        return feelsLikeTemp;
    }

    public void setFeelsLikeTemp(double feelsLikeTemp) {
        this.feelsLikeTemp = feelsLikeTemp;
    }

    public double getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(double minTemp) {
        this.minTemp = minTemp;
    }

    public double getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(double maxTemp) {
        this.maxTemp = maxTemp;
    }
}
