package io.hoogland.weer2track.model.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Model used to serialize the response data from an OpenWeatherMap API call to their weather
 * forecast API.
 *
 * @author dan
 * @see <a href="https://openweathermap.org/forecast5">OpenWeatherMap 5 day weather forecast</a>
 */
public class ForecastResponse implements Serializable {

    @SerializedName("list")
    List<Forecast> forecasts;

    public List<Forecast> getForecasts() {
        return forecasts;
    }

    public void setForecasts(List<Forecast> forecasts) {
        this.forecasts = forecasts;
    }
}
