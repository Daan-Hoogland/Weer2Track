package io.hoogland.weer2track.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalDateTime;

import io.hoogland.weer2track.model.response.CurrentWeatherResponse;

/**
 * Model used to display and save current weather data
 */
@Entity(tableName = "current_weather")
public class CurrentWeather {

    /**
     * ID is set at 1, since there will only ever be one entry in the application database.
     */
    @PrimaryKey
    private int uid = 1;

    private LocalDateTime dateTime;

    private double temp;

    private double feelsLikeTemp;

    private String description;

    private String icon;

    /**
     * Constructor to create a model based on a {@link CurrentWeatherResponse} received through the
     * OpenWeatherMap API.
     * Get the first entry of the list of {@link io.hoogland.weer2track.model.response.Weather} conditions,
     * since the first one will always be the primary one.
     *
     * @param response {@link CurrentWeatherResponse} received from an OpenWeatherMap API call
     * @see <a href="https://openweathermap.org/weather-conditions">Weather Conditions</a>
     * @see <a href="https://openweathermap.org/current">OpenWeatherMap current weather data</a>
     */
    public CurrentWeather(CurrentWeatherResponse response) {
        this.dateTime = response.getDateTime();
        this.temp = response.getMainResponse().getCurrentTemp();
        this.feelsLikeTemp = response.getMainResponse().getFeelsLikeTemp();
        this.description = response.getWeather().get(0).getDescription();
        this.icon = response.getWeather().get(0).getIcon();
    }

    public CurrentWeather() {
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public double getFeelsLikeTemp() {
        return feelsLikeTemp;
    }

    public void setFeelsLikeTemp(double feelsLikeTemp) {
        this.feelsLikeTemp = feelsLikeTemp;
    }

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
