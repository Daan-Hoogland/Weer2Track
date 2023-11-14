package io.hoogland.weer2track.model.response;

import com.google.gson.annotations.SerializedName;

/**
 * Model used to serialize the response data from multiple different OpenWeatherMap API calls.
 *
 * @author dan
 * @see <a href="https://openweathermap.org/forecast5">OpenWeatherMap 5 day weather forecast</a>
 * @see <a href="https://openweathermap.org/current">OpenWeatherMap current weather data</a>
 */
public class Weather {
    @SerializedName("description")
    private String description;

    @SerializedName("icon")
    private String icon;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
