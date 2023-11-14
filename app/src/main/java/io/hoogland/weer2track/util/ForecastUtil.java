package io.hoogland.weer2track.util;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.hoogland.weer2track.model.DailyForecast;
import io.hoogland.weer2track.model.response.Forecast;
import io.hoogland.weer2track.model.response.ForecastResponse;

/**
 * Util class for common tasks executed on {@link DailyForecast} objects.
 *
 * @author dan
 */
public class ForecastUtil {

    /**
     * Converts a {@link ForecastResponse} to a {@link List} of {@link DailyForecast} grouped by day with
     * average/min/max values based on the field.
     *
     * @param response {@link ForecastResponse} received from an OpenWeatherMap API call
     * @return {@link List} of {@link DailyForecast} grouped by day with average/min/max values
     * based on the field.
     */
    public static List<DailyForecast> forecastResponseToDaily(ForecastResponse response) {
        Map<LocalDate, List<DailyForecast>> resultMap = new HashMap<>();

        // Sort the results by day
        for (Forecast forecast : response.getForecasts()) {
            DailyForecast dailyForecast = new DailyForecast(forecast);
            LocalDate date = dailyForecast.getDate();

            // Check if it's a weekday before continuing.
            if (!DateUtils.isWeekend(date.getDayOfWeek())) {
                List<DailyForecast> dailyForecastList;

                /* Check if list already exists for date key, if it does append to
                the existing values, if not create a new list. */
                if (resultMap.containsKey(date)) {
                    dailyForecastList = resultMap.get(date);
                } else {
                    dailyForecastList = new ArrayList<>();
                }
                dailyForecastList.add(dailyForecast);
                resultMap.put(dailyForecast.getDate(), dailyForecastList);
            }
        }

        // Get average of each day and return the list of results.
        List<DailyForecast> resultList = new ArrayList<>();
        resultMap.forEach((date, forecastList) -> {
            resultList.add(new DailyForecast(forecastList));
        });
        return resultList;
    }
}
