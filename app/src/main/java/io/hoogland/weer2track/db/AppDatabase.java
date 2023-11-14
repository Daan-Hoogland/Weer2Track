package io.hoogland.weer2track.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import io.hoogland.weer2track.model.CurrentWeather;
import io.hoogland.weer2track.model.DailyForecast;
import io.hoogland.weer2track.model.converter.DateConverter;
import io.hoogland.weer2track.model.dao.CurrentWeatherDao;
import io.hoogland.weer2track.model.dao.DailyForecastDao;

/**
 * Class used to create, access and manage the database connection used in the app.
 *
 * @author dan
 */
@Database(entities = {CurrentWeather.class, DailyForecast.class}, version = 2)
@TypeConverters({DateConverter.class})
public abstract class AppDatabase extends RoomDatabase {

    /**
     * Singleton instance of {@link AppDatabase}
     */
    private static AppDatabase instance;

    /**
     * Name of the database
     */
    private static final String NAME = "weer2track";

    /**
     * Creates a single instance of AppDatabase for the entire app.
     *
     * @param context Application context
     * @return Singleton of AppDatabase
     */
    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, NAME)
                    .fallbackToDestructiveMigration().build();
        }
        return instance;
    }

    /**
     * Supplies the DAO for {@link CurrentWeather} model.
     *
     * @return Usable {@link CurrentWeatherDao} DAO
     */
    public abstract CurrentWeatherDao currentWeatherDao();

    /**
     * Supplies the DAO for {@link DailyForecast} model.
     *
     * @return Usable {@link DailyForecastDao} DAO
     */
    public abstract DailyForecastDao dailyForecastDao();
}
