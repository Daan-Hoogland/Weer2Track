package io.hoogland.weer2track.model.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.time.LocalDateTime;
import java.util.List;

import io.hoogland.weer2track.model.DailyForecast;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

/**
 * DAO to access {@link DailyForecast} database table.
 *
 * @author dan
 */
@Dao
public abstract class DailyForecastDao {

    /**
     * Retrieve all {@link DailyForecast} from the application database.
     *
     * @return {@link List} containing {@link DailyForecast} models with values from application database
     */
    @Query("SELECT * FROM daily_forecast")
    public abstract Single<List<DailyForecast>> getAll();

    /**
     * Deletes all rows in the {@link DailyForecast} table.
     *
     * @return {@link Completable} to be run off main thread using RxAndroid and execute different
     * code based on error/success
     * @see <a href="https://github.com/ReactiveX/RxAndroid">RxAndroid</a>
     */
    @Query("DELETE FROM daily_forecast")
    public abstract Completable deleteAll();

    /**
     * Inserts all {@link DailyForecast} objects present in the given list into the database, replacing existing rows
     * it is it conflict with.
     *
     * @param dailyForecasts {@link List} of {@link DailyForecast} to be inserted into the application database
     * @return {@link Completable} to be run off main thread using RxAndroid and execute different
     * code based on error/success
     * @see <a href="https://github.com/ReactiveX/RxAndroid">RxAndroid</a>
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract Completable insertAll(List<DailyForecast> dailyForecasts);

    /**
     * Inserts all {@link DailyForecast} objects present in the given list into the database, replacing existing rows
     * it is it conflict with, and applies a timestamp to each object indicating when they last have been modified.
     *
     * @param dailyForecasts {@link List} of {@link DailyForecast} to be inserted into the application database
     * @return {@link Completable} to be run off main thread using RxAndroid and execute different
     * code based on error/success
     * @see <a href="https://github.com/ReactiveX/RxAndroid">RxAndroid</a>
     */
    public Completable insertWithTimestamp(List<DailyForecast> dailyForecasts) {
        LocalDateTime current = LocalDateTime.now();
        dailyForecasts.forEach(dailyForecast -> {
            dailyForecast.setModifiedAt(current);
        });
        return insertAll(dailyForecasts);
    }
}
