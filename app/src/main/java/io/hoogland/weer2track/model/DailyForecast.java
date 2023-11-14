package io.hoogland.weer2track.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.hoogland.weer2track.model.response.Forecast;

/**
 * Model used to display and save daily weather forecast data.
 */
@Entity(tableName = "daily_forecast")
public class DailyForecast {

    @PrimaryKey(autoGenerate = true)
    private int uid;
    private LocalDate date;
    private Double minTemp;
    private Double maxTemp;
    private double precipitationChance;
    private String description;
    private String icon;
    @ColumnInfo(name = "modified_at")
    private LocalDateTime modifiedAt;

    public DailyForecast() {
    }

    /**
     * Constructor to create a model based on a {@link Forecast} received through the
     * OpenWeatherMap API.
     *
     * @param forecast {@link Forecast} received from an OpenWeatherMap API call
     * @see <a href="https://openweathermap.org/forecast5">OpenWeatherMap 5 day weather forecast</a>
     */
    public DailyForecast(Forecast forecast) {
        this.minTemp = forecast.getMainResponse().getMinTemp();
        this.maxTemp = forecast.getMainResponse().getMaxTemp();
        this.precipitationChance = forecast.getPercentOfPrecipitation();
        this.description = forecast.getWeather().get(0).getDescription();
        this.icon = forecast.getWeather().get(0).getIcon();
        this.date = forecast.getDateTime();
    }

    /**
     * Constructor to create a model based on a list of {@link DailyForecast}. This method returns
     * a single {@link DailyForecast} based on the averages, minimum and maximum values for
     * temperature and chance of precipitation.
     *
     * @param forecastList List of {@link DailyForecast} to be turned into a single forecast model
     */
    public DailyForecast(List<DailyForecast> forecastList) {
        // Create variables to hold data from all DailyForecast's
        Map<String, Integer> descriptionCount = new HashMap<>();
        Map<String, Integer> iconCount = new HashMap<>();
        double precipitationSum = 0L;

        for (DailyForecast dailyForecast : forecastList) {
            // Check if temp is lower than minTemp
            this.minTemp = this.minTemp == null ? dailyForecast.getMinTemp() :
                    this.minTemp.compareTo(dailyForecast.getMinTemp()) > 0 ? dailyForecast.getMinTemp() : this.minTemp;

            // Check if temp is higher than maxTemp
            this.maxTemp = this.maxTemp == null ? dailyForecast.getMaxTemp() :
                    this.maxTemp.compareTo(dailyForecast.getMaxTemp()) < 0 ? dailyForecast.getMaxTemp() : this.maxTemp;

            precipitationSum += dailyForecast.getPrecipitationChance();
            // Count description and icon occurrences to get the one with the most occurrences.
            descriptionCount.merge(dailyForecast.getDescription(), 1, Integer::sum);
            iconCount.merge(dailyForecast.getIcon(), 1, Integer::sum);
        }
        // Get averages/most occurring
        this.description = Collections.max(descriptionCount.entrySet(), Map.Entry.comparingByValue()).getKey();
        this.icon = Collections.max(iconCount.entrySet(), Map.Entry.comparingByValue()).getKey();
        this.precipitationChance = precipitationSum / forecastList.size();
        // Doesn't matter which value we're getting since they're already grouped by date.
        this.date = forecastList.get(0).getDate();
    }


    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Double getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(Double minTemp) {
        this.minTemp = minTemp;
    }

    public Double getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(Double maxTemp) {
        this.maxTemp = maxTemp;
    }

    public double getPrecipitationChance() {
        return precipitationChance;
    }

    public void setPrecipitationChance(double precipitationChance) {
        this.precipitationChance = precipitationChance;
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

    public LocalDateTime getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(LocalDateTime modifiedAt) {
        this.modifiedAt = modifiedAt;
    }
}
