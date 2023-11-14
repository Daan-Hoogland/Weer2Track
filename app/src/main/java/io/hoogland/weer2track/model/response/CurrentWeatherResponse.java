package io.hoogland.weer2track.model.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Model used to serialize the response data from an OpenWeatherMap API call to their current
 * weather API.
 *
 * @author dan
 * @see <a href="https://openweathermap.org/current">OpenWeatherMap current weather data</a>
 */
public class CurrentWeatherResponse implements Serializable {
    @SerializedName("weather")
    private List<Weather> weather;

    @SerializedName("main")
    private MainResponse mainResponse;

    @SerializedName("dt")
    private LocalDateTime dateTime;

    public List<Weather> getWeather() {
        return weather;
    }

    public void setWeather(List<Weather> weather) {
        this.weather = weather;
    }

    public MainResponse getMainResponse() {
        return mainResponse;
    }

    public void setMainResponse(MainResponse mainResponse) {
        this.mainResponse = mainResponse;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
}
