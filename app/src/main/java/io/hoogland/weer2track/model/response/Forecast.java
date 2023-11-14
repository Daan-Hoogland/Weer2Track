package io.hoogland.weer2track.model.response;

import com.google.gson.annotations.SerializedName;

import java.time.LocalDate;
import java.util.List;

/**
 * Model used to serialize the response data from an OpenWeatherMap API call to their weather
 * forecast API.
 *
 * @author dan
 * @see <a href="https://openweathermap.org/forecast5">OpenWeatherMap 5 day weather forecast</a>
 */
public class Forecast {

    @SerializedName("main")
    private MainResponse mainResponse;

    @SerializedName("weather")
    private List<Weather> weather;

    @SerializedName("dt")
    private LocalDate dateTime;

    @SerializedName("dt_txt")
    private String dtText;

    @SerializedName("pop")
    private double percentOfPrecipitation;

    public MainResponse getMainResponse() {
        return mainResponse;
    }

    public void setMainResponse(MainResponse mainResponse) {
        this.mainResponse = mainResponse;
    }

    public List<Weather> getWeather() {
        return weather;
    }

    public void setWeather(List<Weather> weather) {
        this.weather = weather;
    }

    public LocalDate getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDate dateTime) {
        this.dateTime = dateTime;
    }

    public String getDtText() {
        return dtText;
    }

    public void setDtText(String dtText) {
        this.dtText = dtText;
    }

    public double getPercentOfPrecipitation() {
        return percentOfPrecipitation;
    }

    public void setPercentOfPrecipitation(double percentOfPrecipitation) {
        this.percentOfPrecipitation = percentOfPrecipitation;
    }

    @Override
    public String toString() {
        return "Forecast{" +
                "dateTime=" + dateTime +
                ", dtText='" + dtText + '\'' +
                '}';
    }
}
