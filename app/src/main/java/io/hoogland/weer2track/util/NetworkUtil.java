package io.hoogland.weer2track.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

import io.hoogland.weer2track.service.WeatherService;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Util class for common network tasks
 *
 * @author dan
 */
public class NetworkUtil {

    private static WeatherService weatherService;

    /**
     * Creates and/or returns a WeatherService singleton with adapters to convert {@link Long}
     * to {@link LocalDate}, {@link LocalDateTime} and the other way around.
     *
     * @return WeatherService usable for OpenWeatherMap API calls
     */
    public static synchronized WeatherService getWeatherServiceInstance() {
        if (weatherService == null) {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDateTime.class, (JsonDeserializer<LocalDateTime>) (json, typeOfT, context) -> LocalDateTime.ofInstant(Instant.ofEpochSecond(json.getAsJsonPrimitive().getAsLong()), ZoneId.systemDefault()))
                    .registerTypeAdapter(LocalDate.class, (JsonDeserializer<LocalDate>) (json, typeOfT, context) -> Instant.ofEpochSecond(json.getAsJsonPrimitive().getAsLong()).atZone(ZoneId.systemDefault()).toLocalDate())
                    .create();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.OPENWEATHER_API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
            weatherService = retrofit.create(WeatherService.class);
        }
        return weatherService;
    }
}
