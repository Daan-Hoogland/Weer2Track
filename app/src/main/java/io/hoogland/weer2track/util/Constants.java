package io.hoogland.weer2track.util;

import java.text.DecimalFormat;

/**
 * Class containing constants used in Java code.
 *
 * @author dan
 */
public class Constants {
    /**
     * Format to display time.
     */
    public static final String DATE_FORMAT_TIME = "HH:mm";

    /**
     * Format to display time and the full date.
     */
    public static final String DATE_FORMAT_FULL = "HH:mm EEEE dd-MM-yyyy";

    /**
     * Format to display the full date.
     */
    public static final String DATE_FORMAT = "EEEE dd-MM-yyyy";

    /**
     * Temperature unit used to display temperature.
     */
    public static final String TEMP_UNIT = "°C";

    /**
     * Base URL used to retrieve an icon for a given weather condition ID as String from OpenWeatherMap.
     */
    public static final String OPENWEATHER_ICON_URL = "https://openweathermap.org/img/wn/%s@2x.png";

    /**
     * Base URL used to access the OpenWeatherMap API.
     */
    public static final String OPENWEATHER_API_BASE_URL = "https://api.openweathermap.org/data/2.5/";

    /**
     * URL subdirectory for the forecast API.
     */
    public static final String OPENWEATHER_API_FORECAST = "forecast";

    /**
     * URL subdirectory for the current weather API.
     */
    public static final String OPENWEATHER_API_CURRENT = "weather";

    /**
     * OpenWeatherMap API parameter for latitude.
     */
    public static final String OPENWEATHER_API_LAT = "lat";

    /**
     * OpenWeatherMap API parameter for longitude.
     */
    public static final String OPENWEATHER_API_LON = "lon";

    /**
     * OpenWeatherMap API parameter for the API key.
     */
    public static final String OPENWEATHER_API_KEY_PARAM = "apiKey";

    /**
     * OpenWeatherMap API parameter for the temperature unit used in the request.
     */
    public static final String OPENWEATHER_API_UNITS = "units";

    /**
     * OpenWeatherMap API parameter for the language used in the request.
     */
    public static final String OPENWEATHER_API_LANGUAGE = "lang";

    /**
     * Decimal format used to display min and max temp values.
     */
    public static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.##");

    /**
     * Hardcoded values used in the OpenWeatherMap API calls.
     * TODO: move to settings if enough time
     */
    public static final String OPENWEATHER_API_UNITS_VALUE = "metric";
    public static final String OPENWEATHER_API_LANGUAGE_VALUE = "nl";
    public static final double OPENWEATHER_API_LAT_VALUE = 52.128479;
    public static final double OPENWEATHER_API_LON_VALUE = 5.566180;


}
