package io.hoogland.weer2track.service;

import io.hoogland.weer2track.model.response.CurrentWeatherResponse;
import io.hoogland.weer2track.model.response.ForecastResponse;
import io.hoogland.weer2track.util.Constants;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Interface used to hold possible API calls to OpenWeatherMap.
 *
 * @author dan
 * @see <a href="https://openweathermap.org/forecast5">OpenWeatherMap 5 day weather forecast</a>
 * @see <a href="https://openweathermap.org/current">OpenWeatherMap current weather data</a>
 */
public interface WeatherService {

    /**
     * Creates a Retrofit {@link Call} to get the current weather data from OpenWeatherMap.
     *
     * @param lat      Latitude used in the API call
     * @param lon      Longitude used in the API call
     * @param apiKey   API key used to access the OpenWeatherMap API
     * @param unit     Unit the temperature will be displayed in
     * @param language Language the dates will be displayed in
     * @return A Retrofit {@link Call} with a {@link CurrentWeatherResponse} model containing the retrieved data
     * @see <a href="https://openweathermap.org/forecast5">OpenWeatherMap 5 day weather forecast</a>
     */
    @GET(Constants.OPENWEATHER_API_CURRENT)
    Call<CurrentWeatherResponse> getCurrentWeather(@Query(Constants.OPENWEATHER_API_LAT) double lat,
                                                   @Query(Constants.OPENWEATHER_API_LON) double lon,
                                                   @Query(Constants.OPENWEATHER_API_KEY_PARAM) String apiKey,
                                                   @Query(Constants.OPENWEATHER_API_UNITS) String unit,
                                                   @Query(Constants.OPENWEATHER_API_LANGUAGE) String language);

    /**
     * Creates a Retrofit {@link Call} to get 5 day forecast data from OpenWeatherMap.
     *
     * @param lat      Latitude used in the API call
     * @param lon      Longitude used in the API call
     * @param apiKey   API key used to access the OpenWeatherMap API
     * @param unit     Unit the temperature will be displayed in
     * @param language Language the dates will be displayed in
     * @return A Retrofit {@link Call} with a {@link ForecastResponse} model containing the retrieved data
     * @see <a href="https://openweathermap.org/forecast5">OpenWeatherMap 5 day weather forecast</a>
     */
    @GET(Constants.OPENWEATHER_API_FORECAST)
    Call<ForecastResponse> getWeatherForecast(@Query(Constants.OPENWEATHER_API_LAT) double lat,
                                              @Query(Constants.OPENWEATHER_API_LON) double lon,
                                              @Query(Constants.OPENWEATHER_API_KEY_PARAM) String apiKey,
                                              @Query(Constants.OPENWEATHER_API_UNITS) String unit,
                                              @Query(Constants.OPENWEATHER_API_LANGUAGE) String language);

}
