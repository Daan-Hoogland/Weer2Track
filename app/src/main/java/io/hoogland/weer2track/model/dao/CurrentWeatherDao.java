package io.hoogland.weer2track.model.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import io.hoogland.weer2track.model.CurrentWeather;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

/**
 * DAO to access {@link CurrentWeather} database table.
 *
 * @author dan
 * @see CurrentWeather
 */
@Dao
public interface CurrentWeatherDao {

    /**
     * Retrieve {@link CurrentWeather} from application database. There will ever only be 1 entry
     * of {@link CurrentWeather} available as the primary key is set by default.
     *
     * @return {@link CurrentWeather} model with values from application database
     */
    @Query("SELECT * FROM current_weather")
    Single<CurrentWeather> getCurrentWeather();

    /**
     * Inserts the given object {@link CurrentWeather} into the database, replacing existing rows
     * it is it conflict with.
     *
     * @param weather Model with values to be inserted into the application database
     * @return {@link Completable} to be run off main thread using RxAndroid and execute different
     * code based on error/success
     * @see <a href="https://github.com/ReactiveX/RxAndroid">RxAndroid</a>
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(CurrentWeather weather);

    /**
     * Updates an object that is already present in the application database, replacing existing
     * rows it is in conflict with.
     *
     * @param weather Model with values to be updated into the application database
     * @return {@link Completable} to be run off main thread using RxAndroid and execute different
     * code based on error/success
     * @see <a href="https://github.com/ReactiveX/RxAndroid">RxAndroid</a>
     */
    @Update(onConflict = OnConflictStrategy.REPLACE)
    Completable update(CurrentWeather weather);
}
