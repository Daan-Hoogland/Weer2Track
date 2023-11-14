package io.hoogland.weer2track.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;

import java.util.List;

import io.hoogland.weer2track.BuildConfig;
import io.hoogland.weer2track.db.AppDatabase;
import io.hoogland.weer2track.model.CurrentWeather;
import io.hoogland.weer2track.model.DailyForecast;
import io.hoogland.weer2track.model.dao.CurrentWeatherDao;
import io.hoogland.weer2track.model.dao.DailyForecastDao;
import io.hoogland.weer2track.model.response.CurrentWeatherResponse;
import io.hoogland.weer2track.model.response.ForecastResponse;
import io.hoogland.weer2track.service.WeatherService;
import io.hoogland.weer2track.ui.currentweather.CurrentWeatherFragment;
import io.hoogland.weer2track.ui.forecast.ForecastFragment;
import io.hoogland.weer2track.util.Constants;
import io.hoogland.weer2track.util.ForecastUtil;
import io.hoogland.weer2track.util.NetworkUtil;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Activity class for the splash screen, responsible for loading data on application startup.
 *
 * @author dan
 */
public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "SplashActivity";

    /**
     * Method called when the {@link AppCompatActivity} is loaded. Responsible for executing OpenWeatherMap
     * API calls and saving the results in the application database for display.
     *
     * @param savedInstanceState Previously saved state, if applicable (e.g. orientation change)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO: if time implement Android 12 splashscreen properly
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        splashScreen.setKeepOnScreenCondition(() -> true);
        super.onCreate(savedInstanceState);

        // Open application database connection & prepare DAO's
        AppDatabase appDatabase = AppDatabase.getInstance(getApplicationContext());
        DailyForecastDao dailyForecastDao = appDatabase.dailyForecastDao();
        CurrentWeatherDao currentWeatherDao = appDatabase.currentWeatherDao();

        // Prepare network calls for current weather
        Call<CurrentWeatherResponse> wResponse = NetworkUtil.getWeatherServiceInstance()
                .getCurrentWeather(Constants.OPENWEATHER_API_LAT_VALUE, Constants.OPENWEATHER_API_LON_VALUE,
                        BuildConfig.WEATHER_API_KEY, Constants.OPENWEATHER_API_UNITS_VALUE, Constants.OPENWEATHER_API_LANGUAGE_VALUE);

        Log.d(TAG, "wResponse: starting wResponse");
        wResponse.enqueue(new Callback<CurrentWeatherResponse>() {

            /**
             * When the response is successful, convert the {@link CurrentWeatherResponse} into a
             * {@link CurrentWeather} object and insert it into the application database.
             *
             * @param call The original call created using {@link WeatherService}
             * @param response Response object containing data from the API call serialized as a
             * {@link CurrentWeatherResponse}
             */
            @Override
            public void onResponse(Call<CurrentWeatherResponse> call, Response<CurrentWeatherResponse> response) {
                // process data to Room objects
                CurrentWeather currentWeather = new CurrentWeather(response.body());
                Log.d(TAG, "wResponse onResponse: retrieved current weather response body");
                // Save data from response in Room
                currentWeatherDao.insert(currentWeather).subscribeOn(Schedulers.io())
                        .subscribe(new CompletableObserver() {
                            @Override
                            public void onSubscribe(@NonNull Disposable d) {
                            }

                            @Override
                            public void onComplete() {
                                Log.d(TAG, "wResponse onComplete: inserted current weather into database successfully");
                            }

                            @Override
                            public void onError(@NonNull Throwable e) {
                                Log.e(TAG, "onError: ", e);
                            }
                        });
            }

            @Override
            public void onFailure(Call<CurrentWeatherResponse> call, Throwable t) {
                call.cancel();
            }
        });

        // Prepare network calls for forecast
        Call<ForecastResponse> fResponse = NetworkUtil.getWeatherServiceInstance()
                .getWeatherForecast(Constants.OPENWEATHER_API_LAT_VALUE, Constants.OPENWEATHER_API_LON_VALUE,
                        BuildConfig.WEATHER_API_KEY, Constants.OPENWEATHER_API_UNITS_VALUE, Constants.OPENWEATHER_API_LANGUAGE_VALUE);

        // Execute forecast network call
        Log.d(TAG, "fResponse: starting fResponse");
        fResponse.enqueue(new Callback<ForecastResponse>() {

            /**
             * When the response is successful, delete old table entries, convert the
             * {@link ForecastResponse} into a {@link DailyForecast} object and insert it into the
             * application database.
             *
             * @param call The original call created using {@link WeatherService}
             * @param response Response object containing data from the API call serialized as a
             * {@link ForecastResponse}
             */
            @Override
            public void onResponse(Call<ForecastResponse> call, Response<ForecastResponse> response) {
                if (response.body() != null) {
                    Log.d(TAG, "fResponse onResponse: retrieved forecast response body");
                    // Delete existing rows if they exist.
                    dailyForecastDao.deleteAll().subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
                        @Override
                        public void onSubscribe(@NonNull Disposable d) {
                        }

                        @Override
                        public void onComplete() {
                            Log.d(TAG, "fResponse onComplete: deleted all rows in database");
                            List<DailyForecast> forecastList = ForecastUtil.forecastResponseToDaily(response.body());
                            dailyForecastDao.insertWithTimestamp(forecastList).subscribeOn(Schedulers.io())
                                    .subscribe(new CompletableObserver() {
                                        @Override
                                        public void onSubscribe(@NonNull Disposable d) {
                                        }

                                        @Override
                                        public void onComplete() {
                                            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                                            startActivity(intent);
                                            finish();
                                            Log.d(TAG, "fResponse onComplete: inserted forecast into database successfully");
                                        }

                                        @Override
                                        public void onError(@NonNull Throwable e) {
                                            Log.e(TAG, "onError: ", e);
                                        }
                                    });
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {
                        }
                    });
                }
            }

            /**
             * Called when the network call fails. Sets the variable that {@link CurrentWeatherFragment}
             * and {@link ForecastFragment} use in order to show the no internet connection text.
             *
             * @param call The original call created using {@link WeatherService}
             * @param t Information as to why the call failed
             */
            @Override
            public void onFailure(Call<ForecastResponse> call, Throwable t) {
                Log.e(TAG, "onFailure: error in network request", t);
                Toast.makeText(SplashActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                MainActivity.isOldData = true;
                startActivity(intent);
            }
        });
    }
}

